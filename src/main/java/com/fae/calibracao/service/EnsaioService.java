package com.fae.calibracao.service;

import com.fae.calibracao.domain.Classificador;
import com.fae.calibracao.domain.JanelaEnsaio;
import com.fae.calibracao.domain.Medicao;
import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.domain.ResultadoEnsaio;
import com.fae.calibracao.measurement.PulseGenerator;
import com.fae.calibracao.net.CommunicationException;
import com.fae.calibracao.net.TcpClient;
import com.fae.calibracao.protocol.Command;
import com.fae.calibracao.protocol.ProtocolException;
import com.fae.calibracao.protocol.Response;

import java.time.LocalDateTime;
import java.util.random.RandomGenerator;

/**
 * Conduz o ensaio metrologico de ponta a ponta.
 *
 * <h2>Sequencia</h2>
 * conecta -> START -> inicia a thread de pulsos -> le a referencia na borda inicial ->
 * READs periodicos de acompanhamento -> para os pulsos -> le a referencia na borda final
 * -> STOP -> calcula, classifica e devolve o laudo.
 *
 * <h2>Por que as duas leituras de borda</h2>
 * O volume de referencia do laudo NAO e o total acumulado pelo simulador, e sim
 * V(t_fim) - V(t_inicio). O total acumulado incluiria o que a referencia mediu antes de
 * t_inicio (entre o simulador processar o START e a thread de pulsos arrancar) e depois
 * de t_fim (ate o STOP ser processado), inflando a referencia e empurrando o erro para
 * baixo em todo ensaio. Ver JanelaEnsaio.
 *
 * As duas bordas usam o MESMO padrao — evento local primeiro, READ logo depois — de modo
 * que a latencia de ida e volta desloca as duas bordas no mesmo sentido e se cancela na
 * subtracao. O que sobra e jitter de rede, aleatorio, e nao vies sistematico.
 */
public class EnsaioService {

    /** Leituras de acompanhamento sao observacionais; algumas podem falhar sem invalidar o ensaio. */
    private static final int FALHAS_CONSECUTIVAS_TOLERADAS = 3;

    private final EnsaioConfig config;
    private final Classificador classificador;
    private final RandomGenerator rng;
    private final EnsaioObserver observer;

    public EnsaioService(EnsaioConfig config, Classificador classificador,
                         RandomGenerator rng, EnsaioObserver observer) {
        this.config = config;
        this.classificador = classificador;
        this.rng = rng;
        this.observer = observer == null ? EnsaioObserver.SILENCIOSO : observer;
    }

