//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file GetCounter.java
*    @author marcos
*    @date 8 de dez de 2016
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
public class GetCounter extends CommandParent {

	private String refMeterSel;
	
	private int counterRead;
	
	private int timeRead;
	
	
	/**
	 * 
	 * Creates a object for class SetFlowRate.java
	 */
	public GetCounter(){
		super.command = PropertiesReaderUtil.getProperty("field.getcounter.val");
	}

	/**
	 * Function returns the value of attribute refMeterSel
	 * @return the refMeterSel
	 */
	public String getRefMeterSel() {
		return refMeterSel;
	}

	/**
	 * Function sets the value for attribute refMeterSel
	 * @param refMeterSel the refMeterSel to set
	 */
	public void setRefMeterSel(String refMeterSel) {
		this.refMeterSel = refMeterSel;
	}

	/**
	 * Function returns the value of attribute counterRead
	 * @return the counterRead
	 */
	public int getCounterRead() {
		return counterRead;
	}

	/**
	 * Function sets the value for attribute counterRead
	 * @param counterRead the counterRead to set
	 */
	public void setCounterRead(int counterRead) {
		this.counterRead = counterRead;
	}

	/**
	 * Function returns the value of attribute timeRead
	 * @return the timeRead
	 */
	public int getTimeRead() {
		return timeRead;
	}

	/**
	 * Function sets the value for attribute timeRead
	 * @param timeRead the timeRead to set
	 */
	public void setTimeRead(int timeRead) {
		this.timeRead = timeRead;
	}
	
	
	
	
}
