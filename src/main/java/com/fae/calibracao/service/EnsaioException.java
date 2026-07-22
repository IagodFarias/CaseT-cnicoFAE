package com.fae.calibracao.service;

/**
 * O ensaio nao pode ser concluido e nao ha laudo a emitir.
 *
 * Checked de proposito: quem chama o servico e obrigado a decidir o que fazer com um
 * ensaio abortado, em vez de deixar a excecao subir e derrubar a aplicacao.
 */
public class EnsaioException extends Exception {

    public EnsaioException(String message) {
        super(message);
    }

    public EnsaioException(String message, Throwable cause) {
        super(message, cause);
    }
}
