//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file StandardProcessStatesEnum.java
*    @author marcos
*    @date 19 de ago de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

/**
 * @author marcos
 *
 */
public enum StandardProcessStatesEnum /* implements PropertyChangeListener */ {

	WAIT, // 0
	CLOSELINE, // 1
	PURGE, // 2
	CHECK_METERS_FOR_NAN, // 3
	DOWNLOAD_INIT_CALIB_VALUES, // 4
	INITIAL_RUN_CONFIGURATION, // 5
	INSERT_PRESSURE_LINE, // 6
	RUN_METERS_TRIM_AGC_STAGE2, // 7
	INITIATE_METERS_TRANSMITION, // 8
	STATIC_PRESSURE, // 9
	ZEROFLOW, // 10
	TEST_FLOWARTE, // 11
	CALCULATE_ZEROFLOW, // 12
	DOWNLOAD_DZ_DSOS, // 13
	FLOWRATE, // 14
	CALCULATE, // 15
	DOWNLOAD_PARAMETERS, // 16
	// ATT_SERIAL_NUMBER, // 17
	SAVE_METER_DATA_IN_DB, // 17
	UPDATE_METERS_SYSTEM_DATE, // 18
	CALIB_REFERENCE, // 19
	SAVE_ZEROFLOW_MAT_DATA, // 20
	SAVE_METER_CONFIG_MAT_DATA, // 21
	SAVE_MAT_DATA, // 22
	SAVE_K_Re_MAT_DATA, // 23
	SAVE_VERIF_ERRORS_MAT_DATA, // 24
	VERIF, // 25
	STOP_METERS_THREADS, // 26
	CALCULATE_ERRORS, // 27
	DISPLAY_REPORT, // 28
	SWITCH_METERS_TO_NORMAL, // 29
	SWITCH_METERS_TO_STAND_BY, // 30
	OPENLINE, // 31
	CLEAR_BUFFERS, // 32
	END_RUN_CONFIGURATIONS, // 33
	SAVE_CONST_MAT_DATA, // 34
	CHECK_VERIF, // 35
	SEND_SERIAL_NUMBER, // 36
	SEND_RADIO_WMBUS_CONFIG, // 37
	UPDATE_METERS_REPLACE_DATE, // 38
	SWITCH_METERS_TO_POST_CALIB, // 39
	SWITCH_METERS_TO_VERIF_QA, // 40
	SORT_TO_VERIF_QA, // 41
	DISPLAY_APPROVED_METERS, // 42
	SEND_METERS_SERIAL_NUMBER, // 43
	DOWN_FIXED_CONSTS, // 44
	CALC_FIXED_CONSTS, // 45
	SAVE_AFTER_ZERO_FLOW; // 46

	private int value;

	private StandardProcessStatesEnum(int value) {
		this.value = value;
	}

	private StandardProcessStatesEnum() {
	}

	public int getValue() {
		return this.value;
	}

