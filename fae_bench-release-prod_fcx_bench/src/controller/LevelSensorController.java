//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file LevelSensorBean.java
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
import si.dbcomm.model.LevelSensorModel;
import si.dbcomm.service.LevelSensorService;
import util.DeviceTagReaderUtil;
import view.bean.BenchBean;

/**
 * @author Marcos
 *
 */
public class LevelSensorController extends ActionParent{
	
	private LevelSensorService levelService;
	
	private List<LevelSensorModel> levelSensors;
	
	private LevelSensorModel levelSensor;
	
	public static final String LEVEL_SENSOR_INF_LOW=DeviceTagReaderUtil.getProperty("level.sensor.inferiorres.low");
	public static final String LEVEL_SENSOR_INF_MID=DeviceTagReaderUtil.getProperty("level.sensor.inferiorres.mid");
	public static final String LEVEL_SENSOR_INF_HIGH=DeviceTagReaderUtil.getProperty("level.sensor.inferiorres.high");
	public static final String LEVEL_SENSOR_SUP_HIGH=DeviceTagReaderUtil.getProperty("level.sensor.superiorres.high");
	
	
	/**
	 * Creates a object for class LevelSensorBean.java
	 */
	public LevelSensorController() {
		this.levelService = new LevelSensorService();
		this.levelSensors = new ArrayList<>();
	}
	
	/**
	 * Creates a object for class LevelSensorBean.java
	 */
	public LevelSensorController(LevelSensorModel levelSensor) {
		this.levelSensor = levelSensor;
		this.levelService = new LevelSensorService();
		this.levelSensors = new ArrayList<>();
	}
	
	/* (non-Javadoc)
	 * @see model.ActionParent#executeAction()
	 */
	@Override
	public void executeAction() {
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		levelSensor = bcimpl.getSensorLevel(levelSensor);
		
	}

	/**
	 * Function returns the value of attribute levelSensor
	 * @return the levelSensor
	 */
	public LevelSensorModel getLevelSensor() {
		return levelSensor;
	}

	/**
	 * Function sets the value for attribute levelSensor
	 * @param levelSensor the levelSensor to set
	 */
	public void setLevelSensor(LevelSensorModel levelSensor) {
		this.levelSensor = levelSensor;
	}
	
	/**
	 * Reads a level sensor indicated by object in parameter
	 * @param levelSensor
	 * @return
	 */
	public LevelSensorModel readLevelSensor(LevelSensorModel levelSensor){
		LevelSensorModel levelReturn = null;
		if(levelSensor != null){
			if(levelSensor.getTag() != null){
				if(!levelSensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					levelReturn = bcimpl.getSensorLevel(levelSensor);
					updateBenchView(levelSensor);
				}
			}
		}
		
		return levelReturn;
	}
	
	/**
	 * Reads the level sensor indicated by the tag parameter
	 * @param tag - related to the level sensor to read
	 * @return
	 */
	public LevelSensorModel readLevelSensor(String tag){
		this.levelSensor = levelService.findByTag(tag);
		LevelSensorModel levelReturn = null;
		if(levelSensor != null){
			if(levelSensor.getTag() != null){
				if(!levelSensor.getTag().equals("")){
					BenchControlImpl bcimpl = BenchControlImpl.getInstance();
					levelReturn = bcimpl.getSensorLevel(levelSensor);
					updateBenchView(levelSensor);
				}
			}
		}
		
		return levelReturn;
	}
	
	
	/**
	 * Reads all the level sensors in the bench
	 * @return
	 */
	public List<LevelSensorModel> readAllLevelSensors(){
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		
		levelSensors = levelService.findAll();
		if(!levelSensors.isEmpty()){
			for(LevelSensorModel levelSensor : levelSensors){		
				levelSensor = bcimpl.getSensorLevel(levelSensor);
				updateBenchView(levelSensor);
			}
		}
		return levelSensors;
	}
	
	private void updateBenchView(LevelSensorModel levelSensor){
		BenchBean benchBeanView = BenchBean.getInstance();
		
		if(LEVEL_SENSOR_SUP_HIGH==levelSensor.getTag()){
			benchBeanView.setLevelSN4(levelSensor.isLevel());
		}
		if(LEVEL_SENSOR_INF_HIGH==levelSensor.getTag()){
			benchBeanView.setLevelSN3(levelSensor.isLevel());
		}
		if(LEVEL_SENSOR_INF_MID==levelSensor.getTag()){
			benchBeanView.setLevelSN2(levelSensor.isLevel());
		}
		if(LEVEL_SENSOR_INF_LOW==levelSensor.getTag()){
			benchBeanView.setLevelSN1(levelSensor.isLevel());
		}
	}
}
