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
public enum UFEnum {

	AC(0),
	AL(1),
	AM(2),
	AP(3),
	BA(4),
	CE(5),
	DF(6),
	ES(7),
	GO(8),
	MA(9),
	MG(10),
	MS(11),
	MT(12),
	PA(13),
	PB(14),
	PE(15),
	PI(16),
	PR(17),
	RJ(18),
	RN(19),
	RO(20),
	RR(21),
	RS(22),
	SC(23),
	SE(24),
	SP(25),
	TO(26),
	ERROR(100);

	private int uf;

	private UFEnum(int uf) {
		this.uf = uf;
	}

	public int getValue() {
		return this.uf;
	}

	public String getName() {
		String ufEnum = "ERROR";

		switch (uf) {

			case 0:
				ufEnum = "AC";
			break;

			case 1:
				ufEnum = "AL";
			break;

			case 2:
				ufEnum = "AM";
			break;

			case 3:
				ufEnum = "AP";
			break;

			case 4:
				ufEnum = "BA";
			break;

			case 5:
				ufEnum = "CE";
			break;

			case 6:
				ufEnum = "DF";
			break;

			case 7:
				ufEnum = "ES";
			break;

			case 8:
				ufEnum = "GO";
			break;

			case 9:
				ufEnum = "MA";
			break;

			case 10:
				ufEnum = "MG";
			break;

			case 11:
				ufEnum = "MS";
			break;

			case 12:
				ufEnum = "MT";
			break;

			case 13:
				ufEnum = "PA";
			break;

			case 14:
				ufEnum = "PB";
			break;

			case 15:
				ufEnum = "PE";
			break;

			case 16:
				ufEnum = "PI";
			break;

			case 17:
				ufEnum = "PR";
			break;

			case 18:
				ufEnum = "RJ";
			break;

			case 19:
				ufEnum = "RN";
			break;

			case 20:
				ufEnum = "RO";
			break;

			case 21:
				ufEnum = "RR";
			break;

			case 22:
				ufEnum = "RS";
			break;

			case 23:
				ufEnum = "SC";
			break;

			case 24:
				ufEnum = "SE";
			break;

			case 25:
				ufEnum = "SP";
			break;

			case 26:
				ufEnum = "TO";
			break;

		}
		return ufEnum;
	}

	public static UFEnum getEnum(int uf) {
		UFEnum ufEnum = ERROR;
		switch (uf) {

			case 0:
				ufEnum = AC;
			break;

			case 1:
				ufEnum = AL;
			break;

			case 2:
				ufEnum = AM;
			break;

			case 3:
				ufEnum = AP;
			break;

			case 4:
				ufEnum = BA;
			break;

			case 5:
				ufEnum = CE;
			break;

			case 6:
				ufEnum = DF;
			break;

			case 7:
				ufEnum = ES;
			break;

			case 8:
				ufEnum = GO;
			break;

			case 9:
				ufEnum = MA;
			break;

			case 10:
				ufEnum = MG;
			break;

			case 11:
				ufEnum = MS;
			break;

			case 12:
				ufEnum = MT;
			break;

			case 13:
				ufEnum = PA;
			break;

			case 14:
				ufEnum = PB;
			break;

			case 15:
				ufEnum = PE;
			break;

			case 16:
				ufEnum = PI;
			break;

			case 17:
				ufEnum = PR;
			break;

			case 18:
				ufEnum = RJ;
			break;

			case 19:
				ufEnum = RN;
			break;

			case 20:
				ufEnum = RO;
			break;

			case 21:
				ufEnum = RR;
			break;

			case 22:
				ufEnum = RS;
			break;

			case 23:
				ufEnum = SC;
			break;

			case 24:
				ufEnum = SE;
			break;

			case 25:
				ufEnum = SP;
			break;

			case 26:
				ufEnum = TO;
			break;

		}
		return ufEnum;
	}

	public static UFEnum getEnum(String uf) {
		UFEnum ufEnum = ERROR;

		if (uf.equals(UFEnum.AC.getName())) {
			ufEnum = AC;
		} else if (uf.equals(UFEnum.AL.getName())) {
			ufEnum = AL;
		} else if (uf.equals(UFEnum.AM.getName())) {
			ufEnum = AM;
		} else if (uf.equals(UFEnum.AP.getName())) {
			ufEnum = AP;
		} else if (uf.equals(UFEnum.BA.getName())) {
			ufEnum = BA;
		} else if (uf.equals(UFEnum.CE.getName())) {
			ufEnum = CE;
		} else if (uf.equals(UFEnum.DF.getName())) {
			ufEnum = DF;
		} else if (uf.equals(UFEnum.ES.getName())) {
			ufEnum = ES;
		} else if (uf.equals(UFEnum.GO.getName())) {
			ufEnum = GO;
		} else if (uf.equals(UFEnum.MA.getName())) {
			ufEnum = MA;
		} else if (uf.equals(UFEnum.MG.getName())) {
			ufEnum = MG;
		} else if (uf.equals(UFEnum.MS.getName())) {
			ufEnum = MS;
		} else if (uf.equals(UFEnum.MT.getName())) {
			ufEnum = MT;
		} else if (uf.equals(UFEnum.PA.getName())) {
			ufEnum = PA;
		} else if (uf.equals(UFEnum.PB.getName())) {
			ufEnum = PB;
		} else if (uf.equals(UFEnum.PE.getName())) {
			ufEnum = PE;
		} else if (uf.equals(UFEnum.PI.getName())) {
			ufEnum = PI;
		} else if (uf.equals(UFEnum.PR.getName())) {
			ufEnum = PR;
		} else if (uf.equals(UFEnum.RJ.getName())) {
			ufEnum = RJ;
		} else if (uf.equals(UFEnum.RN.getName())) {
			ufEnum = RN;
		} else if (uf.equals(UFEnum.RO.getName())) {
			ufEnum = RO;
		} else if (uf.equals(UFEnum.RR.getName())) {
			ufEnum = RR;
		} else if (uf.equals(UFEnum.RS.getName())) {
			ufEnum = RS;
		} else if (uf.equals(UFEnum.SC.getName())) {
			ufEnum = SC;
		} else if (uf.equals(UFEnum.SE.getName())) {
			ufEnum = SE;
		} else if (uf.equals(UFEnum.SP.getName())) {
			ufEnum = SP;
		} else if (uf.equals(UFEnum.TO.getName())) {
			ufEnum = TO;
		}
		return ufEnum;
	}

	public static String[] toArrayString() {
		String[] arrayStringUF = { "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ",
				"RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO" };
		return arrayStringUF;
	}

}
//
//
//
//
//
//
//
//
//
//
//
