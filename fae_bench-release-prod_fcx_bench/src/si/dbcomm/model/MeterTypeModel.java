//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterTypeModel.java
*    @author marcos
*    @date 19 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "metertypemodel")
public class MeterTypeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -760145909364581725L;

	@Id
	@SequenceGenerator(name = "meter_type_seq", sequenceName = "meter_type_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "meter_type_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_meter_type", nullable = false)
	private Long id;

	@Column(name = "string_meterModelDescription")
	private String meterModelDescription;

	@Column(name = "string_meteringClass")
	private String meteringClass;

	@Column(name = "string_manufacturer")
	private String manufacturer;

	@Column(name = "int_range")
	private int range;

	@Column(name = "bool_is_radio_wmbus")
	private boolean isRadioWmBus = false;

	@Column(name = "double_carcassLength")
	private double carcassLength; // in meters 0.115

	@Column(name = "double_nominalDiameter")
	private double nominalDiameter; // in meter eg.:0.02 - DN20

	@Column(name = "double_reductionDiameter")
	private double reductionDiameter;// in meter 0.010 = 10mm

	@Column(name = "double_ultraSoundPathLengh")
	private double ultraSoundPathLengh;// in meter 0.084

	@Column(name = "string_inmetroQnId")
	private String inmetroQnId;// A, B, Y...

	@Column(name = "string_codProduto")
	private String codProduto;

	@Column(name = "double_lowcutoff")
	private double lowCutoff;

	@Column(name = "double_highcutoff")
	private double highCutoff;

	@Column(name = "double_minflow_error_limit")
	private double minFlowRatePermissibleError;

	@Column(name = "double_nominalflow_error_limit")
	private double nominalFlowRatePermissibleError;

	@Column(name = "double_maxflow_error_limit")
	private double maxFlowRatePermissibleError;

	@Column(name = "double_transflow_error_limit")
	private double transitionFlowRatePermissibleError;

	@OneToMany(mappedBy = "meterType", fetch = FetchType.LAZY, targetEntity = BatchModel.class)
	private Set<BatchModel> batches = new HashSet<>();

	@OneToMany(mappedBy = "meterTypeModel", fetch = FetchType.LAZY)
	private Set<MeterModel> meters = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "id_nominalflowrate", nullable = false)
	private FlowRateModel nominalFlowrate;

	@ManyToOne
	@JoinColumn(name = "id_maxflowrate", nullable = false)
	private FlowRateModel maxFlowrate;

	@ManyToOne
	@JoinColumn(name = "id_tansitionFlowrate", nullable = false)
	private FlowRateModel transitionFlowrate;

	@ManyToOne
	@JoinColumn(name = "id_minFlowrate", nullable = false)
	private FlowRateModel minFlowrate;

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
	 * Function returns the value of attribute meterModelDescription
	 * 
	 * @return the meterModelDescription
	 */
	public String getMeterModelDescription() {
		return meterModelDescription;
	}

	/**
	 * Function sets the value for attribute meterModelDescription
	 * 
	 * @param meterModelDescription
	 *            the meterModelDescription to set
	 */
	public void setMeterModelDescription(String meterModelDescription) {
		this.meterModelDescription = meterModelDescription;
	}

	/**
	 * Function returns the value of attribute meteringClass
	 * 
	 * @return the meteringClass
	 */
	public String getMeteringClass() {
		return meteringClass;
	}

	/**
	 * Function sets the value for attribute meteringClass
	 * 
	 * @param meteringClass
	 *            the meteringClass to set
	 */
	public void setMeteringClass(String meteringClass) {
		this.meteringClass = meteringClass;
	}

	/**
	 * Function returns the value of attribute manufacturer
	 * 
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * Function sets the value for attribute manufacturer
	 * 
	 * @param manufacturer
	 *            the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * Function returns the value of attribute range
	 * 
	 * @return the range
	 */
	public int getRange() {
		return range;
	}

	/**
	 * Function sets the value for attribute range
	 * 
	 * @param range
	 *            the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * Function returns the value of attribute isRadioWmBus
	 * 
	 * @return the isRadioWmBus
	 */
	public boolean isRadioWmBus() {
		return isRadioWmBus;
	}

	/**
	 * Function sets the value for attribute isRadioWmBus
	 * 
	 * @param isRadioWmBus
	 *            the isRadioWmBus to set
	 */
	public void setRadioWmBus(boolean isRadioWmBus) {
		this.isRadioWmBus = isRadioWmBus;
	}

	/**
	 * Function returns the value of attribute carcassLength
	 * 
	 * @return the carcassLength
	 */
	public double getCarcassLength() {
		return carcassLength;
	}

	/**
	 * Function sets the value for attribute carcassLength
	 * 
	 * @param carcassLength
	 *            the carcassLength to set
	 */
	public void setCarcassLength(double carcassLength) {
		this.carcassLength = carcassLength;
	}

	/**
	 * Function returns the value of attribute nominalDiameter
	 * 
	 * @return the nominalDiameter
	 */
	public double getNominalDiameter() {
		return nominalDiameter;
	}

	/**
	 * Function sets the value for attribute nominalDiameter
	 * 
	 * @param nominalDiameter
	 *            the nominalDiameter to set
	 */
	public void setNominalDiameter(double nominalDiameter) {
		this.nominalDiameter = nominalDiameter;
	}

	/**
	 * Function returns the value of attribute reductionDiameter
	 * 
	 * @return the reductionDiameter
	 */
	public double getReductionDiameter() {
		return reductionDiameter;
	}

	/**
	 * Function returns the value of attribute nominalFlowrate
	 * 
	 * @return the nominalFlowrate
	 */
	public FlowRateModel getNominalFlowrate() {
		return nominalFlowrate;
	}

	/**
	 * Function sets the value for attribute nominalFlowrate
	 * 
	 * @param nominalFlowrate
	 *            the nominalFlowrate to set
	 */
	public void setNominalFlowrate(FlowRateModel nominalFlowrate) {
		this.nominalFlowrate = nominalFlowrate;
	}

	/**
	 * Function returns the value of attribute maxFlowrate
	 * 
	 * @return the maxFlowrate
	 */
	public FlowRateModel getMaxFlowrate() {
		return maxFlowrate;
	}

	/**
	 * Function sets the value for attribute maxFlowrate
	 * 
	 * @param maxFlowrate
	 *            the maxFlowrate to set
	 */
	public void setMaxFlowrate(FlowRateModel maxFlowrate) {
		this.maxFlowrate = maxFlowrate;
	}

	/**
	 * Function returns the value of attribute transitionFlowrate
	 * 
	 * @return the transitionFlowrate
	 */
	public FlowRateModel getTransitionFlowrate() {
		return transitionFlowrate;
	}

	/**
	 * Function sets the value for attribute transitionFlowrate
	 * 
	 * @param transitionFlowrate
	 *            the transitionFlowrate to set
	 */
	public void setTransitionFlowrate(FlowRateModel transitionFlowrate) {
		this.transitionFlowrate = transitionFlowrate;
	}

	/**
	 * Function returns the value of attribute minFlowrate
	 * 
	 * @return the minFlowrate
	 */
	public FlowRateModel getMinFlowrate() {
		return minFlowrate;
	}

	/**
	 * Function sets the value for attribute minFlowrate
	 * 
	 * @param minFlowrate
	 *            the minFlowrate to set
	 */
	public void setMinFlowrate(FlowRateModel minFlowrate) {
		this.minFlowrate = minFlowrate;
	}

	/**
	 * Function returns the value of attribute batches
	 * 
	 * @return the batches
	 */
	public Set<BatchModel> getBatches() {
		return batches;
	}

	/**
	 * Function sets the value for attribute batches
	 * 
	 * @param batches
	 *            the batches to set
	 */
	public void setBatches(Set<BatchModel> batches) {
		this.batches = batches;
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
	 * Function sets the value for attribute id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Function sets the value for attribute reductionDiameter in mm
	 * 
	 * @param reductionDiameter
	 *            the reductionDiameter to set
	 */
	public void setReductionDiameter(double reductionDiameter) {
		this.reductionDiameter = reductionDiameter;
	}

	/**
	 * Function returns the value of attribute ultraSoundPathLengh
	 * 
	 * @return the ultraSoundPathLengh
	 */
	public double getUltraSoundPathLengh() {
		return ultraSoundPathLengh;
	}

	/**
	 * Function sets the value for attribute ultraSoundPathLengh
	 * 
	 * @param ultraSoundPathLengh
	 *            the ultraSoundPathLengh to set
	 */
	public void setUltraSoundPathLengh(double ultraSoundPathLengh) {
		this.ultraSoundPathLengh = ultraSoundPathLengh;
	}

	/**
	 * Function returns the value of attribute inmetroQnId
	 * 
	 * @return the inmetroQnId
	 */
	public String getInmetroQnId() {
		return inmetroQnId;
	}

	/**
	 * Function sets the value for attribute inmetroQnId
	 * 
	 * @param inmetroQnId
	 *            the inmetroQnId to set
	 */
	public void setInmetroQnId(String inmetroQnId) {
		this.inmetroQnId = inmetroQnId;
	}

	/**
	 * Function returns the value of attribute lowCutoff
	 * 
	 * @return the lowCutoff
	 */
	public double getLowCutoff() {
		return lowCutoff;
	}

	/**
	 * Function sets the value for attribute lowCutoff
	 * 
	 * @param lowCutoff
	 *            the lowCutoff to set
	 */
	public void setLowCutoff(double lowCutoff) {
		this.lowCutoff = lowCutoff;
	}

	/**
	 * Function returns the value of attribute highCutoff
	 * 
	 * @return the highCutoff
	 */
	public double getHighCutoff() {
		return highCutoff;
	}

	/**
	 * Function sets the value for attribute highCutoff
	 * 
	 * @param highCutoff
	 *            the highCutoff to set
	 */
	public void setHighCutoff(double highCutoff) {
		this.highCutoff = highCutoff;
	}

	/**
	 * Function returns the value of attribute codProduto
	 * 
	 * @return the codProduto
	 */
	public String getCodProduto() {
		return codProduto;
	}

	/**
	 * Function sets the value for attribute codProduto
	 * 
	 * @param codProduto
	 *            the codProduto to set
	 */
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}

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
	 * Function returns the value of attribute transitionFlowRatePermissibleError
	 * 
	 * @return the transitionFlowRatePermissibleError
	 */
	public double getTransitionFlowRatePermissibleError() {
		return transitionFlowRatePermissibleError;
	}

	/**
	 * Function sets the value for attribute transitionFlowRatePermissibleError
	 * 
	 * @param transitionFlowRatePermissibleError
	 *            the transitionFlowRatePermissibleError to set
	 */
	public void setTransitionFlowRatePermissibleError(double transitionFlowRatePermissibleError) {
		this.transitionFlowRatePermissibleError = transitionFlowRatePermissibleError;
	}

	@Override
	public String toString() {
		return meterModelDescription;
	}
}
