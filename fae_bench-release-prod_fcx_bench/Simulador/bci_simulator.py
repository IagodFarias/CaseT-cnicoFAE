#!/usr/bin/env python3
# -*- coding: ascii -*-
"""
Simulador da BCI (Bench Control Interface) -- canal A.1 do contrato.

Protocolo (extraido de BCISocketComm / BenchControlImpl / protocolFields.properties):
  - TCP, uma mensagem JSON por linha, terminada em '\n'.
  - A aplicacao escreve com PrintWriter.println e le com BufferedReader.readLine.
  - O JSON e ASCII puro; nao emitimos nenhum caractere fora de ASCII.
  - Roteamento pelo campo "command".
  - EXATAMENTE UMA linha de resposta por linha de requisicao. A app serializa o
    par send/read com um semaforo; uma linha a mais dessincroniza todo o canal.

Regra critica de serializacao:
  O ObjectMapper da aplicacao (json/JSonParser.java) usa a configuracao PADRAO do
  Jackson, ou seja FAIL_ON_UNKNOWN_PROPERTIES = true. Qualquer campo extra em uma
  resposta faz readValue() lancar excecao, o parse devolver null e o comando
  falhar silenciosamente. Por isso cada resposta abaixo contem SOMENTE os campos
  declarados na classe correspondente em bciapi/command/model/.

Uso:
    python bci_simulator.py
    python bci_simulator.py --config bci_config.json
    python bci_simulator.py --host 127.0.0.1 --port 8888
    python bci_simulator.py --selftest
"""

import argparse
import json
import logging
import os
import random
import socket
import socketserver
import sys
import threading
import time
from datetime import datetime

# =============================================================================
# Nomes de comando -- espelham protocolFields.properties
# =============================================================================
CMD_STOP = "stop"
CMD_RESET = "reset"
CMD_ACK = "ackResponse"
CMD_NACK = "nackResponse"
CMD_GET_ALARMS = "getAlarms"
CMD_OPEN_LINE = "openLine"
CMD_CLOSE_LINE = "closeLine"
CMD_GET_LINE_STATE = "getLineState"
CMD_OPEN_VALVE = "openValve"
CMD_CLOSE_VALVE = "closeValve"
CMD_GET_VALVE_STATE = "getValveState"
CMD_START_PUMP = "startPump"
CMD_STOP_PUMP = "stopPump"
CMD_SET_PUMP_LOAD = "setPumpLoad"
CMD_GET_PUMP_LOAD = "getPumpLoad"
CMD_GET_PUMP_STATE = "getPumpState"
CMD_SET_PID = "setPidParameters"
CMD_GET_PID = "getPidParameters"
CMD_SET_FLOW_RATE = "setFlowRate"
CMD_SET_AUTO_FLOW_RATE = "setAutoFlowRate"
CMD_START_COUNTER = "startCounter"
CMD_STOP_COUNTER = "stopCounter"
CMD_GET_COUNTER = "getCounter"
CMD_READ_REF_METER = "readRefMeter"
CMD_GET_SENSOR_TEMP = "getSensorTemp"
CMD_GET_SENSOR_PRESSURE = "getSensorPressure"
CMD_GET_SENSOR_HUMIDITY = "getSensorHumidity"
CMD_GET_SENSOR_LEVEL = "getSensorLevel"
CMD_GET_SCALE_WEIGHT = "getScaleWeight"
CMD_SET_ENABLE_BUFFER = "setEnableBuffer"

# Tags (devicesTagReference.properties)
VALVE_TAGS = ["XV%d" % i for i in range(1, 22)]
PUMP_TAGS = ["BP1", "BP2", "BREP"]
REFMETER_TAGS = ["HSCFTA", "HSCFTM", "HSCFTB"]
TEMP_TAGS = ["TTLI", "TTLO", "TTWL", "TTRI", "TTRS", "TTAMB"]
PRESS_TAGS = ["PTLI", "PTLO", "PTDIF", "PTBAR"]
LEVEL_TAGS = ["SN1", "SN2", "SN3", "SN4"]

# Faixas dos medidores de referencia (L/h) -- alinhadas com testdb/seed_test_db.sql
REFMETER_RANGE = {
    "HSCFTB": (5.0, 120.0),
    "HSCFTM": (120.0, 1200.0),
    "HSCFTA": (1200.0, 20000.0),
}

LOG = logging.getLogger("bci")


