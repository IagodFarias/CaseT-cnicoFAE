//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file FlowRateStateEnum.java
*    @author Marcos Oliveira
*    @date 20 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.enumerations;

import java.util.ArrayList;

/**
 * @author Marcos Oliveira
 *
 */
public enum FlowRateStateEnum {

	STABLE,
	UNSTABLE,
	UNKNOWN;

	public static boolean toBoolean(FlowRateStateEnum flowRateState) {
		boolean retorno = false;
		switch (flowRateState) {
			case STABLE:
				retorno = true;
			break;
			case UNSTABLE:
				retorno = false;
			break;
			case UNKNOWN:
				retorno = false;
			break;
			default:
				retorno = false;
			break;
		}
		return retorno;
	}

	public static String toString(FlowRateStateEnum flowRateState) {
		String retorno = "";
		switch (flowRateState) {
			case STABLE:
				retorno = "STABLE";
			break;
			case UNSTABLE:
				retorno = "UNSTABLE";
			break;
			case UNKNOWN:
				retorno = "UNKNOWN";
			break;
			default:
				retorno = "UNKNOWN";
			break;
		}
		return retorno;
	}
	
	public static ArrayList<FlowRateStateEnum> getList() {
		ArrayList<FlowRateStateEnum> list = new ArrayList<>();
		list.add(FlowRateStateEnum.STABLE);
		list.add(FlowRateStateEnum.UNSTABLE);
		list.add(FlowRateStateEnum.UNKNOWN);
		return list;
	}
}
