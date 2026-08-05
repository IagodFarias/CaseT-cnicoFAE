//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ConversorNumbers.java
*    @author marcos
*    @date 12 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.util;

/**
 * @author marcos
 *
 */
public enum ConversorNumbers {
	CONVERSOR1,
	CONVERSOR2,
	CONVERSOR3,
	CONVERSOR4,
	CONVERSOR5,
	CONVERSOR6,
	CONVERSOR7,
	CONVERSOR8,
	CONVERSOR9,
	CONVERSOR10,
	CONVERSOR11,
	CONVERSOR12,
	CONVERSOR13,
	CONVERSOR14,
	CONVERSOR15,
	CONVERSOR16,
	CONVERSOR17,
	CONVERSOR18,
	CONVERSOR19,
	CONVERSOR20

	{
		@Override
		public ConversorNumbers next() {
			return null; // see below for options for this line
		};
	};

	public ConversorNumbers next() {
		// No bounds checking required here, because the last instance overrides
		return values()[ordinal() + 1];
	}

	public static String getStringValue(ConversorNumbers obj) {
		String retorno = "";
		switch (obj) {
			case CONVERSOR1:
				retorno = "CONVERSOR1";
			break;
			case CONVERSOR2:
				retorno = "CONVERSOR2";
			break;
			case CONVERSOR3:
				retorno = "CONVERSOR3";
			break;
			case CONVERSOR4:
				retorno = "CONVERSOR4";
			break;
			case CONVERSOR5:
				retorno = "CONVERSOR5";
			break;
			case CONVERSOR6:
				retorno = "CONVERSOR6";
			break;
			case CONVERSOR7:
				retorno = "CONVERSOR7";
			break;
			case CONVERSOR8:
				retorno = "CONVERSOR8";
			break;
			case CONVERSOR9:
				retorno = "CONVERSOR9";
			break;
			case CONVERSOR10:
				retorno = "CONVERSOR10";
			break;
			case CONVERSOR11:
				retorno = "CONVERSOR11";
			break;
			case CONVERSOR12:
				retorno = "CONVERSOR12";
			break;
			case CONVERSOR13:
				retorno = "CONVERSOR13";
			break;
			case CONVERSOR14:
				retorno = "CONVERSOR14";
			break;
			case CONVERSOR15:
				retorno = "CONVERSOR15";
			break;
			case CONVERSOR16:
				retorno = "CONVERSOR16";
			break;
			case CONVERSOR17:
				retorno = "CONVERSOR17";
			break;
			case CONVERSOR18:
				retorno = "CONVERSOR18";
			break;
			case CONVERSOR19:
				retorno = "CONVERSOR19";
			break;
			case CONVERSOR20:
				retorno = "CONVERSOR20";
			break;
			default:
				retorno = "";
			break;
		}

		return retorno;
	}

	public static int getIndexValue(ConversorNumbers obj) {
		int retorno = 0;
		switch (obj) {
			case CONVERSOR1:
				retorno = 0;
			break;
			case CONVERSOR2:
				retorno = 1;
			break;
			case CONVERSOR3:
				retorno = 2;
			break;
			case CONVERSOR4:
				retorno = 3;
			break;
			case CONVERSOR5:
				retorno = 4;
			break;
			case CONVERSOR6:
				retorno = 5;
			break;
			case CONVERSOR7:
				retorno = 6;
			break;
			case CONVERSOR8:
				retorno = 7;
			break;
			case CONVERSOR9:
				retorno = 8;
			break;
			case CONVERSOR10:
				retorno = 9;
			break;
			case CONVERSOR11:
				retorno = 10;
			break;
			case CONVERSOR12:
				retorno = 11;
			break;
			case CONVERSOR13:
				retorno = 12;
			break;
			case CONVERSOR14:
				retorno = 13;
			break;
			case CONVERSOR15:
				retorno = 14;
			break;
			case CONVERSOR16:
				retorno = 15;
			break;
			case CONVERSOR17:
				retorno = 16;
			break;
			case CONVERSOR18:
				retorno = 17;
			break;
			case CONVERSOR19:
				retorno = 18;
			break;
			case CONVERSOR20:
				retorno = 19;
			break;
		}
		return retorno;
	}

	public static ConversorNumbers getConversorfromIndex(int index) {
		switch(index){
			case 0:
				return ConversorNumbers.CONVERSOR1;
			case 1:
				return ConversorNumbers.CONVERSOR2;
			case 2:
				return ConversorNumbers.CONVERSOR3;
			case 3:
				return ConversorNumbers.CONVERSOR4;
			case 4:
				return ConversorNumbers.CONVERSOR5;
			case 5:
				return ConversorNumbers.CONVERSOR6;
			case 6:
				return ConversorNumbers.CONVERSOR7;
			case 7:
				return ConversorNumbers.CONVERSOR8;
			case 8:
				return ConversorNumbers.CONVERSOR9;
			case 9:
				return ConversorNumbers.CONVERSOR10;
			case 10:
				return ConversorNumbers.CONVERSOR11;
			case 11:
				return ConversorNumbers.CONVERSOR12;
			case 12:
				return ConversorNumbers.CONVERSOR13;
			case 13:
				return ConversorNumbers.CONVERSOR14;
			case 14:
				return ConversorNumbers.CONVERSOR15;
			case 15:
				return ConversorNumbers.CONVERSOR16;
			case 16:
				return ConversorNumbers.CONVERSOR17;
			case 17:
				return ConversorNumbers.CONVERSOR18;
			case 18:
				return ConversorNumbers.CONVERSOR19;
			case 19:
				return ConversorNumbers.CONVERSOR20;
			default:
				return null;
		}
	}
}

