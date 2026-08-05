//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetSensorPressure.java
*    @author Marcos Oliveira
*    @date 13 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.model;

import bciapi.command.parent.CommandParent;
import util.PropertiesReaderUtil;
/**
 * @author Marcos Oliveira
 *
 */
public class GetSensorPressure extends CommandParent {
	
	private String sensorSel;
	
	private double pressureRead;
	
	/**
	 * 
	 * Creates a object for class GetSensorPressure.java
	 */
	public GetSensorPressure(){
		super.command = PropertiesReaderUtil.getProperty("field.getsensorpressure.val");
	}

	/**
	 * Function returns the value of attribute sensorSel
	 * @return the sensorSel
	 */
	public String getSensorSel() {
		return sensorSel;
	}

	/**
	 * Function sets the value for attribute sensorSel
	 * @param sensorSel the sensorSel to set
	 */
	public void setSensorSel(String sensorSel) {
		this.sensorSel = sensorSel;
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
	
	
	
}
