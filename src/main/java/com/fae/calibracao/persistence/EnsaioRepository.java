package com.fae.calibracao.persistence;

import com.fae.calibracao.domain.RelatorioEnsaio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Acesso aos ensaios gravados.
 *
 * Mantem um unico EntityManagerFactory (caro de criar, thread-safe, vive enquanto a
 * aplicacao viver) e abre um EntityManager curto por operacao, que NAO e thread-safe.
 */
public class EnsaioRepository implements AutoCloseable {

    private static final String UNIDADE_PERSISTENCIA = "calibracaoPU";

    private final EntityManagerFactory emf;

    /**
     * Abre a conexao com o banco.
     *
     * As credenciais vem de variaveis de ambiente quando presentes, com queda para os
     * valores do persistence.xml, que correspondem ao docker-compose deste projeto.
     * Assim o mesmo artefato roda local e em outro ambiente sem recompilar.
     */
    public EnsaioRepository() throws PersistenciaException {
        Map<String, Object> overrides = new HashMap<>();
        aplicarSePresente(overrides, "jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
        aplicarSePresente(overrides, "jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
        aplicarSePresente(overrides, "jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));
        this.emf = criarFactory(overrides);
    }

    /**
     * Abre o repositorio com propriedades JPA arbitrarias, sobrescrevendo o persistence.xml.
     * Permite apontar os testes para um banco em memoria sem duplicar a unidade de
     * persistencia nem exigir Docker para verificar o mapeamento.
     */
    public EnsaioRepository(Map<String, Object> overrides) throws PersistenciaException {
        this.emf = criarFactory(overrides);
    }

    private static EntityManagerFactory criarFactory(Map<String, Object> overrides) throws PersistenciaException {
        try {
            return Persistence.createEntityManagerFactory(UNIDADE_PERSISTENCIA, overrides);
        } catch (RuntimeException e) {
            throw new PersistenciaException("nao foi possivel conectar ao banco: " + causaRaiz(e), e);
        }
    }

    /** Grava o laudo e devolve a entidade ja com o id atribuido pelo banco. */
    public Ensaio salvar(RelatorioEnsaio relatorio) throws PersistenciaException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            Ensaio entidade = Ensaio.de(relatorio);
            tx.begin();
            em.persist(entidade);
            tx.commit();
            return entidade;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();   // nunca deixar transacao pendurada segurando conexao
            }
            throw new PersistenciaException("falha ao gravar o ensaio: " + causaRaiz(e), e);
        } finally {
            em.close();
        }
    }

    /** Ultimos ensaios gravados, do mais recente para o mais antigo. */
    public List<Ensaio> listarUltimos(int limite) throws PersistenciaException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "select e from Ensaio e order by e.dataHora desc, e.id desc", Ensaio.class)
                    .setMaxResults(limite)
                    .getResultList();
        } catch (RuntimeException e) {
            throw new PersistenciaException("falha ao consultar ensaios: " + causaRaiz(e), e);
        } finally {
            em.close();
        }
    }

    public long contar() throws PersistenciaException {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("select count(e) from Ensaio e", Long.class).getSingleResult();
        } catch (RuntimeException e) {
            throw new PersistenciaException("falha ao contar ensaios: " + causaRaiz(e), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    private static void aplicarSePresente(Map<String, Object> destino, String chave, String valor) {
        if (valor != null && !valor.isBlank()) {
            destino.put(chave, valor);
        }
    }

    /** Mensagem da causa mais profunda: o erro util do JDBC costuma estar no fundo da pilha. */
    private static String causaRaiz(Throwable e) {
        Throwable atual = e;
        while (atual.getCause() != null && atual.getCause() != atual) {
            atual = atual.getCause();
        }
        return atual.getMessage() == null ? atual.getClass().getSimpleName() : atual.getMessage();
    }
}
