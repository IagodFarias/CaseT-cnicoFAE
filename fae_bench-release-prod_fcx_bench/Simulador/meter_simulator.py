#!/usr/bin/env python3
# -*- coding: ascii -*-
"""
Simulador dos 20 medidores ultrassonicos -- canal A.2 do contrato.

Um servidor TCP por medidor, em 127.0.0.1:2051..2070, casando com as linhas de
conversmodel do seed (CONVERSOR1..CONVERSOR20).

DOIS FORMATOS NO MESMO SOCKET
-----------------------------
Formato 1 -- pacote de medicao (stream continuo apos 0x21):
    8 x 0xFF (preambulo) + 78 bytes little-endian = 86 bytes
    off  0  short   agStatus
    off  2  short   r1
    off  4  short   gm1
    off  6  double  hfc
    off 14  double  ttstd    (tempo de transito a favor, s)
    off 22  double  ttrev    (tempo de transito reverso, s)
    off 30  double  vel      (velocidade nao calibrada, m/s)
    off 38  double  velRe    (velocidade calibrada, m/s)
    off 46  double  volFlowRe(vazao volumetrica, m3/h)
    off 54  double  volRe    (volume, m3)
    off 62  double  accReVolume
    off 70  double  accReVolumeRev
    Sem checksum. Escrito com UM unico sendall() -- nunca parcial.

Formato 2 -- resposta a comando:
    ufoId (4 bytes) + byte de comando + ACK(0xF1)/NACK(0xF2) + payload opcional

PROTOCOLO DE ESCRITA -- DOIS ACKs
---------------------------------
Para comandos COM payload (0x03, 0x05, 0xAA, 0xCC, 0xD0, 0xD3), com firmware
>= a versao minima, a sequencia em MeterSocketThread.run() e:

    app  -> byte de comando (enviado DUAS vezes, ~20 ms entre as copias)
    app  <- [ufoId][cmd][ACK]            <-- 1o ACK
    app  -> payload
    app  <- [ufoId][cmd][ACK]            <-- 2o ACK (via configureAckOrNackCheck)

Comandos SEM payload (0x10-0x15, 0x21, 0x22, 0x23) recebem apenas UM ACK.

MODELO FISICO
-------------
Invertido a partir de CalibrationService, com c = sosWater(T):

    ttstd = L/(c+v) + tau + delta
    ttrev = L/(c-v) + tau - delta

A app entao calcula, na vazao zero (v=0):
    dzc  = (ttrev - ttstd)/2            = -delta
    dsos = (ttstd + ttrev)/2 - L/sos(T) =  tau
e em ttToVel:
    (L/2)*(1/(ttstd-dsos+dzc) - 1/(ttrev-dsos-dzc)) = (L/2)*((c+v)/L - (c-v)/L) = v

ou seja, a app recupera a velocidade exata. Isso e o que faz K sair ~1,0 e o
erro de verificacao sair ~0%.

Uso:
    python meter_simulator.py
    python meter_simulator.py --selftest
    python meter_simulator.py --flow 2500        # vazao fixa, sem a BCI
"""

import argparse
import json
import logging
import math
import os
import random
import socket
import socketserver
import struct
import sys
import threading
import time
from datetime import datetime

LOG = logging.getLogger("meter")

# ---- comandos (enumerations/MeterCmdEnum.java) ------------------------------
CMD_ACK = 0xF1
CMD_NACK = 0xF2
CMD_READ_FIRMWARE_VERSION = 0x01
CMD_READ_SERIAL_NUMBER = 0x02
CMD_WRITE_SERIAL_NUMBER = 0x03
CMD_READ_REPLACEMENT_DATE = 0x04
CMD_WRITE_REPLACEMENT_DATE = 0x05
CMD_LOAD_PRE_CALIB_OPMODE = 0x10
CMD_LOAD_CALIB_OPMODE = 0x11
CMD_LOAD_POST_CALIB_OPMODE = 0x12
CMD_LOAD_STAND_BY_OPMODE = 0x13
CMD_LOAD_NORMAL_OPMODE = 0x14
CMD_LOAD_VERIF_OPMODE = 0x15
CMD_ENABLE_DATA_TRANSMIT = 0x21
CMD_DISABLE_DATA_TRANSMIT = 0x22
CMD_TRIM_AG_STAGE_2 = 0x23
CMD_LOAD_CALIB_PARAMETERS = 0xAA
CMD_READ_CONFIG_PARAMETERS = 0xBB
CMD_WRITE_CONFIG_PARAMETERS = 0xCC
CMD_UPDATE_DATE = 0xD0
CMD_READ_DATE = 0xD1
CMD_WRITE_WMBUS_CONFIG = 0xD3
CMD_READ_WMBUS_CONFIG = 0xD4

CMD_NAME = {
    0x01: "READ_FIRMWARE_VERSION", 0x02: "READ_SERIAL_NUMBER",
    0x03: "WRITE_SERIAL_NUMBER", 0x04: "READ_REPLACEMENT_DATE",
    0x05: "WRITE_REPLACEMENT_DATE", 0x10: "LOAD_PRE_CALIB_OPMODE",
    0x11: "LOAD_CALIB_OPMODE", 0x12: "LOAD_POST_CALIB_OPMODE",
    0x13: "LOAD_STAND_BY_OPMODE", 0x14: "LOAD_NORMAL_OPMODE",
    0x15: "LOAD_VERIF_OPMODE", 0x21: "ENABLE_DATA_TRANSMIT",
    0x22: "DISABLE_DATA_TRANSMIT", 0x23: "TRIM_AG_STAGE_2",
    0xAA: "LOAD_CALIB_PARAMETERS", 0xBB: "READ_CONFIG_PARAMETERS",
    0xCC: "WRITE_CONFIG_PARAMETERS", 0xD0: "UPDATE_DATE",
    0xD1: "READ_DATE", 0xD3: "WRITE_WMBUS_CONFIG", 0xD4: "READ_WMBUS_CONFIG",
}

