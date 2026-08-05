//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BenchController.java
*    @author marcos
*    @date 26 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;


import org.slf4j.Logger;

import util.ProcessLog;
import java.util.concurrent.CompletableFuture;

import bciapi.impl.BenchControlImpl;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.PidConfigModel;
import si.dbcomm.model.PumpModel;
import si.dbcomm.model.RamalModel;
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.service.FlowRateService;
import util.AssyncronousTimerUtil;
import util.ProcessLoggerUtil;

/**
 * @author marcos
 *
 */
public class BenchController {

	/** Logger SLF4J desta classe (infraestrutura de logs da bancada). */
	private static final Logger LOG = ProcessLog.get(BenchController.class);

	private BenchControlImpl bci = BenchControlImpl.getInstance();

	private AlarmController alarmController = AlarmController.getInstance();

	private BenchDataController benchDataController = BenchDataController.getInstance();

	private RefMeterController refMeterController = new RefMeterController();

	private ValveController valveController = new ValveController();

	private FlowRateController flowRateController = new FlowRateController();

	private PumpController pumpController = new PumpController();

	private WaterLineController waterLineController = new WaterLineController();

	private RamalController ramalController = new RamalController();

	private PressureSensorController pressController = new PressureSensorController();

	private TempSensorController tempSensorController = new TempSensorController();

	private LevelSensorController levelSensorController = new LevelSensorController();

	private boolean running = false;

	private boolean timerRunning = true;

	/**
	 * This method closes the line on the bench
	 * 
	 * @return boolean indicating that the line was closed
	 */
	public boolean closeLine() {
		ProcessLoggerUtil.writeInfo("--------------------Starting Close Line-----------------------");
		boolean retorno = false;
		if (waterLineController.closeLine()) {
			retorno = true;
		}
		return retorno;
	}

	public boolean openLine() {
		ProcessLoggerUtil.writeInfo("--------------------Opening Line-----------------------");
		boolean retorno = false;
		if (waterLineController.openLine()) {
			retorno = true;
		}
		return retorno;
	}

	/**
	 * Executes a purge dividing the purge time between all branches. THis is to purge all branches(ramais) and eliminate the air in all of them.
	 * 
	 */
	public boolean purge(double pumpLoad) {
		ProcessLoggerUtil.writeInfo("--------------------Starting Purge procedure--------------------");
		ProcessLog.setEtapa("PURGA");
		LOG.info("ETAPA INICIO: purga da linha com carga de bomba de {} %", pumpLoad);
		long inicioPurga = ProcessLog.iniciarCronometro();
		boolean succesFinish = false;
		valveController.fillValvesMap();

		try {
			if (waterLineController.isLineClosed()) {
				// This line of code opens the air purge valve
				// if(valveController.openValveByTag(ValveController.AIR_PURGE_VALVE_TAG)){
				if (valveController.openValveByTag(ValveController.DN32_VALVE_TAG)) {
					if (valveController.openValveByTag(ValveController.RAMAL1_VALVE_TAG)) {
						if (valveController.openLineValves()) {
							// if(flowRateController.runFlowRate(purgeFlow)){
							if (pumpController.runPumpLoad(pumpLoad, PumpController.BP1_TAG)) {
								ProcessLoggerUtil.writeInfo("Purging is set to run at " + pumpLoad + " % of pump " + PumpController.BP1_TAG + ".");
								if (!benchDataController.isRunning()) {
									benchDataController.resumeThread();
								}

								/*****/
								// alternate open and close of the airPurgeValve - 4,5 s
								for (int i = 0; i < 4; i++) {
									if (valveController.openValveByTag(ValveController.AIR_PURGE_VALVE_TAG)) {
										Thread.sleep(300);
										if (valveController.closeValveByTag(ValveController.AIR_PURGE_VALVE_TAG)) {
											Thread.sleep(100);
										}
									}
								}
								// ramal 1 is still open
								/*****/
								// alternate open and close of the bypass valve -
								// this needs to happen because of purge test
								for (int i = 0; i < 2; i++) {
									if (valveController.openValveByTag(ValveController.PRESS_REG_VALVE_TAG)) {
										Thread.sleep(300);
										if (valveController.closeValveByTag(ValveController.PRESS_REG_VALVE_TAG)) {
											Thread.sleep(100);
										}
									}
								}

								// ramal 1 is still open
								/*****/

								// Alternate reference meter valves - 4,5 s
								// the Dn32 valve was already open in the beggining of the process
								// now we open the other 2 reference meter valves and close the dn32;
								for (int i = 0; i < 2; i++) {
									if (valveController.openValveByTag(ValveController.DN08_VALVE_TAG)) {
										if (valveController.openValveByTag(ValveController.DN02_VALVE_TAG)) {
											if (valveController.closeValveByTag(ValveController.DN32_VALVE_TAG)) {
												Thread.sleep(1500);// opens dn08 and dn02, then, closes dn32
												if (valveController.openValveByTag(ValveController.DN32_VALVE_TAG)) {
													if (valveController.closeValveByTag(ValveController.DN02_VALVE_TAG)) {
														if (valveController.closeValveByTag(ValveController.DN08_VALVE_TAG)) {
															Thread.sleep(150);
														}
													}
												}
											}
										}
									}
								}
								// The dn32 valve is left open after leaving this block. DN08 and DN02 are closed

								/*****/
								// Alternate between the branches of the bench -
								// Ramal 1 has already been open on previous command

								Thread.sleep(1500);
								if (valveController.openValveByTag(ValveController.RAMAL2_VALVE_TAG)) {
									if (valveController.closeValveByTag(ValveController.RAMAL1_VALVE_TAG)) {
										// Open all the valves in sequence
										valveController.openValveByTag(ValveController.RAMAL3_VALVE_TAG);
										valveController.openValveByTag(ValveController.RAMAL4_VALVE_TAG);
										valveController.openValveByTag(ValveController.RAMAL5_VALVE_TAG);
										valveController.openValveByTag(ValveController.RAMAL6_VALVE_TAG);
										valveController.openValveByTag(ValveController.RAMAL7_VALVE_TAG);
										valveController.openValveByTag(ValveController.RAMAL8_VALVE_TAG);
										valveController.openValveByTag(ValveController.RAMAL9_VALVE_TAG);
										valveController.openValveByTag(ValveController.RAMAL10_VALVE_TAG);
										valveController.openValveByTag(ValveController.RAMAL1_VALVE_TAG);

										// Keep them open for 5 seconds
										Thread.sleep(5000);

										// Close all the valves in decreasing order
										valveController.closeValveByTag(ValveController.RAMAL10_VALVE_TAG);
										valveController.closeValveByTag(ValveController.RAMAL9_VALVE_TAG);
										valveController.closeValveByTag(ValveController.RAMAL8_VALVE_TAG);
										valveController.closeValveByTag(ValveController.RAMAL7_VALVE_TAG);
										valveController.closeValveByTag(ValveController.RAMAL6_VALVE_TAG);
										valveController.closeValveByTag(ValveController.RAMAL5_VALVE_TAG);
										valveController.closeValveByTag(ValveController.RAMAL4_VALVE_TAG);
										valveController.closeValveByTag(ValveController.RAMAL3_VALVE_TAG);
										valveController.closeValveByTag(ValveController.RAMAL2_VALVE_TAG);

										// Finally, guarantee ramal 1 is open
										valveController.openValveByTag(ValveController.RAMAL1_VALVE_TAG);

										// Indicate successful completion
										stopBenchCommand();
										succesFinish = true;

									}
								}
							}
						}
					} else {
						succesFinish = false;
					}
				}
			}
			// }
		} catch (InterruptedException e) {
			ProcessLog.erro(LOG, "BenchController.purge()", e);
			e.printStackTrace();
		}
		LOG.info("ETAPA FIM: purga da linha - resultado {} em {} ms",
				(succesFinish ? "CONCLUIDA" : "NAO CONCLUIDA"), ProcessLog.duracaoMs(inicioPurga));
		ProcessLog.limparEtapa();
		return succesFinish;
	}

