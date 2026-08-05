//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ViewData.java
*    @author marcos
*    @date 3 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.util;

import si.dbcomm.model.BenchDataModel;

/**
 * @author marcos
 *
 */
public class ViewData {
	
	BenchDataModel  benchData = new BenchDataModel();
	
	double diffPressure;
	
	double pressMontante;
	
	double tempMontante;
	
	double reserInfTemp;
	
	double reserSupTemp;
	
	public ViewData(BenchDataModel  benchData, double diffPressure, double pressMontante, double tempMontante, double reserInfTemp, double reserSupTemp ){
		
		this.benchData = benchData;
		this.diffPressure = diffPressure;
		this.pressMontante = pressMontante;
		this.tempMontante = tempMontante;
		this.reserInfTemp = reserInfTemp;
		this.reserSupTemp = reserSupTemp;
		
	}

	/**
	 * Function returns the value of attribute benchData
	 * @return the benchData
	 */
	public BenchDataModel getBenchData() {
		return benchData;
	}

	/**
	 * Function sets the value for attribute benchData
	 * @param benchData the benchData to set
	 */
	public void setBenchData(BenchDataModel benchData) {
		this.benchData = benchData;
	}

	/**
	 * Function returns the value of attribute diffPressure
	 * @return the diffPressure
	 */
	public double getDiffPressure() {
		return diffPressure;
	}

	/**
	 * Function sets the value for attribute diffPressure
	 * @param diffPressure the diffPressure to set
	 */
	public void setDiffPressure(double diffPressure) {
		this.diffPressure = diffPressure;
	}

	
	/**
	 * Function returns the value of attribute tempMontante
	 * @return the tempMontante
	 */
	public double getTempMontante() {
		return tempMontante;
	}

	/**
	 * Function sets the value for attribute tempMontante
	 * @param tempMontante the tempMontante to set
	 */
	public void setTempMontante(double tempMontante) {
		this.tempMontante = tempMontante;
	}

	/**
	 * Function returns the value of attribute reserInfTemp
	 * @return the reserInfTemp
	 */
	public double getReserInfTemp() {
		return reserInfTemp;
	}

	/**
	 * Function sets the value for attribute reserInfTemp
	 * @param reserInfTemp the reserInfTemp to set
	 */
	public void setReserInfTemp(double reserInfTemp) {
		this.reserInfTemp = reserInfTemp;
	}

	/**
	 * Function returns the value of attribute reserSupTemp
	 * @return the reserSupTemp
	 */
	public double getReserSupTemp() {
		return reserSupTemp;
	}

	/**
	 * Function sets the value for attribute reserSupTemp
	 * @param reserSupTemp the reserSupTemp to set
	 */
	public void setReserSupTemp(double reserSupTemp) {
		this.reserSupTemp = reserSupTemp;
	}

	/**
	 * Function returns the value of attribute pressMontante
	 * @return the pressMontante
	 */
	public double getPressMontante() {
		return pressMontante;
	}

	/**
	 * Function sets the value for attribute pressMontante
	 * @param pressMontante the pressMontante to set
	 */
	public void setPressMontante(double pressMontante) {
		this.pressMontante = pressMontante;
	}
	
	
	
}
