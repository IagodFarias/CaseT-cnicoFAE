//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file SetPidParameters.java
*    @author marcos
*    @date 13 de mar de 2017
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
public class SetPidParameters extends CommandParent {
	
	private String pumpSel;
	
	private double kpSel;

	private double tmSel;
	
	private double tvSel;
	
	private double e;
	
	/**
	 * 
	 * Creates a object for class SetPidParameters.java
	 */
	public SetPidParameters(){
		super.command = PropertiesReaderUtil.getProperty("field.setpidparameters.val");
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
	 * Function returns the value of attribute kpSel
	 * @return the kpSel
	 */
	public double getKpSel() {
		return kpSel;
	}

	/**
	 * Function sets the value for attribute kpSel
	 * @param kpSel the kpSel to set
	 */
	public void setKpSel(double kpSel) {
		this.kpSel = kpSel;
	}

	/**
	 * Function returns the value of attribute tmSel
	 * @return the tmSel
	 */
	public double getTmSel() {
		return tmSel;
	}

	/**
	 * Function sets the value for attribute tmSel
	 * @param tmSel the tmSel to set
	 */
	public void setTmSel(double tmSel) {
		this.tmSel = tmSel;
	}

	/**
	 * Function returns the value of attribute tvSel
	 * @return the tvSel
	 */
	public double getTvSel() {
		return tvSel;
	}

	/**
	 * Function sets the value for attribute tvSel
	 * @param tvSel the tvSel to set
	 */
	public void setTvSel(double tvSel) {
		this.tvSel = tvSel;
	}

	/**
	 * Function returns the value of attribute e
	 * @return the e
	 */
	public double getE() {
		return e;
	}

	/**
	 * Function sets the value for attribute e
	 * @param e the e to set
	 */
	public void setE(double e) {
		this.e = e;
	}
	
	
	
}