	/**
	 * Executes a purge dividing the purge time between all branches. THis is to purge all branches(ramais) and eliminate the air in all of them.
	 * 
	 */
	public boolean simplifiedPurge(double pumpLoad) {
		ProcessLoggerUtil.writeInfo("--------------------Starting simplified Purge procedure--------------------");
		boolean succesFinish = false;
		valveController.fillValvesMap();

		try {
			if (waterLineController.isLineClosed()) {
				// This line of code opens the air purge valve
				// if(valveController.openValveByTag(ValveController.AIR_PURGE_VALVE_TAG)){
				if (valveController.openValveByTag(ValveController.DN32_VALVE_TAG)) {
					if (valveController.openValveByTag(ValveController.RAMAL1_VALVE_TAG)) {
						if (valveController.openLineValves()) {
							// if(flowRateController.runFlowRate(purgeFlow)){
							if (pumpController.runPumpLoad(pumpLoad, PumpController.BP1_TAG)) {
								ProcessLoggerUtil.writeInfo("Purging is set to run at " + pumpLoad + " % of pump " + PumpController.BP1_TAG + ".");
								if (!benchDataController.isRunning()) {
									benchDataController.resumeThread();
								}

								/*****/
								// alternate open and close of the airPurgeValve
								for (int i = 0; i < 4; i++) {
									if (valveController.openValveByTag(ValveController.AIR_PURGE_VALVE_TAG)) {
										Thread.sleep(300);
										if (valveController.closeValveByTag(ValveController.AIR_PURGE_VALVE_TAG)) {
											Thread.sleep(100);
										}
									}
								}
								// ramal 1 is still open
								/*****/
								// alternate open and close of the bypass valve -
								// this needs to happen because of purge test
								for (int i = 0; i < 2; i++) {
									if (valveController.openValveByTag(ValveController.PRESS_REG_VALVE_TAG)) {
										Thread.sleep(300);
										if (valveController.closeValveByTag(ValveController.PRESS_REG_VALVE_TAG)) {
											Thread.sleep(100);
										}
									}
								}

								// ramal 1 is still open
								/*****/
								Thread.sleep(5000);
								if (valveController.openValveByTag(ValveController.RAMAL1_VALVE_TAG)) {
									stopBenchCommand();
									succesFinish = true;
								}

								// // Alternate reference meter valves - 4,5 s
								// // the Dn32 valve was already open in the beggining of the process
								// // now we open the other 2 reference meter valves and close the dn32;
								// for(int i = 0; i<2; i++){
								// if(valveController.openValveByTag(ValveController.DN08_VALVE_TAG)){
								// if(valveController.openValveByTag(ValveController.DN02_VALVE_TAG)){
								// if(valveController.closeValveByTag(ValveController.DN32_VALVE_TAG)){
								// Thread.sleep(500);//opens dn08 and dn02, then, closes dn32
								// if(valveController.openValveByTag(ValveController.DN32_VALVE_TAG)){
								// if(valveController.closeValveByTag(ValveController.DN02_VALVE_TAG)){
								// if(valveController.closeValveByTag(ValveController.DN08_VALVE_TAG)){
								// Thread.sleep(300);
								// }
								// }
								// }
								// }
								// }
								// }
								// }
								// //The dn32 valve is left open after leaving this block. DN08 and DN02 are closed

								// /*****/
								// //Alternate between the branches of the bench -
								// // Ramal 1 has already been open on previous command
								//
								// if(valveController.openValveByTag(ValveController.RAMAL2_VALVE_TAG)){
								// if(valveController.closeValveByTag(ValveController.RAMAL1_VALVE_TAG)){
								// Thread.sleep(500);
								// alternateValvesPurge(ValveController.RAMAL2_VALVE_TAG, ValveController.RAMAL3_VALVE_TAG, 500, 2);
								// //-- 4,5
								//
								// if(valveController.openValveByTag(ValveController.RAMAL4_VALVE_TAG)){
								// if(valveController.closeValveByTag(ValveController.RAMAL2_VALVE_TAG)){
								// alternateValvesPurge(ValveController.RAMAL4_VALVE_TAG, ValveController.RAMAL5_VALVE_TAG, 500, 2);
								// //4 stays open
								// }
								// }
								// //-- 4 s
								//
								// if(valveController.openValveByTag(ValveController.RAMAL5_VALVE_TAG)){
								// if(valveController.closeValveByTag(ValveController.RAMAL4_VALVE_TAG)){
								// alternateValvesPurge(ValveController.RAMAL6_VALVE_TAG, ValveController.RAMAL7_VALVE_TAG, 1000, 2);
								// if(valveController.closeValveByTag(ValveController.RAMAL6_VALVE_TAG)){
								// alternateValvesPurge(ValveController.RAMAL8_VALVE_TAG, ValveController.RAMAL9_VALVE_TAG, 1000, 2);
								// if(valveController.closeValveByTag(ValveController.RAMAL8_VALVE_TAG)){
								//// alternateValvesPurge(ValveController.RAMAL9_VALVE_TAG, ValveController.RAMAL10_VALVE_TAG, 1000, 2);
								//
								// for(int i=0; i<2; i++){
								// if(valveController.openValveByTag(ValveController.RAMAL10_VALVE_TAG)){
								// Thread.sleep(500);
								// if(valveController.closeValveByTag(ValveController.RAMAL10_VALVE_TAG)){
								// Thread.sleep(500);
								// }
								// }
								// }
								//
								// if(valveController.openValveByTag(ValveController.RAMAL1_VALVE_TAG)){
								// stopBenchCommand();
								// succesFinish = true;
								// }
								// //finishing purge
								// }
								// }
								// }
								// }
								// }
								// }
							}
						}
					} else {
						succesFinish = false;
					}
				}
			}
			// }
		} catch (InterruptedException e) {
			ProcessLog.erro(LOG, "BenchController.simplifiedPurge()", e);
			e.printStackTrace();
		}
		return succesFinish;
	}

