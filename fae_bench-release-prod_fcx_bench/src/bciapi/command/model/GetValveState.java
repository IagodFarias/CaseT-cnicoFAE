//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetValveState.java
*    @author Marcos Oliveira
*    @date 13 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.model;

import bciapi.command.parent.CommandParent;
import si.dbcomm.enumerations.ValveStateEnum;
import util.PropertiesReaderUtil;

/**
 * @author Marcos Oliveira
 *
 */
public class GetValveState extends CommandParent{

	private String valveSel;
	
	private ValveStateEnum valveState;
	
	/**
	 * Creates a object for class GetValveState.java
	 */
	public GetValveState(){
		super.command = PropertiesReaderUtil.getProperty("field.getvalvestate.val");
		valveState = ValveStateEnum.UNKNOWN;
	}

	/**
	 * Function returns the value of attribute valveSel
	 * @return the valveSel
	 */
	public String getValveSel() {
		return valveSel;
	}

	/**
	 * Function sets the value for attribute valveSel
	 * @param valveSel the valveSel to set
	 */
	public void setValveSel(String valveSel) {
		this.valveSel = valveSel;
	}

	/**
	 * Function returns the value of attribute valveState
	 * @return the valveState
	 */
	public ValveStateEnum getValveState() {
		return valveState;
	}

	/**
	 * Function sets the value for attribute valveState
	 * @param valveState the valveState to set
	 */
	public void setValveState(ValveStateEnum valveState) {
		this.valveState = valveState;
	}

	
	
}
