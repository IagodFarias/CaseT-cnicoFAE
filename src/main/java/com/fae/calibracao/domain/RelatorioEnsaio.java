package com.fae.calibracao.domain;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Resultado consolidado de um ensaio: tudo o que precisa ser exibido e persistido.
 *
 * Reune as grandezas medidas, as derivadas e os parametros de configuracao vigentes,
 * porque um laudo so e reproduzivel se registrar sob quais condicoes foi obtido.
 * A data/hora aqui e de parede (LocalDateTime) por ser um dado de laudo; quem mede
 * intervalo continua sendo o relogio monotonico da JanelaEnsaio.
 */
public record RelatorioEnsaio(
        LocalDateTime dataHora,
        Duration duracao,
        double pulsosPorLitro,
        double vazaoConfiguradaLpm,
        double volumeReferenciaLitros,
        double volumeMedidoLitros,
        double vazaoMediaLpm,
        double erroPercentual,
        long pulsosGerados,
        double desvioAplicadoPercentual,
        ResultadoEnsaio resultado) {

    public static RelatorioEnsaio de(LocalDateTime dataHora,
                                     Medicao medicao,
                                     double vazaoConfiguradaLpm,
                                     double desvioAplicadoPercentual,
                                     ResultadoEnsaio resultado) {
        return new RelatorioEnsaio(
                dataHora,
                medicao.janela().duracao(),
                medicao.pulsosPorLitro(),
                vazaoConfiguradaLpm,
                medicao.volumeReferenciaLitros(),
                medicao.volumeMedidoLitros(),
                medicao.vazaoMediaLpm(),
                medicao.erroPercentual(),
                medicao.pulsosGerados(),
                desvioAplicadoPercentual,
                resultado);
    }

    public double duracaoSegundos() {
        return duracao.toNanos() / 1_000_000_000.0;
    }
}