	/**
	 * Alternates opening and closing between a valve that represents one branch (ramal). the bigger ramal is left opened when this method ends. This method leave the biggerValve open to avoid
	 * pressure on the system
	 * 
	 * @param biggerValve
	 *            - The valve that opens for the ramal that has bigger capability of flowrate
	 * @param smallerValve
	 *            - The valve that opens for the ramal that has lower capability of flowrate
	 * @param msec
	 *            - between alternations
	 * @param numberOfAlternations
	 *            - number of times to alternate between the valves
	 * @return true case it is done
	 */
	private boolean alternateValvesPurge(String biggerValve, String smallerValve, long msec, int numberOfAlternations) {
		boolean retorno = false;
		try {
			for (int i = 0; i < numberOfAlternations; i++) {
				if (valveController.openValveByTag(biggerValve)) {
					if (valveController.closeValveByTag(smallerValve)) {
						Thread.sleep(msec);
						if (valveController.openValveByTag(smallerValve)) {
							if (valveController.closeValveByTag(biggerValve)) {
								Thread.sleep(msec);
							}
						}
					}
				}
			}

			// this is to assure the bigger valve will be opened when the method is done
			if (valveController.openValveByTag(biggerValve)) {
				if (valveController.closeValveByTag(smallerValve)) {

				}
			}
		} catch (InterruptedException e) {
			ProcessLog.erro(LOG, "BenchController.alternateValvesPurge()", e);
			e.printStackTrace();
		}
		return retorno;
	}

