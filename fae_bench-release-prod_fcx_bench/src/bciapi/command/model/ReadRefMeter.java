//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ReadFlowRateRefMeter.java
*    @author Marcos Oliveira
*    @date 13 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.model;

import bciapi.command.parent.CommandParent;
import si.dbcomm.enumerations.FlowRateStateEnum;
import util.PropertiesReaderUtil;

/**
 * @author Marcos Oliveira
 *
 */
public class ReadRefMeter extends CommandParent {
	
	private String refMeterSel;
	
	private double flowRate;
	
	private double volume;
	
	private int timeRead;
	
	private FlowRateStateEnum flowStability; 
	
	/**
	 * Creates a object for class ReadFlowRateRefMeter.java
	 */
	public ReadRefMeter(){
		super.command = PropertiesReaderUtil.getProperty("field.readrefmeter.val");
	}

	/**
	 * Function returns the value of attribute refMeterSel
	 * @return the refMeterSel
	 */
	public String getRefMeterSel() {
		return refMeterSel;
	}

	/**
	 * Function sets the value for attribute refMeterSel
	 * @param refMeterSel the refMeterSel to set
	 */
	public void setRefMeterSel(String refMeterSel) {
		this.refMeterSel = refMeterSel;
	}

	/**
	 * Function returns the value of attribute flowrate
	 * @return the flowrate
	 */
	public double getFlowRate() {
		return flowRate;
	}

	/**
	 * Function sets the value for attribute flowrate
	 * @param flowrate the flowrate to set
	 */
	public void setFlowRate(double flowrate) {
		this.flowRate = flowrate;
	}

	/**
	 * Function returns the value of attribute volumeVal
	 * @return the volumeVal
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * Function sets the value for attribute volumeVal
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	/**
	 * Function returns the value of attribute flowStability
	 * @return the flowStability
	 */
	public FlowRateStateEnum getFlowStability() {
		return flowStability;
	}

	/**
	 * Function sets the value for attribute flowStability
	 * @param flowStability the flowStability to set
	 */
	public void setFlowStability(FlowRateStateEnum flowStability) {
		this.flowStability = flowStability;
	}

	/**
	 * Function returns the value of attribute timeRead
	 * @return the timeRead
	 */
	public int getTimeRead() {
		return timeRead;
	}

	/**
	 * Function sets the value for attribute timeRead
	 * @param timeRead the timeRead to set
	 */
	public void setTimeRead(int timeRead) {
		this.timeRead = timeRead;
	}
	
	
	
	
}