# Comandos sem payload: recebem UM unico ACK.
NO_PAYLOAD = {0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x21, 0x22, 0x23}

# Comandos de escrita: payload de tamanho fixo -> ACK, payload, ACK.
# 0xAA e variavel (42 + 16*N) e tem tratamento proprio.
WRITE_PAYLOAD_SIZE = {0x03: 12, 0x05: 8, 0xD0: 6, 0xD3: 34, 0xCC: 654}

# Comandos de leitura: ACK seguido de N bytes de resposta.
READ_PAYLOAD_SIZE = {0x02: 12, 0x04: 8, 0xBB: 654, 0xD1: 6, 0xD4: 34}

CONFIG_BLOCK_SIZE = 654  # MeterConfigV108Enum.getConfigSize()

PREAMBLE = b"\xFF" * 8
PAYLOAD_SIZE = 78
PACKET_SIZE = len(PREAMBLE) + PAYLOAD_SIZE  # 86


def sos_water(t):
    """Velocidade do som na agua. Copia exata de CalibrationService.sosWater()."""
    return (3.16081885e-9 * t ** 5
            - 1.4815004e-6 * t ** 4
            + 3.34558776e-4 * t ** 3
            - 5.8114229e-2 * t ** 2
            + 5.03835027 * t
            + 1.40238744e3)


def visc_water(t):
    """Viscosidade cinematica. Copia de CalibrationService.viscWater()."""
    return (-0.3758806791e-24 * t ** 11 + 0.1971168646e-21 * t ** 10
            - 0.4475100250e-19 * t ** 9 + 0.5762544630e-17 * t ** 8
            - 0.4630803192e-15 * t ** 7 + 0.2406947983e-13 * t ** 6
            - 0.8120756094e-12 * t ** 5 + 0.1751404064e-10 * t ** 4
            - 0.2441445733e-9 * t ** 3 + 0.2974093901e-8 * t ** 2
            - 0.6511697759e-7 * t + 0.1792e-5)


class FaultInjector(object):
    ACTIONS = ("nack", "silence", "corrupt_stream", "nan_stream", "delay")

    def __init__(self, cfg):
        self.enabled = bool(cfg.get("enabled", False))
        self.rules = [r for r in cfg.get("rules", []) if r.get("enabled", False)]
        self.counts = {}
        self.lock = threading.Lock()
        if self.enabled and self.rules:
            for r in self.rules:
                LOG.warning("INJECAO DE FALHA ATIVA: medidor=%s command=%s action=%s",
                            r.get("meter"), r.get("command"), r.get("action"))

    def _hit(self, rule, key):
        with self.lock:
            n = self.counts.get(key, 0) + 1
            self.counts[key] = n
        occ = rule.get("occurrences")
        every = rule.get("every_n")
        if occ:
            return n in occ
        if every:
            return n % int(every) == 0
        return True

    def for_command(self, meter_num, cmd):
        if not self.enabled:
            return None, None
        for r in self.rules:
            if r.get("meter") != meter_num:
                continue
            rc = r.get("command")
            if rc is None:
                continue
            alvo = int(str(rc), 16) if str(rc).lower().startswith("0x") else int(rc)
            if alvo != cmd:
                continue
            if self._hit(r, "cmd:%s:%s" % (meter_num, cmd)):
                return r.get("action"), r
        return None, None

    def for_stream(self, meter_num):
        if not self.enabled:
            return None, None
        for r in self.rules:
            if r.get("meter") != meter_num:
                continue
            if r.get("action") not in ("corrupt_stream", "nan_stream"):
                continue
            if self._hit(r, "stream:%s:%s" % (meter_num, r.get("action"))):
                return r.get("action"), r
        return None, None


