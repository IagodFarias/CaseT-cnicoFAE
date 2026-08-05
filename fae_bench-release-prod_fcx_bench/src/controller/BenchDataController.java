//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BenchDataController.java
*    @author marcos
*    @date 26 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;


import org.slf4j.Logger;

import util.ProcessLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import services.CalibrationService;
import si.dbcomm.model.BenchDataModel;
import view.bean.BenchBean;


/**
 * @author marcos
 *
 */
//public class BenchDataController extends Observable implements Runnable {
public class BenchDataController implements Runnable {

	/** Logger SLF4J desta classe (infraestrutura de logs da bancada). */
	private static final Logger LOG = ProcessLog.get(BenchDataController.class);
	
	private static BenchDataController INSTANCE;
	
	private int numberSamples = 45;
	
	private static Thread runThread;
	
	private boolean running = false;
	
	private TempSensorController tempSensorController;
	
	private PressureSensorController pressSensorController;
	
	private RefMeterController refMeterController;
	
	private FlowRateController flowRateController;
	
	private ArrayList<BenchDataModel> benchDataZeroFlow = new ArrayList<BenchDataModel>(); 
	
	private ArrayList<BenchDataModel> benchDatas = new ArrayList<BenchDataModel>(); 
	
	private ArrayList<BenchDataModel> benchDataVerif = new ArrayList<BenchDataModel>();
	
	private ScheduledExecutorService schExecutor;
	
	private boolean storeForCalibration = false;

	private boolean storeForVerification = false;
	
	private boolean dataForZeroFlow = false;
	
	private boolean reachedNumSamp = false;
	
	private boolean stop = false;
	
	private double pressDownStream;
	
	private double lastCalcStdFLow;
	
	private double tempDownStream;

	private double tempUpStream;
	
	private CalibrationService calibrationService = new CalibrationService();
	
	private volatile double expectedFlowRate = 0.0;
	
	private int dataCounterZeroFlow = 0;
	
	private int dataCounter = 0;
	
	private String refMeterTag = RefMeterController.METER_TAG_DN02;
	
	public BenchDataController(){
		if(INSTANCE == null){
			tempSensorController = new TempSensorController();
			pressSensorController = new PressureSensorController();
			refMeterController = new RefMeterController();
			flowRateController = new FlowRateController();
		}
	}
	
	int timetowait = 50;
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		BenchDataModel benchData;
		BenchDataModel benchDataAuxComp = new BenchDataModel();
		Thread.currentThread().setName("Bench Data Controller - Thread");
		ProcessLog.setEtapa("LEITURA_SENSORES");
		LOG.info("ETAPA INICIO: thread de leitura dos sensores da bancada iniciada (ciclo de 300 ms)");
		long inicioThreadSensores = ProcessLog.iniciarCronometro();
		long ciclosLidos = 0;
		
//		Interval interval;
//		DateTime timeInicial = new DateTime();
//		interval = new Interval(timeInicial, new DateTime());

		try{
			while(!stop){

//				DateTime newData = new DateTime();
//				interval = new Interval(timeInicial, newData);
//				timeInicial =newData;
//				System.out.println("BenchDataController.Interval: " + interval.toDurationMillis());
				
//				Thread.currentThread().sleep(timetowait);
				refMeterController.readRefMeter(RefMeterController.METER_TAG_DN02);

//				Thread.currentThread().sleep(timetowait);
				refMeterController.readRefMeter(RefMeterController.METER_TAG_DN08);
				
//				Thread.currentThread().sleep(timetowait);
				refMeterController.readRefMeter(RefMeterController.METER_TAG_DN32);
				
//				Thread.currentThread().sleep(timetowait);
				refMeterController.readRefMeter(this.refMeterTag);

//				Thread.currentThread().sleep(timetowait);
				flowRateController.getCounterFromBci(refMeterController.getRefMeter());
				
				pressDownStream = pressSensorController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DOWNSTREAM);
//				Thread.currentThread().sleep(timetowait);
				tempUpStream = tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_UPSTREAM); 
//				Thread.currentThread().sleep(timetowait);
				tempDownStream = tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_DOWNSTREAM);
