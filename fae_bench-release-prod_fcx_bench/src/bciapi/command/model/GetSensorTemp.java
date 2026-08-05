//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetSensorTemp.java
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
public class GetSensorTemp extends CommandParent{

	private String sensorSel;
	
	private double tempRead;
	
	/**
	 * Creates a object for class GetSensorTemp.java
	 */
	public GetSensorTemp(){
		super.command = PropertiesReaderUtil.getProperty("field.getsensortemp.val");
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
	
	
}
