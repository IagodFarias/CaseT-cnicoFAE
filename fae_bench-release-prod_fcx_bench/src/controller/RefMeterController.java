//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RefMeterBean.java
*    @author Marcos
*    @date 17 de jun de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bciapi.impl.BenchControlImpl;
import si.dbcomm.enumerations.FlowRateStateEnum;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.service.RefMeterService;
import util.DeviceTagReaderUtil;
import view.bean.BenchBean;
import si.dbcomm.exceptions.CrudDatabaseException;

/**
 * @author Marcos
 *
 */
public class RefMeterController extends ActionParent {
	
	private RefMeterModel refMeter;
	
	private RefMeterService refMeterService;
	
	private ValveController valveController = new ValveController();
	

	private List<RefMeterModel> refMeters;
	
	static double freqPulseWeight = 0;

	public static final String METER_TAG_DN32 = DeviceTagReaderUtil.getProperty("meter.reference.dn32");
	
	public static final String METER_TAG_DN08 = DeviceTagReaderUtil.getProperty("meter.reference.dn08");
	
	public static final String METER_TAG_DN02 = DeviceTagReaderUtil.getProperty("meter.reference.dn02");
	
	/**
	 * 
	 * Creates a object for class RefMeterController.java
	 */
	public RefMeterController(){
		refMeter = new RefMeterModel();
		refMeterService = new RefMeterService();
		refMeters = new ArrayList<>();
		try {
			refMeters = refMeterService.findAll();
		} catch (CrudDatabaseException e) {
			// Initialize with empty list to prevent NPE
			refMeters = new ArrayList<>();
			System.err.println("WARNING: Could not load reference meters from database: " + e.getMessage());
			// Log or notify about database connection issue
			// Optionally: e.printStackTrace();
		}
	}
	
	/**
	 * Function returns the value of attribute refMeter
	 * @return the refMeter
	 */
	public RefMeterModel getRefMeter() {
		return refMeter;
	}

	/**
	 * Function sets the value for attribute refMeter
	 * @param refMeter the refMeter to set
	 */
	public void setRefMeter(RefMeterModel refMeter) {
		this.refMeter = refMeter;
	}
	
	/**
	 * Read the reference meter specified in parameter
	 * @param refMeter
	 * @return reference meter with the values that have been read
	 */
	public RefMeterModel readRefMeter(RefMeterModel refMeter){
		if(refMeter != null){
			if(refMeter.getTag() != null){
				if(!refMeter.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					refMeter = bcimpl.readRefMeter(refMeter);
					updateBenchView(refMeter);
				}
			}
		}
		
		return refMeter;
	}
	
	/**
	 * Read the reference meter specified in parameter
	 * @param refMeter
	 * @return reference meter with the values that have been read
	 */
	public RefMeterModel readRefMeter(String tag){
		RefMeterModel refMeter = new RefMeterModel();
		if(tag != null){
			refMeter.setTag(tag);
			BenchControlImpl bcimpl = BenchControlImpl.getInstance();
			refMeter = bcimpl.readRefMeter(refMeter);
			this.refMeter = refMeter;
			updateBenchView(refMeter);
		}
		return refMeter;
	
	}
	
	/**
	 * Read the reference meter seted in the object
	 * @param refMeter
	 * @return reference meter with the values that have been read
	 */
	public RefMeterModel readRefMeter(){
		if(refMeter != null){
			if(refMeter.getTag() != null){
				if(!refMeter.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					refMeter = bcimpl.readRefMeter(refMeter);
					updateBenchView(refMeter);
				}
			}
		}
		return refMeter;
	}
	
	/**
	 * Read the reference meter seted in the object
	 * @param refMeter
	 * @return reference meter with the values that have been read
	 */
	public List<RefMeterModel> readAllRefMeter(){
		if(refMeters.isEmpty()){
			refMeters = refMeterService.findAll();
		}
		
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		for(RefMeterModel refMeter : refMeters){		
			refMeter = bcimpl.readRefMeter(refMeter);
			updateBenchView(refMeter);
		}
		return refMeters;
	}
	
	
	/**
	 * This method read a reference meter by the tag name and sets on the controller the object 
	 * persisted in DB. 
	 */
	public void findAndSetRefMeterByTag(String tag){
		this.refMeter = refMeterService.findByTag(tag);
	}
	
