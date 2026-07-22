package com.fae.calibracao.service;

import com.fae.calibracao.measurement.DeviationRange;

import java.time.Duration;

/**
 * Parametros de um ensaio: conexao, medidor sob teste e cadencias.
 */
public record EnsaioConfig(
        String host,
        int porta,
        int timeoutConexaoMs,
        int timeoutLeituraMs,
        double vazaoNominalLpm,
        double pulsosPorLitro,
        DeviationRange faixaDesvio,
        long intervaloPulsosMs,
        long intervaloLeituraMs,
        Duration duracaoEnsaio) {

    public EnsaioConfig {
        if (vazaoNominalLpm <= 0) {
            throw new IllegalArgumentException("vazao nominal deve ser maior que zero");
        }
        if (pulsosPorLitro <= 0) {
            throw new IllegalArgumentException("K deve ser maior que zero");
        }
        if (duracaoEnsaio == null || duracaoEnsaio.isNegative() || duracaoEnsaio.isZero()) {
            throw new IllegalArgumentException("duracao do ensaio deve ser positiva");
        }
    }

    public static EnsaioConfig padrao(String host, int porta, Duration duracao) {
        return new EnsaioConfig(
                host, porta,
                3_000,                      // timeout de conexao
                5_000,                      // timeout de leitura (setSoTimeout)
                15.0,                       // vazao nominal L/min
                100.0,                      // K: pulsos por litro
                DeviationRange.PADRAO,      // -3% a +3%
                20,                         // periodo da thread de pulsos
                1_000,                      // periodo dos READs de acompanhamento
                duracao);
    }

    public String endereco() {
        return host + ":" + porta;
    }
}
