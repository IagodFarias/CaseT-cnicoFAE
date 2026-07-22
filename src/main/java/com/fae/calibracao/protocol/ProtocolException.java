package com.fae.calibracao.protocol;

/**
 * Falha de protocolo: JSON malformado ou resposta que nao corresponde ao comando enviado.
 *
 * Distinta de CommunicationException de proposito — aqui o transporte funcionou,
 * quem esta errado e o conteudo. Sao tratadas de formas diferentes: uma falha de
 * protocolo nao invalida a conexao, uma falha de comunicacao normalmente invalida.
 */
public class ProtocolException extends Exception {

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
}
