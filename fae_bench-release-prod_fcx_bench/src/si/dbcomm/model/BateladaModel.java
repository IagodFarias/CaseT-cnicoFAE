//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BateladaModel.java
*    @author marcos
*    @date 10 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import si.dbcomm.enumerations.CalibrationTypeEnum;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "bateladamodel")
public class BateladaModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1069367919902973800L;

	@Id
	@SequenceGenerator(name = "batelada_seq", sequenceName = "batelada_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "batelada_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_batelada", nullable = false)
	private Long id;

	@Column(name = "int_runcount", nullable = false)
	private int runCount;

	@Temporal(TemporalType.TIMESTAMP)
	private Date initTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "int_connected_meters", nullable = true)
	private int connectedMeters = 0; // On verification

	@Column(name = "int_approved_meters", nullable = true)
	private int approvedMeters = 0; // On verification

	@Column(name = "int_reproved_meters_down_init_const", nullable = true)
	private int reprovedMetersOnDownInitConst = 0;

	@Column(name = "int_reproved_meters_trim_agc", nullable = true)
	private int reprovedMetersOnTrimAgc = 0;

	@Column(name = "int_reproved_meters_calc_zero_flow", nullable = true)
	private int reprovedMetersOnCalcZeroFlow = 0;

	@Column(name = "int_reproved_meters_down_zero_flow", nullable = true)
	private int reprovedMetersOnDownZeroFlow = 0;

	@Column(name = "int_reproved_meters_calc_const", nullable = true)
	private int reprovedMetersOnCalcConst = 0;

	@Column(name = "int_reproved_meters_down_const", nullable = true)
	private int reprovedMetersOnDownConst = 0;

	@Column(name = "int_reproved_meters_calc_errors", nullable = true)
	private int reprovedMetersOnCalcErrors = 0;

	@Column(name = "int_reproved_meters_verif", nullable = true)
	private int reprovedMetersVerif = 0;

	//
	@Column(name = "double_minflow_error_limit", nullable = true)
	private double minFlowRatePermissibleError;

	@Column(name = "double_nominalflow_error_limit", nullable = true)
	private double nominalFlowRatePermissibleError;

	@Column(name = "double_maxflow_error_limit", nullable = true)
	private double maxFlowRatePermissibleError;

	@Column(name = "bool_isrework")
	private boolean isReWork = false;

	// @Column(name = "bool_only_verification")
	// private boolean isOnlyVerification = false;

	@Enumerated(EnumType.STRING)
	@Column(name = "enum_calibration_type", nullable = false)
	private CalibrationTypeEnum calibrationTypeEnum = CalibrationTypeEnum.FULL_PROD;

	// TODO achar meio de carregar os medidores sem pesar o SW. Metodo LAZY
	@OneToMany(fetch = FetchType.EAGER, targetEntity = MeterModel.class, mappedBy = "batelada")
	@Fetch(FetchMode.SUBSELECT)
	private Set<MeterModel> meters = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_batch", nullable = false)
	private BatchModel batch;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_process_config", nullable = true)
	private ProcessConfigModel processesConfigModel;

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
	 * Function returns the value of attribute runCount
	 * 
	 * @return the runCount
	 */
	public int getRunCount() {
		return runCount;
	}

	/**
	 * Function sets the value for attribute runCount
	 * 
	 * @param runCount
	 *            the runCount to set
	 */
	public void setRunCount(int runCount) {
		this.runCount = runCount;
	}

	// /**
	// * Function returns the value of attribute approvedMeters
	// *
	// * @return the approvedMeters
	// */
	// public int getApprovedMeters() {
	// return approvedMeters;
	// }
	//
	// /**
	// * Function sets the value for attribute approvedMeters
	// *
	// * @param approvedMeters
	// * the approvedMeters to set
	// */
	// public void setApprovedMeters(int approvedMeters) {
	// this.approvedMeters = approvedMeters;
	// }

	/**
	 * Function returns the value of attribute initTime
	 * 
	 * @return the initTime
	 */
	public Date getInitTime() {
		return initTime;
	}

	/**
	 * Function sets the value for attribute initTime
	 * 
	 * @param initTime
	 *            the initTime to set
	 */
	public void setInitTime(Date initTime) {
		this.initTime = initTime;
	}

	/**
	 * Function returns the value of attribute endTime
	 * 
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Function sets the value for attribute endTime
	 * 
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	// /**
	// * Function returns the value of attribute connectedMeters
	// *
	// * @return the connectedMeters
	// */
	// public int getConnectedMeters() {
	// return connectedMeters;
	// }
	//
	// /**
	// * Function sets the value for attribute connectedMeters
	// *
	// * @param connectedMeters
	// * the connectedMeters to set
	// */
	// public void setConnectedMeters(int connectedMeters) {
	// this.connectedMeters = connectedMeters;
	// }

	/**
	 * @return the connectedMeters
	 */
	public int getConnectedMeters() {
		return connectedMeters;
	}

	/**
	 * @param connectedMeters
	 *            the connectedMeters to set
	 */
	public void setConnectedMeters(int connectedMeters) {
		this.connectedMeters = connectedMeters;
	}

	/**
	 * @return the approvedMeters
	 */
	public int getApprovedMeters() {
		return approvedMeters;
	}

	/**
	 * @param approvedMeters
	 *            the approvedMeters to set
	 */
	public void setApprovedMeters(int approvedMeters) {
		this.approvedMeters = approvedMeters;
	}

	/**
	 * @return the reprovedMetersOnDownInitConst
	 */
	public int getReprovedMetersOnDownInitConst() {
		return reprovedMetersOnDownInitConst;
	}

	/**
	 * @param reprovedMetersOnDownInitConst
	 *            the reprovedMetersOnDownInitConst to set
	 */
	public void setReprovedMetersOnDownInitConst(int reprovedMetersOnDownInitConst) {
		this.reprovedMetersOnDownInitConst = reprovedMetersOnDownInitConst;
	}

	/**
	 * @return the reprovedMetersOnTrimAgc
	 */
	public int getReprovedMetersOnTrimAgc() {
		return reprovedMetersOnTrimAgc;
	}

	/**
	 * @param reprovedMetersOnTrimAgc
	 *            the reprovedMetersOnTrimAgc to set
	 */
	public void setReprovedMetersOnTrimAgc(int reprovedMetersOnTrimAgc) {
		this.reprovedMetersOnTrimAgc = reprovedMetersOnTrimAgc;
	}

	/**
	 * @return the reprovedMetersOnCalcZeroFlow
	 */
	public int getReprovedMetersOnCalcZeroFlow() {
		return reprovedMetersOnCalcZeroFlow;
	}

	/**
	 * @param reprovedMetersOnCalcZeroFlow
	 *            the reprovedMetersOnCalcZeroFlow to set
	 */
	public void setReprovedMetersOnCalcZeroFlow(int reprovedMetersOnCalcZeroFlow) {
		this.reprovedMetersOnCalcZeroFlow = reprovedMetersOnCalcZeroFlow;
	}

	/**
	 * @return the reprovedMetersOnDownZeroFlow
	 */
	public int getReprovedMetersOnDownZeroFlow() {
		return reprovedMetersOnDownZeroFlow;
	}

	/**
	 * @param reprovedMetersOnDownZeroFlow
	 *            the reprovedMetersOnDownZeroFlow to set
	 */
	public void setReprovedMetersOnDownZeroFlow(int reprovedMetersOnDownZeroFlow) {
		this.reprovedMetersOnDownZeroFlow = reprovedMetersOnDownZeroFlow;
	}

	/**
	 * @return the reprovedMetersOnnCalcConst
	 */
	public int getReprovedMetersOnCalcConst() {
		return reprovedMetersOnCalcConst;
	}

	/**
	 * @param reprovedMetersOnnCalcConst
	 *            the reprovedMetersOnnCalcConst to set
	 */
	public void setReprovedMetersOnCalcConst(int reprovedMetersOnnCalcConst) {
		this.reprovedMetersOnCalcConst = reprovedMetersOnnCalcConst;
	}

	/**
	 * @return the reprovedMetersOnDownConst
	 */
	public int getReprovedMetersOnDownConst() {
		return reprovedMetersOnDownConst;
	}

	/**
	 * @param reprovedMetersOnDownConst
	 *            the reprovedMetersOnDownConst to set
	 */
	public void setReprovedMetersOnDownConst(int reprovedMetersOnDownConst) {
		this.reprovedMetersOnDownConst = reprovedMetersOnDownConst;
	}

	/**
	 * @return the reprovedMetersOnCalcErrors
	 */
	public int getReprovedMetersOnCalcErrors() {
		return reprovedMetersOnCalcErrors;
	}

	/**
	 * @param reprovedMetersOnCalcErrors
	 *            the reprovedMetersOnCalcErrors to set
	 */
	public void setReprovedMetersOnCalcErrors(int reprovedMetersOnCalcErrors) {
		this.reprovedMetersOnCalcErrors = reprovedMetersOnCalcErrors;
	}

	/**
	 * @return the reprovedMetersVerif
	 */
	public int getReprovedMetersVerif() {
		return reprovedMetersVerif;
	}

	/**
	 * @param reprovedMetersVerif
	 *            the reprovedMetersVerif to set
	 */
	public void setReprovedMetersVerif(int reprovedMetersVerif) {
		this.reprovedMetersVerif = reprovedMetersVerif;
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

	// /**
	// * Function returns the value of attribute reprovedMetersOnTrimAgc
	// *
	// * @return the reprovedMetersOnTrimAgc
	// */
	// public int getReprovedMetersOnTrimAgc() {
	// return reprovedMetersOnTrimAgc;
	// }
	//
	// /**
	// * Function sets the value for attribute reprovedMetersOnTrimAgc
	// *
	// * @param reprovedMetersOnTrimAgc
	// * the reprovedMetersOnTrimAgc to set
	// */
	// public void setReprovedMetersOnTrimAgc(int reprovedMetersOnTrimAgc) {
	// this.reprovedMetersOnTrimAgc = reprovedMetersOnTrimAgc;
	// }

	// /**
	// * Function returns the value of attribute reprovedMetersOnZeroFlow
	// *
	// * @return the reprovedMetersOnZeroFlow
	// */
	// public int getReprovedMetersOnZeroFlow() {
	// return reprovedMetersOnZeroFlow;
	// }
	//
	// /**
	// * Function sets the value for attribute reprovedMetersOnZeroFlow
	// *
	// * @param reprovedMetersOnZeroFlow
	// * the reprovedMetersOnZeroFlow to set
	// */
	// public void setReprovedMetersOnZeroFlow(int reprovedMetersOnZeroFlow) {
	// this.reprovedMetersOnZeroFlow = reprovedMetersOnZeroFlow;
	// }
	//
	// /**
	// * Function returns the value of attribute reprovedMetersCalib
	// *
	// * @return the reprovedMetersCalib
	// */
	// public int getReprovedMetersCalib() {
	// return reprovedMetersCalib;
	// }
	//
	// /**
	// * Function sets the value for attribute reprovedMetersCalib
	// *
	// * @param reprovedMetersCalib
	// * the reprovedMetersCalib to set
	// */
	// public void setReprovedMetersCalib(int reprovedMetersCalib) {
	// this.reprovedMetersCalib = reprovedMetersCalib;
	// }

	// /**
	// * Function returns the value of attribute reprovedMetersVerif
	// *
	// * @return the reprovedMetersVerif
	// */
	// public int getReprovedMetersVerif() {
	// return reprovedMetersVerif;
	// }
	//
	// /**
	// * Function sets the value for attribute reprovedMetersVerif
	// *
	// * @param reprovedMetersVerif
	// * the reprovedMetersVerif to set
	// */
	// public void setReprovedMetersVerif(int reprovedMetersVerif) {
	// this.reprovedMetersVerif = reprovedMetersVerif;
	// }

	/**
	 * Function returns the value of attribute minFlowRatePermissibleError
	 * 
	 * @return the minFlowRatePermissibleError
	 */
	public double getMinFlowRatePermissibleError() {
		return minFlowRatePermissibleError;
	}

	/**
	 * Function sets the value for attribute minFlowRatePermissibleError
	 * 
	 * @param minFlowRatePermissibleError
	 *            the minFlowRatePermissibleError to set
	 */
	public void setMinFlowRatePermissibleError(double minFlowRatePermissibleError) {
		this.minFlowRatePermissibleError = minFlowRatePermissibleError;
	}

	/**
	 * Function returns the value of attribute nominalFlowRatePermissibleError
	 * 
	 * @return the nominalFlowRatePermissibleError
	 */
	public double getNominalFlowRatePermissibleError() {
		return nominalFlowRatePermissibleError;
	}

	/**
	 * Function sets the value for attribute nominalFlowRatePermissibleError
	 * 
	 * @param nominalFlowRatePermissibleError
	 *            the nominalFlowRatePermissibleError to set
	 */
	public void setNominalFlowRatePermissibleError(double nominalFlowRatePermissibleError) {
		this.nominalFlowRatePermissibleError = nominalFlowRatePermissibleError;
	}

	/**
	 * Function returns the value of attribute maxFlowRatePermissibleError
	 * 
	 * @return the maxFlowRatePermissibleError
	 */
	public double getMaxFlowRatePermissibleError() {
		return maxFlowRatePermissibleError;
	}

	/**
	 * Function sets the value for attribute maxFlowRatePermissibleError
	 * 
	 * @param maxFlowRatePermissibleError
	 *            the maxFlowRatePermissibleError to set
	 */
	public void setMaxFlowRatePermissibleError(double maxFlowRatePermissibleError) {
		this.maxFlowRatePermissibleError = maxFlowRatePermissibleError;
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

	// /**
	// * @return the isOnlyVerification
	// */
	// public boolean isOnlyVerification() {
	// return isOnlyVerification;
	// }
	//
	// /**
	// * @param isOnlyVerification
	// * the isOnlyVerification to set
	// */
	// public void setOnlyVerification(boolean isOnlyVerification) {
	// this.isOnlyVerification = isOnlyVerification;
	// }

	/**
	 * @return the calibrationTypeEnum
	 */
	public CalibrationTypeEnum getCalibrationType() {
		return calibrationTypeEnum;
	}

	/**
	 * @param calibrationTypeEnum
	 *            the calibrationTypeEnum to set
	 */
	public void setCalibrationType(CalibrationTypeEnum calibrationTypeEnum) {
		this.calibrationTypeEnum = calibrationTypeEnum;
	}

	/**
	 * Function returns the value of attribute processesConfigModel
	 * 
	 * @return the processesConfigModel
	 */
	public ProcessConfigModel getProcessesConfigModel() {
		return processesConfigModel;
	}

	/**
	 * Function sets the value for attribute processesConfigModel
	 * 
	 * @param processesConfigModel
	 *            the processesConfigModel to set
	 */
	public void setProcessesConfigModel(ProcessConfigModel processesConfigModel) {
		this.processesConfigModel = processesConfigModel;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		// return "" + this.id;
		return "" + this.runCount;
	}

	/**
	 * 
	 */
	public static Comparator<BateladaModel> comparatorRunCountAsc = new Comparator<BateladaModel>() {
		public int compare(BateladaModel batelada1, BateladaModel batelada2) {
			return batelada1.getRunCount() < batelada2.getRunCount() ? -1 : (batelada1.getRunCount() > batelada2.getRunCount()) ? 1 : 0;
		}
	};

	/**
	 * 
	 */
	public static Comparator<BateladaModel> comparatorRunCountDesc = new Comparator<BateladaModel>() {
		public int compare(BateladaModel batelada1, BateladaModel batelada2) {
			return batelada2.getRunCount() < batelada1.getRunCount() ? -1 : (batelada2.getRunCount() > batelada1.getRunCount()) ? 1 : 0;
		}
	};
}
