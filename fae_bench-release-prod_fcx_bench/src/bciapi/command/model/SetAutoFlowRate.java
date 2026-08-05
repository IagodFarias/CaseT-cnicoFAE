//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file SetAutoFlowRate.java
*    @author Marcos Oliveira
*    @date 23 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.model;

import bciapi.command.parent.CommandParent;
import util.PropertiesReaderUtil;

/**
 * @author Marcos Oliveira
 * Class used to create JSON command to sendo to Bench Control Interface
 */
public class SetAutoFlowRate extends CommandParent {
	
	//Flowrate value to run 1 - 16.000
	private double flowRateSel;
	
	//Duration to run the flow rate 0 - 3600
	private int secSel;
	
	//Number of reprtition to execute the flow rate 0 - 256
	private int rep;
	
	//Interval between the execution of the flow rates 0 - 3600
	private double interval;
	
	//Period the BCI will send the response with the flowrate value.
	private double period;
	
	//Sets that the BCI must send the current read flow rate as the it changes.
	private boolean onChange;

	/**
	 * 
	 * Creates a object for class SetAutoFlowRate.java
	 */
	public SetAutoFlowRate(){
		super.command = PropertiesReaderUtil.getProperty("field.setautoflowrate.val");
	}
	

	/**
	 * Function returns the value of attribute flowrate
	 * @return the flowrate
	 */
	public double getFlowRateSel() {
		return flowRateSel;
	}


	/**
	 * Function sets the value for attribute flowrate
	 * @param flowrate the flowrate to set
	 */
	public void setFlowRateSel(double flowrate) {
		this.flowRateSel = flowrate;
	}


	/**
	 * Function returns the value of attribute secSel
	 * @return the secSel
	 */
	public int getSecSel() {
		return secSel;
	}

	/**
	 * Function sets the value for attribute secSel
	 * @param secSel the secSel to set
	 */
	public void setSecSel(int secSel) {
		this.secSel = secSel;
	}

	/**
	 * Function returns the value of attribute numRep
	 * @return the numRep
	 */
	public int getRep() {
		return rep;
	}

	/**
	 * Function sets the value for attribute numRep
	 * @param numRep the numRep to set
	 */
	public void setRep(int numRep) {
		this.rep = numRep;
	}

	/**
	 * Function returns the value of attribute interval
	 * @return the interval
	 */
	public double getInterval() {
		return interval;
	}

	/**
	 * Function sets the value for attribute interval
	 * @param interval the interval to set
	 */
	public void setInterval(double interval) {
		this.interval = interval;
	}

	/**
	 * Function returns the value of attribute period
	 * @return the period
	 */
	public double getPeriod() {
		return period;
	}

	/**
	 * Function sets the value for attribute period
	 * @param period the period to set
	 */
	public void setPeriod(double period) {
		this.period = period;
	}

	/**
	 * Function returns the value of attribute onChange
	 * @return the onChange
	 */
	public boolean isOnChange() {
		return onChange;
	}

	/**
	 * Function sets the value for attribute onChange
	 * @param onChange the onChange to set
	 */
	public void setOnChange(boolean onChange) {
		this.onChange = onChange;
	}
	
	
	
}
