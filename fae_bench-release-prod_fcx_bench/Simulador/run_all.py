#!/usr/bin/env python3
# -*- coding: ascii -*-
"""
Ponto de entrada comum: sobe a BCI e os 20 medidores no MESMO processo.

O motivo de rodarem juntos e o acoplamento da vazao: o BenchState da BCI e a
unica fonte da verdade sobre a vazao corrente, e os medidores precisam produzir
tempos de transito coerentes com ela NO MESMO INSTANTE. Se a app manda
setFlowRate(2500) para a BCI, os 20 medidores tem de passar a reportar ~2500 L/h
imediatamente -- senao a constante K sai errada e o erro de verificacao estoura.

Aqui o flow_source dos medidores le direto o BenchState.flow_current da BCI.

Uso:
    python run_all.py
    python run_all.py --faults              # liga a injecao de falha nos dois
    python run_all.py --bci-port 8888
"""

import argparse
import json
import logging
import os
import sys
import time

import bci_simulator as bci
import meter_simulator as met

LOG = logging.getLogger("run_all")


def main():
    aqui = os.path.dirname(os.path.abspath(__file__))
    ap = argparse.ArgumentParser(description="Sobe BCI + 20 medidores juntos")
    ap.add_argument("--bci-config", default=os.path.join(aqui, "bci_config.json"))
    ap.add_argument("--meter-config", default=os.path.join(aqui, "meter_config.json"))
    ap.add_argument("--host", default=None)
    ap.add_argument("--bci-port", type=int, default=None)
    ap.add_argument("--log-dir", default=None)
    ap.add_argument("--log-level", default=None)
    ap.add_argument("--faults", action="store_true")
    args = ap.parse_args()

    cfg_bci = bci.load_config(args.bci_config)
    cfg_met = met.load_config(args.meter_config)

    host = args.host or cfg_bci.get("host", "127.0.0.1")
    porta_bci = args.bci_port or int(cfg_bci.get("port", 8888))
    log_dir = args.log_dir or cfg_bci.get("log_dir", "logs_simulador")
    nivel = args.log_level or cfg_bci.get("log_level", "INFO")

    log_path = met.setup_logging(log_dir, nivel)

    fault_bci = dict(cfg_bci.get("fault_injection", {}))
    fault_met = dict(cfg_met.get("fault_injection", {}))
    if args.faults:
        fault_bci["enabled"] = True
        fault_met["enabled"] = True
        cfg_met["fault_injection"] = fault_met

    # ---- BCI -------------------------------------------------------------
    estado = bci.BenchState(cfg_bci.get("bench", {}))
    falhas_bci = bci.FaultInjector(fault_bci)
    servidor_bci = bci.BciServer((host, porta_bci), estado, falhas_bci)

    import threading
    t = threading.Thread(target=servidor_bci.serve_forever, name="bci", daemon=True)
    t.start()

    # ---- Medidores, acoplados a vazao da BCI ------------------------------
    def vazao_corrente():
        """Vazao que a BCI esta entregando neste instante, em L/h.
        _integrate() avanca o modelo de primeira ordem em direcao ao setpoint."""
        with estado.lock:
            estado._integrate()
            return estado.flow_current

    cfg_met["host"] = host
    fazenda = met.MeterFarm(cfg_met, vazao_corrente)
    fazenda.start()

    base = int(cfg_met.get("base_port", 2051))
    n = len(fazenda.meters)
    LOG.info("=" * 78)
    LOG.info("SIMULADOR COMPLETO NO AR")
    LOG.info("  BCI ........ %s:%d   (socketConfig.xml socketId=\"si\")", host, porta_bci)
    LOG.info("  Medidores .. %s:%d..%d  (conversmodel CONVERSOR1..CONVERSOR%d)",
             host, base, base + n - 1, n)
    LOG.info("  Vazao dos medidores acoplada ao BenchState da BCI")
    LOG.info("  Injecao de falha: BCI=%s  Medidores=%s",
             "ATIVA" if falhas_bci.enabled else "desligada",
             "ATIVA" if fazenda.faults.enabled else "desligada")
    LOG.info("  Log: %s", os.path.abspath(log_path))
    LOG.info("=" * 78)

    try:
        while True:
            time.sleep(30)
            with estado.lock:
                estado._integrate()
                q = estado.flow_current
            pacotes = sum(m.packets_sent for m in fazenda.meters)
            LOG.info("[status] vazao=%.1f L/h | linha=%s | pacotes enviados=%d",
                     q, estado.line_state, pacotes)
    except KeyboardInterrupt:
        LOG.info("Interrompido pelo usuario.")
    finally:
        fazenda.stop()
        servidor_bci.shutdown()
        servidor_bci.server_close()
    return 0


if __name__ == "__main__":
    sys.exit(main())
