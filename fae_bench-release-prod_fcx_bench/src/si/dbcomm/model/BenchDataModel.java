//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BenchData.java
*    @author marcos
*    @date 18 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "benchdatamodel")
public class BenchDataModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6703915443902095290L;

	@Id
	@SequenceGenerator(name = "bench_data_seq", sequenceName = "bench_data_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "bench_data_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_bench_data", nullable = false)
	private long id;

	@ManyToOne
	@JoinColumn(name = "id_refmeter")
	private RefMeterModel refMeter;

	// the expected flowrate to be running
	@Column(name = "double_expected_flowrate", nullable = false)
	private double expectedFlowRate;

	@Transient
	private int seqNumber;

	// real flowrate read at the meter
	@Column(name = "double_flowrate", nullable = false)
	private double flowRate;

	// flowrate calculated by pulse and time count
	@Column(name = "double_pulseCalcFlowrate", nullable = true)
	private double pulseCalcFlowRate = 0;

	@Column(name = "double_pressure", nullable = false)
	private double pressure;

	@Column(name = "double_volume", nullable = false)
	private double volume;

	@Column(name = "double_temperature_li", nullable = false)
	private double temperatureLi;

	@Column(name = "double_temperature_lo", nullable = false)
	private double temperatureLo;

	@Column(name = "date_readat", nullable = false)
	private Date readAt;

	@Column(name = "int_timeval", nullable = false)
	private int timeVal;

	@Column(name = "int_counter", nullable = false)
	private int counter;

	@ManyToOne
	@JoinColumn(name = "id_batch")
	private BatchModel batch;

	/**
	 * Creates a object for class BenchDataModel.java
	 */
	public BenchDataModel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates a object for class BenchDataModel.java
	 */
	public BenchDataModel(RefMeterModel refMeter, double flowRate, double expectedFlowRate, double pulseCalcFlowRate, double volume, double pressure, double temperatureLi, double temperatureLo,
			int timeVal, int counter, Date readAt) {
		this.refMeter = refMeter;
		this.flowRate = flowRate;
		this.expectedFlowRate = expectedFlowRate;
		this.pulseCalcFlowRate = pulseCalcFlowRate;
		this.volume = volume;
		this.pressure = pressure;
		this.temperatureLi = temperatureLi;
		this.temperatureLo = temperatureLo;
		this.timeVal = timeVal;
		this.counter = counter;
		this.readAt = readAt;
	}

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
	 * Function returns the value of attribute pressure
	 * 
	 * @return the pressure
	 */
	public double getPressure() {
		return pressure;
	}

	/**
	 * Function sets the value for attribute pressure
	 * 
	 * @param pressure
	 *            the pressure to set
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/**
	 * Function returns the value of attribute volume
	 * 
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * Function sets the value for attribute volume
	 * 
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	/**
	 * Function returns the value of attribute _temperature_li
	 * 
	 * @return the _temperature_li
	 */
	public double getTemperatureLi() {
		return temperatureLi;
	}

	/**
	 * Function sets the value for attribute _temperature_li
	 * 
	 * @param _temperature_li
	 *            the _temperature_li to set
	 */
	public void setTemperatureLi(double temperatureLi) {
		this.temperatureLi = temperatureLi;
	}

	/**
	 * Function returns the value of attribute readAt
	 * 
	 * @return the readAt
	 */
	public Date getReadAt() {
		return readAt;
	}

	/**
	 * Function sets the value for attribute readAt
	 * 
	 * @param readAt
	 *            the readAt to set
	 */
	public void setReadAt(Date readAt) {
		this.readAt = readAt;
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
	 * Function returns the value of attribute expectedFlowRate
	 * 
	 * @return the expectedFlowRate
	 */
	public double getExpectedFlowRate() {
		return expectedFlowRate;
	}

	/**
	 * Function sets the value for attribute expectedFlowRate
	 * 
	 * @param expectedFlowRate
	 *            the expectedFlowRate to set
	 */
	public void setExpectedFlowRate(double expectedFlowRate) {
		this.expectedFlowRate = expectedFlowRate;
	}

	/**
	 * Function returns the value of attribute timeVal
	 * 
	 * @return the timeVal
	 */
	public int getTimeVal() {
		return timeVal;
	}

	/**
	 * Function sets the value for attribute timeVal
	 * 
	 * @param timeVal
	 *            the timeVal to set
	 */
	public void setTimeVal(int timeVal) {
		this.timeVal = timeVal;
	}

	/**
	 * Function returns the value of attribute counter
	 * 
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Function sets the value for attribute counter
	 * 
	 * @param counter
	 *            the counter to set
	 */
	public void setCounter(int counter) {
		this.counter = counter;
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
	 * Function returns the value of attribute seqNumber
	 * 
	 * @return the seqNumber
	 */
	public int getSeqNumber() {
		return seqNumber;
	}

	/**
	 * Function sets the value for attribute seqNumber
	 * 
	 * @param seqNumber
	 *            the seqNumber to set
	 */
	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	/**
	 * Function returns the value of attribute pulseCalcFlowRate
	 * 
	 * @return the pulseCalcFlowRate
	 */
	public double getPulseCalcFlowRate() {
		return pulseCalcFlowRate;
	}

	/**
	 * Function sets the value for attribute pulseCalcFlowRate
	 * 
	 * @param pulseCalcFlowRate
	 *            the pulseCalcFlowRate to set
	 */
	public void setPulseCalcFlowRate(double pulseCalcFlowRate) {
		this.pulseCalcFlowRate = pulseCalcFlowRate;
	}

	/**
	 * Function returns the value of attribute temperatureLo
	 * 
	 * @return the temperatureLo
	 */
	public double getTemperatureLo() {
		return temperatureLo;
	}

	/**
	 * Function sets the value for attribute temperatureLo
	 * 
	 * @param temperatureLo
	 *            the temperatureLo to set
	 */
	public void setTemperatureLo(double temperatureLo) {
		this.temperatureLo = temperatureLo;
	}

	@Override
	public boolean equals(Object obj) {
		boolean retorno = false;
		if (obj != null) {
			if (BenchDataModel.class.isAssignableFrom(obj.getClass())) {
				final BenchDataModel other = (BenchDataModel) obj;
				if (this.getCounter() == other.getCounter() && this.getTimeVal() == other.getTimeVal()) {
					retorno = true;
				} else {
					retorno = false;
				}
			} else {
				retorno = false;
			}
		} else {
			retorno = false;
		}
		return retorno;
	}

	public boolean isValidSample(BenchDataModel old) {
		boolean retorno = false;

		if (old != null) {
			if (this.getCounter() != old.getCounter() && this.getTimeVal() != old.getTimeVal()) {
				retorno = true;
			} else {
				retorno = false;
			}
		} else {
			retorno = false;
		}

		return retorno;
	}

}
