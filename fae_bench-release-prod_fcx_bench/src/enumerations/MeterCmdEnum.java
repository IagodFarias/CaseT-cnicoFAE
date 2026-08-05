//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterCmdEnum.java
*    @author Marcos
*    @date 3 de abr de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package enumerations;

/**
 * @author Marcos
 *
 */
public enum MeterCmdEnum {

	CMD_ACK(0xF1),
	CMD_NACK(0xF2),
	CMD_READ_FIRMWARE_VERSION(0x01),
	CMD_READ_SERIAL_NUMBER(0x02),
	CMD_WRITE_SERIAL_NUMBER(0x03),
	CMD_READ_REPLACEMENT_DATE(0x04),
	CMD_WRITE_REPLACEMENT_DATE(0x05),
	CMD_LOAD_PRE_CALIB_OPMODE(0x10),
	CMD_LOAD_CALIB_OPMODE(0x11),
	CMD_LOAD_POST_CALIB_OPMODE(0x12),
	CMD_LOAD_STAND_BY_OPMODE(0x13),
	CMD_LOAD_NORMAL_OPMODE(0x14),
	CMD_LOAD_VERIF_OPMODE(0x15),
	CMD_ENABLE_DATA_TRANSMIT(0x21),
	CMD_DISABLE_DATA_TRANSMIT(0x22),
	CMD_TRIM_AG_STAGE_2(0x23),
	CMD_LOAD_CALIB_PARAMETERS(0xAA),
	CMD_READ_CONFIG_PARAMETERS(0xBB),
	CMD_WRITE_CONFIG_PARAMETERS(0xCC),
	CMD_UPDATE_DATE(0xD0),
	CMD_READ_DATE(0xD1),
	CMD_INVALID(0x00),
	CMD_WRITE_WMBUS_CONFIG(0xD3),
	CMD_READ_WMBUS_CONFIG(0xD4);

	byte value;

	MeterCmdEnum(int value) {
		this.value = (byte) value;
	}

	public byte getValue() {
		return value;
	}

	public static MeterCmdEnum toEnum(byte dado) {
		MeterCmdEnum retorno = null;

		switch (dado) {
			case (byte) 0xF1:
				retorno = CMD_ACK;
			break;
			case (byte) 0xF2:
				retorno = CMD_NACK;
			break;
			case (byte) 0x01:
				retorno = CMD_READ_FIRMWARE_VERSION;
			break;
			case (byte) 0x02:
				retorno = CMD_READ_SERIAL_NUMBER;
			break;
			case (byte) 0x03:
				retorno = CMD_WRITE_SERIAL_NUMBER;
			break;
			case (byte) 0x04:
				retorno = CMD_READ_REPLACEMENT_DATE;
			break;
			case (byte) 0x05:
				retorno = CMD_WRITE_REPLACEMENT_DATE;
			break;
			case (byte) 0x10:
				retorno = CMD_LOAD_PRE_CALIB_OPMODE;
			break;
			case (byte) 0x11:
				retorno = CMD_LOAD_CALIB_OPMODE;
			break;
			case (byte) 0x12:
				retorno = CMD_LOAD_POST_CALIB_OPMODE;
			break;
			case (byte) 0x13:
				retorno = CMD_LOAD_STAND_BY_OPMODE;
			break;
			case (byte) 0x14:
				retorno = CMD_LOAD_NORMAL_OPMODE;
			break;
			case (byte) 0x15:
				retorno = CMD_LOAD_VERIF_OPMODE;
			break;
			case (byte) 0x21:
				retorno = CMD_ENABLE_DATA_TRANSMIT;
			break;
			case (byte) 0x22:
				retorno = CMD_DISABLE_DATA_TRANSMIT;
			break;
			case (byte) 0x23:
				retorno = CMD_TRIM_AG_STAGE_2;
			break;
			case (byte) 0xAA:
				retorno = CMD_LOAD_CALIB_PARAMETERS;
			break;
			case (byte) 0xBB:
				retorno = CMD_READ_CONFIG_PARAMETERS;
			break;
			case (byte) 0xCC:
				retorno = CMD_WRITE_CONFIG_PARAMETERS;
			break;
			case (byte) 0xD0:
				retorno = CMD_UPDATE_DATE;
			break;
			case (byte) 0xD1:
				retorno = CMD_READ_DATE;
			break;
			case (byte) 0x00:
				retorno = CMD_INVALID;
			break;
			case (byte) 0xD3:
				retorno = CMD_WRITE_WMBUS_CONFIG;
			break;
			case (byte) 0xD4:
				retorno = CMD_READ_WMBUS_CONFIG;
			break;
		}
		return retorno;

	}
}
