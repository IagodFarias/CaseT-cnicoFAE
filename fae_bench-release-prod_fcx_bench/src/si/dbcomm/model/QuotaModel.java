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
@Table(name = "quotamodel")
public class QuotaModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5850420773640582099L;

	@Id
	@SequenceGenerator(name = "quota_seq", sequenceName = "quota_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "quota_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_quota", nullable = false)
	private Long id;

	// @Column(name = "string_idquota")
	// private String idQuota;

	@Column(name = "string_description")
	private String description;

	@Column(name = "double_nominalvalue", nullable = false)
	private double nominalValue;

	@Column(name = "double_upperlimit", nullable = true)
	private double upperLimit;

	@Column(name = "double_lowerlimit", nullable = true)
	private double lowerLimit;

	// @Column(name = "double_tolerance", nullable = false)
	// private double tolerance;

	@Column(name = "int_fieldposx", nullable = true)
	private int fieldPosX;

	@Column(name = "int_fieldposy", nullable = true)
	private int fieldPosY;

	@Column(name = "int_fieldlength", nullable = true)
	private int fieldLength;

	// @Column(name = "bool_isnullable")
	// private boolean isNullable = false;

	@Column(name = "bool_isrequired")
	private boolean isRequired = false;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_drawing")
	private DrawingModel drawing;

	@OneToMany(mappedBy = "quota")
	private Set<DimensionMeasureModel> measures = new HashSet<>();

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

	// /**
	// * @return the idQuota
	// */
	// public String getIdQuota() {
	// return idQuota;
	// }
	//
	// /**
	// * @param idQuota
	// * the idQuota to set
	// */
	// public void setIdQuota(String idQuota) {
	// this.idQuota = idQuota;
	// }

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
	 * @return the nominalValue
	 */
	public double getNominalValue() {
		return nominalValue;
	}

	/**
	 * @param nominalValue
	 *            the nominalValue to set
	 */
	public void setNominalValue(double nominalValue) {
		this.nominalValue = nominalValue;
	}

	// /**
	// * @return the tolerance
	// */
	// public double getTolerance() {
	// return tolerance;
	// }
	//
	// /**
	// * @param tolerance
	// * the tolerance to set
	// */
	// public void setTolerance(double tolerance) {
	// this.tolerance = tolerance;
	// }

	// /**
	// * @return the isNullable
	// */
	// public boolean isNullable() {
	// return isNullable;
	// }
	//
	// /**
	// * @param isNullable
	// * the isNullable to set
	// */
	// public void setNullable(boolean isNullable) {
	// this.isNullable = isNullable;
	// }

	/**
	 * @return the upperLimit
	 */
	public double getUpperLimit() {
		return upperLimit;
	}

	/**
	 * @param upperLimit
	 *            the upperLimit to set
	 */
	public void setUpperLimit(double upperLimit) {
		this.upperLimit = upperLimit;
	}

	/**
	 * @return the lowerLimit
	 */
	public double getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * @param lowerLimit
	 *            the lowerLimit to set
	 */
	public void setLowerLimit(double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	/**
	 * @return the fieldPosX
	 */
	public int getFieldPosX() {
		return fieldPosX;
	}

	/**
	 * @param fieldPosX
	 *            the fieldPosX to set
	 */
	public void setFieldPosX(int fieldPosX) {
		this.fieldPosX = fieldPosX;
	}

	/**
	 * @return the fieldPosY
	 */
	public int getFieldPosY() {
		return fieldPosY;
	}

	/**
	 * @param fieldPosY
	 *            the fieldPosY to set
	 */
	public void setFieldPosY(int fieldPosY) {
		this.fieldPosY = fieldPosY;
	}

	/**
	 * @return the fieldLength
	 */
	public int getFieldLength() {
		return fieldLength;
	}

	/**
	 * @param fieldLength
	 *            the fieldLength to set
	 */
	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	/**
	 * @return the isRequired
	 */
	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * @param isRequired
	 *            the isRequired to set
	 */
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
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
	 * @return the measures
	 */
	public Set<DimensionMeasureModel> getMeasures() {
		return measures;
	}

	/**
	 * @param measures
	 *            the measures to set
	 */
	public void setMeasures(Set<DimensionMeasureModel> measures) {
		this.measures = measures;
	}
}
