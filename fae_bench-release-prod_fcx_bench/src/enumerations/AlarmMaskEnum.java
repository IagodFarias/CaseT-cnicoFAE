//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file AlarmMaskEnum.java
*    @author marcos
*    @date 10 de dez de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package enumerations;

/**
 * @author marcos
 *
 */
public enum AlarmMaskEnum {
	
	MASK_BIT0((byte)0xFE),
	MASK_BIT1((byte)0xFD),
	MASK_BIT2((byte)0xFB),
	MASK_BIT3((byte)0xF7),
	MASK_BIT4((byte)0xEF),
	MASK_BIT5((byte)0xDF),
	MASK_BIT6((byte)0xBF),
	MASK_BIT7((byte)0x7F);

	private byte mask;
	
	private AlarmMaskEnum(byte mask){
		this.mask = mask;
	}
	
	public byte getMask(){
		return this.mask;
	}
	
}
