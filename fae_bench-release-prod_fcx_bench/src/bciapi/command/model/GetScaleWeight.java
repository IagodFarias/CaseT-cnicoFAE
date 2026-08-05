//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetScaleWeight.java
*    @author Marcos Oliveira
*    @date 16 de mai de 2016
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
public class GetScaleWeight extends CommandParent {
	
	private double weight;
	
	/**
	 * Creates a object for class GetScaleWeight.java
	 */
	public GetScaleWeight(){
		super.command = PropertiesReaderUtil.getProperty("field.getscaleweight.val");
	}

	/**
	 * Function returns the value of attribute weight
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Function sets the value for attribute weight
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
}