class Meter(object):
    """Estado e fisica de um medidor."""

    def __init__(self, num, cfg, profile, flow_source, faults):
        self.num = num
        self.tag = "MEDIDOR-%02d" % num
        self.cfg = cfg
        self.faults = faults
        self.flow_source = flow_source

        ph = cfg["physics"]
        self.L = float(ph["us_path_length_m"])
        self.d = float(ph["reduction_diameter_m"])
        self.area = math.pi * (self.d / 2.0) ** 2
        self.tau = float(ph["tau_s"])
        self.delta = float(ph["delta_s"])
        self.tt_noise = float(ph["tt_noise_s"])
        self.temp = float(ph["temperature_c"])
        self.agc = int(ph["agc_status"])
        self.r1 = int(ph["r1_gain"])
        self.gm1 = int(ph["gm1_gain"])
        self.hfc = float(ph["hfc"])

        self.bias_calib = float(profile.get("bias_calib", 0.0))
        self.bias_verif = float(profile.get("bias_verif", 0.0))

        # ufoId distinto por medidor. A app APRENDE este valor na primeira
        # leitura (MeterSocketComm.readID) -- quem escolhe e o simulador.
        self.ufo_id = bytes([0xA0, 0x00, (num >> 8) & 0xFF, num & 0xFF])

        self.fw = bytes(cfg["firmware"]["version_bytes"])
        self.echo_writes = bool(cfg["firmware"].get("echo_writes", False))

        self.serial = b"\x00" * 12
        self.replacement_date = b"\x00" * 8
        self.date = b"\x00" * 6
        self.wmbus = b"\x00" * 34
        self.config_block = bytes((i * 7) & 0xFF for i in range(CONFIG_BLOCK_SIZE))

        self.mode = "PRE_CALIB"
        self.transmitting = False
        self.packets_sent = 0
        self.commands_handled = 0

        # Tabela de calibracao recebida via 0xAA.
        self.calib_reynolds = []
        self.calib_k = []
        self.calib_loaded = False

        self.vol_total = 0.0
        self.vol_rev = 0.0
        self._last_t = time.time()
        self.lock = threading.RLock()

    # -- fisica ---------------------------------------------------------------
    def current_flow_lph(self):
        try:
            return max(0.0, float(self.flow_source()))
        except Exception:
            return 0.0

    def k_for_reynolds(self, re):
        """Interpola K na tabela recebida via 0xAA. Fora da faixa, extrapola
        pelo extremo. Sem tabela, K = 1."""
        if not self.calib_loaded or not self.calib_reynolds:
            return 1.0
        pares = sorted(zip(self.calib_reynolds, self.calib_k))
        xs = [p[0] for p in pares]
        ys = [p[1] for p in pares]
        if re <= xs[0]:
            return ys[0]
        if re >= xs[-1]:
            return ys[-1]
        for i in range(1, len(xs)):
            if re <= xs[i]:
                x0, x1 = xs[i - 1], xs[i]
                y0, y1 = ys[i - 1], ys[i]
                if x1 == x0:
                    return y1
                return y0 + (y1 - y0) * (re - x0) / (x1 - x0)
        return ys[-1]

    def build_payload(self):
        """78 bytes little-endian do Formato 1."""
        with self.lock:
            now = time.time()
            dt = max(0.0, now - self._last_t)
            self._last_t = now

            q_lph = self.current_flow_lph()

            # Velocidade verdadeira no ponto de reducao (mesma conta de
            # CalibrationService.refFlowRate2Vel).
            v_true = (q_lph / 1000.0) / self.area / 3600.0

            # O medidor "erra" por bias_calib: e isso que a calibracao corrige.
            v_meter = v_true * (1.0 + self.bias_calib)

            c = sos_water(self.temp)
            # Guarda-corpo: v nunca pode chegar perto de c.
            v_eff = max(-c * 0.5, min(c * 0.5, v_meter))

            n1 = random.gauss(0.0, self.tt_noise)
            n2 = random.gauss(0.0, self.tt_noise)
            ttstd = self.L / (c + v_eff) + self.tau + self.delta + n1
            ttrev = self.L / (c - v_eff) + self.tau - self.delta + n2

            # Velocidade nao calibrada, como a app recalcularia dos tempos.
            vel = (self.L / 2.0) * (1.0 / (ttstd - self.tau + (-self.delta))
                                    - 1.0 / (ttrev - self.tau - (-self.delta)))

            # Velocidade calibrada: aplica a tabela K recebida via 0xAA e o
            # residuo bias_verif (usado para reprovar de proposito).
            re = (abs(vel) * self.d / visc_water(self.temp)) if vel else 0.0
            k = self.k_for_reynolds(re)
            vel_re = vel * k * (1.0 + self.bias_verif)

            vol_flow_re = vel_re * self.area * 3600.0  # m3/h
            self.vol_total += max(0.0, vol_flow_re) * (dt / 3600.0)

            action, _rule = self.faults.for_stream(self.num)
            if action == "nan_stream":
                ttstd = ttrev = vel = float("nan")
                LOG.warning("[%s] [FALHA] pacote com NaN injetado", self.tag)

            payload = struct.pack(
                "<hhh" + "d" * 9,
                self.agc, self.r1, self.gm1,
                self.hfc, ttstd, ttrev, vel, vel_re,
                vol_flow_re, self.vol_total, self.vol_total, self.vol_rev)

            if len(payload) != PAYLOAD_SIZE:
                raise AssertionError("payload com %d bytes (esperado %d)"
                                     % (len(payload), PAYLOAD_SIZE))

            if action == "corrupt_stream":
                payload = payload[:-3]  # pacote truncado de proposito
                LOG.warning("[%s] [FALHA] pacote truncado injetado", self.tag)

            return payload

    def build_packet(self):
        return PREAMBLE + self.build_payload()

    # -- comandos -------------------------------------------------------------
    def resp(self, cmd, ok=True):
        return self.ufo_id + bytes([cmd, CMD_ACK if ok else CMD_NACK])

    def parse_calib_params(self, data):
        """Layout de MeterController.sendCalibDataToMeter(), little-endian:
             0  double  ultraSoundPathLengh
             8  double  reductionDiameter
            16  double  area de reducao
            24  double  dsos
            32  double  dzc
            40  short   N (numero de constantes)
            42  double x N  Reynolds (vazao crescente)
          42+8N double x N  K        (vazao crescente)
        """
        (path_len, red_diam, area, dsos, dzc) = struct.unpack_from("<5d", data, 0)
        (n,) = struct.unpack_from("<h", data, 40)
        reynolds = list(struct.unpack_from("<%dd" % n, data, 42))
        ks = list(struct.unpack_from("<%dd" % n, data, 42 + 8 * n))
        with self.lock:
            self.calib_reynolds = reynolds
            self.calib_k = ks
            self.calib_loaded = True
        LOG.info("[%s] CALIB recebida: pathLength=%.6f m | diamReducao=%.6f m | area=%.9f m2",
                 self.tag, path_len, red_diam, area)
        LOG.info("[%s] CALIB recebida: DSOS=%.6e | DZC=%.6e | N=%d", self.tag, dsos, dzc, n)
        for i in range(n):
            LOG.info("[%s] CALIB[%d]: Reynolds=%12.2f  K=%.6f", self.tag, i, reynolds[i], ks[i])


