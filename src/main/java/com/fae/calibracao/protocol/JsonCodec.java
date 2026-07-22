package com.fae.calibracao.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Traducao entre objetos do protocolo e as linhas JSON que trafegam no socket.
 *
 * Nao conhece socket nem stream: recebe e devolve String. Isso mantem o parsing
 * testavel sem rede e concentra num unico ponto o tratamento de JSON malformado.
 */
public class JsonCodec {

    private final ObjectMapper mapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    /** Serializa o comando em uma linha JSON, sem o terminador de linha. */
    public String encode(Command command) throws ProtocolException {
        try {
            return mapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            throw new ProtocolException("falha ao serializar o comando " + command.command(), e);
        }
    }

    /**
     * Interpreta uma linha recebida do simulador.
     *
     * @throws ProtocolException se a linha nao for um objeto JSON valido — o texto
     *                           recebido entra na mensagem (truncado) para facilitar
     *                           o diagnostico sem inundar o console
     */
    public Response decode(String line) throws ProtocolException {
        if (line == null || line.isBlank()) {
            throw new ProtocolException("linha vazia recebida do simulador");
        }
        try {
            Response response = mapper.readValue(line, Response.class);
            if (response == null) {
                throw new ProtocolException("resposta JSON nula: " + resumir(line));
            }
            return response;
        } catch (JsonProcessingException e) {
            throw new ProtocolException("JSON malformado recebido do simulador: " + resumir(line), e);
        }
    }

    private static String resumir(String line) {
        String limpa = line.strip();
        return limpa.length() <= 120 ? '"' + limpa + '"' : '"' + limpa.substring(0, 120) + "...\"";
    }
}
