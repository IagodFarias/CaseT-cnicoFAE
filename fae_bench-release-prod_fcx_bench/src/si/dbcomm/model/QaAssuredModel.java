//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file QaAssuredModel.java
*    @author osmenio
*    @date 17 de out de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import si.dbcomm.enumerations.QaAssuredStatusEnum;

/**
 * @author osmenio
 *
 */
@Entity
@Table(name = "qaassuredmodel")
public class QaAssuredModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4858912554558937512L;

	@Id
	@SequenceGenerator(name = "qaassured_seq", sequenceName = "qaassured_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "qaassured_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_qaassured", nullable = false)
	private Long id;

	@Column(name = "string_serialnumbermeter")
	private String serialNumberMeter;

	@Column(name = "string_serialnumberwmbus", nullable = true)
	private String serialNumberWmBus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_dateofreprove", nullable = true)
	private Date dateOfReprove;

	@Enumerated(EnumType.STRING)
	@Column(name = "enum_qaassuredstatus", nullable = false)
	private QaAssuredStatusEnum qaAssuredStatusEnum;

	@OneToOne
	@JoinColumn(name = "id_meter", unique = true)
	private MeterModel meter;

	@Transient
	private boolean isSelected = false;
	
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
	 * Function returns the value of attribute serialNumberMeter
	 * 
	 * @return the serialNumberMeter
	 */
	public String getSerialNumberMeter() {
		return serialNumberMeter;
	}

	/**
	 * Function sets the value for attribute serialNumberMeter
	 * 
	 * @param serialNumberMeter
	 *            the serialNumberMeter to set
	 */
	public void setSerialNumberMeter(String serialNumberMeter) {
		this.serialNumberMeter = serialNumberMeter;
	}

	/**
	 * Function returns the value of attribute serialNumberWmBus
	 * 
	 * @return the serialNumberWmBus
	 */
	public String getSerialNumberWmBus() {
		return serialNumberWmBus;
	}

	/**
	 * Function sets the value for attribute serialNumberWmBus
	 * 
	 * @param serialNumberWmBus
	 *            the serialNumberWmBus to set
	 */
	public void setSerialNumberWmBus(String serialNumberWmBus) {
		this.serialNumberWmBus = serialNumberWmBus;
	}

	/**
	 * Function returns the value of attribute dateOfReprove
	 * 
	 * @return the dateOfReprove
	 */
	public Date getDateOfReprove() {
		return dateOfReprove;
	}

	/**
	 * Function sets the value for attribute dateOfReprove
	 * 
	 * @param dateOfReprove
	 *            the dateOfReprove to set
	 */
	public void setDateOfReprove(Date dateOfReprove) {
		this.dateOfReprove = dateOfReprove;
	}

	/**
	 * Function returns the value of attribute qaAssuredStatusEnum
	 * 
	 * @return the qaAssuredStatusEnum
	 */
	public QaAssuredStatusEnum getQaAssuredStatusEnum() {
		return qaAssuredStatusEnum;
	}

	/**
	 * Function sets the value for attribute qaAssuredStatusEnum
	 * 
	 * @param qaAssuredStatusEnum
	 *            the qaAssuredStatusEnum to set
	 */
	public void setQaAssuredStatusEnum(QaAssuredStatusEnum qaAssuredStatusEnum) {
		this.qaAssuredStatusEnum = qaAssuredStatusEnum;
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
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
}
