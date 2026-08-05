//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file StopCounter.java
*    @author marcos
*    @date 8 de dez de 2016
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
public class StopCounter extends CommandParent {
	
	private String refMeterSel;
	
	/**
	 * 
	 * Creates a object for class SetFlowRate.java
	 */
	public StopCounter(){
		super.command = PropertiesReaderUtil.getProperty("field.stopcounter.val");
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

	
}
