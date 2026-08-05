//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ScaleModel.java
*    @author Marcos Oliveira
*    @date 11 de mai de 2016
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Marcos Oliveira
 *
 */
@Entity
@Table(name = "logchangebatchmodel")
public class LogChangeBatchModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 809014855985930512L;

	@Id
	@SequenceGenerator(name = "logchangebatch_seq", sequenceName = "logchangebatch_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "logchangebatch_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_logchangebatch", nullable = false)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_dateofchange", nullable = false)
	private Date dateOfChange;

	@Column(name = "int_meterufoid", nullable = false)
	private int meterUfoId;

	@Column(name = "string_serialnumber")
	private String serialNumber;

	@OneToOne
	@JoinColumn(name = "id_meter", nullable = false)
	private MeterModel meter;

	@OneToOne
	@JoinColumn(name = "id_batch_old", nullable = false)
	private BatchModel batchOld;

	@OneToOne
	@JoinColumn(name = "id_batch_new", nullable = false)
	private BatchModel batchNew;

	@OneToOne
	@JoinColumn(name = "id_meter_type_old", nullable = false)
	private MeterTypeModel meterTypeOld;

	@OneToOne
	@JoinColumn(name = "id_meter_type_new", nullable = false)
	private MeterTypeModel meterTypeNew;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the dateOfChange
	 */
	public Date getDateOfChange() {
		return dateOfChange;
	}

	/**
	 * @param dateOfChange
	 *            the dateOfChange to set
	 */
	public void setDateOfChange(Date dateOfChange) {
		this.dateOfChange = dateOfChange;
	}

	/**
	 * @return the meterUfoId
	 */
	public int getMeterUfoId() {
		return meterUfoId;
	}

	/**
	 * @param meterUfoId
	 *            the meterUfoId to set
	 */
	public void setMeterUfoId(int meterUfoId) {
		this.meterUfoId = meterUfoId;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber
	 *            the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the meter
	 */
	public MeterModel getMeter() {
		return meter;
	}

	/**
	 * @param meter
	 *            the meter to set
	 */
	public void setMeter(MeterModel meter) {
		this.meter = meter;
	}

	/**
	 * @return the batchOld
	 */
	public BatchModel getBatchOld() {
		return batchOld;
	}

	/**
	 * @param batchOld
	 *            the batchOld to set
	 */
	public void setBatchOld(BatchModel batchOld) {
		this.batchOld = batchOld;
	}

	/**
	 * @return the batchNew
	 */
	public BatchModel getBatchNew() {
		return batchNew;
	}

	/**
	 * @param batchNew
	 *            the batchNew to set
	 */
	public void setBatchNew(BatchModel batchNew) {
		this.batchNew = batchNew;
	}

	/**
	 * @return the meterTypeOld
	 */
	public MeterTypeModel getMeterTypeOld() {
		return meterTypeOld;
	}

	/**
	 * @param meterTypeOld
	 *            the meterTypeOld to set
	 */
	public void setMeterTypeOld(MeterTypeModel meterTypeOld) {
		this.meterTypeOld = meterTypeOld;
	}

	/**
	 * @return the meterTypeNew
	 */
	public MeterTypeModel getMeterTypeNew() {
		return meterTypeNew;
	}

	/**
	 * @param meterTypeNew
	 *            the meterTypeNew to set
	 */
	public void setMeterTypeNew(MeterTypeModel meterTypeNew) {
		this.meterTypeNew = meterTypeNew;
	}
}