# =============================================================================
# Estado da bancada
# =============================================================================
class BenchState(object):
    """Estado fisico simulado. Todo acesso passa pelo lock: a app abre uma
    conexao so, mas queremos suportar reconexao e clientes de teste em paralelo."""

    def __init__(self, cfg):
        self.cfg = cfg
        self.lock = threading.RLock()

        self.line_state = cfg.get("line_state", "CLOSED")
        self.valves = dict((t, "CLOSED") for t in VALVE_TAGS)
        self.pumps = dict((t, {"state": "OFF", "load": 0.0}) for t in PUMP_TAGS)
        self.pid = dict((t, {"kp": 1.0, "tm": 1.0, "tv": 0.0, "e": 0.0}) for t in PUMP_TAGS)

        # Setpoint corrente de vazao (L/h) e vazao efetiva entregue pela bancada.
        self.flow_setpoint = 0.0
        self.flow_current = 0.0
        self.flow_pump = None
        self.flow_refmeter = None

        # Peso do pulso, com a MESMA formula de RefMeterController.calcFreqPulseWeight():
        #     mL_por_pulso = (maxFlowRate[L/h] / 3.6) / freqMaxFlowRate[Hz]
        # e portanto pulsos_por_litro = 1000 / mL_por_pulso.
        # Derivar (em vez de fixar) garante que a vazao que a app RECALCULA a
        # partir dos pulsos seja igual a vazao que o simulador esta entregando.
        self.pulses_per_liter = {}
        for tag, rm in (cfg.get("refmeters") or {}).items():
            ml_per_pulse = (float(rm["max_flow_lph"]) / 3.6) / float(rm["freq_max_hz"])
            self.pulses_per_liter[tag] = 1000.0 / ml_per_pulse
        for tag in REFMETER_TAGS:
            self.pulses_per_liter.setdefault(tag, 180.0)

        # Totalizadores de pulso. Um totalizador fisico NUNCA volta para tras:
        # CalibrationService.meanRefFlowRateCounterInitFinal() aborta a vazao com
        # "Sample Counter diff is negative" se o contador reiniciar no meio da
        # coleta. Por isso acumulamos sempre; startCounter apenas marca o inicio.
        self.counters = dict((t, {"running": False, "pulses": 0.0}) for t in REFMETER_TAGS)
        # Volume acumulado (litros) por medidor de referencia.
        self.volumes = dict((t, 0.0) for t in REFMETER_TAGS)

        self.buffer_enabled = False
        self.buffer_window = 0
        self.buffer_size = 0

        self._last_flow_update = time.time()
        self.t_start = time.time()

    # -- utilidades ----------------------------------------------------------
    def _noise(self, value, rel):
        if rel <= 0:
            return value
        return value * (1.0 + random.uniform(-rel, rel))

    def _integrate(self):
        """Avanca volume e pulsos conforme a vazao corrente e o tempo decorrido.
        Chamado a cada leitura -- a app faz polling a ~3,3 Hz."""
        now = time.time()
        dt = now - self._last_flow_update
        self._last_flow_update = now
        if dt <= 0:
            return

        # Aproximacao de primeira ordem do setpoint (a bancada leva um tempo
        # para estabilizar). tau = 1 s: em ~3 s a vazao chega a >95% do alvo.
        tau = 1.0
        alpha = min(1.0, dt / tau)
        self.flow_current += (self.flow_setpoint - self.flow_current) * alpha

        liters = self.flow_current * (dt / 3600.0)  # L/h -> L
        if liters <= 0:
            return
        for tag in REFMETER_TAGS:
            self.volumes[tag] += liters
            # Acumula sempre (totalizador fisico), independente de startCounter.
            self.counters[tag]["pulses"] += liters * self.pulses_per_liter[tag]

    # -- acoes ---------------------------------------------------------------
    def open_line(self):
        with self.lock:
            self.line_state = "OPENED"

    def close_line(self):
        with self.lock:
            self.line_state = "CLOSED"

    def get_line_state(self):
        with self.lock:
            return self.line_state

    def set_valve(self, tag, state):
        with self.lock:
            if tag in self.valves:
                self.valves[tag] = state
                return True
            # Tag desconhecida: registramos, mas ainda respondemos ACK para nao
            # travar o processo por causa de uma valvula ausente no seed.
            LOG.warning("Valvula desconhecida '%s' -- registrando mesmo assim", tag)
            self.valves[tag] = state
            return False

    def get_valve(self, tag):
        with self.lock:
            return self.valves.get(tag, "UNKNOWN")

    def set_pump_load(self, tag, pct):
        with self.lock:
            p = self.pumps.setdefault(tag, {"state": "OFF", "load": 0.0})
            p["load"] = float(pct)
            p["state"] = "ON" if pct > 0 else "OFF"
            # Sem setFlowRate ativo, a carga da bomba governa a vazao (purga).
            if self.flow_setpoint <= 0:
                self.flow_setpoint = float(pct) * 30.0  # 100% -> 3000 L/h

    def start_pump(self, tag):
        with self.lock:
            p = self.pumps.setdefault(tag, {"state": "OFF", "load": 0.0})
            p["state"] = "ON"
            if p["load"] <= 0:
                p["load"] = 50.0

    def stop_pump(self, tag):
        with self.lock:
            p = self.pumps.setdefault(tag, {"state": "OFF", "load": 0.0})
            p["state"] = "OFF"
            p["load"] = 0.0
            if not any(q["state"] == "ON" for q in self.pumps.values()):
                self.flow_setpoint = 0.0

    def get_pump(self, tag):
        with self.lock:
            return dict(self.pumps.get(tag, {"state": "UNKNOWN", "load": 0.0}))

    def set_flow_rate(self, setpoint, pump_tag, refmeter_tag):
        with self.lock:
            self.flow_setpoint = float(setpoint)
            self.flow_pump = pump_tag
            self.flow_refmeter = refmeter_tag
            if pump_tag in self.pumps:
                self.pumps[pump_tag]["state"] = "ON"
                self.pumps[pump_tag]["load"] = min(100.0, float(setpoint) / 30.0)

    def stop_all(self):
        """Comando 'stop': a bancada volta ao repouso. Nao mexe nas valvulas --
        BenchController.zeroFlow() chama stop() e depois fecha as valvulas de linha."""
        with self.lock:
            self.flow_setpoint = 0.0
            self.flow_current = 0.0
            for p in self.pumps.values():
                p["state"] = "OFF"
                p["load"] = 0.0

    def reset_all(self):
        with self.lock:
            self.stop_all()
            for t in self.valves:
                self.valves[t] = "CLOSED"
            self.line_state = "CLOSED"
            for t in REFMETER_TAGS:
                self.counters[t] = {"running": False, "pulses": 0.0}
                self.volumes[t] = 0.0

    def counter_start(self, tag):
        with self.lock:
            self._integrate()
            self.counters.setdefault(tag, {"running": False, "pulses": 0.0})["running"] = True

    def counter_stop(self, tag):
        with self.lock:
            self._integrate()
            if tag in self.counters:
                self.counters[tag]["running"] = False

    def counter_read(self, tag):
        """(pulsos acumulados, relogio em ms).

        O relogio e monotonico desde a subida do simulador e COMPARTILHADO por
        todos os medidores -- a app so usa diferencas (timerDiff = t2 - t1) em
        CalibrationService, entao a origem nao importa, mas ele nunca pode
        reiniciar no meio de uma coleta."""
        with self.lock:
            self._integrate()
            c = self.counters.get(tag)
            elapsed_ms = int((time.time() - self.t_start) * 1000.0)
            if c is None:
                return 0, elapsed_ms
            return int(c["pulses"]), elapsed_ms

    def read_refmeter(self, tag):
        """Devolve (flow L/h, volume L, timeRead ms, flowStability)."""
        with self.lock:
            self._integrate()
            flow = self._noise(self.flow_current, self.cfg.get("flow_noise", 0.004))

            # Estabilidade: STABLE quando a vazao esta a menos de 2% do setpoint.
            if self.flow_setpoint <= 0.0:
                stability = "STABLE" if abs(flow) < 1.0 else "UNSTABLE"
            else:
                err = abs(flow - self.flow_setpoint) / self.flow_setpoint
                stability = "STABLE" if err <= 0.02 else "UNSTABLE"

            # Fora da faixa do medidor de referencia selecionado -> instavel.
            lo, hi = REFMETER_RANGE.get(tag, (0.0, 1e9))
            if self.flow_setpoint > 0 and not (lo <= self.flow_setpoint < hi):
                stability = "UNSTABLE"

            return flow, self.volumes.get(tag, 0.0), int((time.time() - self.t_start) * 1000.0), stability

    def read_temp(self, tag):
        with self.lock:
            base = {
                "TTLI": self.cfg.get("temp_upstream_c", 20.0),
                "TTLO": self.cfg.get("temp_downstream_c", 20.0),
                "TTRI": self.cfg.get("temp_reserv_inf_c", 20.0),
                "TTRS": self.cfg.get("temp_reserv_sup_c", 20.0),
                "TTWL": self.cfg.get("temp_scale_c", 20.0),
                "TTAMB": self.cfg.get("temp_amb_c", 22.0),
            }.get(tag, 20.0)
            return self._noise(base, self.cfg.get("temp_noise", 0.002))

    def read_press(self, tag):
        with self.lock:
            self._integrate()
            if tag == "PTDIF":
                # A pressao diferencial cresce com o quadrado da vazao. Em vazao
                # zero fica no valor de repouso -- e ele que assertStaticZeroFlow
                # usa como setpoint e compara contra +/-3%.
                base = self.cfg.get("press_dif_bar", 0.10)
                if self.flow_current > 1.0:
                    base += 0.5 * (self.flow_current / 3000.0) ** 2
                return self._noise(base, self.cfg.get("press_dif_noise", 0.004))
            base = {
                "PTLI": self.cfg.get("press_upstream_bar", 1.20),
                "PTLO": self.cfg.get("press_downstream_bar", 1.10),
                "PTBAR": self.cfg.get("press_barometric_bar", 0.92),
            }.get(tag, 1.0)
            return self._noise(base, self.cfg.get("press_noise", 0.005))

    def read_humidity(self):
        with self.lock:
            return self._noise(self.cfg.get("humidity_pct", 55.0), 0.01)

    def read_level(self, tag):
        # Reservatorios sempre com nivel adequado: nada de alarme por falta de agua.
        return True

    def read_scale(self):
        with self.lock:
            return self.cfg.get("scale_weight_kg", 0.0)

    def snapshot(self):
        with self.lock:
            open_valves = sorted([t for t, s in self.valves.items() if s == "OPENED"])
            return {
                "line": self.line_state,
                "valves_open": open_valves,
                "pumps": dict((t, dict(p)) for t, p in self.pumps.items()),
                "flow_setpoint": round(self.flow_setpoint, 2),
                "flow_current": round(self.flow_current, 2),
            }


