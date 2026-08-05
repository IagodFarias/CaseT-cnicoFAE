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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "dimensionalmodel")
public class DimensionalModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1347198079825015064L;

	@Id
	@SequenceGenerator(name = "dimensional_seq", sequenceName = "dimensional_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "dimensional_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_dimensional", nullable = false)
	private Long id;

	@Column(name = "string_description", unique = true)
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_dateofsampling")
	private Date dateOfSampling;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_carcassBatch")
	private CarcassBatchModel carcassBatch;

	@OneToMany(mappedBy = "dimensional", fetch = FetchType.EAGER)
	private Set<DimensionMeasureModel> dimensionMeasures = new HashSet<>();

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the dateOfSampling
	 */
	public Date getDateOfSampling() {
		return dateOfSampling;
	}

	/**
	 * @param dateOfSampling
	 *            the dateOfSampling to set
	 */
	public void setDateOfSampling(Date dateOfSampling) {
		this.dateOfSampling = dateOfSampling;
	}

	/**
	 * @return the carcassBatch
	 */
	public CarcassBatchModel getCarcassBatch() {
		return carcassBatch;
	}

	/**
	 * @param carcassBatch
	 *            the carcassBatch to set
	 */
	public void setCarcassBatch(CarcassBatchModel carcassBatch) {
		this.carcassBatch = carcassBatch;
	}

	/**
	 * @return the dimensionMeasures
	 */
	public Set<DimensionMeasureModel> getDimensionMeasures() {
		return dimensionMeasures;
	}

	/**
	 * @param dimensionMeasures
	 *            the dimensionMeasures to set
	 */
	public void setDimensionMeasures(Set<DimensionMeasureModel> dimensionMeasures) {
		this.dimensionMeasures = dimensionMeasures;
	}
}
