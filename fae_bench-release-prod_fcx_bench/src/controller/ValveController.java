//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ValveBean.java
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

import bciapi.impl.BenchControlImpl;
import si.dbcomm.enumerations.ValveStateEnum;
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.model.ValveModel;
import si.dbcomm.service.ValveService;
import util.DeviceTagReaderUtil;
import si.dbcomm.exceptions.CrudDatabaseException;

/**
 * @author Marcos
 *
 */
public class ValveController extends ActionParent{
	
	private ValveModel valve;
	
	private ValveService valveService;
	
	private List<ValveModel> valves;
	
	private HashMap<String, ValveModel> valvesMap;
	
	public static final String LINE_INPUT_FROM_SUP_RESERV_TAG = DeviceTagReaderUtil.getProperty("valve.line.input.from.superiorres");
	
	public static final String UPSTREAM_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.line.upstream");
	
	public static final String DOWNSTREAM_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.line.downstream");
	
	public static final String AIR_PURGE_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.airpurge");
	
	public static final String PRESS_REG_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.pressurereg");
	
	public static final String FEED_INF_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.feed.to.inferiorres");
	
	public static final String DN32_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.dn32");
	
	public static final String DN08_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.dn08");
	
	public static final String DN02_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.dn02");
	
	public static final String RAMAL1_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal1");
	
	public static final String RAMAL2_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal2");
	
	public static final String RAMAL3_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal3");
	
	public static final String RAMAL4_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal4");
	
	public static final String RAMAL5_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal5");
	
	public static final String RAMAL6_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal6");
	
	public static final String RAMAL7_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal7");
	
	public static final String RAMAL8_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal8");
	
	public static final String RAMAL9_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal9");
	
	public static final String RAMAL10_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.ramal10");
	
	public static final String SCALE_DIVERSOR_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.diversor");
	
	public static final String SCALE_CONTAIN_VALVE_TAG = DeviceTagReaderUtil.getProperty("valve.scale.container");
	
//	/**
//	 * Creates a object for class ValveBean.java
//	 */
//	public ValveController(ValveModel valve) {
//		this.valve = valve;
//		this.valveService = new ValveService();
//		this.valves = new ArrayList<>();
//		this.valvesMap = new HashMap<>();
//		fillValvesMap();
//	}
	
	/**
	 * Creates a object for class ValveController.java
	 */
	public ValveController() {
		this.valve = new ValveModel();
		this.valveService = new ValveService();
		this.valves = new ArrayList<>();
		this.valvesMap = new HashMap<>();
		fillValvesMap();
	}
	
	/**
	 * Function returns the value of attribute valve
	 * @return the valve
	 */
	public ValveModel getValve() {
		return valve;
	}

	/**
	 * Function sets the value for attribute valve
	 * @param valve the valve to set
	 */
	public void setValve(ValveModel valve) {
		this.valve = valve;
	}

	/**
	 * Function returns the value of attribute valves
	 * @return the valves
	 */
	public List<ValveModel> getValves() {
		return valves;
	}

	/**
	 * Function sets the value for attribute valves
	 * @param valves the valves to set
	 */
	public void setValves(List<ValveModel> valves) {
		this.valves = valves;
	}

	/**
	 * Function returns the value of attribute valvesMap
	 * @return the valvesMap
	 */
	public HashMap<String, ValveModel> getValvesMap() {
		return valvesMap;
	}

	/**
	 * Function sets the value for attribute valvesMap
	 * @param valvesMap the valvesMap to set
	 */
	public void setValvesMap(HashMap<String, ValveModel> valvesMap) {
		this.valvesMap = valvesMap;
	}

	/* (non-Javadoc)
	 * @see model.ActionParent#executeAction()
	 */
	@Override
	public void executeAction() {
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		valve = bcimpl.getValveState(valve);
	}
	
