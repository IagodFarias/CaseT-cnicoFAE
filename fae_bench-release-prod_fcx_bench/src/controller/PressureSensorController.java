//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PressureSensorBean.java
*    @author Marcos
*    @date 17 de jun de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import java.util.ArrayList;
import java.util.List;

import bciapi.impl.BenchControlImpl;
import si.dbcomm.model.PressureSensorModel;
import si.dbcomm.service.PressureSensorService;
import util.DeviceTagReaderUtil;
import view.bean.BenchBean;

/**
 * @author Marcos
 *
 */
public class PressureSensorController extends ActionParent{
	
	private PressureSensorModel pressSensor;
	
	private PressureSensorService pressService;
	
	private List<PressureSensorModel> pressSensors;
	
	public static final String PRESS_SENSOR_TAG_UPSTREAM = DeviceTagReaderUtil.getProperty("press.sensor.line.upstream");
	public static final String PRESS_SENSOR_TAG_DOWNSTREAM = DeviceTagReaderUtil.getProperty("press.sensor.line.downstream");
	public static final String PRESS_SENSOR_TAG_DIF = DeviceTagReaderUtil.getProperty("press.sensor.dif");
	public static final String PRESS_SENSOR_TAG_BAR = DeviceTagReaderUtil.getProperty("press.sensor.barometric");

	/**
	 * Creates a object for class PressureSensorBean.java
	 */
	public PressureSensorController(PressureSensorModel pressureSensor) {
		this.pressSensor = pressureSensor;
	}
	
	/**
	 * Creates a object for class PressureSensorBean.java
	 */
	public PressureSensorController() {
		pressSensor = new PressureSensorModel();
		pressService = new PressureSensorService();
		pressSensors = new ArrayList<>();
	}

	
	/**
	 * Function returns the value of attribute pressSensor
	 * @return the pressSensor
	 */
	public PressureSensorModel getPressSensor() {
		return pressSensor;
	}

	/**
	 * Function sets the value for attribute pressSensor
	 * @param pressSensor the pressSensor to set
	 */
	public void setPressSensor(PressureSensorModel pressSensor) {
		this.pressSensor = pressSensor;
	}

	/**
	 * Reads the temperature sensor specified in by parameter
	 * @param tempSensor
	 * @return TempSensorModel
	 */
	public PressureSensorModel readPressSensor(PressureSensorModel pressSensor){
		if(pressSensor != null){
			if(pressSensor.getTag() != null){
				if(!pressSensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					pressSensor = bcimpl.getSensorPressure(pressSensor);
					updateBenchView(pressSensor);
				}
			}
		}
		return pressSensor;
	}
	
	/**
	 * Reads the temperature sensor specified in by parameter
	 * @param tempSensor
	 * @return TempSensorModel
	 */
	public PressureSensorModel readPressSensor(String tag){
		PressureSensorModel pressSensor = new PressureSensorModel();
		if(tag != null){
			pressSensor.setTag(tag);
			BenchControlImpl bcimpl = BenchControlImpl.getInstance();
			pressSensor = bcimpl.getSensorPressure(pressSensor);
			this.pressSensor = pressSensor;
			updateBenchView(pressSensor);
		}
		
		return pressSensor;
	}
	
	/**
	 * Reads the temperature sensor that is specified in class object
	 * @return TempSensorModel
	 */
	public PressureSensorModel readPressSensor(){
		if(this.pressSensor != null){
			if(pressSensor.getTag() != null){
				if(!pressSensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					pressSensor = bcimpl.getSensorPressure(pressSensor);
					updateBenchView(pressSensor);
				}
			}
		}
		
		return pressSensor;
	}
	
	/**
	 * Reads the temperature sensor that is specified in class object
	 * @return TempSensorModel
	 */
	public double readValuePressSensor(String tag){
		double retorno = Double.NaN;
		if(tag != null){
			if(!tag.isEmpty()){
				PressureSensorModel pressAux;
				pressAux = new PressureSensorModel();
				pressAux.setTag(tag);
				BenchControlImpl bcimpl = BenchControlImpl.getInstance();
				pressAux = bcimpl.getSensorPressure(pressAux);
				retorno = pressAux.getPressureRead();
				updateBenchView(pressAux);
			}
		}
		return retorno;
	}
	/**
	 * Reads the temperature sensor that is specified in class object
	 * @return TempSensorModel
	 */
	public double readValuePressSensor(){
		if(this.pressSensor != null){
			if(pressSensor.getTag() != null){
				if(!pressSensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					pressSensor = bcimpl.getSensorPressure(pressSensor);
					updateBenchView(pressSensor);
				}
			}
		}
		return pressSensor.getPressureRead();
	}
	
	/**
	 * 
	 * @param tempSensor
	 * @return
	 */
	public List<PressureSensorModel> readAllPressSensor(){
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		
		pressSensors = pressService.findAll();
		if(!pressSensors.isEmpty()){
			for(PressureSensorModel pressSensor : pressSensors){		
				pressSensor = bcimpl.getSensorPressure(pressSensor);
				updateBenchView(pressSensor);
			}
		}
		return pressSensors;
	}
	
	public PressureSensorModel readDownstreamPressSensor(){
		
		return null;
	}
	
	
	/**
	 * Method update the GUI. BenchBean encapsulates the variables presented on the bench
	 * @param pressSensor
	 */
	private void updateBenchView(PressureSensorModel pressSensor){
		BenchBean benchBeanView = BenchBean.getInstance();
		
		if(PRESS_SENSOR_TAG_BAR == pressSensor.getTag()){
			benchBeanView.setPressPTBAR(pressSensor.getPressureRead());
		}else{
			if(PRESS_SENSOR_TAG_DIF == pressSensor.getTag()){
				benchBeanView.setPressDiff(pressSensor.getPressureRead());
			}else{
				if(PRESS_SENSOR_TAG_DOWNSTREAM == pressSensor.getTag()){
					benchBeanView.setPressPTLO(pressSensor.getPressureRead());
				}else{
					if(PRESS_SENSOR_TAG_UPSTREAM == pressSensor.getTag()){
						benchBeanView.setPressPTLI(pressSensor.getPressureRead());
					}
				}
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see model.ActionParent#executeAction()
	 */
	@Override
	public void executeAction() {
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		pressSensor = bcimpl.getSensorPressure(pressSensor);
		
	}
	
}
