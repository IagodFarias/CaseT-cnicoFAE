//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ConstDistancesModel.java
*    @author marcos
*    @date 6 de dez de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package model;

/**
 * @author marcos
 *
 */
// @Entity
// @Table(name="estimateddistancemodel")
public class EstimatedDistanceModel {
	// @Id
	// @SequenceGenerator(name = "estdist_seq", sequenceName = "estdist_seq", allocationSize = 1, initialValue = 1)
	// @GeneratedValue(generator = "estdist_seq", strategy = GenerationType.SEQUENCE)
	// @Column(name = "id_estimateddistancemodel", nullable = false)
	private Long id;

	// @ManyToOne(fetch = FetchType.LAZY, targetEntity = FlowRateModel.class)
	private double refFlow;

	// @ManyToOne(fetch = FetchType.LAZY, targetEntity = FlowRateModel.class)
	private double desiredFlow;

	private double kDistValue;

	private double reDistValue;

	private double temp;
	
	private double avgReynolds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getRefFlow() {
		return refFlow;
	}

	public void setRefFlow(double refFlow) {
		this.refFlow = refFlow;
	}

	public double getDesiredFlow() {
		return desiredFlow;
	}

	public void setDesiredFlow(double desiredFlow) {
		this.desiredFlow = desiredFlow;
	}

	public double getkDistValue() {
		return kDistValue;
	}

	public void setkDistValue(double kDistValue) {
		this.kDistValue = kDistValue;
	}

	public double getReDistValue() {
		return reDistValue;
	}

	public void setReDistValue(double reDistValue) {
		this.reDistValue = reDistValue;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	/**
	 * Function returns the value of attribute avgReynolds
	 * @return the avgReynolds
	 */
	public double getAvgReynolds() {
		return avgReynolds;
	}

	/**
	 * Function sets the value for attribute avgReynolds
	 * @param avgReynolds the avgReynolds to set
	 */
	public void setAvgReynolds(double avgReynolds) {
		this.avgReynolds = avgReynolds;
	}

}
