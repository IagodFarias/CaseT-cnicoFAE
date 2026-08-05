//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RefMeterModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import si.dbcomm.enumerations.FlowRateStateEnum;

/**
 * @author Marcos Oliveira
 *
 */
@Entity
@Table(name = "refmetermodel")
public class RefMeterModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6174545836257034911L;

	@Id
	@SequenceGenerator(name = "seq_ref_meter_model", sequenceName = "refmetermodel_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_ref_meter_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_refmeter", nullable = false)
	private long id;

	@Column(name = "double_flowrate", nullable = true)
	private double flowRate;

	@Column(name = "int_pulses", nullable = true)
	private int pulses;

	// this is the timer used to calculate the average flow rate during the test. It is a counter in mS.
	@Column(name = "int_timeread", nullable = true)
	private int timeread;

	@Column(name = "double_volume", nullable = true)
	private double volume;

	@Column(name = "string_tag", nullable = false)
	private String tag;

	@Column(name = "string_description", nullable = false)
	private String description;

	@Column(name = "double_maxflowrate", nullable = false)
	private double maxFlowRate;

	@Column(name = "double_freqmaxflowrate", nullable = false)
	private double freqMaxFlowRate;

	@Column(name = "double_minflowrate", nullable = false)
	private double minFlowRate;

	// this is the time the flow meter was read.
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date readtime;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private ValveModel valve;

	@OneToMany(mappedBy = "refMeter")
	private Set<FlowRateModel> flowRates;

	@OneToMany(mappedBy = "refMeter")
	private Set<BenchDataModel> data;

	@Enumerated(EnumType.STRING)
	private FlowRateStateEnum flowStability;

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
	 * Function returns the value of attribute tag
	 * 
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Function sets the value for attribute tag
	 * 
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Function returns the value of attribute description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Function sets the value for attribute description
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Function returns the value of attribute readtime
	 * 
	 * @return the readtime
	 */
	public Date getReadtime() {
		return readtime;
	}

	/**
	 * Function sets the value for attribute readtime
	 * 
	 * @param readtime
	 *            the readtime to set
	 */
	public void setReadtime(Date readtime) {
		this.readtime = readtime;
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
	 * Function returns the value of attribute volume
	 * 
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * Function sets the value for attribute volume
	 * 
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	/**
	 * Function returns the value of attribute pulses
	 * 
	 * @return the pulses
	 */
	public int getPulses() {
		return pulses;
	}

	/**
	 * Function sets the value for attribute pulses
	 * 
	 * @param pulses
	 *            the pulses to set
	 */
	public void setPulses(int pulses) {
		this.pulses = pulses;
	}

	/**
	 * Function returns the value of attribute flowStability
	 * 
	 * @return the flowStability
	 */
	public FlowRateStateEnum getFlowStability() {
		return flowStability;
	}

	/**
	 * Function sets the value for attribute flowStability
	 * 
	 * @param flowStability
	 *            the flowStability to set
	 */
	public void setFlowStability(FlowRateStateEnum flowStability) {
		this.flowStability = flowStability;
	}

	/**
	 * Function returns the value of attribute maxFlowRate
	 * 
	 * @return the maxFlowRate
	 */
	public double getMaxFlowRate() {
		return maxFlowRate;
	}

	/**
	 * Function sets the value for attribute maxFlowRate
	 * 
	 * @param maxFlowRate
	 *            the maxFlowRate to set
	 */
	public void setMaxFlowRate(double maxFlowRate) {
		this.maxFlowRate = maxFlowRate;
	}

	/**
	 * Function returns the value of attribute minFlowRate
	 * 
	 * @return the minFlowRate
	 */
	public double getMinFlowRate() {
		return minFlowRate;
	}

	/**
	 * Function sets the value for attribute minFlowRate
	 * 
	 * @param minFlowRate
	 *            the minFlowRate to set
	 */
	public void setMinFlowRate(double minFlowRate) {
		this.minFlowRate = minFlowRate;
	}

	/**
	 * Function returns the value of attribute valve
	 * 
	 * @return the valve
	 */
	public ValveModel getValve() {
		return valve;
	}

	/**
	 * Function sets the value for attribute valve
	 * 
	 * @param valve
	 *            the valve to set
	 */
	public void setValve(ValveModel valve) {
		this.valve = valve;
	}

	/**
	 * Function returns the value of attribute timeread
	 * 
	 * @return the timeread
	 */
	public int getTimeread() {
		return timeread;
	}

	/**
	 * Function sets the value for attribute timeread
	 * 
	 * @param timeread
	 *            the timeread to set
	 */
	public void setTimeread(int timeread) {
		this.timeread = timeread;
	}

	/**
	 * Function returns the value of attribute flowRates
	 * 
	 * @return the flowRates
	 */
	public Set<FlowRateModel> getFlowRates() {
		return flowRates;
	}

	/**
	 * Function sets the value for attribute flowRates
	 * 
	 * @param flowRates
	 *            the flowRates to set
	 */
	public void setFlowRates(Set<FlowRateModel> flowRates) {
		this.flowRates = flowRates;
	}

	/**
	 * Function returns the value of attribute freqMaxFlowRate
	 * 
	 * @return the freqMaxFlowRate
	 */
	public double getFreqMaxFlowRate() {
		return freqMaxFlowRate;
	}

	/**
	 * Function sets the value for attribute freqMaxFlowRate
	 * 
	 * @param freqMaxFlowRate
	 *            the freqMaxFlowRate to set
	 */
	public void setFreqMaxFlowRate(double freqMaxFlowRate) {
		this.freqMaxFlowRate = freqMaxFlowRate;
	}

	/**
	 * Function returns the value of attribute data
	 * 
	 * @return the data
	 */
	public Set<BenchDataModel> getData() {
		return data;
	}

	/**
	 * Function sets the value for attribute data
	 * 
	 * @param data
	 *            the data to set
	 */
	public void setData(Set<BenchDataModel> data) {
		this.data = data;
	}
}
