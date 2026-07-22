package com.fae.calibracao.ui.gui;

import com.fae.calibracao.domain.JanelaEnsaio;
import com.fae.calibracao.domain.Medicao;
import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.service.EnsaioConfig;
import com.fae.calibracao.service.EnsaioObserver;
import com.fae.calibracao.service.ProgressoEnsaio;

import javafx.application.Platform;

/**
 * Ponte entre o ensaio e a tela.
 *
 * <h2>Seguranca de thread — o ponto central desta classe</h2>
 *
 * Os metodos abaixo sao invocados pela thread que executa o ensaio (a que fala TCP com o
 * simulador), NUNCA pela JavaFX Application Thread. O JavaFX, por sua vez, so permite
 * tocar em Node e em propriedades ligadas a Node a partir da sua propria thread — violar
 * isso nao trava na hora, produz corrupcao visual intermitente e IllegalStateException
 * esporadico, que e o pior tipo de bug para diagnosticar.
 *
 * A garantia aqui e por CONSTRUCAO, nao por disciplina espalhada:
 *
 * <ol>
 *   <li>Esta e a unica classe da GUI que o EnsaioService conhece, entao e o unico ponto
 *       por onde dados de outra thread entram na tela.</li>
 *   <li>Todo metodo tem a mesma forma: calcula o que precisar com os argumentos recebidos
 *       (valores imutaveis, sem estado compartilhado) e entrega o resultado ao
 *       {@code Platform.runLater}. Nenhuma propriedade do ModeloEnsaio e tocada fora dele.</li>
 *   <li>Como o ModeloEnsaio so e escrito daqui, e daqui so dentro de runLater, todo o
 *       estado da tela fica confinado a JavaFX Application Thread. Confinamento dispensa
 *       lock: nao ha acesso concorrente a excluir.</li>
 * </ol>
 *
 * Os calculos ficam FORA do runLater de proposito: a fila do JavaFX e a mesma que desenha
 * os quadros, entao o que roda nela atrasa a renderizacao. Dentro do runLater ficam apenas
 * atribuicoes.
 */
public class GuiObserver implements EnsaioObserver {

    private final ModeloEnsaio modelo;
    private final EnsaioConfig config;
    private final double duracaoTotalSegundos;

    /**
     * Marca que o ensaio abortou, para o onDesconectado seguinte pintar a conexao como
     * falha em vez de encerramento normal. Sem sincronizacao porque so e escrito e lido
     * dentro de Platform.runLater — e o runLater preserva a ordem de submissao, entao o
     * onFalha sempre e processado antes do onDesconectado.
     */
    private boolean ensaioAbortado;

    /**
     * Ultimo aviso recebido, guardado porque a mensagem final de aborto nao diz a CAUSA.
     *
     * O servico tolera duas falhas antes de desistir, entao um ensaio morto por timeout
     * termina com "conexao perdida apos 3 leituras falhas" — texto que nao distingue
     * timeout de queda. Quem carrega essa informacao sao os avisos. Combinar os dois e
     * agregacao de eventos que a propria tela recebeu, nao inspecao do interior do servico.
     */
    private String ultimoAviso;

    public GuiObserver(ModeloEnsaio modelo, EnsaioConfig config) {
        this.modelo = modelo;
        this.config = config;
        this.duracaoTotalSegundos = config.duracaoEnsaio().toNanos() / 1_000_000_000.0;
    }

    @Override
    public void onConectando(String endereco) {
        Platform.runLater(() -> {
            modelo.situacaoSimuladorProperty().set(ModeloEnsaio.Situacao.ATIVO);
            modelo.textoSimuladorProperty().set("conectando em " + endereco);
        });
    }

    @Override
    public void onConectado(String endereco) {
        Platform.runLater(() -> {
            modelo.situacaoSimuladorProperty().set(ModeloEnsaio.Situacao.OK);
            modelo.textoSimuladorProperty().set("conectado - " + endereco);
        });
    }

    @Override
    public void onIniciado(double desvioPercentual, double vazaoEfetivaLpm) {
        Platform.runLater(() -> {
            modelo.situacaoEnsaioProperty().set(ModeloEnsaio.Situacao.ATIVO);
            modelo.textoEnsaioProperty().set("em andamento");
            modelo.desvioSorteadoProperty().set(desvioPercentual);
        });
    }

