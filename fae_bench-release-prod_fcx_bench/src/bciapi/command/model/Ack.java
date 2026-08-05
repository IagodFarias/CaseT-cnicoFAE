//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file Ack.java
*    @author Marcos Oliveira
*    @date 17 de mai de 2016
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
public class Ack extends CommandParent {

	private String responseTo;
	
	/**
	 * Creates a object for class Ack.java
	 */
	public Ack(){
		super.command = PropertiesReaderUtil.getProperty("field.ack.val");
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
