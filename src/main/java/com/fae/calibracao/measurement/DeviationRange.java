package com.fae.calibracao.measurement;

import java.util.random.RandomGenerator;

/**
 * Faixa de desvio do medidor em teste, em pontos percentuais (ex: -3.0 a +3.0).
 *
 * Representa a imprecisao de fabricacao do medidor sob ensaio: cada unidade tem o seu
 * desvio, fixo enquanto o ensaio dura. E por isso que o desvio e sorteado UMA vez, no
 * inicio do ensaio, e nao a cada pulso — um desvio reamostrado continuamente se
 * cancelaria na media e o erro final tenderia a zero, tornando o ensaio trivial.
 */
public record DeviationRange(double minPercent, double maxPercent) {

    /** Faixa tipica de um medidor domestico dentro da classe metrologica. */
    public static final DeviationRange PADRAO = new DeviationRange(-3.0, 3.0);

    public DeviationRange {
        if (minPercent > maxPercent) {
            throw new IllegalArgumentException(
                    "faixa invalida: min (" + minPercent + ") maior que max (" + maxPercent + ")");
        }
    }

    /** Sorteia um desvio uniforme dentro da faixa, em pontos percentuais. */
    public double sortear(RandomGenerator rng) {
        if (minPercent == maxPercent) {
            return minPercent;
        }
        return rng.nextDouble(minPercent, maxPercent);
    }

    @Override
    public String toString() {
        return String.format("%+.2f%% a %+.2f%%", minPercent, maxPercent);
    }
}
