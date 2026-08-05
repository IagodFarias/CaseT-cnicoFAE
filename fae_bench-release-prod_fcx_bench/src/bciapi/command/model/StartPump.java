//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file StartPump.java
*    @author marcos
*    @date 14 de nov de 2016
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
public class StartPump extends CommandParent{
	
	private String pumpSel;
	
	public StartPump() {
		super.command = PropertiesReaderUtil.getProperty("field.startpump.val");
	}

	/**
	 * Function returns the value of attribute pumpSel
	 * @return the pumpSel
	 */
	public String getPumpSel() {
		return pumpSel;
	}

	/**
	 * Function sets the value for attribute pumpSel
	 * @param pumpSel the pumpSel to set
	 */
	public void setPumpSel(String pumpSel) {
		this.pumpSel = pumpSel;
	}
	
	
}
