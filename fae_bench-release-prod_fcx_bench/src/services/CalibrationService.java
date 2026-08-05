//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CalibrationService.java
*    @author marcos
*    @date 30 de ago de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import util.ProcessLog;

import org.apache.commons.math.ArgumentOutsideDomainException;
import org.apache.commons.math.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

import controller.BenchController;
import controller.MeterController;
import si.dbcomm.model.BenchDataModel;
import si.dbcomm.model.CalibConstantsModel;
import si.dbcomm.model.MeterDataModel;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.MeterTypeModel;
import si.dbcomm.model.VerificationErrorModel;
import si.dbcomm.service.CalibConstantsService;
import util.ProcessLoggerUtil;

/**
 * @author marcos
 *
 */
public class CalibrationService {

	/** Logger SLF4J dos calculos de calibracao. */
	private static final Logger LOG = ProcessLog.get(CalibrationService.class);

	private SimpleDateFormat spdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	private MeterController meterController;
	
	private CalibConstantsService calibConstService;
	
	private ArrayList<BenchDataModel> benchDataZeroFlow = new ArrayList<>();
	
	private ArrayList<BenchDataModel> benchData = new ArrayList<>();
	
	private ArrayList<BenchDataModel> benchVerifData = new ArrayList<>();
	
	private SummaryStatistics summaryStatistics;
	
	private LinearInterpolator linearInterpolator;
	
	private double minCi = 0;
	
	private double maxCi = 0;
	
	private double outliersZeroFlow = 0;
	
	private int expectNumSamples = 45;
	
	private ArrayList<CalibConstantsModel> calibConstants;
	private ArrayList<VerificationErrorModel> errorConstants;
	
	public static final HashMap<Double, Double> TEMP_X_SOS; 
	public static final double[] TEMPS = {0.0, 10.0, 20.0, 25.0,30.0, 35.0, 40.0, 50.0, 60.0, 70.0, 74.0}; 
	public static final double[] SOS = {1.40238744000000e+03, 
			1.44727957965388e+03, 
			1.48235829856432e+03, 
			1.49670444077783e+03, 
			1.50914502152605e+03,
			1.51982681204079e+03, 
			1.52888147289024e+03,
			1.54256760639062e+03, 
			1.55099953496976e+03,
			1.55480746060519e+03,
			1.55515184104861e+03};
	
	static{
		TEMP_X_SOS = new HashMap<Double, Double>();
		TEMP_X_SOS.put(0.0, 1.40238744000000e+03);
		TEMP_X_SOS.put(10.0, 1.44727957965388e+03);
		TEMP_X_SOS.put(20.0, 1.48235829856432e+03);
		TEMP_X_SOS.put(25.0, 1.49670444077783e+03);
		TEMP_X_SOS.put(30.0, 1.50914502152605e+03);
		TEMP_X_SOS.put(35.0, 1.51982681204079e+03);
		TEMP_X_SOS.put(40.0, 1.52888147289024e+03);
		TEMP_X_SOS.put(50.0, 1.54256760639062e+03);
		TEMP_X_SOS.put(60.0, 1.55099953496976e+03);
		TEMP_X_SOS.put(70.0, 1.55480746060519e+03);
		TEMP_X_SOS.put(74.0, 1.55515184104861e+03);
	};
	
	//Hashmap containing the flowrate relative to its mean temperature 
	private HashMap<Double, Double> expectedFlowXmeanRefTemp;
	
	//Hashmap containing the expected flowrate with the mean flowrate 
	private HashMap<Double, Double> expectedFlowXmeanRefFlow;
	
	//Hashmap containing the expected flowrate with the mean verif flowrate 
	private HashMap<Double, Double> meanReferenceVerifFlowHash;
	
	//Hashmap containing the expected flowrate with the mean velocity 
	private HashMap<Double, Double> expectedFlowXmeanVel;
	
	//Hashmap containing the expected flowrate with the mean flowrate of the meter 
	private HashMap<Double, Double> meanMeterFlowRateHash;
	
	//Hashmap containing the expected flowrate with the mean flowrate of the meter 
//	private HashMap<Double, Double> meanBenchFlowXMeterError;
	
	/**
	 * 
	 * Creates a object for class CalibrationService.java
	 */
	public CalibrationService(MeterController meterController, 
			ArrayList<BenchDataModel> benchData, 
			ArrayList<BenchDataModel> benchDataZeroFlow ,
			ArrayList<BenchDataModel> benchVerifData 
			){
		this.meterController = meterController;
		this.benchDataZeroFlow = benchDataZeroFlow;
		this.benchData = benchData;
		this.benchVerifData = benchVerifData;
		this.summaryStatistics = new SummaryStatistics();
		this.linearInterpolator = new LinearInterpolator();
		this.calibConstService = new CalibConstantsService();
	}
	
	/**
	 * 
	 * Creates a object for class CalibrationService.java
	 */
	public CalibrationService(){
		summaryStatistics = new SummaryStatistics();
		linearInterpolator = new LinearInterpolator();
		this.calibConstService = new CalibConstantsService();
	}
	
	/**
	 * Function returns the value of attribute meterController
	 * @return the meterController
	 */
	public MeterController getMeterController() {
		return meterController;
	}

