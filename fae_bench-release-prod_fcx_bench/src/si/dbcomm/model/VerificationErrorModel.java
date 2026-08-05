//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file VerificationErrorModel.java
*    @author marcos
*    @date 10 de fev de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "verificationerrormodel")
public class VerificationErrorModel {

	@Id
	@SequenceGenerator(name = "seq_verif_error_model", sequenceName = "verification_error_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_verif_error_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_verification_error", nullable = false)
	private long id;

	@Column(name = "double_error", nullable = false)
	double error;

	@Column(name = "double_expected_flow_rate", nullable = false)
	double expectedFlowRate;

	@Column(name = "double_ref_flow_rate", nullable = false)
	double meanRefFlowRate;

	@Column(name = "double_meter_flow_rate", nullable = false)
	double meanMeterFlowRate;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = MeterModel.class)
	private MeterModel meter;

	public VerificationErrorModel() {

	}

	public VerificationErrorModel(double error, double expectedFlowRate, double meanRefFlowRate, double meanMeterFlowRate) {
		this.error = error;
		this.expectedFlowRate = expectedFlowRate;
		this.meanRefFlowRate = meanRefFlowRate;
		this.meanMeterFlowRate = meanMeterFlowRate;

	}

	/**
	 * Function returns the value of attribute id
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Function sets the value for attribute id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Function returns the value of attribute error
	 * 
	 * @return the error
	 */
	public double getError() {
		return error;
	}

	/**
	 * Function sets the value for attribute error
	 * 
	 * @param error
	 *            the error to set
	 */
	public void setError(double error) {
		this.error = error;
	}

	/**
	 * Function returns the value of attribute expectedFlowRate
	 * 
	 * @return the expectedFlowRate
	 */
	public double getExpectedFlowRate() {
		return expectedFlowRate;
	}

	/**
	 * Function sets the value for attribute expectedFlowRate
	 * 
	 * @param expectedFlowRate
	 *            the expectedFlowRate to set
	 */
	public void setExpectedFlowRate(double expectedFlowRate) {
		this.expectedFlowRate = expectedFlowRate;
	}

	/**
	 * Function returns the value of attribute meanRefFlowRate
	 * 
	 * @return the meanRefFlowRate
	 */
	public double getMeanRefFlowRate() {
		return meanRefFlowRate;
	}

	/**
	 * Function sets the value for attribute meanRefFlowRate
	 * 
	 * @param meanRefFlowRate
	 *            the meanRefFlowRate to set
	 */
	public void setMeanRefFlowRate(double meanRefFlowRate) {
		this.meanRefFlowRate = meanRefFlowRate;
	}

	/**
	 * Function returns the value of attribute meanMeterFlowRate
	 * 
	 * @return the meanMeterFlowRate
	 */
	public double getMeanMeterFlowRate() {
		return meanMeterFlowRate;
	}

	/**
	 * Function sets the value for attribute meanMeterFlowRate
	 * 
	 * @param meanMeterFlowRate
	 *            the meanMeterFlowRate to set
	 */
	public void setMeanMeterFlowRate(double meanMeterFlowRate) {
		this.meanMeterFlowRate = meanMeterFlowRate;
	}

	/**
	 * Function returns the value of attribute meter
	 * 
	 * @return the meter
	 */
	public MeterModel getMeter() {
		return meter;
	}

	/**
	 * Function sets the value for attribute meter
	 * 
	 * @param meter
	 *            the meter to set
	 */
	public void setMeter(MeterModel meter) {
		this.meter = meter;
	}

	/**
	 * @param
	 * @return
	 */
	public static Comparator<VerificationErrorModel> comparator = new Comparator<VerificationErrorModel>() {

		public int compare(VerificationErrorModel error1, VerificationErrorModel error2) {
			// ascending order
			// return error1.getExpectedFlowRate().compareTo(error2.getExpectedFlowRate());
			if (error1.getExpectedFlowRate() < error2.getExpectedFlowRate())
				return -1;
			if (error1.getExpectedFlowRate() > error2.getExpectedFlowRate())
				return 1;
			return 0;
		}
	};

}
