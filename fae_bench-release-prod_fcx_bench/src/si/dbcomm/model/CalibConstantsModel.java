//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CalibConstantsModel.java
*    @author marcos
*    @date 19 de set de 2016
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
import javax.persistence.JoinColumn;
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
@Table(name = "calibconstantsmodel")
public class CalibConstantsModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2570876983561583373L;

	@Id
	@SequenceGenerator(name = "calibconst_seq", sequenceName = "calibconst_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "calibconst_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_calib_const", nullable = false)
	private Long id;

	@Column(name = "double_reynolds")
	private double reynolds;

	@Column(name = "double_flowrate")
	private double flowRate; // Specified flowrate expected flowrate

	@Column(name = "double_Avgflowrate")
	private double avgFlowRate;

	@Column(name = "double_Avgpressure")
	private double avgPressure;

	@Column(name = "double_constantk")
	private double constantK;

	@Column(name = "double_avgtemperature")
	private double avgTemp;

	@Temporal(value = TemporalType.TIMESTAMP)
	private Date calcDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_meter", nullable = false)
	private MeterModel meter;

	/**
	 * Creates a object for class CalibConstantsModel.java
	 * 
	 * @param flowrate
	 * @param avgFlowrate
	 * @param avgTemp
	 * @param k
	 */
	public CalibConstantsModel(double flowrate, double avgFlowrate, double avgTemp, double k, double reynolds, MeterModel meter) {
		this.flowRate = flowrate;
		this.avgFlowRate = avgFlowrate;
		this.avgTemp = avgTemp;
		this.constantK = k;
		this.reynolds = reynolds;
		this.meter = meter;
		this.calcDate = new Date();
	}

	public CalibConstantsModel() {

	}

	/**
	 * Function returns the value of attribute id
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Function sets the value for attribute id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Function returns the value of attribute flowRate
	 * 
	 * @return the flowRate
	 */
	public double getFlowRate() {
		return flowRate;
	}

	/**
	 * Function sets the value for attribute flowRate
	 * 
	 * @param flowRate
	 *            the flowRate to set
	 */
	public void setFlowRate(double flowRate) {
		this.flowRate = flowRate;
	}

	/**
	 * Function returns the value of attribute avgFlowRate
	 * 
	 * @return the avgFlowRate
	 */
	public double getAvgFlowRate() {
		return avgFlowRate;
	}

	/**
	 * Function sets the value for attribute avgFlowRate
	 * 
	 * @param avgFlowRate
	 *            the avgFlowRate to set
	 */
	public void setAvgFlowRate(double avgFlowRate) {
		this.avgFlowRate = avgFlowRate;
	}

	/**
	 * Function returns the value of attribute constantK
	 * 
	 * @return the constantK
	 */
	public double getConstantK() {
		return constantK;
	}

	/**
	 * Function sets the value for attribute constantK
	 * 
	 * @param constantK
	 *            the constantK to set
	 */
	public void setConstantK(double constantK) {
		this.constantK = constantK;
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
	 * Function returns the value of attribute reynolds
	 * 
	 * @return the reynolds
	 */
	public double getReynolds() {
		return reynolds;
	}

	/**
	 * Function sets the value for attribute reynolds
	 * 
	 * @param reynolds
	 *            the reynolds to set
	 */
	public void setReynolds(double reynolds) {
		this.reynolds = reynolds;
	}

	/**
	 * Function returns the value of attribute avgTemp
	 * 
	 * @return the avgTemp
	 */
	public double getAvgTemp() {
		return avgTemp;
	}

	/**
	 * Function sets the value for attribute avgTemp
	 * 
	 * @param avgTemp
	 *            the avgTemp to set
	 */
	public void setAvgTemp(double avgTemp) {
		this.avgTemp = avgTemp;
	}

	/**
	 * Function returns the value of attribute avgPressure
	 * 
	 * @return the avgPressure
	 */
	public double getAvgPressure() {
		return avgPressure;
	}

	/**
	 * Function sets the value for attribute avgPressure
	 * 
	 * @param avgPressure
	 *            the avgPressure to set
	 */
	public void setAvgPressure(double avgPressure) {
		this.avgPressure = avgPressure;
	}

	/**
	 * Function returns the value of attribute calcDate
	 * 
	 * @return the calcDate
	 */
	public Date getCalcDate() {
		return calcDate;
	}

	/**
	 * Function sets the value for attribute calcDate
	 * 
	 * @param calcDate
	 *            the calcDate to set
	 */
	public void setCalcDate(Date calcDate) {
		this.calcDate = calcDate;
	}

}
