//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief Model defines the Object related to water level sensor
*    @file LevelSensorModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details  This class implement the attributes and actions performed by the
*    water level sensors on the bench
* 
*/
//=============================================================================
package si.dbcomm.model;

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
@Table(name = "levelsensormodel")
public class LevelSensorModel {

	@Id
	@SequenceGenerator(name = "seq_level_sensor_model", sequenceName = "seq_level_sensor_model", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_level_sensor_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "boolean_level", nullable = false)
	private boolean level;

	@Column(name = "string_description")
	private String description;

	@Column(name = "string_tag", nullable = false)
	private String tag;

	/**
	 * Creates a object for class LevelSensorModel.java
	 * 
	 * @param id
	 */
	public LevelSensorModel(int id) {
		this.id = id;
	}

	/**
	 * Creates a object for class LevelSensorModel.java
	 * 
	 * @param id
	 */
	public LevelSensorModel() {
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
	 * Function returns the value of attribute level
	 * 
	 * @return the level
	 */
	public boolean isLevel() {
		return level;
	}

	/**
	 * Function sets the value for attribute level
	 * 
	 * @param level
	 *            the level to set
	 */
	public void setLevel(boolean level) {
		this.level = level;
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

}
