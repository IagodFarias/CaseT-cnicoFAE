//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file VerificationErrorModel.java
*    @author marcos
*    @date 10 de fev de 2017
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "volumetricbenchmodel")
public class VolumetricBenchModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3703969241713510161L;

	@Id
	@SequenceGenerator(name = "volumetricbench_seq", sequenceName = "volumetricbench_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "volumetricbench_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_volumetricbench", nullable = false)
	private long id;

	@Column(name = "id_bench", nullable = false)
	private String idBench;

	@Column(name = "string_description")
	private String description;

	@Column(name = "double_uncertainty", nullable = false)
	private double uncertainty;

	@Column(name = "int_numberpositions", nullable = false)
	private int numberPositions;

	@OneToMany(mappedBy = "volumetricBench", fetch = FetchType.LAZY)
	private Set<VolumetricErrorModel> volumetricErrors = new HashSet<>();

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
	 * @return the idBench
	 */
	public String getIdBench() {
		return idBench;
	}

	/**
	 * @param idBench
	 *            the idBench to set
	 */
	public void setIdBench(String idBench) {
		this.idBench = idBench;
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
	 * @return the uncertainty
	 */
	public double getUncertainty() {
		return uncertainty;
	}

	/**
	 * @param uncertainty
	 *            the uncertainty to set
	 */
	public void setUncertainty(double uncertainty) {
		this.uncertainty = uncertainty;
	}

	/**
	 * @return the numberPositions
	 */
	public int getNumberPositions() {
		return numberPositions;
	}

	/**
	 * @param numberPositions
	 *            the numberPositions to set
	 */
	public void setNumberPositions(int numberPositions) {
		this.numberPositions = numberPositions;
	}

	/**
	 * @return the volumetricErrors
	 */
	public Set<VolumetricErrorModel> getVolumetricErrors() {
		return volumetricErrors;
	}

	/**
	 * @param volumetricErrors
	 *            the volumetricErrors to set
	 */
	public void setVolumetricErrors(Set<VolumetricErrorModel> volumetricErrors) {
		this.volumetricErrors = volumetricErrors;
	}

	@Override
	public String toString() {
		return this.idBench;
	}
}
