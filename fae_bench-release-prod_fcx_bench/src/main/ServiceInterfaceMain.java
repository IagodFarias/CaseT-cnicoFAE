package main;

import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;

import util.FaeLogSetup;
import util.ProcessLog;

import bciapi.impl.BenchControlImpl;
import bciapi.socket.BCISocketComm;
import controller.BenchDataController;
import controller.MeterController;
import controller.ProcessController;
import enumerations.MeterConnectionStatus;
import enumerations.SceneTypeEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import si.dbcomm.dao.DataBasePersistence;
import si.dbcomm.model.BatchModel;
import si.dbcomm.util.ConversorNumbers;
import util.MainMachineStateEnum;
import util.StandardProcessStatesEnum;
import util.Utilities;
import util.ViewStatesUtil;
import util.PreferencesHandler;
import view.bean.BenchBean;
import view.bean.MeterBean;
import view.configDataBase.ConfigDataBase;
import view.controller.SceneFactory;
import view.mainwindow.MainDashBoard;
import view.mainwindow.MainStartView;
import view.util.ViewDataUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;

@SuppressWarnings("deprecation")

//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ServiceInterfaceMain.java
*    @author Marcos Oliveira
*    @date 12 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================

/**
 * @author Marcos Oliveira
 *
 */
public class ServiceInterfaceMain {

	/** Logger SLF4J do ponto de entrada da aplicacao. */
	private static final Logger LOG = ProcessLog.get(ServiceInterfaceMain.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread.currentThread().setName("ServiceInterfaceMain - Thread");

		// Inicializa a infraestrutura de logs (pasta logs/ + arquivo por execucao).
		FaeLogSetup.init();
		ProcessLog.setEtapa("BOOT");
		LOG.info("=============================================================================");
		LOG.info("ETAPA INICIO: Inicializacao da aplicacao ServiceInterface (bancada de calibracao)");
		LOG.info("Arquivo de log desta execucao: {}", FaeLogSetup.getLogFilePath());
		LOG.info("Java: {} | SO: {} | Diretorio de trabalho: {}", System.getProperty("java.version"), System.getProperty("os.name"), System.getProperty("user.dir"));
		LOG.info("=============================================================================");
		long inicioBoot = ProcessLog.iniciarCronometro();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			ProcessLog.erro(LOG, "main(): Thread.sleep(1000) inicial interrompido", e);
			e.printStackTrace();
		}
		LOG.info("ETAPA INICIO: initSys() - conexao com banco de dados e socket da BCI");
		long inicioInitSys = ProcessLog.iniciarCronometro();
		initSys();
		ProcessLog.logDuracao(LOG, "initSys()", inicioInitSys);
		LOG.info("ETAPA FIM: initSys() concluida");

		LOG.info("ETAPA INICIO: abertura da janela principal (MainStartView)");
		MainStartView mainStartDash = new MainStartView();
		mainStartDash.startView();
		LOG.info("ETAPA FIM: janela principal solicitada");
		// ProcessModel process;
		ProcessController processController = null;
		boolean isPurging = false;
		
		// Use AtomicReference to allow modification of the executor
		AtomicReference<ExecutorService> executorRef = new AtomicReference<>(Executors.newFixedThreadPool(10));
		
