//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PumpBean.java
*    @author Marcos
*    @date 17 de jun de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import bciapi.impl.BenchControlImpl;
import si.dbcomm.enumerations.PumpStateEnum;
import si.dbcomm.model.PidConfigModel;
import si.dbcomm.model.PumpModel;
import si.dbcomm.service.PumpService;
import util.DeviceTagReaderUtil;

/**
 * @author Marcos
 *
 */
public class PumpController extends ActionParent{
	
	private PumpModel pump;
	
	private List<PumpModel> pumps;
	
	private PumpService pumpService;
	
	private HashMap<String, PumpModel> pumpsMap;
	
	private WaterLineController lineController;
	
	private ValveController valveController;
	
	public static final String BP1_TAG = DeviceTagReaderUtil.getProperty("pump.bp1.tag"); //Pressure pump
	public static final String BP2_TAG = DeviceTagReaderUtil.getProperty("pump.bp2.tag"); //flow pump
	public static final String BREP_TAG = DeviceTagReaderUtil.getProperty("pump.brep.tag"); // reposition pump
	
	/**
	 * Creates a object for class PumpBean.java
	 */
	public PumpController(PumpModel pump) {
		this.pump = pump;
		this.pumpService = new PumpService();
		this.pumps = new ArrayList<>();
		lineController = new WaterLineController();
		valveController = new ValveController();
	}
	
	/**
	 * Creates a object for class PumpBean.java
	 */
	public PumpController() {
		this.pump = new PumpModel();
		this.pumpService = new PumpService();
		this.pumps = new ArrayList<>();
		lineController = new WaterLineController();
		valveController = new ValveController();
	}

	
	/**
	 * Function returns the value of attribute pump
	 * @return the pump
	 */
	public PumpModel getPump() {
		return pump;
	}


	/**
	 * Function sets the value for attribute pump
	 * @param pump the pump to set
	 */
	public void setPump(PumpModel pump) {
		this.pump = pump;
	}


	/**
	 * Function returns the value of attribute pumpService
	 * @return the pumpService
	 */
	public PumpService getPumpService() {
		return pumpService;
	}


	/**
	 * Function sets the value for attribute pumpService
	 * @param pumpService the pumpService to set
	 */
	public void setPumpService(PumpService pumpService) {
		this.pumpService = pumpService;
	}

	
	
	/**
	 * Function returns the value of attribute pumps
	 * @return the pumps
	 */
	public List<PumpModel> getPumps() {
		return pumps;
	}


	/**
	 * Function sets the value for attribute pumps
	 * @param pumps the pumps to set
	 */
	public void setPumps(List<PumpModel> pumps) {
		this.pumps = pumps;
	}


	
	/**
	 * Function returns the value of attribute pumpsMap
	 * @return the pumpsMap
	 */
	public HashMap<String, PumpModel> getPumpsMap() {
		return pumpsMap;
	}

	/**
	 * Function sets the value for attribute pumpsMap
	 * @param pumpsMap the pumpsMap to set
	 */
	public void setPumpsMap(HashMap<String, PumpModel> pumpsMap) {
		this.pumpsMap = pumpsMap;
	}

	/**
	 * This function sends to the bench the setted attribute Load
	 * It is to be called after setting the desired load with setLoad()
	 * After setting the load, it updates the values reading the BCI
	 * In case load is not setted, the BCI returns pumpLoad = -1 and
	 * state = UNKNOWN 
	 * 
	 */
	public void executeAction(){
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		pump = bcimpl.setPumpLoad(pump);
	}
	
	/**
	 * Reads the pump
	 */
	public void readData() {
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		//pump = bcimpl.getPumpLoad(pump); removed due to changes on the API. Pum state resolves this
		pump = bcimpl.getPumpState(pump);
		
	}
	
	/**
	 * Reads the pump state 
	 * @param tag - name tag of the pump 
	 * @return PumpModel object with the read pump
	 */
	public PumpModel readPumpState(String tag){
		PumpModel pumpRetorno = null;
		pumpRetorno = pumpService.findByTag(tag);
		if(pump != null){
			if(pump.getTag() != null){
				if(!pump.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					pumpRetorno = bcimpl.getPumpState(pumpRetorno);
					pump = pumpRetorno;
				}
			}
		}
		
		return pumpRetorno;
	}
	
