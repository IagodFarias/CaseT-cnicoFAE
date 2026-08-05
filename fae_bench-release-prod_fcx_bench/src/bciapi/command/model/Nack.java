//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file Nack.java
*    @author marcos
*    @date 17 de nov de 2016
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
public class Nack  extends CommandParent{

	private String responseTo;
	/**
	 * Creates a object for class Nack.java
	 */
	public Nack(){
		super.command = PropertiesReaderUtil.getProperty("field.nack.val");
	}
	/**
	 * Function returns the value of attribute responseTo
	 * @return the responseTo
	 */
	public String getResponseTo() {
		return responseTo;
	}
	/**
	 * Function sets the value for attribute responseTo
	 * @param responseTo the responseTo to set
	 */
	public void setResponseTo(String responseTo) {
		this.responseTo = responseTo;
	}
	
}
