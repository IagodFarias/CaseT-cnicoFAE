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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import si.dbcomm.enumerations.DrawingEnum;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "drawingmodel")
public class DrawingModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2434458014270568041L;

	@Id
	@SequenceGenerator(name = "drawing_seq", sequenceName = "drawing_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "drawing_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_drawing", nullable = false)
	private Long id;

	@Column(name = "string_description", unique = true)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "enum_drawing", nullable = true)
	private DrawingEnum drawingEnum = DrawingEnum.DN20_115;

	@Column(name = "int_numberquotas", nullable = false)
	private int numberQuotas;

	@Column(name = "lob_pdffile", nullable = false)
	@Lob
	private byte[] pdfFile;

	@Column(name = "lob_imagefile", nullable = false)
	@Lob
	private byte[] imageFile;

	@OneToMany(mappedBy = "drawing", fetch = FetchType.EAGER)
	private Set<QuotaModel> quotas = new HashSet<>();

	@OneToMany(mappedBy = "drawing")
	private Set<CarcassBatchModel> carcassBatch = new HashSet<>();

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
	 * @return the drawingEnum
	 */
	public DrawingEnum getDrawingEnum() {
		return drawingEnum;
	}

	/**
	 * @param drawingEnum
	 *            the drawingEnum to set
	 */
	public void setDrawingEnum(DrawingEnum drawingEnum) {
		this.drawingEnum = drawingEnum;
	}

	/**
	 * @return the numberDimensions
	 */
	public int getNumberQuotas() {
		return numberQuotas;
	}

	/**
	 * @param numberDimensions
	 *            the numberDimensions to set
	 */
	public void setNumberQuotas(int numberQuotas) {
		this.numberQuotas = numberQuotas;
	}

	/**
	 * @return the pdffile
	 */
	public byte[] getPdfFile() {
		return pdfFile;
	}

	/**
	 * @param pdffile
	 *            the pdffile to set
	 */
	public void setPdfFile(byte[] pdfFile) {
		this.pdfFile = pdfFile;
	}

	/**
	 * @return the imagefile
	 */
	public byte[] getImageFile() {
		return imageFile;
	}

	/**
	 * @param imagefile
	 *            the imagefile to set
	 */
	public void setImageFile(byte[] imageFile) {
		this.imageFile = imageFile;
	}

	/**
	 * @return the quotas
	 */
	public Set<QuotaModel> getQuotas() {
		return quotas;
	}

	/**
	 * @param quotas
	 *            the quotas to set
	 */
	public void setQuotas(Set<QuotaModel> quotas) {
		this.quotas = quotas;
	}

	/**
	 * @return the carcassBatch
	 */
	public Set<CarcassBatchModel> getCarcassBatch() {
		return carcassBatch;
	}

	/**
	 * @param carcassBatch
	 *            the carcassBatch to set
	 */
	public void setCarcassBatch(Set<CarcassBatchModel> carcassBatch) {
		this.carcassBatch = carcassBatch;
	}

	@Override
	public String toString() {
		return this.description;
	}
}
