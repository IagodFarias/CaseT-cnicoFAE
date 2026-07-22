#!/usr/bin/env python3
"""
Simulador do medidor de referencia (padrao) do ensaio metrologico.

Servidor TCP que acumula volume continuamente enquanto o ensaio esta ativo,
integrando vazao x dt com relogio monotonico e ruido aleatorio na vazao.

Protocolo: JSON, uma mensagem por linha (delimitador '\\n').
    {"command":"START"} -> {"command":"START","status":"OK"}
    {"command":"READ"}  -> {"command":"READ","volume":120.45,"flowRate":15.20,"stable":true}
    {"command":"STOP"}  -> {"command":"STOP","status":"OK"}

Unidades: volume em litros (L), vazao em litros por minuto (L/min).

Uso:
    python simulador_medidor.py --vazao 15.0 --ruido 0.02
"""

import argparse
import json
import random
import socket
import socketserver
import threading
import time


class InjecaoDeFalhas:
    """
    Modo de teste do simulador: suspende as respostas por um periodo.

    Serve para o software de calibracao exercitar o tratamento de SocketTimeoutException
    com uma falha REAL — o simulador simplesmente para de responder, sem fechar a conexao,
    que e exatamente o que acontece quando o equipamento trava ou a rede engarrafa.

    O estado e GLOBAL do servidor, e nao por conexao, de proposito: quem aciona a injecao
    e uma conexao de controle separada e efemera, e ela precisa afetar a conexao do ensaio
    em curso. Fosse por conexao, silenciar a conexao de controle nao teria efeito nenhum
    sobre o ensaio.

    A injecao expira sozinha, para o simulador voltar a operar sem precisar reiniciar
    entre uma demonstracao e outra.
    """

    def __init__(self):
        self._lock = threading.Lock()
        self._silencio_ate = 0.0

    def silenciar(self, segundos):
        with self._lock:
            self._silencio_ate = time.monotonic() + segundos

    def ativa(self):
        with self._lock:
            return time.monotonic() < self._silencio_ate

    def cancelar(self):
        with self._lock:
            self._silencio_ate = 0.0


class Ensaio:
    """
    Estado de um ensaio. Cada conexao tem o seu, isolado das demais.

    O acumulo roda numa thread propria (nao no atendimento dos comandos):
    o volume avanca com o tempo real, independente de o cliente estar ou nao
    enviando READs. Todo acesso ao estado passa pelo lock.
    """

    def __init__(self, vazao_nominal, ruido, tick, banda_estavel, aquecimento, rng):
        self._vazao_nominal = vazao_nominal
        self._ruido = ruido                    # desvio padrao relativo do ruido
        self._tick = tick                      # periodo de integracao, em segundos
        self._banda_estavel = banda_estavel    # desvio relativo maximo p/ 'stable'
        self._aquecimento = aquecimento        # segundos ate o ensaio ser considerado estavel
        self._rng = rng

        self._lock = threading.Lock()
        self._ativo = False
        self._volume = 0.0                     # litros acumulados
        self._vazao_atual = 0.0                # vazao instantanea (com ruido)
        self._t_inicio = 0.0
        self._t_ultimo = 0.0
        self._thread = None
        self._parar = threading.Event()

    # ---------- comandos ----------

    def start(self):
        with self._lock:
            if self._ativo:
                return False
            agora = time.monotonic()
            self._ativo = True
            self._volume = 0.0
            self._vazao_atual = self._vazao_nominal
            self._t_inicio = agora
            self._t_ultimo = agora
            self._parar.clear()
            self._thread = threading.Thread(target=self._integrar, daemon=True)
            self._thread.start()
            return True

    def read(self):
        """Fotografia consistente do estado. Apos o STOP devolve o volume final congelado."""
        with self._lock:
            if self._ativo:
                self._acumular_sem_lock(time.monotonic())
                decorrido = self._t_ultimo - self._t_inicio
                desvio = abs(self._vazao_atual - self._vazao_nominal) / self._vazao_nominal
                estavel = decorrido >= self._aquecimento and desvio <= self._banda_estavel
                return self._volume, self._vazao_atual, estavel
            return self._volume, 0.0, False

    def stop(self):
        with self._lock:
            if not self._ativo:
                return False
            self._acumular_sem_lock(time.monotonic())   # integracao final antes de congelar
            self._ativo = False
            self._vazao_atual = 0.0
            thread = self._thread
            self._thread = None
        self._parar.set()
        if thread is not None:
            thread.join(timeout=2.0)
        return True

    def encerrar(self):
        """Desliga a thread de acumulo quando a conexao cai sem STOP."""
        if self._ativo:
            self.stop()

    # ---------- acumulo ----------

    def _integrar(self):
        while not self._parar.wait(self._tick):
            with self._lock:
                if not self._ativo:
                    return
                self._acumular_sem_lock(time.monotonic())

    def _acumular_sem_lock(self, agora):
        """volume += vazao_instantanea x dt. Exige o lock ja adquirido."""
        dt = agora - self._t_ultimo
        if dt <= 0:
            return
        self._t_ultimo = agora
        # Ruido gaussiano reamostrado a cada passo; truncado em zero porque
        # vazao negativa nao tem significado fisico aqui.
        if self._ruido > 0:
            fator = self._rng.gauss(1.0, self._ruido)
            self._vazao_atual = max(0.0, self._vazao_nominal * fator)
        else:
            self._vazao_atual = self._vazao_nominal
        self._volume += self._vazao_atual * (dt / 60.0)   # L/min -> L


