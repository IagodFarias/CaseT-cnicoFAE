package com.fae.calibracao.ui;

import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.service.EnsaioObserver;
import com.fae.calibracao.service.ProgressoEnsaio;

import java.util.List;

/**
 * Distribui os eventos do ensaio para varios observadores.
 *
 * O EnsaioService aceita um unico EnsaioObserver. Em vez de altera-lo para aceitar uma
 * lista — o que mudaria a camada de servico so por causa da apresentacao — o fan-out fica
 * aqui, na camada ui/: para o servico continua sendo um observador so.
 *
 * E o que permite o console e a GUI rodarem juntos, ambos alimentados pelos MESMOS
 * eventos, sem que nenhum dos dois saiba da existencia do outro.
 *
 * <h2>Isolamento de falhas</h2>
 * Se um observador lancar excecao, os demais ainda recebem o evento e o ensaio continua.
 * Sem isso, um erro de formatacao na tela poderia abortar uma medicao em andamento —
 * a apresentacao nunca deve derrubar o ensaio.
 */
public class ObserverComposto implements EnsaioObserver {

    private final List<EnsaioObserver> observadores;

    public ObserverComposto(EnsaioObserver... observadores) {
        this.observadores = List.of(observadores);
    }

    @Override
    public void onConectando(String endereco) {
        notificar(o -> o.onConectando(endereco), "onConectando");
    }

    @Override
    public void onConectado(String endereco) {
        notificar(o -> o.onConectado(endereco), "onConectado");
    }

    @Override
    public void onIniciado(double desvioPercentual, double vazaoEfetivaLpm) {
        notificar(o -> o.onIniciado(desvioPercentual, vazaoEfetivaLpm), "onIniciado");
    }

    @Override
    public void onProgresso(ProgressoEnsaio progresso) {
        notificar(o -> o.onProgresso(progresso), "onProgresso");
    }

    @Override
    public void onAviso(String mensagem) {
        notificar(o -> o.onAviso(mensagem), "onAviso");
    }

    @Override
    public void onFinalizado(RelatorioEnsaio relatorio) {
        notificar(o -> o.onFinalizado(relatorio), "onFinalizado");
    }

    @Override
    public void onFalha(String mensagem) {
        notificar(o -> o.onFalha(mensagem), "onFalha");
    }

    @Override
    public void onDesconectado(String endereco) {
        notificar(o -> o.onDesconectado(endereco), "onDesconectado");
    }

    private void notificar(java.util.function.Consumer<EnsaioObserver> evento, String nome) {
        for (EnsaioObserver observador : observadores) {
            try {
                evento.accept(observador);
            } catch (RuntimeException e) {
                // Reportado na saida de erro para nao se misturar ao laudo no stdout.
                System.err.println("[UI] observador " + observador.getClass().getSimpleName()
                        + " falhou em " + nome + ": " + e);
            }
        }
    }
}
