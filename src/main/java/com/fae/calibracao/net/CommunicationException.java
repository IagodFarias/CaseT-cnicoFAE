package com.fae.calibracao.net;

/**
 * Falha no transporte: conexao recusada, timeout de leitura, queda do simulador
 * ou erro de I/O no socket.
 */
public class CommunicationException extends Exception {

    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