	/**
	 * Runs a Zero Flow procedure. The method blocks waiting for the command to stop. Logging of banchData and meterData is NOT controlled here;
	 * 
	 * @return
	 */
	public boolean zeroFlow() {
		boolean retorno = false;
		ProcessLoggerUtil.writeInfo("--------------------Starting Zero flow procedure--------------------");
		LOG.info("ETAPA INICIO: condicao de vazao zero - parando a bancada e fechando as valvulas de linha");
		long inicioZeroFlow = ProcessLog.iniciarCronometro();
		stopBenchCommand(); // this method sets the bench to a predefined rest state with no flow
		// informs the BenchDataController which flow rate is going to run. this must be done to associate the collected data
		// with a the expectedFlowRate
		benchDataController.setExpectedFlowRate(0.0);
		if (valveController.closeLineValves()) {
			retorno = true;
		} else {
			LOG.error("FALHA: nao foi possivel fechar as valvulas de linha para a condicao de vazao zero");
		}

		LOG.info("ETAPA FIM: condicao de vazao zero - resultado {} em {} ms",
				(retorno ? "ESTABELECIDA" : "FALHOU"), ProcessLog.duracaoMs(inicioZeroFlow));
		return retorno;
	}

	/**
	 * This method stops the zero flow process. It must be called after. It opens the lines valves
	 * 
	 * @return
	 */
	public boolean stopZeroFlow() {
		boolean retorno = false;
		ProcessLoggerUtil.writeInfo("--------------------Stopping zero flow...--------------------");
		if (stopBenchCommand()) {// TODO correct the stop command specification
			retorno = true;
		}
		return retorno;
	}

	/**
	 * This method opens the valves that control the meter water line of the bench
	 * 
	 * @return
	 */
	public boolean openLineValves() {
		boolean retorno = false;
		ProcessLoggerUtil.writeInfo("--------------------Opening LINE VALVES valves...--------------------");
		if (valveController.openLineValves()) {
			retorno = true;
		}
		return retorno;
	}

	/**
	 * 
	 * @return
	 */
	public boolean areUpAndDownStreamValvesOpened() {
		boolean retorno = false;
		if (valveController.isUpStreamValvesOpen() && valveController.isDownStreamValvesOpen()) {
			retorno = true;
		}
		return retorno;
	}

	/**
	 * 
	 * @return
	 */
	public boolean closeLineValves() {
		boolean retorno = false;
		ProcessLoggerUtil.writeInfo("--------------------Closing LINE VALVES valves...--------------------");
		if (valveController.closeLineValves()) {
			retorno = true;
		}

		return retorno;
	}

	boolean retorno = false;

	public boolean startBenchPulseCounter(RefMeterModel refMeter) {
		if (flowRateController.startCounter(refMeter)) {
			retorno = true;
		}
		return retorno;
	}

	public boolean stopBenchPulseCounter(RefMeterModel refMeter) {
		boolean retorno = false;
		if (flowRateController.stopCounter(refMeter)) {
			retorno = true;
		}
		return retorno;
	}

	public int[] getBenchPulseCounter(RefMeterModel refMeter) {
		int[] retorno = new int[2];
		retorno = flowRateController.getCounterFromBci(refMeter);
		return retorno;
	}

	public static double calcPulseToFlowRate(RefMeterModel refMeter, int timeVal, int pulseCount) {
		double timeValAux = 0.0;
		double volume = pulseCount * RefMeterController.calcFreqPulseWeight(refMeter);
		timeValAux = timeVal / 3.6;
		double flowRate = volume / timeValAux;
		flowRate = flowRate * 1000; // to L/h
		return flowRate;
	}

	public double calcPulseToFlowRate(RefMeterModel refMeter) {
		int[] data = getBenchPulseCounter(refMeter);
		int time = data[1];
		int pulses = data[0];
		double timeAux = time / 3.6;
		double volume = pulses * RefMeterController.calcFreqPulseWeight(refMeter);
		double flowRate = volume / timeAux;
		flowRate = flowRate * 1000;
		return flowRate;
	}

	public int[] getPulseAndTime(RefMeterModel refMeter) {
		int[] data = getBenchPulseCounter(refMeter);

		return data;
	}

	/**
	 * Enables the buffer used in the CLP to calculate the average flow rate
	 * 
	 * @param calcWindow
	 * @return
	 */
	public void enableFlowRateBuffer(long calcWindow, int buffSize) {
		bci.setEnableBuffer(true, calcWindow, buffSize);
	}

