//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CalculatedFixedConstModel.java
*    @author Marcos
*    @date 4 de abr de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Marcos
 *
 */
@Entity
@Table(name = "calculatedfixedconstmodel")
public class CalculatedFixedConstModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8208741524673682224L;

	@Id
	@SequenceGenerator(name = "seq_calc_fixed_const", sequenceName = "seq_calc_fixed_const", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "seq_calc_fixed_const", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_calculatedfixedconstmodel", nullable = false)
	private long id;

	@Column(name = "string_descricao")
	private String descricao;

	@Column(name = "double_flowrate")
	private double flowrate;

	@Column(name = "double_re")
	private double re;

	@Column(name = "double_k")
	private double k;

	@Column(name = "double_temp")
	private double temp;

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
	 * Function returns the value of attribute descricao
	 * 
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Function sets the value for attribute descricao
	 * 
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Function returns the value of attribute flowrate
	 * 
	 * @return the flowrate
	 */
	public double getFlowrate() {
		return flowrate;
	}

	/**
	 * Function sets the value for attribute flowrate
	 * 
	 * @param flowrate
	 *            the flowrate to set
	 */
	public void setFlowrate(double flowrate) {
		this.flowrate = flowrate;
	}

	/**
	 * Function returns the value of attribute re
	 * 
	 * @return the re
	 */
	public double getRe() {
		return re;
	}

	/**
	 * Function sets the value for attribute re
	 * 
	 * @param re
	 *            the re to set
	 */
	public void setRe(double re) {
		this.re = re;
	}

	/**
	 * Function returns the value of attribute k
	 * 
	 * @return the k
	 */
	public double getK() {
		return k;
	}

	/**
	 * Function sets the value for attribute k
	 * 
	 * @param k
	 *            the k to set
	 */
	public void setK(double k) {
		this.k = k;
	}

	/**
	 * Function returns the value of attribute temp
	 * 
	 * @return the temp
	 */
	public double getTemp() {
		return temp;
	}

	/**
	 * Function sets the value for attribute temp
	 * 
	 * @param temp
	 *            the temp to set
	 */
	public void setTemp(double temp) {
		this.temp = temp;
	}
}
