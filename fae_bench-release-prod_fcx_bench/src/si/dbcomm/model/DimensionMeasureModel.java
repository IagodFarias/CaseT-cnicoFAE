//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ConversorModel.java
*    @author marcos
*    @date 12 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "dimensionmeasuremodel")
public class DimensionMeasureModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2434458014270568041L;

	@Id
	@SequenceGenerator(name = "dimensionmeasure_seq", sequenceName = "dimensionmeasure_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "dimensionmeasure_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_dimensionmeasure", nullable = false)
	private Long id;

	// @Column(name = "string_description", nullable = true, unique = true)
	// private String description;

	@Column(name = "double_value", nullable = false)
	private double value;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_measurementdate", nullable = true)
	private Date measurementDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_quota")
	private QuotaModel quota;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_dimensional")
	private DimensionalModel dimensional;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the measurementDate
	 */
	public Date getMeasurementDate() {
		return measurementDate;
	}

	/**
	 * @param measurementDate
	 *            the measurementDate to set
	 */
	public void setMeasurementDate(Date measurementDate) {
		this.measurementDate = measurementDate;
	}

	/**
	 * @return the quota
	 */
	public QuotaModel getQuota() {
		return quota;
	}

	/**
	 * @param quota
	 *            the quota to set
	 */
	public void setQuota(QuotaModel quota) {
		this.quota = quota;
	}

	/**
	 * @return the dimensional
	 */
	public DimensionalModel getDimensional() {
		return dimensional;
	}

	/**
	 * @param dimensional
	 *            the dimensional to set
	 */
	public void setDimensional(DimensionalModel dimensional) {
		this.dimensional = dimensional;
	}
}
