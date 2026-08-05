//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ResponseSetAutoFlowRate.java
*    @author marcos
*    @date 14 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.model;

import bciapi.command.parent.CommandParent;

/**
 * @author marcos
 *
 */
public class ResponseSetAutoFlowRate extends CommandParent {
		
	private double flowRateRead;
	private double pressureRead;
	private double tempRead;
	private double volumeRead;
	/**
	 * Function returns the value of attribute flowRateRead
	 * @return the flowRateRead
	 */
	public double getFlowRateRead() {
		return flowRateRead;
	}
	/**
	 * Function sets the value for attribute flowRateRead
	 * @param flowRateRead the flowRateRead to set
	 */
	public void setFlowRateRead(double flowRateRead) {
		this.flowRateRead = flowRateRead;
	}
	/**
	 * Function returns the value of attribute pressureRead
	 * @return the pressureRead
	 */
	public double getPressureRead() {
		return pressureRead;
	}
	/**
	 * Function sets the value for attribute pressureRead
	 * @param pressureRead the pressureRead to set
	 */
	public void setPressureRead(double pressureRead) {
		this.pressureRead = pressureRead;
	}
	/**
	 * Function returns the value of attribute tempRead
	 * @return the tempRead
	 */
	public double getTempRead() {
		return tempRead;
	}
	/**
	 * Function sets the value for attribute tempRead
	 * @param tempRead the tempRead to set
	 */
	public void setTempRead(double tempRead) {
		this.tempRead = tempRead;
	}
	/**
	 * Function returns the value of attribute volumeRead
	 * @return the volumeRead
	 */
	public double getVolumeRead() {
		return volumeRead;
	}
	/**
	 * Function sets the value for attribute volumeRead
	 * @param volumeRead the volumeRead to set
	 */
	public void setVolumeRead(double volumeRead) {
		this.volumeRead = volumeRead;
	}
	
	
}
