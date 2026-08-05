//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file VolumeModel.java
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Marcos Oliveira
 *
 */
@Entity
@Table(name = "volumemodel")
public class VolumeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7358702687271015164L;

	@Id
	@SequenceGenerator(name = "seq_volume_model", sequenceName = "volumemodel_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_volume_model", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_volume", nullable = false)
	private long id;

	@Column(name = "double_volume", nullable = false)
	private double volume;

	@Column(name = "date_readtime")
	private Date readTime;

	@Column(name = "double_description")
	private String description;

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
	 * Function returns the value of attribute readTime
	 * 
	 * @return the readTime
	 */
	public Date getReadTime() {
		return readTime;
	}

	/**
	 * Function sets the value for attribute readTime
	 * 
	 * @param readTime
	 *            the readTime to set
	 */
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
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

}