	/**
	 * This method runs a flow rate. The FlowRate may have a designated pump, ramal(branch) and refmeter. In case it doesn't, a default setup is choosen according to configuration specified on
	 * database. This method will switch pump, valve and refmeter selection according to flow rate.
	 * 
	 * @param flowRate
	 *            - flow rate to run.
	 * @return FlowRateStateEnum indicating the state of the flowrate.
	 * 
	 */
	public boolean runFlowRate(FlowRateModel flowRate) {
		// //TODO remove -- for testing only
		// downStreamValve.setState(ValveStateEnum.OPENED);
		// upStreamValve.setState(ValveStateEnum.OPENED);
		boolean retorno = false;
		double requestedFlowRate = flowRate.getFlowRate();
		ProcessLoggerUtil.writeInfo("--------------------Running flow rate " + requestedFlowRate + "...--------------------");
		ProcessLog.setEtapa("VAZAO_" + requestedFlowRate);
		LOG.info("ETAPA INICIO: estabilizacao da vazao {} (limite inferior={} | limite superior={})",
				requestedFlowRate, flowRate.getLowerLimit(), flowRate.getUpperLimit());
		long inicioRunFlowRate = ProcessLog.iniciarCronometro();

		// flowRate.setLowerLimit(flowRate.getFlowRate()*lowerTol);
		// flowRate.setUpperLimit(flowRate.getFlowRate()*upperTol);

		// informs the reference meter that must be read according to the choosen flowrate
		// if the flowrate does not have a predetermined ref meter to be read from it calculates the default set by the database
		if (flowRate.getRefMeter() == null) {
			flowRate.setRefMeter(refMeterController.defineRefMeterByFlowRate(flowRate));
		}
		benchDataController.setRefMeterTag(flowRate.getRefMeter().getTag());

		// Case pump is null an default pump is assined to the flowrate
		if (flowRate.getPump() == null) {
			flowRate.setPump(pumpController.definePump(flowRate.getFlowRate()));
		}

		// informs the bench data controller the expected flow rate that will run
		benchDataController.setExpectedFlowRate(requestedFlowRate);

		// Stops other pumps except the one tha should be runnig
		// In case the defined pump is already running, it will not be stopped;
		if (pumpController.stopPumpsExcept(flowRate.getPump())) {
			// These checks are done to close other valves that had been running.
			// this allows this method to switch the cofinguration of the bench to run on another flow rate
			if (refMeterController.openRefMeterValve(flowRate.getRefMeter())) {
				if (refMeterController.closeRefMeterValvesExcept(flowRate.getRefMeter().getTag())) {
					if (flowRate.getRamal() == null) {
						if (ramalController.openDefaultRamal(flowRate)) {
							if (ramalController.closeAllRamalExcept(flowRate.getRamal())) {
								if (flowRateController.runFlowRate(flowRate)) {
									retorno = true;
									if (flowRate.getPump().getTag().equals(PumpController.BREP_TAG)) {
										valveController.openValveByTag(ValveController.LINE_INPUT_FROM_SUP_RESERV_TAG);
									}
								} else {
									System.err.println("ERROR: Failed to run " + flowRate.getFlowRate() + " on BenchController.runFlowRate().");
								}
							} else {
								System.err.println("ERROR: Closing of others Ramal to run " + flowRate.getFlowRate() + " FAILED on BenchController.runFlowRate().");
							}
						} else {
							System.err.println("ERROR: Opening of default Ramal to run " + flowRate.getFlowRate() + " FAILED on BenchController.runFlowRate().");
						}
					} else {
						if (ramalController.openRamal(flowRate.getRamal())) {
							if (ramalController.closeAllRamalExcept(flowRate.getRamal())) {
								if (flowRateController.runFlowRate(flowRate)) {
									retorno = true;
									if (flowRate.getPump().getTag().equals(PumpController.BREP_TAG)) {
										valveController.openValveByTag(ValveController.LINE_INPUT_FROM_SUP_RESERV_TAG);
									}
								} else {
									System.err.println("ERROR: Failed to run " + flowRate.getFlowRate() + " on BenchController.runFlowRate().");
								}
							} else {
								System.err.println("ERROR: Closing of others Ramal to run " + flowRate.getFlowRate() + " FAILED on BenchController.runFlowRate().");
							}
						} else {
							System.err.println("ERROR: Opening of Ramal " + flowRate.getRamal().getTag() + " to run " + flowRate.getFlowRate() + " FAILED on BenchController.runFlowRate().");
						}
					}

				} else {
					System.err.println("ERROR: Closing of valves except " + flowRate.getRefMeter().getValve().getTag() + " FAILED on BenchController.runFlowRate().");
				}
			} else {
				System.err.println("ERROR: Opening of valve " + flowRate.getRefMeter().getValve().getTag() + " FAILED for reference meter " + flowRate.getRefMeter().getTag()
						+ " on BenchController.runFlowRate().");
			}
		} else {
			System.err.println("ERROR: Stoping pumps FAILED on BenchController.runFlowRate().");
			LOG.error("FALHA: parada das demais bombas nao concluiu antes de estabelecer a vazao {}", requestedFlowRate);
		}
		LOG.info("ETAPA FIM: vazao {} - resultado {} em {} ms", requestedFlowRate,
				(retorno ? "ESTABELECIDA" : "FALHOU"), ProcessLog.duracaoMs(inicioRunFlowRate));
		ProcessLog.limparEtapa();
		return retorno;
	}

