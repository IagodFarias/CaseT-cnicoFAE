package com.fae.calibracao.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mensagem enviada ao simulador. Serializada como {"command":"START"}.
 */
public record Command(@JsonProperty("command") String command) {

    public static final Command START = new Command("START");
    public static final Command READ = new Command("READ");
    public static final Command STOP = new Command("STOP");

    public Command {
        if (command == null || command.isBlank()) {
            throw new IllegalArgumentException("command nao pode ser vazio");
        }
    }
}
