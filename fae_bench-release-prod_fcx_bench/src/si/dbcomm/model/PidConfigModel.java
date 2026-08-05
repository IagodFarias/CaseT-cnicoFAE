//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PidConfigModel.java
*    @author marcos
*    @date 13 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "pidconfigmodel")
public class PidConfigModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8114860346830014094L;

	@Id
	@SequenceGenerator(name = "seq_pid_config_model", sequenceName = "pidconfigmodel_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_pid_config_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_pidconfig", nullable = false)
	private long id;

	@OneToOne(fetch = FetchType.EAGER, targetEntity = FlowRateModel.class)
	private FlowRateModel flowRate;

	@Column(name = "double_proportionalkp")
	private double proportionalKp; // Proportinal gain

	@Column(name = "double_integrativetm")
	private double integrativeTm; // unity of Gain per sec Integrative

	@Column(name = "double_derivativetv")
	private double derivativeTv; // unity of Gain per sec Devrivative

	@Column(name = "double_error")
	private double errorE; // unity of Gain per sec Devrivative

	/**
	 * Function returns the value of attribute flowRate
	 * 
	 * @return the flowRate
	 */
	public FlowRateModel getFlowRate() {
		return flowRate;
	}

	/**
	 * Function sets the value for attribute flowRate
	 * 
	 * @param flowRate
	 *            the flowRate to set
	 */
	public void setFlowRate(FlowRateModel flowRate) {
		this.flowRate = flowRate;
	}

	/**
	 * Function returns the value of attribute proportionalKp
	 * 
	 * @return the proportionalKp
	 */
	public double getProportionalKp() {
		return proportionalKp;
	}

	/**
	 * Function sets the value for attribute proportionalKp
	 * 
	 * @param proportionalKp
	 *            the proportionalKp to set
	 */
	public void setProportionalKp(double proportionalKp) {
		this.proportionalKp = proportionalKp;
	}

	/**
	 * Function returns the value of attribute integrativeTm
	 * 
	 * @return the integrativeTm
	 */
	public double getIntegrativeTm() {
		return integrativeTm;
	}

	/**
	 * Function sets the value for attribute integrativeTm
	 * 
	 * @param integrativeTm
	 *            the integrativeTm to set
	 */
	public void setIntegrativeTm(double integrativeTm) {
		this.integrativeTm = integrativeTm;
	}

	/**
	 * Function returns the value of attribute derivativeTv
	 * 
	 * @return the derivativeTv
	 */
	public double getDerivativeTv() {
		return derivativeTv;
	}

	/**
	 * Function sets the value for attribute derivativeTv
	 * 
	 * @param derivativeTv
	 *            the derivativeTv to set
	 */
	public void setDerivativeTv(double derivativeTv) {
		this.derivativeTv = derivativeTv;
	}

	/**
	 * Function returns the value of attribute errorE
	 * 
	 * @return the errorE
	 */
	public double getErrorE() {
		return errorE;
	}

	/**
	 * Function sets the value for attribute errorE
	 * 
	 * @param errorE
	 *            the errorE to set
	 */
	public void setErrorE(double errorE) {
		this.errorE = errorE;
	}

}
