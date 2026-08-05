//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetLineState.java
*    @author Marcos Oliveira
*    @date 13 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.model;

import bciapi.command.parent.CommandParent;
import si.dbcomm.enumerations.LineStateEnum;
import util.PropertiesReaderUtil;

/**
 * @author Marcos Oliveira
 *
 */
public class GetLineState extends CommandParent {
	
	private LineStateEnum lineState;
	
	/**
	 * 
	 * Creates a object for class GetLineState.java
	 */
	public GetLineState() {
		super.command = PropertiesReaderUtil.getProperty("field.getlinestate.val");
		lineState = LineStateEnum.UNKNOWN;
	}

	/**
	 * Function returns the value of attribute state
	 * @return the state
	 */
	public LineStateEnum getLineState() {
		return lineState;
	}

	/**
	 * Function sets the value for attribute state
	 * @param state the state to set
	 */
	public void setLineState(LineStateEnum state) {
		this.lineState = state;
	}
	
	
}
