//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetSensorLevel.java
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
public class GetSensorLevel extends CommandParent{
	
	private String sensorSel;
	
	private boolean levelState;
	
	/**
	 * Creates a object for class GetSensorLevel.java
	 */
	public GetSensorLevel(){
		super.command = PropertiesReaderUtil.getProperty("field.getsensorlevel.val");
	}

	/**
	 * Function returns the value of attribute id
	 * @return the id
	 */
	public String getSensorSel() {
		return sensorSel;
	}

	/**
	 * Function sets the value for attribute id
	 * @param id the id to set
	 */
	public void setSensorSel(String id) {
		this.sensorSel = id;
	}

	/**
	 * Function returns the value of attribute levelState
	 * @return the levelState
	 */
	public boolean isLevelState() {
		return levelState;
	}

	/**
	 * Function sets the value for attribute levelState
	 * @param levelState the levelState to set
	 */
	public void setLevelState(boolean levelState) {
		this.levelState = levelState;
	}
	
	
}
