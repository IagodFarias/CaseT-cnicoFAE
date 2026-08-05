//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file TempSensorBean.java
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
import si.dbcomm.model.TempSensorModel;
import si.dbcomm.service.TempSensorService;
import util.DeviceTagReaderUtil;
import view.bean.BenchBean;

/**
 * @author Marcos
 *
 */
public class TempSensorController extends ActionParent {

	private TempSensorModel tempSensor;
	
	private List<TempSensorModel> tempSensors;
	
	private TempSensorService tempSensorService;
	
	public static final String TEMP_SENSOR_TAG_UPSTREAM = DeviceTagReaderUtil.getProperty("temp.sensor.line.upstream");
	public static final String TEMP_SENSOR_TAG_DOWNSTREAM = DeviceTagReaderUtil.getProperty("temp.sensor.line.downstream");
	public static final String TEMP_SENSOR_TAG_SCALE = DeviceTagReaderUtil.getProperty("temp.sensor.scale");
	public static final String TEMP_SENSOR_TAG_RES_INF = DeviceTagReaderUtil.getProperty("temp.sensor.reserv.inf");
	public static final String TEMP_SENSOR_TAG_RES_SUP = DeviceTagReaderUtil.getProperty("temp.sensor.reserv.sup");
	public static final String TEMP_SENSOR_TAG_AMB = DeviceTagReaderUtil.getProperty("temp.sensor.amb");
	
	/**
	 * 
	 * Creates a object for class TempSensorController.java
	 */
	public TempSensorController(TempSensorModel tempSensor){
		this.tempSensor = tempSensor;
		tempSensors = new ArrayList<>();
		tempSensorService = new TempSensorService();
	}
	
	/**
	 * 
	 * Creates a object for class TempSensorController.java
	 */
	public TempSensorController(){
		tempSensor = new TempSensorModel();
		tempSensorService = new TempSensorService();
		
	}
	
	/**
	 * Function returns the value of attribute tempSensor
	 * @return the tempSensor
	 */
	public TempSensorModel getTempSensor() {
		return tempSensor;
	}

	/**
	 * Function sets the value for attribute tempSensor
	 * @param tempSensor the tempSensor to set
	 */
	public void setTempSensor(TempSensorModel tempSensor) {
		this.tempSensor = tempSensor;
	}
 
	/**
	 * Reads the temperature sensor specified in by parameter
	 * @param tempSensor
	 * @return TempSensorModel
	 */
	public TempSensorModel readTempSensor(String tag){
//		this.tempSensor = tempSensorService.findByTag(tag);
		TempSensorModel tempSensor = new TempSensorModel();
		if(tag != null){
			tempSensor.setTag(tag);
			BenchControlImpl bcimpl = BenchControlImpl.getInstance();
			if((tempSensor = bcimpl.getSensorTemp(tempSensor))!=null);
			this.tempSensor = tempSensor;
			updateBenchView(tempSensor);
		}
		
		return tempSensor;
	}
	
	/**
	 * Reads the temperature sensor specified in by parameter
	 * @param tempSensor
	 * @return TempSensorModel
	 */
	public TempSensorModel readTempSensor(TempSensorModel tempSensor){
		if(tempSensor != null){
			if(tempSensor.getTag() != null){
				if(!tempSensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					tempSensor = bcimpl.getSensorTemp(tempSensor);
					updateBenchView(tempSensor);
				}
			}
		}
		
		return tempSensor;
	}
	
	/**
	 * Reads the temperature sensor that is specified in class object
	 * @return TempSensorModel
	 */
	public TempSensorModel readTempSensor(){
		if(this.tempSensor != null){
			if(this.tempSensor.getTag() != null){
				if(!this.tempSensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					this.tempSensor = bcimpl.getSensorTemp(tempSensor);
					updateBenchView(tempSensor);
				}
			}
		}
		return tempSensor;
	}
	
	/**
	 * Reads the temperature sensor that is specified in class object and retrieves its double value
	 * @return double - the temperature double value
	 */
	public double readValueTempSensor(){
		if(this.tempSensor != null){
			if(this.tempSensor.getTag() != null){
				if(!this.tempSensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					this.tempSensor = bcimpl.getSensorTemp(tempSensor);
					updateBenchView(tempSensor);
				}
			}
		}
		return tempSensor.getTemperature();
	}
	
	/**
	 * Reads the temperature sensor that is specified in class object and retrieves its double value
	 * @return double - the temperature double value
	 */
	public double readValueTempSensor(String tag){
		if(tag != null){
			if(!tag.equals("")){
				TempSensorModel tempSensor = new TempSensorModel();
				tempSensor.setTag(tag);
				BenchControlImpl bcimpl = BenchControlImpl.getInstance();
				this.tempSensor = bcimpl.getSensorTemp(tempSensor);
				updateBenchView(tempSensor);
			}
		}
		return tempSensor.getTemperature();
	}
	
	/**
	 * 
	 * @param tempSensor
	 * @return
	 */
	public List<TempSensorModel> readAllTempSensor(){
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		
		tempSensors = tempSensorService.findAll();
		if(!tempSensors.isEmpty()){
			for(TempSensorModel tempSensor : tempSensors){		
				tempSensor = bcimpl.getSensorTemp(tempSensor);
				updateBenchView(tempSensor);
			}
		}
		return tempSensors;
	}
	
	private void updateBenchView(TempSensorModel tempSensor){
		BenchBean benchBeanView = BenchBean.getInstance();
		if(TEMP_SENSOR_TAG_DOWNSTREAM ==  tempSensor.getTag()){
			benchBeanView.setTempTTLO(tempSensor.getTemperature());
		}else{
			if(TEMP_SENSOR_TAG_UPSTREAM ==  tempSensor.getTag()){
				benchBeanView.setTempTTLI(tempSensor.getTemperature());
			}else{
				if(TEMP_SENSOR_TAG_RES_INF ==  tempSensor.getTag()){
					benchBeanView.setTempTTRI(tempSensor.getTemperature());
				}else{
					if(TEMP_SENSOR_TAG_RES_SUP ==  tempSensor.getTag()){
						benchBeanView.setTempTTRS(tempSensor.getTemperature());
					}else{
						if(TEMP_SENSOR_TAG_SCALE ==  tempSensor.getTag()){
							benchBeanView.setTempTTRS(tempSensor.getTemperature());
						}
					}
				}
			}
			
		}
	}
	
	/*
	 * 
	 */
	public void executeAction() {
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		tempSensor = bcimpl.getSensorTemp(tempSensor);
	}
	
	
	
}
