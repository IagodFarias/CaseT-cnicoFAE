//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RadioWmBusConfigModel.java
*    @author Marcos
*    @date 14 de mar de 2018
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Marcos
 *
 */
@Entity
@Table(name = "radiowmbusconfigmodel")
public class RadioWmBusConfigModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7531454710358266710L;

	@Id
	@SequenceGenerator(name = "radioconfig_seq", sequenceName = "radioconfig_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "radioconfig_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_radiowmbusconfigmodel", nullable = false)
	private Long id;

	@Column(name = "string_wmBusSerialNumber", unique = true)
	private String wmBusSerialNumber;

	@Column(name = "string_wmBusCryptoKey", unique = true)
	private String wmBusCryptoKey;

	@Column(name = "int_transmitinterval", unique = false)
	private int transmitInterval;

	@OneToOne
	@JoinColumn(name = "id_meter", unique = true)
	private MeterModel meter;

	/**
	 * Function returns the value of attribute id
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Function sets the value for attribute id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Function returns the value of attribute meter
	 * 
	 * @return the meter
	 */
	public MeterModel getMeter() {
		return meter;
	}

	/**
	 * Function sets the value for attribute meter
	 * 
	 * @param meter
	 *            the meter to set
	 */
	public void setMeter(MeterModel meter) {
		this.meter = meter;
	}

	/**
	 * Function returns the value of attribute wmBusSerialNumber
	 * 
	 * @return the wmBusSerialNumber
	 */
	public String getWmBusSerialNumber() {
		return wmBusSerialNumber;
	}

	/**
	 * Function sets the value for attribute wmBusSerialNumber
	 * 
	 * @param wmBusSerialNumber
	 *            the wmBusSerialNumber to set
	 */
	public void setWmBusSerialNumber(String wmBusSerialNumber) {
		this.wmBusSerialNumber = wmBusSerialNumber;
	}

	/**
	 * Function returns the value of attribute wmBusCryptoKey
	 * 
	 * @return the wmBusCryptoKey
	 */
	public String getWmBusCryptoKey() {
		return wmBusCryptoKey;
	}

	/**
	 * Function sets the value for attribute wmBusCryptoKey
	 * 
	 * @param wmBusCryptoKey
	 *            the wmBusCryptoKey to set
	 */
	public void setWmBusCryptoKey(String wmBusCryptoKey) {
		this.wmBusCryptoKey = wmBusCryptoKey;
	}

	/**
	 * Function returns the value of attribute transmitInterval
	 * 
	 * @return the transmitInterval
	 */
	public int getTransmitInterval() {
		return transmitInterval;
	}

	/**
	 * Function sets the value for attribute transmitInterval
	 * 
	 * @param transmitInterval
	 *            the transmitInterval to set
	 */
	public void setTransmitInterval(int transmitInterval) {
		this.transmitInterval = transmitInterval;
	}

}
