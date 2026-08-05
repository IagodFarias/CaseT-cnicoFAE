//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterCommStateEnum.java
*    @author marcos
*    @date 6 de abr de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

/**
 * @author marcos
 *
 */
public enum MeterCommStateEnum {

	LOAD_CALIB_PARAM,
	READ_METER,
	ENABLE_DATA_TRANSMIT,
	DISABLE_DATA_TRANSMIT,
	TRIM_AG_STAGE2,
	LOAD_STANDBY_OPMODE,
	LOAD_PRE_CALIB_OPMODE,
	LOAD_CALIB_OPMODE,
	LOAD_POST_CALIB_OPMODE,
	CHECK_TRASNMITION,
	WAIT_FOR_CONECTION;
	
	
}