	public static StandardProcessStatesEnum getEnum(int value) {

		StandardProcessStatesEnum state = StandardProcessStatesEnum.WAIT;
		switch (value) {
			case 0:
				state = StandardProcessStatesEnum.WAIT;
			break;

			case 1:
				state = StandardProcessStatesEnum.CLOSELINE;
			break;

			case 2:
				state = StandardProcessStatesEnum.PURGE;
			break;

			case 3:
				state = StandardProcessStatesEnum.CHECK_METERS_FOR_NAN;
			break;

			case 4:
				state = StandardProcessStatesEnum.DOWNLOAD_INIT_CALIB_VALUES;
			break;

			case 5:
				state = StandardProcessStatesEnum.INITIAL_RUN_CONFIGURATION;
			break;

			case 6:
				state = StandardProcessStatesEnum.INSERT_PRESSURE_LINE;
			break;

			case 7:
				state = StandardProcessStatesEnum.RUN_METERS_TRIM_AGC_STAGE2;
			break;

			case 8:
				state = StandardProcessStatesEnum.INITIATE_METERS_TRANSMITION;
			break;

			case 9:
				state = StandardProcessStatesEnum.STATIC_PRESSURE;
			break;

			case 10:
				state = StandardProcessStatesEnum.ZEROFLOW;
			break;

			case 11:
				state = StandardProcessStatesEnum.TEST_FLOWARTE;
			break;

			case 12:
				state = StandardProcessStatesEnum.CALCULATE_ZEROFLOW;
			break;

			case 13:
				state = StandardProcessStatesEnum.DOWNLOAD_DZ_DSOS;
			break;

			case 14:
				state = StandardProcessStatesEnum.FLOWRATE;
			break;

			case 15:
				state = StandardProcessStatesEnum.CALCULATE;
			break;

			case 16:
				state = StandardProcessStatesEnum.DOWNLOAD_PARAMETERS;
			break;

			case 17:
				state = StandardProcessStatesEnum.SAVE_METER_DATA_IN_DB;
			// state = StandardProcessStatesEnum.ATT_SERIAL_NUMBER;
			break;

			case 18:
				state = StandardProcessStatesEnum.UPDATE_METERS_SYSTEM_DATE;
			break;

			case 19:
				state = StandardProcessStatesEnum.CALIB_REFERENCE;
			break;

			case 20:
				state = StandardProcessStatesEnum.SAVE_ZEROFLOW_MAT_DATA;
			break;

			case 21:
				state = StandardProcessStatesEnum.SAVE_METER_CONFIG_MAT_DATA;
			break;

			case 22:
				state = StandardProcessStatesEnum.SAVE_MAT_DATA;
			break;

			case 23:
				state = StandardProcessStatesEnum.SAVE_K_Re_MAT_DATA;
			break;

			case 24:
				state = StandardProcessStatesEnum.SAVE_VERIF_ERRORS_MAT_DATA;
			break;

			case 25:
				state = StandardProcessStatesEnum.VERIF;
			break;

			case 26:
				state = StandardProcessStatesEnum.STOP_METERS_THREADS;
			break;

			case 27:
				state = StandardProcessStatesEnum.CALCULATE_ERRORS;
			break;

			case 28:
				state = StandardProcessStatesEnum.DISPLAY_REPORT;
			break;

			case 29:
				state = StandardProcessStatesEnum.SWITCH_METERS_TO_NORMAL;
			break;

			case 30:
				state = StandardProcessStatesEnum.SWITCH_METERS_TO_STAND_BY;
			break;

			case 31:
				state = StandardProcessStatesEnum.OPENLINE;
			break;

			case 32:
				state = StandardProcessStatesEnum.CLEAR_BUFFERS;
			break;

			case 33:
				state = StandardProcessStatesEnum.END_RUN_CONFIGURATIONS;
			break;

			case 34:
				state = StandardProcessStatesEnum.SAVE_CONST_MAT_DATA;
			break;

			case 35:
				state = StandardProcessStatesEnum.CHECK_VERIF;
			break;

			case 36:
				state = StandardProcessStatesEnum.SEND_SERIAL_NUMBER;
			break;

			case 37:
				state = StandardProcessStatesEnum.SEND_RADIO_WMBUS_CONFIG;
			break;

			case 38:
				state = StandardProcessStatesEnum.UPDATE_METERS_REPLACE_DATE;
			break;

			case 39:
				state = StandardProcessStatesEnum.SWITCH_METERS_TO_POST_CALIB;
			break;

			case 40:
				state = StandardProcessStatesEnum.SWITCH_METERS_TO_VERIF_QA;
			break;

			case 41:
				state = StandardProcessStatesEnum.SORT_TO_VERIF_QA;
			break;

			case 42:
				state = StandardProcessStatesEnum.DISPLAY_APPROVED_METERS;
			break;

			case 43:
				state = StandardProcessStatesEnum.SEND_METERS_SERIAL_NUMBER;
			break;

			case 44:
				state = StandardProcessStatesEnum.DOWN_FIXED_CONSTS;
			break;

			case 45:
				state = StandardProcessStatesEnum.CALC_FIXED_CONSTS;
			break;

			case 46:
				state = StandardProcessStatesEnum.SAVE_AFTER_ZERO_FLOW;
			break;

		}
		return state;
	}

