package com.fae.calibracao.ui;

import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.service.EnsaioObserver;
import com.fae.calibracao.service.ProgressoEnsaio;

/**
 * Renderiza no console os eventos publicados pelo EnsaioService.
 * A formatacao definitiva do laudo fica para a Etapa 7.
 */
public class ConsoleObserver implements EnsaioObserver {

    @Override
    public void onConectando(String endereco) {
        System.out.println("[conexao] conectando em " + endereco + " ...");
    }

    @Override
    public void onConectado(String endereco) {
        System.out.println("[conexao] CONECTADO");
    }

    @Override
    public void onIniciado(double desvioPercentual, double vazaoEfetivaLpm) {
        System.out.println("[ensaio ] iniciado");
        System.out.printf("[medidor] desvio sorteado %+.3f%% (vazao efetiva %.3f L/min)%n",
                desvioPercentual, vazaoEfetivaLpm);
        System.out.println();
    }

    @Override
    public void onProgresso(ProgressoEnsaio p) {
        System.out.printf("[leitura] %2d  t=%5.1fs  Vref=%7.3f L  pulsos=%6d  Vmed=%7.3f L  "
                        + "vazao=%6.2f L/min  estavel=%s%n",
                p.leitura(), p.tempoDecorridoSegundos(), p.volumeReferenciaParcialLitros(),
                p.pulsosGerados(), p.volumeMedidoParcialLitros(), p.vazaoInstantaneaLpm(),
                p.estavel() ? "sim" : "nao");
    }

    @Override
    public void onAviso(String mensagem) {
        System.out.println("[AVISO  ] " + mensagem);
    }

    @Override
    public void onFalha(String mensagem) {
        System.out.println("[FALHA  ] " + mensagem);
    }

    @Override
    public void onFinalizado(RelatorioEnsaio r) {
        System.out.println();
        System.out.println("--------------- RESULTADO DO ENSAIO ---------------");
        System.out.printf("duracao              : %.2f s%n", r.duracaoSegundos());
        System.out.printf("K                    : %.0f pulsos/L%n", r.pulsosPorLitro());
        System.out.printf("vazao configurada    : %.2f L/min%n", r.vazaoConfiguradaLpm());
        System.out.printf("vazao media          : %.3f L/min%n", r.vazaoMediaLpm());
        System.out.printf("pulsos gerados       : %d%n", r.pulsosGerados());
        System.out.printf("volume de referencia : %.3f L%n", r.volumeReferenciaLitros());
        System.out.printf("volume medido        : %.3f L%n", r.volumeMedidoLitros());
        System.out.printf("erro                 : %+.3f %%%n", r.erroPercentual());
        System.out.printf("desvio aplicado      : %+.3f %% (referencia de conferencia)%n",
                r.desvioAplicadoPercentual());
        System.out.println("resultado            : " + r.resultado());
        System.out.println("---------------------------------------------------");
    }

    @Override
    public void onDesconectado(String endereco) {
        System.out.println();
        System.out.println("[conexao] DESCONECTADO");
    }
}