class MeterHandler(socketserver.BaseRequestHandler):

    def setup(self):
        self.request.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)
        self.request.settimeout(0.05)
        self.meter = self.server.meter
        self.send_lock = threading.Lock()
        self.alive = True
        # Enquanto um comando esta sendo tratado, o stream para. O dispositivo
        # da prioridade ao canal de comando: se os 6 bytes do ACK ficarem
        # soterrados no meio do stream de 86 bytes/pacote, a varredura de
        # MeterSocketThread.checkForAck() nao acha o ACK e o comando estoura o
        # timeout (foi o que aconteceu com o trim AGC: 50 s por medidor).
        # A app confirma essa semantica ao chamar setEnableRead(false) antes de
        # comandar o medidor.
        self.stream_paused_until = 0.0
        # Reenvio da ultima resposta de comando. Necessario porque, se o comando
        # chega enquanto a app esta DENTRO de MeterSocketComm.readDataLine(), a
        # maquina de estados dela (PREAMB/MEASURE) descarta tudo que nao for o
        # preambulo -- e ENGOLE o ACK. So quando a app sai do readDataLine e
        # entra em checkForAck() e que ela consegue enxergar a resposta.
        # Reenviamos entao a mesma resposta algumas vezes ate a app captura-la
        # ou ate um novo comando chegar. O device real tem o mesmo dilema.
        self.repeat_data = None
        self.repeat_until = 0.0
        self.repeat_next = 0.0
        # Buffer de "devolucao": bytes lidos adiante que ainda nao eram nossos.
        self.pending = b""

    def send_bytes(self, data, what):
        """Escrita atomica. O sendall e serializado com o stream pelo lock:
        um pacote de medicao nunca fica intercalado com uma resposta."""
        with self.send_lock:
            self.request.sendall(data)
        # Mantem o canal quieto logo apos a resposta, para a app conseguir
        # varrer os 6 bytes do ACK sem stream no meio.
        agora = time.time()
        self.stream_paused_until = max(self.stream_paused_until,
                                       agora + self.server.quiet_window)
        # Arma o reenvio apenas para respostas CURTAS de comando (ACK/NACK).
        # Blocos de leitura (firmware, config, wmbus) nao sao reenviados: a app
        # os consome logo apos o ACK e um reenvio corromperia o fluxo.
        if len(data) == 6:
            self.repeat_data = data
            self.repeat_until = agora + self.server.repeat_window
            self.repeat_next = agora + self.server.repeat_interval
        LOG.debug("[%s] --> %s (%d bytes)", self.meter.tag, what, len(data))

    def read_exact(self, n, timeout=5.0):
        """Le exatamente n bytes, consumindo primeiro o buffer de devolucao."""
        buf = b""
        if self.pending:
            take = self.pending[:n]
            self.pending = self.pending[len(take):]
            buf += take
        limite = time.time() + timeout
        while len(buf) < n and time.time() < limite and self.alive:
            try:
                chunk = self.request.recv(n - len(buf))
                if not chunk:
                    return None
                buf += chunk
            except socket.timeout:
                continue
        return buf if len(buf) == n else None

    def skip_duplicate_cmd(self, cmd, timeout=0.30):
        """Consome a SEGUNDA copia do byte de comando.

        MeterSocketThread.run() envia todo comando duas vezes, com ~20 ms entre
        as copias, ANTES de comecar a procurar o ACK -- ou seja, na ordem do fio
        vem [cmd][cmd][payload...]. Como respondemos o ACK ja na primeira copia,
        a segunda ainda esta a caminho e seria lida como o 1o byte do payload
        (foi exatamente isso que fez N=1214 no lugar de N=4 no selftest).

        Se o byte que chegar NAO for a copia, ele e devolvido ao buffer.
        """
        limite = time.time() + timeout
        while time.time() < limite and self.alive:
            try:
                b = self.request.recv(1)
            except socket.timeout:
                continue
            if not b:
                return
            if b[0] == cmd:
                LOG.debug("[%s] copia duplicada de 0x%02X consumida antes do payload",
                          self.meter.tag, cmd)
            else:
                self.pending += b
            return

    def repeater_loop(self):
        """Reenvia a ultima resposta de comando enquanto a janela estiver aberta."""
        while self.alive:
            time.sleep(0.05)
            agora = time.time()
            if self.repeat_data is None or agora >= self.repeat_until:
                continue
            if agora < self.repeat_next:
                continue
            try:
                with self.send_lock:
                    self.request.sendall(self.repeat_data)
                LOG.debug("[%s] reenvio da resposta (%d bytes)",
                          self.meter.tag, len(self.repeat_data))
            except (ConnectionResetError, BrokenPipeError, OSError):
                return
            self.repeat_next = agora + self.server.repeat_interval

    def stream_loop(self):
        m = self.meter
        periodo = 1.0 / float(self.server.cfg["stream"]["rate_hz"])
        LOG.info("[%s] STREAM INICIADO (%.1f Hz)", m.tag, 1.0 / periodo)
        inicio = time.time()
        enviados = 0
        proximo = time.time()
        try:
            while self.alive and m.transmitting:
                if time.time() < self.stream_paused_until:
                    time.sleep(0.01)
                    proximo = time.time()
                    continue
                pkt = m.build_packet()
                # Escrita ATOMICA dos 86 bytes: um unico sendall sob o lock.
                # Pacote parcial ou preambulo quebrado reprova o medidor na
                # coleta (MeterSocketThread: saveData -> stop imediato).
                with self.send_lock:
                    self.request.sendall(pkt)
                enviados += 1
                m.packets_sent += 1
                proximo += periodo
                dorme = proximo - time.time()
                if dorme > 0:
                    time.sleep(dorme)
                else:
                    proximo = time.time()
        except (ConnectionResetError, BrokenPipeError, OSError) as exc:
            LOG.warning("[%s] stream interrompido: %s", m.tag, exc)
        finally:
            dur = time.time() - inicio
            LOG.info("[%s] STREAM PARADO -- %d pacotes em %.1f s (%.1f Hz efetivo)",
                     m.tag, enviados, dur, enviados / dur if dur > 0 else 0.0)

    def handle_command(self, cmd):
        m = self.meter
        # Silencia o stream durante o tratamento e por uma janela curta depois,
        # para o ACK chegar em canal limpo.
        self.stream_paused_until = time.time() + self.server.quiet_window
        self.repeat_data = None      # novo comando cancela o reenvio do anterior
        m.commands_handled += 1
        nome = CMD_NAME.get(cmd, "0x%02X" % cmd)
        LOG.info("[%s] <-- comando 0x%02X %s", m.tag, cmd, nome)

        action, rule = m.faults.for_command(m.num, cmd)
        if action == "silence":
            LOG.warning("[%s] [FALHA] silencio proposital em 0x%02X", m.tag, cmd)
            return
        if action == "delay":
            time.sleep(float(rule.get("delay_seconds", 3.0)))
        if action == "nack":
            LOG.warning("[%s] [FALHA] NACK proposital em 0x%02X", m.tag, cmd)
            self.send_bytes(m.resp(cmd, ok=False), "NACK %s" % nome)
            return

        # ---- comandos sem payload: UM unico ACK ----------------------------
        if cmd in NO_PAYLOAD:
            if cmd == CMD_ENABLE_DATA_TRANSMIT:
                self.send_bytes(m.resp(cmd), "ACK %s" % nome)
                if not m.transmitting:
                    m.transmitting = True
                    threading.Thread(target=self.stream_loop,
                                     name="stream-%s" % m.tag, daemon=True).start()
                return
            if cmd == CMD_DISABLE_DATA_TRANSMIT:
                m.transmitting = False
                time.sleep(1.0 / float(self.server.cfg["stream"]["rate_hz"]) * 1.5)
                self.send_bytes(m.resp(cmd), "ACK %s" % nome)
                return
            if cmd in (CMD_LOAD_PRE_CALIB_OPMODE, CMD_LOAD_CALIB_OPMODE,
                       CMD_LOAD_POST_CALIB_OPMODE, CMD_LOAD_STAND_BY_OPMODE,
                       CMD_LOAD_NORMAL_OPMODE, CMD_LOAD_VERIF_OPMODE):
                m.mode = nome.replace("LOAD_", "").replace("_OPMODE", "")
                LOG.info("[%s] modo de operacao -> %s", m.tag, m.mode)
            self.send_bytes(m.resp(cmd), "ACK %s" % nome)
            return

        # ---- leitura: ACK + payload ----------------------------------------
        if cmd == CMD_READ_FIRMWARE_VERSION:
            # MeterSocketComm.readFwVersion le 6 bytes e, se
            # (fw[0]==1 && fw[4]<8), assume build 0; senao le mais 2.
            # Com fw[4]=8 ele le os 8 -> enviamos os 8.
            self.send_bytes(m.resp(cmd) + m.fw, "ACK+firmware")
            return
        if cmd in READ_PAYLOAD_SIZE:
            dados = {0x02: m.serial, 0x04: m.replacement_date,
                     0xBB: m.config_block, 0xD1: m.date, 0xD4: m.wmbus}[cmd]
            self.send_bytes(m.resp(cmd) + dados, "ACK+%d bytes" % len(dados))
            return

        # ---- escrita com payload: ACK, payload, ACK -------------------------
        if cmd == CMD_LOAD_CALIB_PARAMETERS:
            self.send_bytes(m.resp(cmd), "ACK %s (1/2)" % nome)
            self.skip_duplicate_cmd(cmd)
            cabecalho = self.read_exact(42)
            if cabecalho is None:
                LOG.error("[%s] 0xAA: cabecalho de 42 bytes nao chegou", m.tag)
                return
            (n,) = struct.unpack_from("<h", cabecalho, 40)
            corpo = self.read_exact(16 * n) if n > 0 else b""
            if corpo is None:
                LOG.error("[%s] 0xAA: corpo de %d bytes nao chegou", m.tag, 16 * n)
                return
            try:
                m.parse_calib_params(cabecalho + corpo)
            except Exception:
                LOG.exception("[%s] 0xAA: falha no parse", m.tag)
                self.send_bytes(m.resp(cmd, ok=False), "NACK %s" % nome)
                return
            if m.echo_writes:
                self.send_bytes(cabecalho + corpo, "eco de %d bytes" % (42 + 16 * n))
            else:
                self.send_bytes(m.resp(cmd), "ACK %s (2/2)" % nome)
            return

        if cmd in WRITE_PAYLOAD_SIZE:
            tam = WRITE_PAYLOAD_SIZE[cmd]
            self.send_bytes(m.resp(cmd), "ACK %s (1/2)" % nome)
            self.skip_duplicate_cmd(cmd)
            dados = self.read_exact(tam)
            if dados is None:
                LOG.error("[%s] 0x%02X: payload de %d bytes nao chegou", m.tag, cmd, tam)
                return
            with m.lock:
                if cmd == 0x03:
                    m.serial = dados
                elif cmd == 0x05:
                    m.replacement_date = dados
                elif cmd == 0xD0:
                    m.date = dados
                elif cmd == 0xD3:
                    m.wmbus = dados
                elif cmd == 0xCC:
                    m.config_block = dados
            LOG.info("[%s] 0x%02X %s: %d bytes gravados", m.tag, cmd, nome, len(dados))
            if m.echo_writes:
                self.send_bytes(dados, "eco de %d bytes" % len(dados))
            else:
                self.send_bytes(m.resp(cmd), "ACK %s (2/2)" % nome)
            return

        LOG.error("[%s] comando DESCONHECIDO 0x%02X -- NACK", m.tag, cmd)
        self.send_bytes(m.resp(cmd, ok=False), "NACK")

    def handle(self):
        m = self.meter
        peer = "%s:%d" % self.client_address[:2]
        LOG.info("=== [%s] conexao de %s (porta %d, ufoId=%s) ===",
                 m.tag, peer, self.server.server_address[1], m.ufo_id.hex().upper())
        threading.Thread(target=self.repeater_loop,
                         name="repeat-%s" % m.tag, daemon=True).start()
        ultimo_cmd = None
        ultimo_t = 0.0
        try:
            while self.alive:
                if self.pending:
                    b, self.pending = self.pending[:1], self.pending[1:]
                else:
                    try:
                        b = self.request.recv(1)
                    except socket.timeout:
                        continue
                if not b:
                    break
                cmd = b[0]
                agora = time.time()
                # A app envia cada comando DUAS vezes com ~20 ms de intervalo
                # (MeterSocketThread.run). A segunda copia e ignorada; a
                # retentativa de ~525 ms passa pela janela e e atendida.
                if cmd == ultimo_cmd and (agora - ultimo_t) < 0.25:
                    LOG.debug("[%s] copia duplicada de 0x%02X ignorada", m.tag, cmd)
                    ultimo_t = agora
                    continue
                ultimo_cmd, ultimo_t = cmd, agora
                self.handle_command(cmd)
        except (ConnectionResetError, BrokenPipeError, OSError) as exc:
            LOG.warning("[%s] conexao perdida: %s", m.tag, exc)
        except Exception:
            LOG.exception("[%s] erro inesperado", m.tag)
        finally:
            self.alive = False
            m.transmitting = False
            LOG.info("=== [%s] conexao encerrada -- %d comandos, %d pacotes ===",
                     m.tag, m.commands_handled, m.packets_sent)


