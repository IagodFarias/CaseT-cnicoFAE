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
import java.text.MessageFormat;
import java.util.Properties;

/**
 * @author Marcos Oliveira
 *
 */
public class TestMessageReaderUtil {
	
	
	static Properties prop = new Properties();
	InputStream input = null;
	
	static TestMessageReaderUtil INSTANCE = new TestMessageReaderUtil();
	
	public static TestMessageReaderUtil getInstance(){
		if(INSTANCE == null){
			INSTANCE = new TestMessageReaderUtil(); 
		}
		return INSTANCE;
	}

	public TestMessageReaderUtil(){
		try {
			input = new FileInputStream("tests/pt_br_messages.properties");
			
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
	 * Defines the property name that will be used to reference the 
	 * field value on the protocol frame
	 * @return
	 */

	
	/**
	 * Returns the string related to the field requested 
	 * @param field to find the respective property
	 * @return String with the field value
	 */
	public static String getProperty(String field){
		if(prop==null){
			getInstance();
		}
		return prop.getProperty(field);
	}
	
	/**
	 * Returns the string related to the field requested 
	 * @param field to find the respective property
	 * @return String with the field value
	 */
	public static String getPropertyWithParam(String field, Object... arguments){
		String mess = getProperty(field);
		String message = MessageFormat.format(mess, arguments);
		return message;
	}
}
