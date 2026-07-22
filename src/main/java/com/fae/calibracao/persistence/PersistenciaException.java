package com.fae.calibracao.persistence;

/**
 * Falha ao gravar ou consultar ensaios.
 *
 * Checked porque um ensaio nao gravado nao pode ser tratado como sucesso silencioso:
 * quem chama precisa decidir se avisa o operador, tenta de novo ou aborta. Encapsula
 * as excecoes do Hibernate para que as camadas de cima nao dependam do ORM.
 */
public class PersistenciaException extends Exception {

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}
