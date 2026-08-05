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
public enum BenchAlarmEnum {

	NO_ALARM((byte) 0),
	EMERGENCY_ACTIVATED((byte) 1),
	LOW_AIR_PRESSURE((byte) 2),
	LOW_LEVEL_INFERIOR_RESERVOIR((byte) 4),
	THERMAL_BP1((byte) 8),
	THERMAL_BP2((byte) 16),
	THERMAL_BREP((byte) 32);

	private byte value;

	private BenchAlarmEnum(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return this.value;
	}

	public static boolean isEmergencyActivated(int alarms) {
		boolean flag = false;
		if ((alarms & 0x01) == 0x01) {
			flag = true;
		}
		return flag;
	}

	public static boolean isLowAirPressure(int alarms) {
		boolean flag = false;
		if ((alarms & 0x02) == 0x02) {
			flag = true;
		}
		return flag;
	}

	public static boolean isLowLevelInferiorReservoir(int alarms) {
		boolean flag = false;
		if ((alarms & 0x04) == 0x04) {
			flag = true;
		}
		return flag;
	}

	public static boolean isThermalBp1(int alarms) {
		boolean flag = false;
		if ((alarms & 0x08) == 0x08) {
			flag = true;
		}
		return flag;
	}

	public static boolean isThermalBp2(int alarms) {
		boolean flag = false;
		if ((alarms & 0x10) == 0x10) {
			flag = true;
		}
		return flag;
	}

	public static boolean isThermalBrep(int alarms) {
		boolean flag = false;
		if ((alarms & 0x20) == 0x20) {
			flag = true;
		}
		return flag;
	}
}
