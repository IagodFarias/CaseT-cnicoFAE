//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file Utils.java
*    @author osmenio
*    @date 18 de out de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author osmenio
 *
 */
public class Utilities {

	private static NumberFormat numberFormat = new DecimalFormat("#0.000");

	/**
	 * @return the readoutsDateFormat
	 */
	public static NumberFormat getDoubleFormat() {
		return numberFormat;
	}

	public static String parseVersionToString(byte[] array) {
		int MASK = 0xFF;
		String version = "";

		int value1 = ((array[1] & MASK) << 8) + array[0];
		int value2 = ((array[3] & MASK) << 8) + array[2];
		int value3 = ((array[5] & MASK) << 8) + array[4];
		int value4 = ((array[7] & MASK) << 8) + array[6];

		version = "" + value1 + "." + value2 + "." + value3+ "." + value4;
		return version;
	}
	
	public static long parseVersionToLong(String version) {
//		long value = 0;
//		String array = version.replace(".", "");
		
		long value = Long.valueOf(version.replace(".", ""));
		return value;
	}
}
