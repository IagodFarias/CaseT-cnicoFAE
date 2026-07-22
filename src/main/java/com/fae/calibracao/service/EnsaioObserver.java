package com.fae.calibracao.service;

import com.fae.calibracao.domain.RelatorioEnsaio;

/**
 * Recebe os eventos do ensaio conforme ele acontece.
 *
 * Existe para que o EnsaioService nao precise saber se a saida e console, log ou
 * interface grafica: ele publica o que aconteceu, a camada ui/ decide como mostrar.
 * Todos os metodos tem implementacao vazia, entao quem observa so sobrescreve o que
 * interessa.
 */
public interface EnsaioObserver {

    EnsaioObserver SILENCIOSO = new EnsaioObserver() { };

    default void onConectando(String endereco) { }

    default void onConectado(String endereco) { }

    /** Ensaio aceito pelo simulador; a thread de pulsos acaba de comecar. */
    default void onIniciado(double desvioPercentual, double vazaoEfetivaLpm) { }

    default void onProgresso(ProgressoEnsaio progresso) { }

    /** Falha contornada: o ensaio continua, mas o operador precisa saber. */
    default void onAviso(String mensagem) { }

    default void onFinalizado(RelatorioEnsaio relatorio) { }

    /** Falha que interrompeu o ensaio. */
    default void onFalha(String mensagem) { }

    default void onDesconectado(String endereco) { }
}
