//=============================================================================
/*!
 *    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ProcessBean.java
*    @author Marcos
*    @date 17 de jun de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;

import util.ProcessLog;

import enumerations.CalibrationErrorEnum;
import exceptions.CryptoMd5Digest;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import si.dbcomm.enumerations.CalibrationTypeEnum;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.ConversorModel;
import si.dbcomm.model.CriticalPointModel;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.LogChangeProcessBatchModel;
import si.dbcomm.model.MeterDataModel;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.ProcessConfigModel;
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.model.VerificationErrorModel;
import si.dbcomm.service.BatchService;
import si.dbcomm.service.BateladaService;
import si.dbcomm.service.ConversorService;
import si.dbcomm.service.CriticalPointService;
import si.dbcomm.service.LogChangeProcessBatchService;
import si.dbcomm.util.ConversorNumbers;
import util.MatFileHandlerUtil;
import util.PreferencesHandler;
import util.ProcessFlowRateEnum;
import util.ProcessLoggerUtil;
import util.StandardProcessStatesEnum;
import util.ViewStatesUtil;
import view.bean.BenchBean;
import view.bean.MeterBean;
import view.util.ViewDataUtil;

/**
 * @author Marcos
 *
 */
public class ProcessController implements Runnable {

	/** Logger SLF4J da maquina de estados do processo de calibracao. */
	private static final Logger LOG = ProcessLog.get(ProcessController.class);

	/** Marca o inicio da etapa corrente, para medir a duracao de cada estado. */
	private long inicioEtapaNano = System.nanoTime();

	private Thread processControllThread;

	private List<MeterController> meterControllers = new ArrayList<>();

	private BenchController benchController = new BenchController();

	private BenchDataController benchDataController = BenchDataController.getInstance();

	private BatchModel batch;

	private BateladaModel batelada;

	private boolean isReWork = false;

	private boolean running = false;

	private boolean stopProcess = true;

	// private boolean displayFailedCalibDown = true;

	private BatchController batchController = new BatchController();

	private HashMap<ConversorNumbers, MeterController> verifiedConnectedMeters = new HashMap<>();

	private StandardProcessStatesEnum runningState = StandardProcessStatesEnum.INITIAL_RUN_CONFIGURATION;

	private ArrayList<FlowRateModel> calibFlowRates = new ArrayList<>();

	private ArrayList<FlowRateModel> verifFlowRates = new ArrayList<>();

	private MatFileHandlerUtil matFile = new MatFileHandlerUtil();

	private Property<Boolean> forceCancelRepeatObservable;

	private Property<Boolean> onlyVerificationObservable;
	
	private ProcessConfigModel processConfig = new ProcessConfigModel();

	private ProcessFlowRateEnum processFlowRateState = ProcessFlowRateEnum.INIT;

	private ProcessFlowRateEnum processVerifFlowRateState = ProcessFlowRateEnum.INIT;
	
	private CriticalPointService criticalPointService = new CriticalPointService();

	/**
	 * Creates a object for class ProcessController.java
	 */
	public ProcessController() {
	}

	/**
	 * Creates a object for class ProcessController.java
	 */
	public ProcessController(BatchModel batch) {
		this.batch = batch;
		this.isReWork = batch.isReWork();

		initializeLinkToView();
	}

//	/**
//	 * Creates a object for class ProcessController.java
//	 */
//	public ProcessController(ProcessModel process, BatchModel batch) {
//		this.batch = batch;
//		this.isReWork = batch.isReWork();
//
//		initializeLinkToView();
//	}

	public void orderFlowRateCrescent(ArrayList<FlowRateModel> calibConstants) {
		Collections.sort(calibConstants, new Comparator<FlowRateModel>() {
			public int compare(FlowRateModel const1, FlowRateModel const2) {
				return const1.getFlowRate() < const2.getFlowRate() ? -1 : (const1.getFlowRate() > const2.getFlowRate()) ? 1 : 0;
			}
		});
	}

	
	public void orderFlowRateDecrescent(ArrayList<FlowRateModel> calibConstants) {
		Collections.sort(calibConstants, new Comparator<FlowRateModel>() {
			public int compare(FlowRateModel const1, FlowRateModel const2) {
				return const1.getFlowRate() > const2.getFlowRate() ? -1 : (const1.getFlowRate() < const2.getFlowRate()) ? 1 : 0;
			}
		});
	}

	
	public void orderMeterControllerCrescentByConversor(List<MeterController> meterControllers) {
		Collections.sort(meterControllers, new Comparator<MeterController>() {
			public int compare(MeterController const1, MeterController const2) {
				return const1.getMeter().getConversor().getId() < const2.getMeter().getConversor().getId() ? -1
						: (const1.getMeter().getConversor().getId() > const2.getMeter().getConversor().getId()) ? 1 : 0;
			}
		});
	}

	/**
	 * Function returns the value of attribute meters
	 * 
	 * @return the meters
	 */
	public List<MeterController> getMeters() {
		return meterControllers;
	}

	/**
	 * Function sets the value for attribute meters
	 * 
	 * @param meters
	 *            the meters to set
	 */
	public void setMeterControllers(List<MeterController> meters) {
		this.meterControllers = meters;
	}

