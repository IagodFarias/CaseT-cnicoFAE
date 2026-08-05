//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file FlowRateBean.java
*    @author Marcos
*    @date 17 de jun de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.util.concurrent.CompletableFuture;

import bciapi.impl.BenchControlImpl;
import si.dbcomm.enumerations.FlowRateStateEnum;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.RefMeterModel;
import view.bean.BenchBean;

/**
 * @author Marcos
 *
 */
public class FlowRateController extends ActionParent  {

	private FlowRateModel flowRate;
	
	private RefMeterController refMeterController;
	
	private int[] counter = new int[2];
	
	private double lowerLimitPercentage = 0.99;
	
	private double upperLimitPercentage = 1.01;
	
	static double lperPulseDn02;

	static double lperPulseDn08;
	
	static double lperPulseDn32;
	/**
	 * Creates a object for class FlowRateBean.java
	 */
	public FlowRateController(FlowRateModel flowRate) {
		this.flowRate = flowRate;
		this.refMeterController = new RefMeterController();
		lperPulseDn02 = refMeterController.getPulsePerLiterDn02();
		lperPulseDn08 = refMeterController.getPulsePerLiterDn08();
		lperPulseDn32 = refMeterController.getPulsePerLiterDn32();
	}
	
	public FlowRateController(){
		this.flowRate = new FlowRateModel();
		this.refMeterController = new RefMeterController();
	}
	
	/**
	 * Executes the setted flow rate
	 * flow rate must be set using setFlowRate()
	 */
	public void executeAction(){
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		flowRate = bcimpl.setFlowRate(flowRate);
	}
	
	/**
	 * Function returns the value of attribute flowRate
	 * @return the flowRate
	 */
	public FlowRateModel getFlowRate() {
		return flowRate;
	}

	/**
	 * Function sets the value for attribute flowRate
	 * @param flowRate the flowRate to set
	 */
	public void setFlowRate(FlowRateModel flowRate) {
		this.flowRate = flowRate;
	}
	
	
	
	/**
	 * Function returns the value of attribute counter
	 * @return the counter
	 */
	public int[] getCounter() {
		return counter;
	}

	/**
	 * Function sets the value for attribute counter
	 * @param counter the counter to set
	 */
	public void setCounter(int[] counter) {
		this.counter = counter;
	}

	/**
	 * Sets a specified flow rate to run in the determined tolerances in the FlowRateModel object.
	 * This also checks if the flow path of the bench is obstructed by any valves. 
	 * It does not change any valve state. In case flow path is obstructed, the flow does not run. 
	 * In case Gravity is used the valve (XV1) that allows the flow to run to the line must be opened
	 * @param flowRate
	 * @return true if the flow rate is stable and running in the determined tolerances
	 */
	public boolean runFlowRate(FlowRateModel flowRate){
		boolean retorno = false;
		if(flowRate!=null){
			if(!Double.isNaN(flowRate.getFlowRate())){
				ValveController valveController = new ValveController();
				if(!valveController.isFlowPathBlocked()){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					flowRate = bcimpl.setFlowRate(flowRate);
					if(flowRate.getState() == FlowRateStateEnum.STABLE){
						retorno = true;
					}else{
						retorno = false;
					}
				}else{
					System.err.println("ERROR: Bench Flow Path is blocked FlowRateController.runFlowRate()");
				}
			}
		}
		return retorno;
	}
	
	
	public boolean startCounter(RefMeterModel refMeter){
		boolean retorno = false;
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		if(bcimpl.startCounter(refMeter)){
//			System.out.println("Counter started");
			retorno = true;
		}
		return retorno;
	}
	
	public boolean stopCounter(RefMeterModel refMeter){
		boolean retorno = false;
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		if(bcimpl.stopCounter(refMeter)){
			retorno = true;
		}
		return retorno;
	}
	
	
	public boolean assertStableFlow(RefMeterModel refMeter){
		boolean retorno = false;
		if(refMeterController.readRefMeter(refMeter).getFlowStability().equals(FlowRateStateEnum.STABLE)){
			retorno = true;
		}else{
			retorno = false;
		}
		return retorno;
	}
	
