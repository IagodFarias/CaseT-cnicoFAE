//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetAlarms.java
*    @author marcos
*    @date 9 de dez de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.model;

import bciapi.command.parent.CommandParent;
import util.PropertiesReaderUtil;

/**
 * @author marcos
 *
 */
public class GetAlarms extends CommandParent{

	private int alarmSel;
	
	/**
	 * 
	 */
	public GetAlarms(){
		super.command = PropertiesReaderUtil.getProperty("field.getalarms.val");
	}

	/**
	 * Function returns the value of attribute alarmSel
	 * @return the alarmSel
	 */
	public int getAlarmSel() {
		return alarmSel;
	}

	/**
	 * Function sets the value for attribute alarmSel
	 * @param alarmSel the alarmSel to set
	 */
	public void setAlarmSel(int alarmSel) {
		this.alarmSel = alarmSel;
	}
	
	
}
