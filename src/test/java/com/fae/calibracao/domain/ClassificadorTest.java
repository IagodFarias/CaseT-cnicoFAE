package com.fae.calibracao.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassificadorTest {

    private final Classificador classificador = new Classificador();

    @ParameterizedTest(name = "erro {0}% -> {1}")
    @CsvSource({
            "0.0,    APROVADO",
            "1.999,  APROVADO",
            "2.0,    APROVADO",     // limite inclusivo
            "-2.0,   APROVADO",     // limite inclusivo do lado negativo
            "2.001,  REPROVADO",
            "-2.001, REPROVADO",
            "5.0,    REPROVADO",
            "-7.3,   REPROVADO"
    })
    void classificaPeloModuloDoErro(double erro, ResultadoEnsaio esperado) {
        assertEquals(esperado, classificador.classificar(erro));
    }

    @Test
    @DisplayName("Classifica a partir de uma Medicao completa")
    void classificaMedicao() {
        JanelaEnsaio janela = new JanelaEnsaio(0, 30_000_000_000L);

        // +2,00% exatos: 102 L contra 100 L de referencia
        Medicao noLimite = new Medicao(janela, 100.0, 10_200, 100.0);
        assertEquals(ResultadoEnsaio.APROVADO, classificador.classificar(noLimite));

        // +3,00%: fora da tolerancia
        Medicao foraDaFaixa = new Medicao(janela, 100.0, 10_300, 100.0);
        assertEquals(ResultadoEnsaio.REPROVADO, classificador.classificar(foraDaFaixa));
    }

    @Test
    @DisplayName("Tolerancia customizada substitui o padrao de 2%")
    void toleranciaCustomizada() {
        Classificador rigoroso = new Classificador(0.5);
        assertEquals(ResultadoEnsaio.REPROVADO, rigoroso.classificar(1.0));
        assertEquals(ResultadoEnsaio.APROVADO, rigoroso.classificar(0.5));
    }

    @Test
    @DisplayName("Erro nao finito reprova em vez de aprovar por acidente")
    void erroNaoFinitoReprova() {
        assertEquals(ResultadoEnsaio.REPROVADO, classificador.classificar(Double.NaN));
        assertEquals(ResultadoEnsaio.REPROVADO, classificador.classificar(Double.POSITIVE_INFINITY));
    }
}
