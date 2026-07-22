package com.fae.calibracao.domain;

import java.time.Duration;

/**
 * Janela temporal [t_inicio, t_fim] do ensaio, em nanossegundos do relogio monotonico
 * (System.nanoTime), que e imune a ajustes do relogio de parede durante o ensaio.
 *
 * <h2>Por que a janela e um tipo explicito</h2>
 *
 * Volume medido, volume de referencia e duracao precisam se referir ao MESMO intervalo,
 * senao o erro percentual carrega um vies que nao vem do medidor. Um exemplo do que se
 * evita: se os pulsos param em t_fim mas a referencia continua acumulando ate o simulador
 * processar o STOP, a referencia fica maior pela latencia da rede e o erro e empurrado
 * para baixo — sistematicamente, em todo ensaio, sempre no mesmo sentido. Nao e ruido,
 * nao se cancela repetindo o ensaio, e num ensaio curto pode ser da ordem do proprio
 * desvio que se quer medir.
 *
 * Modelar a janela como um tipo obriga quem monta a {@link Medicao} a declarar de qual
 * intervalo os numeros vieram, em vez de deixar isso implicito.
 *
 * <h2>Quem define os limites</h2>
 *
 * A janela e ditada pela thread de pulsos, que e o unico relogio local ao processo:
 * t_inicio e o instante em que ela comeca a contar e t_fim o instante em que ela e
 * sinalizada para parar (ambos capturados dentro de PulseGenerator, com a contagem
 * final feita exatamente em t_fim). A referencia e entao amostrada como DIFERENCA de
 * leituras nas duas bordas — Vref = V(t_fim) - V(t_inicio) — e nao pelo total acumulado
 * pelo simulador. Assim o que a referencia mediu antes de t_inicio e depois de t_fim
 * fica fora da conta, e as duas medicoes cobrem o mesmo intervalo.
 */
public record JanelaEnsaio(long inicioNanos, long fimNanos) {

    public JanelaEnsaio {
        if (fimNanos <= inicioNanos) {
            throw new IllegalArgumentException(
                    "janela invalida: t_fim (" + fimNanos + ") nao e posterior a t_inicio (" + inicioNanos + ")");
        }
    }

    public Duration duracao() {
        return Duration.ofNanos(fimNanos - inicioNanos);
    }

    public double duracaoSegundos() {
        return (fimNanos - inicioNanos) / 1_000_000_000.0;
    }

    public double duracaoMinutos() {
        return duracaoSegundos() / 60.0;
    }
}