		// Add shutdown hook to ensure executor is properly terminated when program exits
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOG.warn("ETAPA INICIO: FINALIZACAO - shutdown hook acionado, encerrando o executor service");
			System.out.println("Shutdown hook triggered - shutting down executor service gracefully");
			shutdownExecutor(executorRef.get());
			LOG.warn("ETAPA FIM: FINALIZACAO - aplicacao encerrada");
		}));
		
		// initiates process on setup mode
		ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;
		BatchModel settedBatch = null;

		// Replace busy waiting with a safer approach
		LOG.debug("Aguardando a inicializacao do controller da view (MainStartView.controller)");
		long inicioEsperaView = ProcessLog.iniciarCronometro();
		while (MainStartView.controller == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				ProcessLog.erro(LOG, "main(): espera pelo MainStartView.controller interrompida", e);
				e.printStackTrace();
			}
		}
		LOG.info("Controller da view pronto apos {} ms", ProcessLog.duracaoMs(inicioEsperaView));

		LOG.info("ETAPA INICIO: INICIALIZACAO DA BANCADA - verificando conexao com a BCI");
		long inicioBci = ProcessLog.iniciarCronometro();
		if (BenchControlImpl.getInstance().checkConnectionWithBci() == true) {
			LOG.info("BCI conectada. Iniciando thread de leitura de sensores (BenchDataController)");
			BenchDataController.getInstance().initiateThread();
			LOG.info("Enviando comando STOP para a bancada");
			BenchControlImpl.getInstance().stop();
			LOG.info("Enviando comando RESET para a bancada");
			BenchControlImpl.getInstance().reset();
			LOG.info("ETAPA FIM: INICIALIZACAO DA BANCADA concluida em {} ms", ProcessLog.duracaoMs(inicioBci));
		} else {
			LOG.error("ETAPA FALHA: INICIALIZACAO DA BANCADA - BCI NAO respondeu. Sensores e atuadores ficarao indisponiveis. Verifique o IP em socketConfig.xml");
		}
		ProcessLog.logDuracao(LOG, "BOOT completo", inicioBoot);
		ProcessLog.limparEtapa();

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch (ViewStatesUtil.mainStates) {
				case SETUP:
					// // Retirado: Osmenio Santos - 15/02/19 09:40
					// // Gets the selected batch in view;
					// ViewStatesUtil.setupComplete = false;
					// if (mainStartDash.getController().getSelectedBatch() != null) {
					// if (processController == null) {
					// settedBatch = mainStartDash.getController().getSelectedBatch();
					//
					// process = new ProcessModel();
					// processController = new ProcessController(process, settedBatch);
					// }
					// ViewStatesUtil.mainStates = MainMachineStateEnum.CONNECT_METERS;
					// }

					//
					ViewStatesUtil.mainStates = MainMachineStateEnum.CONNECT_METERS;
				break;

				case CONNECT_METERS:
				System.out.println("ServiceInterfaceMain.CONNECT_METERS");
					ProcessLog.setEtapa("CONNECT_METERS");
					LOG.info("ETAPA INICIO: CONNECT_METERS - conexao dos 20 medidores da bancada");
					long inicioConnectMeters = ProcessLog.iniciarCronometro();

					settedBatch = mainStartDash.getController().getSelectedBatch();
					if (processController == null) {
						if (settedBatch != null) {
							processController = new ProcessController(settedBatch);
						}
					} else {
						if (processController.getBatch() == null) {
							if (settedBatch != null) {
								processController = new ProcessController(settedBatch);
							}
						}
					}

					//
					if (processController != null) {
						if (!processController.isRunning()) {
							ConversorNumbers[] conversorNumber = new ConversorNumbers[20];
							MeterController[] metercontrollers = new MeterController[20];
							conversorNumber[0] = ConversorNumbers.CONVERSOR1;
							// For connects COM and checks first package to assure meter is connected
							for (int i = 0; i < 20; i++) {
								System.out.println("ServiceInterfaceMain.CONNECT_METERS: Connecting meter " + conversorNumber[i]);
								ProcessLog.setMedidor(conversorNumber[i]);
								LOG.info("ETAPA INICIO: conexao do medidor {} (indice {}/20)", conversorNumber[i], i + 1);
								long inicioConexaoMedidor = ProcessLog.iniciarCronometro();
								metercontrollers[i] = processController.connectSingleMeter(conversorNumber[i]);
								LOG.info("Retorno de connectSingleMeter({}): {} - em {} ms", conversorNumber[i],
										(metercontrollers[i] != null ? "CONECTADO" : "NULO (falha de conexao)"),
										ProcessLog.duracaoMs(inicioConexaoMedidor));
								if (metercontrollers[i]  != null) {

									// Check fw version
									String fwVersion = Utilities.parseVersionToString(metercontrollers[i] .getFwVersion());
									LOG.info("Versao de firmware lida do medidor {}: {}", conversorNumber[i], fwVersion);
									//mude para true para testar a versão fcx 3.0
									if (/*fwVersion.equals(settedBatch.getFirmware().getVersion())*/true) {
										// if (fwVersion.equals(settedBatch.getFwVersion())) {
										ViewStatesUtil.resultConnectSingleMeter = MeterConnectionStatus.CONNECTED;

										processController.updateMeterControllers(metercontrollers[i] );
										MeterBean bean = new MeterBean(metercontrollers[i] .getMeter());
										metercontrollers[i] .getSocketThread().addObserver(bean);
										ViewDataUtil.meterBeans.put(conversorNumber[i], bean);
										metercontrollers[i] .setMeterBean(bean);
										ViewStatesUtil.minMeter = true;
										ViewStatesUtil.setupComplete = true;
										// ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;
										ProcessController controller_thread = processController; // Copy reference
										ConversorNumbers conversorNumbers_thread = conversorNumber[i];
										MeterController meterController_thread = metercontrollers[i];
										executorRef.get().submit(() -> {
											// Propaga a identificacao do medidor para a thread do executor,
											// para que todas as linhas de log dessa leitura fiquem marcadas.
											ProcessLog.setMedidor(conversorNumbers_thread);
											ProcessLog.setEtapa("START_METER_READING");
											LOG.info("ETAPA INICIO: startMeterReading do medidor {}", conversorNumbers_thread);
											long inicioLeitura = ProcessLog.iniciarCronometro();
											try {
												if (controller_thread.startMeterReading(meterController_thread)) {
													LOG.info("ETAPA FIM: startMeterReading do medidor {} concluida com SUCESSO em {} ms", conversorNumbers_thread, ProcessLog.duracaoMs(inicioLeitura));
													System.out.println("ServiceInterfaceMain.CONNECT_METERS: Success startMeterReading: " + conversorNumbers_thread);
												} else {
													LOG.error("ETAPA FALHA: startMeterReading do medidor {} retornou FALSE apos {} ms", conversorNumbers_thread, ProcessLog.duracaoMs(inicioLeitura));
													System.out.println("ServiceInterfaceMain.CONNECT_METERS: ERROR startMeterReading");
												}
											} catch (RuntimeException reLog) {
												// Registra e re-lanca: nao altera o comportamento anterior,
												// em que a excecao subia para o executor.
												ProcessLog.critico(LOG, "startMeterReading do medidor " + conversorNumbers_thread, reLog);
												throw reLog;
											} finally {
												ProcessLog.limparContexto();
											}
										});

									} else {
						ViewStatesUtil.resultConnectSingleMeter = MeterConnectionStatus.DIFF_FW_VERSION;
									}
									//
								} else {
									LOG.error("ETAPA FALHA: medidor {} (posicao {}/20) NAO conectou - connectSingleMeter retornou null", conversorNumber[i], i + 1);
								}
								ProcessLog.limparMedidor();
								if(i + 1 < 20)
								{
									conversorNumber[i+1] = conversorNumber[i].next();
								}
								
								// Add delay between meter connections to allow hardware to stabilize

							}
							int meters_running = 0;
							boolean[] updated = new boolean[metercontrollers.length]; // Track updated indices
							System.out.println("ServiceInterfaceMain.CONNECT_METERS: Starting to monitor " + metercontrollers.length + " meters for reading status");
							long startTime = System.currentTimeMillis();
							while(meters_running < 20)	
							{
								for(int i = 0; i < metercontrollers.length; i++)
								{
									if (metercontrollers[i] != null && metercontrollers[i].isReading && !updated[i])
									{
										System.out.println("ServiceInterfaceMain.CONNECT_METERS: Meter " + i + " (Conversor: " + conversorNumber[i] + ") is now reading - updating view");
										LOG.info("[{}] Medidor {} passou a transmitir leituras - atualizando a view. Total: {}/20",
												ProcessLog.tagMedidor(conversorNumber[i]), conversorNumber[i], meters_running + 1);
										mainStartDash.getController().updateConversorView(conversorNumber[i], ViewStatesUtil.resultConnectSingleMeter);
										meters_running++;
										updated[i] = true;
										System.out.println("ServiceInterfaceMain.CONNECT_METERS: Total meters running now: " + meters_running + "/20");
									}
								}
								
								// Add timeout check to prevent infinite loop
								if (System.currentTimeMillis() - startTime > 60000) { // 1 minute timeout
									System.out.println("ServiceInterfaceMain.CONNECT_METERS: WARNING - Timeout waiting for meters to start reading. Only " + meters_running + "/20 meters running");
									LOG.warn("TIMEOUT de 60 s aguardando os medidores iniciarem leitura. Apenas {}/20 estao transmitindo", meters_running);
									for (int d = 0; d < metercontrollers.length; d++) {
										if (metercontrollers[d] == null || !metercontrollers[d].isReading) {
											LOG.warn("[{}] Medidor {} NAO esta transmitindo no momento do timeout", ProcessLog.tagMedidor(d + 1), conversorNumber[d]);
										}
									}
									break;
								}

								// Small sleep to prevent CPU hogging
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									System.out.println("ServiceInterfaceMain.CONNECT_METERS: Sleep interrupted while waiting for meters");
									ProcessLog.erro(LOG, "CONNECT_METERS: espera pelos medidores interrompida", e);
									e.printStackTrace();
								}
							}
							System.out.println("ServiceInterfaceMain.CONNECT_METERS: Finished monitoring meters. " + meters_running + " meters are reading.");
							LOG.info("ETAPA FIM: CONNECT_METERS - {}/20 medidores transmitindo, em {} ms", meters_running, ProcessLog.duracaoMs(inicioConnectMeters));
							
							// Thread monitoring and cleanup - BEGIN
							if (meters_running < 20) {
								System.out.println("ServiceInterfaceMain.CONNECT_METERS: Not all meters are reading (" + meters_running + "/20). Performing thread diagnostics.");
								LOG.warn("Nem todos os medidores estao lendo ({}/20). Iniciando diagnostico de threads.", meters_running);

								// Get thread diagnostic information
								Thread[] threadArray = new Thread[Thread.activeCount()];
								int threadCount = Thread.enumerate(threadArray);
								System.out.println("ServiceInterfaceMain.CONNECT_METERS: Active threads: " + threadCount);
								LOG.warn("Threads ativas no momento do diagnostico: {}", threadCount);

								for (int i = 0; i < threadCount; i++) {
									Thread t = threadArray[i];
									if (t != null) {
										System.out.println("Thread #" + i + " - Name: " + t.getName() +
												", State: " + t.getState() +
												", Priority: " + t.getPriority() +
												", Daemon: " + t.isDaemon());
										LOG.debug("Thread #{} - Nome: {}, Estado: {}, Prioridade: {}, Daemon: {}", i, t.getName(), t.getState(), t.getPriority(), t.isDaemon());
									}
								}
								
								// Force a clean shutdown and recreate the executor if needed
								System.out.println("ServiceInterfaceMain.CONNECT_METERS: Performing executor service cleanup");
								LOG.warn("Recriando o executor service apos falha parcial de conexao dos medidores");
								shutdownExecutor(executorRef.get());
								executorRef.set(Executors.newFixedThreadPool(10));
								System.out.println("ServiceInterfaceMain.CONNECT_METERS: Executor service recreated");
								LOG.info("Executor service recriado com sucesso");
								
								// Force garbage collection to clean up lingering objects
								System.gc();
							}
							// Thread monitoring and cleanup - END
						}
					}
					ProcessLog.limparContexto();
					ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;

				break;

				case CONNECT_SINGLE_METER:
					ProcessLog.setEtapa("CONNECT_SINGLE_METER");
					LOG.info("ETAPA INICIO: CONNECT_SINGLE_METER - conexao de um unico medidor: {}", ViewStatesUtil.conversorNum);

					settedBatch = mainStartDash.getController().getSelectedBatch();
					if (processController == null) {
						if (settedBatch != null) {
							processController = new ProcessController(settedBatch);
						}
					} else {
						if (processController.getBatch() == null) {
							if (settedBatch != null) {
								processController = new ProcessController(settedBatch);
							}
						}
					}

					//
					if (processController != null) {
						if (!processController.isRunning()) {

							ConversorNumbers conversorNumber = ViewStatesUtil.conversorNum;
							ProcessLog.setMedidor(conversorNumber);

							long inicioConexaoUnica = ProcessLog.iniciarCronometro();
							MeterController meterController = processController.connectSingleMeter(conversorNumber);
							LOG.info("Retorno de connectSingleMeter({}): {} - em {} ms", conversorNumber,
									(meterController != null ? "CONECTADO" : "NULO (falha de conexao)"),
									ProcessLog.duracaoMs(inicioConexaoUnica));
							if (meterController != null) {

								// Check fw version
								String fwVersion = Utilities.parseVersionToString(meterController.getFwVersion());
								LOG.info("Versao de firmware lida do medidor {}: {}", conversorNumber, fwVersion);
								//mude para true para testar a versão fcx 3.0
								if (/*fwVersion.equals(settedBatch.getFirmware().getVersion())*/true) {
									// if (fwVersion.equals(settedBatch.getFwVersion())) {
									ViewStatesUtil.resultConnectSingleMeter = MeterConnectionStatus.CONNECTED;

									processController.updateMeterControllers(meterController);
									MeterBean bean = new MeterBean(meterController.getMeter());
									meterController.getSocketThread().addObserver(bean);
									ViewDataUtil.meterBeans.put(conversorNumber, bean);
									meterController.setMeterBean(bean);
									ViewStatesUtil.minMeter = true;
									ViewStatesUtil.setupComplete = true;
									// ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;
									// System.out.println("ServiceInterfaceMain.CONNECT_SINGLE_METER: Success startMeterReading");
									if (processController.startMeterReading(meterController)) {
										System.out.println("ServiceInterfaceMain.CONNECT_SINGLE_METER: Success startMeterReading");
										LOG.info("ETAPA FIM: startMeterReading do medidor {} concluida com SUCESSO", conversorNumber);
									} else {
										System.out.println("ServiceInterfaceMain.CONNECT_SINGLE_METER: ERROR startMeterReading");
										LOG.error("ETAPA FALHA: startMeterReading do medidor {} retornou FALSE", conversorNumber);
									}
								} else {
									LOG.warn("Medidor {} REPROVADO na checagem de firmware: versao divergente", conversorNumber);
									ViewStatesUtil.resultConnectSingleMeter = MeterConnectionStatus.DIFF_FW_VERSION;
								}
								//
								mainStartDash.getController().updateConversorView(conversorNumber, ViewStatesUtil.resultConnectSingleMeter);
							} else {
								LOG.error("ETAPA FALHA: medidor {} NAO conectou - connectSingleMeter retornou null", conversorNumber);
							}
						}
					}
					LOG.info("ETAPA FIM: CONNECT_SINGLE_METER");
					ProcessLog.limparContexto();
					ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;

				break;

				case PURGE:
					if (!isPurging) {
						isPurging = true;
						ProcessLog.setEtapa("PURGE");
						LOG.info("ETAPA INICIO: PURGE - procedimento de purga da linha");
						long inicioPurga = ProcessLog.iniciarCronometro();
						mainStartDash.getController().setFlagPurge(true);

						settedBatch = mainStartDash.getController().getSelectedBatch();
						if (processController == null) {
							if (settedBatch != null) {
								processController = new ProcessController(settedBatch);
							} else {
								processController = new ProcessController();
							}
						} else {
							if (processController.getBatch() == null) {
								if (settedBatch != null) {
									processController = new ProcessController(settedBatch);
								} else {
									processController = new ProcessController();
								}
							}
						}

						if ((processController.getRunningState() == StandardProcessStatesEnum.WAIT) || (processController.getRunningState() == StandardProcessStatesEnum.INITIAL_RUN_CONFIGURATION)) {
							if (processController.purge()) {
								// if (processController.simplifiedPurge()) {
								LOG.info("ETAPA FIM: PURGE concluida com SUCESSO em {} ms", ProcessLog.duracaoMs(inicioPurga));
								ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;
								isPurging = false;
							} else {
								LOG.warn("ETAPA FALHA: PURGE nao concluiu (purge() retornou false) apos {} ms", ProcessLog.duracaoMs(inicioPurga));
							}
						} else {
							System.out.println("ServiceInterfaceMain.PURGE =! ViewStatesUtil.mainStates.WAIT");
							LOG.warn("PURGE ignorada: estado do processo e {} (esperado WAIT ou INITIAL_RUN_CONFIGURATION)", processController.getRunningState());
						}
						// ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;
						// processController = null;
						mainStartDash.getController().setFlagPurge(false);
						isPurging = false;
						ProcessLog.limparEtapa();
					}
				break;

				case RUN:
					ProcessLog.setEtapa("RUN");
					LOG.info("ETAPA INICIO: RUN - solicitacao de inicio do processo de calibracao");
					if (ViewStatesUtil.setupComplete) {
						// if (processController == null) {
						// settedBatch = mainStartDash.getController().getSelectedBatch();
						// if (settedBatch != null) {
						// processController = new ProcessController(settedBatch);
						// }
						// }
						LOG.info("Setup completo. Disparando executeProcess()");
						processController.executeProcess();
						LOG.info("ETAPA FIM: RUN - processo de calibracao disparado");
						ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;
					} else {
						LOG.warn("RUN ignorado: setup ainda NAO esta completo (nenhum medidor conectado com sucesso)");
					}
					ProcessLog.limparEtapa();
				// if (ViewStatesUtil.setupComplete) {
				// if (!processController.isRunning()) {
				// processController.executeProcess();
				// ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;
				// } else {
				// System.out.println("ServiceInterfaceMain.RUN - processController.isRunning()");
				// }
				// } else {
				//
				// }
				break;

				case STOP:
					ProcessLog.setEtapa("STOP");
					if (processController.isRunning()) {
						LOG.warn("ETAPA INICIO: STOP - parada solicitada para o processo em execucao");
						long inicioStop = ProcessLog.iniciarCronometro();
						processController.stopProcess();
						// Clean up executor when stopping
						shutdownExecutor(executorRef.get());
						// Create a new executor for future use
						executorRef.set(Executors.newFixedThreadPool(10));
						LOG.warn("ETAPA FIM: STOP concluida em {} ms - executor recriado", ProcessLog.duracaoMs(inicioStop));
						ViewStatesUtil.mainStates = MainMachineStateEnum.WAIT;
					} else {
						LOG.info("STOP ignorado: nenhum processo em execucao");
					}
					ProcessLog.limparEtapa();
				break;

				case WAIT:
				break;
			}
		}
	}

	/**
	 * Properly shuts down an executor service with timeout
	 * @param executor The executor service to shut down
	 */
	private static void shutdownExecutor(ExecutorService executor) {
		LOG.debug("ENTRADA shutdownExecutor(executor={})", executor);
		if (executor != null && !executor.isShutdown()) {
			try {
				System.out.println("Attempting to shutdown executor service gracefully");
				LOG.info("FINALIZACAO: encerrando o executor service de forma graciosa");
				// Disable new tasks from being submitted
				executor.shutdown();
				// Wait a while for existing tasks to terminate
				if (!executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
					System.out.println("Executor did not terminate in the allotted time. Forcing shutdown.");
					LOG.warn("FINALIZACAO: executor nao encerrou em 5 s. Forcando shutdownNow()");
					// Cancel currently executing tasks
					executor.shutdownNow();
					// Wait a while for tasks to respond to being cancelled
					if (!executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
						System.err.println("Executor did not terminate");
						ProcessLog.critico(LOG, "FINALIZACAO: executor NAO encerrou nem apos shutdownNow(). Threads podem ter vazado.");
					}
				} else {
					LOG.info("FINALIZACAO: executor encerrado com sucesso");
				}
			} catch (InterruptedException ie) {
				ProcessLog.erro(LOG, "shutdownExecutor(): espera pelo encerramento interrompida", ie);
				// (Re-)Cancel if current thread also interrupted
				executor.shutdownNow();
				// Preserve interrupt status
				Thread.currentThread().interrupt();
			}
		} else {
			LOG.debug("shutdownExecutor(): executor nulo ou ja encerrado - nada a fazer");
		}
		LOG.debug("SAIDA shutdownExecutor()");
	}

	// public ProcessController setupProcessController(BatchModel batch) {
	// ProcessController processController = new ProcessController();
	// // Gets the selected batch in view;
	// ViewStatesUtil.setupComplete = false;
	// if (mainStartDash.getController().getSelectedBatch() != null) {
	// if (processController == null) {
	// settedBatch = mainStartDash.getController().getSelectedBatch();
	//
	// process = new ProcessModel();
	// processController = new ProcessController(process, settedBatch);
	// }
	// ViewStatesUtil.mainStates = MainMachineStateEnum.CONNECT_METERS;
	// }
	//
	// return processController;
	// }

	static boolean connectionSuccess = false;
	public static void initSys() {
		LOG.debug("ENTRADA initSys()");
		// Database connections
		String urlDataBase = PreferencesHandler.readDataBaseUrl();
		String userDataBase = PreferencesHandler.readDataBaseUserName();
		String passwordDataBase = PreferencesHandler.readDataBasePassword();
		LOG.debug("Preferences lidas (NAO usadas, ver abaixo). URL: '{}' | usuario: '{}'", urlDataBase, userDataBase);
		// ATENCAO: connectionSuccess e forcado a false de proposito. Com isso o
		// bloco abaixo SEMPRE roda e o banco usado e SEMPRE o endereco fixo --
		// as Preferences acima sao lidas e descartadas (a tela
		// "Configuracao > Configuracoes data base" nao tem efeito enquanto isto
		// estiver assim). Comportamento original da versao da empresa.
		connectionSuccess = false;
		if(!connectionSuccess){
			// ================================================================
			// BANCO DE DADOS -- so um destes pode ficar ativo.
			//
			//   EMPRESA (ativo):
			urlDataBase = "127.0.0.1:5432/ufae_bench_prod";
			//
			//   TESTE local (simulador): comente a linha acima e descomente:
			// urlDataBase = "127.0.0.1:55432/ufae_bench_test";
			// ================================================================
			userDataBase = "postgres";
			passwordDataBase = "postgres";
			LOG.info("Conectando ao banco de dados. URL: {} | usuario: {}", urlDataBase, userDataBase);
			long inicioDb = ProcessLog.iniciarCronometro();
			connectionSuccess = DataBasePersistence.initialize(urlDataBase, userDataBase, passwordDataBase);
			LOG.info("Retorno de DataBasePersistence.initialize(): {} - em {} ms", connectionSuccess, ProcessLog.duracaoMs(inicioDb));
		}
		if (!connectionSuccess) {
			ProcessLog.critico(LOG, "Nao foi possivel conectar ao banco de dados em nenhuma das URLs. Resultados de calibracao NAO poderao ser gravados.");
		}
		LOG.info("Iniciando socket de comunicacao com a BCI (BCISocketComm)");
		BCISocketComm.BCISocketCommStart(); // first we connect to the api to assure we can command the BCI
		LOG.info("Iniciando BenchBean");
		BenchBean.benchBeanStart();
		LOG.info("Iniciando BenchDataController (leitura de sensores da bancada)");
		BenchDataController.benchDataControllerStart();

		LOG.debug("SAIDA initSys() - conexao com banco: {}", connectionSuccess);
	}
}
