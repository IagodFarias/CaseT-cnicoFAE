package com.fae.calibracao.measurement;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PulseGeneratorTest {

    private static final double VAZAO_LPM = 15.0;
    private static final double K = 100.0;   // pulsos por litro

    @Test
    void desvioSorteadoFicaDentroDaFaixa() {
        DeviationRange faixa = new DeviationRange(-3.0, 3.0);
        Random rng = new Random(42);
        for (int i = 0; i < 500; i++) {
            PulseGenerator gerador = new PulseGenerator(VAZAO_LPM, K, faixa, 20, rng);
            assertTrue(gerador.desvioPercentual() >= -3.0 && gerador.desvioPercentual() <= 3.0,
                    "desvio fora da faixa: " + gerador.desvioPercentual());
        }
    }

    @Test
    void vazaoEfetivaAplicaODesvio() {
        // faixa degenerada: desvio fixo de +2%, sem aleatoriedade
        PulseGenerator gerador = new PulseGenerator(VAZAO_LPM, K, new DeviationRange(2.0, 2.0), 20, new Random());
        assertEquals(2.0, gerador.desvioPercentual(), 1e-9);
        assertEquals(15.3, gerador.vazaoEfetivaLpm(), 1e-9);
    }

    @Test
    void contagemAcompanhaOTempoReal() throws InterruptedException {
        // desvio zero: a contagem esperada e exatamente vazao/60 * K por segundo
        PulseGenerator gerador = new PulseGenerator(VAZAO_LPM, K, new DeviationRange(0, 0), 20, new Random());
        gerador.start();
        Thread.sleep(1_000);
        gerador.stop();

        double segundos = gerador.duracao().toNanos() / 1_000_000_000.0;
        double esperado = VAZAO_LPM / 60.0 * K * segundos;
        assertEquals(esperado, gerador.pulsos(), 2.0, "contagem divergiu do tempo decorrido");

        // Vmedido = pulsos / K
        assertEquals(gerador.pulsos() / K, gerador.volumeMedidoLitros(), 1e-9);
    }

    @Test
    void contadorCongelaAposOStop() throws InterruptedException {
        PulseGenerator gerador = new PulseGenerator(VAZAO_LPM, K, new DeviationRange(0, 0), 20, new Random());
        gerador.start();
        Thread.sleep(300);
        gerador.stop();

        long congelado = gerador.pulsos();
        Thread.sleep(300);
        assertEquals(congelado, gerador.pulsos(), "contador avancou depois do stop");
        assertTrue(congelado > 0, "nenhum pulso foi gerado");
    }
}
