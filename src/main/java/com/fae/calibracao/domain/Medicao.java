package com.fae.calibracao.domain;

/**
 * Grandezas brutas de um ensaio e os calculos metrologicos derivados delas.
 *
 * Todos os valores se referem a mesma {@link JanelaEnsaio}: os pulsos foram contados
 * nela, o volume de referencia foi medido como diferenca entre suas bordas e a duracao
 * e o seu comprimento. Ver JanelaEnsaio para o porque dessa exigencia.
 *
 * Imutavel e sem dependencia de rede, threads ou banco: e a camada onde a matematica
 * do ensaio pode ser verificada isoladamente.
 *
 * Unidades: volumes em litros (L), vazao em litros por minuto (L/min), erro em pontos
 * percentuais.
 */
public record Medicao(
        JanelaEnsaio janela,
        double volumeReferenciaLitros,
        long pulsosGerados,
        double pulsosPorLitro) {

    public Medicao {
        if (volumeReferenciaLitros <= 0) {
            // Referencia zero tornaria o erro percentual indefinido (divisao por zero).
            // Na pratica significa ensaio sem escoamento: nao ha o que classificar.
            throw new IllegalArgumentException(
                    "volume de referencia deve ser maior que zero, recebido " + volumeReferenciaLitros);
        }
        if (pulsosPorLitro <= 0) {
            throw new IllegalArgumentException("K deve ser maior que zero, recebido " + pulsosPorLitro);
        }
        if (pulsosGerados < 0) {
            throw new IllegalArgumentException("pulsos gerados nao pode ser negativo");
        }
    }

    /** Vmedido = pulsosGerados / K */
    public double volumeMedidoLitros() {
        return pulsosGerados / pulsosPorLitro;
    }

    /** Vazao media = Vreferencia / tempo do ensaio */
    public double vazaoMediaLpm() {
        return volumeReferenciaLitros / janela.duracaoMinutos();
    }

    /** Erro(%) = (Vmedido - Vreferencia) / Vreferencia x 100 */
    public double erroPercentual() {
        return (volumeMedidoLitros() - volumeReferenciaLitros) / volumeReferenciaLitros * 100.0;
    }

    public double duracaoSegundos() {
        return janela.duracaoSegundos();
    }
}
