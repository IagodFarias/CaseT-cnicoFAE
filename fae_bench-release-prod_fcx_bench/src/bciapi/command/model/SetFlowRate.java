//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file SetFlowRate.java
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
public class SetFlowRate extends CommandParent{
	
	private double flowRateSel;
	
	private double upperLimit;
	
	private double lowerLimit;
	
	private String pumpSel;
	
	private String refMeterSel;
	
	/**
	 * 
	 * Creates a object for class SetFlowRate.java
	 */
	public SetFlowRate(){
		super.command = PropertiesReaderUtil.getProperty("field.setflowrate.val");
	}
	
	/**
	 * Function returns the value of attribute flowRateSel
	 * @return the flowRateSel
	 */
	public double getFlowRateSel() {
		return flowRateSel;
	}

	/**
	 * Function sets the value for attribute flowRateSel
	 * @param flowRateSel the flowRateSel to set
	 */
	public void setFlowRateSel(double flowRateSel) {
		this.flowRateSel = flowRateSel;
	}

	/**
	 * Function returns the value of attribute upperLimit
	 * @return the upperLimit
	 */
	public double getUpperLimit() {
		return upperLimit;
	}

	/**
	 * Function sets the value for attribute upperLimit
	 * @param upperLimit the upperLimit to set
	 */
	public void setUpperLimit(double upperLimit) {
		this.upperLimit = upperLimit;
	}

	/**
	 * Function returns the value of attribute lowerLimit
	 * @return the lowerLimit
	 */
	public double getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * Function sets the value for attribute lowerLimit
	 * @param lowerLimit the lowerLimit to set
	 */
	public void setLowerLimit(double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	/**
	 * Function returns the value of attribute pumpSel
	 * @return the pumpSel
	 */
	public String getPumpSel() {
		return pumpSel;
	}

	/**
	 * Function sets the value for attribute pumpSel
	 * @param pumpSel the pumpSel to set
	 */
	public void setPumpSel(String pumpSel) {
		this.pumpSel = pumpSel;
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


	
	
	
}