# =============================================================================
# Injecao de falha
# =============================================================================
class FaultInjector(object):
    ACTIONS = ("nack", "delay", "silence", "garbage")

    def __init__(self, cfg):
        self.enabled = bool(cfg.get("enabled", False))
        self.rules = [r for r in cfg.get("rules", []) if r.get("enabled", False)]
        self.counts = {}
        self.lock = threading.Lock()
        if self.enabled and self.rules:
            for r in self.rules:
                LOG.warning("INJECAO DE FALHA ATIVA: command=%s action=%s",
                            r.get("command"), r.get("action"))

    def decide(self, command):
        """Devolve (action, params) ou (None, None)."""
        if not self.enabled:
            return None, None
        with self.lock:
            n = self.counts.get(command, 0) + 1
            self.counts[command] = n
        for r in self.rules:
            if r.get("command") != command:
                continue
            action = r.get("action")
            if action not in self.ACTIONS:
                LOG.error("Acao de falha desconhecida: %r", action)
                continue
            occ = r.get("occurrences")
            every = r.get("every_n")
            hit = False
            if occ:
                hit = n in occ
            elif every:
                hit = (n % int(every) == 0)
            else:
                hit = True
            if hit:
                return action, r
        return None, None


# =============================================================================
# Roteador de comandos
# =============================================================================
class BciProtocol(object):
    def __init__(self, state, faults):
        self.state = state
        self.faults = faults

    @staticmethod
    def ack(cmd):
        return {"command": CMD_ACK, "responseTo": cmd}

    @staticmethod
    def nack(cmd):
        return {"command": CMD_NACK, "responseTo": cmd}

    def handle(self, req):
        """req: dict do JSON recebido. Devolve dict de resposta, ou None para
        'nao responder' (silencio proposital)."""
        cmd = req.get("command")
        if not cmd:
            LOG.error("Requisicao sem campo 'command': %r", req)
            return None

        action, rule = self.faults.decide(cmd)
        if action == "silence":
            LOG.warning("[FALHA] silencio proposital para '%s'", cmd)
            return None
        if action == "delay":
            d = float(rule.get("delay_seconds", 5.0))
            LOG.warning("[FALHA] atraso de %.1f s em '%s'", d, cmd)
            time.sleep(d)
        if action == "nack":
            LOG.warning("[FALHA] NACK proposital em '%s'", cmd)
            return self.nack(cmd)
        if action == "garbage":
            LOG.warning("[FALHA] resposta malformada em '%s'", cmd)
            return "{{lixo-nao-json"

        st = self.state

        # ---- sistema --------------------------------------------------------
        if cmd == CMD_STOP:
            st.stop_all()
            return self.ack(cmd)

        if cmd == CMD_RESET:
            st.reset_all()
            return self.ack(cmd)

        if cmd == CMD_GET_ALARMS:
            # alarmSel = 0 -> nenhum alarme. checkConnectionWithBci() so exige
            # que o campo 'command' volte como 'getAlarms'.
            return {"command": CMD_GET_ALARMS, "alarmSel": 0}

        # ---- linha ----------------------------------------------------------
        if cmd == CMD_OPEN_LINE:
            st.open_line()
            return self.ack(cmd)

        if cmd == CMD_CLOSE_LINE:
            st.close_line()
            return self.ack(cmd)

        if cmd == CMD_GET_LINE_STATE:
            return {"command": CMD_GET_LINE_STATE, "lineState": st.get_line_state()}

        # ---- valvulas -------------------------------------------------------
        if cmd == CMD_OPEN_VALVE:
            st.set_valve(req.get("valveSel"), "OPENED")
            return self.ack(cmd)

        if cmd == CMD_CLOSE_VALVE:
            st.set_valve(req.get("valveSel"), "CLOSED")
            return self.ack(cmd)

        if cmd == CMD_GET_VALVE_STATE:
            tag = req.get("valveSel")
            return {"command": CMD_GET_VALVE_STATE,
                    "valveSel": tag,
                    "valveState": st.get_valve(tag)}

        # ---- bombas ---------------------------------------------------------
        if cmd == CMD_START_PUMP:
            st.start_pump(req.get("pumpSel"))
            return self.ack(cmd)

        if cmd == CMD_STOP_PUMP:
            st.stop_pump(req.get("pumpSel"))
            return self.ack(cmd)

        if cmd == CMD_SET_PUMP_LOAD:
            st.set_pump_load(req.get("pumpSel"), req.get("percentSel", 0.0))
            return self.ack(cmd)

        if cmd == CMD_GET_PUMP_STATE:
            tag = req.get("pumpSel")
            p = st.get_pump(tag)
            return {"command": CMD_GET_PUMP_STATE,
                    "pumpSel": tag,
                    "percentSel": round(p["load"], 3),
                    "pumpState": p["state"]}

        if cmd == CMD_GET_PUMP_LOAD:
            # GetPumpLoad declara pumpSel como long: devolvemos numero, nao string.
            p = st.get_pump(req.get("pumpSel"))
            return {"command": CMD_GET_PUMP_LOAD,
                    "pumpSel": 0,
                    "percenSel": round(p["load"], 3)}

        # ---- PID ------------------------------------------------------------
        if cmd == CMD_SET_PID:
            tag = req.get("pumpSel")
            with st.lock:
                st.pid[tag] = {"kp": req.get("kpSel", 0.0), "tm": req.get("tmSel", 0.0),
                               "tv": req.get("tvSel", 0.0), "e": req.get("e", 0.0)}
            return self.ack(cmd)

        if cmd == CMD_GET_PID:
            tag = req.get("pumpSel")
            with st.lock:
                p = st.pid.get(tag, {"kp": 0.0, "tm": 0.0, "tv": 0.0, "e": 0.0})
            return {"command": CMD_GET_PID, "pumpSel": tag,
                    "kpSel": p["kp"], "tmSel": p["tm"], "tvSel": p["tv"], "e": p["e"]}

        # ---- vazao ----------------------------------------------------------
        if cmd == CMD_SET_FLOW_RATE:
            st.set_flow_rate(req.get("flowRateSel", 0.0),
                             req.get("pumpSel"), req.get("refMeterSel"))
            return self.ack(cmd)

        if cmd == CMD_SET_ENABLE_BUFFER:
            with st.lock:
                st.buffer_enabled = bool(req.get("bufferEnable", 0))
                st.buffer_window = req.get("windowTime", 0)
                st.buffer_size = req.get("bufferSize", 0)
            return self.ack(cmd)

        # ---- contadores -----------------------------------------------------
        if cmd == CMD_START_COUNTER:
            st.counter_start(req.get("refMeterSel"))
            return self.ack(cmd)

        if cmd == CMD_STOP_COUNTER:
            st.counter_stop(req.get("refMeterSel"))
            return self.ack(cmd)

        if cmd == CMD_GET_COUNTER:
            tag = req.get("refMeterSel")
            pulses, elapsed = st.counter_read(tag)
            return {"command": CMD_GET_COUNTER, "refMeterSel": tag,
                    "counterRead": pulses, "timeRead": elapsed}

        # ---- medidor de referencia ------------------------------------------
        if cmd == CMD_READ_REF_METER:
            tag = req.get("refMeterSel")
            flow, vol, tread, stab = st.read_refmeter(tag)
            return {"command": CMD_READ_REF_METER, "refMeterSel": tag,
                    "flowRate": round(flow, 4), "volume": round(vol, 4),
                    "timeRead": tread, "flowStability": stab}

        # ---- sensores -------------------------------------------------------
        if cmd == CMD_GET_SENSOR_TEMP:
            tag = req.get("sensorSel")
            return {"command": CMD_GET_SENSOR_TEMP, "sensorSel": tag,
                    "tempRead": round(st.read_temp(tag), 4)}

        if cmd == CMD_GET_SENSOR_PRESSURE:
            tag = req.get("sensorSel")
            return {"command": CMD_GET_SENSOR_PRESSURE, "sensorSel": tag,
                    "pressureRead": round(st.read_press(tag), 5)}

        if cmd == CMD_GET_SENSOR_HUMIDITY:
            tag = req.get("sensorSel")
            return {"command": CMD_GET_SENSOR_HUMIDITY, "sensorSel": tag,
                    "humidityRead": round(st.read_humidity(), 3)}

        if cmd == CMD_GET_SENSOR_LEVEL:
            tag = req.get("sensorSel")
            return {"command": CMD_GET_SENSOR_LEVEL, "sensorSel": tag,
                    "levelState": st.read_level(tag)}

        if cmd == CMD_GET_SCALE_WEIGHT:
            return {"command": CMD_GET_SCALE_WEIGHT, "weight": round(st.read_scale(), 4)}

        # ---- setAutoFlowRate: NAO implementado de proposito -------------------
        # BenchControlImpl.setAutoFlowRate() espera N respostas para 1 requisicao
        # (streaming). Nenhum ponto do fluxo de calibracao o chama -- responder
        # uma linha so aqui dessincronizaria o canal. Registramos e devolvemos
        # NACK, que a app trata sem quebrar.
        if cmd == CMD_SET_AUTO_FLOW_RATE:
            LOG.error("'%s' recebido: nao implementado (protocolo de streaming). "
                      "Respondendo NACK.", cmd)
            return self.nack(cmd)

        LOG.error("Comando DESCONHECIDO: %r -- respondendo NACK", cmd)
        return self.nack(cmd)