	/**
	 * this method determines the reference meter that should be read according to the flowrate passed as parameter.
	 * @return RefMeterModel with the reference meter that should run this flowRate
	 */
	public RefMeterModel defineRefMeterByFlowRate(FlowRateModel flowRate){
		this.refMeters = refMeterService.findAll();
		RefMeterModel refMeterRet = null;
		for(RefMeterModel refMeter : refMeters){
			if(flowRate.getFlowRate() < refMeter.getMaxFlowRate() && flowRate.getFlowRate() >= refMeter.getMinFlowRate()){
				refMeterRet = refMeter;
				break;
			}
		}
		return refMeterRet;
	}
	
	
	public boolean closeRefMeterValve(String refMeterTag){
		boolean retorno = false;
		refMeter = refMeterService.findByTag(refMeterTag);
		if(refMeter != null){
			if(valveController.closeValveByTag(refMeter.getValve().getTag())){
				retorno = true;
			}
		}
		return retorno;
		
	}
	
	
	/**
	 * 
	 * @param refMeterTag - String Containing the tag relative to the reference meter 
	 * @return true case valve is successfully closed
	 */
	public boolean openRefMeterValve(String refMeterTag){
		boolean retorno = false;
		refMeter = refMeterService.findByTag(refMeterTag);
		if(refMeter != null){
			if(valveController.openValveByTag(refMeter.getValve().getTag())){
				retorno = true;
			}
		}
		return retorno;
	}
	
	/**
	 * Opens valve relative to the explicit reference meter 
	 * @param refMeter - Object RefMeterModel relative to the meter to open valve 
	 * @return true case valve is succesfully opened
	 */
	public boolean openRefMeterValve(RefMeterModel refMeter){
		boolean retorno = false;
		if(refMeter != null){
			if(valveController.openValveByTag(refMeter.getValve().getTag())){
				retorno = true;
			}

		}
		return retorno;
	}
	
	/**
	 * Closes the reference meter valves except the one designated in parameter
	 * 
	 * @return true for a sucessfull operation
	 */
	public boolean closeRefMeterValvesExcept(String meterTag){
		boolean retorno = false;
		if(meterTag.equals(METER_TAG_DN02)){
			if(this.closeRefMeterValve(METER_TAG_DN08)&&
					closeRefMeterValve(METER_TAG_DN32)){
				retorno = true;
			}
		}else{
			if(meterTag.equals(METER_TAG_DN08)){
				if(closeRefMeterValve(METER_TAG_DN02)&&
						closeRefMeterValve(METER_TAG_DN32)){
					retorno = true;
				}
			}else{
				if(meterTag.equals(METER_TAG_DN32)){
					if(closeRefMeterValve(METER_TAG_DN02)&&
							closeRefMeterValve(METER_TAG_DN08)){
						retorno = true;
					}
				}
			}
		}
		return retorno;
	}
	
	/**
	 * Closes all valves on reference meters Upstream
	 * @return
	 */
	public boolean closeAllRefMeterValves(){
		boolean retorno = true;
		if(!closeRefMeterValve(RefMeterController.METER_TAG_DN02)){
			retorno = false;
		}
		if(!closeRefMeterValve(RefMeterController.METER_TAG_DN08)){
			retorno = false;
		}
		if(!closeRefMeterValve(RefMeterController.METER_TAG_DN32)){
			retorno = false;
		}
		return retorno;
	}
	
	/**
	 * Opens all valves on reference meters Upstream
	 * @return
	 */
	public boolean openAllRefMeterValves(){
		boolean retorno = true;
		if(!openRefMeterValve(RefMeterController.METER_TAG_DN02)){
			retorno = false;
		}
		if(!openRefMeterValve(RefMeterController.METER_TAG_DN08)){
			retorno = false;
		}
		if(!openRefMeterValve(RefMeterController.METER_TAG_DN32)){
			retorno = false;
		}
		return retorno;
	}
	
