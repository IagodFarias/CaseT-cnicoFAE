//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ProcessConfigModel.java
*    @author marcos
*    @date 10 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author marcos
 *
 */

@Entity
@Table(name = "processconfigmodel")
public class ProcessConfigModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4948385794254852254L;

	@Id
	@SequenceGenerator(name = "seq_process_config", sequenceName = "process_config_model_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_process_config", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_processconfig", nullable = false)
	private long id;

	@Column(name = "string_descricao")
	private String descricao;

	@Column(name = "double_line_pressure_pump_load", nullable = false)
	private double linePressurePumpLoad;

	@Column(name = "int_time_check_pressure_pump_load", nullable = false)
	private int timeCheckPressurePumpLoad;

	@Column(name = "bool_only_verification")
	private boolean isOnlyVerification = false;

	// @Column(name = "bool_is_radio_wmbus")
	@Transient
	private boolean isRadioWmBus = false;

	@Column(name = "bool_zero_flow_enabled")
	private boolean isZeroFlowEnabled = false;

	@Column(name = "bool_chooseQaSamples")
	private boolean chooseQaSamples = false;

	@Column(name = "bool_save_mat_data")
	private boolean saveMatData = false;

	@Column(name = "bool_save_mat_data_reproved_meters")
	private boolean saveMatDataReprovedMeters = false;

	@Column(name = "bool_save_calib_lut_mat_data")
	private boolean saveCalibLutMatData = false;

	@Column(name = "long_time_out_flow_rate", nullable = false)
	private long timeOutFlowRate;

	@Column(name = "long_zero_flow_time", nullable = false)
	private long zeroFlowTime;

	@Column(name = "long_pre_flow_stabilization_time", nullable = false)
	private long preFlowStabilizationTime;

	@Column(name = "long_flow_stability_check_time", nullable = false)
	private long flowStabilityCheckTime;

	@Column(name = "int_init_calib_temp", nullable = false)
	private int initCalibTemp;

	@Column(name = "int_trim_agc_max_tries", nullable = false)
	private int trimAgcMaxTries;

	@Column(name = "int_flow_buff_size", nullable = false)
	private int flowBuffSize;

	@Column(name = "int_window_size", nullable = false)
	private int windowSize;

	@Column(name = "int_meter_date_tries", nullable = false)
	private int meterDateUpdateTries;

	@Column(name = "int_meter_radio_tries", nullable = false)
	private int meterRadioConfigTries;

	@Column(name = "int_meter_download_constants_tries", nullable = false)
	private int meterDownloadConstantsTries;

	@Column(name = "double_perc_to_verif")
	private double percentageToVerif;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "processconfig_flowrate_calib", joinColumns = @JoinColumn(name = "id_processconfig"), inverseJoinColumns = @JoinColumn(name = "id_flowrate"))
	private Set<FlowRateModel> calibFlowRates = new HashSet<FlowRateModel>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "processconfig_flowrate_verif", joinColumns = @JoinColumn(name = "id_processconfig"), inverseJoinColumns = @JoinColumn(name = "id_flowrate"))
	private Set<FlowRateModel> verifFlowRates = new HashSet<FlowRateModel>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "processconfig_calculatedfixedconst", joinColumns = @JoinColumn(name = "id_processconfig"), inverseJoinColumns = @JoinColumn(name = "id_calculatedfixedconstmodel"))
	private Set<CalculatedFixedConstModel> calcFixedConsts = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "processesConfigModel")
	private Set<BateladaModel> batelada = new HashSet<>();

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
	 * Function returns the value of attribute descricao
	 * 
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Function sets the value for attribute descricao
	 * 
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Function returns the value of attribute linePressurePumpLoad
	 * 
	 * @return the linePressurePumpLoad
	 */
	public double getLinePressurePumpLoad() {
		return linePressurePumpLoad;
	}

	/**
	 * Function sets the value for attribute linePressurePumpLoad
	 * 
	 * @param linePressurePumpLoad
	 *            the linePressurePumpLoad to set
	 */
	public void setLinePressurePumpLoad(double linePressurePumpLoad) {
		this.linePressurePumpLoad = linePressurePumpLoad;
	}

	/**
	 * Function returns the value of attribute timeCheckPressurePumpLoad
	 * 
	 * @return the timeCheckPressurePumpLoad
	 */
	public int getTimeCheckPressurePumpLoad() {
		return timeCheckPressurePumpLoad;
	}

	/**
	 * Function sets the value for attribute timeCheckPressurePumpLoad
	 * 
	 * @param timeCheckPressurePumpLoad
	 *            the timeCheckPressurePumpLoad to set
	 */
	public void setTimeCheckPressurePumpLoad(int timeCheckPressurePumpLoad) {
		this.timeCheckPressurePumpLoad = timeCheckPressurePumpLoad;
	}

	/**
	 * Function returns the value of attribute isZeroFlowEnabled
	 * 
	 * @return the isZeroFlowEnabled
	 */
	public boolean isZeroFlowEnabled() {
		return isZeroFlowEnabled;
	}

	/**
	 * Function sets the value for attribute isZeroFlowEnabled
	 * 
	 * @param isZeroFlowEnabled
	 *            the isZeroFlowEnabled to set
	 */
	public void setZeroFlowEnabled(boolean isZeroFlowEnabled) {
		this.isZeroFlowEnabled = isZeroFlowEnabled;
	}

	/**
	 * Function returns the value of attribute chooseQaSamples
	 * 
	 * @return the chooseQaSamples
	 */
	public boolean isChooseQaSamples() {
		return chooseQaSamples;
	}

	/**
	 * Function sets the value for attribute chooseQaSamples
	 * 
	 * @param chooseQaSamples
	 *            the chooseQaSamples to set
	 */
	public void setChooseQaSamples(boolean chooseQaSamples) {
		this.chooseQaSamples = chooseQaSamples;
	}

	/**
	 * Function returns the value of attribute saveMatData
	 * 
	 * @return the saveMatData
	 */
	public boolean isSaveMatData() {
		return saveMatData;
	}

	/**
	 * Function sets the value for attribute saveMatData
	 * 
	 * @param saveMatData
	 *            the saveMatData to set
	 */
	public void setSaveMatData(boolean saveMatData) {
		this.saveMatData = saveMatData;
	}

	/**
	 * Function returns the value of attribute saveMatDataReprovedMeters
	 * 
	 * @return the saveMatDataReprovedMeters
	 */
	public boolean isSaveMatDataReprovedMeters() {
		return saveMatDataReprovedMeters;
	}

	/**
	 * Function sets the value for attribute saveMatDataReprovedMeters
	 * 
	 * @param saveMatDataReprovedMeters
	 *            the saveMatDataReprovedMeters to set
	 */
	public void setSaveMatDataReprovedMeters(boolean saveMatDataReprovedMeters) {
		this.saveMatDataReprovedMeters = saveMatDataReprovedMeters;
	}

	/**
	 * Function returns the value of attribute saveCalibLutMatData
	 * 
	 * @return the saveCalibLutMatData
	 */
	public boolean isSaveCalibLutMatData() {
		return saveCalibLutMatData;
	}

	/**
	 * Function sets the value for attribute saveCalibLutMatData
	 * 
	 * @param saveCalibLutMatData
	 *            the saveCalibLutMatData to set
	 */
	public void setSaveCalibLutMatData(boolean saveCalibLutMatData) {
		this.saveCalibLutMatData = saveCalibLutMatData;
	}

	/**
	 * Function returns the value of attribute timeOutFlowRate
	 * 
	 * @return the timeOutFlowRate
	 */
	public long getTimeOutFlowRate() {
		return timeOutFlowRate;
	}

	/**
	 * Function sets the value for attribute timeOutFlowRate
	 * 
	 * @param timeOutFlowRate
	 *            the timeOutFlowRate to set
	 */
	public void setTimeOutFlowRate(long timeOutFlowRate) {
		this.timeOutFlowRate = timeOutFlowRate;
	}

	/**
	 * Function returns the value of attribute zeroFlowTime
	 * 
	 * @return the zeroFlowTime
	 */
	public long getZeroFlowTime() {
		return zeroFlowTime;
	}

	/**
	 * Function sets the value for attribute zeroFlowTime
	 * 
	 * @param zeroFlowTime
	 *            the zeroFlowTime to set
	 */
	public void setZeroFlowTime(long zeroFlowTime) {
		this.zeroFlowTime = zeroFlowTime;
	}

	/**
	 * Function returns the value of attribute preFlowStabilizationTime
	 * 
	 * @return the preFlowStabilizationTime
	 */
	public long getPreFlowStabilizationTime() {
		return preFlowStabilizationTime;
	}

	/**
	 * Function sets the value for attribute preFlowStabilizationTime
	 * 
	 * @param preFlowStabilizationTime
	 *            the preFlowStabilizationTime to set
	 */
	public void setPreFlowStabilizationTime(long preFlowStabilizationTime) {
		this.preFlowStabilizationTime = preFlowStabilizationTime;
	}

	/**
	 * Function returns the value of attribute flowStabilityCheckTime
	 * 
	 * @return the flowStabilityCheckTime
	 */
	public long getFlowStabilityCheckTime() {
		return flowStabilityCheckTime;
	}

	/**
	 * Function sets the value for attribute flowStabilityCheckTime
	 * 
	 * @param flowStabilityCheckTime
	 *            the flowStabilityCheckTime to set
	 */
	public void setFlowStabilityCheckTime(long flowStabilityCheckTime) {
		this.flowStabilityCheckTime = flowStabilityCheckTime;
	}

	/**
	 * Function returns the value of attribute initCalibTemp
	 * 
	 * @return the initCalibTemp
	 */
	public int getInitCalibTemp() {
		return initCalibTemp;
	}

	/**
	 * Function sets the value for attribute initCalibTemp
	 * 
	 * @param initCalibTemp
	 *            the initCalibTemp to set
	 */
	public void setInitCalibTemp(int initCalibTemp) {
		this.initCalibTemp = initCalibTemp;
	}

	/**
	 * Function returns the value of attribute trimAgcMaxTries
	 * 
	 * @return the trimAgcMaxTries
	 */
	public int getTrimAgcMaxTries() {
		return trimAgcMaxTries;
	}

	/**
	 * Function sets the value for attribute trimAgcMaxTries
	 * 
	 * @param trimAgcMaxTries
	 *            the trimAgcMaxTries to set
	 */
	public void setTrimAgcMaxTries(int trimAgcMaxTries) {
		this.trimAgcMaxTries = trimAgcMaxTries;
	}

	/**
	 * Function returns the value of attribute flowBuffSize
	 * 
	 * @return the flowBuffSize
	 */
	public int getFlowBuffSize() {
		return flowBuffSize;
	}

	/**
	 * Function sets the value for attribute flowBuffSize
	 * 
	 * @param flowBuffSize
	 *            the flowBuffSize to set
	 */
	public void setFlowBuffSize(int flowBuffSize) {
		this.flowBuffSize = flowBuffSize;
	}

	/**
	 * Function returns the value of attribute windowSize
	 * 
	 * @return the windowSize
	 */
	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * Function sets the value for attribute windowSize
	 * 
	 * @param windowSize
	 *            the windowSize to set
	 */
	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	/**
	 * Function returns the value of attribute isOnlyVerification
	 * 
	 * @return the isOnlyVerification
	 */
	public boolean isOnlyVerification() {
		return isOnlyVerification;
	}

	/**
	 * Function sets the value for attribute isOnlyVerification
	 * 
	 * @param isOnlyVerification
	 *            the isOnlyVerification to set
	 */
	public void setOnlyVerification(boolean isOnlyVerification) {
		this.isOnlyVerification = isOnlyVerification;
	}

	/**
	 * Function returns the value of attribute isRadioWm_bus
	 * 
	 * @return the isRadioWm_bus
	 */
	public boolean isRadioWmBus() {
		return isRadioWmBus;
	}

	/**
	 * Function sets the value for attribute isRadioWm_bus
	 * 
	 * @param isRadioWm_bus
	 *            the isRadioWm_bus to set
	 */
	public void setRadioWmBus(boolean isRadioWmBus) {
		this.isRadioWmBus = isRadioWmBus;
	}

	/**
	 * Function returns the value of attribute meterDateUpdateTries
	 * 
	 * @return the meterDateUpdateTries
	 */
	public int getMeterDateUpdateTries() {
		return meterDateUpdateTries;
	}

	/**
	 * Function sets the value for attribute meterDateUpdateTries
	 * 
	 * @param meterDateUpdateTries
	 *            the meterDateUpdateTries to set
	 */
	public void setMeterDateUpdateTries(int meterDateUpdateTries) {
		this.meterDateUpdateTries = meterDateUpdateTries;
	}

	/**
	 * Function returns the value of attribute meterRadioConfigTries
	 * 
	 * @return the meterRadioConfigTries
	 */
	public int getMeterRadioConfigTries() {
		return meterRadioConfigTries;
	}

	/**
	 * Function sets the value for attribute meterRadioConfigTries
	 * 
	 * @param meterRadioConfigTries
	 *            the meterRadioConfigTries to set
	 */
	public void setMeterRadioConfigTries(int meterRadioConfigTries) {
		this.meterRadioConfigTries = meterRadioConfigTries;
	}

	/**
	 * Function returns the value of attribute meterDownloadConstantsTries
	 * 
	 * @return the meterDownloadConstantsTries
	 */
	public int getMeterDownloadConstantsTries() {
		return meterDownloadConstantsTries;
	}

	/**
	 * Function sets the value for attribute meterDownloadConstantsTries
	 * 
	 * @param meterDownloadConstantsTries
	 *            the meterDownloadConstantsTries to set
	 */
	public void setMeterDownloadConstantsTries(int meterDownloadConstantsTries) {
		this.meterDownloadConstantsTries = meterDownloadConstantsTries;
	}

	/**
	 * Function returns the value of attribute percentageToVerif
	 * 
	 * @return the percentageToVerif
	 */
	public double getPercentageToVerif() {
		return percentageToVerif;
	}

	/**
	 * Function sets the value for attribute percentageToVerif
	 * 
	 * @param percentageToVerif
	 *            the percentageToVerif to set
	 */
	public void setPercentageToVerif(double percentageToVerif) {
		this.percentageToVerif = percentageToVerif;
	}

	/**
	 * Function returns the value of attribute calibFlowRates
	 * 
	 * @return the calibFlowRates
	 */
	public Set<FlowRateModel> getCalibFlowRates() {
		return calibFlowRates;
	}

	/**
	 * Function sets the value for attribute calibFlowRates
	 * 
	 * @param calibFlowRates
	 *            the calibFlowRates to set
	 */
	public void setCalibFlowRates(Set<FlowRateModel> calibFlowRates) {
		this.calibFlowRates = calibFlowRates;
	}

	/**
	 * Function returns the value of attribute verifFlowRates
	 * 
	 * @return the verifFlowRates
	 */
	public Set<FlowRateModel> getVerifFlowRates() {
		return verifFlowRates;
	}

	/**
	 * Function sets the value for attribute verifFlowRates
	 * 
	 * @param verifFlowRates
	 *            the verifFlowRates to set
	 */
	public void setVerifFlowRates(Set<FlowRateModel> verifFlowRates) {
		this.verifFlowRates = verifFlowRates;
	}

	/**
	 * Function returns the value of attribute calcFixedConsts
	 * 
	 * @return the calcFixedConsts
	 */
	public Set<CalculatedFixedConstModel> getCalcFixedConsts() {
		return calcFixedConsts;
	}

	/**
	 * Function sets the value for attribute calcFixedConsts
	 * 
	 * @param calcFixedConsts
	 *            the calcFixedConsts to set
	 */
	public void setCalcFixedConsts(Set<CalculatedFixedConstModel> calcFixedConsts) {
		this.calcFixedConsts = calcFixedConsts;
	}

	/**
	 * Function returns the value of attribute batelada
	 * 
	 * @return the batelada
	 */
	public Set<BateladaModel> getBatelada() {
		return batelada;
	}

	/**
	 * Function sets the value for attribute batelada
	 * 
	 * @param batelada
	 *            the batelada to set
	 */
	public void setBatelada(Set<BateladaModel> batelada) {
		this.batelada = batelada;
	}
}
