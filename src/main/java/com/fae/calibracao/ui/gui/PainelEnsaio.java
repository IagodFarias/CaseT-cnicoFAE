package com.fae.calibracao.ui.gui;

import com.fae.calibracao.domain.Classificador;
import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.domain.ResultadoEnsaio;
import com.fae.calibracao.measurement.DeviationRange;
import com.fae.calibracao.persistence.Ensaio;
import com.fae.calibracao.persistence.EnsaioRepository;
import com.fae.calibracao.persistence.PersistenciaException;
import com.fae.calibracao.service.EnsaioConfig;
import com.fae.calibracao.service.EnsaioException;
import com.fae.calibracao.service.EnsaioService;
import com.fae.calibracao.ui.ConsoleObserver;
import com.fae.calibracao.ui.ObserverComposto;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * Tela principal do ensaio.
 *
 * Responsavel apenas por layout e ligacao com o {@link ModeloEnsaio}. A logica do ensaio
 * vem inteira do EnsaioService — aqui nao ha calculo de erro, criterio de aprovacao nem
 * regra de medicao.
 *
 * O ensaio roda numa thread de trabalho: se fosse disparado na JavaFX Application Thread,
 * a janela congelaria durante todo o ensaio (nao repinta, nao responde ao botao Parar).
 */
public class PainelEnsaio extends BorderPane {

    private static final Color VERDE = Color.web("#1b7f3b");
    private static final Color VERMELHO = Color.web("#b3261e");
    private static final Color AMBAR = Color.web("#b26a00");
    private static final Color CINZA = Color.web("#9aa0a6");

    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM HH:mm:ss");
    private static final int HISTORICO_EXIBIDO = 10;

    /**
     * Duracao do silencio do simulador na injecao de timeout.
     *
     * Maior que 3 timeouts consecutivos (3 x 5 s do setSoTimeout) para garantir que o
     * ensaio aborte, ja que as leituras de acompanhamento toleram duas falhas.
     */
    private static final int SEGUNDOS_DE_SILENCIO = 30;

    private final ModeloEnsaio modelo = new ModeloEnsaio();

    private final TextField campoHost = new TextField("127.0.0.1");
    private final TextField campoPorta = new TextField("5000");
    private final TextField campoVazao = new TextField("15.0");
    private final TextField campoK = new TextField("100");
    private final TextField campoDuracao = new TextField("30");

    private final Button botaoIniciar = new Button("Iniciar ensaio");
    private final Button botaoParar = new Button("Parar");

    private final ComboBox<CenarioFalha> comboFalha = new ComboBox<>();
    private final Button botaoAplicarFalha = new Button("Aplicar");
    private final Label rotuloExplicacaoFalha = new Label();
    private final InjetorDeFalhas injetor = new InjetorDeFalhas();

    private final XYChart.Series<Number, Number> serieReferencia = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> serieMedido = new XYChart.Series<>();

    private final Label rotuloResultado = new Label("aguardando ensaio");
    private final Label rotuloDetalheResultado = new Label("");
    private final VBox painelResultado = new VBox(6);

    /** Compartilhado entre ensaios; aberto uma vez, fora da thread da UI. */
    private volatile EnsaioRepository repositorio;
    /** Thread do ensaio em curso, guardada para o botao Parar poder interrompe-la. */
    private volatile Thread threadEnsaio;
    /** Servico do ensaio em curso, guardado para o botao de queda poder derrubar o socket. */
    private volatile EnsaioService servicoAtivo;
    /** Configuracao do ensaio em curso, usada para alcancar o simulador na injecao de timeout. */
    private volatile EnsaioConfig configuracaoEmUso;
    /** Porta sem servidor armada para o proximo Iniciar; 0 quando nao ha injecao pendente. */
    private int portaRecusa;

    public PainelEnsaio() {
        setPadding(new Insets(14));
        setTop(montarTopo());
        setCenter(montarCentro());
        setRight(montarLateral());
        ligarBotoes();
        abrirBancoEmBackground();
    }

