//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetPumpLoad.java
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
 * this class has been removed from the API. GetPumpState will retrieve the pump
 * data
 */
public class GetPumpLoad extends CommandParent {

	private long pumpSel;
	
	private double percenSel;
	
	/**
	 * Creates a object for class GetPumpLoad.java
	 */
	public GetPumpLoad(){
		super.command = PropertiesReaderUtil.getProperty("field.getpumpload.val");
	}

	/**
	 * Function returns the value of attribute pumpSel
	 * @return the pumpSel
	 */
	public long getPumpSel() {
		return pumpSel;
	}

	/**
	 * Function sets the value for attribute pumpSel
	 * @param pumpSel the pumpSel to set
	 */
	public void setPumpSel(long pumpSel) {
		this.pumpSel = pumpSel;
	}

	/**
	 * Function returns the value of attribute percenSel
	 * @return the percenSel
	 */
	public double getPercenSel() {
		return percenSel;
	}

	/**
	 * Function sets the value for attribute percenSel
	 * @param percenSel the percenSel to set
	 */
	public void setPercenSel(double percenSel) {
		this.percenSel = percenSel;
	}
	
	
	
	
}