class ManipuladorEnsaio(socketserver.StreamRequestHandler):
    """Atende uma conexao: le linhas JSON, responde linhas JSON."""

    def setup(self):
        super().setup()
        self.request.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)
        cfg = self.server.config
        self.ensaio = Ensaio(
            vazao_nominal=cfg.vazao,
            ruido=cfg.ruido,
            tick=cfg.tick,
            banda_estavel=cfg.banda_estavel,
            aquecimento=cfg.aquecimento,
            rng=random.Random(cfg.semente) if cfg.semente is not None else random.Random(),
        )

    def handle(self):
        par = f"{self.client_address[0]}:{self.client_address[1]}"
        log(f"conexao aberta   <- {par}")
        try:
            for linha_bruta in self.rfile:
                linha = linha_bruta.decode("utf-8", errors="replace").strip()
                if not linha:
                    continue
                resposta = self._processar(linha)
                if resposta is None:
                    # Injecao de falha ativa: a resposta e engolida de proposito. A conexao
                    # continua aberta, entao o cliente fica esperando ate o setSoTimeout dele
                    # disparar — que e o cenario que se quer exercitar.
                    continue
                self._responder(resposta)
        except (ConnectionResetError, BrokenPipeError):
            log(f"conexao perdida  -- {par}")
        finally:
            self.ensaio.encerrar()
            log(f"conexao fechada  -- {par}")

    def _processar(self, linha):
        try:
            msg = json.loads(linha)
        except json.JSONDecodeError:
            log(f"JSON malformado recebido: {linha[:80]!r}")
            return {"command": "UNKNOWN", "status": "ERROR", "message": "JSON malformado"}

        if not isinstance(msg, dict):
            return {"command": "UNKNOWN", "status": "ERROR", "message": "objeto JSON esperado"}

        comando = str(msg.get("command", "")).upper()

        # INJETAR e um comando de CONTROLE, fora do protocolo do ensaio. Fica isolado aqui
        # e e sempre respondido, mesmo com a injecao ativa — senao nao haveria como
        # confirmar o acionamento nem cancelar o silencio depois.
        if comando == "INJETAR":
            segundos = float(msg.get("segundos", 30))
            self.server.injecao.silenciar(segundos)
            log(f"INJETAR -> respostas suspensas por {segundos:.0f}s (teste de robustez)")
            return {"command": "INJETAR", "status": "OK", "segundos": segundos}

        if comando == "CANCELAR_INJECAO":
            self.server.injecao.cancelar()
            log("CANCELAR_INJECAO -> respostas normalizadas")
            return {"command": "CANCELAR_INJECAO", "status": "OK"}

        if self.server.injecao.ativa():
            return None   # engole a resposta: ver o tratamento no metodo handle

        if comando == "START":
            if self.ensaio.start():
                log("START -> ensaio iniciado")
                return {"command": "START", "status": "OK"}
            return {"command": "START", "status": "ERROR", "message": "ensaio ja esta ativo"}

        if comando == "READ":
            volume, vazao, estavel = self.ensaio.read()
            return {
                "command": "READ",
                "volume": round(volume, 3),
                "flowRate": round(vazao, 3),
                "stable": estavel,
            }

        if comando == "STOP":
            if self.ensaio.stop():
                volume, _, _ = self.ensaio.read()
                log(f"STOP  -> ensaio encerrado, volume final {volume:.3f} L")
                return {"command": "STOP", "status": "OK"}
            return {"command": "STOP", "status": "ERROR", "message": "nenhum ensaio ativo"}

        return {"command": comando or "UNKNOWN", "status": "ERROR",
                "message": f"comando desconhecido: {comando}"}

    def _responder(self, obj):
        # separators sem espacos: mensagem compacta, sempre em uma unica linha
        self.wfile.write((json.dumps(obj, separators=(",", ":")) + "\n").encode("utf-8"))
        self.wfile.flush()


class ServidorSimulador(socketserver.ThreadingTCPServer):
    allow_reuse_address = True
    daemon_threads = True

    def __init__(self, endereco, handler, config):
        self.config = config
        self.injecao = InjecaoDeFalhas()   # compartilhada por todas as conexoes
        super().__init__(endereco, handler)


def log(mensagem):
    print(f"[{time.strftime('%H:%M:%S')}] {mensagem}", flush=True)


def main():
    p = argparse.ArgumentParser(description="Simulador do medidor de referencia")
    p.add_argument("--host", default="127.0.0.1", help="endereco de escuta")
    p.add_argument("--porta", type=int, default=5000, help="porta TCP")
    p.add_argument("--vazao", type=float, default=15.0, help="vazao nominal em L/min")
    p.add_argument("--ruido", type=float, default=0.02,
                   help="desvio padrao relativo do ruido de vazao (0.02 = 2%%; 0 desliga)")
    p.add_argument("--tick", type=float, default=0.05, help="periodo de integracao em segundos")
    p.add_argument("--banda-estavel", type=float, default=0.05,
                   help="desvio relativo maximo para stable=true")
    p.add_argument("--aquecimento", type=float, default=2.0,
                   help="segundos de ensaio antes de stable poder ser true")
    p.add_argument("--semente", type=int, default=None,
                   help="semente do gerador aleatorio (reprodutibilidade)")
    cfg = p.parse_args()

    if cfg.vazao <= 0:
        p.error("--vazao deve ser maior que zero")

    with ServidorSimulador((cfg.host, cfg.porta), ManipuladorEnsaio, cfg) as servidor:
        log(f"simulador ouvindo em {cfg.host}:{cfg.porta}")
        log(f"vazao nominal {cfg.vazao} L/min | ruido {cfg.ruido * 100:.1f}% | tick {cfg.tick}s")
        try:
            servidor.serve_forever()
        except KeyboardInterrupt:
            log("encerrando simulador")


if __name__ == "__main__":
    main()
