//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CrudDatabaseException.java
*    @author marcos
*    @date 7 de abr de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.exceptions;

/**
 * @author marcos
 *
 */
public class CrudDatabaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6352022980870118956L;

	public CrudDatabaseException(String message) {
		super(message);
	}

	public CrudDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	// Other constructors as needed
}
