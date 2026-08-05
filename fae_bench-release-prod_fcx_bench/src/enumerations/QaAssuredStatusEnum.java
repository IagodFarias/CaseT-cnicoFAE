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

import java.util.ArrayList;

/**
 * @author marcos
 *
 */
public enum QaAssuredStatusEnum {

	// VIEW;

	// View
	RESINA_DISPLAY(101, "Resina display"),
	FALHA_DISPLAY(102, "Falha display"),
	DISPLAY_ARRANHADO(103, "Display arranhado"),
	BASE_COM_RESINA(104, "Base com resina"),
	SENSOR_OPTICO_RESINA(105, "Sensor optico com resina"),
	JTAG_RESINA(106, "JTAG com resina"),
	ROSCA_RESINA(107, "Rosca com resina"),
	FIOS_RESINA(108, "Fios com resina"),

	// Metrologico
	QMIN_POS(201, "Qmin +"),
	QMIN_NEG(202, "Qmin -"),
	QT_POS(203, "Qt +"),
	QT_NEG(204, "Qt -"),
	QN_POS(205, "Qn +"),
	QN_NEG(206, "Qn -"),
	MEDINDO_FLUXO_REVERSO(207, "Medidor fluxo reverso"),
	MEDINDO_FLUXO_LINHA_FECHADA(208, "Medidor fluxo linha fechada "),
	MEDIDOR_PARADO_LINHA_ABERTA(209, "Medidor parado linha aberta"),

	// Montagem
	CONDICIONADOR_FORA_POSICAO(301, "Condicionador fora posi��o"),
	TRANSDUTOR_CORTADO(302, "Transdutor cortado"),
	SNAP_DANIFICADO(303, "Snap danificado"),
	ESTANQUEIDADE(304, "Estanqueidade"),
	BATERIA_SEM_RESINA(305, "Bateria sem resina"),

	// Firmware
	FALHA_FIRMWARE(401, "Falha de firmware");

	// -------------------
	private int intValue;
	private String name;

	private QaAssuredStatusEnum(int intValue, String name) {
		this.intValue = intValue;
		this.name = name;
	}

	public static ArrayList<QaAssuredStatusEnum> getList() {
		ArrayList<QaAssuredStatusEnum> list = new ArrayList<>();
		list.add(QaAssuredStatusEnum.RESINA_DISPLAY);
		list.add(QaAssuredStatusEnum.FALHA_DISPLAY);
		list.add(QaAssuredStatusEnum.DISPLAY_ARRANHADO);
		list.add(QaAssuredStatusEnum.BASE_COM_RESINA);
		list.add(QaAssuredStatusEnum.SENSOR_OPTICO_RESINA);
		list.add(QaAssuredStatusEnum.JTAG_RESINA);
		list.add(QaAssuredStatusEnum.ROSCA_RESINA);
		list.add(QaAssuredStatusEnum.FIOS_RESINA);
		list.add(QaAssuredStatusEnum.QMIN_POS);
		list.add(QaAssuredStatusEnum.QMIN_NEG);
		list.add(QaAssuredStatusEnum.QT_POS);
		list.add(QaAssuredStatusEnum.QT_NEG);
		list.add(QaAssuredStatusEnum.QN_POS);
		list.add(QaAssuredStatusEnum.QN_NEG);
		list.add(QaAssuredStatusEnum.MEDINDO_FLUXO_REVERSO);
		list.add(QaAssuredStatusEnum.MEDINDO_FLUXO_LINHA_FECHADA);
		list.add(QaAssuredStatusEnum.MEDIDOR_PARADO_LINHA_ABERTA);
		list.add(QaAssuredStatusEnum.CONDICIONADOR_FORA_POSICAO);
		list.add(QaAssuredStatusEnum.TRANSDUTOR_CORTADO);
		list.add(QaAssuredStatusEnum.SNAP_DANIFICADO);
		list.add(QaAssuredStatusEnum.ESTANQUEIDADE);
		list.add(QaAssuredStatusEnum.BATERIA_SEM_RESINA);
		list.add(QaAssuredStatusEnum.FALHA_FIRMWARE);
		return list;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}
