//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file DateFormaterUtil.java
*    @author marcos
*    @date 13 de ago de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

import java.text.SimpleDateFormat;

/**
 * @author marcos
 *
 */
public class DateFormaterUtil {
	
	private static String formatString =  PropertiesReaderUtil.getProperty("date.format");
	
	static SimpleDateFormat dtFormat = new SimpleDateFormat(formatString);
	
	public DateFormaterUtil(){
	}

	/**
	 * Function returns the value of attribute formatString
	 * @return the formatString
	 */
	public static String getFormat() {
		return formatString;
	}

	/**
	 * Function sets the value for attribute formatString
	 * @param formatString the formatString to set
	 */
	public static void setFormat(String format) {
		DateFormaterUtil.formatString = format;
	}

	/**
	 * Function returns the value of attribute dtFormat
	 * @return the dtFormat
	 */
	public static SimpleDateFormat getDtFormat() {
		return dtFormat;
	}

	/**
	 * Function sets the value for attribute dtFormat
	 * @param dtFormat the dtFormat to set
	 */
	public static void setDtFormat(SimpleDateFormat dtFormat) {
		DateFormaterUtil.dtFormat = dtFormat;
	}
	
	
	
}
