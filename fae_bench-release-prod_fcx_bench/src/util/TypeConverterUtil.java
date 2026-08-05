//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file TypeConverterUtil.java
*    @author Marcos
*    @date 29 de mar de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

/**
 * @author Marcos
 *
 */
public class TypeConverterUtil {
	
	public static byte stringDecToHex(char chara){
		byte retorno = 0;
		
		switch(chara){
			case '0':
				retorno = 0x0;
				break;
			case '1':
				retorno = 0x1;
				break;
			case '2':
				retorno = 0x2;
				break;
			case '3':
				retorno = 0x3;
				break;
			case '4':
				retorno = 0x4;
				break;
			case '5':
				retorno = 0x5;
				break;
			case '6':
				retorno = 0x6;
				break;
			case '7':
				retorno = 0x7;
				break;
			case '8':
				retorno = 0x8;
				break;
			case '9':
				retorno = 0x9;
				break;
		}
		return retorno;
	}
	
	public static String arrayBytesToStringHex(byte[] dados){
		String retorno = "";
		for(int i = 0; i < dados.length; i++){
			
		}
		
		return retorno;
	}
	
}
