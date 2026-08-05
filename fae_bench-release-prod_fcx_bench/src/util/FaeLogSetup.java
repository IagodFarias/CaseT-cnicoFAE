//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
*
*    @brief Bootstrap da infraestrutura de logs (SLF4J + Logback).
*    @file FaeLogSetup.java
*    @details Configura, de forma programatica, dois destinos de log:
*             - CONSOLE
*             - ARQUIVO logs/calibracao_<yyyy-MM-dd_HH-mm-ss>.log (um por execucao)
*
*             A configuracao e feita em codigo (e nao via logback.xml) porque o
*             build Ant deste projeto nao copia recursos para a raiz do
*             classpath, e a aplicacao pode ser iniciada tanto a partir do
*             fat-jar quanto de bin/. Configurar em codigo garante que o
*             arquivo de log seja sempre criado, independente de como o
*             programa foi empacotado ou iniciado.
*
*             Nenhuma logica de negocio depende desta classe: ela apenas
*             prepara os appenders. Se algo falhar aqui, a aplicacao continua
*             executando normalmente (o erro e reportado no console).
*/
//=============================================================================
package util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;

/**
 * Bootstrap do sistema de logs da bancada de calibracao.
 */
public final class FaeLogSetup {

	/**
	 * Padrao de cada linha de log:
	 *
	 * <pre>
	 * 2026-08-05 14:30:00.123 INFO     [MEDIDOR-07] [ZEROFLOW      ] [Process Controller - Thread] ProcessController - mensagem
	 * </pre>
	 *
	 * - %d{yyyy-MM-dd HH:mm:ss.SSS} : data/hora com milissegundos
	 * - %-8level                    : nivel (INFO, DEBUG, WARN, ERROR)
	 * - %X{medidor:-BANCADA}        : identificacao do medidor via MDC (ex.: MEDIDOR-07);
	 *                                 "BANCADA" quando a operacao nao e de um medidor especifico
	 * - %X{etapa:-GERAL}            : etapa do processo via MDC
	 * - %thread / %logger{0}        : thread e classe de origem
	 * - %msg / %n                   : mensagem descritiva
	 *
	 * ATENCAO: parenteses sao caracteres de agrupamento no PatternLayout do
	 * Logback e truncam o restante do padrao se usados literalmente. Por isso
	 * todos os campos abaixo usam colchetes.
	 */
	private static final String PATTERN =
			"%d{yyyy-MM-dd HH:mm:ss.SSS} %-8level [%-11X{medidor:-BANCADA}] [%-22X{etapa:-GERAL}] [%thread] %logger{0} - %msg%n";

	/** Nome do diretorio de logs, na raiz do diretorio de trabalho. */
	private static final String LOG_DIR_NAME = "logs";

	/** Propriedade de sistema opcional para trocar o diretorio de logs. */
	private static final String LOG_DIR_PROPERTY = "fae.log.dir";

	/** Propriedade de sistema opcional para trocar o nivel de log. */
	private static final String LOG_LEVEL_PROPERTY = "fae.log.level";

	private static volatile boolean initialized = false;

	private static String logFilePath = null;

	private FaeLogSetup() {
	}

	/**
	 * Inicializa os appenders de console e de arquivo. Idempotente: chamadas
	 * subsequentes sao ignoradas.
	 *
	 * @return o caminho absoluto do arquivo de log, ou null se a inicializacao falhou
	 */
	public static synchronized String init() {
		if (initialized) {
			return logFilePath;
		}
		initialized = true;

		try {
			String dirName = System.getProperty(LOG_DIR_PROPERTY, LOG_DIR_NAME);
			File logDir = new File(dirName);
			if (!logDir.exists()) {
				// Cria a pasta logs/ automaticamente se ela nao existir.
				logDir.mkdirs();
			}

			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
			File logFile = new File(logDir, "calibracao_" + timestamp + ".log");
			logFilePath = logFile.getAbsolutePath();

			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
			context.reset();

			// ---- Appender de arquivo -------------------------------------------------
			PatternLayoutEncoder fileEncoder = new PatternLayoutEncoder();
			fileEncoder.setContext(context);
			fileEncoder.setPattern(PATTERN);
			fileEncoder.setCharset(StandardCharsets.UTF_8);
			fileEncoder.start();

			FileAppender<ch.qos.logback.classic.spi.ILoggingEvent> fileAppender = new FileAppender<>();
			fileAppender.setContext(context);
			fileAppender.setName("ARQUIVO");
			fileAppender.setFile(logFile.getPath());
			fileAppender.setAppend(true);
			fileAppender.setImmediateFlush(true);
			fileAppender.setEncoder(fileEncoder);
			fileAppender.start();

			// ---- Appender de console -------------------------------------------------
			PatternLayoutEncoder consoleEncoder = new PatternLayoutEncoder();
			consoleEncoder.setContext(context);
			consoleEncoder.setPattern(PATTERN);
			consoleEncoder.start();

			ConsoleAppender<ch.qos.logback.classic.spi.ILoggingEvent> consoleAppender = new ConsoleAppender<>();
			consoleAppender.setContext(context);
			consoleAppender.setName("CONSOLE");
			consoleAppender.setEncoder(consoleEncoder);
			consoleAppender.start();

			ch.qos.logback.classic.Logger root = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
			root.setLevel(resolveLevel());
			root.addAppender(fileAppender);
			root.addAppender(consoleAppender);

			// Hibernate e bibliotecas de terceiros sao ruidosos em DEBUG; mantem em WARN.
			context.getLogger("org.hibernate").setLevel(Level.WARN);
			context.getLogger("org.jboss").setLevel(Level.WARN);
			context.getLogger("com.zaxxer").setLevel(Level.WARN);

			System.out.println("=============================================================================");
			System.out.println("Arquivo de log desta execucao: " + logFilePath);
			System.out.println("Nivel: " + root.getLevel() + "  (para mais detalhe use -D" + LOG_LEVEL_PROPERTY + "=TRACE)");
			System.out.println("=============================================================================");

		} catch (Throwable t) {
			// Falha na infraestrutura de log nao pode derrubar a bancada.
			System.err.println("FALHA ao inicializar a infraestrutura de logs: " + t);
			t.printStackTrace();
			logFilePath = null;
		}

		return logFilePath;
	}

	/**
	 * Resolve o nivel de log a partir da propriedade de sistema fae.log.level.
	 * Padrao: DEBUG (necessario para rastrear entrada/saida de funcoes criticas
	 * e valores lidos dos sensores).
	 */
	private static Level resolveLevel() {
		String configured = System.getProperty(LOG_LEVEL_PROPERTY);
		if (configured == null || configured.trim().isEmpty()) {
			return Level.DEBUG;
		}
		return Level.toLevel(configured.trim().toUpperCase(), Level.DEBUG);
	}

	/**
	 * Caminho absoluto do arquivo de log desta execucao.
	 *
	 * @return caminho do arquivo, ou null se ainda nao inicializado
	 */
	public static String getLogFilePath() {
		return logFilePath;
	}
}
