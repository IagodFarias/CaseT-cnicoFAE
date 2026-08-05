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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "volumetricerrormodel")
public class VolumetricErrorModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9144609269423668489L;

	@Id
	@SequenceGenerator(name = "volumetricerror_seq", sequenceName = "volumetricerror_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "volumetricerror_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_volumetricerror", nullable = false)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_dateofverification", nullable = true)
	private Date dateOfVerification;

	@Column(name = "double_tankvolume", nullable = false)
	private double tankVolume;

	@Column(name = "double_duration", nullable = false)
	private double duration;

	@Column(name = "double_initialvolume", nullable = false)
	private double initialVolume;

	@Column(name = "double_finalvolume", nullable = false)
	private double finalVolume;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = MeterModel.class)
	private MeterModel meter;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = FlowRateModel.class)
	private FlowRateModel flowRate;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = VolumetricBenchModel.class)
	private VolumetricBenchModel volumetricBench;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the dateOfVerification
	 */
	public Date getDateOfVerification() {
		return dateOfVerification;
	}

	/**
	 * @param dateOfVerification
	 *            the dateOfVerification to set
	 */
	public void setDateOfVerification(Date dateOfVerification) {
		this.dateOfVerification = dateOfVerification;
	}

	/**
	 * @return the tankVolume
	 */
	public double getTankVolume() {
		return tankVolume;
	}

	/**
	 * @param tankVolume
	 *            the tankVolume to set
	 */
	public void setTankVolume(double tankVolume) {
		this.tankVolume = tankVolume;
	}

	/**
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}

	/**
	 * @return the initialVolume
	 */
	public double getInitialVolume() {
		return initialVolume;
	}

	/**
	 * @param initialVolume
	 *            the initialVolume to set
	 */
	public void setInitialVolume(double initialVolume) {
		this.initialVolume = initialVolume;
	}

	/**
	 * @return the finalVolume
	 */
	public double getFinalVolume() {
		return finalVolume;
	}

	/**
	 * @param finalVolume
	 *            the finalVolume to set
	 */
	public void setFinalVolume(double finalVolume) {
		this.finalVolume = finalVolume;
	}

	/**
	 * @return the meter
	 */
	public MeterModel getMeter() {
		return meter;
	}

	/**
	 * @param meter
	 *            the meter to set
	 */
	public void setMeter(MeterModel meter) {
		this.meter = meter;
	}

	/**
	 * @return the flowRate
	 */
	public FlowRateModel getFlowRate() {
		return flowRate;
	}

	/**
	 * @param flowRate
	 *            the flowRate to set
	 */
	public void setFlowRate(FlowRateModel flowRate) {
		this.flowRate = flowRate;
	}

	/**
	 * @return the volumetricBench
	 */
	public VolumetricBenchModel getVolumetricBench() {
		return volumetricBench;
	}

	/**
	 * @param volumetricBench
	 *            the volumetricBench to set
	 */
	public void setVolumetricBench(VolumetricBenchModel volumetricBench) {
		this.volumetricBench = volumetricBench;
	}
}