# =============================================================================
# Servidor TCP
# =============================================================================
class BciHandler(socketserver.StreamRequestHandler):
    # A app pode ficar ociosa entre etapas; nao derrubamos a conexao por isso.
    timeout = None

    def handle(self):
        peer = "%s:%d" % self.client_address[:2]
        self.server.conn_count += 1
        conn_id = self.server.conn_count
        LOG.info("=== CONEXAO #%d aberta de %s ===", conn_id, peer)
        proto = self.server.protocol
        n = 0
        try:
            self.connection.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)
            for raw in self.rfile:
                line = raw.decode("ascii", errors="replace").strip()
                if not line:
                    continue
                n += 1
                LOG.info("[#%d %04d] <-- %s", conn_id, n, line)

                try:
                    req = json.loads(line)
                except ValueError as exc:
                    LOG.error("[#%d %04d] JSON invalido (%s) -- ignorando linha", conn_id, n, exc)
                    continue

                try:
                    resp = proto.handle(req)
                except Exception:
                    LOG.exception("[#%d %04d] ERRO ao tratar %r", conn_id, n, req.get("command"))
                    resp = BciProtocol.nack(req.get("command", "unknown"))

                if resp is None:
                    LOG.warning("[#%d %04d] --> (sem resposta)", conn_id, n)
                    continue

                payload = resp if isinstance(resp, str) else json.dumps(resp, ensure_ascii=True)
                LOG.info("[#%d %04d] --> %s", conn_id, n, payload)
                self.wfile.write((payload + "\n").encode("ascii", errors="replace"))
                self.wfile.flush()

        except (ConnectionResetError, BrokenPipeError) as exc:
            LOG.warning("[#%d] conexao perdida: %s", conn_id, exc)
        except Exception:
            LOG.exception("[#%d] erro inesperado no handler", conn_id)
        finally:
            LOG.info("=== CONEXAO #%d encerrada (%d mensagens) | estado: %s ===",
                     conn_id, n, json.dumps(self.server.state.snapshot(), ensure_ascii=True))


