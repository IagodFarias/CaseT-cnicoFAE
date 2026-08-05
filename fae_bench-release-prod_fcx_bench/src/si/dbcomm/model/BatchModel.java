//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BatchModel.java
*    @author marcos
*    @date 16 de ago de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "batchmodel")
public class BatchModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6992257350261714474L;

	@Id
	@SequenceGenerator(name = "batch_seq", sequenceName = "batch_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "batch_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_batch", nullable = false)
	private Long id;

	@Column(name = "string_batchid", nullable = false, unique = true)
	private String batchId;

	@Column(name = "string_description", nullable = true, unique = true)
	private String description;

	@OneToMany(mappedBy = "batch")
	private Set<MeterModel> meters = new HashSet<>();

	@Column(name = "int_nummeters", nullable = false)
	private int numMeters;

	@Column(name = "int_startserialSeq", nullable = false)
	private int startSerialSeq = 0;

	@Column(name = "int_lastassignedserial", nullable = true)
	private int lastAssignedSerialSeq = -1;

	@Column(name = "string_manufacturercods", nullable = false)
	private String manufacturerCode; // F (fae)

	@Temporal(TemporalType.DATE)
	@Column(name = "date_yearofmanufacture", nullable = false)
	private Date yearOfManufacture;

	// @Column(name = "string_fwversion", nullable = false)
	// private String fwVersion;

	@Column(name = "bool_finished")
	private boolean finished = false;
	
//	@Column(name = "double_litter_pulse")
//	private double litterPulse = 10;

	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_client", nullable = false)
	private ClientModel client;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_meter_type", nullable = false)
	private MeterTypeModel meterType;

	@OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<BateladaModel> bateladas = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_firmware", nullable = true)
	private FirmwareModel firmware;

	// Osmenio - 07/06/18 - 09:00
	@Column(name = "long_idxprocessconfig", nullable = true)
	private long idxProcessConfig = -1;

	@Transient
	private boolean isSelected = false;

	@Transient
	private boolean isReWork = false;

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
	 * Function returns the value of attribute batchId
	 * 
	 * @return the batchId
	 */
	public String getBatchId() {
		return batchId;
	}

	/**
	 * Function sets the value for attribute batchId
	 * 
	 * @param batchId
	 *            the batchId to set
	 */
	public void setBatchId(String batchId) {
		this.batchId = batchId;
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
	 * Function returns the value of attribute meters
	 * 
	 * @return the meters
	 */
	public Set<MeterModel> getMeters() {
		return meters;
	}

	/**
	 * Function sets the value for attribute meters
	 * 
	 * @param meters
	 *            the meters to set
	 */
	public void setMeters(Set<MeterModel> meters) {
		this.meters = meters;
	}

	/**
	 * Function returns the value of attribute numMeters
	 * 
	 * @return the numMeters
	 */
	public int getNumMeters() {
		return numMeters;
	}

	/**
	 * Function sets the value for attribute numMeters
	 * 
	 * @param numMeters
	 *            the numMeters to set
	 */
	public void setNumMeters(int numMeters) {
		this.numMeters = numMeters;
	}

	/**
	 * Function returns the value of attribute startSerial
	 * 
	 * @return the startSerial
	 */
	public int getStartSerialSeq() {
		return startSerialSeq;
	}

	/**
	 * Function sets the value for attribute startSerial
	 * 
	 * @param startSerial
	 *            the startSerial to set
	 */
	public void setStartSerialSeq(int startSerial) {
		this.startSerialSeq = startSerial;
	}

	/**
	 * Function returns the value of attribute client
	 * 
	 * @return the client
	 */
	public ClientModel getClient() {
		return client;
	}

	/**
	 * Function sets the value for attribute client
	 * 
	 * @param client
	 *            the client to set
	 */
	public void setClient(ClientModel client) {
		this.client = client;
	}

	/**
	 * Function returns the value of attribute meterType
	 * 
	 * @return the meterType
	 */
	public MeterTypeModel getMeterType() {
		return meterType;
	}

	/**
	 * Function sets the value for attribute meterType
	 * 
	 * @param meterType
	 *            the meterType to set
	 */
	public void setMeterType(MeterTypeModel meterType) {
		this.meterType = meterType;
	}

	// /**
	// * @return the fwVersion
	// */
	// public String getFwVersion() {
	// return fwVersion;
	// }
	//
	// /**
	// * @param fwVersion
	// * the fwVersion to set
	// */
	// public void setFwVersion(String fwVersion) {
	// this.fwVersion = fwVersion;
	// }

	/**
	 * Function returns the value of attribute finished
	 * 
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Function sets the value for attribute finished
	 * 
	 * @param finished
	 *            the finished to set
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * Function returns the value of attribute bateladas
	 * 
	 * @return the bateladas
	 */
	public Set<BateladaModel> getBateladas() {
		return bateladas;
	}

	/**
	 * Function sets the value for attribute bateladas
	 * 
	 * @param bateladas
	 *            the bateladas to set
	 */
	public void setBateladas(Set<BateladaModel> bateladas) {
		this.bateladas = bateladas;
	}

	/**
	 * @return the firmware
	 */
	public FirmwareModel getFirmware() {
		return firmware;
	}

	/**
	 * @param firmware
	 *            the firmware to set
	 */
	public void setFirmware(FirmwareModel firmware) {
		this.firmware = firmware;
	}

	/**
	 * Function returns the value of attribute lastAssignedSerial
	 * 
	 * @return the lastAssignedSerial
	 */
	public int getLastAssignedSerialSeq() {
		return lastAssignedSerialSeq;
	}

	/**
	 * Function sets the value for attribute lastAssignedSerial
	 * 
	 * @param lastAssignedSerial
	 *            the lastAssignedSerial to set
	 */
	public void setLastAssignedSerialSeq(int lastAssignedSerial) {
		this.lastAssignedSerialSeq = lastAssignedSerial;
	}

	/**
	 * Function returns the value of attribute manufacturerCode
	 * 
	 * @return the manufacturerCode
	 */
	public String getManufacturerCode() {
		return manufacturerCode;
	}

	/**
	 * Function sets the value for attribute manufacturerCode
	 * 
	 * @param manufacturerCode
	 *            the manufacturerCode to set
	 */
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	/**
	 * Function returns the value of attribute yearOfManufacture
	 * 
	 * @return the yearOfManufacture
	 */
	public Date getYearOfManufacture() {
		return yearOfManufacture;
	}

	/**
	 * Function sets the value for attribute yearOfManufacture
	 * 
	 * @param yearOfManufacture
	 *            the yearOfManufacture to set
	 */
	public void setYearOfManufacture(Date yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	/**
	 * Function returns the value of attribute idxProcessConfig
	 * 
	 * @return the idxProcessConfig
	 */
	public long getIdxProcessConfig() {
		return idxProcessConfig;
	}

	/**
	 * Function sets the value for attribute idxProcessConfig
	 * 
	 * @param idxProcessConfig
	 *            the idxProcessConfig to set
	 */
	public void setIdxProcessConfig(long idxProcessConfig) {
		this.idxProcessConfig = idxProcessConfig;
	}

	/**
	 * 
	 */
	public int getLeftProduce() {
		int lastProduced = lastAssignedSerialSeq;
		if (lastAssignedSerialSeq == -1)
			lastProduced = startSerialSeq;
		int count = numMeters - (lastProduced - startSerialSeq);
		return count;
	}

	/**
	 * Function returns the value of attribute isSelected
	 * 
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * Function sets the value for attribute isSelected
	 * 
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * Function returns the value of attribute isReWork
	 * 
	 * @return the isReWork
	 */
	public boolean isReWork() {
		return isReWork;
	}

	/**
	 * Function sets the value for attribute isReWork
	 * 
	 * @param isReWork
	 *            the isReWork to set
	 */
	public void setReWork(boolean isReWork) {
		this.isReWork = isReWork;
	}

//	public double getLitterPulse() {
//		return litterPulse;
//	}
//
//	public void setLitterPulse(double litterPulse) {
//		this.litterPulse = litterPulse;
//	}
	
	@Override
	public String toString() {
		// return this.description;
		return this.batchId;
	}
}
