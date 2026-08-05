//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CommandParent.java
*    @author Marcos Oliveira
*    @date 13 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.parent;

import util.PropertiesReaderUtil;

/**
 * @author Marcos Oliveira
 *
 */
public class CommandParent {
	
	protected String command = PropertiesReaderUtil.getProperty("command");

	/**
	 * Function returns the value of attribute command
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Function sets the value for attribute command
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	
	
}