class BciServer(socketserver.ThreadingTCPServer):
    allow_reuse_address = True
    daemon_threads = True

    def __init__(self, addr, state, faults):
        self.state = state
        self.protocol = BciProtocol(state, faults)
        self.conn_count = 0
        socketserver.ThreadingTCPServer.__init__(self, addr, BciHandler)


# =============================================================================
# Infraestrutura
# =============================================================================
def setup_logging(log_dir, level):
    os.makedirs(log_dir, exist_ok=True)
    path = os.path.join(log_dir, "bci_%s.log" % datetime.now().strftime("%Y-%m-%d_%H-%M-%S"))
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


def selftest():
    """Exercita o roteador em memoria, sem socket. Cobre o caminho que leva
    checkConnectionWithBci a passar e CLOSELINE a avancar."""
    cfg = load_config(os.path.join(os.path.dirname(os.path.abspath(__file__)), "bci_config.json"))
    state = BenchState(cfg.get("bench", {}))
    proto = BciProtocol(state, FaultInjector({"enabled": False}))
    fails = []

    def check(desc, req, expect):
        got = proto.handle(req)
        for k, v in expect.items():
            if got is None or got.get(k) != v:
                fails.append("%s: esperado %s=%r, obtido %r" % (desc, k, v, got))
                return None
        print("  OK  %-46s -> %s" % (desc, json.dumps(got)))
        return got

    print("checkConnectionWithBci:")
    check("getAlarms", {"command": "getAlarms", "alarmSel": 0},
          {"command": "getAlarms", "alarmSel": 0})

    print("boot (stop + reset):")
    check("stop", {"command": "stop"}, {"command": "ackResponse", "responseTo": "stop"})
    check("reset", {"command": "reset"}, {"command": "ackResponse", "responseTo": "reset"})

    print("CLOSELINE (closeLine + getLineState):")
    check("closeLine", {"command": "closeLine"},
          {"command": "ackResponse", "responseTo": "closeLine"})
    check("getLineState", {"command": "getLineState"},
          {"command": "getLineState", "lineState": "CLOSED"})

    print("openLine reflete no getLineState:")
    check("openLine", {"command": "openLine"},
          {"command": "ackResponse", "responseTo": "openLine"})
    check("getLineState", {"command": "getLineState"},
          {"command": "getLineState", "lineState": "OPENED"})
    proto.handle({"command": "closeLine"})

    print("valvula: openValve reflete no getValveState:")
    check("openValve XV2", {"command": "openValve", "valveSel": "XV2"},
          {"command": "ackResponse", "responseTo": "openValve"})
    check("getValveState XV2", {"command": "getValveState", "valveSel": "XV2"},
          {"command": "getValveState", "valveState": "OPENED"})
    check("closeValve XV2", {"command": "closeValve", "valveSel": "XV2"},
          {"command": "ackResponse", "responseTo": "closeValve"})
    check("getValveState XV2", {"command": "getValveState", "valveSel": "XV2"},
          {"command": "getValveState", "valveState": "CLOSED"})

    print("bomba: setPumpLoad 45% reflete no getPumpState:")
    check("setPumpLoad BP1 45", {"command": "setPumpLoad", "pumpSel": "BP1", "percentSel": 45.0},
          {"command": "ackResponse", "responseTo": "setPumpLoad"})
    check("getPumpState BP1", {"command": "getPumpState", "pumpSel": "BP1"},
          {"command": "getPumpState", "percentSel": 45.0, "pumpState": "ON"})

    print("vazao: setFlowRate 2500 -> readRefMeter converge e fica STABLE:")
    check("setFlowRate 2500", {"command": "setFlowRate", "flowRateSel": 2500.0,
                               "pumpSel": "BP1", "refMeterSel": "HSCFTA"},
          {"command": "ackResponse", "responseTo": "setFlowRate"})
    got = None
    for _ in range(60):
        time.sleep(0.1)
        got = proto.handle({"command": "readRefMeter", "refMeterSel": "HSCFTA"})
        if got.get("flowStability") == "STABLE":
            break
    if not got or got.get("flowStability") != "STABLE":
        fails.append("readRefMeter nao estabilizou: %r" % (got,))
    else:
        print("  OK  %-46s -> %s" % ("readRefMeter estabilizou", json.dumps(got)))

    print("contador: startCounter -> getCounter incrementa:")
    proto.handle({"command": "startCounter", "refMeterSel": "HSCFTA"})
    c1 = proto.handle({"command": "getCounter", "refMeterSel": "HSCFTA"})
    time.sleep(0.5)
    c2 = proto.handle({"command": "getCounter", "refMeterSel": "HSCFTA"})
    if c2["counterRead"] <= c1["counterRead"]:
        fails.append("getCounter nao incrementou: %r -> %r" % (c1, c2))
    else:
        print("  OK  %-46s -> %d -> %d" % ("getCounter incrementou",
                                           c1["counterRead"], c2["counterRead"]))

    # Verificacao central: refazer EXATAMENTE a conta da aplicacao
    # (BenchController.calcPulseToFlowRate + RefMeterController.calcFreqPulseWeight)
    # sobre os pulsos que o simulador entregou. Se divergir, as constantes K
    # calibradas saem erradas mesmo com o ciclo "passando".
    print("coerencia pulso->vazao (formula da aplicacao):")
    for tag, setpoint in (("HSCFTA", 2500.0), ("HSCFTM", 800.0), ("HSCFTB", 100.0)):
        rm = cfg.get("bench", {}).get("refmeters", {}).get(tag)
        proto.handle({"command": "setFlowRate", "flowRateSel": setpoint,
                      "pumpSel": "BP1", "refMeterSel": tag})
        time.sleep(3.0)  # deixa a vazao convergir para o setpoint
        a = proto.handle({"command": "getCounter", "refMeterSel": tag})
        time.sleep(2.0)
        b = proto.handle({"command": "getCounter", "refMeterSel": tag})

        ml_per_pulse = (rm["max_flow_lph"] / 3.6) / rm["freq_max_hz"]
        counter_diff = b["counterRead"] - a["counterRead"]
        timer_diff = b["timeRead"] - a["timeRead"]
        volume_ml = counter_diff * ml_per_pulse
        calc_lph = (volume_ml / (timer_diff / 3.6)) * 1000.0

        erro = abs(calc_lph - setpoint) / setpoint
        if counter_diff <= 0:
            fails.append("%s: counterDiff nao positivo (%d)" % (tag, counter_diff))
        elif erro > 0.02:
            fails.append("%s: vazao recalculada %.1f L/h vs setpoint %.1f (erro %.2f%%)"
                         % (tag, calc_lph, setpoint, erro * 100))
        else:
            print("  OK  %-46s -> %.1f L/h (setpoint %.0f, erro %.2f%%)"
                  % ("%s pulsos->vazao" % tag, calc_lph, setpoint, erro * 100))
    proto.handle({"command": "stop"})

    print("vazao zero: PTDIF estavel dentro de +/-3% (assertStaticZeroFlow):")
    proto.handle({"command": "stop"})
    time.sleep(1.2)
    base = proto.handle({"command": "getSensorPressure", "sensorSel": "PTDIF"})["pressureRead"]
    worst = 0.0
    for _ in range(120):
        v = proto.handle({"command": "getSensorPressure", "sensorSel": "PTDIF"})["pressureRead"]
        worst = max(worst, abs(v - base) / base)
    if worst >= 0.03:
        fails.append("PTDIF variou %.2f%% (limite 3%%)" % (worst * 100))
    else:
        print("  OK  %-46s -> desvio maximo %.3f%%" % ("PTDIF dentro da banda", worst * 100))

    print("sensores restantes respondem:")
    for c, extra, key in (("getSensorTemp", {"sensorSel": "TTLI"}, "tempRead"),
                          ("getSensorHumidity", {"sensorSel": "TRH"}, "humidityRead"),
                          ("getSensorLevel", {"sensorSel": "SN1"}, "levelState"),
                          ("getScaleWeight", {}, "weight")):
        r = dict(extra); r["command"] = c
        got = proto.handle(r)
        if got is None or key not in got:
            fails.append("%s sem campo %s: %r" % (c, key, got))
        else:
            print("  OK  %-46s -> %s" % (c, json.dumps(got)))

    print("")
    if fails:
        print("FALHAS (%d):" % len(fails))
        for f in fails:
            print("  - " + f)
        return 1
    print("SELFTEST OK -- todas as verificacoes passaram.")
    return 0


