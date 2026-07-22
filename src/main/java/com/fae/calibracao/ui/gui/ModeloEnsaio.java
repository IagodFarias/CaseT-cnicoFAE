package com.fae.calibracao.ui.gui;

import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.persistence.Ensaio;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Estado observavel da tela.
 *
 * A tela nao le nada do ensaio diretamente: ela se liga a estas propriedades, e quem as
 * atualiza e o {@link GuiObserver}. Isso inverte a dependencia — o painel nao conhece o
 * EnsaioService, so conhece este modelo.
 *
 * <h2>Regra de thread</h2>
 * Todo o acesso a esta classe acontece na JavaFX Application Thread. Nao ha sincronizacao
 * aqui de proposito: ela e desnecessaria porque existe um unico ponto de escrita, o
 * GuiObserver, e ele so escreve de dentro de Platform.runLater. Colocar locks aqui daria
 * uma falsa sensacao de seguranca — o que protege este estado e o confinamento a uma
 * unica thread, nao exclusao mutua.
 */
public class ModeloEnsaio {

    /** Semantica visual de um indicador de estado. A cor concreta e escolhida pela tela. */
    public enum Situacao {
        NEUTRO, OK, ATIVO, ERRO
    }

    /** Uma linha da tabela de leituras. */
    public record Leitura(
            int numero,
            double tempoSegundos,
            double volumeReferencia,
            long pulsos,
            double volumeMedido,
            double vazao,
            boolean estavel) {
    }

    private final ObjectProperty<Situacao> situacaoSimulador = new SimpleObjectProperty<>(Situacao.NEUTRO);
    private final ObjectProperty<Situacao> situacaoBanco = new SimpleObjectProperty<>(Situacao.NEUTRO);
    private final ObjectProperty<Situacao> situacaoEnsaio = new SimpleObjectProperty<>(Situacao.NEUTRO);

    private final StringProperty textoSimulador = new SimpleStringProperty("desconectado");
    private final StringProperty textoBanco = new SimpleStringProperty("nao verificado");
    private final StringProperty textoEnsaio = new SimpleStringProperty("parado");

    private final DoubleProperty progresso = new SimpleDoubleProperty(0);
    private final DoubleProperty volumeReferencia = new SimpleDoubleProperty(0);
    private final DoubleProperty volumeMedido = new SimpleDoubleProperty(0);
    private final DoubleProperty erroAtual = new SimpleDoubleProperty(0);
    private final DoubleProperty vazaoInstantanea = new SimpleDoubleProperty(0);
    private final DoubleProperty desvioSorteado = new SimpleDoubleProperty(0);
    private final LongProperty pulsos = new SimpleLongProperty(0);

    private final BooleanProperty emAndamento = new SimpleBooleanProperty(false);
    private final StringProperty ultimaMensagem = new SimpleStringProperty("");
    /** Vazia enquanto o ensaio corre bem; preenchida quando ele e abortado. */
    private final StringProperty mensagemFalha = new SimpleStringProperty("");
    private final ObjectProperty<RelatorioEnsaio> resultado = new SimpleObjectProperty<>();

    private final ObservableList<Leitura> leituras = FXCollections.observableArrayList();
    private final ObservableList<Ensaio> historico = FXCollections.observableArrayList();

    /** Zera as grandezas do ensaio anterior, preservando o historico do banco. */
    public void limparParaNovoEnsaio() {
        progresso.set(0);
        volumeReferencia.set(0);
        volumeMedido.set(0);
        erroAtual.set(0);
        vazaoInstantanea.set(0);
        desvioSorteado.set(0);
        pulsos.set(0);
        ultimaMensagem.set("");
        mensagemFalha.set("");
        resultado.set(null);
        leituras.clear();
    }

    public ObjectProperty<Situacao> situacaoSimuladorProperty() {
        return situacaoSimulador;
    }

    public ObjectProperty<Situacao> situacaoBancoProperty() {
        return situacaoBanco;
    }

    public ObjectProperty<Situacao> situacaoEnsaioProperty() {
        return situacaoEnsaio;
    }

    public StringProperty textoSimuladorProperty() {
        return textoSimulador;
    }

    public StringProperty textoBancoProperty() {
        return textoBanco;
    }

    public StringProperty textoEnsaioProperty() {
        return textoEnsaio;
    }

    public DoubleProperty progressoProperty() {
        return progresso;
    }

    public DoubleProperty volumeReferenciaProperty() {
        return volumeReferencia;
    }

    public DoubleProperty volumeMedidoProperty() {
        return volumeMedido;
    }

    public DoubleProperty erroAtualProperty() {
        return erroAtual;
    }

    public DoubleProperty vazaoInstantaneaProperty() {
        return vazaoInstantanea;
    }

    public DoubleProperty desvioSorteadoProperty() {
        return desvioSorteado;
    }

    public LongProperty pulsosProperty() {
        return pulsos;
    }

    public BooleanProperty emAndamentoProperty() {
        return emAndamento;
    }

    public StringProperty ultimaMensagemProperty() {
        return ultimaMensagem;
    }

    public StringProperty mensagemFalhaProperty() {
        return mensagemFalha;
    }

    public ObjectProperty<RelatorioEnsaio> resultadoProperty() {
        return resultado;
    }

    public ObservableList<Leitura> getLeituras() {
        return leituras;
    }

    public ObservableList<Ensaio> getHistorico() {
        return historico;
    }
}
