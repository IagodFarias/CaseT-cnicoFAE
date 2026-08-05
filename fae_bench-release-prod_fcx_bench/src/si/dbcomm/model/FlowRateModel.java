//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file FlowRateModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import si.dbcomm.enumerations.FlowRateStateEnum;

/**
 * @author Marcos Oliveira
 *
 */
@Entity
@Table(name = "flowratemodel")
public class FlowRateModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3289169180158453786L;

	@Id
	@SequenceGenerator(name = "seq_flow_rate_model", sequenceName = "flowratemodel_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_flow_rate_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_flowrate", nullable = false)
	private long id;

	@Column(name = "double_flowrate", nullable = false)
	private double flowRate;

	@Column(name = "bool_running")
	private boolean running;

	@Enumerated(EnumType.STRING)
	private FlowRateStateEnum state;

	@Temporal(TemporalType.DATE)
	private Date flowRateReadTime;

	@Column(name = "string_description")
	private String description;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = PumpModel.class)
	private PumpModel pump;

	@OneToOne(fetch = FetchType.EAGER, targetEntity = PidConfigModel.class, cascade = CascadeType.ALL)
	private PidConfigModel pidConfig;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = RefMeterModel.class)
	private RefMeterModel refMeter;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = RamalModel.class)
	private RamalModel ramal;

	@Column(name = "double_upperlimit", nullable = false)
	private double upperLimit;

	@Column(name = "double_lowerlimit", nullable = false)
	private double lowerLimit;

	@Column(name = "double_uncertanty_limit", nullable = true)
	private double uncertantyLimit;

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
	 * Function returns the value of attribute flowRate
	 * 
	 * @return the flowRate
	 */
	public double getFlowRate() {
		return flowRate;
	}

	/**
	 * Function sets the value for attribute flowRate
	 * 
	 * @param flowRate
	 *            the flowRate to set
	 */
	public void setFlowRate(double flowRate) {
		this.flowRate = flowRate;
	}

	/**
	 * Function returns the value of attribute running
	 * 
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Function sets the value for attribute running
	 * 
	 * @param running
	 *            the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Function returns the value of attribute state
	 * 
	 * @return the state
	 */
	public FlowRateStateEnum getState() {
		return state;
	}

	/**
	 * Function sets the value for attribute state
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(FlowRateStateEnum state) {
		this.state = state;
	}

	/**
	 * Function returns the value of attribute flowRateReadTime
	 * 
	 * @return the flowRateReadTime
	 */
	public Date getFlowRateReadTime() {
		return flowRateReadTime;
	}

	/**
	 * Function sets the value for attribute flowRateReadTime
	 * 
	 * @param flowRateReadTime
	 *            the flowRateReadTime to set
	 */
	public void setFlowRateReadTime(Date flowRateReadTime) {
		this.flowRateReadTime = flowRateReadTime;
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
	 * Function returns the value of attribute pump
	 * 
	 * @return the pump
	 */
	public PumpModel getPump() {
		return pump;
	}

	/**
	 * Function sets the value for attribute pump
	 * 
	 * @param pump
	 *            the pump to set
	 */
	public void setPump(PumpModel pump) {
		this.pump = pump;
	}

	/**
	 * Function returns the value of attribute pidConfig
	 * 
	 * @return the pidConfig
	 */
	public PidConfigModel getPidConfig() {
		return pidConfig;
	}

	/**
	 * Function sets the value for attribute pidConfig
	 * 
	 * @param pidConfig
	 *            the pidConfig to set
	 */
	public void setPidConfig(PidConfigModel pidConfig) {
		this.pidConfig = pidConfig;
	}

	/**
	 * Function returns the value of attribute refMeter
	 * 
	 * @return the refMeter
	 */
	public RefMeterModel getRefMeter() {
		return refMeter;
	}

	/**
	 * Function sets the value for attribute refMeter
	 * 
	 * @param refMeter
	 *            the refMeter to set
	 */
	public void setRefMeter(RefMeterModel refMeter) {
		this.refMeter = refMeter;
	}

	/**
	 * Function returns the value of attribute ramal
	 * 
	 * @return the ramal
	 */
	public RamalModel getRamal() {
		return ramal;
	}

	/**
	 * Function sets the value for attribute ramal
	 * 
	 * @param ramal
	 *            the ramal to set
	 */
	public void setRamal(RamalModel ramal) {
		this.ramal = ramal;
	}

	/**
	 * Function returns the value of attribute upperLimit
	 * 
	 * @return the upperLimit
	 */
	public double getUpperLimit() {
		return upperLimit;
	}

	/**
	 * Function sets the value for attribute upperLimit
	 * 
	 * @param upperLimit
	 *            the upperLimit to set
	 */
	public void setUpperLimit(double upperLimit) {
		this.upperLimit = upperLimit;
	}

	/**
	 * Function returns the value of attribute lowerLimit
	 * 
	 * @return the lowerLimit
	 */
	public double getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * Function sets the value for attribute lowerLimit
	 * 
	 * @param lowerLimit
	 *            the lowerLimit to set
	 */
	public void setLowerLimit(double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	/**
	 * Function returns the value of attribute uncertantyLimit
	 * 
	 * @return the uncertantyLimit
	 */
	public double getUncertantyLimit() {
		return uncertantyLimit;
	}

	/**
	 * Function sets the value for attribute uncertantyLimit
	 * 
	 * @param uncertantyLimit
	 *            the uncertantyLimit to set
	 */
	public void setUncertantyLimit(double uncertantyLimit) {
		this.uncertantyLimit = uncertantyLimit;
	}

	@Override
	public String toString() {
		return "" + this.flowRate + " - " + this.description;
	}

	/**
	 * 
	 */
	public static Comparator<FlowRateModel> comparatorFlowRateAsc = new Comparator<FlowRateModel>() {
		public int compare(FlowRateModel flowRate1, FlowRateModel flowRate2) {
			return flowRate1.getFlowRate() < flowRate2.getFlowRate() ? -1 : (flowRate1.getFlowRate() > flowRate2.getFlowRate()) ? 1 : 0;
		}
	};
}
