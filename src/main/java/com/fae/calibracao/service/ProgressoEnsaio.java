package com.fae.calibracao.service;

/**
 * Fotografia do ensaio em andamento, publicada a cada leitura de acompanhamento.
 *
 * Os volumes aqui sao parciais e servem so para exibicao: o volume de referencia do
 * laudo e calculado no fim, como diferenca entre as bordas da janela.
 */
public record ProgressoEnsaio(
        int leitura,
        double tempoDecorridoSegundos,
        double volumeReferenciaParcialLitros,
        long pulsosGerados,
        double volumeMedidoParcialLitros,
        double vazaoInstantaneaLpm,
        boolean estavel) {
}