//				Thread.currentThread().sleep(timetowait);
				ciclosLidos++;
				// Valores brutos a cada ciclo (3,3 Hz) ficam em TRACE para nao inundar o
				// arquivo; ative com -Dfae.log.level=TRACE quando precisar do detalhe.
				if (LOG.isTraceEnabled()) {
					LOG.trace("SENSORES ciclo {}: pressao jusante={} bar | temp montante={} C | temp jusante={} C | vazao ref={} | volume={} | contador={}",
							ciclosLidos, pressDownStream, tempUpStream, tempDownStream,
							refMeterController.getRefMeter().getFlowRate(),
							refMeterController.getRefMeter().getVolume(),
							flowRateController.getCounter()[0]);
				}

				benchData = new BenchDataModel(refMeterController.getRefMeter(),
						refMeterController.getRefMeter().getFlowRate(), 
						expectedFlowRate, 
						0,
//						flowRateController.calcFlowRateFromCounter(this.refMeterTag),
						refMeterController.getRefMeter().getVolume(),
						pressDownStream,
						tempUpStream,
						tempDownStream,
						flowRateController.getCounter()[1], // gets the timeread attribute
						flowRateController.getCounter()[0], // gest the counter value
						new Date());
				
//				Thread.currentThread().sleep(timetowait);
				pressSensorController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_UPSTREAM);
//				Thread.currentThread().sleep(timetowait);
				pressSensorController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF);
//				Thread.currentThread().sleep(timetowait);
				tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_RES_INF);
//				Thread.currentThread().sleep(timetowait);
				tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_RES_SUP);
						
						
//				viewData = new ViewData(benchData,
//						pressSensorController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF), 
//						pressSensorController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_UPSTREAM), 
//						tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_UPSTREAM),
//						tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_RES_INF),
//						tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_RES_SUP));
//				benchBeanView.setPressDiff(pressSensorController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DIF));
//				benchBeanView.setPressPTLI(pressSensorController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_UPSTREAM));
//				benchBeanView.setPressPTLO(pressSensorController.readValuePressSensor(PressureSensorController.PRESS_SENSOR_TAG_DOWNSTREAM));
//				benchBeanView.setTempTTLI(tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_UPSTREAM));
//				benchBeanView.setTempTTLO(tempSensorController.readValueTempSensor(TempSensorController.TEMP_SENSOR_TAG_DOWNSTREAM));
				
				
				if (storeForCalibration) {
					if (dataForZeroFlow) {
						benchDataZeroFlow.add(benchData);
						dataCounterZeroFlow++;
						LOG.debug("AMOSTRA VAZAO ZERO {}/{} armazenada: pressao jusante={} | temp montante={} | temp jusante={}",
								dataCounterZeroFlow, numberSamples, pressDownStream, tempUpStream, tempDownStream);
					} else {
						if (benchData.isValidSample(benchDataAuxComp)) {
							this.benchDatas.add(benchData);
							dataCounter++;
							LOG.debug("AMOSTRA CALIBRACAO {}/{} armazenada: vazao esperada={} | vazao ref={} | temp montante={} | temp jusante={}",
									dataCounter, numberSamples, expectedFlowRate,
									refMeterController.getRefMeter().getFlowRate(), tempUpStream, tempDownStream);
						}
					}
					if (dataCounter >= numberSamples || dataCounterZeroFlow >= numberSamples) {
						if (!reachedNumSamp) {
							LOG.info("Numero de amostras necessario ({}) atingido na bancada", numberSamples);
						}
						reachedNumSamp = true;
					}
					CompletableFuture.supplyAsync(() -> this.calculateRelativeStdDeviation(retrieveArrayForFlowRate(this.benchDatas, this.getExpectedFlowRate())));
//					System.out.println("DATA COUNTER " + dataCounter);
					benchDataAuxComp = benchData;
				}
				
				if(storeForVerification){
					if(!benchDataAuxComp.equals(benchData)){
						this.benchDataVerif.add(benchData);
						dataCounter++;
						LOG.debug("AMOSTRA VERIFICACAO {}/{} armazenada: vazao esperada={} | vazao ref={} | temp montante={}",
								dataCounter, numberSamples, expectedFlowRate,
								refMeterController.getRefMeter().getFlowRate(), tempUpStream);
						CompletableFuture.supplyAsync(() -> this.calculateRelativeStdDeviation(retrieveArrayForFlowRate(this.benchDataVerif, this.getExpectedFlowRate())));
						if(dataCounter>=numberSamples){
							if (!reachedNumSamp) {
								LOG.info("Numero de amostras de verificacao ({}) atingido", numberSamples);
							}
							reachedNumSamp = true;
						}
						benchDataAuxComp = benchData;
					}
				}
				
