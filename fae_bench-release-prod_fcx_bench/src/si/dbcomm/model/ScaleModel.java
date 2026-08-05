//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ScaleModel.java
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Marcos Oliveira
 *
 */
@Entity
@Table(name = "scalemodel")
public class ScaleModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7734505030508359060L;

	@Id
	@SequenceGenerator(name = "seq_scale_model", sequenceName = "scalemodel_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_scale_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_scale", nullable = false)
	private long id;

	@Column(name = "double_weight")
	private double weight;

	@Column(name = "string_tag", nullable = false)
	private String tag;

	@Column(name = "string_description")
	private String description;

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
	 * Function returns the value of attribute weight
	 * 
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Function sets the value for attribute weight
	 * 
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
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
