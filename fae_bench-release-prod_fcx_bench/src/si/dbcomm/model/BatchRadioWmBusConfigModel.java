//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BatchRadioWmBusConfigModel.java
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
@Table(name = "batchradiowmbusconfigmodel")
public class BatchRadioWmBusConfigModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5605607948675349631L;

	@Id
	@SequenceGenerator(name = "batchradioconfig_seq", sequenceName = "batchradioconfig_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "batchradioconfig_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_batchradioconfig", nullable = false)
	private Long id;

	@Column(name = "string_manufacnumber", nullable = false)
	private String manufacNumber = "2518";

	@Column(name = "string_prodcod", nullable = false)
	private String productCode = "07";

	@Column(name = "string_versao", nullable = false)
	private String version = "00";

	@Column(name = "int_lastassignedserial", nullable = false)
	private int lastAssignedSerial = -1;

	@Column(name = "int_startserialSeq", nullable = false)
	private int startSerialSeq = 0;

	@Column(name = "short_transmitInterval", nullable = false)
	private short transmitInterval = 0;

	@OneToOne
	@JoinColumn(name = "batchId", unique = true)
	private BatchModel batch;

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
	 * Function returns the value of attribute manufacNumber
	 * 
	 * @return the manufacNumber
	 */
	public String getManufacNumber() {
		return manufacNumber;
	}

	/**
	 * Function sets the value for attribute manufacNumber
	 * 
	 * @param manufacNumber
	 *            the manufacNumber to set
	 */
	public void setManufacNumber(String manufacNumber) {
		this.manufacNumber = manufacNumber;
	}

	/**
	 * Function returns the value of attribute productCode
	 * 
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * Function sets the value for attribute productCode
	 * 
	 * @param productCode
	 *            the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/**
	 * Function returns the value of attribute version
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Function sets the value for attribute version
	 * 
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Function returns the value of attribute lastAssignedSerial
	 * 
	 * @return the lastAssignedSerial
	 */
	public int getLastAssignedSerial() {
		return lastAssignedSerial;
	}

	/**
	 * Function sets the value for attribute lastAssignedSerial
	 * 
	 * @param lastAssignedSerial
	 *            the lastAssignedSerial to set
	 */
	public void setLastAssignedSerial(int lastAssignedSerial) {
		this.lastAssignedSerial = lastAssignedSerial;
	}

	/**
	 * Function returns the value of attribute batch
	 * 
	 * @return the batch
	 */
	public BatchModel getBatch() {
		return batch;
	}

	/**
	 * Function sets the value for attribute batch
	 * 
	 * @param batch
	 *            the batch to set
	 */
	public void setBatch(BatchModel batch) {
		this.batch = batch;
	}

	/**
	 * Function returns the value of attribute startSerialSeq
	 * 
	 * @return the startSerialSeq
	 */
	public int getStartSerialSeq() {
		return startSerialSeq;
	}

	/**
	 * Function sets the value for attribute startSerialSeq
	 * 
	 * @param startSerialSeq
	 *            the startSerialSeq to set
	 */
	public void setStartSerialSeq(int startSerialSeq) {
		this.startSerialSeq = startSerialSeq;
	}

	/**
	 * Function returns the value of attribute transmitInterval
	 * 
	 * @return the transmitInterval
	 */
	public short getTransmitInterval() {
		return transmitInterval;
	}

	/**
	 * Function sets the value for attribute transmitInterval
	 * 
	 * @param transmitInterval
	 *            the transmitInterval to set
	 */
	public void setTransmitInterval(short transmitInterval) {
		this.transmitInterval = transmitInterval;
	}

}