	//
	public static StandardProcessStatesEnum getEnum(String name) {

		StandardProcessStatesEnum state = StandardProcessStatesEnum.WAIT;
		switch (name) {
			case "WAIT":
				state = StandardProcessStatesEnum.WAIT;
			break;

			case "CLOSELINE":
				state = StandardProcessStatesEnum.CLOSELINE;
			break;

			case "PURGE":
				state = StandardProcessStatesEnum.PURGE;
			break;

			case "CHECK_METERS_FOR_NAN":
				state = StandardProcessStatesEnum.CHECK_METERS_FOR_NAN;
			break;

			case "DOWNLOAD_INIT_CALIB_VALUES":
				state = StandardProcessStatesEnum.DOWNLOAD_INIT_CALIB_VALUES;
			break;

			case "INITIAL_RUN_CONFIGURATION":
				state = StandardProcessStatesEnum.INITIAL_RUN_CONFIGURATION;
			break;

			case "INSERT_PRESSURE_LINE":
				state = StandardProcessStatesEnum.INSERT_PRESSURE_LINE;
			break;

			case "RUN_METERS_TRIM_AGC_STAGE2":
				state = StandardProcessStatesEnum.RUN_METERS_TRIM_AGC_STAGE2;
			break;

			case "INITIATE_METERS_TRANSMITION":
				state = StandardProcessStatesEnum.INITIATE_METERS_TRANSMITION;
			break;

			case "STATIC_PRESSURE":
				state = StandardProcessStatesEnum.STATIC_PRESSURE;
			break;

			case "ZEROFLOW":
				state = StandardProcessStatesEnum.ZEROFLOW;
			break;

			case "TEST_FLOWARTE":
				state = StandardProcessStatesEnum.TEST_FLOWARTE;
			break;

			case "CALCULATE_ZEROFLOW":
				state = StandardProcessStatesEnum.CALCULATE_ZEROFLOW;
			break;

			case "DOWNLOAD_DZ_DSOS":
				state = StandardProcessStatesEnum.DOWNLOAD_DZ_DSOS;
			break;

			case "FLOWRATE":
				state = StandardProcessStatesEnum.FLOWRATE;
			break;

			case "CALCULATE":
				state = StandardProcessStatesEnum.CALCULATE;
			break;

			case "DOWNLOAD_PARAMETERS":
				state = StandardProcessStatesEnum.DOWNLOAD_PARAMETERS;
			break;

			case "SAVE_METER_DATA_IN_DB":
				// state = StandardProcessStatesEnum.ATT_SERIAL_NUMBER;
				state = StandardProcessStatesEnum.SAVE_METER_DATA_IN_DB;
			break;

			case "UPDATE_METERS_SYSTEM_DATE":
				state = StandardProcessStatesEnum.UPDATE_METERS_SYSTEM_DATE;
			break;

			case "CALIB_REFERENCE":
				state = StandardProcessStatesEnum.CALIB_REFERENCE;
			break;

			case "SAVE_ZEROFLOW_MAT_DATA":
				state = StandardProcessStatesEnum.SAVE_ZEROFLOW_MAT_DATA;
			break;

			case "SAVE_METER_CONFIG_MAT_DATA":
				state = StandardProcessStatesEnum.SAVE_METER_CONFIG_MAT_DATA;
			break;

			case "SAVE_MAT_DATA":
				state = StandardProcessStatesEnum.SAVE_MAT_DATA;
			break;

			case "SAVE_K_Re_MAT_DATA":
				state = StandardProcessStatesEnum.SAVE_K_Re_MAT_DATA;
			break;

			case "SAVE_VERIF_ERRORS_MAT_DATA":
				state = StandardProcessStatesEnum.SAVE_VERIF_ERRORS_MAT_DATA;
			break;

			case "VERIF":
				state = StandardProcessStatesEnum.VERIF;
			break;

			case "STOP_METERS_THREADS":
				state = StandardProcessStatesEnum.STOP_METERS_THREADS;
			break;

			case "CALCULATE_ERRORS":
				state = StandardProcessStatesEnum.CALCULATE_ERRORS;
			break;

			case "DISPLAY_REPORT":
				state = StandardProcessStatesEnum.DISPLAY_REPORT;
			break;

			case "SWITCH_METERS_TO_NORMAL":
				state = StandardProcessStatesEnum.SWITCH_METERS_TO_NORMAL;
			break;

			case "SWITCH_METERS_TO_STAND_BY":
				state = StandardProcessStatesEnum.SWITCH_METERS_TO_STAND_BY;
			break;

			case "OPENLINE":
				state = StandardProcessStatesEnum.OPENLINE;
			break;

			case "CLEAR_BUFFERS":
				state = StandardProcessStatesEnum.CLEAR_BUFFERS;
			break;

			case "END_RUN_CONFIGURATIONS":
				state = StandardProcessStatesEnum.END_RUN_CONFIGURATIONS;
			break;

			case "SAVE_CONST_MAT_DATA":
				state = StandardProcessStatesEnum.SAVE_CONST_MAT_DATA;
			break;

			case "CHECK_VERIF":
				state = StandardProcessStatesEnum.CHECK_VERIF;
			break;

			case "SEND_SERIAL_NUMBER":
				state = StandardProcessStatesEnum.SEND_SERIAL_NUMBER;
			break;

			case "SEND_RADIO_WMBUS_CONFIG":
				state = StandardProcessStatesEnum.SEND_RADIO_WMBUS_CONFIG;
			break;

			case "UPDATE_METERS_REPLACE_DATE":
				state = StandardProcessStatesEnum.UPDATE_METERS_REPLACE_DATE;
			break;

			case "SWITCH_METERS_TO_POST_CALIB":
				state = StandardProcessStatesEnum.SWITCH_METERS_TO_POST_CALIB;
			break;

			case "SWITCH_METERS_TO_VERIF_QA":
				state = StandardProcessStatesEnum.SWITCH_METERS_TO_VERIF_QA;
			break;

			case "SORT_TO_VERIF_QA":
				state = StandardProcessStatesEnum.SORT_TO_VERIF_QA;
			break;

			case "DISPLAY_APPROVED_METERS":
				state = StandardProcessStatesEnum.DISPLAY_APPROVED_METERS;
			break;

			case "SEND_METERS_SERIAL_NUMBER":
				state = StandardProcessStatesEnum.SEND_METERS_SERIAL_NUMBER;
			break;

			case "CALC_FIXED_CONSTS":
				state = StandardProcessStatesEnum.CALC_FIXED_CONSTS;
			break;

			case "DOWN_FIXED_CONSTS":
				state = StandardProcessStatesEnum.DOWN_FIXED_CONSTS;
			break;

			case "SAVE_AFTER_ZERO_FLOW":
				state = StandardProcessStatesEnum.SAVE_AFTER_ZERO_FLOW;
			break;

		}
		return state;
	}
}