	/**
	 * Closes the valve with tag set in parameter. Also changes the model valve in controller to valve referencing the tag;
	 * @param tag - string that represents the valve
	 * @return boolean indicating the valve has been effectively closed
	 */
	public boolean closeValveByTag(String tag){
		boolean retorno = false;
		if(tag != null) {
//			valve = valveService.findByTag(tag);
			if(valvesMap.containsKey(tag)){
				valve = valvesMap.get(tag);
				BenchControlImpl bcimpl = BenchControlImpl.getInstance();
				valve = bcimpl.closeValve(valve);
				
				if(valve != null){
					if(valve.getState() == ValveStateEnum.CLOSED){
						retorno = true;
					}else{
						retorno = false;
					}
				}else{
					System.err.println("ERROR: The selected Valve "+tag+" returned null");
				}
			}else{
				System.err.println("ERROR: The selected Valve "+tag+" was not found on valvesMap in ValvesController.closeValveByTag()");
				retorno = false;
			}
		}else{
			System.err.println("ERROR: The Valve tag was NULL in ValvesController.closeValveByTag()");
			retorno = false;
		}
		return retorno;
	}
	
	/**
	 * Opens the valve pointed by the tag. Also set the controller ValveModel to selected valve;
	 * @param tag 
	 * @return
	 */
	public boolean openValveByTag(String tag){
		boolean retorno = false;
		if(tag != null) {
//			valve = valveService.findByTag(tag);
			if(valvesMap.containsKey(tag)){
				valve = valvesMap.get(tag);
				BenchControlImpl bcimpl = BenchControlImpl.getInstance();
				valve = bcimpl.openValve(valve);
				if(valve != null){
					if(valve.getState() == ValveStateEnum.OPENED){
						retorno = true;
					}else{
						retorno = false;
					}
				}
			}else{
				System.err.println("ERROR: The selected Valve "+tag+" was not found on valvesMap in ValvesController.openValveByTag()");
				retorno = false;
			}
		}else{
			System.err.println("ERROR: The Valve tag was NULL in ValvesController.openValveByTag()");
			retorno = false;
		}
		return retorno;
	}
	
	/**
	 * Opens the line's upstreamvalve
	 * @return
	 */
	public boolean closeUpStreamValve(){
		boolean retorno = false;
		if(closeValveByTag(UPSTREAM_VALVE_TAG)){
			retorno = true;
		}else{
			retorno = false;
		}
		return retorno;
	}
	
	/**
	 * Opens the line's upstreamvalve
	 * @return
	 */
	public boolean openUpStreamValve(){
		boolean retorno = false;
		if(openValveByTag(UPSTREAM_VALVE_TAG)){
			retorno = true;
		}else{
			retorno = false;
		}
		return retorno;
	}
	
	public boolean openRefMeterValve(RefMeterModel refMeter){
		return openValve(refMeter.getValve());
	}
	
	/**
	 * Opens the line's downstreamvalve
	 * @return
	 */
	public boolean closeDownStreamValve(){
		boolean retorno = false;
		if(closeValveByTag(DOWNSTREAM_VALVE_TAG)){
			retorno = true;
		}else{
			retorno = false;
		}
		return retorno;
	}
	
	/**
	 * Opens the line's downstream valve
	 * @return
	 */
	public boolean openDownStreamValve(){
		boolean retorno = false;
		if(openValveByTag(DOWNSTREAM_VALVE_TAG)){
			retorno = true;
		}else{
			retorno = false;
		}
		return retorno;
	}
	
