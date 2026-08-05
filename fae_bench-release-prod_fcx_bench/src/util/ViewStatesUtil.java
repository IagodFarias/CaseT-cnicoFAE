//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ViewStatesUtil.java
*    @author marcos
*    @date 11 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

import enumerations.MeterConnectionStatus;
import si.dbcomm.util.ConversorNumbers;

/**
 * @author marcos
 *
 */
public class ViewStatesUtil {

	public static MainMachineStateEnum mainStates;

	public static boolean setupComplete = false;

	public static boolean minMeter = false;

	public static boolean bciConected = false;

	public static int meterLeft = 0;

	// public static byte[] arrayMinVersion = { 2, 0, 0, 0, 0, 0, 0, 0 };
	public static byte[] arrayMinVersion = { 1, 0, 0, 0, 8, 0, 1, 0 };

	//
	// public static ProcessController processController = null;

	public static ConversorNumbers conversorNum;
	
	public static MeterConnectionStatus resultConnectSingleMeter = MeterConnectionStatus.DISCONNECTED;

	static {
		mainStates = MainMachineStateEnum.WAIT;
	}
}
