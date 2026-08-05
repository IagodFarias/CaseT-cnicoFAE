//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PumpStateEnum.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package enumerations;

/**
 * @author Marcos Oliveira
 *
 */
public enum PumpStateEnum {
	// ON means the pump is RUNNING!!
	ON,
	// OFF means it is not running. These parameters do not mean that they are powered, but means they are running
	OFF,
	UNKNOWN,
	OVERLOAD;
}