	/**
	 * Executes the process
	 */
	public void run() {
		Thread.currentThread().setName("Process Controller - Thread");
		ProcessLog.setEtapa("PROCESSO");
		LOG.info("=============================================================================");
		LOG.info("ETAPA INICIO: thread da maquina de estados do processo de calibracao iniciada");
		LOG.info("Lote (batch): {} | reWork: {} | medidores conectados: {}",
				(batch != null ? batch.getBatchId() : "n/d"), isReWork, meterControllers.size());
		LOG.info("=============================================================================");
		long inicioProcessoNano = ProcessLog.iniciarCronometro();

		StandardProcessStatesEnum auxState = StandardProcessStatesEnum.WAIT;
		boolean stopProcessAux = false;
		boolean hasRunnedInitConfiguration = false;

		while (running) {
			// This conditional is to avoid printing the state on process log without change
			if (auxState != runningState) {
				if (hasRunnedInitConfiguration) {
					ProcessLoggerUtil.writeInfo("************************* State machine to run: " + runningState.toString() + " *************************");
				}
				// Fecha a etapa anterior (com o tempo gasto) e abre a nova.
				LOG.info("ETAPA FIM: {} - duracao {} ms", auxState, ProcessLog.duracaoMs(inicioEtapaNano));
				inicioEtapaNano = ProcessLog.iniciarCronometro();
				ProcessLog.setEtapa(runningState.toString());
				LOG.info("ETAPA INICIO: {} (transicao {} -> {})", runningState, auxState, runningState);
				auxState = runningState;
				BenchBean.getInstance().setRunningState(runningState.toString());
			}
			// Check stop process
			if (stopProcess) {
				runningState = StandardProcessStatesEnum.WAIT;
				processFlowRateState = ProcessFlowRateEnum.WAIT;
				processVerifFlowRateState = ProcessFlowRateEnum.WAIT;
				if (!stopProcessAux) {
					stopProcessAux = true;
					if (hasRunnedInitConfiguration) {
						ProcessLoggerUtil.writeInfo("---------------------------- State machine to run: STOP ----------------------------");
					}
					LOG.warn("PARADA solicitada: maquina de estados forcada para WAIT apos {} ms de processo", ProcessLog.duracaoMs(inicioProcessoNano));
				}
			} else {
				stopProcessAux = false;
			}

			switch (runningState) {
				case INITIAL_RUN_CONFIGURATION:

					if (!batchController.checkForBateladas(batch)) {
						batchController.createInitalBatelada(batch);
					}
					int incRunCountBatelada = batchController.incBatchRunCount(batch.getBatchId());

					ProcessLoggerUtil.createDir(this.batch.getBatchId(), incRunCountBatelada);
					ProcessLoggerUtil.writeInfo("---------------------------- Starting process at " + new Date().toString() + " Batch " + batch.getBatchId() + " ---------------------------- ");
					LOG.info("Execucao numero {} do lote {}", incRunCountBatelada, batch.getBatchId());

					batelada = batchController.getLastBateladaStored();
					batelada.setReWork(isReWork);
					if(BenchBean.getInstance().getCalibrationTypeProperty().getValue() != null)
					batelada.setCalibrationType(CalibrationTypeEnum.getCalibrationType(BenchBean.getInstance().getCalibrationTypeProperty().getValue()));
					
					
					processConfig = batelada.getProcessesConfigModel();
					processConfig.setRadioWmBus(batch.getMeterType().isRadioWmBus());

					//
					if(batelada.getCalibrationType() == CalibrationTypeEnum.FULL_PROD){
						calibFlowRates.addAll(processConfig.getCalibFlowRates());
					}else if(batelada.getCalibrationType() == CalibrationTypeEnum.ESTIMATED_CONST){
						List<FlowRateModel> flowrates = new ArrayList();
						List<CriticalPointModel> criticalPoints = criticalPointService.findAllByBatch(batelada.getBatch());
						for(CriticalPointModel criticalPoint : criticalPoints){
							flowrates.add(criticalPoint.getFlowRate());
						}
						calibFlowRates.addAll(flowrates);
					}
					
					verifFlowRates.addAll(processConfig.getVerifFlowRates());
					orderFlowRateDecrescent(calibFlowRates);
					orderFlowRateDecrescent(verifFlowRates);
					LOG.info("Tipo de calibracao: {} | vazoes de calibracao: {} | vazoes de verificacao: {}",
							batelada.getCalibrationType(), calibFlowRates.size(), verifFlowRates.size());

					orderMeterControllerCrescentByConversor(meterControllers);
					for (MeterController meterController : meterControllers) {
						ProcessLog.setMedidor(meterController.getMeter().getConversor().getName());
						LOG.debug("Vinculando medidor a batelada. IP: {} | firmware do lote: {}",
								meterController.getMeter().getConversor().getIp(), batch.getFirmware());
						meterController.getMeter().setBatelada(batelada);
						meterController.getMeter().setFirmware(batch.getFirmware());
						meterController.updateMeterUfoIdAndFwVersion();
						batelada.getMeters().add(meterController.getMeter());
					}
					ProcessLog.limparMedidor();
					LOG.info("Configuracao inicial concluida para {} medidores", meterControllers.size());

					batelada.setMinFlowRatePermissibleError(batch.getMeterType().getMinFlowRatePermissibleError());
					batelada.setNominalFlowRatePermissibleError(batch.getMeterType().getNominalFlowRatePermissibleError());
					batelada.setMaxFlowRatePermissibleError(batch.getMeterType().getMaxFlowRatePermissibleError());
					batelada.setConnectedMeters(meterControllers.size());
					BenchBean.getInstance().setBateladaCount(batelada.getRunCount());
					runningState = StandardProcessStatesEnum.CLOSELINE;

					BenchBean.getInstance().setBatelada(batelada);
					hasRunnedInitConfiguration = true;

				// runningState = StandardProcessStatesEnum.SAVE_METER_CONFIG_MAT_DATA;
				break;

				case CLOSELINE:
					if (benchController.closeLine()) {
						LOG.info("Linha fechada com sucesso. Somente verificacao: {}", onlyVerificationObservable.getValue());
						if (onlyVerificationObservable.getValue()) {
							runningState = StandardProcessStatesEnum.VERIF;
							// batelada.setOnlyVerification(true);
						} else {
							runningState = StandardProcessStatesEnum.DOWNLOAD_INIT_CALIB_VALUES;
						}
					} else {
						LOG.warn("closeLine() retornou FALSE - a linha nao foi fechada. Permanecendo em CLOSELINE.");
					}
				break;

				case DOWN_FIXED_CONSTS:

				break;

				case DOWNLOAD_INIT_CALIB_VALUES:
					int countReprovedDownloadInitConstants = 0;
					LOG.info("Enviando constantes iniciais de calibracao para {} medidores. Temperatura inicial: {}",
							meterControllers.size(), processConfig.getInitCalibTemp());
					for (MeterController meterController : meterControllers) {
						ProcessLog.setMedidor(meterController.getMeter().getConversor().getName());
						boolean downloadOk = false;
						if (!meterController.isCommDown()) {
							// if (!meterController.isDownloadedInitContants()) {// this is to assure the sw does not try to resend data to same meter
							if (meterController.getCalibService().getMeterController() == null) { // transfered the instantiation of this object to the meter controller
								meterController.getCalibService().setMeterController(meterController);
							}
							long inicioEnvioInit = ProcessLog.iniciarCronometro();
							for (int i = 0; i < 20; i++) {
								LOG.debug("Tentativa {}/20 de envio das constantes iniciais", i + 1);
								if (meterController.sendInitCalibrationData(processConfig.getInitCalibTemp(), meterController.getMeter())) {
									ProcessLoggerUtil.writeInfo("SUCCESS: Initial calibration data sent to position: " + meterController.getMeter().getConversor().getIp());
									LOG.info("SUCESSO: constantes iniciais enviadas ao medidor (IP {}) na tentativa {} de 20, em {} ms",
											meterController.getMeter().getConversor().getIp(), i + 1, ProcessLog.duracaoMs(inicioEnvioInit));
									meterController.setDownloadedInitContants(true);
									downloadOk = true;
									break;
								}
							}
							if (!downloadOk) {
								LOG.error("FALHA: nao foi possivel enviar as constantes iniciais ao medidor (IP {}) apos 20 tentativas, em {} ms",
										meterController.getMeter().getConversor().getIp(), ProcessLog.duracaoMs(inicioEnvioInit));
							}
							// Nao reprova
							// if (!downloadOk) {
							// countReprovedDownloadInitConstants++;
							// ProcessLoggerUtil.writeInfo("FAILED to send initial calibration data to position: " + meterController.getMeter().getConversor().getIp());
							// meterController.setDownloadedInitContants(false);
							// meterController.setIsReproved(true);
							// diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.INIT_DOWN));
							// }
						} else {
							LOG.warn("Medidor com comunicacao caida (isCommDown). Envio das constantes iniciais ignorado. IP: {}",
									meterController.getMeter().getConversor().getIp());
						}
					}
					ProcessLog.limparMedidor();
					this.batelada.setReprovedMetersOnDownInitConst(countReprovedDownloadInitConstants);
					LOG.info("Envio de constantes iniciais concluido. Medidores reprovados: {}", countReprovedDownloadInitConstants);

					runningState = StandardProcessStatesEnum.ZEROFLOW;
				// if (allMeterComplete) {
				// runningState = StandardProcessStatesEnum.ZEROFLOW;
				// } else {
				// runningState = StandardProcessStatesEnum.DOWNLOAD_INIT_CALIB_VALUES;
				// }
				break;

				case INSERT_PRESSURE_LINE:
					if (benchController.insertPressureInLine(processConfig.getLinePressurePumpLoad(), PumpController.BP1_TAG, processConfig.getTimeCheckPressurePumpLoad())) {
						BenchBean.getInstance().getIsLeaking().turnLedOff();
						runningState = StandardProcessStatesEnum.ZEROFLOW;
					} else {
						BenchBean.getInstance().getIsLeaking().turnLedRed();
						runningState = StandardProcessStatesEnum.WAIT;
					}
				break;

				case ZEROFLOW:
					int reprovedTrim = 0;
					if (benchController.zeroFlow()) {
						ProcessLoggerUtil.writeInfo("Initiating Zero flow");
						LOG.info("ETAPA INICIO: trim AGC estagio 2 em {} medidores", meterControllers.size());
						long inicioTrimTodos = ProcessLog.iniciarCronometro();
						for (MeterController meterController : meterControllers) {
							ProcessLog.setMedidor(meterController.getMeter().getConversor().getName());

							// Approved Trim AGC
							if (!meterController.isReproved()) {
								long inicioTrim = ProcessLog.iniciarCronometro();
								if (trimMetersAgcStage2(meterController)) {
									LOG.info("SUCESSO: trim AGC estagio 2 concluido em {} ms", ProcessLog.duracaoMs(inicioTrim));
									meterController.setAgcTrimmed(true);
								} else {
									LOG.error("FALHA: trim AGC estagio 2 REPROVADO apos {} ms. IP: {}",
											ProcessLog.duracaoMs(inicioTrim), meterController.getMeter().getConversor().getIp());
									reprovedTrim++;
								}
							} else {
								LOG.warn("Medidor ja reprovado anteriormente - trim AGC ignorado");
							}

							// if (trimMetersAgcStage2(meterController)) {
							// meterController.setAgcTrimmed(true);
							// // meterController.setIsReproved(!isTrimSucces);
							// } else {
							// reprovedTrim++;
							// meterController.setAgcTrimmed(false);
							//// meterController.setIsReproved(true);
							//// diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.CALC_ZERO));
							// }
						}
						ProcessLog.limparMedidor();
						// reprovedTrim = trimMetersAgcStage2();// runs 100 times in case meter does not work
						ProcessLoggerUtil.writeInfo("Finished trimming AGC stage 2 Zero flow");
						LOG.info("ETAPA FIM: trim AGC estagio 2 - {} medidores reprovados, duracao total {} ms",
								reprovedTrim, ProcessLog.duracaoMs(inicioTrimTodos));
						ProcessLoggerUtil.writeInfo("Sampling Zero flow");
						LOG.info("ETAPA INICIO: amostragem de vazao zero (tempo configurado: {} s)", processConfig.getZeroFlowTime());
						BenchBean.getInstance().setRunningFlowRate(0);
						BenchBean.getInstance().getIsSampling().turnLedGreen();

						benchDataController.setReachedNumSamp(false);
						benchDataController.setDataCounterZeroFlow(0);

						benchDataController.setDataForZeroFlow(true);
						benchDataController.setStoreForCalibration(true);
						benchDataController.resumeThread();

						saveMetersData();
						// bench controller checks if the determined parameters for zero flow go off limits.
						long inicioZeroFlowAmostragem = ProcessLog.iniciarCronometro();
						if (benchController.assertStaticZeroFlow(1.03, 0.97, 1.03, 0.97, processConfig.getZeroFlowTime())) {
							LOG.info("Vazao zero estatica validada dentro dos limites (1.03/0.97) em {} ms. Aguardando o numero de amostras.",
									ProcessLog.duracaoMs(inicioZeroFlowAmostragem));
							long inicioEsperaAmostras = ProcessLog.iniciarCronometro();
							while (!benchDataController.isReachedNumSamp() || !this.checkAllMetersSampleCount());
							LOG.info("Numero de amostras de vazao zero atingido em todos os medidores apos {} ms", ProcessLog.duracaoMs(inicioEsperaAmostras));
							stopSaveMetersData();
							benchController.stopZeroFlow();
							benchDataController.setDataForZeroFlow(false);
							benchDataController.setStoreForCalibration(false);
							benchDataController.setDataCounterZeroFlow(0);
							collectMetersZeroFlowData();
							clearMetersData();
							BenchBean.getInstance().getIsSampling().turnLedOff();
							LOG.info("ETAPA FIM: amostragem de vazao zero concluida em {} ms", ProcessLog.duracaoMs(inicioZeroFlowAmostragem));
							runningState = StandardProcessStatesEnum.CALCULATE_ZEROFLOW;
							clearAllMetersSampleCount();
						} else {
							ProcessLog.critico(LOG, "ETAPA FALHA: vazao zero FORA dos limites estaticos (assertStaticZeroFlow retornou false) apos "
									+ ProcessLog.duracaoMs(inicioZeroFlowAmostragem) + " ms. Possivel vazamento na linha. Processo vai para WAIT.");
							// SET FLAG FOR BENCH ERROR ZEROFLOW
							stopSaveMetersData();
							benchController.stopZeroFlow();
							benchDataController.setDataForZeroFlow(false);
							benchDataController.setStoreForCalibration(false);
							clearMetersData();
							BenchBean.getInstance().getIsSampling().turnLedOff();
							BenchBean.getInstance().getIsLeaking().turnLedRed(); // turns leaking led red to demonstrate
							runningState = StandardProcessStatesEnum.WAIT;
						}
					} else {
						LOG.error("ETAPA FALHA: benchController.zeroFlow() retornou FALSE - nao foi possivel estabelecer a condicao de vazao zero");
						runningState = StandardProcessStatesEnum.WAIT;
					}
					this.batelada.setReprovedMetersOnTrimAgc(reprovedTrim);
				break;
				

				case CALCULATE_ZEROFLOW:
					int countReprovedCalcZeroFlow = 0;
					int countReprovedDesvZeroFlow = 0;
					LOG.info("Calculando parametros de vazao zero (DZC, DSOS, path length) para {} medidores. Amostras de bancada: {}",
							meterControllers.size(), benchDataController.getBenchDataZeroFlow().size());
					for (MeterController meterController : meterControllers) {
						ProcessLog.setMedidor(meterController.getMeter().getConversor().getName());
						if (!meterController.isReproved() || processConfig.isSaveMatDataReprovedMeters()) { // this conditional allows the sw to save data of reproved meters on process
							if (meterController.getCalibService().getMeterController() == null) {
								meterController.getCalibService().setMeterController(meterController);
							}
							meterController.getCalibService().setBenchDataZeroFlow(benchDataController.getBenchDataZeroFlow());

							// calculateZeroFlow(): Calculates the DZC, DSOS and ESTIMATED PATH LENGH
							long inicioCalcZero = ProcessLog.iniciarCronometro();
							if (meterController.getCalibService().calculateZeroFlow()) {
								ProcessLoggerUtil.writeInfo("SUCCESS: Calculating zeroflow for: " + meterController.getMeter().getConversor().getIp());
								LOG.info("SUCESSO: calculateZeroFlow() do medidor (IP {}) concluido em {} ms",
										meterController.getMeter().getConversor().getIp(), ProcessLog.duracaoMs(inicioCalcZero));
								// meterController.setApprovedZeroFlow(true);
								meterController.setCalculatedZeroFlow(true);
								if (meterController.getCalibService().calculateStdDeviationZeroFlow()) {
									ProcessLoggerUtil.writeInfo("SUCCESS: Calculate StdDeviation zero flow for: " + meterController.getMeter().getConversor().getIp());
									LOG.info("SUCESSO: desvio padrao de vazao zero calculado. IP: {}", meterController.getMeter().getConversor().getIp());
									meterController.setDeviationZeroFlow(true);
								} else {
									ProcessLoggerUtil.writeInfo("FAILED: Calculating StdDeviation zeroflow for: " + meterController.getMeter().getConversor().getIp());
									LOG.error("FALHA: calculo do desvio padrao de vazao zero. IP: {}", meterController.getMeter().getConversor().getIp());
									// meterController.setIsReproved(true);
									// meterController.setDeviationZeroFlow(false);
									countReprovedDesvZeroFlow++;
									// diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.CALC_ZERO));

									// // ************************
									// // Reprovacao DESVIO
									// meterController.setDeviationZeroFlow(true);
									// // ************************
								}
							} else {
								ProcessLoggerUtil.writeInfo("FAILED: Calculating zeroflow for: " + meterController.getMeter().getConversor().getIp());
								LOG.error("FALHA: calculateZeroFlow() do medidor (IP {}) retornou FALSE apos {} ms - medidor REPROVADO ({})",
										meterController.getMeter().getConversor().getIp(), ProcessLog.duracaoMs(inicioCalcZero),
										CalibrationErrorEnum.getStringError(CalibrationErrorEnum.CALC_ZERO));
								// Update view
								if (!meterController.isReproved()) {
									countReprovedCalcZeroFlow++;
									meterController.setIsReproved(true);
									meterController.setCalculatedZeroFlow(false);
									diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.CALC_ZERO));
								}
							}
						} else {
							ProcessLoggerUtil.writeInfo("FAILED: DID NOT Calculate zeroflow for: " + meterController.getMeter().getConversor().getIp() + " previously reproved in TRIM");
							LOG.warn("Calculo de vazao zero NAO executado para o medidor (IP {}): reprovado previamente no TRIM",
									meterController.getMeter().getConversor().getIp());
						}
					}
					ProcessLog.limparMedidor();
					LOG.info("Calculo de vazao zero concluido. Reprovados no calculo: {} | reprovados no desvio: {}",
							countReprovedCalcZeroFlow, countReprovedDesvZeroFlow);

					this.batelada.setReprovedMetersOnCalcZeroFlow(countReprovedCalcZeroFlow);
					// this.batelada.setReprovedDesvZeroFlow(countReprovedCalcZeroFlow);
					runningState = StandardProcessStatesEnum.DOWNLOAD_DZ_DSOS;
				break;

				case DOWNLOAD_DZ_DSOS:
					int countReprovedDownloadZeroFlow = 0;
					LOG.info("Enviando DZ/DSOS (dados de vazao zero) para os medidores. Tipo de calibracao: {}", batelada.getCalibrationType());
					for (MeterController meterController : meterControllers) {
						ProcessLog.setMedidor(meterController.getMeter().getConversor().getName());
						if (!meterController.isReproved()) {
							boolean downloadOk = false;
							long inicioEnvioZero = ProcessLog.iniciarCronometro();
							// Reprovacao DESVIO
							// if (meterController.isDeviationZeroFlow()) {
							//
							for (int i = 0; i <= 200; i++) { // processConfig.getMeterDownloadConstantsTries(); i++) {
								if (batelada.getCalibrationType() == CalibrationTypeEnum.FIXED_CONST) {
									double tempMediaZero = meterController.getCalibService().meanBenchTemperatureZeroFlow();
									LOG.debug("Tentativa {}/201 de envio (FIXED_CONST). Temperatura media de vazao zero: {}", i + 1, tempMediaZero);
									if (meterController.sendInitFixedCalibrationData(tempMediaZero, meterController.getMeter())) {
										ProcessLoggerUtil.writeInfo("SUCCESS: Zero Flow data sent to position: " + meterController.getMeter().getConversor().getIp());
										LOG.info("SUCESSO: dados de vazao zero (FIXED_CONST) enviados ao medidor (IP {}) na tentativa {}, em {} ms",
												meterController.getMeter().getConversor().getIp(), i + 1, ProcessLog.duracaoMs(inicioEnvioZero));
										downloadOk = true;
										meterController.setDownloadedZeroFlow(true);
										// For 10 min fixed const are sent with the zeroflow
										meterController.setDownloadedConstants(true);
										break;
									} else {
										try {
											Thread.currentThread().sleep(30);
										} catch (InterruptedException e) {
											ProcessLog.erro(LOG, "DOWNLOAD_DZ_DSOS: espera entre tentativas de envio (FIXED_CONST) interrompida", e);
											e.printStackTrace();
										}
									}
								} else {
									if (batelada.getCalibrationType() == CalibrationTypeEnum.ESTIMATED_CONST || batelada.getCalibrationType() == CalibrationTypeEnum.FULL_PROD) {
										double tempMediaZero = meterController.getCalibService().meanBenchTemperatureZeroFlow();
										LOG.debug("Tentativa {}/201 de envio ({}). Temperatura media de vazao zero: {}", i + 1, batelada.getCalibrationType(), tempMediaZero);
										if (meterController.sendInitCalibrationData(tempMediaZero, meterController.getMeter())) {
											ProcessLoggerUtil.writeInfo("SUCCESS: Zero Flow data sent to position: " + meterController.getMeter().getConversor().getIp());
											LOG.info("SUCESSO: dados de vazao zero enviados ao medidor (IP {}) na tentativa {}, em {} ms",
													meterController.getMeter().getConversor().getIp(), i + 1, ProcessLog.duracaoMs(inicioEnvioZero));
											downloadOk = true;
											meterController.setDownloadedZeroFlow(true);
											break;
										} else {
											try {
												Thread.currentThread().sleep(30);
											} catch (InterruptedException e) {
												ProcessLog.erro(LOG, "DOWNLOAD_DZ_DSOS: espera entre tentativas de envio interrompida", e);
												e.printStackTrace();
											}
										}
									}
								}

							}
							if (!downloadOk) {
								ProcessLoggerUtil.writeInfo("FAILED: Could not send Zero Flow data to meter in position: " + meterController.getMeter().getConversor().getIp());
								LOG.error("FALHA: dados de vazao zero NAO enviados ao medidor (IP {}) apos 201 tentativas, em {} ms - medidor REPROVADO ({})",
										meterController.getMeter().getConversor().getIp(), ProcessLog.duracaoMs(inicioEnvioZero),
										CalibrationErrorEnum.getStringError(CalibrationErrorEnum.DWZERO));
								if (!meterController.isReproved()) {
									countReprovedDownloadZeroFlow++;
									meterController.setIsReproved(true);
									meterController.setDownloadedZeroFlow(false);
									diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.DWZERO));
								}
							}
						} else {
							LOG.warn("Medidor ja reprovado - envio de DZ/DSOS ignorado. IP: {}", meterController.getMeter().getConversor().getIp());
						}
					}
					ProcessLog.limparMedidor();
					LOG.info("Envio de DZ/DSOS concluido. Medidores reprovados nesta etapa: {}", countReprovedDownloadZeroFlow);

					this.batelada.setReprovedMetersOnDownZeroFlow(countReprovedDownloadZeroFlow);

					if (processConfig.isSaveMatData()) {
						runningState = StandardProcessStatesEnum.SAVE_ZEROFLOW_MAT_DATA;
					} else {
						runningState = StandardProcessStatesEnum.FLOWRATE;
					}
				break;

				case SAVE_ZEROFLOW_MAT_DATA:
					if (matFile.createDir(this.batch.getBatchId(), batchController.getBatchRunCount(batch.getBatchId()))) {
						matFile.generateBenchZeroFlowDataFileName();
						matFile.saveBenchZeroData(benchDataController.getBenchDataZeroFlow());
						String meterAuxFileName = "";
						for (MeterController meterController : meterControllers) {
							// if (meterController.isZeroFlowApproved() || processConfig.isSaveMatDataReprovedMeters()) {
							if (!meterController.isReproved() || processConfig.isSaveMatDataReprovedMeters()) {
								if (meterController.getMatFileName() == null) {
									meterAuxFileName = matFile.generateMeterFileName(meterController.getMeter().getConversor().getIp(), new Date());
									meterController.setMatFileName(meterAuxFileName);
								}
								ProcessLoggerUtil.writeInfo("SUCCESS: Saving zero flow mat data file: " + matFile.getMeterDir() + meterController.getMatFileName());
								matFile.saveMeterDataZeroFlow(meterController.getMeterDataZeroFlow(), meterController.getMatFileName());
								matFile.saveMeterConfig(meterController.getMeter(), meterController.getMatFileName());
							}
							meterController.clearMeterZeroFlowData();
						}

						if (batelada.getCalibrationType() == CalibrationTypeEnum.FIXED_CONST) {
							//runningState = StandardProcessStatesEnum.SAVE_AFTER_ZERO_FLOW; // if(10 min)
							runningState = StandardProcessStatesEnum.VERIF;
						} else {
							if (batelada.getCalibrationType() == CalibrationTypeEnum.ESTIMATED_CONST || batelada.getCalibrationType() == CalibrationTypeEnum.FULL_PROD) {
								runningState = StandardProcessStatesEnum.FLOWRATE;
							}
						}
					}
				break;

				case SAVE_AFTER_ZERO_FLOW:
					int reprovedVerif10Min = 0;
					int approvedTotal10Min = 0;

					for (MeterController meterController : meterControllers) {
						ArrayList<VerificationErrorModel> errorConstants = new ArrayList();
						if (!meterController.isReproved()) {
							meterController.setCalibConstants(meterController.getFixedCalibConst2_5m());
							// meterController.setCalculatedConstants(true);

							// meterController.setConstantsCalculated(true);
							// meterController.setConstantsDownloaded(true);
							errorConstants.add(new VerificationErrorModel(1.0, 25.0, 25.0, 25.0));
							errorConstants.add(new VerificationErrorModel(0.5, 37.5, 37.5, 37.5));
							errorConstants.add(new VerificationErrorModel(0.5, 2500.0, 2500.0, 2500.0));
							meterController.setCalcErrors(errorConstants);
							// meterController.getMeter().getErrors().addAll(errorConstants);
							approvedTotal10Min++;

						} else {
							reprovedVerif10Min++;
						}
					}

					this.batelada.setReprovedMetersVerif(reprovedVerif10Min);
					this.batelada.setApprovedMeters(approvedTotal10Min);
					// runningState = StandardProcessStatesEnum.ATT_SERIAL_NUMBER;
					runningState = StandardProcessStatesEnum.SAVE_METER_DATA_IN_DB;

				break;

				case FLOWRATE:
					switch (processFlowRateState) {

						case INIT:
							// for (MeterController meterController : meterControllers) {
							// if (!meterController.isReproved()) {
							// enableMeterTransmit(meterController);
							// }
							// }
							processFlowRateState = ProcessFlowRateEnum.OPEN_VALVE;
						break;

						case OPEN_VALVE:
							ProcessLoggerUtil.writeInfo("Opening Line valves for Calibration ");
							if (benchController.openLineValves()) {
								processFlowRateState = ProcessFlowRateEnum.RUN;
								ProcessLoggerUtil.writeInfo("Line valves are OPENED for Calibration ");
							} else {
								if (benchController.openLineValves()) {
									processFlowRateState = ProcessFlowRateEnum.RUN;
									ProcessLoggerUtil.writeInfo("Line valves are OPENED for Calibration ");
								}
								else{
								ProcessLoggerUtil.writeInfo("Line valves are could not be opened going to wait");
								runningState = StandardProcessStatesEnum.WAIT;
								}
							}
						break;

						case WAIT:
							while (processFlowRateState == ProcessFlowRateEnum.WAIT);
						break;

						case RUN:// FLOWRATE
							benchController.setEnableBuffer(processConfig.getWindowSize(), processConfig.getFlowBuffSize(), true);
							boolean repeatFlowForUncertanty = false;
							


							for (FlowRateModel flowRate : calibFlowRates) {
								do {
									clearGraphsView();
									clearMeterGraphsView();

									BenchBean.getInstance().setRunningFlowRate(flowRate.getFlowRate());
									BenchBean.getInstance().setRunningLowerFlowRate(flowRate.getLowerLimit());
									BenchBean.getInstance().setRunningUpperFlowRate(flowRate.getUpperLimit());

									setUncertantyLimit(flowRate);

									// if (!benchController.checkWaterReservoirState()) {
									// benchController.refillReservoirs();
									// }

									if (benchController.runFlowRate(flowRate)) {
										BenchBean.getInstance().getIsAdjustingFlow().alternateRedAndOff();
										// wait for stabilization
										while (!benchController.assertStableFlow(processConfig.getFlowStabilityCheckTime(), flowRate));
										try {
											Thread.currentThread().sleep(processConfig.getPreFlowStabilizationTime());
										} catch (InterruptedException e) {
											ProcessLog.erro(LOG, "ProcessController.run()", e);
											e.printStackTrace();
										}
										BenchBean.getInstance().getIsAdjustingFlow().stopAlternateLed();

										if (!benchDataController.isRunning()) {
											benchDataController.resumeThread();
										}
										ProcessLoggerUtil.writeInfo("Starting to acquire Data.");


										clearGraphsViewFlowRate();
										BenchBean.getInstance().getIsSampling().turnLedGreen();

										benchDataController.setReachedNumSamp(false);
										benchDataController.setDataCounter(0);

										if (benchController.startBenchPulseCounter(flowRate.getRefMeter())) {
											
										
											
											try {
												Thread.currentThread().sleep(500);
											} catch (InterruptedException e) {
												ProcessLog.erro(LOG, "ProcessController.run()", e);
												e.printStackTrace();
											}
											benchDataController.setStoreForCalibration(true);
											saveMetersData();

											Interval interval;
											DateTime timeInicial = new DateTime();
											interval = new Interval(timeInicial, new DateTime());

											while ((!benchDataController.isReachedNumSamp() || !this.checkAllMetersSampleCount())
													&& (interval.toDurationMillis() < processConfig.getTimeOutFlowRate())) {
												DateTime novaData = new DateTime();
												if (novaData.isAfter(timeInicial)) {
													interval = new Interval(timeInicial, new DateTime());
												}
											}
											stopSaveMetersData();
											
										
											
											benchDataController.setStoreForCalibration(false);// this is to assure a zero is not found on the last data
											if (benchController.stopBenchPulseCounter(flowRate.getRefMeter())) {
												BenchBean.getInstance().getIsSampling().turnLedOff();
												clearAllMetersSampleCount();

												// checks it the established current reference flow rate uncertanty is
												// under the uncertanty limit f
												// case a forced cancel repeat is selected on view checkbox
												if (forceCancelRepeatObservable.getValue()) {
													if (benchDataController.getLastCalcStdFLow() > flowRate.getUncertantyLimit()) {
														try {
															Thread.currentThread().sleep(2000);
														} catch (InterruptedException e) {
															ProcessLog.erro(LOG, "ProcessController.run()", e);
															e.printStackTrace();
														}
														benchDataController.removeDataForFlowRate(flowRate.getFlowRate());
														removeFlowRateFromMeters(flowRate.getFlowRate());
														repeatFlowForUncertanty = true;
													} else {
														repeatFlowForUncertanty = false;
													}
												} else {
													repeatFlowForUncertanty = false;
												}
											}
										}
										

										
										processFlowRateState = ProcessFlowRateEnum.STOP;
									} else {
										runningState = StandardProcessStatesEnum.FLOWRATE;
										if (!benchController.areUpAndDownStreamValvesOpened()) {
											processFlowRateState = ProcessFlowRateEnum.OPEN_VALVE;
										}
										break;
									}
								} while (repeatFlowForUncertanty);
							}
							collectMetersFlowData();
							
							clearMetersData();
						break;

						case STOP:
							ProcessLoggerUtil.writeInfo("Stopping FLOWRATES.");
							// for (MeterController meterController : meterControllers) {
							// if (!meterController.isReproved()) {
							// disableMeterTransmit(meterController);
							// }
							// }

							if (benchController.stopFlowRate()) {
								runningState = StandardProcessStatesEnum.CALCULATE;
								processFlowRateState = ProcessFlowRateEnum.OPEN_VALVE;
							}
						break;

						default:
						break;
					}
				break; // FLOWRATE

				case CALCULATE:
					int calcConstReproved = 0;
					for (MeterController meterController : meterControllers) {
						// if (meterController.isZeroFlowApproved() || processConfig.isSaveMatDataReprovedMeters()) {
						if (!meterController.isReproved() || processConfig.isSaveMatDataReprovedMeters()) {
							if (meterController.getCalibService().getMeterController() == null) {
								meterController.getCalibService().setMeterController(meterController);
							}
							meterController.getCalibService().setBenchData(benchDataController.getBenchData());
							if (meterController.getCalibService().calculateContansts()) {
								meterController.setCalibConstants(meterController.getCalibService().getCalibConstants());

								/*** if not 40 min calculated fixed constants must be added here ****/
								if (batelada.getCalibrationType() == CalibrationTypeEnum.FULL_PROD) {
									meterController.addCalculatedFixedConsts(processConfig.getCalcFixedConsts());
									meterController.setCalculatedConstants(true);
								}
								/*********************************************************************/

								// meterController.setConstantsCalculated(true);
								ProcessLoggerUtil.writeInfo("SUCCESS: " + meterController.getCalibService().getCalibConstants().size() + " Constants calculated for meter at position "
										+ meterController.getMeter().getConversor().getIp());
							} else {
								ProcessLoggerUtil.writeInfo("FAILED: Could NOT calculate constant for meter at position " + meterController.getMeter().getConversor().getIp());
								// Update view
								if (!meterController.isReproved()) {
									calcConstReproved++;
									meterController.setCalculatedConstants(false);
									meterController.setIsReproved(true);
									diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.CALC));
								}
							}
						}
					}
					// this.batelada.setReprovedMetersCalib(calibReproved);
					this.batelada.setReprovedMetersOnCalcConst(calcConstReproved);
					if (batelada.getCalibrationType() == CalibrationTypeEnum.FIXED_CONST || batelada.getCalibrationType() == CalibrationTypeEnum.ESTIMATED_CONST) {
						runningState = StandardProcessStatesEnum.CALC_FIXED_CONSTS; /// if 10 min or 40 min
					} else {
						if (batelada.getCalibrationType() == CalibrationTypeEnum.FULL_PROD) {
							runningState = StandardProcessStatesEnum.DOWNLOAD_PARAMETERS; // Full prod
						}
					}
				break;

				case CALC_FIXED_CONSTS:
					int calcFixedConstReproved = 0;
					// 40 min
					for (MeterController meterController : meterControllers) {
						if (!meterController.isReproved()) {
							ProcessLoggerUtil.writeInfo("MESSAGE: calculating estimated fixed const for " + meterController.getMeter().getConversor().getIp());

							// if (meterController.calculateEstimatedDistanceConstIdm3m_NUMERO2()) {
							if (meterController.calculateEstimatedDistanceConstIdm2_5m()){

								// if (meterController.calculateEstimatedDistanceConst()) {
								meterController.setCalculatedConstants(true);
								meterController.addCalculatedFixedConsts(processConfig.getCalcFixedConsts());
								ProcessLoggerUtil.writeInfo("SUCCESS: Constants ESTIMATED calculated for meter at position " + meterController.getMeter().getConversor().getIp());
							} else {

								ProcessLoggerUtil.writeInfo("FAILED: COULD NOT ESTIMATE Constants for meter at position " + meterController.getMeter().getConversor().getIp());
								if (!meterController.isReproved()) {
									calcFixedConstReproved++;
									meterController.setCalculatedConstants(false);
									meterController.setIsReproved(true);
									diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.CALC));
								}
							}
						}
					}
					this.batelada.setReprovedMetersOnCalcConst(calcFixedConstReproved);
					runningState = StandardProcessStatesEnum.DOWNLOAD_PARAMETERS;
				break;

				case DOWNLOAD_PARAMETERS:
					int countReprovedDownloadConstants = 0;
					for (MeterController meterController : meterControllers) {
						boolean downloadOk = false;
						if (!meterController.isReproved()) {
							for (int i = 0; i < 100; i++) { // processConfig.getMeterDownloadConstantsTries(); i++) {
								// if (meterController.sendInitFixedCalibrationData(meterController.getCalibService().meanBenchTemperatureZeroFlow(), meterController.getMeter())) {
								if (meterController.sendCalibrationData()) {
									ProcessLoggerUtil.writeInfo("SUCCESS: data has been downloaded to meter at position " + meterController.getMeter().getConversor().getIp() + " at try n" + i);
									meterController.setDownloadedConstants(true);
									downloadOk = true;
									break;
								} else {
									try {
										Thread.currentThread().sleep(50);
									} catch (InterruptedException e) {
										ProcessLog.erro(LOG, "ProcessController.run()", e);
										e.printStackTrace();
									}
								}
							}
							if (!downloadOk) {
								ProcessLoggerUtil.writeInfo("FAILED: Data has NOT been downloaded to meter at position " + meterController.getMeter().getConversor().getIp());
								if (!meterController.isReproved()) {
									countReprovedDownloadConstants++;
									meterController.setIsReproved(true);
									meterController.setDownloadedConstants(false);
									diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.DOWN));
								}
							}
						}
					}

					this.batelada.setReprovedMetersOnDownConst(countReprovedDownloadConstants);

					if (processConfig.isSaveCalibLutMatData()) {
						runningState = StandardProcessStatesEnum.SAVE_K_Re_MAT_DATA;
					} else {
						runningState = StandardProcessStatesEnum.VERIF;
					}
				break;

				case SAVE_K_Re_MAT_DATA:
					if (matFile.createDir(this.batch.getBatchId(), batchController.getBatchRunCount(batch.getBatchId()))) {
						String meterAuxFileName = "";
						for (MeterController meterController : meterControllers) {
							if (meterController.isCalculatedConstants() || processConfig.isSaveMatDataReprovedMeters()) {
								matFile.generateMeterDir(meterController.getMeter().getConversor().getIp());
								if (meterController.getMatFileName() == null) {
									meterAuxFileName = matFile.generateMeterFileName(meterController.getMeter().getConversor().getIp(), new Date());
									meterController.setMatFileName(meterAuxFileName);
								}
								ProcessLoggerUtil.writeInfo("SUCCESS: Saving CALIB_LUT mat data file: " + matFile.getMeterDir() + meterController.getMatFileName());
								matFile.saveMeterConstantsData(meterController.getCalibConstants(), meterController.getMatFileName());
							}
						}
					}
					if (processConfig.isSaveMatData()) {
						runningState = StandardProcessStatesEnum.SAVE_MAT_DATA;
					} else {
						runningState = StandardProcessStatesEnum.VERIF;
					}
				break;

				case SAVE_MAT_DATA:
					if (matFile.createDir(this.batch.getBatchId(), batchController.getBatchRunCount(batch.getBatchId()))) {
						matFile.generateBenchDataFileName();
						matFile.saveBenchData(benchDataController.getBenchData());
						String meterAuxFileName = "";
						for (MeterController meterController : meterControllers) {
							if (meterController.isCalculatedConstants() || processConfig.isSaveMatDataReprovedMeters()) {
								matFile.generateMeterDir(meterController.getMeter().getConversor().getIp());
								if (meterController.getMatFileName() == null) {
									meterAuxFileName = matFile.generateMeterFileName(meterController.getMeter().getConversor().getIp(), new Date());
									meterController.setMatFileName(meterAuxFileName);
								}
								ProcessLoggerUtil.writeInfo("SUCCESS: Saving mat data file: " + matFile.getMeterDir() + meterController.getMatFileName());
								matFile.saveMeterData(meterController.getMeterData(), meterController.getMatFileName());
								meterController.clearMeterData();
							}
						}
					}
					runningState = StandardProcessStatesEnum.VERIF;
				break;

				case VERIF:
					ProcessLoggerUtil.writeInfo("------------- Running Verification ----------------");
					ProcessLoggerUtil.writeInfo("-------------Verification Step: " + processVerifFlowRateState.toString() + "----------------");
					double volInicial = 0.0;
					double volFinal = 0.0;
					switch (processVerifFlowRateState) {

						case INIT:
							// for (MeterController meterController : meterControllers) {
							// if (!meterController.isReproved()) {
							// enableMeterTransmit(meterController);
							// }
							// }
							

							processVerifFlowRateState = ProcessFlowRateEnum.OPEN_VALVE;
						break;

						case OPEN_VALVE:
							try {
								Thread.currentThread().sleep(1000);
							} catch (InterruptedException e1) {
								ProcessLog.erro(LOG, "ProcessController.run()", e1);
								e1.printStackTrace();
							} // sleep for 1 s to start verification

							ProcessLoggerUtil.writeInfo("Opening Line valves for Verification ");
							if (benchController.openLineValves()) {
								processVerifFlowRateState = ProcessFlowRateEnum.RUN;
								ProcessLoggerUtil.writeInfo("Line valves are Opened for Verification ");
							} else {
								runningState = StandardProcessStatesEnum.VERIF;
								processVerifFlowRateState = ProcessFlowRateEnum.OPEN_VALVE;
								ProcessLoggerUtil.writeInfo("Could NOT open valves for Verification... RETRYING.. ");
							}
						break;

						case WAIT:
							while (processVerifFlowRateState == ProcessFlowRateEnum.WAIT);
						break;

						case RUN:// VERIF
							// benchDataController.clearDataVerif();
							benchController.setEnableBuffer(processConfig.getWindowSize(), processConfig.getFlowBuffSize(), true);
							for (MeterController meterController : meterControllers) {
								meterController.clearDataBuffer();
							}
							boolean repeatVerifFlowRate = false;
							for (FlowRateModel flowRate : verifFlowRates) {
								do {
									clearGraphsView();
									clearMeterGraphsView();

									BenchBean.getInstance().setRunningFlowRate(flowRate.getFlowRate());
									BenchBean.getInstance().setRunningLowerFlowRate(flowRate.getLowerLimit());
									BenchBean.getInstance().setRunningUpperFlowRate(flowRate.getUpperLimit());

									setUncertantyLimit(flowRate);

									// Sets the flowRate to run in the bench and retrieves the State to check if
									// flowRate is running smoothly
									// if (!benchController.checkWaterReservoirState()) {
									// benchController.refillReservoirs();
									// }

									if (benchController.runFlowRate(flowRate)) {
										BenchBean.getInstance().getIsAdjustingFlow().alternateRedAndOff();

										// Sets the lower limit of uncertainty of specific flow rate to the flow rate
										// graph

										while (!benchController.assertStableFlow(processConfig.getFlowStabilityCheckTime(), flowRate));
										try {
											Thread.currentThread().sleep(processConfig.getPreFlowStabilizationTime());
										} catch (InterruptedException e) {
											ProcessLog.erro(LOG, "ProcessController.run()", e);
											e.printStackTrace();
										}
										if (!benchDataController.isRunning()) {
											benchDataController.resumeThread();
										}
										BenchBean.getInstance().getIsAdjustingFlow().stopAlternateLed();

										ProcessLoggerUtil.writeInfo("Starting to acquire Data.");

										benchDataController.setReachedNumSamp(false);
										benchDataController.setDataCounter(0);

										clearAllMetersSampleCount();
										clearGraphsViewFlowRate();

										if (benchController.startBenchPulseCounter(flowRate.getRefMeter())) {
											
											//RefMeterModel verifRefMeter = flowRate.getRefMeter();

										
											
											try {
												Thread.currentThread().sleep(500);
												//benchDataController.getRefMeterController().readRefMeter(verifRefMeter);
												//volInicial = verifRefMeter.getVolume();
											} catch (InterruptedException e) {
												ProcessLog.erro(LOG, "ProcessController.run()", e);
												e.printStackTrace();
											}
											BenchBean.getInstance().getIsSampling().turnLedGreen();
											benchDataController.setStoreForVerification(true);
											saveMetersVerifData();

											Interval interval;
											DateTime timeInicial = new DateTime();
											
											interval = new Interval(timeInicial, new DateTime());
											while ((!benchDataController.isReachedNumSamp() || !this.checkAllMetersSampleCount())
													&& (interval.toDurationMillis() < processConfig.getTimeOutFlowRate())) {
												interval = new Interval(timeInicial, new DateTime());
											}											
											// putControllerToSleep(flowTime);
											stopSaveMetersVerifData();
											
											//benchDataController.getRefMeterController().readRefMeter(verifRefMeter);
											//volFinal = verifRefMeter.getVolume();
											//ProcessLoggerUtil.writeInfo("Volume in Verif from RefMeter: Volume Final = " + (volFinal - volInicial));												
																						
											benchDataController.setStoreForVerification(false);
											if (benchController.stopBenchPulseCounter(flowRate.getRefMeter())) {

												BenchBean.getInstance().getIsSampling().turnLedOff();
												clearAllMetersSampleCount();

												if (forceCancelRepeatObservable.getValue()) {
													if (benchDataController.getLastCalcStdFLow() > flowRate.getUncertantyLimit()) {
														repeatVerifFlowRate = true;
														try {
															Thread.currentThread().sleep(3000);
														} catch (InterruptedException e) {
															ProcessLog.erro(LOG, "ProcessController.run()", e);
															e.printStackTrace();
														}
														benchDataController.removeDataForVerifFlowRate(flowRate.getFlowRate());
														removeFlowRateFromMeters(flowRate.getFlowRate());

													} else {
														repeatVerifFlowRate = false;
													}
												} else {
													repeatVerifFlowRate = false;
												}
												
											}
										}
										


										processVerifFlowRateState = ProcessFlowRateEnum.STOP;
									} else {
										
										
										
										runningState = StandardProcessStatesEnum.VERIF;
										
										
										if (!benchController.areUpAndDownStreamValvesOpened()) {
											processVerifFlowRateState = ProcessFlowRateEnum.OPEN_VALVE;
										}
										break;
									}
								} while (repeatVerifFlowRate);
								
								/*
								collectMetersVerifFlowData();
								
								List<MeterController> meterControllersVerif = getMeters();
								
							    if (meterControllersVerif != null) {
							        int i = 0; // Initialize index for logging purposes
							        for (MeterController meterController : meterControllersVerif) {
							            if (!meterController.isReproved()) { // Check if the meterController is not reproved
							                ArrayList<MeterDataModel> meterDataList = meterController.getMeterDataVerif();
							                if (meterDataList != null && !meterDataList.isEmpty()) { // Check if the list is not null and not empty
							                    double maxVolume = Double.MIN_VALUE;
							                    double minVolume = Double.MAX_VALUE;
							                    boolean foundMatchingFlowRate = false;

							                    for (MeterDataModel meterData : meterDataList) { // Iterate over MeterDataModel objects
							                        try {
							                            double currentFlowRate = meterData.getExpectedFlowRate();
							                            if (currentFlowRate == flowRate.getFlowRate()) { // Check if the flow rate matches
							                                foundMatchingFlowRate = true;
							                                double currentVolume = meterData.getAccReVolume();
							                                if (currentVolume > maxVolume) {
							                                    maxVolume = currentVolume; // Update max volume
							                                }
							                                if (currentVolume < minVolume) {
							                                    minVolume = currentVolume; // Update min volume
							                                }
							                            }
							                        } catch (NullPointerException e) {
							                            // Skip this meterData if there's a NullPointerException
							                            continue;
							                        }
							                    }

							                    if (foundMatchingFlowRate) { // If a matching flow rate was found, log the information
							                        try {
							                            ProcessLoggerUtil.writeInfo("Meter " + meterController.getMeter().getConversor().getIp() + " in " + flowRate.getFlowRate() + ":Saldo: " + 1000*(maxVolume - minVolume) + " Erro: "+ ((1000*(maxVolume - minVolume) - (volFinal - volInicial))/(volFinal - volInicial)));
							                        } catch (Exception e) {
							                            ProcessLoggerUtil.writeInfo("Meter " + meterController.getMeter().getConversor().getIp() + " in " + flowRate.getFlowRate() + " Saldo: " + 1000*(maxVolume - minVolume) + " Erro: "+ ((1000*(maxVolume - minVolume) - (volFinal - volInicial))/(volFinal - volInicial)));
							                            // Handle exceptions from logging, continue processing
							                        }
							                    }
							                }
							            }
							            i++; // Increment index after processing each meterController
							        }
							    }
							    */
							
								
							}
							collectMetersVerifFlowData();
							clearMetersData();// Never forget this!!
						break;

						case STOP:
							ProcessLoggerUtil.writeInfo("Stopping FLOWRATES verification.");
							// for (MeterController meterController : meterControllers) {
							// if (!meterController.isReproved()) {
							// meterController.disableTransmitData();
							// // for (int i = 0; i < 20; i++) {
							// // if (meterController.disableTransmitData()) {
							// // ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " disabled transmition");
							// // meterController.setEnableRead(false);
							// // } else {
							// // ProcessLoggerUtil.writeInfo("Could NOT switch Meter in position: " + meterController.getMeter().getConversor().getIp() + " to disable transmition");
							// // }
							// // }
							// }
							// }
							if (benchController.stopFlowRate()) {
								runningState = StandardProcessStatesEnum.CALCULATE_ERRORS;
								processVerifFlowRateState = ProcessFlowRateEnum.OPEN_VALVE;
							}
						break;
						default:
							runningState = StandardProcessStatesEnum.WAIT;
						break;
					}
				break; // VERIF

				case CALCULATE_ERRORS:
					int countReprovedCalculateErrors = 0;
					for (MeterController meterController : meterControllers) {
						// if (meterController.isDownloadedConstants()) {
						if ((!meterController.isReproved()) || (onlyVerificationObservable.getValue())) {
							// This is for the case when a verification must run without a calibration
							if (meterController.getCalibService().getMeterController() == null) {
								meterController.getCalibService().setMeterController(meterController);
							}
							// ------

							meterController.getCalibService().setBenchVerifData(benchDataController.getBenchDataVerif());
							ProcessLoggerUtil.writeInfo("Calculating error for Meter in position: " + meterController.getMeter().getConversor().getIp());
							if (meterController.getCalibService().calculateVerifError()) {
								if (!meterController.isReproved()) {
								    Set<VerificationErrorModel> errorSet = meterController.getMeter().getErrors();
								    if (!errorSet.isEmpty()) {
								        // Convert the Set to a List and sort it by expectedFlowRate instead of error
								        List<VerificationErrorModel> errorList = new ArrayList<>(errorSet);
								        errorList.sort(Comparator.comparingDouble(VerificationErrorModel::getExpectedFlowRate)); // list by expected

								        if (Math.abs(errorList.get(0).getError()) <= 4) {  
								        	if (Math.abs(errorList.get(2).getError()) <= 3) {// Using getError() to access the error value
									            ProcessLoggerUtil.writeInfo("debug: error has been calculated for Meter in position: " 
									                + meterController.getMeter().getConversor().getIp() + " Error: " + errorList.get(0).getError());
	
									            boolean allWithinRange = errorList.stream().skip(1)
									                .allMatch(e -> Math.abs(e.getError()) <= 3);
									            if (allWithinRange) {
									                meterController.getMeter().setApprovedVerification(true);
									            }
								        	}
								        } //TODO AJEITAR GAMBIARRA
								    }
								}

								ProcessLoggerUtil.writeInfo("SUCCESS: error has been calculated for Meter in position: " + meterController.getMeter().getConversor().getIp());
								meterController.setCalculatedErrors(true);
							} else {
								ProcessLoggerUtil.writeInfo("FAILED: Error calculating error for Meter in position: " + meterController.getMeter().getConversor().getIp());
								if (!meterController.isReproved()) {
									countReprovedCalculateErrors++;
									meterController.setIsReproved(true);
									meterController.setCalculatedErrors(false);
									diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.CALC_VERIF));
								}
							}
						}
					}

					this.batelada.setReprovedMetersOnCalcErrors(countReprovedCalculateErrors);

					runningState = StandardProcessStatesEnum.SAVE_METER_DATA_IN_DB;
					//runningState = StandardProcessStatesEnum.CHECK_VERIF;
				break;

				case CHECK_VERIF:
					// only runs in case "only verification" is running
					int reprovedVerif = 0;
					int approvedTotal = 0;
					for (MeterController meterController : meterControllers) {
						if (!meterController.isReproved()) {// if meter has not been reproved before
							ProcessLog.setMedidor(meterController.getMeter().getConversor().getName());
							if (meterController.checkMeterApproval()) {
								ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " is APPROVED.");
								LOG.info("RESULTADO FINAL: medidor (IP {}) APROVADO na verificacao", meterController.getMeter().getConversor().getIp());
								meterController.setVerifAproved(true);
								approvedTotal++;
							} else {
								ProcessLoggerUtil.writeInfo("\nMeter in position: " + meterController.getMeter().getConversor().getIp() + " is NOT APPROVED.\n");
								LOG.warn("RESULTADO FINAL: medidor (IP {}) REPROVADO na verificacao ({})",
										meterController.getMeter().getConversor().getIp(),
										CalibrationErrorEnum.getStringError(CalibrationErrorEnum.VERIF));
								reprovedVerif++;
								// Update view
								meterController.setVerifAproved(false);
								meterController.setIsReproved(true);
								diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.VERIF));
							}
						}
						// Print errors
						int i = 1;
						if (meterController.getCalcErrors() != null) {
							if (!meterController.getCalcErrors().isEmpty()) {
								System.err.println("Errors for meter in position " + meterController.getMeter().getConversor().getIp());
								for (VerificationErrorModel verifError : meterController.getCalcErrors()) {
									System.err.println("Calculated Error " + i++ + " at flowrate: " + verifError.getExpectedFlowRate() + " error:" + verifError.getError());
									LOG.info("Erro calculado na vazao {}: {} %", verifError.getExpectedFlowRate(), verifError.getError());
								}
							}
						}
					}
					ProcessLog.limparMedidor();

					this.batelada.setReprovedMetersVerif(reprovedVerif);
					this.batelada.setApprovedMeters(approvedTotal);
					LOG.info("RESUMO DA VERIFICACAO: {} medidores APROVADOS | {} REPROVADOS de {} conectados",
							approvedTotal, reprovedVerif, meterControllers.size());

					// runningState = StandardProcessStatesEnum.DISPLAY_APPROVED_METERS;
					runningState = StandardProcessStatesEnum.SAVE_METER_DATA_IN_DB;
				break;

				// case ATT_SERIAL_NUMBER:
				case SAVE_METER_DATA_IN_DB:

					boolean saveOk = false;
					Calendar dateOfCalibration = Calendar.getInstance();
					LOG.info("ETAPA INICIO: gravacao dos resultados no banco de dados para {} medidores. Data de calibracao: {}",
							meterControllers.size(), dateOfCalibration.getTime());
					long inicioGravacaoDb = ProcessLog.iniciarCronometro();
					for (MeterController meterController : meterControllers) {
						ProcessLog.setMedidor(meterController.getMeter().getConversor().getName());

						// Save meter into db
						meterController.getMeter().setDateOfCalibration(dateOfCalibration.getTime());
						if (meterController.saveMeterInDb()) {
							ProcessLoggerUtil.writeInfo("SUCCESS: Save meter in database " + meterController.getMeter().getConversor().getIp());
							if (meterController.saveVerificationErrorsInDb()) {
								ProcessLoggerUtil.writeInfo("SUCCESS: Save Verification Errors in database " + meterController.getMeter().getConversor().getIp());

								if (!onlyVerificationObservable.getValue()) {
									if (meterController.saveCalibConstantsInDb()) {
										ProcessLoggerUtil.writeInfo("SUCCESS: Save Calibration Constant in database " + meterController.getMeter().getConversor().getIp());
										LOG.info("SUCESSO: constantes de calibracao gravadas no banco. IP: {}", meterController.getMeter().getConversor().getIp());
										saveOk = true;
									} else {
										ProcessLoggerUtil.writeInfo("FAILED: Could not save Calibration Constant in database " + meterController.getMeter().getConversor().getIp());
										ProcessLog.critico(LOG, "FALHA na gravacao das constantes de calibracao no banco para o medidor (IP "
												+ meterController.getMeter().getConversor().getIp() + ")");
									}
								} else {
									saveOk = true;
								}
							} else {
								ProcessLoggerUtil.writeInfo("FAILED: Could not save Verification Errors in database " + meterController.getMeter().getConversor().getIp());
								ProcessLog.critico(LOG, "FALHA na gravacao dos erros de verificacao no banco para o medidor (IP "
										+ meterController.getMeter().getConversor().getIp() + ")");
							}
						} else {
							ProcessLoggerUtil.writeInfo("FAILED: Could not save Meter in database: " + meterController.getMeter().getConversor().getIp());
							ProcessLog.critico(LOG, "FALHA na gravacao do medidor no banco (IP "
									+ meterController.getMeter().getConversor().getIp() + ")");
						}

						// //
						// if (!saveOk) {
						// meterController.setIsReproved(true);
						// diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.SAVE_ERR));
						// }

						//
						if ((!meterController.isReproved()) && (!saveOk)) {
							LOG.error("Medidor (IP {}) REPROVADO por erro de gravacao ({})",
									meterController.getMeter().getConversor().getIp(),
									CalibrationErrorEnum.getStringError(CalibrationErrorEnum.SAVE_ERR));
							meterController.setIsReproved(true);
							diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.SAVE_ERR));
						}
					}
					ProcessLog.limparMedidor();
					LOG.info("ETAPA FIM: gravacao dos resultados no banco concluida em {} ms", ProcessLog.duracaoMs(inicioGravacaoDb));

					// if (onlyVerificationObservable.getValue()) {
					// runningState = StandardProcessStatesEnum.DISPLAY_APPROVED_METERS;
					// } else {
					// runningState = StandardProcessStatesEnum.SAVE_CONST_MAT_DATA;
					// }
					// 10 min
					if (batelada.getCalibrationType() == CalibrationTypeEnum.FIXED_CONST) {
						runningState = StandardProcessStatesEnum.DISPLAY_APPROVED_METERS;
					} else {
						if (batelada.getCalibrationType() == CalibrationTypeEnum.ESTIMATED_CONST || batelada.getCalibrationType() == CalibrationTypeEnum.FULL_PROD) {
							runningState = StandardProcessStatesEnum.SAVE_METER_CONFIG_MAT_DATA;
							// runningState = StandardProcessStatesEnum.SAVE_CONST_MAT_DATA;
						}
					}

				break;

				case SEND_METERS_SERIAL_NUMBER:

					for (MeterController meterController : meterControllers) {
						if (!meterController.isReproved()) {
							ProcessLoggerUtil.writeInfo("Send Meter Serial Number for position: " + meterController.getMeter().getConversor().getIp());
							for (int i = 0; i < 20; i++) {
								if (meterController.sendMeterSerialNumber(meterController.getMeter().getSerialNumber())) {
									ProcessLoggerUtil.writeInfo("SUCCESS: Meter Serial Number seted " + meterController.getMeter().getConversor().getIp());
									meterController.setMeterSerialNumberConfigured(true);
									break;
								} else {
									if (i == processConfig.getMeterDateUpdateTries() - 1) {
										// display message only in the last try
										ProcessLoggerUtil.writeInfo("FAILED: Could NOT set meter serial number " + meterController.getMeter().getConversor().getIp());
										meterController.setMeterSerialNumberConfigured(false);
									}
								}
							}
						}
					}
					runningState = StandardProcessStatesEnum.UPDATE_METERS_SYSTEM_DATE;
				break;

				case UPDATE_METERS_SYSTEM_DATE:
					Calendar currentDate = Calendar.getInstance();
					// Date currentDate = calendar.getTime();

					for (MeterController meterController : meterControllers) {
						if (!meterController.isReproved()) {
							ProcessLoggerUtil.writeInfo("Updating Meter SYSTEM Date for position: " + meterController.getMeter().getConversor().getIp());
							for (int i = 0; i < processConfig.getMeterDateUpdateTries(); i++) {
								if (meterController.sendMeterSystemDate(currentDate)) {
									ProcessLoggerUtil.writeInfo("SUCCESS: Meter SYSTEM Date SETED and saved " + meterController.getMeter().getConversor().getIp());
									meterController.setMeterSystemDateConfigured(true);
									break;
								} else {
									if (i == processConfig.getMeterDateUpdateTries() - 1) {
										// display message only in the last try
										ProcessLoggerUtil.writeInfo("FAILED: Could NOT set and save meter SYSTEM DATE " + meterController.getMeter().getConversor().getIp());
										meterController.setMeterSystemDateConfigured(false);
									}
								}
							}
						}
					}
					runningState = StandardProcessStatesEnum.UPDATE_METERS_REPLACE_DATE;
				break;

				case UPDATE_METERS_REPLACE_DATE:
					Calendar currentReplaceDate = Calendar.getInstance();
					// Date currentDate = calendar.getTime();

					for (MeterController meterController : meterControllers) {
						if (!meterController.isReproved()) {
							ProcessLoggerUtil.writeInfo("Updating Meter BATTERY REPLACE Date " + meterController.getMeter().getConversor().getIp());
							for (int i = 0; i < processConfig.getMeterDateUpdateTries(); i++) {
								if (meterController.sendMeterReplaceDate(currentReplaceDate, 10)) {
									// if (meterController.updateMeterInDb()) {
									ProcessLoggerUtil.writeInfo("SUCCESS: Meter REPLACE Date SETED and saved " + meterController.getMeter().getConversor().getIp());
									meterController.setMeterReplaceDateConfigured(true);
									break;
								} else {
									if (i == processConfig.getMeterDateUpdateTries() - 1) {
										// display message only in the last try
										ProcessLoggerUtil.writeInfo("FAILED: Could NOT set and save meter REPLACE DATE " + meterController.getMeter().getConversor().getIp());
										meterController.setMeterReplaceDateConfigured(false);
									}
								}
							}
						}
					}

					if (processConfig.isSaveMatData()) {
						runningState = StandardProcessStatesEnum.SAVE_METER_CONFIG_MAT_DATA;
					} else {
						runningState = StandardProcessStatesEnum.SAVE_VERIF_ERRORS_MAT_DATA;
					}
				break;

				case SAVE_METER_CONFIG_MAT_DATA:
					if (!meterControllers.isEmpty()) {
						for (MeterController meterController : meterControllers) {
							if (!meterController.isReproved()) {
								byte[] data = null;
								for (int i = 0; i < processConfig.getMeterDateUpdateTries(); i++) {
									data = readMeterConfigData(meterController);
									if (data != null) {
										String matFileLocalDir = PreferencesHandler.readMatFileLocalDir();
										String matFileRemoteDir = PreferencesHandler.readMatFileRemoteDir();
										// String netDir = "\\\\10.30.0.15/sistemas eletronicos de medicao/11 - Produ��o/MatFiles";
										try {
											ProcessLoggerUtil.writeInfo("Saving FULL CONFIG MATFILE DATA for meter in position: " + meterController.getMeter().getConversor().getIp());
											matFile.saveFullMeterConfig(matFileLocalDir, "" + meterController.getMeter().getMeterUfoId(), data);
											//matFile.saveFullMeterConfig(matFileRemoteDir, "" + meterController.getMeter().getMeterUfoId(), data);
											break;
										} catch (IOException e) {
											ProcessLog.erro(LOG, "ProcessController.run()", e);
											e.printStackTrace();
										}
									} else {
										ProcessLoggerUtil.writeInfo("FAILED: Dont READ FULL CONFIG from meter in position: " + meterController.getMeter().getConversor().getIp());
									}
								}
							} else {
								ProcessLoggerUtil.writeInfo("FAILED: Dont Save FULL CONFIG in matfile for meter in position: " + meterController.getMeter().getConversor().getIp());
							}
						}
					}
					runningState = StandardProcessStatesEnum.SAVE_CONST_MAT_DATA;
				break;

				case SAVE_CONST_MAT_DATA:
					if (!meterControllers.isEmpty()) {
						for (MeterController meterController : meterControllers) {
							if (!meterController.isReproved() || processConfig.isSaveMatDataReprovedMeters()) {
								if (meterController.getCalibConstants() != null) {
									if (!meterController.getCalibConstants().isEmpty()) {
										// if(meterController.isApproved()){
										ProcessLoggerUtil.writeInfo("Saving Constant MAT data for meter in position: " + meterController.getMeter().getConversor().getIp());
										matFile.saveMeterConstantsData(meterController.getCalibConstants(), meterController.getMatFileName());
									}
								}
							} else {
								ProcessLoggerUtil.writeInfo("FAILED: Dont Save CALIB CONSTANTS in Database data for meter in position: " + meterController.getMeter().getConversor().getIp());
							}
						}
					}
					runningState = StandardProcessStatesEnum.SAVE_VERIF_ERRORS_MAT_DATA;
				break;

				case SAVE_VERIF_ERRORS_MAT_DATA:
					if (matFile.createDir(this.batch.getBatchId(), batchController.getBatchRunCount(batch.getBatchId()))) {
						matFile.generateBenchVerifDataFileName();
						matFile.saveBenchVerifData(benchDataController.getBenchDataVerif());
						String meterAuxFileName = "";
						String meterDir = "";
						for (MeterController meterController : meterControllers) {
							if (!meterController.isReproved() || processConfig.isSaveMatDataReprovedMeters()) {
								meterDir = matFile.generateMeterDir(meterController.getMeter().getConversor().getIp());
								if (!matFile.dirExists(meterDir)) {
									matFile.createMeterDir(meterDir);
								}
								if (meterController.getMatFileName() == null) {
									meterAuxFileName = matFile.generateMeterFileName(meterController.getMeter().getConversor().getIp(), new Date());
									meterController.setMatFileName(meterAuxFileName);
								}
								ProcessLoggerUtil.writeInfo("Saving Verification MAT data and ERRORS for meter in position: " + meterController.getMeter().getConversor().getIp());
								matFile.saveMeterVerifErrors(meterController.fetchCalcErrors(), meterController.getMatFileName());
								matFile.saveMeterVerifData(meterController.getMeterDataVerif(), meterController.getMatFileName());
							} else {
								ProcessLoggerUtil.writeInfo("FAILED: Will NOT save Verification MAT data and ERRORS for meter in position: " + meterController.getMeter().getConversor().getIp());
							}
						}
					}

					runningState = StandardProcessStatesEnum.SWITCH_METERS_TO_NORMAL;
					/*if (false  processConfig.isChooseQaSamples()) {
						runningState = StandardProcessStatesEnum.SORT_TO_VERIF_QA;
					} else {
						runningState = StandardProcessStatesEnum.DISPLAY_APPROVED_METERS;
					}*/
				break;

				// This case generates the wM-Bus serial number and cripto key and sends it to the meter
				case SEND_RADIO_WMBUS_CONFIG:
					if (!meterControllers.isEmpty()) {
						String avaliableRfSn;
						String generatedCryptoKey;
						short transmitInterval = batchController.getWmBusTransmitInterval(batch);
						for (MeterController meterController : meterControllers) {
							if (!meterController.isReproved()) {
								avaliableRfSn = batchController.getNextAvailableRadioSN(batch.getBatchId());
								try {
									generatedCryptoKey = meterController.generateCryptoKey(avaliableRfSn);
									if (meterController.saveMeterWmBusData(avaliableRfSn, generatedCryptoKey, transmitInterval)) {
										if (batchController.allocateWmBusConfigToBatch(batch, avaliableRfSn)) {
											for (int i = 0; i < processConfig.getMeterRadioConfigTries(); i++) {
												if (meterController.sendWmBusConfig(avaliableRfSn, generatedCryptoKey, transmitInterval)) {
													ProcessLoggerUtil.writeInfo("SUCCESS: wM-Bus data sent to meter in position: " + meterController.getMeter().getConversor().getIp());
													meterController.setWmBusConfigured(true);
													break;
												} else {
													if (i == processConfig.getMeterRadioConfigTries() - 1) {
														// display message only in the last try
														ProcessLoggerUtil.writeInfo("FAILED: wM-Bus data NOT sent to meter in position: " + meterController.getMeter().getConversor().getIp());
														meterController.setWmBusConfigured(false);
													}
												}
											}
										}
									}
								} catch (CryptoMd5Digest e) {
									ProcessLoggerUtil.writeInfo("FAILED: Could not generate crypto key.  meter in position: " + meterController.getMeter().getConversor().getIp());
								}
							}
						}
					}
					runningState = StandardProcessStatesEnum.SWITCH_METERS_TO_NORMAL;
					/*if (false processConfig.isChooseQaSamples()) {
						runningState = StandardProcessStatesEnum.SORT_TO_VERIF_QA; //SORT_TO_VERIF_QA
					} else {
						runningState = StandardProcessStatesEnum.SWITCH_METERS_TO_NORMAL;
					}*/
				break;

				case SORT_TO_VERIF_QA:
					sortMetersToQa();
					runningState = StandardProcessStatesEnum.SWITCH_METERS_TO_VERIF_QA;
				break;

				case SWITCH_METERS_TO_NORMAL:
					/*
					for (MeterController meterController : meterControllers) {
						if (meterController.isQa()) {
							switchMeterToNormal(meterController);
						} else {
							ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " wasn't selected for quality assurance");
						}
					}
					balbino 2024 03: QA t� com defeito, corrigir s� depois */
					runningState = StandardProcessStatesEnum.SWITCH_METERS_TO_POST_CALIB;
				break;

				case SWITCH_METERS_TO_VERIF_QA:
					switchMetersToVerif();
					runningState = StandardProcessStatesEnum.DISPLAY_APPROVED_METERS;
				break;

				case SWITCH_METERS_TO_POST_CALIB:
					switchMetersToPostCalib();
					runningState = StandardProcessStatesEnum.DISPLAY_APPROVED_METERS;

				break;

				case DISPLAY_APPROVED_METERS:
					if (!meterControllers.isEmpty()) {
						//displayApprovedMeters();
					}
					runningState = StandardProcessStatesEnum.END_RUN_CONFIGURATIONS;
				break;

				case END_RUN_CONFIGURATIONS:

					// Update Batch
					BatchService batchService = new BatchService();
					// Update batelada
					BateladaService bateladaService = new BateladaService();
					this.batelada.setEndTime(new Date());					
					//LogChangeProcessBatchService logChangeProcessBatchService = new LogChangeProcessBatchService();
					//ogChangeProcessBatchModel logChangeProccessBatchModel = logChangeProcessBatchService.findByBatch(this.batch);
					//logChangeProccessBatchModel.setBateladaEnd(this.batelada); //da erro aqui e crasha TODO
					//if(this.batelada.getBatch().getId() != this.batch.getId()){
					//	logChangeProccessBatchModel.setBateladaInit(this.batelada);
					//}
					this.batelada.setBatch(batchService.findById(this.batch.getId()));
					try {
						bateladaService.update(this.batelada);
					//	logChangeProcessBatchService.update(logChangeProccessBatchModel);
					} catch (CrudDatabaseException e1) {
						ProcessLog.erro(LOG, "ProcessController.run()", e1);
						e1.printStackTrace();
					}
					runningState = StandardProcessStatesEnum.CLEAR_BUFFERS;
				break;

				case CLEAR_BUFFERS:
					for (MeterController meterController : meterControllers) {
						meterController.clearDataBuffer();
						benchDataController.clearDataFlow();
						benchDataController.clearDataVerif();
						benchDataController.clearDataZeroFlow();
					}
					runningState = StandardProcessStatesEnum.WAIT;

				break;

				case WAIT:
				break;

				default:
					ProcessLoggerUtil.writeInfo("************************* DEFAULT - State machine to run: " + runningState.toString() + " *************************");
					runningState = StandardProcessStatesEnum.WAIT;
				break;
			}
		}
	}

	/**
	 * Function returns the value of attribute running
	 * 
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * 
	*/
	public StandardProcessStatesEnum getRunningState() {
		return runningState;
	}

	/**
	 * Places the process controller in sleep for a time o milis seconds
	 * 
	 * @param milis
	 */
	static void putControllerToSleep(long milis) {
		try {
			ProcessLoggerUtil.writeInfo("Going to sleep in ProcessController Thread: " + Thread.currentThread().getName());
			Thread.currentThread().sleep(milis);
		} catch (InterruptedException e) {
			ProcessLog.erro(LOG, "ProcessController.getRunningState()", e);
			e.printStackTrace();
		}
		ProcessLoggerUtil.writeInfo("WAKING UP in ProcessController Thread: " + Thread.currentThread().getName());
	}

	/**
	 * Clears the meters data buffer
	 */
	public void clearMetersData() {
		for (MeterController meterController : meterControllers) {
			meterController.clearDataBuffer();
		}
	}

	/**
	 * Function returns the value of attribute verifiedConnectedMeters
	 * 
	 * @return the verifiedConnectedMeters
	 */
	public HashMap<ConversorNumbers, MeterController> getVerifiedConnectedMeters() {
		return verifiedConnectedMeters;
	}

	/**
	 * Function sets the value for attribute verifiedConnectedMeters
	 * 
	 * @param verifiedConnectedMeters
	 *            the verifiedConnectedMeters to set
	 */
	public void setVerifiedConnectedMeters(HashMap<ConversorNumbers, MeterController> verifiedConnectedMeters) {
		this.verifiedConnectedMeters = verifiedConnectedMeters;
	}

	/**
	 * Function returns the value of attribute batch
	 * 
	 * @return the batch
	 */
	public BatchModel getBatch() {
		return batch;
	}

	/**
	 * Function sets the value for attribute batch
	 * 
	 * @param batch
	 *            the batch to set
	 */
	public void setBatch(BatchModel batch) {
		this.batch = batch;
	}

	// public boolean areAllMetersNotTrasmiting() {
	// boolean retorno = false;
	// for (MeterController meterController : meterControllers) {
	// if (meterController.isDisableDataTransmitOpModeSuccesfull()) {
	// retorno = true;
	// } else {
	// retorno = false;
	// break;
	// }
	// }
	// return retorno;
	// }

	public boolean areAllMetersTrasmiting() {
		boolean retorno = false;
		for (MeterController meterController : meterControllers) {
			if (meterController.isTransmiting()) {
				retorno = true;
			} else {
				retorno = false;
				break;
			}
		}
		return retorno;

	}

	public boolean areAllAgcTrimingSuccesfull() {
		boolean retorno = false;
		for (MeterController meterController : meterControllers) {
			if (meterController.isAgcTrimmed()) {
				ProcessLoggerUtil.writeInfo("Triming is SUCCESSFULL for meter in postion:" + meterController.getMeter().getConversor().getIp());
				retorno = true;
			} else {
				ProcessLoggerUtil.writeInfo("Triming is UNSUCESSFULL for meter in postion:" + meterController.getMeter().getConversor().getIp());
				retorno = false;
			}
		}
		return retorno;
	}

	/**
	 * Function send a TRIM command to the meter so it can try to adjust the AGC. Must be done in Zeroflow
	 * 
	 * @return
	 */
	public boolean trimMetersAgcStage2(MeterController meterController) {
		// int retorno = 0;
		boolean acked = false;
		int attemptivesCount = 0;

		boolean isTrimSucces = false;
		// for (MeterController meterController : meterControllers) {
		if (!meterController.isAgcTrimmed()) {

			meterController.setEnableRead(false);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.err.println("ERROR: InterruptedException of assync thread at ProcessController.trimMetersAgcStage2()");
			}

			ProcessLoggerUtil.writeInfo("Starting to Trim AGC stage 2 for meter in position:" + meterController.getMeter().getConversor().getIp());

			CompletableFuture cpf = CompletableFuture.runAsync(() -> {
				try {
					Thread.sleep(501);
				} catch (InterruptedException e) {
					System.err.println("ERROR: InterruptedException of assync thread at ProcessController.trimMetersAgcStage2()");
				}
			});

			CompletableFuture timeout = CompletableFuture.runAsync(() -> {
				try {
					Thread.sleep(50000);
				} catch (InterruptedException e) {
					System.err.println("ERROR: InterruptedException of assync thread at ProcessController.trimMetersAgcStage2()");
				}
			});

			meterController.setAcked(false);
			boolean execute = true;

			while (!(acked = meterController.checkAckForCommand())) {
				if (!meterController.isThreadWorking()) {
					if (attemptivesCount >= processConfig.getTrimAgcMaxTries()) {// count 50 tries to trim agc
						ProcessLoggerUtil.writeInfo("MESSAGE: " + processConfig.getTrimAgcMaxTries() + " attemps to trim agc failed at ProcessController.trimMetersAgcStage2()");
						isTrimSucces = false;
						acked = false;
						break;
					}
					meterController.runTrimAgcStage2();
					attemptivesCount++;
				} else {
					if (cpf.isDone()) {
						meterController.runTrimAgcStage2();
						attemptivesCount++;
						cpf = CompletableFuture.runAsync(() -> {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								ProcessLoggerUtil.writeInfo("ERROR: InterruptedException of assync thread at ProcessController.trimMetersAgcStage2()");
							}
						});
					}
					if (timeout.isDone()) {
						meterController.setForceStopCheckAckOrNack(true);
						ProcessLoggerUtil.writeInfo("TIMEOUT FORCE exited Trim AGC stage 2 for meter in position:" + meterController.getMeter().getConversor().getIp());
						break;
					}
				}
			}

			if (acked) {// if the loop has not been interrupted
				// this will happen if in 5 tries the agc trim works
				isTrimSucces = true;
				ProcessLoggerUtil.writeInfo("Trim AGC stage 2 for meter in position:" + meterController.getMeter().getConversor().getIp() + " is SUCCESSFUL TRIM AGC at try n�" + attemptivesCount);
			} else {
				isTrimSucces = false;
				ProcessLoggerUtil.writeInfo("Trim AGC stage 2 for meter in position:" + meterController.getMeter().getConversor().getIp() + " is UNSUCCESSFUL TRIM AGC");
			}

			// meterController.setAgcTrimmed(isTrimSucces);
			// meterController.setIsReproved(!isTrimSucces);
			// // meterController.getMeter().setApprovedAgcTrim(isTrimSucces);
			// if (!isTrimSucces) {
			// diplayReprovedReason(meterController, CalibrationErrorEnum.getStringError(CalibrationErrorEnum.TRIM));
			// }

			// Controle da thread [**nao mexer**]
			meterController.setCheckForAckOrNack(false); // to set the
			meterController.setEnableRead(true);
			meterController.setForceStopCheckAckOrNack(true); // to set the
			attemptivesCount = 0;
			// isTrimSucces = false;
			acked = false;
			cpf.cancel(true);
		} else {
			ProcessLoggerUtil.writeInfo("Trim AGC stage 2 for meter in position:" + meterController.getMeter().getConversor().getIp() + " Has already been executed");
		}
		// }
		// return retorno;

		return isTrimSucces;
	}

	/**
	 * 
	 * @return
	 */
	public int sortMetersToQa() {
		int retorno = 0;

		int totalToSelect = 0;
		double toRound = 0;
		if (!meterControllers.isEmpty()) {
			if (batelada != null) {
				if (processConfig != null) {

					//
					if (batelada.getApprovedMeters() > 0) {
						toRound = (this.batelada.getApprovedMeters() * processConfig.getPercentageToVerif());
						totalToSelect = (int) Math.ceil(toRound);
					} else {
						return retorno;
					}

					//
					// System.out.println("TOTAL to SELECT " + totalToSelect);

					int totalSelected = 0;
					int positionToSelect = 0;
					int lastSelectedPos = -1;
					int counter = 0;

					MeterController meterControllerAux;
					Random rand = new Random();
					HashMap<Integer, MeterController> hash = new HashMap<Integer, MeterController>();

					for (MeterController meterController : meterControllers) {
						if (!meterController.isReproved()) {
							hash.put(counter, meterController);
							counter++;
						}
					}

					if (counter >= totalToSelect) {
						while (totalSelected < totalToSelect) {
							// while (totalSelected <= totalToSelect) {
							// System.out.println("Selected " + totalSelected);
							positionToSelect = rand.nextInt(this.batelada.getApprovedMeters());
							if (positionToSelect != lastSelectedPos) {
								lastSelectedPos = positionToSelect;

								if (hash.containsKey(positionToSelect)) {
									meterControllerAux = (MeterController) hash.get(positionToSelect);
									meterControllerAux.setQa(true);
									totalSelected++;
									hash.remove(positionToSelect);
								}
							}
						}
					}
				}
			}
		}
		return retorno;
	}

	/**
	 * 
	 */
	public byte[] readMeterConfigData(MeterController meterController) {
		// int retorno = 0;
		byte[] array = null;
		boolean acked = false;
		int attemptivesCount = 0;

		CompletableFuture cpf;
		cpf = CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.err.println("ERROR: InterruptedException of assync thread at ProcessController.readMeterConfigData()");
			}
		});

		ProcessLoggerUtil.writeInfo("Reading CONFIG DATA from meter in position: " + meterController.getMeter().getConversor().getIp());

		meterController.setAcked(false);
		meterController.setEnableRead(false);
		while (!(acked = meterController.checkAckForCommand())) {
			if (attemptivesCount >= 20) {
				ProcessLoggerUtil.writeInfo("MESSAGE: 20 attemps to read config meter data. ProcessController.readMeterConfigData()");
				meterController.setForceStopCheckAckOrNack(true);
				// isSwitchToVerif = false;
				acked = false;
				break;
			}
			if (!meterController.isThreadWorking()) {
				meterController.readMeterConfigData();
				attemptivesCount++;
			} else {
				if (cpf.isDone()) {
					meterController.readMeterConfigData();
					attemptivesCount++;
					cpf = CompletableFuture.runAsync(() -> {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							ProcessLoggerUtil.writeInfo("ERROR: InterruptedException of assync thread at ProcessController.readMeterConfigData()");
						}
					});
				}
			}
		}

		if (acked) {
			array = meterController.receiverData(654);
			// array = meterController.getMeterSocketComm().readData();
		} else {
			ProcessLoggerUtil.writeInfo("Could NOT read config data from Meter in position: " + meterController.getMeter().getConversor().getIp());
		}

		meterController.setEnableRead(true);
		meterController.setCheckForAckOrNack(false); // to set the
		meterController.setForceStopCheckAckOrNack(true); // to set the
		attemptivesCount = 0;
		acked = false;
		cpf.cancel(true);
		return array;
	}

	/**
	 * This Method selects and shifts the meter to verif mode for quality assurance and to post
	 * 
	 * @return
	 */
	public int switchMeterToNormal(MeterController meterController) {
		int retorno = 0;
		boolean acked = false;
		int attemptivesCount = 0;
		// for (MeterController meterController : meterControllers) {
		// if (meterController.isQa()) {
		// try {
		// Thread.currentThread().sleep(10);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }

		CompletableFuture cpf;
		cpf = CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.err.println("ERROR: InterruptedException of assync thread at ProcessController.switchMetersToVerifOrPost()");
			}
		});

		ProcessLoggerUtil.writeInfo("Switching meter in position: " + meterController.getMeter().getConversor().getIp() + " to Normal for quality assurance");

		meterController.setAcked(false);
		meterController.setEnableRead(false);
		while (!(acked = meterController.checkAckForCommand())) {
			if (attemptivesCount >= 20) {
				ProcessLoggerUtil
						.writeInfo("MESSAGE: 20 attemps to Switch meter in position " + meterController.getMeter().getConversor().getIp() + " to NORMAL at ProcessController.switchMetersToNormal()");
				meterController.setForceStopCheckAckOrNack(true);
				// isSwitchToVerif = false;
				acked = false;
				break;
			}
			if (!meterController.isThreadWorking()) {
				meterController.switchStateToNormalMode();
				attemptivesCount++;
			} else {
				if (cpf.isDone()) {
					meterController.switchStateToNormalMode();
					attemptivesCount++;
					cpf = CompletableFuture.runAsync(() -> {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							ProcessLoggerUtil.writeInfo("ERROR: InterruptedException of assync thread at ProcessController.switchMetersToNormal()");
						}
					});
				}
			}
		}

		if (acked) {// if the loop has not been interrupted
			// this will happen if in 5 tries the agc trim works
			ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " in Normal for quality assurance");
			retorno++;
			// meterController.setVerifMode(true);
		} else {
			ProcessLoggerUtil.writeInfo("Could NOT switch Meter in position: " + meterController.getMeter().getConversor().getIp() + " to Normal for quality assurance");
		}

		meterController.setCheckForAckOrNack(false); // to set the
		meterController.setForceStopCheckAckOrNack(true); // to set the
		attemptivesCount = 0;
		acked = false;
		cpf.cancel(true);
		// } else {
		// ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " no selected for quality assurance");
		// }
		// }
		return retorno;
	}

	/**
	 * This Method selects and shifts the meter to verif mode for quality assurance and to post
	 * 
	 * @return
	 */
	public int switchMetersToVerif() {
		int retorno = 0;
		boolean acked = false;
		int attemptivesCount = 0;
		CompletableFuture cpf;
		for (MeterController meterController : meterControllers) {
			if (true /* meterController.isQa() */) {
				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException e1) {
					ProcessLog.erro(LOG, "ProcessController.switchMetersToVerif()", e1);
					e1.printStackTrace();
				}
				cpf = CompletableFuture.runAsync(() -> {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						System.err.println("ERROR: InterruptedException of assync thread at ProcessController.switchMetersToVerifOrPost()");
					}
				});

				ProcessLoggerUtil.writeInfo("Switching meter in position: " + meterController.getMeter().getConversor().getIp() + " to VERIF for quality assurance");

				meterController.setAcked(false);
				while (!(acked = meterController.checkAckForCommand())) {
					if (attemptivesCount >= 100) {
						ProcessLoggerUtil.writeInfo("MESSAGE: 100 attemps to Switch meter to VERIF at ProcessController.switchMetersToVerif()");
						meterController.setForceStopCheckAckOrNack(true);
						// isSwitchToVerif = false;
						acked = false;
						break;
					}
					if (!meterController.isThreadWorking()) {
						meterController.switchStateToVerifMode();
						attemptivesCount++;
					} else {
						if (cpf.isDone()) {
							meterController.switchStateToVerifMode();
							attemptivesCount++;
							cpf = CompletableFuture.runAsync(() -> {
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									ProcessLoggerUtil.writeInfo("ERROR: InterruptedException of assync thread at ProcessController.switchMetersToVerif()");
								}
							});
						}
					}
				}

				if (acked) {// if the loop has not been interrupted
					// this will happen if in 5 tries the agc trim works
					ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " in VERIF for quality assurance");
					retorno++;
					meterController.setVerifMode(true);
				} else {
					ProcessLoggerUtil.writeInfo("Could NOT switch Meter in position: " + meterController.getMeter().getConversor().getIp() + " to VERIF for quality assurance");
				}

				meterController.setCheckForAckOrNack(false); // to set the
				meterController.setForceStopCheckAckOrNack(true); // to set the
				attemptivesCount = 0;
				acked = false;
				cpf.cancel(true);
			} else {
				ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " no selected for quality assurance");
			}
		}
		return retorno;
	}

	/**
	 * 
	 */
	public void switchMetersToPostCalib() {
		int retorno = 0;
		boolean acked = false;
		int attemptivesCount = 0;
		CompletableFuture cpf;
		// switch all meter to post calib
		for (MeterController meterController : meterControllers) {
			// if (meterController.fwVersionLong() >= meterController.fwVersionLong(new byte[] { 2, 0, 0, 0, 0, 0 })) {
			if (true /*meterController.fwVersionLong() >= meterController.fwVersionLong(ViewStatesUtil.arrayMinVersion)*/) {
				if (true /*!meterController.isQa()*/) {
					// if (!meterController.isReproved()) {
					if ((meterController.isMeterSystemDateConfigured()) && (meterController.isMeterReplaceDateConfigured()) /*&& (meterController.isWmBusConfigured())*/) {
						meterController.setEnableRead(false);
						try {
							Thread.currentThread().sleep(10);
						} catch (InterruptedException e1) {
							ProcessLog.erro(LOG, "ProcessController.switchMetersToPostCalib()", e1);
							e1.printStackTrace();
						}

						cpf = CompletableFuture.runAsync(() -> {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								System.err.println("ERROR: InterruptedException of assync thread at ProcessController.switchMetersToPostCalib()");
							}
						});

						meterController.setAcked(false);

						ProcessLoggerUtil.writeInfo("MESSAGE: 100 attemps to Switch meter to POST at ProcessController.switchMetersToPostCalib()");
						while (!(acked = meterController.checkAckForCommand())) {
							if (attemptivesCount >= 100) {
								ProcessLoggerUtil.writeInfo("MESSAGE: 100 attemps to Switch meter to POST at ProcessController.switchMetersToPostCalib()");
								meterController.setForceStopCheckAckOrNack(true);
								acked = false;
								break;
							}
							if (!meterController.isThreadWorking()) {
								meterController.switchStatePostCalibMode();
								attemptivesCount++;
							} else {
								if (cpf.isDone()) {
									meterController.switchStatePostCalibMode();
									attemptivesCount++;
									cpf = CompletableFuture.runAsync(() -> {
										try {
											Thread.sleep(500);
										} catch (InterruptedException e) {
											ProcessLoggerUtil.writeInfo("ERROR: InterruptedException of assync thread at ProcessController.switchMetersToPostCalib()");
										}
									});
								}
							}
						}

						if (acked) {// if the loop has not been interrupted
							ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " in VERIF for quality assurance");
							retorno++;
							meterController.setPostCalibMode(true);
						} else {
							ProcessLoggerUtil.writeInfo("Could NOT switch Meter in position: " + meterController.getMeter().getConversor().getIp() + " to VERIF for quality assurance");
						}

						// meterController.getMeterBean().setStatusLabelPos("EMBALA");
						meterController.setCheckForAckOrNack(false); // to set the
						meterController.setForceStopCheckAckOrNack(true); // to set the
						attemptivesCount = 0;
						acked = false;
						cpf.cancel(true);
					}
				}
			} else {
				// case version is prior to 2.0.0 set return to True
				System.err.println(
						"Did not SWITCH STATE POST CALIB MODE. Meter in position " + meterController.getMeter().getConversor().getIp() + " firmware version: " + meterController.fwVersionLong());
			}
		}
	}

	public boolean loadMetersNormalMode() {
		boolean retorno = false;
		for (MeterController meterController : meterControllers) {
			meterController.setAcked(false);
			while (!meterController.checkAckForCommand()) {
				if (!meterController.isThreadWorking()) {
					meterController.switchStateToNormalMode();
				}
			}
			meterController.setNormalMode(true);
		}
		if (areAllNormalModeLoadSucessfull()) {
			retorno = true;
		} else {
			retorno = false;
		}
		return retorno;
	}

	// public boolean loadMetersStandByMode() {
	// boolean retorno = false;
	// for (MeterController meterController : meterControllers) {
	// while (!meterController.checkAckForCommand()) {
	// if (!meterController.isThreadWorking()) {
	// meterController.switchStateToStandByMode();
	// }
	// }
	// }
	// if (areAllStandByLoadSucessfull()) {
	// retorno = true;
	// } else {
	// retorno = false;
	// }
	// return retorno;
	//
	// }

	public boolean areAllNormalModeLoadSucessfull() {
		boolean retorno = false;
		for (MeterController meterController : meterControllers) {
			if (meterController.isNormalMode()) {
				retorno = true;
			} else {
				retorno = false;
				break;
			}
		}
		return retorno;
	}

	public void disableMetersTransmiting() {
		// int retorno = 0;
		boolean acked = false;
		int attemptivesCount = 0;
		CompletableFuture cpf;
		// switch all meter to post calib
		for (MeterController meterController : meterControllers) {
			// if (!meterController.isQa()) {
			// if (!meterController.isReproved()) {
			// if ((meterController.isMeterSystemDateConfigured()) && (meterController.isMeterReplaceDateConfigured()) && (meterController.isWmBusConfigured())) {
			meterController.setEnableRead(false);
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e1) {
				ProcessLog.erro(LOG, "ProcessController.disableMetersTransmiting()", e1);
				e1.printStackTrace();
			}

			cpf = CompletableFuture.runAsync(() -> {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.err.println("ERROR: InterruptedException of assync thread at ProcessController.disableMetersTransmiting()");
				}
			});

			meterController.setAcked(false);

			// ProcessLoggerUtil.writeInfo("MESSAGE: 100 attemps to Switch meter to POST at ProcessController.disableMetersTransmiting()");
			while (!(acked = meterController.checkAckForCommand())) {
				if (attemptivesCount >= 100) {
					ProcessLoggerUtil.writeInfo("MESSAGE: 100 attemps to Switch meter at ProcessController.disableMetersTransmiting()");
					meterController.setForceStopCheckAckOrNack(true);
					acked = false;
					break;
				}
				if (!meterController.isThreadWorking()) {
					// meterController.switchStatePostCalibMode();
					meterController.disableTransmitData();
					attemptivesCount++;
				} else {
					if (cpf.isDone()) {
						// meterController.switchStatePostCalibMode();
						meterController.disableTransmitData();
						attemptivesCount++;
						cpf = CompletableFuture.runAsync(() -> {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								ProcessLoggerUtil.writeInfo("ERROR: InterruptedException of assync thread at ProcessController.disableMetersTransmiting()");
							}
						});
					}
				}
			}

			if (acked) {// if the loop has not been interrupted
				ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " disabled transmition");
				// retorno++;
				// meterController.setPostCalibMode(true);
			} else {
				ProcessLoggerUtil.writeInfo("Could NOT switch Meter in position: " + meterController.getMeter().getConversor().getIp() + " to disabled transmition");
			}

			meterController.setAcked(false);
			meterController.setCheckForAckOrNack(false); // to set the
			meterController.setForceStopCheckAckOrNack(true); // to set the
			attemptivesCount = 0;
			acked = false;
			cpf.cancel(true);
			// }
			// }
		}
	}

	// /**
	// *
	// */
	// public boolean disableMeterTransmit(MeterController meterController) {
	// boolean result = false;
	// if (meterController.isReadStatus()) { //
	//
	// // // System.out.println("ProcessController.startMeterReading().enableTransmitData()");
	// // while (!meterController.checkAckForCommand()) {
	// // if (!meterController.isThreadWorking()) {
	// // meterController.disableTransmitData();
	// // }
	// // }
	//
	// if (meterController.disableTransmitData()) {
	// ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " disabled transmition");
	// meterController.setEnableRead(false);
	// result = true;
	// } else {
	// ProcessLoggerUtil.writeInfo("Could NOT switch Meter in position: " + meterController.getMeter().getConversor().getIp() + " to disabled transmition");
	// result = false;
	// }
	// }
	// return result;
	// }

	/**
	 * 
	 */
	public boolean enableMeterTransmit(MeterController meterController) {
		boolean result = false;
		if (meterController.isReadStatus()) { //

			// System.out.println("ProcessController.startMeterReading().enableTransmitData()");
			while (!meterController.checkAckForCommand()) {
				if (!meterController.isThreadWorking()) {
					meterController.enableTransmitData();
				}
			}
			ProcessLoggerUtil.writeInfo("Meter in position: " + meterController.getMeter().getConversor().getIp() + " enabled transmition");
			meterController.setEnableRead(true);
			result = true;
		}
		return result;
	}

	public boolean purge() {
		boolean retorno = false;
		if (benchController.purge(60)) {
			retorno = true;
		} else {
			retorno = false;
		}
		return retorno;
	}

	public boolean simplifiedPurge() {
		boolean retorno = false;
		if (benchController.simplifiedPurge(60)) {
			retorno = true;
		} else {
			retorno = false;
		}
		return retorno;
	}

	public boolean checkAllMetersSampleCount() {
		boolean retorno = true;
		for (MeterController meterController : meterControllers) {
			if (!meterController.isTimedOut()) {
				if (!meterController.getHasReachedSampleCount()) {
					retorno = false;
					break;
				}
			} else {
				retorno = true;
			}
		}
		return retorno;
	}

	public boolean clearAllMetersSampleCount() {
		boolean retorno = true;
		for (MeterController meterController : meterControllers) {
			meterController.setHasReachedSampleCount(false);
		}
		return retorno;
	}

	public void clearGraphsViewFlowRate() {
		BenchBean.getInstance().getRefMeterDN02().getViewGraph().clearGraph();
		BenchBean.getInstance().getRefMeterDN08().getViewGraph().clearGraph();
		BenchBean.getInstance().getRefMeterDN32().getViewGraph().clearGraph();
	}

	public void clearGraphsView() {
		BenchBean.getInstance().getRefMeterDN02().getViewGraph().clearGraph();
		BenchBean.getInstance().getRefMeterDN08().getViewGraph().clearGraph();
		BenchBean.getInstance().getRefMeterDN32().getViewGraph().clearGraph();
		BenchBean.getInstance().getRefMeterDN02().getStdGraph().clearGraph();
		BenchBean.getInstance().getRefMeterDN08().getStdGraph().clearGraph();
		BenchBean.getInstance().getRefMeterDN32().getStdGraph().clearGraph();
	}

	public void clearMeterGraphsView() {
		for (Entry<ConversorNumbers, MeterBean> entry : ViewDataUtil.meterBeans.entrySet()) {
			entry.getValue().clearGraph();
		}
	}

	/**
	 * Sets the uncertainty limit to be visualized in the GUI
	 * 
	 * @param flowRate
	 */
	public void setUncertantyLimit(FlowRateModel flowRate) {
		if (flowRate != null) {
			String refString = flowRate.getRefMeter().getTag();
			if (!refString.isEmpty()) {

				if (refString.equals(RefMeterController.METER_TAG_DN02)) {
					BenchBean.getInstance().getRefMeterDN02().getStdGraph().setyLower(flowRate.getUncertantyLimit());
				} else {
					if (refString.equals(RefMeterController.METER_TAG_DN08)) {
						BenchBean.getInstance().getRefMeterDN08().getStdGraph().setyLower(flowRate.getUncertantyLimit());
					} else {
						if (refString.equals(RefMeterController.METER_TAG_DN32)) {
							BenchBean.getInstance().getRefMeterDN32().getStdGraph().setyLower(flowRate.getUncertantyLimit());
						}
					}
				}
			}
		}
	}

	/**
	 * Initiates the data acquisition on all meters related to this process Thread for each meter is statrted here
	 */
	public boolean startMeterReading(MeterController meterController) {
		boolean result = false;
		// if (meterControllers != null) {
		// if (!meterControllers.isEmpty()) {
		// for (MeterController meterController : meterControllers) {
		if (!meterController.isReadStatus()) { //
			if (meterController.startReading()) {

				// System.out.println("ProcessController.startMeterReading().switchStateToCalibMode()");
				while (!meterController.checkAckForCommand()) {
					if (!meterController.isThreadWorking()) {
						meterController.switchStateToCalibMode();
					}
				}

				meterController.setInCalibMod(true);
				meterController.setAcked(false);

				// System.out.println("ProcessController.startMeterReading().enableTransmitData()");
				while (!meterController.checkAckForCommand()) {
					if (!meterController.isThreadWorking()) {
						meterController.enableTransmitData();
					}
				}
				meterController.setEnableRead(true);

				result = true;
			}
		}
		return result;
	}

	/**
	 * Initiates the data acquisition on all meters related to this process Thread for each meter is statrted here
	 */
	public boolean startMetersReading() {
		boolean retorno = false;
		if (meterControllers != null) {
			if (!meterControllers.isEmpty()) {
				for (MeterController meterController : meterControllers) {
					if (!meterController.isReadStatus()) { //
						if (meterController.startReading()) {

							while (!meterController.checkAckForCommand()) {
								if (!meterController.isThreadWorking()) {
									meterController.switchStateToCalibMode();
								}
							}

							meterController.setInCalibMod(true);
							meterController.setAcked(false);

							while (!meterController.checkAckForCommand()) {
								if (!meterController.isThreadWorking()) {
									meterController.enableTransmitData();
								}
							}
							meterController.setEnableRead(true);
						}
					}
				}

				if (areAllMetersTrasmiting()) {
					retorno = true;
				} else {
					retorno = false;
				}
			}
		}
		return retorno;
	}

	/**
	 * This method signals to the meter threads to save the data they are acquiring.
	 */
	public void saveMetersData() {
		if (meterControllers != null) {
			if (!meterControllers.isEmpty()) {
				for (MeterController meterController : meterControllers) {
					meterController.clearSampleCounter();
					meterController.setHasReachedSampleCount(false);
					meterController.saveMeterData();
				}
			}
		}
	}

	/**
	 * This method signals to the meter threads to save the data they are acquiring.
	 */
	public void saveMetersVerifData() {
		if (meterControllers != null) {
			if (!meterControllers.isEmpty()) {
				for (MeterController meterController : meterControllers) {
					meterController.clearSampleCounter();
					meterController.setHasReachedSampleCount(false);
					meterController.saveMeterVerifData();
				}
			}
		}
	}

	/**
	 * This method stops saving the meter data
	 */
	public void stopSaveMetersVerifData() {
		if (meterControllers != null) {
			if (!meterControllers.isEmpty()) {
				for (MeterController meterController : meterControllers) {
					meterController.stopSaveMeterVerifData();
				}
			}
		}
	}

	/**
	 * This method stops saving the meter data
	 */
	public void stopSaveMetersData() {
		if (meterControllers != null) {
			if (!meterControllers.isEmpty()) {
				for (MeterController meterController : meterControllers) {
					meterController.stopSaveMeterData();
				}
			}
		}
	}

	/**
	 * Disconnects meters from the bench
	 */
	public boolean disconnectMeters() {
		boolean retorno = false;
		ProcessLog.setEtapa("FINALIZACAO");
		LOG.info("ETAPA INICIO: desconexao dos medidores ({} controladores)", meterControllers.size());
		long inicioDesconexao = ProcessLog.iniciarCronometro();
		if (!verifiedConnectedMeters.isEmpty()) {
			for (MeterController controller : meterControllers) {
				ProcessLog.setMedidor(controller.getMeter().getConversor().getName());
				retorno = controller.disconnectComm();
				LOG.info("Desconexao do medidor (IP {}): {}", controller.getMeter().getConversor().getIp(), retorno);
			}
			ProcessLog.limparMedidor();
		} else {
			LOG.warn("disconnectMeters(): nenhum medidor verificado/conectado a desconectar");
		}
		LOG.info("ETAPA FIM: desconexao dos medidores concluida em {} ms", ProcessLog.duracaoMs(inicioDesconexao));
		return retorno;
	}

	// /**
	// * Connects and instantiates meters in the bench
	// */
	// public void connectMeters() {
	// ConversorService conversorService = new ConversorService();
	// ConversorModel conversor;
	// MeterModel meter;
	// MeterController meterController;
	// ConversorNumbers conversorNum = ConversorNumbers.CONVERSOR1;
	// // boolean createNew = true;
	// // For connects COM and checks first package to assure meter is connected
	// for (int i = 0; i < 20; i++) {
	// // Check to see if conversor is in the list that contains the already connected meters/conversors
	// meterController = verifiedConnectedMeters.get(conversorNum);
	// if (meterController != null) {
	//
	// if (meterController.isCommInitiated()) {
	// if (meterController.isCommVerified()) {
	// if (meterController.isFwVersionVerified()) {
	// if (meterControllers.contains(meterController)) {
	// System.out.println("REFRESH - nothing changed on meter on pos: " + ConversorNumbers.getStringValue(conversorNum));
	// } else {
	// // Nao faz sentido !!!
	// // meterControllers.add(meterController);
	// }
	// } else {
	// if (meterController.readFwVersion()) {
	// meterControllers.add(meterController);
	// System.out.println("REFRESH - Verified meter on pos: " + ConversorNumbers.getStringValue(conversorNum));
	// } else {
	// System.err
	// .println("WARNING: REFRESH - Meter firmware NOT verified on ProcessController.verifiConnection(). Conversor: " + ConversorNumbers.getStringValue(conversorNum));
	// }
	// }
	// } else {
	// if (meterController.verifiConnection()) {
	// if (meterController.readFwVersion()) {
	// meterControllers.add(meterController);
	// System.out.println("REFRESH - Verified meter on pos: " + ConversorNumbers.getStringValue(conversorNum));
	// } else {
	// System.err
	// .println("WARNING: REFRESH - Meter firmware NOT verified on ProcessController.verifiConnection(). Conversor: " + ConversorNumbers.getStringValue(conversorNum));
	// }
	// } else {
	// System.err.println("WARNING: REFRESH - Meter NOT verified on ProcessController.verifiConnection(). Conversor: " + ConversorNumbers.getStringValue(conversorNum));
	// }
	// }
	// } else {
	// if (meterController.initiateComm()) {
	// if (meterController.verifiConnection()) {
	// if (meterController.readFwVersion()) {
	// meterControllers.add(meterController);
	// System.out.println("REFRESH - Verified meter on pos: " + ConversorNumbers.getStringValue(conversorNum));
	// } else {
	// System.err
	// .println("WARNING: REFRESH - Meter firmware NOT verified on ProcessController.verifiConnection(). Conversor: " + ConversorNumbers.getStringValue(conversorNum));
	// }
	// } else {
	// System.err.println("WARNING: REFRESH - Meter NOT verified on ProcessController.verifiConnection(). Conversor: " + ConversorNumbers.getStringValue(conversorNum));
	// }
	// } else {
	// System.err.println("WARNING: REFRESH - NO COMM found with conversor " + ConversorNumbers.getStringValue(conversorNum) + " connection not stablished.");
	// }
	// }
	// }
	//
	// // meterController = null
	// else {
	// try {
	// Thread.currentThread().sleep(100);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//
	// // if (createNew) {
	// conversor = conversorService.findByName(conversorNum);
	// meter = new MeterModel();
	// meterController = new MeterController();
	// meter.setConversor(conversor);
	// meter.setBatch(batch);
	// meter.setMeterTypeModel(batch.getMeterType());
	// meterController.setMeter(meter);
	// verifiedConnectedMeters.put(conversorNum, meterController);
	// if (meterController.initiateComm()) {
	// if (meterController.verifiConnection()) {
	// if (meterController.readFwVersion()) {
	// meterControllers.add(meterController);
	// System.out.println("Verified meter on pos: " + conversor.getName());
	// } else {
	// System.err.println("WARNING: Meter firmware NOT verified on ProcessController.verifiConnection(). Conversor: " + conversor.getName());
	// }
	// } else {
	// System.err.println("WARNING: Meter NOT verified on ProcessController.verifiConnection(). Conversor: " + conversor.getName());
	// }
	// } else {
	// System.err.println("WARNING: NO COMM found with conversor " + ConversorNumbers.getStringValue(conversorNum) + " connection not stablished.");
	// }
	// // } else {
	// // createNew = true;
	// }
	// conversorNum = conversorNum.next();
	// }
	// }

	// public void updateMeterControllers(MeterController meterController) {
	// boolean isUpdated = false;
	// for (MeterController controller : meterControllers) {
	// if (controller.getMeter().getConversor() == meterController.getMeter().getConversor()) {
	// meterControllers.remove(controller);
	// meterControllers.add(meterController);
	// isUpdated = true;
	// break;
	// }
	// }
	// if (!isUpdated) {
	// meterControllers.add(meterController);
	// }
	// }

	public void updateMeterControllers(MeterController meterController) {
		// for (MeterController controller : meterControllers) {
		// if (controller.getMeter().getConversor().getId() == meterController.getMeter().getConversor().getId()) {
		// controller.resetComm();
		// meterControllers.remove(controller);
		// }
		// }
		// meterControllers.add(meterController);

		// Remove all equal conversors
		Iterator<MeterController> iterator = meterControllers.iterator();
		while (iterator.hasNext()) {
			MeterController controller = iterator.next();
			if (controller.getMeter().getConversor().getId() == meterController.getMeter().getConversor().getId()) {
				controller.resetComm();
				iterator.remove();
			}
		}
		meterControllers.add(meterController);
		// orderMeterControllerCrescentByConversor(meterControllers);
		// Collections.sort(meterControllers);
	}

	/**
	 * Connects and instantiates meters in the bench
	 */
	public MeterController connectSingleMeter(ConversorNumbers conversorNum) {
		// MeterConnectionStatus result = MeterConnectionStatus.DISCONNECTED;
		boolean result = false;

		ConversorService conversorService = new ConversorService();
		ConversorModel conversor;
		MeterModel meter;
		MeterController meterController = null;
		// ConversorNumbers conversorNum = ViewStatesUtil.conversorNum;

		// Check to see if conversor is in the list that contains the already connected meters/conversors
		// meterController = verifiedConnectedMeters.get(conversorNum);
		ProcessLog.setMedidor(conversorNum);
		LOG.debug("ENTRADA connectSingleMeter(conversor={})", conversorNum);
		long inicioConnectSingle = ProcessLog.iniciarCronometro();
		conversor = conversorService.findByName(conversorNum);
		if (conversor.isEnabled()) {
			LOG.debug("Conversor {} habilitado. IP {}:{}", conversorNum, conversor.getIp(), conversor.getPort());
			if (meterController == null) {
				meter = new MeterModel();
				meterController = new MeterController();
				meter.setConversor(conversor);
				meter.setBatch(batch);
				meter.setMeterTypeModel(batch.getMeterType());
				meterController.setMeter(meter);
				// verifiedConnectedMeters.put(conversorNum, meterController);
				// }else{
				// meterController.resetComm();
			}

			if (!meterController.isCommInitiated()) {
				meterController.initiateComm();
			}

			if (meterController.isCommInitiated()) {
				if (!meterController.isCommVerified()) {
					meterController.verifiConnection();
				}
				if (meterController.isCommVerified()) {
					if (!meterController.isFwVersionVerified()) {
						meterController.readFwVersion();
					}
					//mude para true para testar a versão fcx 3.0
					if (true/*meterController.isFwVersionVerified()*/) {
						// updateMeterControllers(meterController);
						result = true;
						// System.out.println("Verified meter on pos: " + conversor.getName());
					} else {
						System.err.println("WARNING: Meter firmware NOT verified on ProcessController.verifiConnection(). Conversor: " + conversor.getName());
						LOG.warn("Firmware do medidor NAO verificado. Conversor: {}", conversor.getName());
					}
				} else {
					System.err.println("WARNING: Meter NOT verified on ProcessController.verifiConnection(). Conversor: " + conversor.getName());
					LOG.error("Medidor NAO verificado (verifiConnection falhou). Conversor: {} | IP: {}", conversor.getName(), conversor.getIp());
				}
			} else {
				System.err.println("WARNING: NO COMM found with conversor " + ConversorNumbers.getStringValue(conversorNum) + " connection not stablished.");
				LOG.error("SEM COMUNICACAO com o conversor {} - conexao nao estabelecida (IP {}:{})",
						ConversorNumbers.getStringValue(conversorNum), conversor.getIp(), conversor.getPort());
			}
		} else {
			System.err.println("WARNING: Conversor " + ConversorNumbers.getStringValue(conversorNum) + " not enabled.");
			LOG.warn("Conversor {} esta DESABILITADO no banco - medidor ignorado", ConversorNumbers.getStringValue(conversorNum));
		}

		LOG.debug("SAIDA connectSingleMeter({}): resultado={} em {} ms", conversorNum, result, ProcessLog.duracaoMs(inicioConnectSingle));
		ProcessLog.limparMedidor();
		if (result) {
			return meterController;
		} else {
			return null;
		}
	}

	// /**
	// * Connects and instantiates meters in the bench
	// */
	// public boolean connectSingleMeter(ConversorNumbers conversorNum) {
	// // MeterConnectionStatus result = MeterConnectionStatus.DISCONNECTED;
	// boolean result = false;
	//
	// ConversorService conversorService = new ConversorService();
	// ConversorModel conversor;
	// MeterModel meter;
	// MeterController meterController;
	// // ConversorNumbers conversorNum = ViewStatesUtil.conversorNum;
	//
	// // Check to see if conversor is in the list that contains the already connected meters/conversors
	// meterController = verifiedConnectedMeters.get(conversorNum);
	// conversor = conversorService.findByName(conversorNum);
	// if (conversor.isEnabled()) {
	// if (meterController == null) {
	// meter = new MeterModel();
	// meterController = new MeterController();
	// meter.setConversor(conversor);
	// meter.setBatch(batch);
	// meter.setMeterTypeModel(batch.getMeterType());
	// meterController.setMeter(meter);
	// verifiedConnectedMeters.put(conversorNum, meterController);
	// }else{
	// meterController.resetComm();
	// }
	//
	// if (!meterController.isCommInitiated()) {
	// meterController.initiateComm();
	// }
	// if (meterController.isCommInitiated()) {
	// if (!meterController.isCommVerified()) {
	// meterController.verifiConnection();
	// }
	// if (meterController.isCommVerified()) {
	// if (!meterController.isFwVersionVerified()) {
	// meterController.readFwVersion();
	// }
	// if (meterController.isFwVersionVerified()) {
	//// updateMeterControllers(meterController);
	// result = true;
	// System.out.println("Verified meter on pos: " + conversor.getName());
	// } else {
	// System.err.println("WARNING: Meter firmware NOT verified on ProcessController.verifiConnection(). Conversor: " + conversor.getName());
	// }
	// } else {
	// System.err.println("WARNING: Meter NOT verified on ProcessController.verifiConnection(). Conversor: " + conversor.getName());
	// }
	// } else {
	// System.err.println("WARNING: NO COMM found with conversor " + ConversorNumbers.getStringValue(conversorNum) + " connection not stablished.");
	// }
	// } else {
	// System.err.println("WARNING: Conversor " + ConversorNumbers.getStringValue(conversorNum) + " not enabled.");
	// }
	// return result;
	// }

	// /**
	// * Connects and instantiates meters in the bench
	// */
	// public void connectMeters() {
	// ConversorService conversorService = new ConversorService();
	// ConversorModel conversor;
	// MeterModel meter;
	// MeterController meterController;
	// ConversorNumbers conversorNum = ConversorNumbers.CONVERSOR1;
	//
	// // For connects COM and checks first package to assure meter is connected
	// for (int i = 0; i < 20; i++) {
	// // Check to see if conversor is in the list that contains the already connected meters/conversors
	// meterController = verifiedConnectedMeters.get(conversorNum);
	// conversor = conversorService.findByName(conversorNum);
	// if (conversor.isEnabled()) {
	// if (meterController == null) {
	// meter = new MeterModel();
	// meterController = new MeterController();
	// meter.setConversor(conversor);
	// meter.setBatch(batch);
	// meter.setMeterTypeModel(batch.getMeterType());
	// meterController.setMeter(meter);
	// verifiedConnectedMeters.put(conversorNum, meterController);
	// }
	//
	// if (!meterController.isCommInitiated()) {
	// meterController.initiateComm();
	// }
	// if (meterController.isCommInitiated()) {
	// if (!meterController.isCommVerified()) {
	// meterController.verifiConnection();
	// }
	// if (meterController.isCommVerified()) {
	// if (!meterController.isFwVersionVerified()) {
	// meterController.readFwVersion();
	// }
	// if (meterController.isFwVersionVerified()) {
	// // meterControllers.add(meterController);
	// updateMeterControllers(meterController);
	// System.out.println("Verified meter on pos: " + conversor.getName());
	// } else {
	// System.err.println("WARNING: Meter firmware NOT verified on ProcessController.verifiConnection(). Conversor: " + conversor.getName());
	// }
	// } else {
	// System.err.println("WARNING: Meter NOT verified on ProcessController.verifiConnection(). Conversor: " + conversor.getName());
	// }
	// } else {
	// System.err.println("WARNING: NO COMM found with conversor " + ConversorNumbers.getStringValue(conversorNum) + " connection not stablished.");
	// }
	// } else {
	// System.err.println("WARNING: Conversor " + ConversorNumbers.getStringValue(conversorNum) + " not enabled.");
	// }
	// conversorNum = conversorNum.next();
	// }
	// }

	/**
	 * 
	 */
	public void resetAllStructure() {
		// Meters controller
		for (MeterController mtrController : meterControllers) {
			mtrController.resetStructure();
		}
		processFlowRateState = ProcessFlowRateEnum.OPEN_VALVE;
		processVerifFlowRateState = ProcessFlowRateEnum.OPEN_VALVE;
	}

	public void executeProcess() {
		LOG.debug("ENTRADA executeProcess(): thread ja existente={} | estado atual={}",
				(processControllThread != null), runningState);
		if (processControllThread == null) {
			LOG.info("Criando e iniciando a thread do processo de calibracao (primeira execucao)");
			processControllThread = new Thread(this);
			this.running = true;
			this.stopProcess = false;
			processControllThread.start();
		} else {
			// Restart process
			if (runningState == StandardProcessStatesEnum.WAIT) {
				LOG.info("Reiniciando o processo de calibracao: resetando estruturas e voltando para INITIAL_RUN_CONFIGURATION");
				resetAllStructure();
				this.running = true;
				this.stopProcess = false;
				runningState = StandardProcessStatesEnum.INITIAL_RUN_CONFIGURATION;
			} else {
				LOG.warn("executeProcess() ignorado: o processo nao esta em WAIT (estado atual: {})", runningState);
			}
		}
		LOG.debug("SAIDA executeProcess()");
	}

	public void stopProcess() {
		LOG.warn("FINALIZACAO: stopProcess() solicitado. Thread ativa={} | running={} | estado={}",
				(processControllThread != null), running, runningState);
		// processControllThread = new Thread(this);
		// stopProcess = true;
		// processControllThread.interrupt();

		// if (processControllThread != null) {
		// stopProcess = true;
		// processControllThread.interrupt();
		// processControllThread = null;
		// }

		if (processControllThread != null) {
			if (running) {
				stopProcess = true;
			}
		}
	}

	/**
	 * This method gets the data collected and sets it as zeroFlowData on the meterControllers
	 */
	public void collectMetersZeroFlowData() {
		for (MeterController mtrController : meterControllers) {
			mtrController.fetchZeroFlowData();
		}
	}

	/**
	 * This method gets the data collected and sets it as zeroFlowData on the meterControllers
	 */
	public void collectMetersFlowData() {
		for (MeterController mtrController : meterControllers) {
			mtrController.fetchFlowData();
		}
	}

	/**
	 * This method gets the data collected and sets it as data on the meterControllers
	 */
	public void collectMetersVerifFlowData() {
		for (MeterController mtrController : meterControllers) {
			mtrController.fetchVerifFlowData();
		}
	}

	public void removeFlowRateFromMeters(double flowRate) {
		for (MeterController meterController : meterControllers) {
			meterController.removeFlowRate(flowRate);
		}
	}
	
	/**
	 * This method prints the expected flow rate and accumulated volume for each meter verification data.
	 */
	public void printFlowRatesAndVolumes() {
	    if (meterControllers != null) {
	        if (!meterControllers.isEmpty()) {
	            for (MeterController meterController : meterControllers) {
	                // Check if MeterDataVerif is not null and not empty before processing
	                if (meterController.getMeterDataVerif() != null && !meterController.getMeterDataVerif().isEmpty()) {
	                    for (int i = 0; i < meterController.getMeterDataVerif().size(); i++) {
	                        try {
	                            double volume = meterController.getMeterDataVerif().get(i).getAccReVolume();
	                            double flowRate = meterController.getMeterDataVerif().get(i).getExpectedFlowRate();
	                            System.out.println("Meter " + i + ": Volume = " + volume + ", Expected Flow Rate = " + flowRate);
	                        } catch (NullPointerException | IndexOutOfBoundsException e) {
	                            // Handle the case where elements of MeterDataVerif may be null or out of bounds
	                            System.out.println("Error accessing data for meter " + i);
	                        }
	                    }
	                }
	            }
	        }
	    }
	}


	/**
	 * This method displays on the view the reason for which the meter has been reproved. The first reason is always displayed and it never over written
	 *
	 * @param meterController
	 * @param reason
	 */
	public void diplayReprovedReason(MeterController meterController, String reason) {
		// if (!meterController.isReproved()) {
		meterController.getMeterBean().setLedToRed();
		meterController.getMeterBean().setStatusLabelPos(reason);
		meterController.setIsReproved(true);
		// }
	}

	public void initializeLinkToView() {
		forceCancelRepeatObservable = new SimpleBooleanProperty();
		forceCancelRepeatObservable.setValue(true);
		BenchBean.getInstance().getRepeatFlowUncertaintyProperty().bindBidirectional(forceCancelRepeatObservable);

		onlyVerificationObservable = new SimpleBooleanProperty();
		BenchBean.getInstance().getOnlyVerificationProperty().bindBidirectional(onlyVerificationObservable);
				
		
	}
}