	/**
	 * This method sets a pump with a given load. It does not verify if an overload has occured
	 * its purpose is to run a pump with selected load. Later checking has to be executed to assure if pump
	 * has not stopped due to overload. It does not also check the flowrate that is running
	 * @param load a double indicating the load that must be set in the pump
	 * 
	 * @return true if the pump has been started with the selected load.
	 */
	private boolean runPumpLoad(double load){
		boolean retorno = false;
		if(!Double.isNaN(load)){
			if(pump!=null){
				
				BenchControlImpl bcimpl = BenchControlImpl.getInstance();
				this.pump.setLoad(load);
				pump = bcimpl.setPumpLoad(pump);
				if(pump != null){
					PumpStateEnum pumpState = pump.getState();
					if(pumpState != PumpStateEnum.OVERLOAD){
						if(pumpState == PumpStateEnum.OFF){
							ValveController valveController = new ValveController();
							
							// if the flow is not blocked, then the pump can be started
							if(!valveController.isFlowPathBlocked()){
								if(lineController.closeLine()){
									if(bcimpl.startPump(pump)){
										retorno = true;
									}else{
										retorno = false;
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
	
	public boolean runPumpLoadHighPressure(double load, String pumpTag){
		boolean retorno = false;
		PumpModel pumpAux = pumpService.findByTag(pumpTag);
		pumpAux.setLoad(load);
		
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		if(bcimpl.setPumpLoad(pumpAux)!= null){
			if(bcimpl.startPump(pumpAux)){
				retorno = true;
			}
		}
		return retorno;
	}
	
	/**
	 * This method sets a pump with a given load. It does not verify if an overload has occured
	 * its purpose is to run a pump with selected load. Later checking has to be executed to assure if pump
	 * has not stopped due to overload. It does not also check the flowrate that is running
	 * @param load a double indicating the load that must be set in the pump
	 * 
	 * @return true if the pump has been started with the selected load.
	 */
	public boolean runPumpLoad(double load, PumpModel pump){
		boolean retorno = false;
		this.pump = pump;
		if(runPumpLoad(load)){
			retorno = true;
		}
		return retorno;
	}
	/**
	 * This method sets a pump with a given load. It does not verify if an overload has occured
	 * its purpose is to run a pump with selected load. Later checking has to be executed to assure if pump
	 * has not stopped due to overload. It does not also check the flowrate that is running
	 * @param load a double indicating the load that must be set in the pump
	 * 
	 * @return true if the pump has been started with the selected load.
	 */
	public boolean runPumpLoad(double load, String tag){
		boolean retorno = false;
		if(this.pump.getTag() != null){
			if(!this.pump.getTag().equals(tag)){
				this.pump = pumpService.findByTag(tag);
			}
		}else{
			pump.setTag(tag);
		}
			if(runPumpLoad(load)){
				retorno = true;
			}
		return retorno;
	}
	
	public boolean runBrepPump(){
		boolean retorno = false;
		this.pump = new PumpModel();
		pump.setTag(BREP_TAG);
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		if(bcimpl.startPump(pump)){
			retorno = true;
		}
//		if(runPumpLoad(10)){
//			retorno = true;
//		}
		return retorno;
	}
	
	/**
	 * Method receives a flow rate and chooses the pump that should run it
	 * @param flowRate
	 * @return the PumpModel of the selected pump
	 */
	public PumpModel definePump(double flowRate){
		pumps = pumpService.findAll();
		PumpModel retorno = null;
		for (PumpModel pump : pumps) {
			if(pump.getMinFlowRate() <= flowRate && pump.getMaxFlowRate() > flowRate  ){
				retorno = pump;
				break;
			}
		}
		return retorno;
	}
	
	private PumpModel pumpAux;
	
	public void incPumpLoad(double setPoint, double incStep, int incTime, String pumpTag){
		
		if(pumpTag != null){
			BenchControlImpl bcimpl = BenchControlImpl.getInstance();
			PumpModel pump = new PumpModel(); 
			
			if(this.pump.getTag() != pumpTag){
				pump = pumpService.findByTag(pumpTag);
			}else{
				pump = new PumpModel();
				pump.setTag(pumpTag);
			}
			pumpAux = pump;
			while(pumpAux.getLoad()!=setPoint){
				// Retrieves updated pump state
				pump = bcimpl.getPumpState(pump);
				
				if(pump.getLoad() < setPoint){
					if((pump.getLoad()+incStep) > setPoint){
						pump.setLoad(setPoint);
						bcimpl.setPumpLoad(pump);
					}else{
						CompletableFuture.runAsync(() -> {
							try {
								Thread.sleep(incTime);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}).thenAccept((timer) -> {
							pumpAux.setLoad(pumpAux.getLoad()+incStep);
						});
					}
				}
			}
		}
	}
	
	public void getAllPumpsState(){
		this.pumpsMap = new HashMap<>();
		pumpsMap.put(BP1_TAG, readPumpState(BP1_TAG));
		pumpsMap.put(BP2_TAG, readPumpState(BP2_TAG));
		pumpsMap.put(BREP_TAG, readPumpState(BREP_TAG));
	}
	
	public boolean checkIfPumpIsRunning(PumpModel pump){
		getAllPumpsState();
		boolean retorno = false;
		if((pumpsMap.get(pump.getTag())).getState() == PumpStateEnum.ON){
			retorno = true;
		}
		return retorno;
	}
	
	/**
	 * This method stops all currently running pumps except the pump determined in the parameter
	 * @param pump
	 * @return true if method has succeeded
	 */
	public boolean stopPumpsExcept(PumpModel pump) {
		PumpModel pumpAux = new PumpModel();
		boolean retorno = false;
		pumpAux.setTag(pump.getTag());
		if(pump.getTag().equals(BP1_TAG)){
			valveController.closeValveByTag(ValveController.LINE_INPUT_FROM_SUP_RESERV_TAG);
			stopPumpByTag(BP2_TAG);
			stopPumpByTag(BREP_TAG);
			retorno = true;
		}else{
			if(pump.getTag().equals(BP2_TAG)){
				valveController.closeValveByTag(ValveController.LINE_INPUT_FROM_SUP_RESERV_TAG);
				stopPumpByTag(BP1_TAG);
				stopPumpByTag(BREP_TAG);
				retorno = true;
			}else{
				if(pump.getTag().equals(BREP_TAG)){
					stopPumpByTag(BP1_TAG);
					stopPumpByTag(BP2_TAG);
					retorno = true;
				}	
			}
		}
		return retorno;
	}
	
	public boolean stopPumpByTag(String pumpTag){
		boolean retorno = false;
		BenchControlImpl bcipml = BenchControlImpl.getInstance();
		PumpModel pumpAux = new PumpModel();
		pumpAux.setTag(pumpTag);
		if(bcipml.stopPump(pumpAux)){
			retorno = true;
		}
		return retorno;
	}
	
	
	public boolean stopAllPumps(){
		boolean retorno = true;
		
		if(!stopPumpByTag(BP1_TAG)){
			retorno = false;
		}
		if(!stopPumpByTag(BP2_TAG)){
			retorno = false;
		}
		if(!stopPumpByTag(BREP_TAG)){
			retorno = false;
		}
		
		return retorno;
	}
	
	public boolean setPidParamsForPump(PumpModel pump, double proportionalKp, double integrativeTm, double derivativeTv){
		BenchControlImpl bcipml = BenchControlImpl.getInstance();
		boolean retorno = false;
		if(bcipml.setPidParameters(pump, proportionalKp, integrativeTm, derivativeTv)){
			retorno = true;
		}
		return retorno;
	}
	
	
	/**
	 * Retrieves the specified PID config parameters for the pump.
	 * @param pump
	 * @return PidConfigModel
	 */
	public PidConfigModel getPidParametersForPump(PumpModel pump){
		BenchControlImpl bcipml = BenchControlImpl.getInstance();
		PidConfigModel retorno = null;
		if(pump != null){
			retorno = bcipml.getPidParameters(pump);
		}
		
		return retorno;
	}
	
	
	
	
	
	
	
	
	
}
