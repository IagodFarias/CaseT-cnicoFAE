//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CryptoMd5Digest.java
*    @author Marcos
*    @date 2 de abr de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package exceptions;

/**
 * @author Marcos
 *
 */
public class CryptoMd5Digest extends Exception  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1509247543200187205L;
	
	private String message;
	
	public CryptoMd5Digest(String message){
		this.message = message;
	}

	/**
	 * Function returns the value of attribute message
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Function sets the value for attribute message
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
