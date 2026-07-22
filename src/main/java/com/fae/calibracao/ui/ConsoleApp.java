package com.fae.calibracao.ui;

import com.fae.calibracao.domain.Classificador;
import com.fae.calibracao.service.EnsaioConfig;
import com.fae.calibracao.service.EnsaioException;
import com.fae.calibracao.service.EnsaioService;

import java.time.Duration;
import java.util.Random;

/**
 * Ponto de entrada do software de calibracao.
 *
 * Uso: java -jar calibracao.jar [host] [porta] [duracaoSegundos]
 */
public class ConsoleApp {

    private static final String HOST_PADRAO = "127.0.0.1";
    private static final int PORTA_PADRAO = 5000;
    private static final int DURACAO_PADRAO_S = 10;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : HOST_PADRAO;
        int porta;
        int duracaoSegundos;
        try {
            porta = args.length > 1 ? Integer.parseInt(args[1]) : PORTA_PADRAO;
            duracaoSegundos = args.length > 2 ? Integer.parseInt(args[2]) : DURACAO_PADRAO_S;
        } catch (NumberFormatException e) {
            System.err.println("[ERRO] argumentos invalidos. Uso: [host] [porta] [duracaoSegundos]");
            return;
        }

        EnsaioConfig config = EnsaioConfig.padrao(host, porta, Duration.ofSeconds(duracaoSegundos));

        System.out.println("=== Software de Calibracao de Medidores de Agua ===");
        System.out.printf("alvo %s | vazao %.2f L/min | K %.0f pulsos/L | duracao %d s%n",
                config.endereco(), config.vazaoNominalLpm(), config.pulsosPorLitro(), duracaoSegundos);
        System.out.println();

        EnsaioService service = new EnsaioService(
                config, new Classificador(), new Random(), new ConsoleObserver());

        try {
            service.executar();
        } catch (EnsaioException e) {
            // O observador ja detalhou a falha; aqui so encerramos com codigo de erro.
            System.err.println();
            System.err.println("[ERRO] ensaio nao concluido: " + e.getMessage());
            System.exit(1);
        }
    }
}