	/**
	 * This method opens the valve designated in parameter
	 * @param valve
	 * @return true if valve has been opened. False if operation failed
	 */
	public boolean openValve(ValveModel valve){
		boolean retorno = false;
		if(valve != null){
			if(!valve.getTag().equals("")){
//				ValveModel valveAux = valveService.findByTag(valve.getTag());
				if(valvesMap.isEmpty()){
					fillValvesMap();
				}
				if(valvesMap.containsKey(valve.getTag())){
					ValveModel valveAux = valvesMap.get(valve.getTag());
					if(valveAux != null){
						BenchControlImpl bcimpl = BenchControlImpl.getInstance();
						if(bcimpl.openValve(valveAux)!=null){
							retorno = true;
						}else{
							retorno = false;
						}
					}else{
						retorno = false;
					}
				}
			}else{
				retorno = false;
			}
		}else{
			retorno = false;
		}
		return retorno;
	}
	
	
	/**
	 * Closes the valve designated in parameter
	 * @param valve
	 * @return boolean to indicate the success of the operation.
	 * true if has been closed. False if operation 
	 */
	public boolean closeValve(ValveModel valve){
		boolean retorno = false;
		if(valve != null){
			if(!valve.getTag().equals("")){
//				ValveModel valveAux = valveService.findByTag(valve.getTag());
				if(valvesMap.isEmpty()){
					fillValvesMap();
				}
				if(valvesMap.containsKey(valve.getTag())){
					ValveModel valveAux = valvesMap.get(valve.getTag());
					if(valveAux != null){
						BenchControlImpl bcimpl = BenchControlImpl.getInstance();
						if(bcimpl.closeValve(valveAux)!=null){
							retorno = true;
						}else{
							retorno = false;
						}
					}else{
						retorno = false;
					}
				}
			}else{
				retorno = false;
			}
		}else{
			retorno = false;
		}
		return retorno;
	}
	
	/**
	 * Function retrieves from database the selected valve
	 * @param valve
	 * @return the valve read from database
	 */
	public ValveModel findValveByTag(ValveModel valve){
		ValveModel retorno = null;
		if(valve != null){
			if(!valve.getTag().equals("")){
				retorno = valveService.findByTag(valve.getTag());
			}
		}
		return retorno;
	}
	