	/**
	 * 
	 * @param refMeter
	 * @return
	 */
	public static double calcFreqPulseWeight(RefMeterModel refMeter){
		//first with convert the max flowrate from L/h to L/second by divinding the flowrate by 3600.
		// then we divide the L/s and divided it by the corresponding setted frequency: FreqMaxFlowRate
		// ex.: for a meter with maxFlowRate of 100 L/h and max Frequency of 8000Hz
		// 100/3600 = 0.027 L/s
		// then we divide it by the freq:
		// 0.027/8000 = 3,4722222222222222222222222222222e-6 mL/pulse
			RefMeterModel auxRefMeter = refMeter;
			if(refMeter.getMaxFlowRate() == 0 || refMeter.getMaxFlowRate() == Double.NaN || refMeter.getFreqMaxFlowRate() == Double.NaN){
				RefMeterService refMeterService = new RefMeterService();
				auxRefMeter = refMeterService.findByTag(refMeter.getTag());
			}
			double lpersec = auxRefMeter.getMaxFlowRate()/3.6;
			double lperPulse = lpersec/auxRefMeter.getFreqMaxFlowRate();
		
		return lperPulse;
	}
	

	private void updateBenchView(RefMeterModel refMeter){
		BenchBean benchBeanView = BenchBean.getInstance();
		
		if(refMeter != null){
			if(refMeter.getFlowStability() == null){
				refMeter.setFlowStability(FlowRateStateEnum.UNKNOWN);
			}
			if(METER_TAG_DN32 == refMeter.getTag()){
				benchBeanView.getRefMeterDN32().setFlowRate(refMeter.getFlowRate());
				benchBeanView.getRefMeterDN32().setVolume(refMeter.getVolume());
				benchBeanView.getRefMeterDN32().setPulses(refMeter.getPulses());
				benchBeanView.getRefMeterDN32().setStability(FlowRateStateEnum.toString(refMeter.getFlowStability()));
				benchBeanView.getRefMeterDN32().setTimeRead(refMeter.getTimeread());
			}else{
				if(METER_TAG_DN02 == refMeter.getTag()){
					benchBeanView.getRefMeterDN02().setFlowRate(refMeter.getFlowRate());
					benchBeanView.getRefMeterDN02().setVolume(refMeter.getVolume());
					benchBeanView.getRefMeterDN02().setPulses(refMeter.getPulses());
					benchBeanView.getRefMeterDN02().setStability(FlowRateStateEnum.toString(refMeter.getFlowStability()));
					benchBeanView.getRefMeterDN02().setTimeRead(refMeter.getTimeread());
				}else{
					if(METER_TAG_DN08 == refMeter.getTag()){
						benchBeanView.getRefMeterDN08().setFlowRate(refMeter.getFlowRate());
						benchBeanView.getRefMeterDN08().setVolume(refMeter.getVolume());
						benchBeanView.getRefMeterDN08().setPulses(refMeter.getPulses());
						benchBeanView.getRefMeterDN08().setStability(FlowRateStateEnum.toString(refMeter.getFlowStability()));
						benchBeanView.getRefMeterDN08().setTimeRead(refMeter.getTimeread());
					}
				}
			}
		}
	}
	
	public double getPulsePerLiter(RefMeterModel refMeter){
		double retorno = Double.NaN;
		retorno = RefMeterController.calcFreqPulseWeight(refMeter);
		return retorno;
	}
	
	public double getPulsePerLiterDn02(){
		double retorno = Double.NaN;
		RefMeterModel refMeter = refMeterService.findByTag(METER_TAG_DN02);
		retorno = getPulsePerLiter(refMeter);
		return retorno;
	}
	
	public double getPulsePerLiterDn08(){
		double retorno = Double.NaN;
		RefMeterModel refMeter = refMeterService.findByTag(METER_TAG_DN08);
		retorno = getPulsePerLiter(refMeter);
		return retorno;
	}
	
	public double getPulsePerLiterDn32(){
		double retorno = Double.NaN;
		RefMeterModel refMeter = refMeterService.findByTag(METER_TAG_DN32);
		retorno = getPulsePerLiter(refMeter);
		return retorno;
	}
	
	public boolean setEnableBuffer(int windowSize, int bufferSize, boolean state){
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		boolean retorno = false;
		retorno = bcimpl.setEnableBuffer(state, windowSize, bufferSize);
		return retorno;
	}
	
	/* (non-Javadoc)
	 * @see interfaces.ActionInterface#executeAction()
	 */
	@Override
	public void executeAction() {
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		refMeter = bcimpl.readRefMeter(refMeter);
	}
}
