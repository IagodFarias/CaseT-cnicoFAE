//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetPumpState.java
*    @author Marcos Oliveira
*    @date 13 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.model;

import bciapi.command.parent.CommandParent;
import si.dbcomm.enumerations.PumpStateEnum;
import util.PropertiesReaderUtil;

/**
 * @author Marcos Oliveira
 *
 */
public class GetPumpState extends CommandParent {

	private String pumpSel;
	
	private double percentSel;

	private PumpStateEnum pumpState;
	
	
	public GetPumpState(){
		super.command = PropertiesReaderUtil.getProperty("field.getpumpstate.val");
		pumpState = PumpStateEnum.UNKNOWN;
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

	/**
	 * Function returns the value of attribute pumpState
	 * @return the pumpState
	 */
	public PumpStateEnum getPumpState() {
		return pumpState;
	}

	/**
	 * Function sets the value for attribute pumpState
	 * @param pumpState the pumpState to set
	 */
	public void setPumpState(PumpStateEnum pumpState) {
		this.pumpState = pumpState;
	}

	/**
	 * Function returns the value of attribute percentSel
	 * @return the percentSel
	 */
	public double getPercentSel() {
		return percentSel;
	}

	/**
	 * Function sets the value for attribute percentSel
	 * @param percentSel the percentSel to set
	 */
	public void setPercentSel(double percentSel) {
		this.percentSel = percentSel;
	}
	
	
}