    // ------------------------------------------------------------------ layout

    private Node montarTopo() {
        Label titulo = new Label("Sistema de Calibracao de Medidores de Agua");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));
        Label subtitulo = new Label("Ensaio metrologico contra medidor de referencia via TCP/IP");
        subtitulo.setTextFill(CINZA);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.addRow(0, new Label("Host:"), campoHost, new Label("Porta:"), campoPorta,
                new Label("Vazao (L/min):"), campoVazao);
        form.addRow(1, new Label("K (pulsos/L):"), campoK, new Label("Duracao (s):"), campoDuracao);
        campoHost.setPrefWidth(120);
        campoPorta.setPrefWidth(70);
        campoVazao.setPrefWidth(70);
        campoK.setPrefWidth(70);
        campoDuracao.setPrefWidth(70);

        botaoIniciar.setDefaultButton(true);
        HBox acoes = new HBox(10, botaoIniciar, botaoParar);
        acoes.setPadding(new Insets(6, 0, 0, 0));

        HBox indicadores = new HBox(24,
                indicador(modelo.situacaoSimuladorProperty(), modelo.textoSimuladorProperty(), "Simulador"),
                indicador(modelo.situacaoBancoProperty(), modelo.textoBancoProperty(), "Banco"),
                indicador(modelo.situacaoEnsaioProperty(), modelo.textoEnsaioProperty(), "Ensaio"));
        indicadores.setPadding(new Insets(10, 0, 0, 0));

        Label mensagem = new Label();
        mensagem.textProperty().bind(modelo.ultimaMensagemProperty());
        mensagem.setTextFill(AMBAR);
        mensagem.setWrapText(true);

        VBox topo = new VBox(4, titulo, subtitulo, espacador(8), form, acoes, indicadores,
                mensagem, montarPainelInjecao());
        topo.setPadding(new Insets(0, 0, 12, 0));
        return topo;
    }

    /**
     * Painel de injecao de falhas.
     *
     * Visualmente separado e rotulado como ferramenta de teste porque nao faz parte da
     * operacao do ensaio: existe para demonstrar que o tratamento de excecao funciona,
     * provocando falhas REAIS de rede em vez de exibir mensagens fabricadas.
     */
    private Node montarPainelInjecao() {
        Label titulo = new Label("Injecao de falhas (teste de robustez)");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 12));
        titulo.setTextFill(VERMELHO);

        comboFalha.getItems().setAll(CenarioFalha.values());
        comboFalha.getSelectionModel().select(CenarioFalha.QUEDA_CONEXAO);
        comboFalha.setPrefWidth(220);

        rotuloExplicacaoFalha.setTextFill(CINZA);
        rotuloExplicacaoFalha.setWrapText(true);
        rotuloExplicacaoFalha.setMaxWidth(560);
        atualizarExplicacaoFalha();
        comboFalha.getSelectionModel().selectedItemProperty()
                .addListener((obs, antigo, novo) -> atualizarExplicacaoFalha());

        botaoAplicarFalha.setStyle("-fx-base: #f6d4d2;");

        HBox linha = new HBox(10, comboFalha, botaoAplicarFalha);
        linha.setAlignment(Pos.CENTER_LEFT);

        VBox painel = new VBox(6, titulo, linha, rotuloExplicacaoFalha);
        painel.setPadding(new Insets(10));
        painel.setStyle("-fx-border-color: " + paraHex(VERMELHO) + "; -fx-border-width: 1;"
                + " -fx-border-radius: 6; -fx-border-style: dashed;");
        VBox.setMargin(painel, new Insets(10, 0, 0, 0));
        return painel;
    }

    private void atualizarExplicacaoFalha() {
        CenarioFalha cenario = comboFalha.getValue();
        rotuloExplicacaoFalha.setText(cenario == null ? "" : cenario.explicacao());
    }

    /** Bolinha colorida + rotulo, reagindo a Situacao do modelo. */
    private Node indicador(javafx.beans.property.ObjectProperty<ModeloEnsaio.Situacao> situacao,
                           javafx.beans.property.StringProperty texto, String nome) {
        Circle bolinha = new Circle(7);
        bolinha.fillProperty().bind(Bindings.createObjectBinding(() -> switch (situacao.get()) {
            case OK -> VERDE;
            case ATIVO -> AMBAR;
            case ERRO -> VERMELHO;
            case NEUTRO -> CINZA;
        }, situacao));

        Label rotulo = new Label();
        rotulo.textProperty().bind(Bindings.concat(nome, ": ", texto));
        return new HBox(6, bolinha, rotulo);
    }

    private Node montarCentro() {
        HBox cartoes = new HBox(12,
            cartao("Volume referencia", modelo.volumeReferenciaProperty(), "%.3f L"),
            cartao("Volume medido", modelo.volumeMedidoProperty(), "%.3f L"),
            cartao("Pulsos gerados", modelo.pulsosProperty(), "%d"),
            cartao("Erro atual", modelo.erroAtualProperty(), "%+.3f %%"));
        HBox.setHgrow(cartoes, Priority.ALWAYS);

        ProgressBar barra = new ProgressBar(0);
        barra.progressProperty().bind(modelo.progressoProperty());
        barra.setMaxWidth(Double.MAX_VALUE);
        barra.setPrefHeight(18);
        Label percentual = new Label();
        percentual.textProperty().bind(modelo.progressoProperty().multiply(100).asString("%.0f%%"));
        HBox linhaProgresso = new HBox(10, barra, percentual);
        HBox.setHgrow(barra, Priority.ALWAYS);
        linhaProgresso.setAlignment(Pos.CENTER_LEFT);

        VBox centro = new VBox(12, cartoes, linhaProgresso, montarGrafico(), montarTabelaLeituras());
        centro.setPadding(new Insets(0, 12, 0, 0));
        return centro;
    }

    private Node montarGrafico() {
        NumberAxis eixoX = new NumberAxis();
        eixoX.setLabel("tempo (s)");
        eixoX.setForceZeroInRange(true);
        NumberAxis eixoY = new NumberAxis();
        eixoY.setLabel("volume (L)");

        LineChart<Number, Number> grafico = new LineChart<>(eixoX, eixoY);
        grafico.setTitle("Volume acumulado: referencia x medido");
        grafico.setCreateSymbols(false);
        grafico.setAnimated(false);   // animacao atrapalha a leitura em atualizacao continua
        grafico.setPrefHeight(260);

        serieReferencia.setName("referencia");
        serieMedido.setName("medido");
        grafico.getData().add(serieReferencia);
        grafico.getData().add(serieMedido);

        // A lista so e modificada dentro de Platform.runLater (ver GuiObserver), entao
        // este listener sempre dispara na JavaFX Application Thread e pode tocar no grafico.
        modelo.getLeituras().addListener((ListChangeListener<ModeloEnsaio.Leitura>) mudanca -> {
            while (mudanca.next()) {
                if (mudanca.wasAdded()) {
                    for (ModeloEnsaio.Leitura l : mudanca.getAddedSubList()) {
                        serieReferencia.getData().add(new XYChart.Data<>(l.tempoSegundos(), l.volumeReferencia()));
                        serieMedido.getData().add(new XYChart.Data<>(l.tempoSegundos(), l.volumeMedido()));
                    }
                } else if (mudanca.wasRemoved() && modelo.getLeituras().isEmpty()) {
                    serieReferencia.getData().clear();
                    serieMedido.getData().clear();
                }
            }
        });
        return grafico;
    }

    private Node montarTabelaLeituras() {
        TableView<ModeloEnsaio.Leitura> tabela = new TableView<>(modelo.getLeituras());
        tabela.setPrefHeight(200);
        tabela.getColumns().addAll(List.of(
                coluna("#", 45, l -> l.numero()),
                coluna("tempo (s)", 85, l -> String.format("%.1f", l.tempoSegundos())),
                coluna("Vref (L)", 95, l -> String.format("%.3f", l.volumeReferencia())),
                coluna("pulsos", 80, l -> l.pulsos()),
                coluna("Vmed (L)", 95, l -> String.format("%.3f", l.volumeMedido())),
                coluna("vazao (L/min)", 110, l -> String.format("%.2f", l.vazao())),
                coluna("estavel", 75, l -> l.estavel() ? "sim" : "nao")));
        return tabela;
    }

    private Node montarLateral() {
        rotuloResultado.setFont(Font.font("System", FontWeight.BOLD, 24));
        rotuloDetalheResultado.setWrapText(true);
        painelResultado.getChildren().addAll(rotuloResultado, rotuloDetalheResultado);
        painelResultado.setPadding(new Insets(14));
        painelResultado.setAlignment(Pos.CENTER);
        painelResultado.setStyle(estiloPainel(CINZA));
        painelResultado.setMinHeight(120);

        // O veredito e a unica coisa que muda a aparencia do painel inteiro, entao a
        // reacao fica aqui em vez de espalhada em bindings de cor.
        modelo.resultadoProperty().addListener((obs, antigo, novo) -> atualizarPainelResultado(novo));
        // Falha tem precedencia visual: um ensaio abortado nunca exibe veredito.
        modelo.mensagemFalhaProperty().addListener((obs, antigo, novo) -> {
            if (novo != null && !novo.isBlank()) {
                mostrarInterrupcao(novo);
            }
        });

        TableView<Ensaio> tabelaHistorico = new TableView<>(modelo.getHistorico());
        tabelaHistorico.getColumns().addAll(List.of(
                coluna("id", 45, Ensaio::getId),
                coluna("data/hora", 120, e -> e.getDataHora().format(DATA_HORA)),
                coluna("erro (%)", 80, e -> String.format("%+.3f", e.getErroPercentual())),
                coluna("resultado", 100, e -> e.getResultado().toString())));
        tabelaHistorico.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Ensaio item, boolean vazio) {
                super.updateItem(item, vazio);
                if (vazio || item == null) {
                    setStyle("");
                } else if (item.getResultado() == ResultadoEnsaio.APROVADO) {
                    setStyle("-fx-background-color: #e6f4ea;");
                } else {
                    setStyle("-fx-background-color: #fce8e6;");
                }
            }
        });
        VBox.setVgrow(tabelaHistorico, Priority.ALWAYS);

        Label tituloHistorico = new Label("Historico de ensaios");
        tituloHistorico.setFont(Font.font("System", FontWeight.BOLD, 13));

        VBox lateral = new VBox(10, painelResultado, tituloHistorico, tabelaHistorico);
        lateral.setPrefWidth(400);
        lateral.setMinWidth(400);
        return lateral;
    }

    private void atualizarPainelResultado(RelatorioEnsaio r) {
        if (r == null) {
            rotuloResultado.setText("aguardando ensaio");
            rotuloResultado.setTextFill(CINZA);
            rotuloDetalheResultado.setText("");
            painelResultado.setStyle(estiloPainel(CINZA));
            return;
        }
        boolean aprovado = r.resultado().aprovado();
        Color cor = aprovado ? VERDE : VERMELHO;
        rotuloResultado.setText(r.resultado().toString());
        rotuloResultado.setTextFill(cor);
        rotuloDetalheResultado.setText(String.format(
                "erro %+.3f %%   |   Vref %.3f L   |   Vmed %.3f L%nvazao media %.3f L/min   |   duracao %.2f s",
                r.erroPercentual(), r.volumeReferenciaLitros(), r.volumeMedidoLitros(),
                r.vazaoMediaLpm(), r.duracaoSegundos()));
        painelResultado.setStyle(estiloPainel(cor));
    }

    /** Painel de status para ensaio abortado. Nao ha veredito: nao houve medicao valida. */
    private void mostrarInterrupcao(String motivo) {
        rotuloResultado.setText("ENSAIO INTERROMPIDO");
        rotuloResultado.setTextFill(VERMELHO);
        rotuloDetalheResultado.setText(
                resumirFalha(motivo) + System.lineSeparator()
                        + System.lineSeparator() + motivo + System.lineSeparator()
                        + System.lineSeparator() + "Nenhum laudo foi gravado no banco.");
        painelResultado.setStyle(estiloPainel(VERMELHO));
    }

    /**
     * Manchete especifica por tipo de falha.
     *
     * Classifica pela mensagem que o servico produziu, e nao pelo cenario que a GUI
     * injetou: assim uma queda espontanea de rede — sem injecao nenhuma — recebe o mesmo
     * texto de uma injetada. A mensagem original e sempre exibida logo abaixo, entao a
     * classificacao nunca esconde informacao; ela so destaca.
     */
    private String resumirFalha(String motivo) {
        String texto = motivo == null ? "" : motivo.toLowerCase(java.util.Locale.ROOT);
        if (texto.contains("timeout")) {
            EnsaioConfig config = configuracaoEmUso;
            double segundos = (config == null ? 5_000 : config.timeoutLeituraMs()) / 1000.0;
            return String.format("Timeout: simulador nao respondeu em %.0fs - ensaio interrompido.", segundos);
        }
        if (texto.contains("recusada")) {
            return "Conexao recusada: nao ha simulador escutando no endereco alvo.";
        }
        if (texto.contains("perdida") || texto.contains("encerrou") || texto.contains("nao ha conexao")) {
            return "Conexao perdida com o simulador - ensaio interrompido.";
        }
        return "Ensaio interrompido por falha de comunicacao.";
    }

    private static String estiloPainel(Color borda) {
        return "-fx-border-color: " + paraHex(borda) + "; -fx-border-width: 2; -fx-border-radius: 6;";
    }

    private static String paraHex(Color c) {
        return String.format("#%02X%02X%02X",
                (int) (c.getRed() * 255), (int) (c.getGreen() * 255), (int) (c.getBlue() * 255));
    }

    private Node cartao(String titulo, javafx.beans.value.ObservableValue<? extends Number> valor, String formato) {
        Label rotuloTitulo = new Label(titulo);
        rotuloTitulo.setTextFill(CINZA);
        Label rotuloValor = new Label();
        rotuloValor.setFont(Font.font("System", FontWeight.BOLD, 20));
        rotuloValor.textProperty().bind(Bindings.createStringBinding(
                () -> formato.contains("d")
                        ? String.format(formato, valor.getValue().longValue())
                        : String.format(formato, valor.getValue().doubleValue()),
                valor));

        VBox caixa = new VBox(2, rotuloTitulo, rotuloValor);
        caixa.setPadding(new Insets(10));
        caixa.setStyle("-fx-border-color: #d7d9dd; -fx-border-radius: 6; -fx-background-radius: 6;");
        caixa.setPrefWidth(190);
        HBox.setHgrow(caixa, Priority.ALWAYS);
        return caixa;
    }

    private static <T> TableColumn<T, Object> coluna(String titulo, double largura,
                                                     java.util.function.Function<T, Object> extrator) {
        TableColumn<T, Object> col = new TableColumn<>(titulo);
        col.setPrefWidth(largura);
        // Extrator direto em vez de PropertyValueFactory: funciona com record e nao
        // depende de reflexao sobre nomes de getters.
        col.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(extrator.apply(c.getValue())));
        return col;
    }

    private static Region espacador(double altura) {
        Region r = new Region();
        r.setMinHeight(altura);
        return r;
    }

    // ------------------------------------------------------------- comportamento

    private void ligarBotoes() {
        botaoIniciar.disableProperty().bind(modelo.emAndamentoProperty());
        botaoParar.disableProperty().bind(modelo.emAndamentoProperty().not());
        botaoIniciar.setOnAction(e -> iniciarEnsaio());
        botaoParar.setOnAction(e -> pararEnsaio());

        // Aplicar depende do cenario escolhido, e nao so de haver ensaio: queda e timeout
        // exigem ensaio ativo, recusa de conexao so faz sentido com o ensaio parado.
        botaoAplicarFalha.disableProperty().bind(Bindings.createBooleanBinding(
                () -> {
                    CenarioFalha cenario = comboFalha.getValue();
                    return cenario == null || !cenario.aplicavelCom(modelo.emAndamentoProperty().get());
                },
                comboFalha.valueProperty(), modelo.emAndamentoProperty()));
        botaoAplicarFalha.setOnAction(e -> aplicarFalha());
    }

    /** Dispara o cenario selecionado. Todo I/O sai da Application Thread. */
    private void aplicarFalha() {
        CenarioFalha cenario = comboFalha.getValue();
        if (cenario == null) {
            return;
        }
        switch (cenario) {
            case QUEDA_CONEXAO -> derrubarConexao();
            case TIMEOUT_SIMULADOR -> suspenderRespostasDoSimulador();
            case RECUSA_CONEXAO -> armarRecusaDeConexao();
        }
    }

    /**
     * Fecha o socket em uso, provocando excecao de I/O real na proxima operacao.
     *
     * Retorna imediatamente: TcpClient.derrubarConexao() nao adquire o lock do socket,
     * justamente para nao bloquear a Application Thread enquanto o ensaio le da rede.
     */
    private void derrubarConexao() {
        EnsaioService servico = this.servicoAtivo;
        if (servico != null) {
            modelo.ultimaMensagemProperty().set("queda de conexao provocada pelo operador...");
            servico.derrubarConexao();
        }
    }

    /**
     * Manda o simulador parar de responder, sem fechar a conexao.
     *
     * O comando viaja por uma conexao de controle propria — a do ensaio esta ocupada e
     * protegida por lock, e usa-la aqui congelaria a Application Thread. Por isso tambem o
     * envio acontece numa thread separada: e I/O de rede.
     *
     * O silencio dura mais que 3 timeouts consecutivos para garantir o aborto, e expira
     * sozinho, deixando o simulador utilizavel na demonstracao seguinte.
     */
    private void suspenderRespostasDoSimulador() {
        EnsaioConfig config = configuracaoEmUso;
        if (config == null) {
            return;
        }
        modelo.ultimaMensagemProperty().set("solicitando ao simulador que pare de responder...");
        Thread t = new Thread(() -> {
            try {
                injetor.suspenderRespostas(config.host(), config.porta(), SEGUNDOS_DE_SILENCIO);
                Platform.runLater(() -> modelo.ultimaMensagemProperty().set(
                        "simulador silenciado por " + SEGUNDOS_DE_SILENCIO + "s - aguardando o timeout do cliente"));
            } catch (IOException e) {
                Platform.runLater(() -> modelo.ultimaMensagemProperty().set(
                        "nao foi possivel acionar a injecao no simulador: " + e.getMessage()));
            }
        }, "injecao-timeout");
        t.setDaemon(true);
        t.start();
    }

    /**
     * Arma o proximo Iniciar para mirar uma porta sem servidor.
     *
     * Nao ha simulacao de erro: o connect e feito de verdade contra uma porta onde ninguem
     * escuta, e o ConnectException resultante e legitimo. O alvo desviado fica visivel na
     * mensagem para nao parecer que o ensaio falhou na porta configurada.
     */
    private void armarRecusaDeConexao() {
        try {
            portaRecusa = injetor.portaSemServidor();
            modelo.ultimaMensagemProperty().set(
                    "injecao armada: o proximo ensaio vai mirar a porta " + portaRecusa
                            + " (sem simulador) para provocar recusa de conexao");
        } catch (IOException e) {
            modelo.ultimaMensagemProperty().set("nao foi possivel reservar porta para o teste: " + e.getMessage());
        }
    }

    /**
     * Interrompe a thread do ensaio.
     *
     * O EnsaioService ja trata InterruptedException: ele restaura o flag de interrupcao,
     * para a thread de pulsos, fecha o socket e lanca EnsaioException. Ou seja, parar nao
     * exigiu nenhuma alteracao na camada de servico.
     *
     * Ressalva: a interrupcao so tem efeito imediato durante os Thread.sleep entre
     * leituras — leitura de socket bloqueada nao e interrompivel em Java. Na pratica o
     * ensaio passa a maior parte do tempo dormindo, e o setSoTimeout limita o resto.
     */
    private void pararEnsaio() {
        Thread t = threadEnsaio;
        if (t != null && t.isAlive()) {
            modelo.ultimaMensagemProperty().set("parada solicitada pelo operador...");
            t.interrupt();
        }
    }

    private void iniciarEnsaio() {
        EnsaioConfig config;
        try {
            config = lerFormulario();
        } catch (IllegalArgumentException e) {
            modelo.ultimaMensagemProperty().set("configuracao invalida: " + e.getMessage());
            return;
        }

        // Injecao de recusa armada: desvia este ensaio para uma porta sem servidor e se
        // desarma em seguida, para nao contaminar os ensaios seguintes.
        String avisoInjecao = null;
        if (portaRecusa != 0) {
            config = new EnsaioConfig(config.host(), portaRecusa,
                    config.timeoutConexaoMs(), config.timeoutLeituraMs(),
                    config.vazaoNominalLpm(), config.pulsosPorLitro(), config.faixaDesvio(),
                    config.intervaloPulsosMs(), config.intervaloLeituraMs(), config.duracaoEnsaio());
            avisoInjecao = "injecao de recusa ativa: mirando a porta " + portaRecusa + " (sem simulador)";
            portaRecusa = 0;
        }
        this.configuracaoEmUso = config;

        modelo.limparParaNovoEnsaio();
        // Chamada explicita, e nao via listener de resultadoProperty: depois de um ensaio
        // abortado a propriedade continua null, entao set(null) nao dispara mudanca e o
        // painel vermelho de interrupcao sobreviveria para dentro do ensaio seguinte.
        atualizarPainelResultado(null);
        modelo.emAndamentoProperty().set(true);
        modelo.situacaoEnsaioProperty().set(ModeloEnsaio.Situacao.ATIVO);
        modelo.textoEnsaioProperty().set("iniciando");
        if (avisoInjecao != null) {
            // Depois do limparParaNovoEnsaio, que zera a mensagem.
            modelo.ultimaMensagemProperty().set(avisoInjecao);
        }

        Classificador classificador = new Classificador();
        ConsoleObserver console = new ConsoleObserver(config, classificador.toleranciaPercentual());
        console.imprimirCabecalho();

        // O console continua recebendo tudo: o ObserverComposto entrega os mesmos eventos
        // aos dois, sem que nenhum saiba do outro.
        EnsaioService service = new EnsaioService(config, classificador, new Random(),
                new ObserverComposto(console, new GuiObserver(modelo, config)));

        this.servicoAtivo = service;
        Thread worker = new Thread(() -> executarEnsaio(service), "ensaio-gui");
        worker.setDaemon(true);
        threadEnsaio = worker;
        worker.start();
    }

    /**
     * Corpo da thread de trabalho. Nada aqui toca em Node: tudo passa pelo GuiObserver.
     *
     * A chamada de persistencia esta DENTRO do try, imediatamente apos executar(): se o
     * ensaio abortar, executar() lanca EnsaioException e o fluxo salta direto para o
     * catch, de modo que persistir() nunca chega a ser invocado. Ensaio interrompido nao
     * gera laudo — nem como APROVADO, nem como registro parcial.
     */
    private void executarEnsaio(EnsaioService service) {
        try {
            RelatorioEnsaio relatorio = service.executar();
            persistir(relatorio);
        } catch (EnsaioException e) {
            // Ja publicado como onFalha pelo servico; nada a persistir e nada a fazer
            // alem de encerrar. Nenhuma transacao foi aberta neste caminho.
        } finally {
            Platform.runLater(() -> modelo.emAndamentoProperty().set(false));
            threadEnsaio = null;
            servicoAtivo = null;
        }
    }

    private void persistir(RelatorioEnsaio relatorio) {
        EnsaioRepository repo = this.repositorio;
        if (repo == null) {
            Platform.runLater(() -> modelo.ultimaMensagemProperty()
                    .set("laudo NAO gravado: banco indisponivel"));
            return;
        }
        try {
            Ensaio gravado = repo.salvar(relatorio);
            List<Ensaio> ultimos = repo.listarUltimos(HISTORICO_EXIBIDO);
            Platform.runLater(() -> {
                modelo.ultimaMensagemProperty().set("laudo gravado com id " + gravado.getId());
                modelo.getHistorico().setAll(ultimos);
            });
        } catch (PersistenciaException e) {
            Platform.runLater(() -> modelo.ultimaMensagemProperty()
                    .set("falha ao gravar o laudo: " + e.getMessage()));
        }
    }

    /**
     * Abre o banco fora da JavaFX Application Thread.
     *
     * Criar o EntityManagerFactory leva alguns segundos e faz I/O de rede; feito na thread
     * da UI, a janela abriria congelada.
     */
    private void abrirBancoEmBackground() {
        Thread t = new Thread(() -> {
            try {
                EnsaioRepository repo = new EnsaioRepository();
                List<Ensaio> ultimos = repo.listarUltimos(HISTORICO_EXIBIDO);
                this.repositorio = repo;
                Platform.runLater(() -> {
                    modelo.situacaoBancoProperty().set(ModeloEnsaio.Situacao.OK);
                    modelo.textoBancoProperty().set("conectado");
                    modelo.getHistorico().setAll(ultimos);
                });
            } catch (PersistenciaException e) {
                Platform.runLater(() -> {
                    modelo.situacaoBancoProperty().set(ModeloEnsaio.Situacao.ERRO);
                    modelo.textoBancoProperty().set("indisponivel");
                    modelo.ultimaMensagemProperty().set(
                            "banco indisponivel - os ensaios rodam, mas nao serao gravados: " + e.getMessage());
                });
            }
        }, "abertura-banco");
        t.setDaemon(true);
        t.start();
    }

    private EnsaioConfig lerFormulario() {
        String host = campoHost.getText().trim();
        if (host.isEmpty()) {
            throw new IllegalArgumentException("host nao pode ser vazio");
        }
        int porta = inteiro(campoPorta.getText(), "porta");
        if (porta < 1 || porta > 65535) {
            throw new IllegalArgumentException("porta fora da faixa 1-65535");
        }
        double vazao = decimal(campoVazao.getText(), "vazao");
        double k = decimal(campoK.getText(), "K");
        int duracao = inteiro(campoDuracao.getText(), "duracao");
        if (duracao < 1) {
            throw new IllegalArgumentException("duracao deve ser de pelo menos 1 segundo");
        }
        // Validacoes de dominio (vazao > 0, K > 0) ficam no proprio EnsaioConfig.
        return new EnsaioConfig(host, porta, 3_000, 5_000, vazao, k,
                DeviationRange.PADRAO, 20, 1_000, Duration.ofSeconds(duracao));
    }

    private static int inteiro(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " deve ser um numero inteiro");
        }
    }

    private static double decimal(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " deve ser um numero");
        }
    }

    /** Chamado no encerramento da janela: interrompe o ensaio e fecha o banco. */
    public void encerrar() {
        Thread t = threadEnsaio;
        if (t != null) {
            t.interrupt();
        }
        EnsaioRepository repo = this.repositorio;
        if (repo != null) {
            repo.close();
        }
    }
}
