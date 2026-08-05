//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ValveStateEnum.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.enumerations;

import java.util.ArrayList;

/**
 * @author Osmenio Santos
 *
 */
public enum CalibrationTypeEnum {

	FIXED_CONST(1, "Constantes fixas"),
	ESTIMATED_CONST(2, "Constantes estimadas"),
	FULL_PROD(3, "Batelada completa"),
	ONLY_VERIFICATION(4, "Apenas verificação"),
	INCOMPLETE(5, "Incompleta");

	int value;
	String name;

	/**
	*
	*/
	private CalibrationTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public static ArrayList<CalibrationTypeEnum> getList() {
		ArrayList<CalibrationTypeEnum> list = new ArrayList<>();
		list.add(CalibrationTypeEnum.FIXED_CONST);
		list.add(CalibrationTypeEnum.ESTIMATED_CONST);
		list.add(CalibrationTypeEnum.FULL_PROD);
		list.add(CalibrationTypeEnum.ONLY_VERIFICATION);
		list.add(CalibrationTypeEnum.INCOMPLETE);
		return list;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	public static CalibrationTypeEnum getCalibrationType(String name){
		CalibrationTypeEnum cte = null;
		switch (name) {
			case "FULL_PROD":
				cte = CalibrationTypeEnum.FULL_PROD;
				break;
			case "ESTIMATED_CONST":
				cte = CalibrationTypeEnum.ESTIMATED_CONST;
				break;
			case "FIXED_CONST":
				cte = CalibrationTypeEnum.FIXED_CONST;
			default:
				break;
		}
		
		return cte;
	}

	// public static String getStringError(CalibrationTypeEnum enumeration) {
	// String retorno = "";
	// switch (enumeration) {
	//
	// case FIXED_CONST:
	// retorno = "COMM";
	// break;
	//
	// case ESTIMATED_CONST:
	// retorno = "TRIM";
	// break;
	//
	// case FULL_PROD:
	// retorno = "DESV";
	// break;
	//
	// case ONLY_VERIFICATION:
	// retorno = "CALC";
	// break;
	// }
	// return retorno;
	// }
}