	public boolean runPump(double load, RamalModel ramal, PumpModel pump, RefMeterModel refMeter) {
		ProcessLoggerUtil.writeInfo("--------------------Activating pump ...--------------------");
		boolean retorno = false;
		ramalController.openRamal(ramal);
		valveController.openRefMeterValve(refMeter);
		valveController.openLineValves();
		valveController.closeValveByTag(ValveController.PRESS_REG_VALVE_TAG);
		pumpController.runPumpLoad(load, pump);

		return retorno;
	}

	public boolean setPidAndRunFlowRate(FlowRateModel flowRate) {
		PumpModel pump;
		boolean retorno = false;
		if (flowRate != null) {
			FlowRateService flowRateService = new FlowRateService();
			flowRate.setPidConfig(flowRateService.findPidByFlowRate(flowRate));
			if ((pump = flowRate.getPump()) != null) {
				if (flowRate.getPidConfig() != null) {
					if (!Double.isNaN(flowRate.getPidConfig().getProportionalKp())) {
						if (!Double.isNaN(flowRate.getPidConfig().getIntegrativeTm())) {
							if (!Double.isNaN(flowRate.getPidConfig().getDerivativeTv())) {
								if (pumpController.setPidParamsForPump(pump, flowRate.getPidConfig().getProportionalKp(), flowRate.getPidConfig().getIntegrativeTm(),
										flowRate.getPidConfig().getDerivativeTv())) {

									runFlowRate(flowRate);
									retorno = true;
								}

							}
						}
					}
				}
			}
		}

		return retorno;
	}

	public PidConfigModel getPidParamByFlowRate(FlowRateModel flowRate) {
		PidConfigModel pidConfigModel = null;
		if (flowRate != null) {
			FlowRateService flowRateService = new FlowRateService();
			flowRate.setPidConfig(flowRateService.findPidByFlowRate(flowRate));
			pidConfigModel = pumpController.getPidParametersForPump(flowRate.getPump());
		}
		return pidConfigModel;
	}

	/**
	 * Stops the current running flowRate
	 * 
	 * @return
	 */
	public boolean stopFlowRate() {
		boolean retorno = false;
		ProcessLoggerUtil.writeInfo("--------------------Stopping flow rate ...--------------------");
		if (stopBenchCommand()) {
			retorno = true;
		}
		return retorno;
	}

