//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
*
*    @brief Fachada de logging do processo de calibracao (SLF4J).
*    @file ProcessLog.java
*    @details Centraliza:
*             - obtencao de Logger SLF4J por classe
*             - identificacao do medidor (1 a 20) via MDC -> [MEDIDOR-07]
*             - identificacao da etapa do processo via MDC
*             - cronometragem das etapas demoradas
*             - registro de excecoes com stack trace completo
*
*             IMPORTANTE: esta classe NAO altera fluxo. Os metodos de excecao
*             apenas REGISTRAM; quem chama continua responsavel por relancar ou
*             tratar a excecao exatamente como antes.
*/
//=============================================================================
package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import si.dbcomm.util.ConversorNumbers;

/**
 * Fachada de logging do processo de calibracao.
 */
public final class ProcessLog {

	static {
		// Garante que os appenders (console + arquivo logs/calibracao_<ts>.log)
		// estejam configurados antes de qualquer logger ser usado, independente
		// de qual classe da aplicacao registrar a primeira mensagem.
		FaeLogSetup.init();
	}

	/** Chave MDC com a identificacao do medidor (ex.: MEDIDOR-07). */
	public static final String MDC_MEDIDOR = "medidor";

	/** Chave MDC com a etapa corrente do processo (ex.: ZEROFLOW). */
	public static final String MDC_ETAPA = "etapa";

	private ProcessLog() {
	}

	// =========================================================================
	// Loggers
	// =========================================================================

	/**
	 * Obtem o logger SLF4J da classe informada.
	 *
	 * @param clazz classe de origem das mensagens
	 * @return logger SLF4J
	 */
	public static Logger get(Class<?> clazz) {
		return LoggerFactory.getLogger(clazz);
	}

	// =========================================================================
	// Identificacao do medidor (1 a 20)
	// =========================================================================

	/**
	 * Converte o conversor em identificacao legivel do medidor.
	 *
	 * @param conversor conversor do medidor (CONVERSOR1 .. CONVERSOR20)
	 * @return "MEDIDOR-07" para CONVERSOR7; "MEDIDOR-??" se conversor for nulo
	 */
	public static String tagMedidor(ConversorNumbers conversor) {
		if (conversor == null) {
			return "MEDIDOR-??";
		}
		return String.format("MEDIDOR-%02d", conversor.ordinal() + 1);
	}

	/**
	 * Converte um numero de medidor em identificacao legivel.
	 *
	 * @param numero numero do medidor, de 1 a 20
	 * @return "MEDIDOR-07"
	 */
	public static String tagMedidor(int numero) {
		return String.format("MEDIDOR-%02d", numero);
	}

	/**
	 * Marca o medidor corrente da thread. Todas as linhas de log emitidas por
	 * esta thread passam a exibir [MEDIDOR-NN] ate que {@link #limparMedidor()}
	 * seja chamado.
	 *
	 * @param conversor conversor do medidor
	 */
	public static void setMedidor(ConversorNumbers conversor) {
		MDC.put(MDC_MEDIDOR, tagMedidor(conversor));
	}

	/**
	 * Marca o medidor corrente da thread pelo numero (1 a 20).
	 *
	 * @param numero numero do medidor
	 */
	public static void setMedidor(int numero) {
		MDC.put(MDC_MEDIDOR, tagMedidor(numero));
	}

	/**
	 * Marca o medidor corrente da thread por um identificador livre (ex.: IP),
	 * usado quando o conversor nao esta disponivel no ponto de chamada.
	 *
	 * @param identificacao identificacao textual do medidor
	 */
	public static void setMedidorTag(String identificacao) {
		MDC.put(MDC_MEDIDOR, identificacao);
	}

	/**
	 * Remove a identificacao do medidor da thread corrente.
	 */
	public static void limparMedidor() {
		MDC.remove(MDC_MEDIDOR);
	}

	// =========================================================================
	// Etapa do processo
	// =========================================================================

	/**
	 * Marca a etapa corrente do processo para a thread.
	 *
	 * @param etapa nome da etapa (ex.: "ZEROFLOW", "CONNECT_METERS")
	 */
	public static void setEtapa(String etapa) {
		MDC.put(MDC_ETAPA, etapa);
	}

	/**
	 * Remove a etapa da thread corrente.
	 */
	public static void limparEtapa() {
		MDC.remove(MDC_ETAPA);
	}

	/**
	 * Limpa todo o contexto (medidor e etapa) da thread corrente.
	 */
	public static void limparContexto() {
		MDC.remove(MDC_MEDIDOR);
		MDC.remove(MDC_ETAPA);
	}

	// =========================================================================
	// Cronometragem de etapas demoradas
	// =========================================================================

	/**
	 * Marca o instante inicial de uma etapa.
	 *
	 * @return timestamp em nanossegundos, a ser passado para {@link #duracaoMs(long)}
	 */
	public static long iniciarCronometro() {
		return System.nanoTime();
	}

	/**
	 * Calcula a duracao decorrida desde o instante informado.
	 *
	 * @param inicioNano valor retornado por {@link #iniciarCronometro()}
	 * @return duracao em milissegundos
	 */
	public static long duracaoMs(long inicioNano) {
		return (System.nanoTime() - inicioNano) / 1_000_000L;
	}

	/**
	 * Registra a duracao de uma etapa em nivel INFO.
	 *
	 * @param logger logger de origem
	 * @param etapa nome da etapa medida
	 * @param inicioNano valor retornado por {@link #iniciarCronometro()}
	 */
	public static void logDuracao(Logger logger, String etapa, long inicioNano) {
		logger.info("TEMPO: etapa '{}' concluida em {} ms", etapa, duracaoMs(inicioNano));
	}

	// =========================================================================
	// Excecoes
	// =========================================================================

	/**
	 * Registra uma excecao com stack trace completo em nivel ERROR.
	 *
	 * Este metodo NAO engole nem altera o fluxo: apenas registra. O chamador
	 * mantem o tratamento original (relancar, retornar, seguir adiante).
	 *
	 * @param logger logger de origem
	 * @param contexto descricao do que estava sendo feito quando falhou
	 * @param t excecao capturada
	 */
	public static void erro(Logger logger, String contexto, Throwable t) {
		logger.error("EXCECAO em {}: {}", contexto, t.toString(), t);
	}

	/**
	 * Registra uma excecao com stack trace completo em nivel CRITICAL (mapeado
	 * para ERROR do SLF4J, com marcacao explicita na mensagem).
	 *
	 * Assim como {@link #erro}, apenas registra e nao altera o fluxo.
	 *
	 * @param logger logger de origem
	 * @param contexto descricao do que estava sendo feito quando falhou
	 * @param t excecao capturada
	 */
	public static void critico(Logger logger, String contexto, Throwable t) {
		logger.error("CRITICAL: EXCECAO em {}: {}", contexto, t.toString(), t);
	}

	/**
	 * Registra uma mensagem de nivel CRITICAL sem excecao associada.
	 *
	 * @param logger logger de origem
	 * @param mensagem mensagem descritiva
	 */
	public static void critico(Logger logger, String mensagem) {
		logger.error("CRITICAL: {}", mensagem);
	}
}
