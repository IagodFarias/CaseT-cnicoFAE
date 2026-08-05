//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetSensorHumidity.java
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
public class GetSensorHumidity extends CommandParent{

	private String sensorSel;
	
	private double humidityRead;
	
	/**
	 * Creates a object for class GetSensorHumidity.java
	 */
	public GetSensorHumidity(){
		super.command = PropertiesReaderUtil.getProperty("field.getsensorhumidity.val");
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
	public void setSensorSel(String id) {
		this.sensorSel = id;
	}

	/**
	 * Function returns the value of attribute humidityRead
	 * @return the humidityRead
	 */
	public double getHumidityRead() {
		return humidityRead;
	}

	/**
	 * Function sets the value for attribute humidityRead
	 * @param humidityRead the humidityRead to set
	 */
	public void setHumidityRead(double humidityRead) {
		this.humidityRead = humidityRead;
	}
	
	
}
