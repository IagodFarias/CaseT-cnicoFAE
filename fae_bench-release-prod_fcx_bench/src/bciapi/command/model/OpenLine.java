//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file OpenLine.java
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
public class OpenLine extends CommandParent {
	
	/**
	 * Creates a object for class OpenLine.java
	 */
	public OpenLine(){
		super.command = PropertiesReaderUtil.getProperty("field.openline.val");
	}
	
}