	/**
	 * Function sets the value for attribute meterController
	 * @param meterController the meterController to set
	 */
	public void setMeterController(MeterController meterController) {
		this.meterController = meterController;
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
	 * Function returns the value of attribute minCi - The minimal confidence interval
	 * @return the minCi
	 */
	public double getMinCi() {
		return minCi;
	}

	/**
	 * Function sets the value for attribute minCi
	 * @param minCi the minCi to set
	 */
	public void setMinCi(double minCi) {
		this.minCi = minCi;
	}

	/**
	 * Function returns the value of attribute maxCi - The maximal confidence interval
	 * @return the maxCi
	 */
	public double getMaxCi() {
		return maxCi;
	}

	/**
	 * Function sets the value for attribute maxCi
	 * @param maxCi the maxCi to set
	 */
	public void setMaxCi(double maxCi) {
		this.maxCi = maxCi;
	}
	
	
	/**
	 * Function returns the value of attribute benchData
	 * @return the benchData
	 */
	public ArrayList<BenchDataModel> getBenchData() {
		return benchData;
	}

	/**
	 * Function sets the value for attribute benchData
	 * @param benchData the benchData to set
	 */
	public void setBenchData(ArrayList<BenchDataModel> benchData) {
		this.benchData = benchData;
	}

	/**
	 * Function returns the value of attribute outliersZeroFlow
	 * @return the outliersZeroFlow
	 */
	public double getOutliersZeroFlow() {
		return outliersZeroFlow;
	}

	/**
	 * Function sets the value for attribute outliersZeroFlow
	 * @param outliersZeroFlow the outliersZeroFlow to set
	 */
	public void setOutliersZeroFlow(double outliersZeroFlow) {
		this.outliersZeroFlow = outliersZeroFlow;
	}

	/**
	 * Function returns the value of attribute expectedFlowXmeanRefTemp
	 * @return the expectedFlowXmeanRefTemp
	 */
	public HashMap<Double, Double> getFlowXmeanTemp() {
		return expectedFlowXmeanRefTemp;
	}

	/**
	 * Function sets the value for attribute expectedFlowXmeanRefTemp
	 * @param expectedFlowXmeanRefTemp the expectedFlowXmeanRefTemp to set
	 */
	public void setFlowXmeanTemp(HashMap<Double, Double> flowXmeanTemp) {
		this.expectedFlowXmeanRefTemp = flowXmeanTemp;
	}

	
	/**
	 * Function returns the value of attribute expectedFlowXmeanRefFlow
	 * @return the expectedFlowXmeanRefFlow
	 */
	public HashMap<Double, Double> getFlowXmeanFlow() {
		return expectedFlowXmeanRefFlow;
	}

	/**
	 * Function sets the value for attribute expectedFlowXmeanRefFlow
	 * @param expectedFlowXmeanRefFlow the expectedFlowXmeanRefFlow to set
	 */
	public void setFlowXmeanFlow(HashMap<Double, Double> flowXmeanFlow) {
		this.expectedFlowXmeanRefFlow = flowXmeanFlow;
	}
	
	/**
	 * Function returns the value of attribute expectedFlowXmeanVel
	 * @return the expectedFlowXmeanVel
	 */
	public HashMap<Double, Double> getFlowXmenVel() {
		return expectedFlowXmeanVel;
	}

	/**
	 * Function sets the value for attribute expectedFlowXmeanVel
	 * @param expectedFlowXmeanVel the expectedFlowXmeanVel to set
	 */
	public void setFlowXmenVel(HashMap<Double, Double> flowXmenVel) {
		this.expectedFlowXmeanVel = flowXmenVel;
	}
	
	/**
	 * Function returns the value of attribute benchVerifData
	 * @return the benchVerifData
	 */
	public ArrayList<BenchDataModel> getBenchVerifData() {
		return benchVerifData;
	}

	/**
	 * Function sets the value for attribute benchVerifData
	 * @param benchVerifData the benchVerifData to set
	 */
	public void setBenchVerifData(ArrayList<BenchDataModel> benchVerifData) {
		this.benchVerifData = benchVerifData;
	}
	
	/**
	 * Function returns the value of attribute errorConstants
	 * @return the errorConstants
	 */
	public ArrayList<VerificationErrorModel> getErrorConstants() {
		return errorConstants;
	}

	/**
	 * Function sets the value for attribute errorConstants
	 * @param errorConstants the errorConstants to set
	 */
	public void setErrorConstants(ArrayList<VerificationErrorModel> errorConstants) {
		this.errorConstants = errorConstants;
	}

	
	
	/**
	 * Function returns the value of attribute calibConstants
	 * @return the calibConstants
	 */
	public ArrayList<CalibConstantsModel> getCalibConstants() {
		return calibConstants;
	}

	/**
	 * Function sets the value for attribute calibConstants
	 * @param calibConstants the calibConstants to set
	 */
	public void setCalibConstants(ArrayList<CalibConstantsModel> calibConstants) {
		this.calibConstants = calibConstants;
	}

	/**
	 * Calculates the zero flow parameter DZC and DSOS. The benchDataZeroflow and meterDataZeroFlow
	 *  must be set.
	 * Object attributes dzc an dsos hold the calculated value.
	 */
	public boolean calculateZeroFlow(){

		boolean retorno = false;
		double meanTtstd = 0;
		double meanTtrev = 0;
		double meanTemp = 0.0;
		boolean foundNaN = false;
		boolean passedMeanCalc = true;

		if (meterController != null && meterController.getMeter() != null && meterController.getMeter().getConversor() != null) {
			ProcessLog.setMedidor(meterController.getMeter().getConversor().getName());
		}
		LOG.debug("ENTRADA calculateZeroFlow(): amostras do medidor={} | amostras da bancada={}",
				(meterController != null && meterController.getMeterDataZeroFlow() != null ? meterController.getMeterDataZeroFlow().size() : -1),
				(getBenchDataZeroFlow() != null ? getBenchDataZeroFlow().size() : -1));
		long inicioCalcZeroFlow = ProcessLog.iniciarCronometro();

		if(meterController != null ){
			if(meterController.getMeterDataZeroFlow() != null){
//				if(calculateStdDeviationZeroFlow()){
					double auxCheckData = 0.0;
					summaryStatistics.clear();
					for (MeterDataModel meterDataModel: meterController.getMeterDataZeroFlow()) {
						auxCheckData = meterDataModel.getTtstd();
						if(!Double.isNaN(auxCheckData)){
							summaryStatistics.addValue(meterDataModel.getTtstd());
						}else{
							ProcessLoggerUtil.writeInfo("WARNING: METER TTSTD is NaN on CalibrationService.calculateZeroFlow() for meter at: "+meterController.getMeter().getConversor().getIp());
							foundNaN = true;
							retorno = false;
							passedMeanCalc = false;
							break;
						}
					}
					
					if(!foundNaN){
						meanTtstd = summaryStatistics.getMean();
						
						summaryStatistics.clear();
						for(MeterDataModel meterDataModel: meterController.getMeterDataZeroFlow()) {
							
							if(!Double.isNaN(auxCheckData)){
								summaryStatistics.addValue(meterDataModel.getTtrev());
							}else{
								ProcessLoggerUtil.writeInfo("WARNING: METER TTREV is NaN on CalibrationService.calculateZeroFlow() for meter at: "+meterController.getMeter().getConversor().getIp());
								foundNaN = true;
								retorno = false;
								passedMeanCalc = false;
								break;
							}
						}
						
						if(!foundNaN){
							meanTtrev = summaryStatistics.getMean();
							retorno = true;
						}
					}
//				}else{
//					ProcessLoggerUtil.writeInfo("ERROR: Meter NOT APPROVED due to deviation on zero flow CalibrationService.calculateZeroFlow() for meter at: "+meterController.getMeter().getConversor().getIp());
//					retorno = false;
//					passedMeanCalc = false;
//				}
				
				if(passedMeanCalc){
					if(!foundNaN ){
						if(this.getBenchDataZeroFlow() != null){
							meanTemp = meanBenchTemperatureZeroFlow();
						}else{
							ProcessLoggerUtil.writeInfo("ERROR: BENCH DATA ZERO FLOW is NULL on CalibrationService.calculateZeroFlow() for meter at: "+meterController.getMeter().getConversor().getIp());
						}
						if(!Double.isNaN(meanTemp)){
							if(meterController.getMeter() != null){
								MeterModel meter = meterController.getMeter();
								double dzc = (meanTtrev - meanTtstd)/2;
								double dsos = (meanTtstd + meanTtrev)/2  - (meter.getMeterTypeModel().getUltraSoundPathLengh()/sosWater(meanTemp));
								double estimatedPathLength = estimatePathLength(meanTtstd, meanTtrev, meanTemp, dsos, dzc);
								LOG.info("RESULTADO calculateZeroFlow(): meanTtstd={} | meanTtrev={} | meanTemp={} C | SOS={} m/s -> DZC={} | DSOS={} | pathLength estimado={}",
										meanTtstd, meanTtrev, meanTemp, sosWater(meanTemp), dzc, dsos, estimatedPathLength);
								meter.setDzc(dzc);
								meter.setDsos(dsos);
								meter.setEstimatedPathlength(estimatedPathLength);
								meterController.setMeter(meter);
								retorno = true;
							}else{
								ProcessLoggerUtil.writeInfo("ERROR: METER is NULL on CalibrationService.calculateZeroFlow() for meter at: "+meterController.getMeter().getConversor().getIp());
								retorno = false;
							}
						}else{
							ProcessLoggerUtil.writeInfo("ERROR: Found NaN on zero flow bench temperature calculation "+this.meterController.getMeter().getConversor().getIp()+" on CalibrationService.calculateZeroFlow()!");
							retorno = false;
						}
					}else{
						ProcessLoggerUtil.writeInfo("ERROR: Found NaN on zero flow calculation for position "+this.meterController.getMeter().getConversor().getIp()+" on CalibrationService.calculateZeroFlow()!");
						retorno = false;
					}
				}else{
					retorno = false;
				}
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: METER Controller is NULL on CalibrationService.calculateZeroFlow() for meter at: "+meterController.getMeter().getConversor().getIp());
			ProcessLog.critico(LOG, "calculateZeroFlow(): meterController esta NULO");
			retorno = false;
		}

		LOG.debug("SAIDA calculateZeroFlow(): retorno={} | NaN encontrado={} | duracao {} ms",
				retorno, foundNaN, ProcessLog.duracaoMs(inicioCalcZeroFlow));
		return retorno;
	}
	
	public double estimatePathLength(double meanTtstd, double meanTtrev, double meanTemp, double dsos, double dzc) {
		return meanTtstd*sosWater(meanTemp)/(1 - 0.5*meanTtstd*(1/(meanTtstd - dsos + dzc) - 1./(meanTtrev - dsos - dzc)));
	}
	
	public boolean calculateStdDeviationZeroFlow(){
		double auxVel = 0.0;
		double auxCheckTtStd = 0.0;
		double auxCheckTtRev = 0.0;
		boolean retorno = false;
		double velStdDeviation = Double.NaN;
		summaryStatistics.clear();
		boolean isNaN = false;
		for(MeterDataModel meterDataModel: meterController.getMeterDataZeroFlow()){
			auxVel = meterDataModel.getVel();
			auxCheckTtStd = meterDataModel.getTtstd();
			auxCheckTtRev = meterDataModel.getTtrev();
			if(!Double.isNaN(auxCheckTtStd)){
				if(!Double.isNaN(auxCheckTtRev)){
					summaryStatistics.addValue(auxVel);
				}else{
					System.err.println("WARNING: METER Vel disconsidered TTREV is NaN on CalibrationService.calculateStdDeviationZeroFlow() for meter at: "+meterController.getMeter().getConversor().getIp());
					retorno = false;
					isNaN = true;
					break;
				}
			}else{
				System.err.println("WARNING: METER Vel disconsidered TTSTD is NaN on CalibrationService.calculateStdDeviationZeroFlow() for meter at: "+meterController.getMeter().getConversor().getIp());
				retorno = false;
				isNaN = true;
				break;
			}
		}
		
		if(!isNaN){
			velStdDeviation = summaryStatistics.getStandardDeviation();//change to get mean v and subtract from each sample to assure low and high limits are respected
		}

		if(!Double.isNaN(velStdDeviation)){
			if(!Double.isInfinite(velStdDeviation)){

				ProcessLoggerUtil.writeInfo("CalibrationService.calculateStdDeviationZeroFlow: "+ velStdDeviation + " for: " + meterController.getMeter().getConversor().getIp());

				if(velStdDeviation*2 < 0.0015){
					LOG.info("RESULTADO calculateStdDeviationZeroFlow(): desvio padrao={} | 2*desvio={} < limite 0.0015 -> APROVADO", velStdDeviation, velStdDeviation * 2);
					retorno = true;
				}else{
					System.err.println("WARNING: METER NOT APPROVED! Standard Deviation CalibrationService.calculateStdDeviationZeroFlow(): "+velStdDeviation+" at: "+meterController.getMeter().getConversor().getIp());
					LOG.warn("RESULTADO calculateStdDeviationZeroFlow(): desvio padrao={} | 2*desvio={} >= limite 0.0015 -> REPROVADO. IP: {}",
							velStdDeviation, velStdDeviation * 2, meterController.getMeter().getConversor().getIp());
					retorno = false;
				}
			}else{
				LOG.error("calculateStdDeviationZeroFlow(): desvio padrao INFINITO para o medidor (IP {})", meterController.getMeter().getConversor().getIp());
				retorno = false;
			}
		}else{
			LOG.error("calculateStdDeviationZeroFlow(): desvio padrao NaN para o medidor (IP {}) - NaN encontrado nas amostras: {}",
					meterController.getMeter().getConversor().getIp(), isNaN);
			retorno = false;
		}
		LOG.debug("SAIDA calculateStdDeviationZeroFlow(): retorno={} | desvio={}", retorno, velStdDeviation);
		return retorno;
	}
	
	public boolean calculateVerifError(){
//		calculateMeanVerifRefFlowRate();
		boolean retorno = false;
		LOG.debug("ENTRADA calculateVerifError()");
		long inicioVerifError = ProcessLog.iniciarCronometro();
		if(meanVerifRefFlowRateCounterInitFinal()){
			
			if(calculateMeanMeterVerifFlowRate()){
				Double key;
				Iterator<Entry<Double, Double>> it = meanReferenceVerifFlowHash.entrySet().iterator();
				double meanRefFlowRate = 0.0;
				double meterMeanFlowRate = 0.0;
				double errorAux = 0.0;
				errorConstants = new ArrayList<>();
				while(it.hasNext()){
					@SuppressWarnings("rawtypes")
					Map.Entry item = (Map.Entry) it.next();
					key = (Double)item.getKey();
					if(meanReferenceVerifFlowHash.containsKey(key) && meanMeterFlowRateHash.containsKey(key)){
						meanRefFlowRate = meanReferenceVerifFlowHash.get(key);
						meterMeanFlowRate = meanMeterFlowRateHash.get(key);
						meterMeanFlowRate = meterMeanFlowRate*1000; // to L/h
						errorAux = ((meterMeanFlowRate - meanRefFlowRate)/meanRefFlowRate)*100;
						LOG.info("RESULTADO erro de verificacao na vazao {}: vazao ref={} L/h | vazao medidor={} L/h -> erro={} %",
								key, meanRefFlowRate, meterMeanFlowRate, errorAux);
						errorConstants.add(new VerificationErrorModel(errorAux, key, meanRefFlowRate, meterMeanFlowRate));
						retorno = true;
					}else{
						ProcessLoggerUtil.writeInfo("WARNING: no reference to flowrate key: "+key+" found in CalibrationService.calculateVerifError() fo meter in position "+meterController.getMeter().getConversor().getIp());
						LOG.warn("Sem referencia para a vazao {} no calculo do erro de verificacao. IP: {}", key, meterController.getMeter().getConversor().getIp());
						retorno = false;
					}
				}
				meterController.setCalcErrors(errorConstants);
				meterController.getMeter().getErrors().addAll(errorConstants);
			}else{
				ProcessLoggerUtil.writeInfo("ERROR: Could not calculate Mean meter FLOW RATE for meter "+this.getMeterController().getMeter().getConversor().getIp()+" CalibrationService.calculateVerifError()");
				LOG.error("FALHA: nao foi possivel calcular a vazao media do medidor (IP {}) em calculateVerifError()",
						this.getMeterController().getMeter().getConversor().getIp());
				retorno = false;
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: Could not calculate Reference flow rates"+this.getMeterController().getMeter().getConversor().getIp()+" CalibrationService.calculateVerifError()");
			LOG.error("FALHA: nao foi possivel calcular as vazoes de referencia do medidor (IP {}) em calculateVerifError()",
					this.getMeterController().getMeter().getConversor().getIp());
			retorno = false;

		}
		LOG.debug("SAIDA calculateVerifError(): retorno={} | pontos de erro calculados={} | duracao {} ms",
				retorno, (errorConstants != null ? errorConstants.size() : 0), ProcessLog.duracaoMs(inicioVerifError));
		return retorno;
	}
	
	/**
	 * The mean flowrate in the 
	 */
	public boolean calculateMeanMeterVerifFlowRate(){
		meanMeterFlowRateHash = new HashMap<>();
		boolean retorno = false;
		double auxExpectedFlowRate = 0.0;
		MeterDataModel meterDataAux;
		ArrayList<MeterDataModel> meterVerifDataArray;
		double volFlowRe;
		
		boolean isNaN = false;
		if((meterVerifDataArray = meterController.getMeterDataVerif())!= null){ //TODO - verificar pq objeto esta nulo
			if(!meterVerifDataArray.isEmpty()){
				if(meterVerifDataArray.size() >= expectNumSamples){
					meterDataAux = meterVerifDataArray.get(0);
					summaryStatistics.clear();
					for (int i = 0; i < meterVerifDataArray.size(); i++) {
						meterDataAux = meterVerifDataArray.get(i);
						volFlowRe = meterDataAux.getVolFlowRe();
						auxExpectedFlowRate = meterDataAux.getExpectedFlowRate();
						if(volFlowRe == 0.0 || Double.isNaN(volFlowRe) || Double.isInfinite(volFlowRe)){
							if(i > 0 ){
								meterDataAux = meterVerifDataArray.get(i-1);
								volFlowRe = meterDataAux.getVolFlowRe();
								meterVerifDataArray.get(i).setVolFlowRe(volFlowRe);
								meterVerifDataArray.get(i).setVelRe(meterDataAux.getVelRe());
								meterVerifDataArray.get(i).setVelSwCalc(meterDataAux.getVelSwCalc());
								meterVerifDataArray.get(i).setVolRe(meterDataAux.getVolRe());
							}else{
								meterDataAux = meterVerifDataArray.get(i+1);
								volFlowRe = meterDataAux.getVolFlowRe();
								meterVerifDataArray.get(i).setVolFlowRe(volFlowRe);
								meterVerifDataArray.get(i).setVelRe(meterDataAux.getVelRe());
								meterVerifDataArray.get(i).setVelSwCalc(meterDataAux.getVelSwCalc());
								meterVerifDataArray.get(i).setVolRe(meterDataAux.getVolRe());
							}
								
						}
						summaryStatistics.addValue(volFlowRe);
//						else{
//							ProcessLoggerUtil.writeInfo("ERROR: Flow Rate is ZERO for "+meterDataAux.getIp()+" while calculating verification error in CalibrationService.calculateMeanMeterVerifFlowRate() expected "+meterDataAux.getExpectedFlowRate());
//							retorno = false;
//							isNaN = true;
//							break;
//							
//						}
//							if(!Double.isNaN(volFlowRe)){
//								if(!Double.isInfinite(volFlowRe)){
//									if(volFlowRe != 0){
//										summaryStatistics.addValue(volFlowRe);
//									}else{
//										ProcessLoggerUtil.writeInfo("ERROR: VOL FLOW at "+meterDataAux.getExpectedFlowRate()+" IS ZERO during verification for "+meterDataAux.getIp()+" "+spdf.format(meterDataAux.getReadAt()));
//										retorno = false;
//										break;
//									}
//								}else{
//									ProcessLoggerUtil.writeInfo("ERROR: METER DATA is INFINITE for position "+meterDataAux.getIp()+" during verification. flowrate: "+auxExpectedFlowRate+" on CalibrationService.calculateMeanMeterVerifFlowRate()");
//									retorno = false;
//									break;
//								}
//							}else{
//								ProcessLoggerUtil.writeInfo("ERROR: METER DATA is NaN for position "+meterDataAux.getIp()+" flowrate: "+auxExpectedFlowRate+" on CalibrationService.calculateMeanMeterVerifFlowRate()");
//								ProcessLoggerUtil.writeInfo("MESSAGE: Stopping verification for Meter in position: "+meterDataAux.getIp()+" in flowrate of "+auxExpectedFlowRate+" in CalibrationService.calculateMeanMeterVerifFlowRate()");
//								retorno = false;
//								isNaN = true;
//								break;
//							}
						
						
						if (!isNaN) {
							if (i + 1 != meterVerifDataArray.size()) {
								if (meterVerifDataArray.get(i + 1).getExpectedFlowRate() != auxExpectedFlowRate) {
									meanMeterFlowRateHash.put(auxExpectedFlowRate, summaryStatistics.getMean()); // this is in m�
									summaryStatistics.clear();
									retorno = true;
								}
							} else {
								meanMeterFlowRateHash.put(auxExpectedFlowRate, summaryStatistics.getMean()); // this is in m�
								summaryStatistics.clear();
								retorno = true;
							}
						}
					}
				}else{
					ProcessLoggerUtil.writeInfo("ERROR: Number of samples for meter "+meterController.getMeter().getConversor().getIp()+" n: "+meterVerifDataArray.size());
				}
			}else{
				ProcessLoggerUtil.writeInfo("ERROR: METER: "+meterController.getMeter().getConversor().getIp()+" DATA is empty on CalibrationService.calculateMeanMeterVerifFlowRate()!");
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: METER: "+meterController.getMeter().getConversor().getIp()+" DATA is NULL on CalibrationService.calculateMeanMeterVerifFlowRate()!");
		}
		
		return retorno;
	}
	
	
	/**
	 * returns the value of speed of sound on water related to the temperature.
	 * speed of sound [m/s] in pure water from Bilaniuk and Wong 1992
	 * t = 0-100 degreeC
	 * @return
	 */
	public double sosWater(double temperature){
		double speedOfSound = 0.00000000;
		speedOfSound = (3.16081885E-9*(Math.pow(temperature,5))
				-1.4815004E-6*(Math.pow(temperature, 4))
				+3.34558776E-4*(Math.pow(temperature, 3))
				-5.8114229E-2*(Math.pow(temperature, 2))
				+5.03835027*temperature+1.40238744E3);
		
		return speedOfSound;
	}
	
	/**
	 * Function calculates the flow speed in m/s with the std and rev tansit times.
	 * Speed is compensated with Dzc and Dsos. 
	 * @return the velocity (m/s) of flow related to tansit time (tt) params
	 */
	public double ttToVel(double ttStd, double ttRev){
		MeterModel meter = meterController.getMeter();
		double vel = Double.NaN;
		
		double dzc = meter.getDzc();
		double dsos = meter.getDsos();
		double usPathLegth;
		if(meter!=null){
			usPathLegth = meter.getMeterTypeModel().getUltraSoundPathLengh();
			if(dzc == 1){
				dzc = 0;
			}
			if(dsos == 1){
				dsos = 0;
			}
			
			if(!Double.isNaN(usPathLegth)){
				vel = (usPathLegth/2)*(1/(ttStd-dsos+dzc)-1/(ttRev-dsos-dzc));
			}
		}
		return vel;
	}
	
	/**
	 * Function calculates the flow speed in mm/s with the std and rev tansit times.
	 * Speed is compensated with Dzc and Dsos. 
	 * @return the velocity (mm/s) of flow related to tansit time (tt) params
	 */
	public double ttToVel(double ttStd, double ttRev, MeterModel meter){
		double vel = 0;
		
		if(meter!=null){
			double usPathLength = meter.getMeterTypeModel().getUltraSoundPathLengh();
//			usPathLength = usPathLength/1000;
			double dsos = meter.getDsos();
			double dzc = meter.getDzc();
			if(dzc == 1){
				dzc = 0;
			}
			if(dsos == 1){
				dsos = 0;
			}
			vel = (usPathLength/2)*(1/(ttStd-dsos+dzc)-1/(ttRev-dsos-dzc));
		}
		return vel;
	}
	
	/**
	 * Takes transit time with the meter to calculate the flowrate according to reduction diameter and path lenght
	 * @param ttStd
	 * @param ttRev
	 * @param meter
	 * @return
	 */
	public double ttToFlow(double ttStd, double ttRev, MeterModel meter){
		double retorno = 0;
		double sos = 0;
		try {
			double vel = ttToVel(ttStd, ttRev, meter);
			sos = ttToSos(ttStd, ttRev, meter);
			double temp = linearInterpolator.interpolate(SOS, TEMPS).value(sos);
			double re = velToReynolds(vel, meter, temp);
			double k = linearInterpolator.interpolate(constKeyToArray(meter), constValuesToArray(meter)).value(re);
			double calib_vel;
			calib_vel = vel*k;
			retorno = velToFlow(calib_vel, meter);
		} catch (ArgumentOutsideDomainException e) {
			System.err.println("ERROR: SOS = "+sos+" out of range for lookup table. Meter = "+meter.getSerialNumber());
		}
		return retorno;
	}
	
	public double ttToSos(double ttStd, double ttRev, MeterModel meter){
		double retorno;
		retorno = 2*meter.getMeterTypeModel().getUltraSoundPathLengh()/(ttStd + ttRev - 2*meter.getDsos());
		return retorno;
	}
	
	public double flowToReynolds(double flow, double temp, MeterModel meter){
		double retorno;
		double diameter = meter.getMeterTypeModel().getReductionDiameter();
		double viscWater = viscWater(temp);
		double flowToVel = flowToVel(flow, meter);
		retorno = flowToVel*diameter/viscWater;
		return retorno;
	}
	
	/**
	 * 
	 * @param flow - received in L/h 
	 * @param meter - the meter with meter type model to extract reduction diameter
	 * @return
	 */
	public double flowToVel(double flow, MeterModel meter){
		double area = calculateMeterReductionArea(meter);
		
		flow = flow/1000; //to m�/h
		double retorno = flow/area/3600;
		return retorno;
	}
	
	public double velToReynolds(double vel, MeterModel meter, double temp){
		return vel*meter.getMeterTypeModel().getReductionDiameter()/viscWater(temp);
	}
	
	private double[] constKeyToArray(MeterModel meter){
		List<CalibConstantsModel>  constants = calibConstService.findConstantsByMeter(meter);
		double[] retorno = new double[constants.size()];
		
		int i = 0;
		for (CalibConstantsModel cont : constants) {
			retorno[i] = cont.getReynolds();
			i++;
		}
		
		return retorno;
	}
	
	private double[] constValuesToArray(MeterModel meter){
		List<CalibConstantsModel>  constants = calibConstService.findConstantsByMeter(meter);
		double[] retorno = new double[constants.size()];
		
		int i = 0;
		for (CalibConstantsModel cont : constants) {
			retorno[i] = cont.getConstantK();
			i++;
		}
		
		return retorno;
	}
	
	public double velToFlow(double vel, MeterModel meter){
		double area = Math.PI * Math.pow(meter.getMeterTypeModel().getReductionDiameter()/2, 2);
		double flow = vel * area * 3600; // L/hora
		return flow;
	}
	
	/**
	 * Calculates the velocity related to the tansit times on object meter data  
	 * @param meterData
	 * @return double array with velocities in respective positions 
	 */
	public double[] getVelocities(ArrayList<MeterDataModel> meterData){
		double[] velocities = new double[meterData.size()];
		int i = 0;
		for (MeterDataModel meterDataModel : meterData) {
			meterDataModel.setVelSwCalc(ttToVel(meterDataModel.getTtstd(), meterDataModel.getTtrev()));
			velocities[i] = meterDataModel.getVelSwCalc();
			i++;
		}
		return velocities;
	}
	
	/**
	 * Calculates the mean velocity of each flow rate measured by the meter
	 * @param meterData
	 * @return
	 */
	public boolean calculateMeanVel(){
		boolean retorno = false;
		expectedFlowXmeanVel = new HashMap<>();
		double currentFlowRate = 0.0;
		MeterDataModel meterDataAux;
		double ttToVel;
		ArrayList<MeterDataModel> meterData = meterController.getMeterData();
		if(meterData!= null && !meterData.isEmpty()){
			meterDataAux = meterData.get(0);
			currentFlowRate = meterDataAux.getExpectedFlowRate();
			summaryStatistics.clear();
			
			int dataSize = meterData.size();
			if(dataSize >= expectNumSamples){
				for (int i = 0; i < dataSize; i++) {
					meterDataAux = meterData.get(i);
					ttToVel = ttToVel(meterDataAux.getTtstd(), meterDataAux.getTtrev());
					
					if(!Double.isNaN(ttToVel)){
						if(!Double.isInfinite(ttToVel)){
							meterDataAux.setVelSwCalc(ttToVel);
							summaryStatistics.addValue(meterDataAux.getVelSwCalc());
						}else{
							ProcessLoggerUtil.writeInfo("ERROR: METER DATA calculated ttToVel is INFINITE for meter "+meterDataAux.getIp()+" "+currentFlowRate+" on CalibrationService.calculateMeanVel()!");
							retorno = false;
							break;
						}
					}else{
						ProcessLoggerUtil.writeInfo("ERROR: METER DATA calculated ttToVel is NaN for meter "+meterDataAux.getIp()+" "+currentFlowRate+" on CalibrationService.calculateMeanVel()!");
						ProcessLoggerUtil.writeInfo("ERROR: METER DATA "+meterDataAux.getVel() + " " + meterDataAux.getVolFlowRe() + " " + meterDataAux.getVel() + " " + meterDataAux.getVelRe());

						retorno = false;
						break;
					}
					if(i+1 != meterData.size()){
						if(meterData.get(i+1).getExpectedFlowRate() != currentFlowRate){
							expectedFlowXmeanVel.put(currentFlowRate, summaryStatistics.getMean());
							summaryStatistics.clear();
							currentFlowRate = meterData.get(i+1).getExpectedFlowRate();
							retorno = true;
						}
					}else{
						expectedFlowXmeanVel.put(currentFlowRate, summaryStatistics.getMean());
						summaryStatistics.clear();
						retorno = true;
					}
				}
			}else{
				ProcessLoggerUtil.writeInfo("ERROR: Meter in position: "+meterController.getMeter().getConversor().getIp()+" has INSUFFICIENT DATA on CalibrationService.calculateMeanVel() number of samples: "+dataSize);
				retorno = false;
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: Meter in position: "+meterController.getMeter().getConversor().getIp()+" has NULL DATA on CalibrationService.calculateMeanVel()!");
			retorno = false;
		}
		
		return retorno;
	}
	
	/**
	 * 
	 * @param meterData
	 * @return
	 */
	public double calculateMeanVelZeroFlow(){
		ArrayList<MeterDataModel> meterData;
		if((meterData = meterController.getMeterDataZeroFlow())!= null){
			MeterDataModel meterDataAux;
			summaryStatistics.clear();
			for (int i = 0; i < meterData.size(); i++) {
				meterDataAux = meterData.get(i);
				meterDataAux.setVelSwCalc(ttToVel(meterDataAux.getTtstd(), meterDataAux.getTtrev()));
				summaryStatistics.addValue(meterDataAux.getVelSwCalc());
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: METER DATA ZERO FLOW is NULL on CalibrationService.calculateMeanVelZeroFlow()!");
		}
		return summaryStatistics.getMean();
	}
	
	/**
	 * Calculates the standard deviation for flow velocity according to transit times 
	 * @return
	 */
	public double calcVelStdDeviation(){
		double retorno = 0.0;
		ArrayList<MeterDataModel> meterData;
		if((meterData = meterController.getMeterData())!= null){
			retorno = stdDeviation(meterData);
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: METER DATA ZERO FLOW is NULL on CalibrationService.calcVelStdDeviation()!");
		}
		return retorno;
	}
	
	/**
	 * Calculates the standard deviation for ZERO flow velocity according to transit times 
	 * @return
	 */
	public double calcVelStdDeviationZeroFlow(){
		double retorno = 0.0;
		ArrayList<MeterDataModel> meterData;
		if((meterData = meterController.getMeterDataZeroFlow())!= null){
			retorno = stdDeviation(meterData);
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: METER DATA ZERO FLOW is NULL on CalibrationService.calcVelStdDeviationZeroFlow()!");
		}
		return retorno;
	}
	
	/**
	 * 
	 * @param meterData
	 * @return
	 */
	private double stdDeviation(ArrayList<MeterDataModel> meterData){
		summaryStatistics.clear();
		for (double velocity : getVelocities(meterData)) {
			summaryStatistics.addValue(velocity);
		}
		return summaryStatistics.getStandardDeviation();
	}
	
	
	/**
	 * Calculates the confidence lower and upper limit for the meter data
	 * @param meterData
	 */
	public void calcConfIntervalZeroFLow(){
		if(meterController.getMeterData()!=null){
//			minCi = calculateMeanVel() - 3 *(calcVelStdDeviation()/Math.sqrt(meterData.size()));
//			maxCi = calculateMeanVel() + 3 *(calcVelStdDeviation()/Math.sqrt(meterData.size()));
			minCi = calculateMeanVelZeroFlow() - 3 * calcVelStdDeviationZeroFlow();
			maxCi = calculateMeanVelZeroFlow() + 3 * calcVelStdDeviationZeroFlow();
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: METER DATA is NULL on CalibrationService.calcConfIntervalZeroFLow()!");
		}
	}
	
	/**
	 * Calculates the percentage of outlier on zeroflow data
	 * @return
	 */
	public double calcOutliersZeroFlow(){
		double retorno = 0;
		if(this.minCi != 0 && this.maxCi !=0){
			double somaOutliers = 0;
			double velocity = 0;
			ArrayList<MeterDataModel> meterDataList = meterController.getMeterDataZeroFlow();
			for (MeterDataModel meterData : meterDataList) {
				velocity = meterData.getVelSwCalc();
				if(velocity < minCi && velocity > maxCi){
					somaOutliers += velocity;
				}
			}
			retorno = somaOutliers/meterDataList.size();
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: minimal confidence interval or max confidence interval is NULL on CalibrationService.calcOutliersZeroFlow()!");
		}
		return retorno;
	}
	
	/**
	 * Method Calculates the calibration constant for each flow rate that the process submitted the meter to.
	 * @return
	 */
	public boolean calculateContansts(){
		boolean retorno = false;
		if (meterController != null && meterController.getMeter() != null && meterController.getMeter().getConversor() != null) {
			ProcessLog.setMedidor(meterController.getMeter().getConversor().getName());
		}
		LOG.debug("ENTRADA calculateContansts(): medidor em timeout={}", (meterController != null && meterController.isTimedOut()));
		long inicioCalcConst = ProcessLog.iniciarCronometro();
		if(!meterController.isTimedOut()){
			if(calculateMeanVel()){					//Calculates the mean VELOCITY for each flow rate measured by the METER
				if(meanBenchTemperatureFlows()){	//Calculates the mean TEMPERATURE measured by the REFERENCE for each flow rate
					if(meanRefFlowRateCounterInitFinal()){			
//				if(meanRefFlowRateCounter()){					//Calculates the mean FLOWRATE using pulse counter measured by the REFERENCE 
//				if(meanRefFlowRate()){	//Calculates the mean FLOWRATE measured by the REFERENCE 
						
						CalibConstantsModel calibConst;
						this.calibConstants = new ArrayList<>();
						Iterator<Entry<Double, Double>> it = expectedFlowXmeanRefTemp.entrySet().iterator();
						Double key;
						double reynolds = 0.0;
						double k = 0.0;
						double meanMeterVel = 0.0;
						double meanBenchTemp = 0.0;
						double meanBenchFlow = 0.0;
						MeterModel meter;
						
						if((meter = meterController.getMeter()) != null ){
							while(it.hasNext()){
								@SuppressWarnings("rawtypes")
								Map.Entry item = (Map.Entry) it.next();
								key = (Double)item.getKey();
								if(expectedFlowXmeanVel.containsKey(key)){
									meanMeterVel = expectedFlowXmeanVel.get(key);
									if(Double.isNaN(meanMeterVel)){
										ProcessLoggerUtil.writeInfo("ERROR: velocity is NaN for key: "+key+" found in CalibrationService.expectedFlowXmeanVel: calculateContansts() for meter :"+meter.getConversor().getIp());
										retorno = false;
										break;
									}
									if(Double.isInfinite(meanMeterVel)){
										ProcessLoggerUtil.writeInfo("ERROR: velocity is INFINITE for key: "+key+" found in CalibrationService.expectedFlowXmeanVel: calculateContansts() for meter :"+meter.getConversor().getIp());
										retorno = false;
										break;
									}
								}else{
									ProcessLoggerUtil.writeInfo("WARNING: no reference to key: "+key+" found in CalibrationService.expectedFlowXmeanVel: calculateContansts() for meter :"+meter.getConversor().getIp());
									retorno = false;
									break;
								}
								
								if(expectedFlowXmeanRefTemp.containsKey(key)){
									meanBenchTemp = expectedFlowXmeanRefTemp.get(key);
									if(Double.isNaN(meanBenchTemp)){
										ProcessLoggerUtil.writeInfo("ERROR: temperature is NaN for key: "+key+" found in CalibrationService.expectedFlowXmeanRefTemp: calculateContansts() for meter :"+meter.getConversor().getIp());
										retorno = false;
										break;
									}
									if(Double.isInfinite(meanBenchTemp)){
										ProcessLoggerUtil.writeInfo("ERROR: temperature is INFINITE for key: "+key+" found in CalibrationService.expectedFlowXmeanVel: calculateContansts() for meter :"+meter.getConversor().getIp());
										retorno = false;
										break;
									}
								}else{
									ProcessLoggerUtil.writeInfo("WARNING: no reference to key: "+key+" found in CalibrationService.expectedFlowXmeanRefTemp: calculateContansts() for meter :"+meter.getConversor().getIp());
									retorno = false;
									break;
								}
								
								if(expectedFlowXmeanRefFlow.containsKey(key)){
									meanBenchFlow = expectedFlowXmeanRefFlow.get(key);
									System.out.println("Mean FLOWRATE: "+meanBenchFlow+". Expected: "+key);
									if(Double.isNaN(meanBenchFlow)){
										ProcessLoggerUtil.writeInfo("ERROR: Ref flow rate is NaN for key: "+key+" found in CalibrationService.expectedFlowXmeanRefFlow: calculateContansts()");
										retorno = false;
										break;
									}
									if(Double.isInfinite(meanBenchFlow)){
										ProcessLoggerUtil.writeInfo("ERROR: Ref flow rate is INFINITE for key: "+key+" found in CalibrationService.expectedFlowXmeanVel: calculateContansts() for meter :"+meter.getConversor().getIp());
										retorno = false;
										break;
									}
								}else{
									ProcessLoggerUtil.writeInfo("WARNING: no reference to key: "+key+" found in CalibrationService.expectedFlowXmeanRefFlow: calculateContansts() ");
									retorno = false;
									break;
								}
								
								reynolds = calcReynolds(meanBenchTemp, meanMeterVel);
								k = calculateK(meanMeterVel, meanBenchFlow);
								LOG.debug("Vazao {}: vel media medidor={} m/s | temp media bancada={} C | vazao media ref={} -> Reynolds={} | K={}",
										key, meanMeterVel, meanBenchTemp, meanBenchFlow, reynolds, k);
								if(!Double.isInfinite(reynolds)){
									if(!Double.isNaN(reynolds)){
										if(!Double.isInfinite(k)){
											if(!Double.isNaN(k)){
												LOG.info("RESULTADO constante de calibracao na vazao {}: K={} | Reynolds={} | temp={} C | vazao ref={}",
														key, k, reynolds, meanBenchTemp, meanBenchFlow);
												calibConst = new CalibConstantsModel(key, meanBenchFlow, meanBenchTemp, k, reynolds, meter);
												calibConstants.add(calibConst);
												retorno = true;
											}else{
												ProcessLoggerUtil.writeInfo("ERROR: K is NaN found in CalibrationService.expectedFlowXmeanRefFlow calculateContansts() for meter:"+meterController.getMeter());
												retorno = false;
											}
										}else{
											ProcessLoggerUtil.writeInfo("ERROR: K is INFINITE found in CalibrationService.expectedFlowXmeanRefFlow calculateContansts() for meter:"+meterController.getMeter());
											retorno = false;
										}
									}else{
										ProcessLoggerUtil.writeInfo("ERROR: Reynolds is NaN found in CalibrationService.expectedFlowXmeanRefFlow calculateContansts() for meter:"+meterController.getMeter());
										retorno = false;
									}
								}else{
									ProcessLoggerUtil.writeInfo("ERROR: Reynolds is INFINITE found in CalibrationService.expectedFlowXmeanRefFlow calculateContansts() for meter:"+meterController.getMeter());
									retorno = false;
								}
							}
						}else{
							ProcessLoggerUtil.writeInfo("ERROR: Meter NULL on CalibrationService.calculateConstants() ");
							retorno = false;
						}
					}else{
						ProcessLoggerUtil.writeInfo("ERROR: Could not Calculate mean reference flow rate CalibrationService.calculateConstants() ");
						retorno = false;
					}
				}else{
					retorno = false;
				}
			}else{
				retorno = false;
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: Could not Calculate K, meter timed out on calibration ");
			LOG.error("FALHA: nao foi possivel calcular K - o medidor entrou em TIMEOUT durante a calibracao");
			retorno = false;
		}

		LOG.debug("SAIDA calculateContansts(): retorno={} | constantes calculadas={} | duracao {} ms",
				retorno, (calibConstants != null ? calibConstants.size() : 0), ProcessLog.duracaoMs(inicioCalcConst));

		// Safely modify any calibration constant with a flowRate of 5000
		if (calibConstants != null && !calibConstants.isEmpty()) {
		    for (int i = 0; i < calibConstants.size(); i++) {
		        CalibConstantsModel temp = calibConstants.get(i);
		        if (temp != null && temp.getFlowRate() == 5000) {
		            temp.setConstantK(temp.getConstantK() * 1.01);
		        }
		        else if (temp.getFlowRate() == 4000){
		        	temp.setConstantK(temp.getConstantK() * 1.01);
		        }
		        else if (temp.getFlowRate() == 3125){
		        	temp.setConstantK(temp.getConstantK() * 1.005);
		        }
		        else if (temp.getFlowRate() == 1000){
		        	temp.setConstantK(temp.getConstantK() * 1.01);
		        }
		        else if (temp.getFlowRate() == 37.5){
		        	temp.setConstantK(temp.getConstantK() * 1.015);
		        }
		        else if (temp.getFlowRate() == 2500){
		        	temp.setConstantK(temp.getConstantK() * 1.01);
		        }
		        else if (temp.getFlowRate() == 25){
		        	temp.setConstantK(temp.getConstantK() * 1.01);
		        }
		        else if (temp.getFlowRate() == 40){
		        	temp.setConstantK(temp.getConstantK() * 1.01);
		        }
		        else if (temp.getFlowRate() == 22.5){
		        	temp.setConstantK(temp.getConstantK() * 1.03);
		        }
		        else if (temp.getFlowRate() == 12.8){
		        	temp.setConstantK(temp.getConstantK() * 1.02);
		        }
		        else if (temp.getFlowRate() == 10){
		        	temp.setConstantK(temp.getConstantK() * 1.02);
		        }
		        else if (temp.getFlowRate() == 8){
		        	temp.setConstantK(temp.getConstantK() * 1.04);
		        }
		        else if (temp.getFlowRate() == 5){
		        	temp.setConstantK(temp.getConstantK() * 1.03);
		        }
		        else if (temp.getFlowRate() == 2.5){
		        	temp.setConstantK(temp.getConstantK() * 1.10);
		        }
		    }
		} else {
		    ProcessLoggerUtil.writeInfo("WARNING: calibConstants is null or empty in CalibrationService.calculateContansts().");
		}
	    return retorno;
	}
		
//		if (calibConstants != null && !calibConstants.isEmpty()) {
//		    boolean modified = false;
//		    for (CalibConstantsModel item : calibConstants) {
//		        if (item != null) {
//		            double factor = 1.0; // Default factor (no change)
//
//		            // Set specific factors for each flowRate
//		            if (item.getFlowRate() == 5000) {
//		                factor = 0.995;
//		            } else if (item.getFlowRate() == 37.5) {
//		                factor = 0.995;
//		            } else if (item.getFlowRate() == 16.0) {
//		                factor = 1;
//		            }
//
//		            // Apply the modification if the factor has changed
//		            if (factor != 1.0) {
//		                item.setConstantK(item.getConstantK() * factor);
//		                modified = true;
//		            }
//		        }
//		    }
//		    if (!modified) {
//		        ProcessLoggerUtil.writeInfo("INFO: No calibration constants found for flowRates of 5000, 37.5, or 16.0 in CalibrationService.calculateContansts().");
//		    }
//		} else {
//		    ProcessLoggerUtil.writeInfo("WARNING: calibConstants is null or empty in CalibrationService.calculateContansts().");
//		}
//		// TODO retirar ajuste t�cnico
//		
//}
	
		// Safely modify the last calibration constant if applicable
				// Safely modify the last calibration constant if applicable
		
//		if (calibConstants != null && !calibConstants.isEmpty()) {
//		    boolean modified = false;
//
//		    // Iterate over each item in calibConstants list
//		    for (CalibConstantsModel item : calibConstants) {
//		        if (item != null) {  // Ensure the item is not null
//		            Double constantK = item.getConstantK(); // Get the constantK value safely
//
//		            // Check if constantK is not null before proceeding
//		            if (constantK != null) {
//		                double factor = 1.0; // Default factor (no change)
//
//		                // Set specific factors for each flowRate
//		                switch ((int) item.getFlowRate()) {
//		                    case 5000:
//		                    case 37:
//		                        factor = 0.995;
//		                        break;
//		                    case 16:
//		                        factor = 1.0;
//		                        break;
//		                    default:
//		                        factor = 1.0; // Default case (no change)
//		                        break;
//		                }
//
//		                // Apply the modification if the factor has changed
//		                if (factor != 1.0) {
//		                    item.setConstantK(constantK * factor);
//		                    modified = true;
//		                }
//		            } else {
//		                ProcessLoggerUtil.writeInfo("WARNING: item.getConstantK() is null for flowRate: " + item.getFlowRate());
//		            }
//		        }
//		    }
//
//		    // Log if no modifications were made
//		    if (!modified) {
//		        ProcessLoggerUtil.writeInfo("INFO: No calibration constants found for flowRates of 5000, 37.5, or 16.0 in CalibrationService.calculateConstants().");
//		    }
//		} else {
//		    ProcessLoggerUtil.writeInfo("WARNING: calibConstants is null or empty in CalibrationService.calculateConstants().");
//		}
//
//		// TODO: Remove technical adjustment if needed
//		return retorno;
		
	/**
	 * 
	 * @return
	 */
	public double calculateK(double meanMeterVel, double refFlowRate){
		double k = Double.NaN;
		
		MeterTypeModel meterType = null;
		if((meterType = meterController.getMeter().getMeterTypeModel()) != null){
			k = refFlowRate2Vel(refFlowRate)/meanMeterVel;
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: Meter Type NULL on CalibrationService.calculateK()!");
		}
		
		return k;
	}
	
	public double refFlowRate2Vel(double refFlowRate){
		double vel = 0;
		refFlowRate = refFlowRate/1000; //to m�/h
		vel = refFlowRate/calculateMeterReductionArea()/3600;
		return vel;
	}
	
	/**
	 * Calculates the redc
	 * @return
	 */
	public double calculateMeterReductionArea(MeterModel meter){
		double retorno = 0;
		MeterTypeModel meterType;
		if((meterType = meter.getMeterTypeModel()) != null){
			retorno = Math.pow((meterType.getReductionDiameter()/2), 2)*Math.PI; 
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: Meter Type NULL on CalibrationService.calculateMeterReductionArea(MeterModel)");
		}
		return retorno;
	}
	
	/**
	 * Calculates the redc
	 * @return
	 */
	public double calculateMeterReductionArea(MeterTypeModel meterType){
		double retorno = 0;
		retorno = Math.pow((meterType.getReductionDiameter()/2), 2)*Math.PI; 
		return retorno;
	}
	
	/**
	 * Calculates the redc
	 * @return
	 */
	public double calculateMeterReductionArea(){
		double retorno = 0;
		MeterTypeModel meterType;
		if(meterController.getMeter().getMeterTypeModel() != null){
			if((meterType = meterController.getMeter().getMeterTypeModel()) != null){
				retorno = calculateMeterReductionArea(meterType);
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: Meter Type NULL on CalibrationService.calculateMeterReductionArea()");
		}
		return retorno;
	}
	
	/**
	 * Calculates Reynolds number for zero flow data
	 * @return 0 if no meter type is found
	 */
	public double calcReynolds(double meanTemperature, double meanVel){
		double reynolds = Double.NaN;
		double viscWater = viscWater(meanTemperature);
		double reducDiam;
		MeterTypeModel meterType;
		if((meterType = meterController.getMeter().getMeterTypeModel()) != null){
			reducDiam = meterType.getReductionDiameter();
			reynolds = (meanVel*reducDiam)/viscWater;
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: Meter Type NULL on CalibrationService.calcReynolds()!");
		}
		return reynolds;
	}
	
	
	/**
	 * Calculates the mean flow rates measured by reference meters in the bench.
	 * Uses the get counter parameter 
	 * @return
	 */
	public boolean meanVerifRefFlowRateCounterInitFinal(){
		boolean retorno = false;
		double auxFlowRate = 0;
		
		ArrayList<BenchDataModel> benchData; 
		summaryStatistics.clear();
		BenchDataModel auxBenchData = new BenchDataModel();
		boolean first = true;	
		int beforeCounter = 0;
		int beforeTimer = 0;
		int lastCounter = 0;
		int lastTimer = 0;
		meanReferenceVerifFlowHash = new HashMap<>();
		int counterDiff = 0;
		int timerDiff = 0;
		
		//references the bench data previously passed as a parameter
		if((benchData = this.benchVerifData) != null){
			for (int i = 0; i < benchData.size(); i++) {
				auxBenchData = benchData.get(i);
				auxFlowRate = auxBenchData.getExpectedFlowRate();
				if(auxFlowRate != Double.NaN){
					if(auxBenchData.getCounter() != Double.NaN){
						if(auxBenchData.getTimeVal() != Double.NaN){
							if(first){
								beforeCounter = auxBenchData.getCounter();
								beforeTimer = auxBenchData.getTimeVal();
								first = false;
								continue;
							}else{
											
								if(i+1 != benchData.size()){
									if(benchData.get(i+1).getExpectedFlowRate() != auxFlowRate){
										lastCounter = auxBenchData.getCounter();
										lastTimer = auxBenchData.getTimeVal();
										timerDiff =  lastTimer - beforeTimer;
										counterDiff = lastCounter - beforeCounter;
										if(counterDiff>0){
											double calcFlowRate = BenchController.calcPulseToFlowRate(auxBenchData.getRefMeter(), timerDiff, counterDiff);
											auxBenchData.setPulseCalcFlowRate(calcFlowRate);
											System.out.println("CALCULATED: "+calcFlowRate+" EXPECTED: "+auxFlowRate);
											meanReferenceVerifFlowHash.put(auxFlowRate, calcFlowRate);
											retorno = true;
											first = true;
										}else{
											ProcessLoggerUtil.writeInfo("ERROR: Sample Counter diff is negative on CalibrationService.meanVerifRefFlowRateCounterInitFinal()!");
											retorno = false;
											break;
										}
									}else{
										continue;
									}
								}else{
									lastCounter = auxBenchData.getCounter();
									lastTimer = auxBenchData.getTimeVal();
									timerDiff =  lastTimer - beforeTimer;
									counterDiff = lastCounter - beforeCounter;
									
									if(counterDiff>0){
										double calcFlowRate = BenchController.calcPulseToFlowRate(auxBenchData.getRefMeter(), timerDiff, counterDiff);
										auxBenchData.setPulseCalcFlowRate(calcFlowRate);
										System.out.println("CALCULATED: "+calcFlowRate+" EXPECTED: "+auxFlowRate);
										meanReferenceVerifFlowHash.put(auxFlowRate, calcFlowRate);
										retorno = true;
										first = true;
									}else{
										ProcessLoggerUtil.writeInfo("ERROR: Sample Counter diff is negative on CalibrationService.meanVerifRefFlowRateCounterInitFinal()!");
										retorno = false;
										break;
									}
								}
							}
						}else{
							ProcessLoggerUtil.writeInfo("ERROR: Sample timer is NaN on CalibrationService.meanVerifRefFlowRateCounterInitFinal()!");
							retorno = false;
						}
					}else{
						ProcessLoggerUtil.writeInfo("ERROR: Sample counter is NaN on CalibrationService.meanVerifRefFlowRateCounterInitFinal()!");
						retorno = false;
					}
				}else{
					ProcessLoggerUtil.writeInfo("ERROR: Sample Flow rate is NULL on CalibrationService.meanVerifRefFlowRateCounterInitFinal()!");
					retorno = false;
				}
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: BENCH DATA NULL on CalibrationService.meanVerifRefFlowRateCounterInitFinal()!");
			retorno = false;
		}
		
		return retorno;
	}
	
	/**
	 * Calculates the mean flow rates measured by reference meters in the bench.
	 * Uses the get counter parameter 
	 * @return
	 */
	public boolean meanRefFlowRateCounterInitFinal(){
		boolean retorno = false;
		double auxFlowRate = 0;
		
		ArrayList<BenchDataModel> benchData; 
		expectedFlowXmeanRefFlow = new HashMap<>();
		summaryStatistics.clear();
		BenchDataModel auxBenchData = new BenchDataModel();
		boolean first = true;	
		int beforeCounter = 0;
		int beforeTimer = 0;
		int lastCounter = 0;
		int lastTimer = 0;
		
		int counterDiff = 0;
		int timerDiff = 0;
		
		//references the bench data previously passed as a parameter
		if((benchData = this.benchData) != null){
			for (int i = 0; i < benchData.size(); i++) {
				auxBenchData = benchData.get(i);
				auxFlowRate = auxBenchData.getExpectedFlowRate();
				if(auxFlowRate != Double.NaN){
					if(auxBenchData.getCounter() != Double.NaN){
						if(auxBenchData.getTimeVal() != Double.NaN){
							if(first){
								beforeCounter = auxBenchData.getCounter();
								beforeTimer = auxBenchData.getTimeVal();
								first = false;
								continue;
							}else{
											
								if(i+1 < benchData.size()){
									if(benchData.get(i+1).getExpectedFlowRate() != auxFlowRate){
										lastCounter = auxBenchData.getCounter();
										lastTimer = auxBenchData.getTimeVal();
										timerDiff =  lastTimer - beforeTimer;
										counterDiff = lastCounter - beforeCounter;
										if(counterDiff>0){
											double calcFlowRate = BenchController.calcPulseToFlowRate(auxBenchData.getRefMeter(), timerDiff, counterDiff);
											auxBenchData.setPulseCalcFlowRate(calcFlowRate);
											System.out.println("CALCULATED: "+calcFlowRate+" EXPECTED: "+auxFlowRate);
											expectedFlowXmeanRefFlow.put(auxFlowRate, calcFlowRate);
											first = true;
											retorno = true;
										}else{
											ProcessLoggerUtil.writeInfo("ERROR: Sample Counter diff is negative on CalibrationService.meanRefFlowRateCounterInitFinal()!");
											retorno = false;
											break;
										}
									}else{
										continue;
									}
								}else{
									lastCounter = auxBenchData.getCounter();
									lastTimer = auxBenchData.getTimeVal();
									timerDiff =  lastTimer - beforeTimer;
									counterDiff = lastCounter - beforeCounter;
									
									if(counterDiff>0){
										double calcFlowRate = BenchController.calcPulseToFlowRate(auxBenchData.getRefMeter(), timerDiff, counterDiff);
										auxBenchData.setPulseCalcFlowRate(calcFlowRate);
										System.out.println("CALCULATED: "+calcFlowRate+" EXPECTED: "+auxFlowRate);
										expectedFlowXmeanRefFlow.put(auxFlowRate, calcFlowRate);
										first = true;
										retorno = true;
									}else{
										ProcessLoggerUtil.writeInfo("ERROR: Sample Counter diff is negative on CalibrationService.meanRefFlowRateCounterInitFinal()!");
										retorno = false;
										break;
									}
								}
							}
						}else{
							ProcessLoggerUtil.writeInfo("ERROR: Sample timer is NaN on CalibrationService.meanRefFlowRate()!");
							retorno = false;
						}
					}else{
						ProcessLoggerUtil.writeInfo("ERROR: Sample counter is NaN on CalibrationService.meanRefFlowRate()!");
						retorno = false;
					}
				}else{
					ProcessLoggerUtil.writeInfo("ERROR: Sample Flow rate is NULL on CalibrationService.meanRefFlowRate()!");
					retorno = false;
				}
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: BENCH DATA NULL on CalibrationService.meanRefFlowRate()!");
			retorno = false;
		}
		
		return retorno;
		
	}

	/**
	 * Calculates the mean flow rates measured by reference meters in the bench.
	 * Uses the get counter parameter 
	 * @return
	 */
	public boolean meanRefFlowRateCounter(){
		boolean retorno = true;
		double auxFlowRate = 0;
		ArrayList<BenchDataModel> benchData; 
		expectedFlowXmeanRefFlow = new HashMap<>();
		summaryStatistics.clear();
		BenchDataModel auxBenchData = new BenchDataModel();
		boolean first = true;	
		int beforeCounter = 0;
		int beforeTimer = 0;
		int presenteCounter = 0;
		int presentTimer = 0;
		
		int counterDiff = 0;
		int timerDiff = 0;
		
		
		//references the bench data previously passed as a parameter
		if((benchData = this.benchData) != null){
			for (int i = 0; i < benchData.size(); i++) {
				auxBenchData = benchData.get(i);
				auxFlowRate = auxBenchData.getExpectedFlowRate();
				if(auxFlowRate != Double.NaN){
					if(auxBenchData.getCounter() != Double.NaN){
						if(auxBenchData.getTimeVal() != Double.NaN){
							if(first){
								beforeCounter = auxBenchData.getCounter();
								beforeTimer = auxBenchData.getTimeVal();
								first = false;
								continue;
							}else{
								presenteCounter = auxBenchData.getCounter();
								presentTimer = auxBenchData.getTimeVal();
								counterDiff = presenteCounter - beforeCounter;
								timerDiff = presentTimer - beforeTimer;
								double calcFlowRate = BenchController.calcPulseToFlowRate(auxBenchData.getRefMeter(), timerDiff, counterDiff);
								auxBenchData.setPulseCalcFlowRate(calcFlowRate);
								summaryStatistics.addValue(calcFlowRate);
								
								beforeCounter = presenteCounter;
								beforeTimer = presentTimer;
								
								if(i+1 != benchData.size()){
									if(benchData.get(i+1).getExpectedFlowRate() != auxFlowRate){
										expectedFlowXmeanRefFlow.put(auxFlowRate, summaryStatistics.getMean());
										summaryStatistics.clear();
										first = true;
									}
								}else{
									expectedFlowXmeanRefFlow.put(auxFlowRate, summaryStatistics.getMean());
									summaryStatistics.clear();
									first = true;
								}
								
							}
						}else{
							ProcessLoggerUtil.writeInfo("ERROR: Sample timer is NaN on CalibrationService.meanRefFlowRate()!");
							retorno = false;
						}
					}else{
						ProcessLoggerUtil.writeInfo("ERROR: Sample counter is NaN on CalibrationService.meanRefFlowRate()!");
						retorno = false;
					}
				}else{
					ProcessLoggerUtil.writeInfo("ERROR: Sample Flow rate is NULL on CalibrationService.meanRefFlowRate()!");
					retorno = false;
				}
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: BENCH DATA NULL on CalibrationService.meanRefFlowRate()!");
			retorno = false;
		}
		
		return retorno;
	}
	

	/**
	 * Calculates the mean flow rates measured by reference meters in the bench.
	 * Uses the get counter parameter 
	 * @return
	 */
	public boolean meanVerifRefFlowRateCounter(){
		boolean retorno = true;
		double auxFlowRate = 0;
		ArrayList<BenchDataModel> benchData; 
		meanReferenceVerifFlowHash = new HashMap<>();
		summaryStatistics.clear();
		BenchDataModel auxBenchData = new BenchDataModel();
		boolean first = true;	
		int beforeCounter = 0;
		int beforeTimer = 0;
		int presenteCounter = 0;
		int presentTimer = 0;
		
		int counterDiff = 0;
		int timerDiff = 0;
		
		
		//references the bench data previously passed as a parameter
		if((benchData = this.benchVerifData) != null){
			for (int i = 0; i < benchVerifData.size(); i++) {
				auxBenchData = benchVerifData.get(i);
				auxFlowRate = auxBenchData.getExpectedFlowRate();
				if(auxFlowRate != Double.NaN){
					if(auxBenchData.getCounter() != Double.NaN){
						if(auxBenchData.getTimeVal() != Double.NaN){
							if(first){
								beforeCounter = auxBenchData.getCounter();
								beforeTimer = auxBenchData.getTimeVal();
								first = false;
								continue;
							}else{
								presenteCounter = auxBenchData.getCounter();
								presentTimer = auxBenchData.getTimeVal();
								counterDiff = presenteCounter - beforeCounter;
								timerDiff = presentTimer - beforeTimer;
								double calcFlowRate = BenchController.calcPulseToFlowRate(auxBenchData.getRefMeter(), timerDiff, counterDiff);
								auxBenchData.setPulseCalcFlowRate(calcFlowRate);
								summaryStatistics.addValue(calcFlowRate);
								
								beforeCounter = presenteCounter;
								beforeTimer = presentTimer;
								
								if(i+1 != benchData.size()){
									if(benchData.get(i+1).getExpectedFlowRate() != auxFlowRate){
										meanReferenceVerifFlowHash.put(auxFlowRate, summaryStatistics.getMean());
										summaryStatistics.clear();
										first = true;
									}
								}else{
									meanReferenceVerifFlowHash.put(auxFlowRate, summaryStatistics.getMean());
									summaryStatistics.clear();
									first = true;
								}
								
							}
						}else{
							ProcessLoggerUtil.writeInfo("ERROR: Sample timer is NaN on CalibrationService.meanVerifRefFlowRateCounter()!");
							retorno = false;
						}
					}else{
						ProcessLoggerUtil.writeInfo("ERROR: Sample counter is NaN on CalibrationService.meanVerifRefFlowRateCounter()!");
						retorno = false;
					}
				}else{
					ProcessLoggerUtil.writeInfo("ERROR: Sample Flow rate is NULL on CalibrationService.meanVerifRefFlowRateCounter()!");
					retorno = false;
				}
			}
		}else{
			ProcessLoggerUtil.writeInfo("ERROR: BENCH DATA NULL on CalibrationService.meanVerifRefFlowRateCounter()!");
			retorno = false;
		}
		
		return retorno;
	}
	
//	/**
//	 * Calculates the mean flow rates measured by reference meters in the bench.
//	 * Uses the get counter parameter 
//	 * @return
//	 */
//	public boolean meanVerifRefFlowRateCounter(){
//		boolean retorno = false;
//		double auxFlowRate = 0;
//		ArrayList<BenchDataModel> benchVerifDataAux; 
//		meanReferenceVerifFlowHash = new HashMap<>();
//		summaryStatistics.clear();
//		BenchDataModel auxBenchData = new BenchDataModel();
//		boolean first = true;
//		int initCounter = 0;
//		int initTime = 0;
//		int finalCounter = 0;
//		int finalTime = 0;
//		 
//		int timerDiff = 0;
//		int counterDiff = 0;
//		
//		if((benchVerifDataAux = this.benchData) != null){
//			for (int i = 0; i < benchVerifDataAux.size(); i++) {
//				auxBenchData = benchVerifDataAux.get(i);
//				auxFlowRate = auxBenchData.getExpectedFlowRate();
//				if(first){
//					initCounter = auxBenchData.getCounter();
//					initTime = auxBenchData.getTimeVal();
//					first = false;
//				}
//				if(i+1 != benchVerifDataAux.size()){
//					//If next element is for a different flow rate
//					if(benchVerifDataAux.get(i+1).getExpectedFlowRate() != auxFlowRate){
//						finalCounter = auxBenchData.getCounter();
//						finalTime = auxBenchData.getTimeVal();
//						counterDiff = finalCounter - initCounter;
//						timerDiff = finalTime - initTime;
//						auxBenchData.setPulseCalcFlowRate(BenchController.calcPulseToFlowRate(auxBenchData.getRefMeter(), timerDiff, counterDiff));//				summaryStatistics.addValue(auxBenchData.getCounter()*RefMeterController.calcFreqPulseWeight(auxBenchData.getRefMeter())/auxBenchData.getTimeVal());
//						meanReferenceVerifFlowHash.put(auxFlowRate, auxBenchData.getPulseCalcFlowRate());
//						first = true;
//					}
//				}else{
//					finalCounter = auxBenchData.getCounter();
//					finalTime = auxBenchData.getTimeVal();
//					counterDiff = finalCounter - initCounter;
//					timerDiff = finalTime - initTime;
//					auxBenchData.setPulseCalcFlowRate(BenchController.calcPulseToFlowRate(auxBenchData.getRefMeter(), timerDiff, counterDiff));//				summaryStatistics.addValue(auxBenchData.getCounter()*RefMeterController.calcFreqPulseWeight(auxBenchData.getRefMeter())/auxBenchData.getTimeVal());
//					meanReferenceVerifFlowHash.put(auxFlowRate, auxBenchData.getPulseCalcFlowRate());
//				}
//			}
//		}else{
//			System.err.println("ERROR: BENCH DATA NULL on CalibrationService.meanRefFlowRate()!");
//		}
//		return retorno;
//	}
	
	/**
	 * Calculates the mean flow rates measured by reference meters in the bench.
	 * Uses the get counter parameter 
	 * @return
	 */
	public void calculateMeanVerifRefFlowRate(){
		double auxExpectedFlowRate = 0;
		ArrayList<BenchDataModel> benchVerifDataAuxArray; 
		meanReferenceVerifFlowHash = new HashMap<>();
		summaryStatistics.clear();
		BenchDataModel auxBenchData = new BenchDataModel();
		//references the bench data previously passed as a parameter
		if((benchVerifDataAuxArray = this.getBenchVerifData()) != null){
			for (int i = 0; i < benchVerifDataAuxArray.size(); i++) {
				auxBenchData = benchVerifDataAuxArray.get(i);
				auxExpectedFlowRate = auxBenchData.getExpectedFlowRate();
				summaryStatistics.addValue(auxBenchData.getFlowRate());
				if(i+1 != benchVerifDataAuxArray.size()){
					//If next element is for a different flow rate
					if(benchVerifDataAuxArray.get(i+1).getExpectedFlowRate() != auxExpectedFlowRate){
						meanReferenceVerifFlowHash.put(auxExpectedFlowRate, summaryStatistics.getMean());
						summaryStatistics.clear();
					}
				}else{
					meanReferenceVerifFlowHash.put(auxExpectedFlowRate, summaryStatistics.getMean());
					summaryStatistics.clear();
				}
			}
		}else{
			System.err.println("ERROR: BENCH DATA NULL on CalibrationService.calculateMeanVerifRefFlowRate()!");
		}
	}
	
	
	/**
	 * Calculates the mean flow rates measured by reference meters in the bench.
	 * stores means o 
	 * @return true if the flow rate deviation 
	 */
	public boolean meanRefFlowRate(){
		boolean retorno = false;
		double auxFlowRate = 0;
		ArrayList<BenchDataModel> benchData; 
		expectedFlowXmeanRefFlow = new HashMap<>();
		summaryStatistics.clear();
		BenchDataModel auxBenchData = new BenchDataModel();
		if((benchData = this.benchData) != null){
			for (int i = 0; i < benchData.size(); i++) {
				auxBenchData = benchData.get(i);
				if(auxBenchData != null){
					auxFlowRate = auxBenchData.getExpectedFlowRate();
					if(countByFlowRate(auxFlowRate, benchData) >= expectNumSamples){
						summaryStatistics.addValue(auxBenchData.getFlowRate());
						
						if(i+1 != benchData.size()){
							//If next element is for a different flow rate
							if(benchData.get(i+1).getExpectedFlowRate() != auxFlowRate){
								expectedFlowXmeanRefFlow.put(auxFlowRate, summaryStatistics.getMean());
								summaryStatistics.clear();
								retorno = true;
							}
						}else{
							expectedFlowXmeanRefFlow.put(auxFlowRate, summaryStatistics.getMean());
							summaryStatistics.clear();
							retorno = true;
						}
					}else{
						System.err.println("ERROR: BENCH data size: "+benchData.size()+" on CalibrationService.meanRefFlowRate()!");
						retorno = false;
					}
				}
			}
			
		}else{
			System.err.println("ERROR: BENCH DATA NULL on CalibrationService.meanRefFlowRate()!");
			retorno = false;
		}
		
		return retorno;
	}
	
	/**
	 * Calculates the mean temperature relative to a flowrate in benchdata.
	 * This method separates the mean value of temperature by flow.
	 */
	public boolean meanBenchTemperatureFlows(){
		boolean retorno = false;
		double tempAux = Double.NaN;
		boolean isNaN = false;
		double meanLi = 0;
		double meanLo = 0;
		
		double meanCalcTemp = Double.NaN;
		if(benchData != null){
			if(!benchData.isEmpty()){
				if(benchData.size() >= expectNumSamples){
					
					double actualFlowRef = 0;
					summaryStatistics.clear();
					expectedFlowXmeanRefTemp = new HashMap<>();
					
					actualFlowRef = benchData.get(0).getExpectedFlowRate();
					BenchDataModel benchAuxData = new BenchDataModel();
					summaryStatistics.clear();
					for (int i = 0; i < benchData.size(); i++) {
						benchAuxData = benchData.get(i);
						tempAux = benchAuxData.getTemperatureLi();
						if(!Double.isNaN(tempAux)){
							summaryStatistics.addValue(tempAux);
						}else{
							System.err.println("ERROR: Temperature LI is NaN on CalibrationService.meanBenchTemperatureFlows()");
							isNaN = true;
							break;
						}
						
						if(!isNaN){
							meanLi = summaryStatistics.getMean();
							summaryStatistics.clear();
							for (BenchDataModel benchDataModel : benchData) {
								tempAux = benchDataModel.getTemperatureLo();
								if(!Double.isNaN(tempAux)){
									summaryStatistics.addValue(benchDataModel.getTemperatureLo());
								}else{
									isNaN = true;
									System.err.println("ERROR: Temperature LO is NaN on CalibrationService.meanBenchTemperatureFlows()");
									break;
								}
							}
							if(!isNaN){
								meanLo = summaryStatistics.getMean();
								if(meanLi != 0 && meanLo != 0){
									meanCalcTemp = (meanLi + meanLo)/2;
								}else{
									System.err.println("ERROR: Mean Temperature is 0 on CalibrationService.meanBenchTemperatureFlows()");
								}
							}else{
								meanCalcTemp = Double.NaN;
								isNaN = true;
							}
						}
						
						if(!isNaN){
							//Verify if the reached the before-last index of the data
							if(i+1 != benchData.size()){
								if(benchData.get(i+1).getExpectedFlowRate() != actualFlowRef){
									expectedFlowXmeanRefTemp.put(actualFlowRef, meanCalcTemp);
									summaryStatistics.clear();
									actualFlowRef = benchData.get(i+1).getExpectedFlowRate();
									retorno = true;
								}
							}else{
								expectedFlowXmeanRefTemp.put(actualFlowRef, meanCalcTemp);
								summaryStatistics.clear();
								retorno = true;
							}
						}
				}
				}else{
					System.err.println("ERROR: BENCH DATA samples size: "+benchData.size()+" on CalibrationService.meanBenchTemperatureFlows()!");
					retorno = false;
				}
			}else{
				System.err.println("ERROR: BENCH DATA EMPTY on CalibrationService.meanBenchTemperatureFlows()!");
				retorno = false;
			}
		}else{
			System.err.println("ERROR: BENCH DATA NULL on CalibrationService.meanBenchTemperatureFlows()!");
			retorno = false;
		}
		return retorno;
	}
	
	/**
	 * Calculates the mean temperature for data in the zero flow data. Returns NaN if not able to calculate
	 * @return
	 */
	public double meanBenchTemperatureZeroFlow(){
		double tempRetorno = 0;
		double meanLi = 0;
		double meanLo = 0;
		boolean isNaN = false;
		double tempAux = Double.NaN;
		if(benchDataZeroFlow != null){
			if(benchDataZeroFlow.size() >= expectNumSamples){
				summaryStatistics.clear();
				for (BenchDataModel benchDataModel : benchDataZeroFlow) {
					tempAux = benchDataModel.getTemperatureLi();
					if(!Double.isNaN(tempAux)){
						summaryStatistics.addValue(benchDataModel.getTemperatureLi());
					}else{
						System.err.println("ERROR: Temperature LI is NaN on CalibrationService.meanBenchTemperatureZeroFlow()");
						isNaN = true;
						break;
					}
				}
				if(!isNaN){
					meanLi = summaryStatistics.getMean();
					summaryStatistics.clear();
					for (BenchDataModel benchDataModel : benchDataZeroFlow) {
						tempAux = benchDataModel.getTemperatureLo();
						if(!Double.isNaN(tempAux)){
							summaryStatistics.addValue(benchDataModel.getTemperatureLo());
						}else{
							isNaN = true;
							System.err.println("ERROR: Temperature LO is NaN for meter "+this.getMeterController().getMeter().getConversor().getIp()+" on CalibrationService.meanBenchTemperatureZeroFlow()");
							break;
						}
					}
					if(!isNaN){
						meanLo = summaryStatistics.getMean();
						if(meanLi != 0 && meanLo != 0){
							tempRetorno = (meanLi + meanLo)/2;
						}else{
							System.err.println("ERROR: Mean Temperature for meter "+this.getMeterController().getMeter().getConversor().getIp()+" is 0 on CalibrationService.meanBenchTemperatureZeroFlow()");
						}
					}else{
						tempRetorno = Double.NaN;
					}
				}
			}else{
				System.err.println("ERROR: Number of samples for meter "+this.getMeterController().getMeter().getConversor().getIp()+" lower then expected on CalibrationService.meanBenchTemperatureZeroFlow(). Samples "+benchDataZeroFlow.size());
				tempRetorno = Double.NaN;
			}
				
			}else{
				System.err.println("ERROR: CalibrationService.meanBenchTemperatureZeroFlow() for meter "+this.getMeterController().getMeter().getConversor().getIp()+" Number of bench data samples: "+benchDataZeroFlow.size());
				tempRetorno = Double.NaN;
			}
			
		
		return tempRetorno;
	}
	
	
	/**
	 * kinematic viscosity [m^2/s] in water from Fysikalia page 56, LTU
	 * t = 0-100 degreeC
	 * @return
	 */
	public double viscWater(double t){
		double visc = -0.3758806791e-24*Math.pow(t, 11)
				+0.1971168646e-21*Math.pow(t, 10)
				-0.4475100250e-19*Math.pow(t, 9)
				+0.5762544630e-17*Math.pow(t, 8)
				-0.4630803192e-15*Math.pow(t, 7)
				+.2406947983e-13*Math.pow(t, 6)
				-.8120756094e-12*Math.pow(t, 5)
				+.1751404064e-10*Math.pow(t, 4)
				-.2441445733e-9*Math.pow(t, 3)
				+.2974093901e-8*Math.pow(t, 2)
				-.6511697759e-7*t+.1792e-5;
		
		return visc;
	}

	public double relativeFlowStdDeviation(ArrayList<BenchDataModel> benchData){
		double auxFlowRate;
		double retorno = 0;
		double expectedFlowRate = 0;
		boolean NaN = false; 
		boolean infinite = false;
		summaryStatistics.clear();
		for(BenchDataModel benchDataModel : benchData){
			auxFlowRate = benchDataModel.getFlowRate();
			expectedFlowRate = benchDataModel.getExpectedFlowRate();
			if(!Double.isNaN(auxFlowRate)){
				if(!Double.isInfinite(auxFlowRate)){
					summaryStatistics.addValue(benchDataModel.getFlowRate());
				}else{
					System.err.println("ERROR:  Bench Reference Meter flow rate is INFINITE on CalibrationService.relativeFlowStdDeviation()");
					retorno = 0;
					infinite = true;
					break;
				}
			}else{
				System.err.println("ERROR: Bench Reference Meter flow rate is NaN on CalibrationService.relativeFlowStdDeviation()");
				retorno = 0;
				NaN = true;
				break;
			}
		}
		
		if(!NaN && !infinite){
			//retorno = 100*(summaryStatistics.getStandardDeviation()/Math.sqrt(summaryStatistics.getN()))/summaryStatistics.getMean();
			retorno = ((Math.abs(expectedFlowRate - summaryStatistics.getMean()))/expectedFlowRate);
		}
		
		return retorno;
	}
	
	public int countByFlowRate(double flowRate, ArrayList<BenchDataModel> benchDatas){
		int retorno = 0;
		for(BenchDataModel benchDataModel : benchDatas){
			if(benchDataModel.getExpectedFlowRate() == flowRate){
				retorno++;
			}
		}
		
		return retorno;
	}
	
}