class MeterServer(socketserver.ThreadingTCPServer):
    allow_reuse_address = True
    daemon_threads = True

    def __init__(self, addr, meter, cfg):
        self.meter = meter
        self.cfg = cfg
        st = cfg.get("stream", {})
        self.quiet_window = float(st.get("quiet_window_s", 0.25))
        self.repeat_window = float(st.get("ack_repeat_window_s", 3.0))
        self.repeat_interval = float(st.get("ack_repeat_interval_s", 0.3))
        socketserver.ThreadingTCPServer.__init__(self, addr, MeterHandler)


class MeterFarm(object):
    """Os 20 medidores."""

    def __init__(self, cfg, flow_source):
        self.cfg = cfg
        self.faults = FaultInjector(cfg.get("fault_injection", {}))
        self.servers = []
        self.threads = []
        self.meters = []

        host = cfg.get("host", "127.0.0.1")
        base = int(cfg.get("base_port", 2051))
        count = int(cfg.get("meter_count", 20))
        defaults = cfg.get("defaults", {})
        por_medidor = cfg.get("meters", {})

        for i in range(1, count + 1):
            perfil = dict(defaults)
            perfil.update(por_medidor.get(str(i), {}) or {})
            if not perfil.get("enabled", True):
                LOG.warning("MEDIDOR-%02d desabilitado no config -- porta nao sobe", i)
                continue
            m = Meter(i, cfg, perfil, flow_source, self.faults)
            porta = base + (i - 1)
            srv = MeterServer((host, porta), m, cfg)
            self.servers.append(srv)
            self.meters.append(m)

    def start(self):
        for srv in self.servers:
            t = threading.Thread(target=srv.serve_forever,
                                 name="meter-%d" % srv.server_address[1], daemon=True)
            t.start()
            self.threads.append(t)
        portas = [s.server_address[1] for s in self.servers]
        LOG.info("%d medidores escutando em %s:%d..%d",
                 len(portas), self.cfg.get("host", "127.0.0.1"),
                 min(portas), max(portas))

    def stop(self):
        for srv in self.servers:
            srv.shutdown()
            srv.server_close()


