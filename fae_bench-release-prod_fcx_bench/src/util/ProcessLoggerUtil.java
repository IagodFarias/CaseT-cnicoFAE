//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ProcessLoggerUtil.java
*    @author marcos
*    @date 24 de ago de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author marcos
 *
 */
public class ProcessLoggerUtil {
	static Logger logger = Logger.getLogger("ProcessLog");
	static FileHandler fh;

	/**
	 * Ponte para a infraestrutura SLF4J. Todas as chamadas existentes a
	 * writeInfo()/writeSevere() passam a ser espelhadas para o arquivo
	 * logs/calibracao_<timestamp>.log e para o console, com data/hora em
	 * milissegundos, nivel, medidor e etapa. O comportamento original
	 * (java.util.logging) e mantido intacto abaixo.
	 */
	private static final org.slf4j.Logger SLF4J_LOG = ProcessLog.get(ProcessLoggerUtil.class);

	// private static ProcessLoggerUtil INSTANCE = new ProcessLoggerUtil();
	private static ProcessLoggerUtil INSTANCE;

	/**
	 */
	public ProcessLoggerUtil() {

		System.out.println("=============================================================================");
		System.out.println("ProcessLoggerUtil()");
		System.out.println("=============================================================================");
		try {
			// This block configure the logger with handler and formatter
			fh = new FileHandler("../../ProcessLog.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

		} catch (SecurityException se) {
			ProcessLog.erro(SLF4J_LOG, "ProcessLoggerUtil(): criacao do FileHandler ../../ProcessLog.log", se);
			se.printStackTrace();
		} catch (IOException e) {
			ProcessLog.erro(SLF4J_LOG, "ProcessLoggerUtil(): criacao do FileHandler ../../ProcessLog.log", e);
			e.printStackTrace();
		}
	}

	/**
	 */
	public ProcessLoggerUtil(String bacthName, int run) {

		System.out.println("=============================================================================");
		System.out.println("ProcessLoggerUtil(" + bacthName + ", " + run + ")");
		System.out.println("=============================================================================");
		
		// Create dir
		String fullDir = "../Logs/" + bacthName + "/" + run;
		File dir = new File(fullDir);
		if(dir.mkdirs()){
			try {
				// This block configure the logger with handler and formatter
				fh = new FileHandler(fullDir + "/ProcessLog.log");
				logger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
				
			} catch (SecurityException se) {
				ProcessLog.erro(SLF4J_LOG, "ProcessLoggerUtil(" + bacthName + ", " + run + "): criacao do FileHandler em " + fullDir, se);
				se.printStackTrace();
			} catch (IOException e) {
				ProcessLog.erro(SLF4J_LOG, "ProcessLoggerUtil(" + bacthName + ", " + run + "): criacao do FileHandler em " + fullDir, e);
				e.printStackTrace();
			}

		}

	}

	/**
	 * 
	 */
	public static void createDir(String bacthName, int run) {
		if (INSTANCE == null) {
			INSTANCE = new ProcessLoggerUtil(bacthName, run);
		}
	}

	/**
	 * 
	 * @param info
	 */
	public static void writeInfo(String info) {
		SLF4J_LOG.info(info);
		if (INSTANCE == null) {
			INSTANCE = new ProcessLoggerUtil();
		}
		logger.info(info);
	}

	public static void writeSevere(String info) {
		SLF4J_LOG.error(info);
		if (INSTANCE == null) {
			INSTANCE = new ProcessLoggerUtil();
		}
		logger.severe(info);
	}

	public static ProcessLoggerUtil getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ProcessLoggerUtil();
		}
		return INSTANCE;
	}
}

/// **
// * @author marcos
// *
// */
// public class ProcessLoggerUtil {
// static Logger logger = Logger.getLogger("ProcessLog");
// static FileHandler fh;
//
// private static ProcessLoggerUtil INSTANCE = new ProcessLoggerUtil();
// {
// try {
// // This block configure the logger with handler and formatter
// fh = new FileHandler("../../ProcessLog.log");
// logger.addHandler(fh);
// SimpleFormatter formatter = new SimpleFormatter();
// fh.setFormatter(formatter);
//
// // the following statement is used to log any messages
//// logger.info("");
//
// } catch (SecurityException se) {
// se.printStackTrace();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
//
// /**
// *
// * @param info
// */
// public static void writeInfo(String info){
// if(INSTANCE==null){
// INSTANCE = new ProcessLoggerUtil();
// }
//
// logger.info(info);
// }
//
// public static void writeSevere(String info){
// if(INSTANCE==null){
// INSTANCE = new ProcessLoggerUtil();
// }
// logger.severe(info);
// }
//
// public static ProcessLoggerUtil getInstance(){
// if(INSTANCE==null){
// INSTANCE = new ProcessLoggerUtil();
// }
// return INSTANCE;
// }
//
// }
