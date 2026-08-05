//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file DeviceTagReaderUtil.java
*    @author marcos
*    @date 19 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author marcos
 *
 */
public class DeviceTagReaderUtil {

	static Properties  prop = new Properties();
	static DeviceTagReaderUtil INSTANCE = new DeviceTagReaderUtil();
	InputStream input = null;

	public DeviceTagReaderUtil(){
		try {
			input = new FileInputStream("./devicesTagReference.properties");
			prop.load(input);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Returns the string related to the field requested 
	 * @param field to find the respective property
	 * @return String with the field value
	 */
	public static String getProperty(String field){
		return prop.getProperty(field);
	}
}
