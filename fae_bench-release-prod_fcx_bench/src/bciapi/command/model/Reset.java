//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file Reset.java
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
public class Reset extends CommandParent  {
	
	/**
	 * Creates a object for class ReadFlowRateRefMeter.java
	 */
	public Reset(){
		super.command = PropertiesReaderUtil.getProperty("field.reset.val");
	}
}
