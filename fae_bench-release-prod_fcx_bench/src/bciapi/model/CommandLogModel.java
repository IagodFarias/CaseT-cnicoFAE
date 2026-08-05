//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CommandLogModel.java
*    @author marcos
*    @date 19 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author marcos
 * Thid class log the commands that are sent to the bench 
 */
public class CommandLogModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4244222840530258571L;

	private String command;
	
	private Date readAt;

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

	/**
	 * Function returns the value of attribute readAt
	 * @return the readAt
	 */
	public Date getReadAt() {
		return readAt;
	}

	/**
	 * Function sets the value for attribute readAt
	 * @param readAt the readAt to set
	 */
	public void setReadAt(Date readAt) {
		this.readAt = readAt;
	}
	
	
	
}
