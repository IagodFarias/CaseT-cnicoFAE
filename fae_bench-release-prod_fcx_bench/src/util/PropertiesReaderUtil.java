//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PropertiesReaderUtil.java
*    @author Marcos Oliveira
*    @date 12 de mai de 2016
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
 * @author Marcos Oliveira
 *
 */
public class PropertiesReaderUtil {
	
	
	private static Properties prop = new Properties();
	private static InputStream input = null;

	static{
		try {
			input = new FileInputStream("./protocolFields.properties");
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
