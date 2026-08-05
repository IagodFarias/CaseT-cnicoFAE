//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CloseValve.java
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
public class CloseValve extends CommandParent {

	private String valveSel;
	
	
	/**
	 * Creates a object for class CloseValve.java
	 */
	public CloseValve(){
		super.command = PropertiesReaderUtil.getProperty("field.closevalve.val");
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
	
	
}
