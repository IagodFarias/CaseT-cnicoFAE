package com.fae.calibracao.ui;

import com.fae.calibracao.domain.Classificador;
import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.persistence.Ensaio;
import com.fae.calibracao.persistence.EnsaioRepository;
import com.fae.calibracao.persistence.PersistenciaException;
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

        // O banco e aberto ANTES do ensaio: melhor descobrir que ele esta fora agora do
        // que depois de rodar o ensaio inteiro e nao ter onde gravar o laudo.
        try (EnsaioRepository repositorio = abrirRepositorio()) {

            EnsaioService service = new EnsaioService(
                    config, new Classificador(), new Random(), new ConsoleObserver());

            RelatorioEnsaio relatorio;
            try {
                relatorio = service.executar();
            } catch (EnsaioException e) {
                System.err.println();
                System.err.println("[ERRO] ensaio nao concluido: " + e.getMessage());
                System.exit(1);
                return;
            }

            persistir(repositorio, relatorio);
        }
    }

    /** Devolve null (sem persistencia) em vez de abortar: o ensaio ainda tem valor sem banco. */
    private static EnsaioRepository abrirRepositorio() {
        try {
            EnsaioRepository repositorio = new EnsaioRepository();
            System.out.println("[banco  ] conectado");
            System.out.println();
            return repositorio;
        } catch (PersistenciaException e) {
            System.out.println("[banco  ] INDISPONIVEL - o ensaio rodara sem gravar o laudo");
            System.out.println("[banco  ] " + e.getMessage());
            System.out.println("[banco  ] suba o PostgreSQL com: docker compose up -d");
            System.out.println();
            return null;
        }
    }

    private static void persistir(EnsaioRepository repositorio, RelatorioEnsaio relatorio) {
        if (repositorio == null) {
            System.out.println("[banco  ] laudo NAO gravado (banco indisponivel)");
            return;
        }
        try {
            Ensaio gravado = repositorio.salvar(relatorio);
            System.out.println("[banco  ] laudo gravado com id " + gravado.getId()
                    + " (total de ensaios: " + repositorio.contar() + ")");
        } catch (PersistenciaException e) {
            // Falha de gravacao nao derruba a aplicacao: o resultado ja foi exibido.
            System.err.println("[banco  ] falha ao gravar o laudo: " + e.getMessage());
        }
    }
}
