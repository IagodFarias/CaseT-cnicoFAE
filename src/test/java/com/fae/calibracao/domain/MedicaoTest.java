package com.fae.calibracao.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Valores escolhidos para que a conta feche "na mao" e possa ser conferida sem calculadora.
 */
class MedicaoTest {

    private static final double K = 100.0;                 // pulsos por litro
    private static final double TOLERANCIA = 1e-9;

    /** Janela de duracao exata, em segundos, a partir de uma origem arbitraria. */
    private static JanelaEnsaio janelaDe(double segundos) {
        long inicio = 1_000_000_000L;
        return new JanelaEnsaio(inicio, inicio + (long) (segundos * 1_000_000_000L));
    }

    @Test
    @DisplayName("Vmedido = pulsos / K")
    void volumeMedido() {
        Medicao m = new Medicao(janelaDe(400), 100.0, 10_200, K);
        assertEquals(102.0, m.volumeMedidoLitros(), TOLERANCIA);
    }

    @Test
    @DisplayName("Vazao media = Vreferencia / tempo: 100 L em 400 s = 15 L/min")
    void vazaoMedia() {
        Medicao m = new Medicao(janelaDe(400), 100.0, 10_200, K);
        assertEquals(15.0, m.vazaoMediaLpm(), TOLERANCIA);
        assertEquals(400.0, m.duracaoSegundos(), 1e-6);
    }

    @Test
    @DisplayName("Erro(%) positivo: 102 L medidos contra 100 L de referencia = +2%")
    void erroPositivo() {
        Medicao m = new Medicao(janelaDe(400), 100.0, 10_200, K);
        assertEquals(2.0, m.erroPercentual(), TOLERANCIA);
    }

    @Test
    @DisplayName("Erro(%) negativo: 98 L medidos contra 100 L de referencia = -2%")
    void erroNegativo() {
        Medicao m = new Medicao(janelaDe(400), 100.0, 9_800, K);
        assertEquals(98.0, m.volumeMedidoLitros(), TOLERANCIA);
        assertEquals(-2.0, m.erroPercentual(), TOLERANCIA);
    }

    @Test
    @DisplayName("Erro(%) zero quando medido e referencia coincidem")
    void erroZero() {
        Medicao m = new Medicao(janelaDe(400), 100.0, 10_000, K);
        assertEquals(0.0, m.erroPercentual(), TOLERANCIA);
    }

    @Test
    @DisplayName("Caso realista: 30 s a 15 L/min com desvio de +1,5% no medidor")
    void casoRealista() {
        // referencia: 15 L/min x 0,5 min = 7,5 L
        // medidor com +1,5%: 7,6125 L -> 761 pulsos (truncados) = 7,61 L
        Medicao m = new Medicao(janelaDe(30), 7.5, 761, K);
        assertEquals(7.61, m.volumeMedidoLitros(), TOLERANCIA);
        assertEquals(15.0, m.vazaoMediaLpm(), TOLERANCIA);
        assertEquals(1.4666666, m.erroPercentual(), 1e-6);
    }

    @Test
    @DisplayName("K diferente nao muda o erro se a contagem escalar junto")
    void kNaoAlteraOErro() {
        Medicao comK100 = new Medicao(janelaDe(400), 100.0, 10_200, 100.0);
        Medicao comK500 = new Medicao(janelaDe(400), 100.0, 51_000, 500.0);
        assertEquals(comK100.erroPercentual(), comK500.erroPercentual(), TOLERANCIA);
    }

    @Test
    @DisplayName("Referencia zero e rejeitada: erro percentual seria indefinido")
    void referenciaZeroRejeitada() {
        assertThrows(IllegalArgumentException.class, () -> new Medicao(janelaDe(400), 0.0, 100, K));
    }

    @Test
    @DisplayName("Janela invertida ou de duracao zero e rejeitada")
    void janelaInvalidaRejeitada() {
        assertThrows(IllegalArgumentException.class, () -> new JanelaEnsaio(2_000, 1_000));
        assertThrows(IllegalArgumentException.class, () -> new JanelaEnsaio(1_000, 1_000));
    }
}
