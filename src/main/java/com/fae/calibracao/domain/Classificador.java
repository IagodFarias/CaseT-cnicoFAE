package com.fae.calibracao.domain;

/**
 * Aplica o criterio de aceitacao do ensaio: APROVADO se |Erro| <= tolerancia.
 *
 * A tolerancia e injetada em vez de fixada em constante porque a classe metrologica
 * do medidor muda o limite aceitavel; o padrao de +/-2% atende o caso deste desafio.
 */
public class Classificador {

    public static final double TOLERANCIA_PADRAO_PERCENTUAL = 2.0;

    private final double toleranciaPercentual;

    public Classificador() {
        this(TOLERANCIA_PADRAO_PERCENTUAL);
    }

    public Classificador(double toleranciaPercentual) {
        if (toleranciaPercentual < 0) {
            throw new IllegalArgumentException("tolerancia nao pode ser negativa");
        }
        this.toleranciaPercentual = toleranciaPercentual;
    }

    /**
     * Classifica pelo erro percentual.
     *
     * O limite e inclusivo: um erro de exatamente +/-2% esta dentro da tolerancia e
     * aprova. Erro NaN ou infinito reprova — sao sintoma de medicao invalida, e o
     * seguro e nao aprovar um medidor com base em conta que nao fechou.
     */
    public ResultadoEnsaio classificar(double erroPercentual) {
        if (!Double.isFinite(erroPercentual)) {
            return ResultadoEnsaio.REPROVADO;
        }
        return Math.abs(erroPercentual) <= toleranciaPercentual
                ? ResultadoEnsaio.APROVADO
                : ResultadoEnsaio.REPROVADO;
    }

    public ResultadoEnsaio classificar(Medicao medicao) {
        return classificar(medicao.erroPercentual());
    }

    public double toleranciaPercentual() {
        return toleranciaPercentual;
    }
}