    /**
     * Executa um ensaio completo.
     *
     * @return o laudo, sempre com resultado APROVADO ou REPROVADO
     * @throws EnsaioException se o ensaio nao pode ser concluido; a aplicacao segue viva
     *                         e quem chama decide o que fazer
     */
    public RelatorioEnsaio executar() throws EnsaioException {
        LocalDateTime dataHora = LocalDateTime.now();
        PulseGenerator pulsos = new PulseGenerator(
                config.vazaoNominalLpm(), config.pulsosPorLitro(),
                config.faixaDesvio(), config.intervaloPulsosMs(), rng);

        observer.onConectando(config.endereco());

        // try-with-resources: o socket fecha em qualquer saida, inclusive por excecao.
        try (TcpClient cliente = new TcpClient(
                config.host(), config.porta(), config.timeoutConexaoMs(), config.timeoutLeituraMs())) {

            cliente.connect();
            observer.onConectado(config.endereco());

            iniciarNoSimulador(cliente);

            double referenciaInicial;
            try {
                // Ordem deliberada: a thread de pulsos define t_inicio, o READ vem logo
                // depois. Ver o javadoc da classe sobre o cancelamento da latencia.
                pulsos.start();
                observer.onIniciado(pulsos.desvioPercentual(), pulsos.vazaoEfetivaLpm());
                referenciaInicial = lerVolume(cliente, "leitura inicial da referencia");

                acompanhar(cliente, pulsos);
            } finally {
                // A thread de pulsos morre mesmo se o acompanhamento falhar no meio.
                pulsos.stop();
            }

            double referenciaFinal = lerVolume(cliente, "leitura final da referencia");
            encerrarNoSimulador(cliente);

            return consolidar(dataHora, pulsos, referenciaInicial, referenciaFinal);

        } catch (CommunicationException e) {
            throw falha("falha de comunicacao: " + e.getMessage(), e);
        } catch (ProtocolException e) {
            throw falha("falha de protocolo: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw falha("ensaio interrompido antes de terminar", e);
        } finally {
            pulsos.stop();   // idempotente: rede de seguranca contra thread orfa
            observer.onDesconectado(config.endereco());
        }
    }

    private void iniciarNoSimulador(TcpClient cliente) throws CommunicationException, ProtocolException, EnsaioException {
        Response inicio = cliente.send(Command.START);
        if (inicio.isError()) {
            throw falha("simulador recusou o START: " + inicio.errorText(), null);
        }
    }

    private void encerrarNoSimulador(TcpClient cliente) throws CommunicationException, ProtocolException {
        Response fim = cliente.send(Command.STOP);
        if (fim.isError()) {
            // O STOP e limpeza: as medicoes ja foram colhidas, entao isso nao invalida
            // o ensaio — mas o operador precisa saber que o simulador ficou ativo.
            observer.onAviso("simulador recusou o STOP: " + fim.errorText());
        }
    }

    /** READ cujo valor entra no calculo: falhar aqui invalida o ensaio. */
    private double lerVolume(TcpClient cliente, String contexto)
            throws CommunicationException, ProtocolException, EnsaioException {
        Response leitura = cliente.send(Command.READ);
        if (leitura.isError()) {
            throw falha(contexto + " recusada pelo simulador: " + leitura.errorText(), null);
        }
        return leitura.requireVolume();
    }

    /**
     * Leituras periodicas enquanto o ensaio corre.
     *
     * Estas leituras nao entram no calculo — servem para o operador acompanhar. Por isso
     * falhas isoladas viram aviso em vez de aborto: perder um quadro de progresso nao
     * corrompe a medicao, que depende apenas das duas leituras de borda. Falhas
     * consecutivas, porem, indicam conexao morta, e ai nao adianta insistir: a leitura
     * final de borda tambem falharia.
     */
    private void acompanhar(TcpClient cliente, PulseGenerator pulsos)
            throws InterruptedException, CommunicationException, EnsaioException {
        long fimPrevistoNanos = pulsos.inicioNanos() + config.duracaoEnsaio().toNanos();
        int leitura = 0;
        int falhasSeguidas = 0;

        while (System.nanoTime() < fimPrevistoNanos) {
            long restanteNanos = fimPrevistoNanos - System.nanoTime();
            long esperaMs = Math.min(config.intervaloLeituraMs(), Math.max(0, restanteNanos / 1_000_000));
            Thread.sleep(esperaMs);
            if (System.nanoTime() >= fimPrevistoNanos) {
                break;
            }

            leitura++;
            try {
                Response resposta = cliente.send(Command.READ);
                if (resposta.isError()) {
                    observer.onAviso("leitura " + leitura + " recusada: " + resposta.errorText());
                    continue;
                }
                falhasSeguidas = 0;
                observer.onProgresso(new ProgressoEnsaio(
                        leitura,
                        (System.nanoTime() - pulsos.inicioNanos()) / 1_000_000_000.0,
                        resposta.requireVolume(),
                        pulsos.pulsos(),
                        pulsos.volumeMedidoLitros(),
                        resposta.requireFlowRate(),
                        resposta.isStable()));
            } catch (ProtocolException e) {
                // Transporte intacto, conteudo invalido: a conexao segue utilizavel.
                observer.onAviso("leitura " + leitura + " descartada: " + e.getMessage());
            } catch (CommunicationException e) {
                falhasSeguidas++;
                observer.onAviso("leitura " + leitura + " falhou (" + falhasSeguidas + "/"
                        + FALHAS_CONSECUTIVAS_TOLERADAS + "): " + e.getMessage());
                if (falhasSeguidas >= FALHAS_CONSECUTIVAS_TOLERADAS) {
                    throw falha("conexao com " + config.endereco()
                            + " perdida durante o ensaio apos " + falhasSeguidas + " leituras consecutivas falhas", e);
                }
            }
        }
    }

    private RelatorioEnsaio consolidar(LocalDateTime dataHora, PulseGenerator pulsos,
                                       double referenciaInicial, double referenciaFinal) throws EnsaioException {
        double volumeReferencia = referenciaFinal - referenciaInicial;
        if (volumeReferencia <= 0) {
            throw falha(String.format(
                    "referencia nao acumulou volume na janela (inicial %.3f L, final %.3f L): "
                            + "o simulador estava escoando?", referenciaInicial, referenciaFinal), null);
        }

        JanelaEnsaio janela = new JanelaEnsaio(pulsos.inicioNanos(), pulsos.fimNanos());
        Medicao medicao = new Medicao(janela, volumeReferencia, pulsos.pulsos(), pulsos.pulsosPorLitro());
        ResultadoEnsaio resultado = classificador.classificar(medicao);

        RelatorioEnsaio relatorio = RelatorioEnsaio.de(
                dataHora, medicao, config.vazaoNominalLpm(), pulsos.desvioPercentual(), resultado);
        observer.onFinalizado(relatorio);
        return relatorio;
    }

    /** Publica a falha para o observador e devolve a excecao a ser lancada. */
    private EnsaioException falha(String mensagem, Throwable causa) {
        observer.onFalha(mensagem);
        return causa == null ? new EnsaioException(mensagem) : new EnsaioException(mensagem, causa);
    }
}
