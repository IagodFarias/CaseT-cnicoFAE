//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file DataView.java
*    @author marcos
*    @date 1 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.datathread;

import bciapi.impl.BenchControlImpl;
import si.dbcomm.model.PressureSensorModel;
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.model.TempSensorModel;
import util.ProcessLoggerUtil;

/**
 * @author marcos
 *
 */
public class DataView implements Runnable{
	private double temperature;
	
	private double pressure;
	
	private double flowRate;
	
	private double waterLevel;
	
	private double volume;
	
	private BenchControlImpl bci = BenchControlImpl.getInstance();
	
	private RefMeterModel refMeter;
	
	private PressureSensorModel pressSensor;
	
	private TempSensorModel tempSensor;
	
	private boolean running = false;
	
	
	/**
	 * Creates a object for class DataView.java
	 */
	public DataView(RefMeterModel refMeter, PressureSensorModel pressSensor, TempSensorModel tempSensor) {
		this.refMeter = refMeter;
		this.pressSensor = pressSensor;
		this.tempSensor = tempSensor;
		
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

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(running){
			refMeter = bci.readRefMeter(refMeter);
			pressure = bci.getSensorPressure(pressSensor).getPressureRead();
			temperature = bci.getSensorTemp(tempSensor).getTemperature();
			
			flowRate = refMeter.getFlowRate();
			volume = refMeter.getVolume();
			try {
				Thread.sleep(100); // TODO
			} catch (InterruptedException e) {
				e.printStackTrace();
				running=false;
			}
		
		}
		
		
	}

	/**
	 * Function returns the value of attribute temperature
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Function sets the value for attribute temperature
	 * @param temperature the temperature to set
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Function returns the value of attribute pressure
	 * @return the pressure
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * Function sets the value for attribute pressure
	 * @param pressure the pressure to set
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/**
	 * Function returns the value of attribute flowRate
	 * @return the flowRate
	 */
	public double getFlowRate() {
		return flowRate;
	}

	/**
	 * Function sets the value for attribute flowRate
	 * @param flowRate the flowRate to set
	 */
	public void setFlowRate(double flowRate) {
		this.flowRate = flowRate;
	}

	/**
	 * Function returns the value of attribute waterLevel
	 * @return the waterLevel
	 */
	public double getWaterLevel() {
		return waterLevel;
	}

	/**
	 * Function sets the value for attribute waterLevel
	 * @param waterLevel the waterLevel to set
	 */
	public void setWaterLevel(double waterLevel) {
		this.waterLevel = waterLevel;
	}

	/**
	 * Function returns the value of attribute volume
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * Function sets the value for attribute volume
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
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
 
	
	
	
}
