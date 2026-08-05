//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CalibrationErrorEnum.java
*    @author Marcos
*    @date 6 de abr de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package enumerations;

/**
 * @author Marcos
 *
 */
public enum CalibrationErrorEnum {

	COMM,
	INIT_DOWN,
	TRIM,
	DESV,
	CALC,
	DOWN,
	DWZERO,
	CALC_VERIF,
	VERIF,
	RFCONF,
	SYSDATE,
	REPDATE,
	SAVE_ERR,
	CALC_ZERO;

	public static String getStringError(CalibrationErrorEnum enumeration) {
		String retorno = "";
		switch (enumeration) {
			case COMM:
				retorno = "COMM";
			break;
			case INIT_DOWN:
				retorno = "INIT_DOWN";
			break;
			case TRIM:
				retorno = "TRIM";
			break;
			case DESV:
				retorno = "DESV";
			break;
			case CALC:
				retorno = "CALC";
			break;
			case DOWN:
				retorno = "DOWN";
			break;
			case DWZERO:
				retorno = "DWZERO";
			break;
			case CALC_VERIF:
				retorno = "CALC_VERIF";
			break;
			case VERIF:
				retorno = "VERIF";
			break;
			case RFCONF:
				retorno = "RFCONF";
			break;
			case SYSDATE:
				retorno = "SYSDATE";
			break;
			case REPDATE:
				retorno = "REPDATE";
			break;
			case SAVE_ERR:
				retorno = "SAVE_ERR";
			break;
			case CALC_ZERO:
				retorno = "CALC_ZERO";
			break;
		}
		return retorno;

	}
}
