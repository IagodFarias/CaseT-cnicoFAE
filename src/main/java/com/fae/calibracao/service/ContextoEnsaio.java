package com.fae.calibracao.service;

import org.apache.logging.log4j.ThreadContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Identidade de rastreamento de um ensaio no log.
 *
 * Gera um ensaioId unico e o publica no ThreadContext (o MDC do Log4j2), de onde o layout
 * dos arquivos o le via {@code %X{ensaioId}}. Com isso TODAS as linhas de log daquele
 * ensaio — do EnsaioService, da thread de pulsos e da persistencia — carregam o mesmo id,
 * e um ensaio inteiro pode ser filtrado depois com um simples grep.
 *
 * <p>Uso obrigatorio em try-with-resources, para que o id seja removido do MDC ao final e
 * nao contamine ensaios seguintes que rodem na mesma thread:
 *
 * <pre>{@code
 * try (ContextoEnsaio ctx = ContextoEnsaio.abrir()) {
 *     RelatorioEnsaio laudo = service.executar();
 *     persistir(laudo);
 * }
 * }</pre>
 *
 * <p>Abrange executar() E a persistencia de proposito: gravar o laudo faz parte do ensaio,
 * entao a falha ao persistir tambem deve sair marcada com o ensaioId.
 */
public final class ContextoEnsaio implements AutoCloseable {

    /** Chave do MDC; o mesmo nome usado em %X{ensaioId} no log4j2.xml. */
    public static final String CHAVE = "ensaioId";

    private static final DateTimeFormatter CARIMBO = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    /** Desempata ids gerados no mesmo segundo (ex.: ensaios em sequencia rapida na GUI). */
    private static final AtomicInteger SEQUENCIA = new AtomicInteger();

    private ContextoEnsaio() {
    }

    /** Gera um ensaioId, publica-o no MDC da thread atual e devolve o escopo a ser fechado. */
    public static ContextoEnsaio abrir() {
        ThreadContext.put(CHAVE, gerarId());
        return new ContextoEnsaio();
    }

    /** ensaioId da thread atual, ou null se nenhum ensaio esta em curso nela. */
    public static String atual() {
        return ThreadContext.get(CHAVE);
    }

    private static String gerarId() {
        int seq = Math.floorMod(SEQUENCIA.incrementAndGet(), 1000);
        return LocalDateTime.now().format(CARIMBO) + String.format("-%03d", seq);
    }

    @Override
    public void close() {
        ThreadContext.remove(CHAVE);
    }
}
