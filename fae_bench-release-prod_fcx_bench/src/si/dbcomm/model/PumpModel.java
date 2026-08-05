//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PumpModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import si.dbcomm.enumerations.PumpStateEnum;

/**
 * @author Marcos Oliveira
 *
 */
@Entity
@Table(name = "pumpmodel")
public class PumpModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8734079403490150434L;

	@Id
	@SequenceGenerator(name = "seq_pump_model", sequenceName = "pumpmodel_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_pump_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_pump", nullable = false)
	private long id;

	@Column(name = "double_load", nullable = false)
	private double load;

	@Column(name = "double_maxflowrate")
	private double maxFlowrate;

	@Column(name = "double_minflowrate")
	private double minFlowRate;

	@Enumerated(EnumType.STRING)
	private PumpStateEnum state;

	@Column(name = "string_tag", nullable = false)
	private String tag;

	@Column(name = "string_description")
	private String description;

	@OneToMany(mappedBy = "pump")
	private Set<FlowRateModel> flowRates;

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
	 * Function returns the value of attribute load
	 * 
	 * @return the load
	 */
	public double getLoad() {
		return load;
	}

	/**
	 * Function sets the value for attribute load
	 * 
	 * @param load
	 *            the load to set
	 */
	public void setLoad(double load) {
		this.load = load;
	}

	/**
	 * Function returns the value of attribute state
	 * 
	 * @return the state
	 */
	public PumpStateEnum getState() {
		return state;
	}

	/**
	 * Function sets the value for attribute state
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(PumpStateEnum state) {
		this.state = state;
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
	 * Function returns the value of attribute maxFlowrate
	 * 
	 * @return the maxFlowrate
	 */
	public double getMaxFlowRate() {
		return maxFlowrate;
	}

	/**
	 * Function sets the value for attribute maxFlowrate
	 * 
	 * @param maxFlowrate
	 *            the maxFlowrate to set
	 */
	public void setMaxFlowRate(double maxFlowrate) {
		this.maxFlowrate = maxFlowrate;
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

}
