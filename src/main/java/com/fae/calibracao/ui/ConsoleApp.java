package com.fae.calibracao.ui;

import com.fae.calibracao.measurement.DeviationRange;
import com.fae.calibracao.measurement.PulseGenerator;
import com.fae.calibracao.net.CommunicationException;
import com.fae.calibracao.net.TcpClient;
import com.fae.calibracao.protocol.Command;
import com.fae.calibracao.protocol.ProtocolException;
import com.fae.calibracao.protocol.Response;

import java.util.Random;

/**
 * Ponto de entrada do software de calibracao.
 *
 * Etapa 3: duas threads em paralelo — a principal conversa com o simulador (START,
 * READs, STOP) e a de pulsos conta a vazao do medidor em teste. O calculo do erro e
 * a classificacao ficam para a Etapa 4.
 */
public class ConsoleApp {

    private static final String HOST_PADRAO = "127.0.0.1";
    private static final int PORTA_PADRAO = 5000;
    private static final int TIMEOUT_CONEXAO_MS = 3_000;
    private static final int TIMEOUT_LEITURA_MS = 5_000;

    private static final double VAZAO_NOMINAL_LPM = 15.0;
    private static final double PULSOS_POR_LITRO = 100.0;    // constante K
    private static final long INTERVALO_PULSOS_MS = 20;
    private static final int LEITURAS = 5;
    private static final long INTERVALO_LEITURA_MS = 1_000;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : HOST_PADRAO;
        int porta = args.length > 1 ? Integer.parseInt(args[1]) : PORTA_PADRAO;

        PulseGenerator pulsos = new PulseGenerator(
                VAZAO_NOMINAL_LPM, PULSOS_POR_LITRO, DeviationRange.PADRAO, INTERVALO_PULSOS_MS, new Random());

        System.out.println("=== Software de Calibracao de Medidores de Agua ===");
        System.out.println("Etapa 3 - geracao de pulsos em thread dedicada");
        System.out.println();
        System.out.printf("vazao nominal   : %.2f L/min%n", VAZAO_NOMINAL_LPM);
        System.out.printf("K               : %.0f pulsos/L%n", PULSOS_POR_LITRO);
        System.out.printf("faixa de desvio : %s%n", DeviationRange.PADRAO);
        System.out.printf("desvio sorteado : %+.3f%% (vazao efetiva %.3f L/min)%n",
                pulsos.desvioPercentual(), pulsos.vazaoEfetivaLpm());
        System.out.println();

        try (TcpClient cliente = new TcpClient(host, porta, TIMEOUT_CONEXAO_MS, TIMEOUT_LEITURA_MS)) {
            System.out.println("[conexao] conectando em " + cliente.endereco() + " ...");
            cliente.connect();
            System.out.println("[conexao] CONECTADO");

            Response inicio = cliente.send(Command.START);
            if (inicio.isError()) {
                System.out.println("[START] recusado pelo simulador: " + inicio.errorText());
                return;
            }
            System.out.println("[START] status=OK - ensaio iniciado");
            pulsos.start();   // a partir daqui rodam duas threads
            System.out.println();

            try {
                for (int i = 1; i <= LEITURAS; i++) {
                    Thread.sleep(INTERVALO_LEITURA_MS);
                    Response leitura = cliente.send(Command.READ);
                    if (leitura.isError()) {
                        System.out.println("[READ ] recusado: " + leitura.errorText());
                        continue;
                    }
                    // A thread principal le o contador sem bloquear a thread de pulsos.
                    System.out.printf("[READ ] %d/%d  Vref=%7.3f L   pulsos=%6d   Vmed=%7.3f L   estavel=%s%n",
                            i, LEITURAS, leitura.requireVolume(), pulsos.pulsos(),
                            pulsos.volumeMedidoLitros(), leitura.isStable());
                }
            } finally {
                // Para os pulsos antes do STOP: as duas medicoes tem que fechar
                // a janela de tempo no mesmo ponto, senao o erro sai viesado.
                pulsos.stop();
            }

            Response fim = cliente.send(Command.STOP);
            System.out.println("[STOP ] status=" + (fim.isError() ? "ERROR - " + fim.errorText() : fim.status()));

            System.out.println();
            System.out.printf("duracao         : %.2f s%n", pulsos.duracao().toNanos() / 1e9);
            System.out.printf("pulsos gerados  : %d%n", pulsos.pulsos());
            System.out.printf("volume medido   : %.3f L  (pulsos / K)%n", pulsos.volumeMedidoLitros());

        } catch (CommunicationException e) {
            System.err.println("[ERRO comunicacao] " + e.getMessage());
        } catch (ProtocolException e) {
            System.err.println("[ERRO protocolo] " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[INTERROMPIDO] execucao cancelada");
        } catch (NumberFormatException e) {
            System.err.println("[ERRO argumento] porta invalida: " + args[1]);
        } finally {
            pulsos.stop();   // rede de seguranca: nenhuma thread sobrevive a uma falha
        }

        System.out.println();
        System.out.println("[conexao] DESCONECTADO");
    }
}
