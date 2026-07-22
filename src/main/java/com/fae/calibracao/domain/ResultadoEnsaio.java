package com.fae.calibracao.domain;

/**
 * Veredito do ensaio metrologico.
 */
public enum ResultadoEnsaio {

    APROVADO,
    REPROVADO;

    public boolean aprovado() {
        return this == APROVADO;
    }
}
