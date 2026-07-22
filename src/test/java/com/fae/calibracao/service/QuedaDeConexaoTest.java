package com.fae.calibracao.service;

import com.fae.calibracao.domain.Classificador;
import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.measurement.DeviationRange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre o recurso de "simular queda de conexao" da GUI.
 *
 * O ponto do recurso e que a falha seja REAL: o socket e fechado de verdade e quem reage e
 * o tratamento de excecao normal do EnsaioService. Este teste fixa esse contrato sem
 * depender do simulador Python nem da interface grafica, usando um servidor TCP minimo que
 * fala o protocolo.
 */
class QuedaDeConexaoTest {

    private ServerSocket servidor;
    private Thread threadServidor;

    @BeforeEach
    void subirServidorFalso() throws IOException {
        servidor = new ServerSocket(0);   // porta efemera: evita conflito com o simulador real
        threadServidor = new Thread(this::atender, "servidor-falso");
        threadServidor.setDaemon(true);
        threadServidor.start();
    }

    @AfterEach
    void derrubarServidor() throws IOException {
        servidor.close();
    }

    /** Responde ao protocolo com volume proporcional ao tempo desde o START. */
    private void atender() {
        try (Socket conexao = servidor.accept();
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8));
             Writer out = new OutputStreamWriter(conexao.getOutputStream(), StandardCharsets.UTF_8)) {

            long inicioNanos = 0;
            String linha;
            while ((linha = in.readLine()) != null) {
                if (linha.contains("START")) {
                    inicioNanos = System.nanoTime();
                    out.write("{\"command\":\"START\",\"status\":\"OK\"}\n");
                } else if (linha.contains("READ")) {
                    double segundos = (System.nanoTime() - inicioNanos) / 1_000_000_000.0;
                    double volume = 15.0 / 60.0 * segundos;   // 15 L/min
                    out.write(String.format(java.util.Locale.ROOT,
                            "{\"command\":\"READ\",\"volume\":%.4f,\"flowRate\":15.0,\"stable\":true}%n", volume));
                } else if (linha.contains("STOP")) {
                    out.write("{\"command\":\"STOP\",\"status\":\"OK\"}\n");
                }
                out.flush();
            }
        } catch (IOException e) {
            // Esperado quando o cliente derruba a conexao: encerra o atendimento.
        }
    }

    private EnsaioConfig config() {
        return new EnsaioConfig("127.0.0.1", servidor.getLocalPort(),
                2_000, 3_000,          // timeouts
                15.0, 100.0,           // vazao, K
                new DeviationRange(0, 0),
                20,                    // periodo dos pulsos
                200,                   // leituras rapidas: o aborto ocorre apos 3 falhas
                Duration.ofSeconds(20));
    }

    @Test
    @DisplayName("Derrubar a conexao aborta o ensaio com falha real, sem produzir laudo")
    void quedaDeConexaoAbortaSemLaudo() throws InterruptedException {
        CountDownLatch emAndamento = new CountDownLatch(1);
        AtomicReference<RelatorioEnsaio> laudo = new AtomicReference<>();
        AtomicReference<EnsaioException> falha = new AtomicReference<>();
        AtomicReference<String> mensagemFalha = new AtomicReference<>();

        EnsaioService service = new EnsaioService(config(), new Classificador(), new Random(),
                new EnsaioObserver() {
                    @Override
                    public void onProgresso(ProgressoEnsaio progresso) {
                        emAndamento.countDown();   // ensaio realmente rodando
                    }

                    @Override
                    public void onFalha(String mensagem) {
                        mensagemFalha.set(mensagem);
                    }
                });

        Thread ensaio = new Thread(() -> {
            try {
                laudo.set(service.executar());
            } catch (EnsaioException e) {
                falha.set(e);
            }
        }, "ensaio-teste");
        ensaio.start();

        assertTrue(emAndamento.await(10, TimeUnit.SECONDS), "o ensaio nao chegou a comecar");

        service.derrubarConexao();

        ensaio.join(TimeUnit.SECONDS.toMillis(15));
        assertFalse(ensaio.isAlive(), "o ensaio nao terminou apos a queda");

        // 1. Nao ha laudo: nada seria persistido neste caminho.
        assertNull(laudo.get(), "um ensaio abortado nao pode produzir laudo");

        // 2. A falha e propagada como EnsaioException, obrigando quem chama a tratar.
        assertNotNull(falha.get(), "a queda deveria ter lancado EnsaioException");
        assertNotNull(mensagemFalha.get(), "a falha deveria ter sido publicada ao observador");

        // 3. A thread de pulsos foi encerrada: nenhuma thread orfa sobrevive a queda.
        assertFalse(existeThreadDePulsos(), "a thread de pulsos continuou viva apos a queda");
    }

    @Test
    @DisplayName("Derrubar sem ensaio ativo nao lanca nada")
    void derrubarSemEnsaioAtivoEInocuo() {
        EnsaioService service = new EnsaioService(config(), new Classificador(), new Random(),
                EnsaioObserver.SILENCIOSO);
        service.derrubarConexao();   // nao deve lancar
    }

    private static boolean existeThreadDePulsos() {
        return Thread.getAllStackTraces().keySet().stream()
                .anyMatch(t -> "gerador-pulsos".equals(t.getName()) && t.isAlive());
    }
}
