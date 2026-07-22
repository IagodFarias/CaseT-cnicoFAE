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

    private final ModeloEnsaio modelo = new ModeloEnsaio();

    private final TextField campoHost = new TextField("127.0.0.1");
    private final TextField campoPorta = new TextField("5000");
    private final TextField campoVazao = new TextField("15.0");
    private final TextField campoK = new TextField("100");
    private final TextField campoDuracao = new TextField("30");

    private final Button botaoIniciar = new Button("Iniciar ensaio");
    private final Button botaoParar = new Button("Parar");

    private final XYChart.Series<Number, Number> serieReferencia = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> serieMedido = new XYChart.Series<>();

    private final Label rotuloResultado = new Label("aguardando ensaio");
    private final Label rotuloDetalheResultado = new Label("");
    private final VBox painelResultado = new VBox(6);

    /** Compartilhado entre ensaios; aberto uma vez, fora da thread da UI. */
    private volatile EnsaioRepository repositorio;
    /** Thread do ensaio em curso, guardada para o botao Parar poder interrompe-la. */
    private volatile Thread threadEnsaio;

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

        VBox topo = new VBox(4, titulo, subtitulo, espacador(8), form, acoes, indicadores, mensagem);
        topo.setPadding(new Insets(0, 0, 12, 0));
        return topo;
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

        modelo.limparParaNovoEnsaio();
        modelo.emAndamentoProperty().set(true);
        modelo.situacaoEnsaioProperty().set(ModeloEnsaio.Situacao.ATIVO);
        modelo.textoEnsaioProperty().set("iniciando");

        Classificador classificador = new Classificador();
        ConsoleObserver console = new ConsoleObserver(config, classificador.toleranciaPercentual());
        console.imprimirCabecalho();

        // O console continua recebendo tudo: o ObserverComposto entrega os mesmos eventos
        // aos dois, sem que nenhum saiba do outro.
        EnsaioService service = new EnsaioService(config, classificador, new Random(),
                new ObserverComposto(console, new GuiObserver(modelo, config)));

        Thread worker = new Thread(() -> executarEnsaio(service), "ensaio-gui");
        worker.setDaemon(true);
        threadEnsaio = worker;
        worker.start();
    }

    /** Corpo da thread de trabalho. Nada aqui toca em Node: tudo passa pelo GuiObserver. */
    private void executarEnsaio(EnsaioService service) {
        try {
            RelatorioEnsaio relatorio = service.executar();
            persistir(relatorio);
        } catch (EnsaioException e) {
            // Ja publicado como onFalha pelo servico; nada a fazer alem de encerrar.
        } finally {
            Platform.runLater(() -> modelo.emAndamentoProperty().set(false));
            threadEnsaio = null;
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
