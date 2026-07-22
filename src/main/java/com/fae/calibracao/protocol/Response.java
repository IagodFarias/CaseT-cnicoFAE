package com.fae.calibracao.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resposta do simulador.
 *
 * O protocolo tem dois formatos de resposta (status para START/STOP, leitura para READ),
 * representados aqui por um unico tipo com campos opcionais: campos ausentes chegam null.
 * Campos desconhecidos sao ignorados, entao o simulador pode ganhar novos campos sem
 * quebrar o cliente.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Response(
        @JsonProperty("command") String command,
        @JsonProperty("status") String status,
        @JsonProperty("message") String message,
        @JsonProperty("volume") Double volume,
        @JsonProperty("flowRate") Double flowRate,
        @JsonProperty("stable") Boolean stable) {

    public boolean isOk() {
        return "OK".equalsIgnoreCase(status);
    }

    public boolean isError() {
        return "ERROR".equalsIgnoreCase(status);
    }

    /** Texto do erro reportado pelo simulador, ou um padrao quando ele nao detalha. */
    public String errorText() {
        return (message == null || message.isBlank()) ? "erro nao detalhado pelo simulador" : message;
    }

    /**
     * Volume em litros de uma resposta READ.
     *
     * @throws ProtocolException se o campo nao veio — READ sem volume e resposta invalida
     */
    public double requireVolume() throws ProtocolException {
        if (volume == null) {
            throw new ProtocolException("resposta READ sem o campo 'volume'");
        }
        return volume;
    }

    /** Vazao em L/min de uma resposta READ. */
    public double requireFlowRate() throws ProtocolException {
        if (flowRate == null) {
            throw new ProtocolException("resposta READ sem o campo 'flowRate'");
        }
        return flowRate;
    }

    public boolean isStable() {
        return Boolean.TRUE.equals(stable);
    }
}
