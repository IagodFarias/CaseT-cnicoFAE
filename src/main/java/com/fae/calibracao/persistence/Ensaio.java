package com.fae.calibracao.persistence;

import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.domain.ResultadoEnsaio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Registro persistente de um ensaio metrologico.
 *
 * Espelha o RelatorioEnsaio, que e um record imutavel do dominio. Sao dois tipos de
 * proposito: o dominio nao deve carregar anotacoes de JPA nem precisar de construtor
 * vazio e setters, exigencias do Hibernate que quebrariam a imutabilidade. A traducao
 * fica concentrada em {@link #de(RelatorioEnsaio)}.
 */
@Entity
@Table(name = "ensaio")
public class Ensaio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "duracao_segundos", nullable = false)
    private double duracaoSegundos;

    /** Constante K do medidor, em pulsos por litro. */
    @Column(name = "pulsos_por_litro", nullable = false)
    private double pulsosPorLitro;

    @Column(name = "vazao_configurada_lpm", nullable = false)
    private double vazaoConfiguradaLpm;

    @Column(name = "vazao_media_lpm", nullable = false)
    private double vazaoMediaLpm;

    @Column(name = "volume_referencia_litros", nullable = false)
    private double volumeReferenciaLitros;

    @Column(name = "volume_medido_litros", nullable = false)
    private double volumeMedidoLitros;

    @Column(name = "erro_percentual", nullable = false)
    private double erroPercentual;

    @Column(name = "pulsos_gerados", nullable = false)
    private long pulsosGerados;

    /**
     * Desvio sorteado para o medidor sob teste. Nao e exigido no laudo, mas fica gravado
     * porque e o unico jeito de auditar depois se o erro apurado corresponde ao desvio
     * simulado — util justamente por ser um ambiente de simulacao.
     */
    @Column(name = "desvio_aplicado_percentual", nullable = false)
    private double desvioAplicadoPercentual;

    @Enumerated(EnumType.STRING)   // grava APROVADO/REPROVADO, nao 0/1: legivel em SQL e estavel se a enum crescer
    @Column(name = "resultado", nullable = false, length = 20)
    private ResultadoEnsaio resultado;

    /** Exigido pelo JPA. Nao usar diretamente: prefira {@link #de(RelatorioEnsaio)}. */
    protected Ensaio() {
    }

    public static Ensaio de(RelatorioEnsaio r) {
        Ensaio e = new Ensaio();
        e.dataHora = r.dataHora();
        e.duracaoSegundos = r.duracaoSegundos();
        e.pulsosPorLitro = r.pulsosPorLitro();
        e.vazaoConfiguradaLpm = r.vazaoConfiguradaLpm();
        e.vazaoMediaLpm = r.vazaoMediaLpm();
        e.volumeReferenciaLitros = r.volumeReferenciaLitros();
        e.volumeMedidoLitros = r.volumeMedidoLitros();
        e.erroPercentual = r.erroPercentual();
        e.pulsosGerados = r.pulsosGerados();
        e.desvioAplicadoPercentual = r.desvioAplicadoPercentual();
        e.resultado = r.resultado();
        return e;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public double getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public double getPulsosPorLitro() {
        return pulsosPorLitro;
    }

    public double getVazaoConfiguradaLpm() {
        return vazaoConfiguradaLpm;
    }

    public double getVazaoMediaLpm() {
        return vazaoMediaLpm;
    }

    public double getVolumeReferenciaLitros() {
        return volumeReferenciaLitros;
    }

    public double getVolumeMedidoLitros() {
        return volumeMedidoLitros;
    }

    public double getErroPercentual() {
        return erroPercentual;
    }

    public long getPulsosGerados() {
        return pulsosGerados;
    }

    public double getDesvioAplicadoPercentual() {
        return desvioAplicadoPercentual;
    }

    public ResultadoEnsaio getResultado() {
        return resultado;
    }

    @Override
    public String toString() {
        return String.format("Ensaio#%d[%s, erro %+.3f%%, %s]", id, dataHora, erroPercentual, resultado);
    }
}