	/**
	 * This method will check if any valve obstructs the flow path of the bench.
	 * This check does not verify the diversion of flow between inferior reservoir and 
	 * scale recipient. This means that if the flow is directed either way, the flow is not
	 * considered to be obstructed. 
	 * @return true if the flow will be blocked by any valve
	 */
	public boolean isFlowPathBlocked(){
		boolean retorno = false;
		HashMap<String, ValveStateEnum> tagXstateHash =  new HashMap<>();
		valves = valveService.findAll();
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		
		//First we read all the valves states on the CLP
		for(ValveModel valve :  valves){
			if(!valve.getTag().isEmpty()){
				valve = bcimpl.getValveState(valve);
				tagXstateHash.put(valve.getTag(), valve.getState());
			}
		}
		WaterLineController lineController = new WaterLineController();
		
		//Check to see if line is closed
		if(lineController.isLineClosed()){
			//if upstream XV2 OR downstream XV3 are closed 
			if(tagXstateHash.get(UPSTREAM_VALVE_TAG).equals(ValveStateEnum.CLOSED) || tagXstateHash.get(DOWNSTREAM_VALVE_TAG).equals(ValveStateEnum.CLOSED)){
				retorno = true;
			}
			
			//if ref meters valves XV7, XV8 AND XV9 are CLOSED
			if(tagXstateHash.get(DN02_VALVE_TAG).equals(ValveStateEnum.CLOSED) && 
					tagXstateHash.get(DN08_VALVE_TAG).equals(ValveStateEnum.CLOSED) &&
					tagXstateHash.get(DN32_VALVE_TAG).equals(ValveStateEnum.CLOSED)){
				retorno = true;
			}
			
			//if ramal (branches) valves XV10 ... XV18 AND XV19 are CLOSED
			if(tagXstateHash.get(RAMAL1_VALVE_TAG).equals(ValveStateEnum.CLOSED) && 
					tagXstateHash.get(RAMAL2_VALVE_TAG).equals(ValveStateEnum.CLOSED) &&
					tagXstateHash.get(RAMAL3_VALVE_TAG).equals(ValveStateEnum.CLOSED) &&
					tagXstateHash.get(RAMAL4_VALVE_TAG).equals(ValveStateEnum.CLOSED) &&
					tagXstateHash.get(RAMAL5_VALVE_TAG).equals(ValveStateEnum.CLOSED) &&
					tagXstateHash.get(RAMAL6_VALVE_TAG).equals(ValveStateEnum.CLOSED) &&
					tagXstateHash.get(RAMAL7_VALVE_TAG).equals(ValveStateEnum.CLOSED) &&
					tagXstateHash.get(RAMAL8_VALVE_TAG).equals(ValveStateEnum.CLOSED) &&
					tagXstateHash.get(RAMAL9_VALVE_TAG).equals(ValveStateEnum.CLOSED) &&
					tagXstateHash.get(RAMAL10_VALVE_TAG).equals(ValveStateEnum.CLOSED) ){
				retorno = true;
			}
			
			
		}else{
			System.err.println("ERROR: line is not closed on ValveController.isFlowBlocked()");
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * this method closes the line valves: Upstream and Downstream.
	 * @return true if valves have been closed
	 */
	public boolean closeLineValves(){
		boolean retorno = false;
		if(this.closeDownStreamValve()){
			if(this.closeUpStreamValve()){
				retorno = true;
			}
		}
		return retorno;
	}
	
	/**
	 * this method closes the line valves: Upstream and Downstream.
	 * @return true if valves have been closed
	 */
	public boolean openLineValves(){
		boolean retorno = false;
		if(this.openDownStreamValve()){ 
			if(this.openUpStreamValve()){
				retorno = true;
			}
		}
		return retorno;
	}
	
	/**
	 * Procedure to retrieve all valves from database and place their objects with the name TAG as key on a hashmap. 
	 */
	public void fillValvesMap(){
		try {
			valves = valveService.findAll();
			for(ValveModel valve : valves){
				valvesMap.put(valve.getTag(), valve);
			}
		} catch (CrudDatabaseException e) {
			// Handle the exception - use empty list if database connection fails
			valves = new ArrayList<>();
			System.err.println("Database connection error: " + e.getMessage());
		}
	}
	
	
//	private boolean bciOpenValve(ValveModel valve){
//		boolean retorno = false;
//		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
//		if(bcimpl.openValve(valve)!=null){
//			retorno = true;
//		}else{
//			retorno = false;
//		}
//		return retorno;
//	}
//	
//	private void updateBenchView(ValveModel valve, boolean value){
//		BenchBean benchBeanView = BenchBean.getInstance();
//		
//		if(FEED_INF_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX1(value);
//		}
//		
//		if(UPSTREAM_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX2(value);
//		}
//		
//		if(DOWNSTREAM_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX3(value);
//		}
//		
//		if(AIR_PURGE_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX4(value);
//		}
//		
//		if(PRESS_REG_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX5(value);
//		}
//		
//		if(FEED_INF_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX6(value);
//		}
//		
//		if(DN32_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX7(value);
//		}
//		
//		if(DN08_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX8(value);
//		}
//		
//		if(DN02_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX9(value);
//		}
//		
//		if(RAMAL1_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX10(value);
//		}
//		
//		if(RAMAL2_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX11(value);
//		}
//		
//		if(RAMAL3_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX12(value);
//		}
//		
//		if(RAMAL4_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX13(value);
//		}
//		
//		if(RAMAL5_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX14(value);
//		}
//		
//		if(RAMAL6_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX15(value);
//		}
//		
//		if(RAMAL7_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX16(value);
//		}
//
//		if(RAMAL8_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX17(value);
//		}
//		
//		if(RAMAL9_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX18(value);
//		}
//		
//		if(RAMAL10_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX19(value);
//		}
//		
//		if(SCALE_DIVERSOR_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX20(value);
//		}
//		
//		if(SCALE_CONTAIN_VALVE_TAG == valve.getTag()){
//			benchBeanView.setValveVX21(value);
//		}
//	}
		
	public boolean isDownStreamValvesOpen(){
		boolean retorno = false;
		if(getValveState(DOWNSTREAM_VALVE_TAG).getState().equals(ValveStateEnum.OPENED)){
			retorno = true;
		}
		return retorno;
	}
		
	public boolean isUpStreamValvesOpen(){
		boolean retorno = false;
		if(getValveState(UPSTREAM_VALVE_TAG).getState().equals(ValveStateEnum.OPENED)){
			retorno = true;
		}
		return retorno;
	}
	
	public ValveModel getValveState(String valveTag){
		ValveModel valveAux = new ValveModel();
		valveAux.setTag(valveTag);
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		valveAux = bcimpl.getValveState(valveAux);
		return valveAux; 
	}
	
}
