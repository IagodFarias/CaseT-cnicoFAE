package com.fae.calibracao.persistence;

import com.fae.calibracao.domain.JanelaEnsaio;
import com.fae.calibracao.domain.Medicao;
import com.fae.calibracao.domain.RelatorioEnsaio;
import com.fae.calibracao.domain.ResultadoEnsaio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Verifica o mapeamento JPA e o repositorio contra um H2 em memoria.
 *
 * Nao substitui o teste contra o PostgreSQL real, mas cobre o que costuma quebrar sem
 * depender de Docker: campos faltando no mapeamento, enum gravada errada, transacao que
 * nao commita, ordenacao da consulta.
 */
class EnsaioRepositoryTest {

    private EnsaioRepository repositorio;

    @BeforeEach
    void abrir() throws PersistenciaException {
        Map<String, Object> h2 = new HashMap<>();
        h2.put("jakarta.persistence.jdbc.driver", "org.h2.Driver");
        // Banco novo a cada teste: nome unico e ciclo de vida preso a conexao
        h2.put("jakarta.persistence.jdbc.url", "jdbc:h2:mem:ensaio-" + System.nanoTime() + ";DB_CLOSE_DELAY=-1");
        h2.put("jakarta.persistence.jdbc.user", "sa");
        h2.put("jakarta.persistence.jdbc.password", "");
        h2.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        h2.put("hibernate.hbm2ddl.auto", "create-drop");
        repositorio = new EnsaioRepository(h2);
    }

    @AfterEach
    void fechar() {
        if (repositorio != null) {
            repositorio.close();
        }
    }

    private static RelatorioEnsaio relatorio(double erroAlvoIgnorado, long pulsos, ResultadoEnsaio resultado) {
        JanelaEnsaio janela = new JanelaEnsaio(0L, 30_000_000_000L);   // 30 s
        Medicao medicao = new Medicao(janela, 7.5, pulsos, 100.0);
        return RelatorioEnsaio.de(LocalDateTime.now(), medicao, 15.0, 1.5, resultado);
    }

    @Test
    @DisplayName("Grava o laudo e devolve o id gerado pelo banco")
    void salvaEAtribuiId() throws PersistenciaException {
        Ensaio gravado = repositorio.salvar(relatorio(0, 761, ResultadoEnsaio.APROVADO));

        assertNotNull(gravado.getId(), "o id deveria ter sido gerado pelo banco");
        assertEquals(1, repositorio.contar());
    }

    @Test
    @DisplayName("Todos os campos exigidos sobrevivem ao round-trip")
    void todosOsCamposPersistem() throws PersistenciaException {
        RelatorioEnsaio original = relatorio(0, 761, ResultadoEnsaio.APROVADO);
        repositorio.salvar(original);

        Ensaio lido = repositorio.listarUltimos(1).get(0);

        assertNotNull(lido.getDataHora(), "data/hora");
        assertEquals(30.0, lido.getDuracaoSegundos(), 1e-6, "duracao");
        assertEquals(100.0, lido.getPulsosPorLitro(), 1e-9, "K");
        assertEquals(15.0, lido.getVazaoConfiguradaLpm(), 1e-9, "vazao configurada");
        assertEquals(15.0, lido.getVazaoMediaLpm(), 1e-9, "vazao media");
        assertEquals(7.5, lido.getVolumeReferenciaLitros(), 1e-9, "volume de referencia");
        assertEquals(7.61, lido.getVolumeMedidoLitros(), 1e-9, "volume medido");
        assertEquals(original.erroPercentual(), lido.getErroPercentual(), 1e-9, "erro percentual");
        assertEquals(761, lido.getPulsosGerados(), "pulsos gerados");
        assertEquals(1.5, lido.getDesvioAplicadoPercentual(), 1e-9, "desvio aplicado");
        assertEquals(ResultadoEnsaio.APROVADO, lido.getResultado(), "resultado");
    }

    @Test
    @DisplayName("REPROVADO tambem persiste, gravado como texto")
    void persisteReprovado() throws PersistenciaException {
        repositorio.salvar(relatorio(0, 800, ResultadoEnsaio.REPROVADO));
        assertEquals(ResultadoEnsaio.REPROVADO, repositorio.listarUltimos(1).get(0).getResultado());
    }

    @Test
    @DisplayName("listarUltimos respeita o limite e traz o mais recente primeiro")
    void listaOrdenadaELimitada() throws PersistenciaException {
        for (int i = 0; i < 5; i++) {
            repositorio.salvar(relatorio(0, 700 + i, ResultadoEnsaio.APROVADO));
        }

        List<Ensaio> ultimos = repositorio.listarUltimos(3);
        assertEquals(3, ultimos.size());
        assertEquals(5, repositorio.contar());
        // o ultimo gravado tem o maior id e deve vir primeiro
        assertEquals(704, ultimos.get(0).getPulsosGerados());
    }
}
