//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief Model defines the Object related to humidity sensor
*    @file HumiditySensorModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details This class implement the attributes and actions performed by the
*    humidity sensor on the bench
* 
*/
//=============================================================================
package si.dbcomm.model;

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
@Table(name = "humiditysensormodel")
public class HumiditySensorModel {

	@Id
	@SequenceGenerator(name = "humidity_sensor_model_seq", sequenceName = "humidity_sensor_model_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "humidity_sensor_model_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_humidity", nullable = false)
	private long id;

	@Column(name = "string_tag", nullable = false)
	private String tag;

	@Column(name = "string_description")
	private String description;

	@Column(name = "double_humidity", nullable = false)
	private double humidity;

	private Date readTime;

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

	public HumiditySensorModel() {

	}

	public HumiditySensorModel(int id) {
		this.id = id;
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
	 * Function returns the value of attribute humidity
	 * 
	 * @return the humidity
	 */
	public double getHumidity() {
		return humidity;
	}

	/**
	 * Function sets the value for attribute humidity
	 * 
	 * @param humidity
	 *            the humidity to set
	 */
	public void setHumidity(double humidity) {
		this.humidity = humidity;
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
