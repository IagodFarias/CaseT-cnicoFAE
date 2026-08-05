//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief Model defines the Object related to pressure sensor
*    @file PressureSensorModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details  This class implement the attributes and actions performed by the
*    pressure sensors on the bench
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
@Table(name = "pressuresensormodel")
public class PressureSensorModel {

	@Id
	@SequenceGenerator(name = "pressure_sensor_model_seq", sequenceName = "pressure_sensor_model_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "pressure_sensor_model_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_pressure", nullable = false)
	private long id;

	@Column(name = "double_pressure")
	private double pressureRead;

	@Column(name = "string_tag", nullable = false)
	private String tag;

	@Column(name = "string_description")
	private String description;

	/**
	 * 
	 * Creates a object for class PressureSensorModel.java
	 */
	public PressureSensorModel() {
	}

	/**
	 * 
	 * Creates a object for class PressureSensorModel.java
	 */
	public PressureSensorModel(int sensorNum) {
		this.id = sensorNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see interfaces.SensorInterface#readSensor()
	 */
	public double readSensor() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Function returns the value of attribute sensorNumber
	 * 
	 * @return the sensorNumber
	 */
	public long getId() {
		return id;
	}

	/**
	 * Function sets the value for attribute sensorNumber
	 * 
	 * @param sensorNumber
	 *            the sensorNumber to set
	 */
	public void setId(long sensorNumber) {
		this.id = sensorNumber;
	}

	/**
	 * Function returns the value of attribute pressureRead
	 * 
	 * @return the pressureRead
	 */
	public double getPressureRead() {
		return pressureRead;
	}

	/**
	 * Function sets the value for attribute pressureRead
	 * 
	 * @param pressureRead
	 *            the pressureRead to set
	 */
	public void setPressureRead(double pressureRead) {
		this.pressureRead = pressureRead;
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
