//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file SetPumpLoad.java
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
public class SetPumpLoad extends CommandParent{

	private String pumpSel;
	private double percentSel;
	
	/**
	 * Creates a object for class SetPumpLoad.java
	 */
	public SetPumpLoad(){
		super.command = PropertiesReaderUtil.getProperty("field.setpumpload.val");
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
