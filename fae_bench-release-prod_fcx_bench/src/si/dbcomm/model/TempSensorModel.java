//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file TempSensorModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "tempsensormodel")
public class TempSensorModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6689328866864959352L;

	@Id
	@SequenceGenerator(name = "seq_temp_sensor_model", sequenceName = "temperaturesens_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_temp_sensor_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_temperature", nullable = false)
	private long id;

	@Column(name = "double_temperature")
	private double temperature;

	@Column(name = "string_tag", nullable = false)
	private String tag;

	@Column(name = "string_description")
	private String description;

	private Date readTime;

	/**
	 * Creates a object for class TempSensorModel.java
	 * 
	 * @param id
	 */
	public TempSensorModel(long id) {
		this.id = id;
	}

	/**
	 * Creates a object for class TempSensorModel.java
	 * 
	 * @param id
	 */
	public TempSensorModel() {
	}

	/**
	 * Function returns the value of attribute temperature
	 * 
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Function sets the value for attribute temperature
	 * 
	 * @param temperature
	 *            the temperature to set
	 */
	public void setTemperature(final double temperature) {
		this.temperature = temperature;
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
	public void setId(final long id) {
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
	 * Function returns the value of attribute readTime
	 * 
	 * @return the readTime
	 */
	public Date getReadTime() {
		return readTime;
	}

	/**
	 * Function sets the value for attribute readTime
	 * 
	 * @param readTime
	 *            the readTime to set
	 */
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

}