    @Override
    public void onProgresso(ProgressoEnsaio p) {
        // Calculado aqui, na thread do ensaio, para nao ocupar a thread de renderizacao.
        double fracao = Math.min(1.0, p.tempoDecorridoSegundos() / duracaoTotalSegundos);
        double erroParcial = estimarErro(p);
        ModeloEnsaio.Leitura linha = new ModeloEnsaio.Leitura(
                p.leitura(), p.tempoDecorridoSegundos(), p.volumeReferenciaParcialLitros(),
                p.pulsosGerados(), p.volumeMedidoParcialLitros(), p.vazaoInstantaneaLpm(), p.estavel());

        Platform.runLater(() -> {
            modelo.progressoProperty().set(fracao);
            modelo.volumeReferenciaProperty().set(p.volumeReferenciaParcialLitros());
            modelo.volumeMedidoProperty().set(p.volumeMedidoParcialLitros());
            modelo.pulsosProperty().set(p.pulsosGerados());
            modelo.vazaoInstantaneaProperty().set(p.vazaoInstantaneaLpm());
            modelo.erroAtualProperty().set(erroParcial);
            modelo.getLeituras().add(linha);
        });
    }

    /**
     * Erro parcial apenas para acompanhamento.
     *
     * Reusa a formula do dominio (Medicao) em vez de reimplementa-la na tela: se o
     * criterio mudar, muda num lugar so. E parcial porque usa o volume acumulado pelo
     * simulador, e nao a diferenca entre as bordas da janela — o valor oficial do laudo
     * so existe no fim, em onFinalizado.
     */
    private double estimarErro(ProgressoEnsaio p) {
        if (p.volumeReferenciaParcialLitros() <= 0 || p.tempoDecorridoSegundos() <= 0) {
            return 0;
        }
        try {
            JanelaEnsaio janela = new JanelaEnsaio(0L, (long) (p.tempoDecorridoSegundos() * 1_000_000_000L));
            return new Medicao(janela, p.volumeReferenciaParcialLitros(),
                    p.pulsosGerados(), config.pulsosPorLitro()).erroPercentual();
        } catch (IllegalArgumentException e) {
            return 0;   // primeiras leituras podem nao ter volume suficiente ainda
        }
    }

    @Override
    public void onAviso(String mensagem) {
        Platform.runLater(() -> {
            ultimoAviso = mensagem;
            modelo.ultimaMensagemProperty().set("aviso: " + mensagem);
        });
    }

    @Override
    public void onFalha(String mensagem) {
        Platform.runLater(() -> {
            ensaioAbortado = true;
            modelo.situacaoEnsaioProperty().set(ModeloEnsaio.Situacao.ERRO);
            modelo.textoEnsaioProperty().set("interrompido");
            modelo.ultimaMensagemProperty().set("falha: " + mensagem);
            modelo.mensagemFalhaProperty().set(comFimECausa(mensagem));
        });
    }

    /** Junta o motivo do aborto com a causa observada nos avisos, quando houver. */
    private String comFimECausa(String mensagemDeFalha) {
        if (ultimoAviso == null || ultimoAviso.isBlank()) {
            return mensagemDeFalha;
        }
        return mensagemDeFalha + System.lineSeparator() + "Causa observada: " + ultimoAviso;
    }

    @Override
    public void onFinalizado(RelatorioEnsaio relatorio) {
        Platform.runLater(() -> {
            modelo.progressoProperty().set(1.0);
            modelo.volumeReferenciaProperty().set(relatorio.volumeReferenciaLitros());
            modelo.volumeMedidoProperty().set(relatorio.volumeMedidoLitros());
            modelo.pulsosProperty().set(relatorio.pulsosGerados());
            modelo.erroAtualProperty().set(relatorio.erroPercentual());
            modelo.situacaoEnsaioProperty().set(ModeloEnsaio.Situacao.OK);
            modelo.textoEnsaioProperty().set("concluido");
            modelo.resultadoProperty().set(relatorio);
        });
    }

    @Override
    public void onDesconectado(String endereco) {
        Platform.runLater(() -> {
            // Encerramento normal e queda de conexao terminam no mesmo evento; o que os
            // distingue visualmente e se houve falha antes.
            modelo.situacaoSimuladorProperty().set(
                    ensaioAbortado ? ModeloEnsaio.Situacao.ERRO : ModeloEnsaio.Situacao.NEUTRO);
            modelo.textoSimuladorProperty().set(
                    ensaioAbortado ? "conexao perdida" : "desconectado");
        });
    }
}
