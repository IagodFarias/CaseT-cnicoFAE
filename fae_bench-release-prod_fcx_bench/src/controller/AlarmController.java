//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file AlarmController.java
*    @author marcos
*    @date 10 de dez de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package controller;

import bciapi.impl.BenchControlImpl;

/**
 * @author marcos
 *
 */
public class AlarmController {

	private static AlarmController INSTANCE = new AlarmController();
	
	private int alarm;
	
	private boolean emgActivated = false;
	
	private boolean lowAirPressure = false;
	
	private boolean lowLevelinfReserv = false;
	
	private boolean thermalBp1 = false;
	
	private boolean thermalBp2 = false;
	
	private boolean thermaBrep = false;
	

	public static AlarmController getInstance(){
		return INSTANCE;
	}
	
	
	public void readAlarmsFromBci(){
		BenchControlImpl bcimpl = BenchControlImpl.getInstance();
		
		alarm = bcimpl.getAlarms();
		if(alarm>0){
			if((alarm & 0x01)>0){
				emgActivated = true;
			}
			if((alarm & 0x02)>0){
				lowAirPressure = true;
			}
			if((alarm & 0x04)>0){
				lowLevelinfReserv = true;
			}
			if((alarm & 0x08)>0){
				thermalBp1 = true;
			}
			if((alarm & 0x10)>0){
				thermalBp2 = true;
			}
			if((alarm & 0x20)>0){
				thermaBrep = true;
			}
//			if((alarm & 0xBF)>0){
//				emgActivated = true;
//			}
//			if((alarm & 0x7F)>0){
//				emgActivated = true;
//			}
		}else{
			emgActivated = false;
			lowAirPressure = false;
			lowLevelinfReserv = false;
			thermalBp1 = false;
			thermalBp2 = false;
			thermaBrep = false;
		}
		
	}

	/**
	 * Function returns the value of attribute alarm
	 * @return the alarm
	 */
	public int getAlarm() {
		return alarm;
	}

	/**
	 * Function sets the value for attribute alarm
	 * @param alarm the alarm to set
	 */
	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	/**
	 * Function returns the value of attribute emgActivated
	 * @return the emgActivated
	 */
	public boolean isEmgActivated() {
		return emgActivated;
	}

	/**
	 * Function sets the value for attribute emgActivated
	 * @param emgActivated the emgActivated to set
	 */
	public void setEmgActivated(boolean emgActivated) {
		this.emgActivated = emgActivated;
	}

	/**
	 * Function returns the value of attribute lowAirPressure
	 * @return the lowAirPressure
	 */
	public boolean isLowAirPressure() {
		return lowAirPressure;
	}

	/**
	 * Function sets the value for attribute lowAirPressure
	 * @param lowAirPressure the lowAirPressure to set
	 */
	public void setLowAirPressure(boolean lowAirPressure) {
		this.lowAirPressure = lowAirPressure;
	}

	/**
	 * Function returns the value of attribute lowLevelinfReserv
	 * @return the lowLevelinfReserv
	 */
	public boolean isLowLevelinfReserv() {
		return lowLevelinfReserv;
	}

	/**
	 * Function sets the value for attribute lowLevelinfReserv
	 * @param lowLevelinfReserv the lowLevelinfReserv to set
	 */
	public void setLowLevelinfReserv(boolean lowLevelinfReserv) {
		this.lowLevelinfReserv = lowLevelinfReserv;
	}

	/**
	 * Function returns the value of attribute thermalBp1
	 * @return the thermalBp1
	 */
	public boolean isThermalBp1() {
		return thermalBp1;
	}

	/**
	 * Function sets the value for attribute thermalBp1
	 * @param thermalBp1 the thermalBp1 to set
	 */
	public void setThermalBp1(boolean thermalBp1) {
		this.thermalBp1 = thermalBp1;
	}

	/**
	 * Function returns the value of attribute thermalBp2
	 * @return the thermalBp2
	 */
	public boolean isThermalBp2() {
		return thermalBp2;
	}

	/**
	 * Function sets the value for attribute thermalBp2
	 * @param thermalBp2 the thermalBp2 to set
	 */
	public void setThermalBp2(boolean thermalBp2) {
		this.thermalBp2 = thermalBp2;
	}

	/**
	 * Function returns the value of attribute thermaBrep
	 * @return the thermaBrep
	 */
	public boolean isThermaBrep() {
		return thermaBrep;
	}

	/**
	 * Function sets the value for attribute thermaBrep
	 * @param thermaBrep the thermaBrep to set
	 */
	public void setThermaBrep(boolean thermaBrep) {
		this.thermaBrep = thermaBrep;
	}
	
	
	
	
	
}