//				setChanged();
//				notifyObservers(viewData);
				
//				Puts the thread on sleep state till it is time to request from bench again
				Thread.currentThread().sleep((300));
			}	
			if(Thread.interrupted()){
				throw new InterruptedException();
			}
		}catch(InterruptedException ex){
			// Registra com stack trace completo, sem suprimir o tratamento original.
			ProcessLog.erro(LOG, "BenchDataController.run(): thread de leitura dos sensores interrompida apos "
					+ ciclosLidos + " ciclos e " + ProcessLog.duracaoMs(inicioThreadSensores) + " ms", ex);
			System.err.println("MESSAGE: BenchDataController Thread was interrupted. Stopping.");
		}
		LOG.info("ETAPA FIM: thread de leitura dos sensores encerrada apos {} ciclos e {} ms",
				ciclosLidos, ProcessLog.duracaoMs(inicioThreadSensores));
		ProcessLog.limparEtapa();
	}

	
	/**
	 * Function returns the instance of object BenchControlImpl
	 * @return
	 */
	public static BenchDataController getInstance(){
		if(INSTANCE == null ){
			INSTANCE = new BenchDataController();
		}
		return INSTANCE;
	}
	
	public static void benchDataControllerStart(){
		INSTANCE = new BenchDataController();
	}

	/**
	 * Function returns the value of attribute storeForCalibration
	 * @return the storeForCalibration
	 */
	public boolean isStoreForCalibration() {
		return storeForCalibration;
	}

	/**
	 * Function sets the value for attribute storeForCalibration
	 * @param storeForCalibration the storeForCalibration to set
	 */
	public void setStoreForCalibration(boolean storeForCalibration) {
		this.storeForCalibration = storeForCalibration;
	}

	/**
	 * Function returns the value of attribute storeForVerification
	 * @return the storeForVerification
	 */
	public boolean isStoreForVerification() {
		return storeForVerification;
	}

	/**
	 * Function sets the value for attribute storeForVerification
	 * @param storeForVerification the storeForVerification to set
	 */
	public void setStoreForVerification(boolean storeForVerification) {
		this.storeForVerification = storeForVerification;
	}

	/**
	 * Function returns the value of attribute benchDataZeroFlow
	 * @return the benchDataZeroFlow
	 */
	public ArrayList<BenchDataModel> getBenchDataZeroFlow() {
		return benchDataZeroFlow;
	}

	/**
	 * Function sets the value for attribute benchDataZeroFlow
	 * @param benchDataZeroFlow the benchDataZeroFlow to set
	 */
	public void setBenchDataZeroFlow(ArrayList<BenchDataModel> benchDataZeroFlow) {
		this.benchDataZeroFlow = benchDataZeroFlow;
	}

	/**
	 * Function returns the value of attribute benchDatas
	 * @return the benchDatas
	 */
	public ArrayList<BenchDataModel> getBenchData() {
		return benchDatas;
	}

	/**
	 * Function sets the value for attribute benchDatas
	 * @param benchDatas the benchDatas to set
	 */
	public void setBenchData(ArrayList<BenchDataModel> benchData) {
		this.benchDatas = benchData;
	}

	/**
	 * Function returns the value of attribute benchDataVerif
	 * @return the benchDataVerif
	 */
	public ArrayList<BenchDataModel> getBenchDataVerif() {
		return benchDataVerif;
	}

	/**
	 * Function sets the value for attribute benchDataVerif
	 * @param benchDataVerif the benchDataVerif to set
	 */
	public void setBenchDataVerif(ArrayList<BenchDataModel> benchDataVerif) {
		this.benchDataVerif = benchDataVerif;
	} 
	
	
	/**
	 * Function returns the value of attribute running
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Function sets the value for attribute running
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
	

	/**
	 * Function returns the value of attribute dataForZeroFlow
	 * @return the dataForZeroFlow
	 */
	public boolean isDataForZeroFlow() {
		return dataForZeroFlow;
	}

	/**
	 * Function sets the value for attribute dataForZeroFlow
	 * @param dataForZeroFlow the dataForZeroFlow to set
	 */
	public void setDataForZeroFlow(boolean dataForZeroFlow) {
		this.dataForZeroFlow = dataForZeroFlow;
	}

	/**
	 * Function returns the value of attribute expectedFlowRate
	 * @return the expectedFlowRate
	 */
	public double getExpectedFlowRate() {
		return expectedFlowRate;
	}

	/**
	 * Function sets the value for attribute expectedFlowRate
	 * @param expectedFlowRate the expectedFlowRate to set
	 */
	public void setExpectedFlowRate(double expectedFlowRate) {
		this.expectedFlowRate = expectedFlowRate;
		
	}

	/**
	 * Function returns the value of attribute refMeterController
	 * @return the refMeterController
	 */
	public RefMeterController getRefMeterController() {
		return refMeterController;
	}

	/**
	 * Function sets the value for attribute refMeterController
	 * @param refMeterController the refMeterController to set
	 */
	public void setRefMeterController(RefMeterController refMeterController) {
		this.refMeterController = refMeterController;
	}

	/**
	 * Function returns the value of attribute refMeterTag
	 * @return the refMeterTag
	 */
	public String getRefMeterTag() {
		return refMeterTag;
	}

	/**
	 * Function sets the value for attribute refMeterTag
	 * @param refMeterTag the refMeterTag to set
	 */
	public void setRefMeterTag(String refMeterTag) {
		this.refMeterTag = refMeterTag;
	}
	
	public void clearDataVerif(){
		benchDataVerif.clear();
	}

	public void clearDataZeroFlow(){
		benchDataZeroFlow.clear();
	}
	
	public void clearDataFlow(){
		this.benchDatas.clear();
	}
	
	/**
	 * Function returns the value of attribute dataCounterZeroFlow
	 * @return the dataCounterZeroFlow
	 */
	public int getDataCounterZeroFlow() {
		return dataCounterZeroFlow;
	}

	/**
	 * Function sets the value for attribute dataCounterZeroFlow
	 * @param dataCounterZeroFlow the dataCounterZeroFlow to set
	 */
	public void setDataCounterZeroFlow(int dataCounterZeroFlow) {
		this.dataCounterZeroFlow = dataCounterZeroFlow;
	}


	/**
	 * Function returns the value of attribute dataCounter
	 * @return the dataCounter
	 */
	public int getDataCounter() {
		return dataCounter;
	}


	/**
	 * Function sets the value for attribute dataCounter
	 * @param dataCounter the dataCounter to set
	 */
	public void setDataCounter(int dataCounter) {
		this.dataCounter = dataCounter;
	}

	/**
	 * Function returns the value of attribute reachedNumSamp
	 * @return the reachedNumSamp
	 */
	public boolean isReachedNumSamp() {
		
		return reachedNumSamp;
	}

	/**
	 * Function sets the value for attribute reachedNumSamp
	 * @param reachedNumSamp the reachedNumSamp to set
	 */
	public void setReachedNumSamp(boolean reachedNumSamp) {
		this.reachedNumSamp = reachedNumSamp;
	}


	/**
	 * Function returns the value of attribute lastCalcStdFLow
	 * @return the lastCalcStdFLow
	 */
	public double getLastCalcStdFLow() {
		return lastCalcStdFLow;
	}

	/**
	 * Function sets the value for attribute lastCalcStdFLow
	 * @param lastCalcStdFLow the lastCalcStdFLow to set
	 */
	public void setLastCalcStdFLow(double lastCalcStdFLow) {
		this.lastCalcStdFLow = lastCalcStdFLow;
	}


	public void initiateThread(){
		schExecutor = Executors.newSingleThreadScheduledExecutor();
		runThread = new Thread(this);
		runThread.setPriority(Thread.NORM_PRIORITY);
		runThread.setName("BenchDataControl-Thread");
		schExecutor.scheduleAtFixedRate(runThread, 0, 500, TimeUnit.MILLISECONDS);
//		runThread.start();
		running = true;
	}
	
	/**
	 * Pauses the BenchDataController thread and returns the running state
	 * @return boolean indicates if the thread is running
	 */
	public boolean pauseThread(){
		try {
			runThread.wait();
			if(runThread.getState() == Thread.State.WAITING){
				running = false;
			}
		} catch (InterruptedException e) {
			System.err.println("ERROR: PAUSING  BenchDataController.pauseThread()");
			ProcessLog.erro(LOG, "BenchDataController.pauseThread()", e);
			e.printStackTrace();
		}
		return running;
	}
	
	/**
	 * RESUMES the BenchDataController thread and returns the running state
	 * @return boolean indicates if the thread is running
	 */
	public boolean resumeThread(){
		if(running = false){
			if(runThread.getState() == Thread.State.WAITING){
				runThread.notify(); 
				if(runThread.getState() == Thread.State.RUNNABLE){
					running = true;
					
				}
			}
		}
		return running;
	}
	
	public ArrayList<BenchDataModel> retrieveArrayForFlowRate(ArrayList<BenchDataModel> benchAuxData, double expectedFlowRate){
		ArrayList<BenchDataModel> benchReturnData = new ArrayList<>();
		
		@SuppressWarnings("unused")
		int i = 0;
		for(BenchDataModel benchData : benchAuxData){
			if(benchData.getExpectedFlowRate() == expectedFlowRate){
				benchReturnData.add(benchData);
				i++;
			}
		}
		
		return benchReturnData;
	}
	
	public double calculateRelativeStdDeviation(ArrayList<BenchDataModel> benchData){
		double retorno = 0.;
		ArrayList<BenchDataModel> aux = benchData;
		retorno = calibrationService.relativeFlowStdDeviation(aux);
		String refMeterTag = aux.get(0).getRefMeter().getTag();
		
		if(!Double.isNaN(retorno)){
			if(!Double.isInfinite(retorno)){
				
				lastCalcStdFLow = retorno;
				
				if(refMeterTag.equals(RefMeterController.METER_TAG_DN02)){
					BenchBean.getInstance().getRefMeterDN02().setFlowRateStdDeviation(retorno);
				}else{
					if(refMeterTag.equals(RefMeterController.METER_TAG_DN08)){
						BenchBean.getInstance().getRefMeterDN08().setFlowRateStdDeviation(retorno);
					}else{
						if(refMeterTag.equals(RefMeterController.METER_TAG_DN32)){
							BenchBean.getInstance().getRefMeterDN32().setFlowRateStdDeviation(retorno);
							
						}
					}
				}
			}
		}
		return retorno;
	}
	
	public void removeDataForFlowRate(double expecFlowRate){
		
		int size = benchDatas.size()-1;
//		for(BenchDataModel benchData : benchDatas){
		for(int i = size; i >= 0; i--){
			if(expecFlowRate == benchDatas.get(i).getExpectedFlowRate()){
//				System.out.println(i+" "+benchDatas.get(i).getExpectedFlowRate());
				benchDatas.remove(i);
			}
		}
		
	}
	
	public void removeDataForVerifFlowRate(double expecFlowRate){
		int size = benchDataVerif.size()-1;
		for(int i = size; i >= 0; i--){
			if(expecFlowRate == benchDataVerif.get(i).getExpectedFlowRate()){
				benchDataVerif.remove(i);
			}
		}
	}
	
	public void printAllData(){
		int size = benchDataVerif.size()-1;
		for(int i = size; i >= 0; i--){
				System.out.println(benchDataVerif.get(i).getExpectedFlowRate());
		}
	}
	
}
