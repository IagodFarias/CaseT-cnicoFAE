package com.fae.calibracao.ui;

import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.service.EnsaioConfig;
import com.fae.calibracao.service.EnsaioObserver;
import com.fae.calibracao.service.ProgressoEnsaio;

import java.time.format.DateTimeFormatter;

/**
 * Renderiza no console os eventos publicados pelo EnsaioService.
 *
 * Todo o texto e ASCII de proposito. O console do Windows costuma estar em CP850 ou
 * CP1252 e nem todo caractere sobrevive a conversao — na Etapa 2 um travessao virou "?"
 * na mensagem de erro. Como o laudo pode ser lido em qualquer maquina, texto sem acento
 * e a opcao que nunca corrompe. Ver o README para rodar com acentuacao via UTF-8.
 */
public class ConsoleObserver implements EnsaioObserver {

    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final int LARGURA_BARRA = 20;

    private final EnsaioConfig config;
    private final double toleranciaPercentual;
    private final double duracaoTotalSegundos;

    private boolean cabecalhoDaTabelaImpresso;

    public ConsoleObserver(EnsaioConfig config, double toleranciaPercentual) {
        this.config = config;
        this.toleranciaPercentual = toleranciaPercentual;
        this.duracaoTotalSegundos = config.duracaoEnsaio().toNanos() / 1_000_000_000.0;
    }

    /** Cabecalho com os parametros do ensaio: sem eles o laudo nao e reproduzivel. */
    public void imprimirCabecalho() {
        linha('=');
        System.out.println("  SOFTWARE DE CALIBRACAO DE MEDIDORES DE AGUA");
        System.out.println("  Ensaio metrologico contra medidor de referencia via TCP/IP");
        linha('=');
        System.out.println("  simulador (referencia) : " + config.endereco());
        System.out.printf("  vazao nominal          : %.2f L/min%n", config.vazaoNominalLpm());
        System.out.printf("  constante K            : %.0f pulsos/L%n", config.pulsosPorLitro());
        System.out.printf("  faixa de desvio        : %s%n", config.faixaDesvio());
        System.out.printf("  duracao programada     : %.0f s%n", duracaoTotalSegundos);
        System.out.printf("  criterio de aprovacao  : |erro| <= %.2f %%%n", toleranciaPercentual);
        linha('-');
    }

    public void onBancoConectado() {
        System.out.println("  [banco  ] CONECTADO");
    }

    public void onBancoIndisponivel(String motivo) {
        System.out.println("  [banco  ] INDISPONIVEL - o ensaio rodara sem gravar o laudo");
        System.out.println("            motivo: " + motivo);
        System.out.println("            suba o PostgreSQL com: docker compose up -d");
    }

    @Override
    public void onConectando(String endereco) {
        System.out.println("  [conexao] conectando em " + endereco + " ...");
    }

    @Override
    public void onConectado(String endereco) {
        System.out.println("  [conexao] CONECTADO");
    }

    @Override
    public void onIniciado(double desvioPercentual, double vazaoEfetivaLpm) {
        System.out.println("  [ensaio ] START aceito - ensaio EM ANDAMENTO");
        System.out.printf("  [medidor] desvio sorteado %+.3f %% (vazao efetiva %.3f L/min)%n",
                desvioPercentual, vazaoEfetivaLpm);
        linha('-');
    }

    @Override
    public void onProgresso(ProgressoEnsaio p) {
        if (!cabecalhoDaTabelaImpresso) {
            System.out.println("    #   tempo   andamento               Vref      pulsos      Vmed     vazao   estavel");
            cabecalhoDaTabelaImpresso = true;
        }
        double fracao = Math.min(1.0, p.tempoDecorridoSegundos() / duracaoTotalSegundos);
        System.out.printf("  %3d  %5.1fs  %s %3.0f%%  %8.3f L  %6d  %8.3f L  %6.2f  %s%n",
                p.leitura(),
                p.tempoDecorridoSegundos(),
                barra(fracao),
                fracao * 100,
                p.volumeReferenciaParcialLitros(),
                p.pulsosGerados(),
                p.volumeMedidoParcialLitros(),
                p.vazaoInstantaneaLpm(),
                p.estavel() ? "sim" : "nao");
    }

    @Override
    public void onAviso(String mensagem) {
        System.out.println("  [AVISO  ] " + mensagem);
    }

    @Override
    public void onFalha(String mensagem) {
        System.out.println("  [FALHA  ] " + mensagem);
    }

    @Override
    public void onFinalizado(RelatorioEnsaio r) {
        linha('-');
        System.out.println("  [ensaio ] STOP enviado - ensaio CONCLUIDO");
        System.out.println();
        linha('=');
        System.out.println("  RESULTADO DO ENSAIO");
        linha('=');
        System.out.println("  data/hora              : " + r.dataHora().format(DATA_HORA));
        System.out.printf("  duracao                : %.2f s%n", r.duracaoSegundos());
        System.out.printf("  constante K            : %.0f pulsos/L%n", r.pulsosPorLitro());
        System.out.printf("  vazao configurada      : %.2f L/min%n", r.vazaoConfiguradaLpm());
        System.out.printf("  vazao media do ensaio  : %.3f L/min%n", r.vazaoMediaLpm());
        System.out.println();
        System.out.printf("  pulsos gerados         : %d%n", r.pulsosGerados());
        System.out.printf("  volume de referencia   : %8.3f L   (medidor padrao)%n", r.volumeReferenciaLitros());
        System.out.printf("  volume medido          : %8.3f L   (pulsos / K)%n", r.volumeMedidoLitros());
        System.out.printf("  erro                   : %+8.3f %%   (tolerancia +/- %.2f %%)%n",
                r.erroPercentual(), toleranciaPercentual);
        System.out.println();
        System.out.println("  RESULTADO              : " + r.resultado());
        // Registrado so porque o medidor sob teste e simulado: permite conferir se o erro
        // apurado corresponde ao desvio que foi injetado. Nao existiria num ensaio real.
        System.out.printf("  (desvio injetado no medidor simulado: %+.3f %%)%n", r.desvioAplicadoPercentual());
        linha('=');
    }

    @Override
    public void onDesconectado(String endereco) {
        System.out.println("  [conexao] DESCONECTADO");
    }

    private static String barra(double fracao) {
        int preenchido = (int) Math.round(fracao * LARGURA_BARRA);
        return "[" + "#".repeat(preenchido) + "-".repeat(LARGURA_BARRA - preenchido) + "]";
    }

    private static void linha(char c) {
        System.out.println(String.valueOf(c).repeat(78));
    }
}
