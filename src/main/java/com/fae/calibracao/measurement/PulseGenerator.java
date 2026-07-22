package com.fae.calibracao.measurement;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.util.random.RandomGenerator;

/**
 * Gerador de pulsos do medidor em teste.
 *
 * Roda numa thread dedicada, emitindo pulsos na cadencia da vazao nominal corrigida
 * pelo desvio proprio do medidor. NAO toca no socket: o unico estado compartilhado
 * com o resto da aplicacao e o contador AtomicLong, lido sem bloqueio por quem quiser.
 *
 * O desvio e independente do ruido do simulador. Como o simulador integra a vazao
 * nominal (com ruido que se cancela na media) e este gerador conta pulsos sobre a
 * vazao nominal x (1 + desvio), o erro final do ensaio converge para o desvio sorteado.
 */
public class PulseGenerator {

    private final double pulsosPorLitro;          // constante K do medidor
    private final double desvioPercentual;        // sorteado uma vez, no construtor
    private final double vazaoEfetivaLpm;         // vazao nominal ja corrigida pelo desvio
    private final double pulsosPorSegundo;
    private final long intervaloMs;

    /**
     * Contador compartilhado entre a thread de pulsos (escreve) e a thread principal (le).
     * AtomicLong porque leitura e escrita concorrentes de long nao sao atomicas na JVM:
     * sem isso, a UI poderia ler metade de um valor sendo escrito.
     */
    private final AtomicLong pulsos = new AtomicLong();

    private volatile Thread thread;
    private volatile boolean ativo;
    private volatile long inicioNanos;
    private volatile long fimNanos;

    public PulseGenerator(double vazaoNominalLpm, double pulsosPorLitro,
                          DeviationRange faixaDesvio, long intervaloMs, RandomGenerator rng) {
        if (vazaoNominalLpm <= 0) {
            throw new IllegalArgumentException("vazao nominal deve ser maior que zero");
        }
        if (pulsosPorLitro <= 0) {
            throw new IllegalArgumentException("K (pulsos por litro) deve ser maior que zero");
        }
        this.pulsosPorLitro = pulsosPorLitro;
        this.intervaloMs = intervaloMs;
        this.desvioPercentual = faixaDesvio.sortear(rng);
        this.vazaoEfetivaLpm = vazaoNominalLpm * (1.0 + desvioPercentual / 100.0);
        this.pulsosPorSegundo = vazaoEfetivaLpm / 60.0 * pulsosPorLitro;
    }

    /** Inicia a thread de pulsos. Chamar duas vezes e erro de programacao, nao de operacao. */
    public synchronized void start() {
        if (thread != null) {
            throw new IllegalStateException("gerador de pulsos ja foi iniciado");
        }
        pulsos.set(0);
        inicioNanos = System.nanoTime();
        fimNanos = 0;
        ativo = true;
        thread = new Thread(this::gerar, "gerador-pulsos");
        thread.setDaemon(true);   // nao impede a JVM de encerrar se o ensaio abortar
        thread.start();
    }

    /** Encerra a thread, contabilizando os pulsos ate o instante exato da parada. */
    public synchronized void stop() {
        Thread t = this.thread;
        if (t == null || !ativo) {
            return;
        }
        ativo = false;
        fimNanos = System.nanoTime();
        atualizarContagem(fimNanos);   // contagem final: nada se perde entre o ultimo tick e o stop
        t.interrupt();
        try {
            t.join(Duration.ofSeconds(2).toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.thread = null;
    }

    private void gerar() {
        while (ativo) {
            try {
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;   // interrupcao so vem do stop(), que ja fez a contagem final
            }
            if (ativo) {
                atualizarContagem(System.nanoTime());
            }
        }
    }

    /**
     * Recalcula o total de pulsos a partir do tempo decorrido desde o inicio.
     *
     * Deliberadamente baseado no tempo TOTAL, e nao em incrementos por tick: Thread.sleep
     * nao e preciso e sempre dorme um pouco a mais, entao somar "pulsos deste tick" a cada
     * iteracao acumularia o erro de cada sleep ao longo do ensaio. Ancorando no instante
     * inicial, o atraso de um tick e absorvido no proximo e a contagem acompanha o tempo real.
     */
    private void atualizarContagem(long agoraNanos) {
        double decorridoSegundos = (agoraNanos - inicioNanos) / 1_000_000_000.0;
        pulsos.set((long) (pulsosPorSegundo * decorridoSegundos));
    }

    /** Total de pulsos gerados ate agora. Seguro para ler de qualquer thread. */
    public long pulsos() {
        return pulsos.get();
    }

    /** Volume medido pelo medidor em teste: Vmedido = pulsos / K. */
    public double volumeMedidoLitros() {
        return pulsos.get() / pulsosPorLitro;
    }

    public double desvioPercentual() {
        return desvioPercentual;
    }

    public double vazaoEfetivaLpm() {
        return vazaoEfetivaLpm;
    }

    public double pulsosPorLitro() {
        return pulsosPorLitro;
    }

    public boolean isRunning() {
        return ativo;
    }

    /**
     * Instante (System.nanoTime) em que a contagem comecou: o t_inicio da janela do ensaio.
     */
    public long inicioNanos() {
        return inicioNanos;
    }

    /**
     * Instante em que a thread foi sinalizada para parar: o t_fim da janela do ensaio.
     * E exatamente o instante usado na contagem final de pulsos, entao os pulsos gerados
     * e a janela sempre concordam. Zero enquanto o gerador nao foi parado.
     */
    public long fimNanos() {
        return fimNanos;
    }

    /** Duracao da geracao: ate agora se ativo, ou o total final se ja parou. */
    public Duration duracao() {
        if (inicioNanos == 0) {
            return Duration.ZERO;
        }
        long fim = (fimNanos != 0) ? fimNanos : System.nanoTime();
        return Duration.ofNanos(fim - inicioNanos);
    }
}
