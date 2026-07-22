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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * Ponto de entrada do software de calibracao.
 *
 * Uso: java -jar calibracao.jar [host] [porta] [duracaoSegundos]
 */
public class ConsoleApp {

    private static final String HOST_PADRAO = "127.0.0.1";
    private static final int PORTA_PADRAO = 5000;
    private static final int DURACAO_PADRAO_S = 30;
    private static final int HISTORICO_EXIBIDO = 5;

    private static final DateTimeFormatter DATA_HORA_CURTA = DateTimeFormatter.ofPattern("dd/MM HH:mm:ss");

    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equals("-h") || args[0].equals("--help"))) {
            imprimirAjuda();
            return;
        }

        EnsaioConfig config;
        try {
            config = lerConfiguracao(args);
        } catch (IllegalArgumentException e) {
            System.err.println("[ERRO] " + e.getMessage());
            imprimirAjuda();
            System.exit(2);
            return;
        }

        Classificador classificador = new Classificador();
        ConsoleObserver observer = new ConsoleObserver(config, classificador.toleranciaPercentual());
        observer.imprimirCabecalho();

        // O banco e aberto ANTES do ensaio: melhor descobrir que ele esta fora agora do
        // que depois de rodar o ensaio inteiro e nao ter onde gravar o laudo.
        try (EnsaioRepository repositorio = abrirRepositorio(observer)) {

            EnsaioService service = new EnsaioService(config, classificador, new Random(), observer);

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

    private static EnsaioConfig lerConfiguracao(String[] args) {
        String host = args.length > 0 ? args[0] : HOST_PADRAO;
        int porta = inteiro(args, 1, PORTA_PADRAO, "porta");
        int duracao = inteiro(args, 2, DURACAO_PADRAO_S, "duracao em segundos");
        if (porta < 1 || porta > 65535) {
            throw new IllegalArgumentException("porta fora da faixa valida (1-65535): " + porta);
        }
        if (duracao < 1) {
            throw new IllegalArgumentException("duracao deve ser de pelo menos 1 segundo");
        }
        return EnsaioConfig.padrao(host, porta, Duration.ofSeconds(duracao));
    }

    private static int inteiro(String[] args, int indice, int padrao, String nome) {
        if (args.length <= indice) {
            return padrao;
        }
        try {
            return Integer.parseInt(args[indice]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nome + " deve ser um numero inteiro, recebido: " + args[indice]);
        }
    }

    /** Devolve null (sem persistencia) em vez de abortar: o ensaio ainda tem valor sem banco. */
    private static EnsaioRepository abrirRepositorio(ConsoleObserver observer) {
        try {
            EnsaioRepository repositorio = new EnsaioRepository();
            observer.onBancoConectado();
            return repositorio;
        } catch (PersistenciaException e) {
            observer.onBancoIndisponivel(e.getMessage());
            return null;
        }
    }

    private static void persistir(EnsaioRepository repositorio, RelatorioEnsaio relatorio) {
        if (repositorio == null) {
            System.out.println("  [banco  ] laudo NAO gravado (banco indisponivel)");
            return;
        }
        try {
            Ensaio gravado = repositorio.salvar(relatorio);
            System.out.println("  [banco  ] laudo gravado com id " + gravado.getId());
            imprimirHistorico(repositorio);
        } catch (PersistenciaException e) {
            // Falha de gravacao nao derruba a aplicacao: o resultado ja foi exibido.
            System.err.println("  [banco  ] falha ao gravar o laudo: " + e.getMessage());
        }
    }

    private static void imprimirHistorico(EnsaioRepository repositorio) throws PersistenciaException {
        List<Ensaio> ultimos = repositorio.listarUltimos(HISTORICO_EXIBIDO);
        System.out.println();
        System.out.println("  Ultimos ensaios registrados (total: " + repositorio.contar() + ")");
        // Mesmas larguras do formato das linhas, para o cabecalho nao sair torto
        System.out.printf("  %4s  %-14s  %7s  %9s  %s%n", "id", "data/hora", "dur(s)", "erro(%)", "resultado");
        for (Ensaio e : ultimos) {
            System.out.printf("  %4d  %-14s  %7.2f  %+9.3f  %s%n",
                    e.getId(), e.getDataHora().format(DATA_HORA_CURTA), e.getDuracaoSegundos(),
                    e.getErroPercentual(), e.getResultado());
        }
    }

    private static void imprimirAjuda() {
        System.out.println("Uso: java -jar calibracao.jar [host] [porta] [duracaoSegundos]");
        System.out.println();
        System.out.println("  host             endereco do simulador   (padrao " + HOST_PADRAO + ")");
        System.out.println("  porta            porta TCP do simulador  (padrao " + PORTA_PADRAO + ")");
        System.out.println("  duracaoSegundos  duracao do ensaio       (padrao " + DURACAO_PADRAO_S + ")");
        System.out.println();
        System.out.println("Conexao com o banco: DB_URL, DB_USER e DB_PASSWORD sobrescrevem o persistence.xml.");
    }
}
