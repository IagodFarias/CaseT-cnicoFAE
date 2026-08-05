//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RamalModel.java
*    @author marcos
*    @date 18 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Marcos Oliveira
 *
 */
@Entity
@Table(name = "ramalmodel")
public class RamalModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9006710930995473220L;

	@Id
	@SequenceGenerator(name = "ramal_model_seq", sequenceName = "ramal_model_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "ramal_model_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_ramal", nullable = false)
	private long id;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private ValveModel valve;

	@Column(name = "string_tag")
	private String tag;

	@Column(name = "string_description")
	private String description;

	@Column(name = "double_maxflowrate")
	private double maxFlowRate;

	@Column(name = "double_minflowrate")
	private double minFlowRate;

	@OneToMany(mappedBy = "ramal")
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

}