def main():
    ap = argparse.ArgumentParser(description="Simulador da BCI (JSON sobre TCP)")
    here = os.path.dirname(os.path.abspath(__file__))
    ap.add_argument("--config", default=os.path.join(here, "bci_config.json"))
    ap.add_argument("--host", default=None)
    ap.add_argument("--port", type=int, default=None)
    ap.add_argument("--log-dir", default=None)
    ap.add_argument("--log-level", default=None)
    ap.add_argument("--faults", action="store_true",
                    help="liga a injecao de falha (sobrepoe o config)")
    ap.add_argument("--selftest", action="store_true",
                    help="roda a bateria de verificacao interna e sai")
    args = ap.parse_args()

    if args.selftest:
        logging.basicConfig(level=logging.WARNING, format="%(levelname)s %(message)s")
        return selftest()

    cfg = load_config(args.config)
    host = args.host or cfg.get("host", "127.0.0.1")
    port = args.port or int(cfg.get("port", 8888))
    log_dir = args.log_dir or cfg.get("log_dir", "logs_simulador")
    level = args.log_level or cfg.get("log_level", "INFO")

    log_path = setup_logging(log_dir, level)

    fault_cfg = dict(cfg.get("fault_injection", {}))
    if args.faults:
        fault_cfg["enabled"] = True

    state = BenchState(cfg.get("bench", {}))
    faults = FaultInjector(fault_cfg)

    LOG.info("=" * 78)
    LOG.info("Simulador da BCI escutando em %s:%d", host, port)
    LOG.info("Arquivo de log: %s", os.path.abspath(log_path))
    LOG.info("Injecao de falha: %s", "ATIVA" if faults.enabled else "desligada")
    LOG.info("Aponte socketConfig.xml <socket socketId=\"si\"> para %s:%d", host, port)
    LOG.info("=" * 78)

    try:
        server = BciServer((host, port), state, faults)
    except OSError as exc:
        LOG.error("Nao foi possivel abrir %s:%d -- %s", host, port, exc)
        return 2

    try:
        server.serve_forever()
    except KeyboardInterrupt:
        LOG.info("Interrompido pelo usuario. Estado final: %s",
                 json.dumps(state.snapshot(), ensure_ascii=True))
    finally:
        server.server_close()
    return 0


if __name__ == "__main__":
    sys.exit(main())
