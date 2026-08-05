//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterModel.java
*    @author marcos
*    @date 21 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "metermodel")
public class MeterModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5155995592652654540L;

	@Id
	@SequenceGenerator(name = "meter_seq", sequenceName = "meter_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "meter_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_meter", nullable = false)
	private Long id;

	@Column(name = "int_meterufoid", nullable = true)
	private int meterUfoId;

	// @Column(name = "string_fwversion", nullable = true)
	// private String fwVersion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_dateofcalibration", nullable = true)
	private Date dateOfCalibration;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_dateofreplace", nullable = true)
	private Date dateOfReplace;

	@Column(name = "string_serialnumber", unique = true)
	private String serialNumber;

	// @Column(name = "string_description") // MeterTypeModel description
	// private String description;

	@OneToMany(mappedBy = "meter")
	private Set<MeterDataModel> data = new HashSet<>();

	@Column(name = "double_dsos")
	private double dsos;

	@Column(name = "double_dzc")
	private double dzc;

	@Column(name = "double_estimatedpathlength")
	private double estimatedPathlength;

	//
	@Column(name = "bool_downloadedinitcontants")
	private boolean isDownloadedInitContants = false;

	@Column(name = "bool_approvedagc")
	private boolean isApprovedAgcTrim = false;

	@Column(name = "bool_deviationzeroflow")
	private boolean isDeviationZeroFlow = false;

	@Column(name = "bool_calculatedzeroflow")
	private boolean isCalculatedZeroFlow = false;

	@Column(name = "bool_downloadedzeroflow")
	private boolean isDownloadedZeroFlow = false;

	@Column(name = "bool_calculatedconstants")
	private boolean isCalculatedConstants = false;

	@Column(name = "bool_downloadedconstants")
	private boolean isDownloadedConstants = false;

	@Column(name = "bool_calculatederrors")
	private boolean isCalculatedErrors = false;

	@Column(name = "bool_approvedverification")
	private boolean isApprovedVerification = false;

	//
	@Column(name = "bool_meter_serial_number_configured")
	private boolean isMeterSerialNumberConfigured = false;

	@Column(name = "bool_wmbus_configured")
	private boolean isWmBusConfigured = false;

	@Column(name = "bool_meter_system_date_configured")
	private boolean isMeterSystemDateConfigured = false;

	@Column(name = "bool_meter_replace_date_configured")
	private boolean isMeterReplaceDateConfigured = false;

	@Column(name = "bool_qa")
	private boolean isQa = false;

	@Column(name = "bool_commdown")
	private boolean isCommDown = false;

	// @Column(name = "bool_calculatedDistanceFixedconstants")
	// private boolean isCalculatedEstimatedFixedConstants = false;

	@Transient
	private boolean isAvailableSN = true;
	//
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "meter")
	@JoinColumn(name = "id_radioWmBusConfig")
	private RadioWmBusConfigModel radioWmBusConfig;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_conversor")
	private ConversorModel conversor;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_batelada")
	private BateladaModel batelada;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_batch")
	private BatchModel batch;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_meter_type")
	private MeterTypeModel meterTypeModel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_firmware")
	private FirmwareModel firmware;

	@OneToMany(mappedBy = "meter", fetch = FetchType.LAZY)
	private Set<CalibConstantsModel> calibConstants = new HashSet<>();

	@OneToMany(mappedBy = "meter", fetch = FetchType.LAZY)
	private Set<VerificationErrorModel> errors = new HashSet<>();

	@OneToMany(mappedBy = "meter", fetch = FetchType.LAZY)
	private Set<VolumetricErrorModel> volumetricErrors = new HashSet<>();

	//
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "meter")
	@JoinColumn(name = "id_qaAssured")
	private QaAssuredModel QaAssured;

	/**
	 * Function returns the value of attribute data
	 * 
	 * @return the data
	 */
	public Set<MeterDataModel> getData() {
		return data;
	}

	/**
	 * Function sets the value for attribute data
	 * 
	 * @param data
	 *            the data to set
	 */
	public void setData(Set<MeterDataModel> data) {
		this.data = data;
	}

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
	 * Function returns the value of attribute meterUfoId
	 * 
	 * @return the meterUfoId
	 */
	public int getMeterUfoId() {
		return meterUfoId;
	}

	/**
	 * Function sets the value for attribute meterUfoId
	 * 
	 * @param meterUfoId
	 *            the meterUfoId to set
	 */
	public void setMeterUfoId(int meterUfoId) {
		this.meterUfoId = meterUfoId;
	}

	// /**
	// * Function returns the value of attribute fwVersion
	// *
	// * @return the fwVersion
	// */
	// public String getFwVersion() {
	// return fwVersion;
	// }
	//
	// /**
	// * Function sets the value for attribute fwVersion
	// *
	// * @param fwVersion
	// * the fwVersion to set
	// */
	// public void setFwVersion(String fwVersion) {
	// this.fwVersion = fwVersion;
	// }

	/**
	 * Function returns the value of attribute date
	 * 
	 * @return the date
	 */
	public Date getDateOfCalibration() {
		return dateOfCalibration;
	}

	/**
	 * Function sets the value for attribute date
	 * 
	 * @param date
	 *            the date to set
	 */
	public void setDateOfCalibration(Date date) {
		this.dateOfCalibration = date;
	}

	/**
	 * Function returns the value of attribute dateOfReplace
	 * 
	 * @return the dateOfReplace
	 */
	public Date getDateOfReplace() {
		return dateOfReplace;
	}

	/**
	 * Function sets the value for attribute dateOfReplace
	 * 
	 * @param dateOfReplace
	 *            the dateOfReplace to set
	 */
	public void setDateOfReplace(Date dateOfReplace) {
		this.dateOfReplace = dateOfReplace;
	}

	/**
	 * Function returns the value of attribute serialNumber
	 * 
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Function sets the value for attribute serialNumber
	 * 
	 * @param serialNumber
	 *            the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	// /**
	// * Function returns the value of attribute description
	// *
	// * @return the description
	// */
	// public String getDescription() {
	// return description;
	// }
	//
	// /**
	// * Function sets the value for attribute description
	// *
	// * @param description
	// * the description to set
	// */
	// public void setDescription(String description) {
	// this.description = description;
	// }

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
	 * Function returns the value of attribute dsos
	 * 
	 * @return the dsos
	 */
	public double getDsos() {
		return dsos;
	}

	/**
	 * Function sets the value for attribute dsos
	 * 
	 * @param dsos
	 *            the dsos to set
	 */
	public void setDsos(double dsos) {
		this.dsos = dsos;
	}

	/**
	 * Function returns the value of attribute dzc
	 * 
	 * @return the dzc
	 */
	public double getDzc() {
		return dzc;
	}

	/**
	 * Function sets the value for attribute dzc
	 * 
	 * @param dzc
	 *            the dzc to set
	 */
	public void setDzc(double dzc) {
		this.dzc = dzc;
	}

	/**
	 * @return the estimatedPathlength
	 */
	public double getEstimatedPathlength() {
		return estimatedPathlength;
	}

	/**
	 * @param estimatedPathlength
	 *            the estimatedPathlength to set
	 */
	public void setEstimatedPathlength(double estimatedPathlength) {
		this.estimatedPathlength = estimatedPathlength;
	}

	//
	/**
	 * @return the isDownloadedInitContants
	 */
	public boolean isDownloadedInitContants() {
		return isDownloadedInitContants;
	}

	/**
	 * @param isDownloadedInitContants
	 *            the isDownloadedInitContants to set
	 */
	public void setDownloadedInitContants(boolean isDownloadedInitContants) {
		this.isDownloadedInitContants = isDownloadedInitContants;
	}

	/**
	 * @return the isApprovedAgcTrim
	 */
	public boolean isApprovedAgcTrim() {
		return isApprovedAgcTrim;
	}

	/**
	 * @param isApprovedAgcTrim
	 *            the isApprovedAgcTrim to set
	 */
	public void setApprovedAgcTrim(boolean isApprovedAgcTrim) {
		this.isApprovedAgcTrim = isApprovedAgcTrim;
	}

	/**
	 * @return the isDeviationZeroFlow
	 */
	public boolean isDeviationZeroFlow() {
		return isDeviationZeroFlow;
	}

	/**
	 * @param isDeviationZeroFlow
	 *            the isDeviationZeroFlow to set
	 */
	public void setDeviationZeroFlow(boolean isDeviationZeroFlow) {
		this.isDeviationZeroFlow = isDeviationZeroFlow;
	}

	/**
	 * @return the isCalculatedZeroFlow
	 */
	public boolean isCalculatedZeroFlow() {
		return isCalculatedZeroFlow;
	}

	/**
	 * @param isCalculatedZeroFlow
	 *            the isCalculatedZeroFlow to set
	 */
	public void setCalculatedZeroFlow(boolean isCalculatedZeroFlow) {
		this.isCalculatedZeroFlow = isCalculatedZeroFlow;
	}

	/**
	 * @return the isDownloadedZeroFlow
	 */
	public boolean isDownloadedZeroFlow() {
		return isDownloadedZeroFlow;
	}

	/**
	 * @param isDownloadedZeroFlow
	 *            the isDownloadedZeroFlow to set
	 */
	public void setDownloadedZeroFlow(boolean isDownloadedZeroFlow) {
		this.isDownloadedZeroFlow = isDownloadedZeroFlow;
	}

	/**
	 * @return the isCalculatedConstants
	 */
	public boolean isCalculatedConstants() {
		return isCalculatedConstants;
	}

	/**
	 * @param isCalculatedConstants
	 *            the isCalculatedConstants to set
	 */
	public void setCalculatedConstants(boolean isCalculatedConstants) {
		this.isCalculatedConstants = isCalculatedConstants;
	}

	/**
	 * @return the isDownloadedConstants
	 */
	public boolean isDownloadedConstants() {
		return isDownloadedConstants;
	}

	/**
	 * @param isDownloadedConstants
	 *            the isDownloadedConstants to set
	 */
	public void setDownloadedConstants(boolean isDownloadedConstants) {
		this.isDownloadedConstants = isDownloadedConstants;
	}

	/**
	 * @return the isCalculatedErrors
	 */
	public boolean isCalculatedErrors() {
		return isCalculatedErrors;
	}

	/**
	 * @param isCalculatedErrors
	 *            the isCalculatedErrors to set
	 */
	public void setCalculatedErrors(boolean isCalculatedErrors) {
		this.isCalculatedErrors = isCalculatedErrors;
	}

	/**
	 * @return the isApprovedVerification
	 */
	public boolean isApprovedVerification() {
		return isApprovedVerification;
	}

	/**
	 * @param isApprovedVerification
	 *            the isApprovedVerification to set
	 */
	public void setApprovedVerification(boolean isApprovedVerification) {
		this.isApprovedVerification = isApprovedVerification;
	}

	/**
	 * Function returns the value of attribute meterTypeModel
	 * 
	 * @return the meterTypeModel
	 */
	public MeterTypeModel getMeterTypeModel() {
		return meterTypeModel;
	}

	/**
	 * Function sets the value for attribute meterTypeModel
	 * 
	 * @param meterTypeModel
	 *            the meterTypeModel to set
	 */
	public void setMeterTypeModel(MeterTypeModel meterTypeModel) {
		this.meterTypeModel = meterTypeModel;
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
	 * Function returns the value of attribute calibConstants
	 * 
	 * @return the calibConstants
	 */
	public Set<CalibConstantsModel> getCalibConstants() {
		return calibConstants;
	}

	/**
	 * Function sets the value for attribute calibConstants
	 * 
	 * @param calibConstants
	 *            the calibConstants to set
	 */
	public void setCalibConstants(Set<CalibConstantsModel> calibConstants) {
		this.calibConstants = calibConstants;
	}

	/**
	 * Function returns the value of attribute isMeterSerialNumberConfigured
	 * 
	 * @return the isMeterSerialNumberConfigured
	 */
	public boolean isMeterSerialNumberConfigured() {
		return isMeterSerialNumberConfigured;
	}

	/**
	 * Function sets the value for attribute isMeterSerialNumberConfigured
	 * 
	 * @param isMeterSerialNumberConfigured
	 *            the isMeterSerialNumberConfigured to set
	 */
	public void setMeterSerialNumberConfigured(boolean isMeterSerialNumberConfigured) {
		this.isMeterSerialNumberConfigured = isMeterSerialNumberConfigured;
	}

	/**
	 * Function returns the value of attribute isWmBusConfigured
	 * 
	 * @return the isWmBusConfigured
	 */
	public boolean isWmBusConfigured() {
		return isWmBusConfigured;
	}

	/**
	 * Function sets the value for attribute isWmBusConfigured
	 * 
	 * @param isWmBusConfigured
	 *            the isWmBusConfigured to set
	 */
	public void setWmBusConfigured(boolean isWmBusConfigured) {
		this.isWmBusConfigured = isWmBusConfigured;
	}

	/**
	 * Function returns the value of attribute isMeterSystemDateConfigured
	 * 
	 * @return the isMeterSystemDateConfigured
	 */
	public boolean isMeterSystemDateConfigured() {
		return isMeterSystemDateConfigured;
	}

	/**
	 * Function sets the value for attribute isMeterSystemDateConfigured
	 * 
	 * @param isMeterSystemDateConfigured
	 *            the isMeterSystemDateConfigured to set
	 */
	public void setMeterSystemDateConfigured(boolean isMeterSystemDateConfigured) {
		this.isMeterSystemDateConfigured = isMeterSystemDateConfigured;
	}

	/**
	 * Function returns the value of attribute isMeterReplaceDateConfigured
	 * 
	 * @return the isMeterReplaceDateConfigured
	 */
	public boolean isMeterReplaceDateConfigured() {
		return isMeterReplaceDateConfigured;
	}

	/**
	 * Function sets the value for attribute isMeterReplaceDateConfigured
	 * 
	 * @param isMeterReplaceDateConfigured
	 *            the isMeterReplaceDateConfigured to set
	 */
	public void setMeterReplaceDateConfigured(boolean isMeterReplaceDateConfigured) {
		this.isMeterReplaceDateConfigured = isMeterReplaceDateConfigured;
	}

	/**
	 * Function returns the value of attribute isQa
	 * 
	 * @return the isQa
	 */
	public boolean isQa() {
		return isQa;
	}

	/**
	 * Function sets the value for attribute isQa
	 * 
	 * @param isQa
	 *            the isQa to set
	 */
	public void setQa(boolean isQa) {
		this.isQa = isQa;
	}

	/**
	 * Function returns the value of attribute isCommDown
	 * 
	 * @return the isCommDown
	 */
	public boolean isCommDown() {
		return isCommDown;
	}

	/**
	 * Function sets the value for attribute isCommDown
	 * 
	 * @param isCommDown
	 *            the isCommDown to set
	 */
	public void setCommDown(boolean isCommDown) {
		this.isCommDown = isCommDown;
	}

	/**
	 * Function returns the value of attribute isAvailableSN
	 * 
	 * @return the isAvailableSN
	 */
	public boolean isAvailableSN() {
		return isAvailableSN;
	}

	/**
	 * Function sets the value for attribute isAvailableSN
	 * 
	 * @param isAvailableSN
	 *            the isAvailableSN to set
	 */
	public void setAvailableSN(boolean isAvailableSN) {
		this.isAvailableSN = isAvailableSN;
	}

	/**
	 * Function returns the value of attribute radioWmBusConfig
	 * 
	 * @return the radioWmBusConfig
	 */
	public RadioWmBusConfigModel getRadioWmBusConfig() {
		return radioWmBusConfig;
	}

	/**
	 * Function sets the value for attribute radioWmBusConfig
	 * 
	 * @param radioWmBusConfig
	 *            the radioWmBusConfig to set
	 */
	public void setRadioWmBusConfig(RadioWmBusConfigModel radioWmBusConfig) {
		this.radioWmBusConfig = radioWmBusConfig;
	}

	/**
	 * Function returns the value of attribute conversor
	 * 
	 * @return the conversor
	 */
	public ConversorModel getConversor() {
		return conversor;
	}

	/**
	 * Function sets the value for attribute conversor
	 * 
	 * @param conversor
	 *            the conversor to set
	 */
	public void setConversor(ConversorModel conversor) {
		this.conversor = conversor;
	}

	/**
	 * Function returns the value of attribute batelada
	 * 
	 * @return the batelada
	 */
	public BateladaModel getBatelada() {
		return batelada;
	}

	/**
	 * Function sets the value for attribute batelada
	 * 
	 * @param batelada
	 *            the batelada to set
	 */
	public void setBatelada(BateladaModel batelada) {
		this.batelada = batelada;
	}

	/**
	 * Function returns the value of attribute errors
	 * 
	 * @return the errors
	 */
	public Set<VerificationErrorModel> getErrors() {
		return errors;
	}

	/**
	 * Function sets the value for attribute errors
	 * 
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(Set<VerificationErrorModel> errors) {
		this.errors = errors;
	}

	/**
	 * Function returns the value of attribute qaAssured
	 * 
	 * @return the qaAssured
	 */
	public QaAssuredModel getQaAssured() {
		return QaAssured;
	}

	/**
	 * Function sets the value for attribute qaAssured
	 * 
	 * @param qaAssured
	 *            the qaAssured to set
	 */
	public void setQaAssured(QaAssuredModel qaAssured) {
		QaAssured = qaAssured;
	}
}
