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
@Table(name = "carcassbatchmodel")
public class CarcassBatchModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2434458014270568041L;

	@Id
	@SequenceGenerator(name = "carcassbatch_seq", sequenceName = "carcassbatch_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "carcassbatch_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_carcassbatch", nullable = false)
	private Long id;

	@Column(name = "string_description", nullable = true, unique = true)
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_dateofmanufacture", nullable = true)
	private Date dateOfManufacture;

	@Column(name = "int_numbercarcass", nullable = false)
	private int numberCarcass;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_drawing")
	private DrawingModel drawing;

	@OneToMany(mappedBy = "carcassBatch")
	private Set<DimensionalModel> dimensional = new HashSet<>();

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
	 * @return the dateOfManufacture
	 */
	public Date getDateOfManufacture() {
		return dateOfManufacture;
	}

	/**
	 * @param dateOfManufacture
	 *            the dateOfManufacture to set
	 */
	public void setDateOfManufacture(Date dateOfManufacture) {
		this.dateOfManufacture = dateOfManufacture;
	}

	/**
	 * @return the numberCarcass
	 */
	public int getNumberCarcass() {
		return numberCarcass;
	}

	/**
	 * @param numberCarcass
	 *            the numberCarcass to set
	 */
	public void setNumberCarcass(int numberCarcass) {
		this.numberCarcass = numberCarcass;
	}

	/**
	 * @return the drawing
	 */
	public DrawingModel getDrawing() {
		return drawing;
	}

	/**
	 * @param drawing
	 *            the drawing to set
	 */
	public void setDrawing(DrawingModel drawing) {
		this.drawing = drawing;
	}

	/**
	 * @return the dimensional
	 */
	public Set<DimensionalModel> getDimensional() {
		return dimensional;
	}

	/**
	 * @param dimensional
	 *            the dimensional to set
	 */
	public void setDimensional(Set<DimensionalModel> dimensional) {
		this.dimensional = dimensional;
	}

	@Override
	public String toString() {
		return this.description;
	}
}