# =============================================================================
# Infraestrutura
# =============================================================================
def setup_logging(log_dir, level):
    os.makedirs(log_dir, exist_ok=True)
    path = os.path.join(log_dir, "medidores_%s.log"
                        % datetime.now().strftime("%Y-%m-%d_%H-%M-%S"))
    fmt = logging.Formatter("%(asctime)s.%(msecs)03d %(levelname)-8s %(message)s",
                            datefmt="%Y-%m-%d %H:%M:%S")
    root = logging.getLogger()
    root.setLevel(getattr(logging, str(level).upper(), logging.INFO))
    for h in list(root.handlers):
        root.removeHandler(h)
    fh = logging.FileHandler(path, encoding="utf-8")
    fh.setFormatter(fmt)
    root.addHandler(fh)
    sh = logging.StreamHandler(sys.stdout)
    sh.setFormatter(fmt)
    root.addHandler(sh)
    return path


def load_config(path):
    if path and os.path.exists(path):
        with open(path, "r", encoding="utf-8") as f:
            return json.load(f)
    return {}


def default_config_path():
    return os.path.join(os.path.dirname(os.path.abspath(__file__)), "meter_config.json")


# =============================================================================
# Selftest
# =============================================================================
def selftest():
    cfg = load_config(default_config_path())
    host = cfg.get("host", "127.0.0.1")
    base = int(cfg.get("base_port", 2051))
    count = int(cfg.get("meter_count", 20))
    vazao = {"q": 0.0}

    farm = MeterFarm(cfg, lambda: vazao["q"])
    farm.start()
    time.sleep(1.0)

    falhas = []

    def ok(desc, extra=""):
        print("  OK  %-52s %s" % (desc, extra))

    # (a) 20 portas
    print("(a) os 20 servidores sobem nas portas certas:")
    abertas = 0
    for i in range(count):
        s = socket.socket()
        s.settimeout(1.0)
        try:
            s.connect((host, base + i))
            abertas += 1
        except Exception as exc:
            falhas.append("porta %d fechada: %s" % (base + i, exc))
        finally:
            s.close()
    if abertas == count:
        ok("%d portas %d..%d aceitando conexao" % (abertas, base, base + count - 1))

    # (b) handshake 0x10
    print("(b) handshake de conexao (0x10) responde ACK:")
    for num in (1, 7, 20):
        s = socket.socket()
        s.settimeout(3.0)
        s.connect((host, base + num - 1))
        s.sendall(bytes([CMD_LOAD_PRE_CALIB_OPMODE]))
        time.sleep(0.02)
        s.sendall(bytes([CMD_LOAD_PRE_CALIB_OPMODE]))
        r = s.recv(6)
        s.close()
        if len(r) != 6 or r[4] != CMD_LOAD_PRE_CALIB_OPMODE or r[5] != CMD_ACK:
            falhas.append("medidor %d: handshake devolveu %r" % (num, r))
        else:
            ok("MEDIDOR-%02d ufoId=%s cmd=0x%02X ACK" % (num, r[:4].hex().upper(), r[4]))

    # (c) 0x21 inicia stream e os offsets batem
    print("(c) 0x21 inicia o stream e o payload bate com PackageHandler:")
    vazao["q"] = 2500.0
    s = socket.socket()
    s.settimeout(5.0)
    s.connect((host, base))
    s.sendall(bytes([CMD_ENABLE_DATA_TRANSMIT]))
    time.sleep(0.02)
    s.sendall(bytes([CMD_ENABLE_DATA_TRANSMIT]))
    r = s.recv(6)
    if len(r) != 6 or r[5] != CMD_ACK:
        falhas.append("0x21 nao devolveu ACK: %r" % (r,))

    def ler_pacote(sock):
        """Replica MeterSocketComm.readDataLine: 8x0xFF depois 78 bytes."""
        amb = 0
        while amb < 8:
            b = sock.recv(1)
            if not b:
                return None
            amb = amb + 1 if b[0] == 0xFF else 0
        buf = b""
        while len(buf) < PAYLOAD_SIZE:
            c = sock.recv(PAYLOAD_SIZE - len(buf))
            if not c:
                return None
            buf += c
        return buf

    p = ler_pacote(s)
    if p is None or len(p) != PAYLOAD_SIZE:
        falhas.append("primeiro pacote invalido")
    else:
        # Mesmos offsets de PackageHandler.unpackData()
        agc = struct.unpack_from("<h", p, 0)[0]
        r1 = struct.unpack_from("<h", p, 2)[0]
        gm1 = struct.unpack_from("<h", p, 4)[0]
        hfc = struct.unpack_from("<d", p, 6)[0]
        ttstd = struct.unpack_from("<d", p, 14)[0]
        ttrev = struct.unpack_from("<d", p, 22)[0]
        vel = struct.unpack_from("<d", p, 30)[0]
        velre = struct.unpack_from("<d", p, 38)[0]
        volflow = struct.unpack_from("<d", p, 46)[0]
        ok("payload de 78 bytes, offsets conferem",
           "agc=%d r1=%d gm1=%d" % (agc, r1, gm1))
        ok("ttstd=%.9e s  ttrev=%.9e s" % (ttstd, ttrev))
        ok("vel=%.4f m/s  velRe=%.4f m/s  volFlowRe=%.4f m3/h" % (vel, velre, volflow))
        if any(math.isnan(x) for x in (ttstd, ttrev, vel)):
            falhas.append("ttstd/ttrev/vel com NaN")
        if ttstd == 0 and ttrev == 0 and vel == 0:
            falhas.append("ttstd/ttrev/vel todos zero")
        if hfc <= 0:
            falhas.append("hfc invalido")

        # Coerencia fisica: a app recalcularia esta vazao
        area = math.pi * (float(cfg["physics"]["reduction_diameter_m"]) / 2) ** 2
        q_calc = volflow * 1000.0
        erro = abs(q_calc - 2500.0) / 2500.0
        if erro > 0.01:
            falhas.append("volFlowRe implica %.1f L/h (esperado 2500, erro %.2f%%)"
                          % (q_calc, erro * 100))
        else:
            ok("volFlowRe -> %.1f L/h (alvo 2500, erro %.3f%%)" % (q_calc, erro * 100))

    # (d) integridade do stream
    print("(d) integridade do stream por 5 s:")
    n = 0
    t0 = time.time()
    ruins = 0
    while time.time() - t0 < 5.0:
        p = ler_pacote(s)
        if p is None or len(p) != PAYLOAD_SIZE:
            ruins += 1
            break
        tt1 = struct.unpack_from("<d", p, 14)[0]
        tt2 = struct.unpack_from("<d", p, 22)[0]
        vv = struct.unpack_from("<d", p, 30)[0]
        if any(math.isnan(x) for x in (tt1, tt2, vv)) or (tt1 == 0 and tt2 == 0 and vv == 0):
            ruins += 1
        n += 1
    if ruins:
        falhas.append("%d pacote(s) invalido(s) em %d" % (ruins, n))
    else:
        ok("%d pacotes integros em 5 s (%.1f Hz), 0 perdidos" % (n, n / 5.0))

    s.sendall(bytes([CMD_DISABLE_DATA_TRANSMIT]))
    time.sleep(0.5)
    s.close()

    # (e) 0xAA parseado
    print("(e) 0xAA (parametros de calibracao) parseado:")
    s = socket.socket()
    s.settimeout(5.0)
    s.connect((host, base))
    n_const = 4
    reynolds = [1000.0, 5000.0, 20000.0, 60000.0]
    ks = [1.001, 1.002, 1.003, 1.004]
    corpo = struct.pack("<5d", 0.084, 0.010, 7.853981634e-5, 1.0e-6, -1.0e-9)
    corpo += struct.pack("<h", n_const)
    corpo += struct.pack("<%dd" % n_const, *reynolds)
    corpo += struct.pack("<%dd" % n_const, *ks)
    esperado = 42 + 16 * n_const
    if len(corpo) != esperado:
        falhas.append("payload 0xAA montado com %d bytes (esperado %d)" % (len(corpo), esperado))
    s.sendall(bytes([CMD_LOAD_CALIB_PARAMETERS]))
    time.sleep(0.02)
    s.sendall(bytes([CMD_LOAD_CALIB_PARAMETERS]))
    a1 = s.recv(6)
    s.sendall(corpo)
    a2 = s.recv(6)
    s.close()
    if len(a1) != 6 or a1[5] != CMD_ACK:
        falhas.append("0xAA: 1o ACK ausente (%r)" % (a1,))
    elif len(a2) != 6 or a2[5] != CMD_ACK:
        falhas.append("0xAA: 2o ACK ausente (%r)" % (a2,))
    else:
        ok("0xAA: 2 ACKs (%d bytes de payload, N=%d)" % (esperado, n_const))
        m0 = farm.meters[0]
        if m0.calib_reynolds != reynolds or m0.calib_k != ks:
            falhas.append("tabela K/Re nao bateu: %r / %r" % (m0.calib_reynolds, m0.calib_k))
        else:
            ok("tabela Reynolds/K registrada igual a enviada")

    farm.stop()
    print("")
    if falhas:
        print("FALHAS (%d):" % len(falhas))
        for f in falhas:
            print("  - " + f)
        return 1
    print("SELFTEST OK -- todas as verificacoes passaram.")
    return 0


