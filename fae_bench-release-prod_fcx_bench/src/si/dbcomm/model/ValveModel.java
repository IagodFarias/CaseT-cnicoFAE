//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ValveModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import si.dbcomm.enumerations.ValveStateEnum;

/**
 * @author Marcos Oliveira
 *
 */
@Entity
@Table(name = "valvemodel")
public class ValveModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6728255239303853794L;

	@Id
	@SequenceGenerator(name = "seq_valve_model", sequenceName = "valvemodel_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_valve_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_valve", nullable = false)
	private long id;

	@Enumerated(EnumType.STRING)
	private ValveStateEnum state;

	@Column(name = "string_tag", nullable = false, unique = true)
	private String tag;

	@Column(name = "string_description")
	private String description;

	/**
	 * Creates a object for class ValveModel.java
	 * 
	 * @param id
	 */
	public ValveModel(int id) {
		this.id = id;
	}

	/**
	 * Creates a object for class ValveModel.java
	 * 
	 * @param id
	 */
	public ValveModel() {
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
	 * Function returns the value of attribute state
	 * 
	 * @return the state
	 */
	public ValveStateEnum getState() {
		return state;
	}

	/**
	 * Function sets the value for attribute state
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(ValveStateEnum state) {
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

}