	/**
	 * This method is to pressurize the line. May be used for checking for leaks and possible line failures.
	 */
	public boolean checkWaterReservoirState() {
		boolean retorno = false;

		if (levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_INF_HIGH).isLevel()) {
			retorno = true;
		}
		if (levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_SUP_HIGH).isLevel()) {
			retorno = true;
		}

		return retorno;
	}

	/**
	 * 
	 * @return
	 */
	public boolean refillReservoirs() {
		boolean retorno = false;

		if (!levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_INF_HIGH).isLevel()) {
			valveController.openValveByTag(ValveController.FEED_INF_VALVE_TAG);
		}

		// this assures that water level has reached at least the middle level sensor on inferior reservoir
		while (!levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_INF_MID).isLevel());

		// check is superior reservoir is not to level
		if (!levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_SUP_HIGH).isLevel()) {
			pumpController.runBrepPump();

			// blocks till inferior reservoir is not full
			while (!levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_SUP_HIGH).isLevel());
			pumpController.stopPumpByTag(PumpController.BREP_TAG);
		}

		//
		if (!levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_INF_HIGH).isLevel()) {
			valveController.openValveByTag(ValveController.FEED_INF_VALVE_TAG);
			while (!levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_INF_HIGH).isLevel());
			retorno = true;
		}
		valveController.closeValveByTag(ValveController.FEED_INF_VALVE_TAG);

		return retorno;
	}

	/**
	 * 
	 * @return
	 */
	public boolean refillInferiorReservoir() {
		boolean retorno = false;

		if (!levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_INF_HIGH).isLevel()) {
			valveController.openValveByTag(ValveController.FEED_INF_VALVE_TAG);
		}

		while (!levelSensorController.readLevelSensor(LevelSensorController.LEVEL_SENSOR_SUP_HIGH).isLevel()) {
			pumpController.runBrepPump();
		}

		return retorno;
	}

	/**
	 * This method is to pressurize the line. May be used for checking for leaks and possible line failures.
	 * 
	 * @param load
	 *            - the pump load to achieve desired pressure
	 * @param pumpTag
	 * @param checkTime
	 * @return
	 */
	public boolean insertPressureInLine(double load, String pumpTag, int checkTime) {
		boolean retorno = false;
		ProcessLog.setEtapa("PRESSURIZA_LINHA");
		LOG.info("ETAPA INICIO: pressurizacao da linha. Carga={} % | bomba={} | tempo de verificacao={} s", load, pumpTag, checkTime);
		long inicioPressurizacao = ProcessLog.iniciarCronometro();
		if (valveController.openValveByTag(ValveController.PRESS_REG_VALVE_TAG)) {
			if (refMeterController.closeAllRefMeterValves()) {
				if (valveController.openUpStreamValve()) {
					if (valveController.openDownStreamValve()) {
						if (waterLineController.closeLine()) {

							// Insert pressure on line
							pumpController.runPumpLoadHighPressure(load, pumpTag);

							try {
								Thread.sleep(20000);
							} catch (InterruptedException e) {
								ProcessLog.erro(LOG, "BenchController.insertPressureInLine()", e);
								e.printStackTrace();
							}
							// with the pump on we will close the upstream valve to store the pressure in line
							if (valveController.closeUpStreamValve()) {
								pumpController.stopPumpByTag(pumpTag);
								double intialPressDownStream = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DOWNSTREAM);
								double intialDiffPress = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
								LOG.info("Pressao inicial apos pressurizacao: jusante={} | diferencial={} (limite de queda: {})",
										intialPressDownStream, intialDiffPress, intialDiffPress * .95);

								CompletableFuture cf = CompletableFuture.runAsync(() -> {
									try {
										Thread.sleep(checkTime);
									} catch (InterruptedException e) {
										ProcessLog.erro(LOG, "BenchController.insertPressureInLine()", e);
										e.printStackTrace();
									}
								});

								double pressAfter;
								double diffPressAfter;
								while (!cf.isDone()) {
									pressAfter = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DOWNSTREAM);
									diffPressAfter = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);

									if (diffPressAfter <= intialDiffPress * .95) {
										LOG.error("VAZAMENTO DETECTADO: pressao diferencial caiu de {} para {} (abaixo de 95 % do valor inicial)",
												intialDiffPress, diffPressAfter);
										retorno = false;
										break;
									} else {
										retorno = true;

									}
								}
							}
						}
					}
				}
			}
		}
		valveController.closeValveByTag(ValveController.PRESS_REG_VALVE_TAG);
		LOG.info("ETAPA FIM: pressurizacao da linha - resultado {} em {} ms",
				(retorno ? "SEM VAZAMENTO" : "COM VAZAMENTO / FALHA"), ProcessLog.duracaoMs(inicioPressurizacao));
		ProcessLog.limparEtapa();
		return retorno;
	}

	/**
	 * Runs a high pressure test
	 * 
	 * @param loadLimit
	 * @param press
	 * @param tolerance
	 * @param mSecs
	 * @return
	 */
	public boolean highPressureTest(double loadLimit, double press, double tolerance, int mSecs) {
		ProcessLoggerUtil.writeInfo("--------------------Starting High pressure test--------------------");
		boolean retorno = false;
		if (loadLimit <= 100) {
			if (valveController.openValveByTag(ValveController.PRESS_REG_VALVE_TAG)) {
				if (valveController.closeDownStreamValve()) {
					if (valveController.openUpStreamValve()) {
						if (waterLineController.closeLine()) {

							double intialDiff = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
							double auxDiff;

							double currentLoad = 0;

							while (pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_UPSTREAM) < press * 1.05) {
								alarmController.readAlarmsFromBci();
								System.out.println(pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_UPSTREAM));
								if (!alarmController.isLowAirPressure()) {
									auxDiff = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
									// if(auxDiff > (intialDiff*3)){
									// this.stopBenchCommand();
									// retorno = false;
									// break;
									// }else{
									if (currentLoad <= loadLimit) {
										currentLoad++;
										pumpController.runPumpLoadHighPressure(currentLoad, PumpController.BP1_TAG);
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											ProcessLog.erro(LOG, "BenchController.highPressureTest()", e);
											e.printStackTrace();
										}
										retorno = true;
									} else {
										retorno = false;
										this.stopBenchCommand();
										break;
									}
									// }
								} else {
									retorno = false;
									this.stopBenchCommand();
									break;
								}
							}
							intialDiff = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
							auxDiff = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
							if (retorno) {
								CompletableFuture.runAsync(() -> {
									try {
										Thread.sleep(mSecs);
									} catch (InterruptedException e) {
										ProcessLog.erro(LOG, "BenchController.highPressureTest()", e);
										e.printStackTrace();
									}
								}).thenAccept((timer) -> timerRunning = false);

								while (timerRunning) {
									if (auxDiff > (intialDiff * 3) || auxDiff < (intialDiff * 0.5)) {
										break;
									}
									alarmController.readAlarmsFromBci();
									if (alarmController.isLowAirPressure()) {
										break;
									}
									if (pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_UPSTREAM) >= press * tolerance) {
										break;
									}

									auxDiff = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
									System.out.println(pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_UPSTREAM));
								}
								stopBenchCommand();
							} else {
								stopBenchCommand();
							}
						}
					}
				}
			}
		} else {
			retorno = false;
		}
		return retorno;
	}

	public boolean checkConnectionWithBci() {
		return bci.checkConnectionWithBci();
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
	 * Function sets the value for attribute running
	 * 
	 * @param running
	 *            the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean stopBenchCommandManual() {
		boolean retorno = false;
		if (pumpController.stopAllPumps()) {
			if (ramalController.closeAllRamal()) {
				if (refMeterController.closeAllRefMeterValves()) {
					if (valveController.closeLineValves()) {
						if (valveController.closeValveByTag(ValveController.FEED_INF_VALVE_TAG)) {
							if (valveController.closeValveByTag(ValveController.AIR_PURGE_VALVE_TAG)) {
								if (valveController.openValveByTag(ValveController.PRESS_REG_VALVE_TAG)) {
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										ProcessLog.erro(LOG, "BenchController.stopBenchCommandManual()", e);
										e.printStackTrace();
									}
									if (valveController.closeValveByTag(ValveController.PRESS_REG_VALVE_TAG)) {
										retorno = true;
									}
								}
							}

						}
					}
				}
			}
		}
		return retorno;
	}

	public boolean stopBenchCommand() {
		return bci.stop();
	}

	public void reset() {
		bci.reset();
	}

	public int getAlarms() {
		return bci.getAlarms();
	}

	public void incPumpLoad(int setPoint, int incStep, int incTime, String pumpTag) {
		pumpController.incPumpLoad(setPoint, incStep, incTime, pumpTag);
	}

	/**
	 * Assert for the duration of checkTime that the pressure and temperature do not go off given limits
	 * 
	 * @param upperLimitPress
	 * @param lowerLimitPress
	 * @param upperTempLimit
	 * @param lowerTempLimit
	 * @param checkTime
	 * @return TRUE case the limits are respected for the duration of process
	 */
	public boolean assertStaticZeroFlow(double upperLimitPress, double lowerLimitPress, double upperTempLimit, double lowerTempLimit, long checkTime) {
		double setPointPress = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
		double setPointTempUpStream = tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_UPSTREAM);
		double setPointTempDownStream = tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_DOWNSTREAM);

		double diffPressValAux;
		double upStreamTempAux;
		double downStreamTempAux;

		boolean retorno = true;

		AssyncronousTimerUtil asyncTimer = new AssyncronousTimerUtil(checkTime, "Assert Zero Flow Timer - Thread");
		asyncTimer.startCounter();

		diffPressValAux = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
		upStreamTempAux = tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_UPSTREAM);
		downStreamTempAux = tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_DOWNSTREAM);

		// TODO mudar varia��o para 0.5
		// while(check){
		while (!asyncTimer.isDone()) {

			if ((setPointPress * upperLimitPress) < diffPressValAux) {
				ProcessLoggerUtil.writeInfo("MESSAGE: Process error! Diferential pressure over superior limit!: " + diffPressValAux + " > " + setPointPress * upperLimitPress);
				retorno = false;
				break;
			}

			if ((setPointPress * lowerLimitPress) > diffPressValAux) {
				ProcessLoggerUtil.writeInfo("MESSAGE: Process error! Diferential pressure under inferior limit!: " + diffPressValAux + " < " + setPointPress * lowerLimitPress);
				retorno = false;
				break;
			}

			if ((setPointTempUpStream * upperTempLimit) < upStreamTempAux) {
				ProcessLoggerUtil.writeInfo("MESSAGE: Process error! UpStream temperature over superior limit!: " + setPointTempUpStream + " > " + setPointTempUpStream * upperTempLimit);
				retorno = false;
				break;
			}

			if ((setPointTempUpStream * lowerTempLimit) > upStreamTempAux) {
				ProcessLoggerUtil.writeInfo("MESSAGE: Process error! UpStream temperature under inferior limit!: " + setPointTempUpStream + " > " + setPointTempUpStream * lowerTempLimit);
				retorno = false;
				break;
			}

			if ((setPointTempDownStream * upperTempLimit) < downStreamTempAux) {
				ProcessLoggerUtil.writeInfo("MESSAGE: Process error! DownStream temperature over superior limit!: " + setPointTempDownStream + " > " + setPointTempDownStream * upperTempLimit);
				retorno = false;
				break;
			}

			if ((setPointTempDownStream * lowerTempLimit) > downStreamTempAux) {
				ProcessLoggerUtil.writeInfo("MESSAGE: Process error! DownStream temperature under inferior limit!: " + setPointTempDownStream + " > " + setPointTempDownStream * lowerTempLimit);
				retorno = false;
				break;
			}

			diffPressValAux = pressController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
			upStreamTempAux = tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_UPSTREAM);
			downStreamTempAux = tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_DOWNSTREAM);
		}
		asyncTimer.kill();
		return retorno;
	}

	/**
	 * Manually Checks if flow is stable. THis method does not use API setted parameter.
	 * 
	 * @param refMeter
	 * @return
	 */
	public boolean assertStableFlow(long checkTime, FlowRateModel flowRate) {
		return flowRateController.assertStableFlow(checkTime, flowRate);
	}

	/**
	 * Checks if flow is stable for the
	 * 
	 * @param refMeter
	 * @return
	 */
	public boolean assertStableFlow(RefMeterModel refMeter) {
		return flowRateController.assertStableFlow(refMeter);
	}

	/**
	 * 
	 */
	public boolean setEnableBuffer(int windowSize, int bufferSize, boolean state) {
		return refMeterController.setEnableBuffer(windowSize, bufferSize, state);
	}

}
