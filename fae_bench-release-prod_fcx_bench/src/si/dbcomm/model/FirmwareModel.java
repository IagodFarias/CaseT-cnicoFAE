package si.dbcomm.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "firmwaremodel")
public class FirmwareModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -38571019798209943L;

	@Id
	@SequenceGenerator(name = "firmware_seq", sequenceName = "firmware_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "firmware_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_firmware", nullable = false)
	private Long id;

	@Column(name = "string_description", nullable = false, unique = true)
	private String description;

	@Column(name = "string_version", nullable = false, unique = true)
	private String version;

//	@Column(name = "enum_devicetype", nullable = false)
//	private DeviceTypeEnum deviceType;

	@Column(name = "string_firmwarename", nullable = false, unique = true)
	private String name;

	@Column(name = "boolean_active")
	private boolean isActive = true;

	@Column(name = "lob_firmwarefile", nullable = false)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] file;

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
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

//	/**
//	 * @return the deviceType
//	 */
//	public DeviceTypeEnum getDeviceType() {
//		return deviceType;
//	}

//	/**
//	 * @param deviceType
//	 *            the deviceType to set
//	 */
//	public void setDeviceType(DeviceTypeEnum deviceType) {
//		this.deviceType = deviceType;
//	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the file
	 */
	public byte[] getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(byte[] file) {
		this.file = file;
	}

	@Override
	public String toString() {
		String toString = version + " - " + description;
		return toString;
	}
}