	//TODO method needs to be better develop
	public boolean assertStableFlow(long checkTime, FlowRateModel flowRate){
		RefMeterModel refMeterAux;
		boolean retorno = false;
//		AssyncronousTimerUtil asyncTimer = new AssyncronousTimerUtil(checkTime, "Assert Stable Flow Rate - Thread");//TImer to count for timeout
		
		
		refMeterAux = refMeterController.readRefMeter(flowRate.getRefMeter().getTag());
		if(flowRate.getLowerLimit() == 0){
			flowRate.setLowerLimit(flowRate.getFlowRate()*lowerLimitPercentage);
		}
		if(flowRate.getUpperLimit() == 0){
			flowRate.setUpperLimit(flowRate.getFlowRate()*upperLimitPercentage);
			
		}
		CompletableFuture<?> cpf = CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(checkTime);
			} catch (InterruptedException e) {
				System.err.println("ERROR: InterruptedException of assync thread BenchController.assertStableFlow()");
			}
		});//thenAccept();
		
		while(!cpf.isDone()){
			refMeterAux = refMeterController.readRefMeter(flowRate.getRefMeter().getTag());
			if(refMeterAux.getFlowRate() > flowRate.getLowerLimit() && refMeterAux.getFlowRate() < flowRate.getUpperLimit()){
				retorno = true;
			}else{
				retorno = false;
			}
		}
		cpf.cancel(true);
		return retorno;
		
	}
	
	public double calcFlowRateFromCounter(String tag){
		BenchBean benchBeanView = BenchBean.getInstance();
		double retorno = Double.NaN;
		if(counter != null){
			if(counter[1] != 0){
				if(counter[0] != 0){
					if(tag == RefMeterController.METER_TAG_DN02){
						retorno = calcPulseToFlowRateDn02(this.counter[1], this.counter[0]);
//						benchBeanView.getRefMeterDN02().setFlowRate(retorno);
					}else{
						if(tag == RefMeterController.METER_TAG_DN08){
							retorno = calcPulseToFlowRateDn08(this.counter[1], this.counter[0]);
//							benchBeanView.getRefMeterDN08().setFlowRate(retorno);
						}else{
							if(tag == RefMeterController.METER_TAG_DN32){
								retorno = calcPulseToFlowRateDn32(this.counter[1], this.counter[0]);
//								benchBeanView.getRefMeterDN32().setFlowRate(retorno);
							}
						}
					}
				}
			}
		}
		return retorno;
	}
	
	
	public static double calcPulseToFlowRateDn02(int timeVal, int pulseCount){
		return calcPulseToFlowRate(timeVal, pulseCount, lperPulseDn02);
	}
	
	public static double calcPulseToFlowRateDn08(int timeVal, int pulseCount){
		return calcPulseToFlowRate(timeVal, pulseCount, lperPulseDn08);
	}
	
	public static double calcPulseToFlowRateDn32(int timeVal, int pulseCount){
		return calcPulseToFlowRate(timeVal, pulseCount, lperPulseDn32);
	}
	
	private static double calcPulseToFlowRate(int timeVal, int pulseCount, double pulsePerLiter){
		double timeValAux = 0.0;
		double volume = pulseCount*pulsePerLiter;
		timeValAux = timeVal/3.6;
		double flowRate = volume/timeValAux;
		flowRate = flowRate*1000; // to L/h
		return flowRate;
	}
	
	public int[] getCounterFromBci(RefMeterModel refMeter){
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		counter = bcimpl.getCounter(refMeter);
		if(counter[0] == 0){
//			System.err.println("WARNING: Counter is 0 for RefMeter "+refMeter.getTag());
		}
		if(counter[1] == 0){
//			System.err.println("WARNING: Timer is 0 for RefMeter "+refMeter.getTag());
		}
		return counter;
	}
}
