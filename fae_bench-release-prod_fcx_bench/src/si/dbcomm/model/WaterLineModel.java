//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief Model describes the attributes and functions the bench line exe-
*    cutes
*    @file WaterLineModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import si.dbcomm.enumerations.LineStateEnum;

/**
 * @author Marcos Oliveira
 *
 */
@Entity
@Table(name = "waterlinemodel")
public class WaterLineModel {

	@Id
	@SequenceGenerator(name = "seq_water_line_model", sequenceName = "linemodel_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_water_line_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_line", nullable = false)
	private long id;

	@Enumerated(EnumType.STRING)
	private LineStateEnum state;

	@Column(name = "string_tag", nullable = false)
	private String tag;

	@Column(name = "string_description")
	private String description;

	/**
	 * Creates a object for class WaterLineModel.java
	 * 
	 * @param id
	 */
	public WaterLineModel(long id) {
		this.id = id;
	}

	/**
	 * Creates a object for class WaterLineModel.java
	 * 
	 * @param id
	 */
	public WaterLineModel() {
	}

	/**
	 * Function returns the value of attribute state
	 * 
	 * @return the state
	 */
	public LineStateEnum getState() {
		return state;
	}

	/**
	 * Function sets the value for attribute state
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(LineStateEnum state) {
		this.state = state;
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
