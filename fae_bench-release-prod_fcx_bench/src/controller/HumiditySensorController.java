//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file HumiditySensorBean.java
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
import si.dbcomm.model.HumiditySensorModel;
import si.dbcomm.service.HumiditySensorService;
import util.DeviceTagReaderUtil;
import view.bean.BenchBean;

/**
 * @author Marcos
 *
 */
public class HumiditySensorController extends ActionParent{
	
	private HumiditySensorModel humiditySensor = new HumiditySensorModel();
	
	private List<HumiditySensorModel> humiditySensors;
	
	private HumiditySensorService humidityService;
	
	public static final String HUM_SENSOR_TAG = DeviceTagReaderUtil.getProperty("humidity.sensor.tag");


	/**
	 * Creates a object for class HumiditySensorBean.java
	 */
	public HumiditySensorController(HumiditySensorModel humiditySensor) {
		this.humiditySensors = new ArrayList<>();
		this.humiditySensor = humiditySensor;
	}
	/**
	 * Creates a object for class HumiditySensorBean.java
	 */
	public HumiditySensorController() {
		humiditySensor.setTag(HUM_SENSOR_TAG);
	}
	
	/**
	 * Function returns the value of attribute humiditySensor
	 * @return the humiditySensor
	 */
	public HumiditySensorModel getHumiditySensor() {
		this.humiditySensors = new ArrayList<>();
		return humiditySensor;
	}

	/**
	 * Function sets the value for attribute humiditySensor
	 * @param humiditySensor the humiditySensor to set
	 */
	public void setHumiditySensor(HumiditySensorModel humiditySensor) {
		this.humiditySensor = humiditySensor;
	}
	
	/* (non-Javadoc)
	 * @see model.ActionParent#executeAction()
	 */
	@Override
	public void executeAction() {
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		this.humiditySensor = bcimpl.getSensorHumidity(humiditySensor);
	}
	
	/**
	 * Reads de humidity sensor designated by the tag in parameter 
	 * @param tag of the humidity sensor to read
	 * @return
	 */
	public HumiditySensorModel readHumiditySensor(String tag){
		this.humiditySensor = humidityService.findByTag(tag);
		if(humiditySensor != null){
			if(humiditySensor.getTag() != null){
				if(!humiditySensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					humiditySensor = bcimpl.getSensorHumidity(humiditySensor);
					updateBenchView(humiditySensor);
				}
			}
		}
		
		return humiditySensor;
	}
	
	/**
	 * Reads the humidity sensor that is specified in class object and retrieves its double value
	 * @return double - the temperature double value
	 */
	public double readValueHumiSensor(){
		if(this.humiditySensor != null){
			if(this.humiditySensor.getTag() != null){
				if(!this.humiditySensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					this.humiditySensor = bcimpl.getSensorHumidity(humiditySensor);
					updateBenchView(humiditySensor);
				}
			}
		}
		return humiditySensor.getHumidity();
	}
	
	/**
	 * Read all the humidity sensors in the bench and sets them on the array list placed in the controller
	 * @return array list with all the sensors read
	 */
	public List<HumiditySensorModel> readAllHumiSensor(){
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		
		this.humiditySensors = humidityService.findAll();
		if(!humiditySensors.isEmpty()){
			for(HumiditySensorModel humiditySensor  : humiditySensors){		
				humiditySensor  = bcimpl.getSensorHumidity(humiditySensor );
				updateBenchView(humiditySensor );
			}
		}
		return humiditySensors;
	}
	
	private void updateBenchView(HumiditySensorModel humiditySensor){
		BenchBean benchBeanView = BenchBean.getInstance();
		benchBeanView.setHumTRH(humiditySensor.getHumidity());
	}
}
