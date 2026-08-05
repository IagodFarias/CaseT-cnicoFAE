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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import si.dbcomm.util.ConversorNumbers;

/**
 * @author marcos
 *
 */
@Entity
@Table(name = "conversmodel")
public class ConversorModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6012137135646443499L;

	@Id
	@SequenceGenerator(name = "conversor_seq", sequenceName = "conversor_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "conversor_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_conversor", nullable = false)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "string_name", nullable = false, unique = true)
	private ConversorNumbers name;

	@Column(name = "string_ip", nullable = false, unique = true)
	private String ip;

	@Column(name = "int_port", nullable = false)
	private int port;

	@Column(name = "hex_color", nullable = false)
	private String colorCode;

	@Column(name = "bool_enabled", nullable = true)
	private boolean enabled;

	@OneToMany(mappedBy = "conversor")
	private Set<MeterModel> meters = new HashSet<>();

	/**
	 * Function returns the value of attribute name
	 * 
	 * @return the name
	 */
	public ConversorNumbers getName() {
		return name;
	}

	/**
	 * Function sets the value for attribute name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(ConversorNumbers name) {
		this.name = name;
	}

	/**
	 * Function returns the value of attribute ip
	 * 
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Function sets the value for attribute ip
	 * 
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Function returns the value of attribute port
	 * 
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Function sets the value for attribute port
	 * 
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Function returns the value of attribute meters
	 * 
	 * @return the meters
	 */
	public Set<MeterModel> getMeters() {
		return meters;
	}

	/**
	 * Function sets the value for attribute meters
	 * 
	 * @param meters
	 *            the meters to set
	 */
	public void setMeters(Set<MeterModel> meters) {
		this.meters = meters;
	}

	/**
	 * Function returns the value of attribute id
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Function sets the value for attribute id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Function returns the value of attribute colorCode
	 * 
	 * @return the colorCode
	 */
	public String getColorCode() {
		return colorCode;
	}

	/**
	 * Function sets the value for attribute colorCode
	 * 
	 * @param colorCode
	 *            the colorCode to set
	 */
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	/**
	 * Function returns the value of attribute enabled
	 * 
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Function sets the value for attribute enabled
	 * 
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