def main():
    ap = argparse.ArgumentParser(description="Simulador dos 20 medidores (binario sobre TCP)")
    ap.add_argument("--config", default=default_config_path())
    ap.add_argument("--host", default=None)
    ap.add_argument("--base-port", type=int, default=None)
    ap.add_argument("--log-dir", default=None)
    ap.add_argument("--log-level", default=None)
    ap.add_argument("--flow", type=float, default=0.0,
                    help="vazao fixa em L/h quando rodando sem a BCI")
    ap.add_argument("--faults", action="store_true")
    ap.add_argument("--selftest", action="store_true")
    args = ap.parse_args()

    if args.selftest:
        logging.basicConfig(level=logging.WARNING, format="%(levelname)s %(message)s")
        return selftest()

    cfg = load_config(args.config)
    if args.host:
        cfg["host"] = args.host
    if args.base_port:
        cfg["base_port"] = args.base_port
    if args.faults:
        cfg.setdefault("fault_injection", {})["enabled"] = True

    log_path = setup_logging(args.log_dir or cfg.get("log_dir", "logs_simulador"),
                             args.log_level or cfg.get("log_level", "INFO"))

    LOG.info("=" * 78)
    LOG.info("Simulador dos medidores -- arquivo de log: %s", os.path.abspath(log_path))
    LOG.info("Vazao fixa de %.1f L/h (sem a BCI). Use run_all.py para acoplar a vazao real.",
             args.flow)
    LOG.info("=" * 78)

    farm = MeterFarm(cfg, lambda: args.flow)
    farm.start()
    try:
        while True:
            time.sleep(3600)
    except KeyboardInterrupt:
        LOG.info("Interrompido pelo usuario.")
    finally:
        farm.stop()
    return 0


if __name__ == "__main__":
    sys.exit(main())
