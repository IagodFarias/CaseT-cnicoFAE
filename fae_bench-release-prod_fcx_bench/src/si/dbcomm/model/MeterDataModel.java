//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterDataModel.java
*    @author marcos
*    @date 21 de jul de 2016
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
@Table(name = "meterdatamodel")
public class MeterDataModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -998055750596136586L;

	@Id
	@SequenceGenerator(name = "meter_data_seq", sequenceName = "meter_data_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "meter_data_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "meter_data_seq", nullable = false)
	private long id;

	@Column(name = "string_ip", nullable = false)
	private String ip;

	@Column(name = "short_agstatus", nullable = false)
	private short agStatus;

	@Column(name = "short_r1", nullable = false)
	private short r1;

	@Column(name = "short_gm1", nullable = false)
	private short gm1;

	@Column(name = "double_ttstd", nullable = false)
	private double ttstd;

	@Column(name = "double_ttrev", nullable = false)
	private double ttrev;

	@Column(name = "double_hfc", nullable = false)
	private double hfc;

	// Velocity calculated in meter
	@Column(name = "double_vel", nullable = false)
	private double vel;

	// Velocity calculated in software
	@Column(name = "double_vel_swcalc", nullable = false)
	private double velSwCalc;

	@Column(name = "double_velre", nullable = false)
	private double velRe;

	@Column(name = "double_volflowre", nullable = false)
	private double volFlowRe;

	@Column(name = "double_volre", nullable = false)
	private double volRe;

	@Column(name = "double_temperature", nullable = false)
	private double temperature;

	@Column(name = "double_accrevolume", nullable = false)
	private double accReVolume;

	@Column(name = "double_accrevolumerev", nullable = false)
	private double accReVolumeRev;

	// the expected flowrate to be running
	@Column(name = "double_expected_flowrate", nullable = false)
	private double expectedFlowRate;

	@ManyToOne
	@JoinColumn(name = "meter_id")
	private MeterModel meter;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_readat", nullable = false)
	private Date readAt;

	public MeterDataModel() {

	}

	public MeterDataModel(double expectedFlowRate, double ttstd, double ttrev, short agStatus, short r1, short gm1, double hfc, double vel, double velRev, double volFlowRe, double volRe,
			double temperature, double accReVolume, double accReVolumeRev) {
		this.expectedFlowRate = expectedFlowRate;
		this.ttstd = ttstd;
		this.ttrev = ttrev;
		this.agStatus = agStatus;
		this.r1 = r1;
		this.gm1 = gm1;
		this.hfc = hfc;
		this.volFlowRe = volFlowRe;
		this.vel = vel;
		this.velRe = velRev;
		this.volRe = volRe;
		this.temperature = temperature;
		this.accReVolume = accReVolume;
		this.accReVolumeRev = accReVolumeRev;

	}

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
	 * Function returns the value of attribute agStatus
	 * 
	 * @return the agStatus
	 */
	public short getAgStatus() {
		return agStatus;
	}

	/**
	 * Function sets the value for attribute agStatus
	 * 
	 * @param agStatus
	 *            the agStatus to set
	 */
	public void setAgStatus(short agStatus) {
		this.agStatus = agStatus;
	}

	/**
	 * Function returns the value of attribute r1
	 * 
	 * @return the r1
	 */
	public short getR1() {
		return r1;
	}

	/**
	 * Function sets the value for attribute r1
	 * 
	 * @param r1
	 *            the r1 to set
	 */
	public void setR1(short r1) {
		this.r1 = r1;
	}

	/**
	 * Function returns the value of attribute gm1
	 * 
	 * @return the gm1
	 */
	public short getGm1() {
		return gm1;
	}

	/**
	 * Function sets the value for attribute gm1
	 * 
	 * @param gm1
	 *            the gm1 to set
	 */
	public void setGm1(short gm1) {
		this.gm1 = gm1;
	}

	/**
	 * Function returns the value of attribute ttstd
	 * 
	 * @return the ttstd
	 */
	public double getTtstd() {
		return ttstd;
	}

	/**
	 * Function sets the value for attribute ttstd
	 * 
	 * @param ttstd
	 *            the ttstd to set
	 */
	public void setTtstd(double ttstd) {
		this.ttstd = ttstd;
	}

	/**
	 * Function returns the value of attribute ttrev
	 * 
	 * @return the ttrev
	 */
	public double getTtrev() {
		return ttrev;
	}

	/**
	 * Function sets the value for attribute ttrev
	 * 
	 * @param ttrev
	 *            the ttrev to set
	 */
	public void setTtrev(double ttrev) {
		this.ttrev = ttrev;
	}

	/**
	 * Function returns the value of attribute hfc
	 * 
	 * @return the hfc
	 */
	public double getHfc() {
		return hfc;
	}

	/**
	 * Function sets the value for attribute hfc
	 * 
	 * @param hfc
	 *            the hfc to set
	 */
	public void setHfc(double hfc) {
		this.hfc = hfc;
	}

	/**
	 * Function returns the value of attribute vel
	 * 
	 * @return the vel
	 */
	public double getVel() {
		return vel;
	}

	/**
	 * Function sets the value for attribute vel
	 * 
	 * @param vel
	 *            the vel to set
	 */
	public void setVel(double vel) {
		this.vel = vel;
	}

	/**
	 * Function returns the value of attribute velRe
	 * 
	 * @return the velRe
	 */
	public double getVelRe() {
		return velRe;
	}

	/**
	 * Function sets the value for attribute velRe
	 * 
	 * @param velRe
	 *            the velRe to set
	 */
	public void setVelRe(double velRev) {
		this.velRe = velRev;
	}

	/**
	 * Function returns the value of attribute volFlowRe
	 * 
	 * @return the volFlowRe
	 */
	public double getVolFlowRe() {
		return volFlowRe;
	}

	/**
	 * Function sets the value for attribute volFlowRe
	 * 
	 * @param volFlowRe
	 *            the volFlowRe to set
	 */
	public void setVolFlowRe(double volFlowRe) {
		this.volFlowRe = volFlowRe;
	}

	/**
	 * Function returns the value of attribute volRe
	 * 
	 * @return the volRe
	 */
	public double getVolRe() {
		return volRe;
	}

	/**
	 * Function sets the value for attribute volRe
	 * 
	 * @param volRe
	 *            the volRe to set
	 */
	public void setVolRe(double volRe) {
		this.volRe = volRe;
	}

	/**
	 * Function returns the value of attribute temperature
	 * 
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Function sets the value for attribute temperature
	 * 
	 * @param temperature
	 *            the temperature to set
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	/**
	 * Function returns the value of attribute accReVolume
	 * 
	 * @return the accReVolume
	 */
	public double getAccReVolume() {
		return accReVolume;
	}

	/**
	 * Function sets the value for attribute accReVolume
	 * 
	 * @param accReVolume
	 *            the accReVolume to set
	 */
	public void setAccReVolume(double accReVolume) {
		this.accReVolume = accReVolume;
	}

	/**
	 * Function returns the value of attribute accReVolumeRev
	 * 
	 * @return the accReVolumeRev
	 */
	public double getAccReVolumeRev() {
		return accReVolumeRev;
	}

	/**
	 * Function sets the value for attribute accReVolumeRev
	 * 
	 * @param accReVolumeRev
	 *            the accReVolumeRev to set
	 */
	public void setAccReVolumeRev(double accReVolumeRev) {
		this.accReVolumeRev = accReVolumeRev;
	}

	/**
	 * Function returns the value of attribute meter
	 * 
	 * @return the meter
	 */
	public MeterModel getMeter() {
		return meter;
	}

	/**
	 * Function sets the value for attribute meter
	 * 
	 * @param meter
	 *            the meter to set
	 */
	public void setMeter(MeterModel meter) {
		this.meter = meter;
	}

	/**
	 * Function returns the value of attribute readAt
	 * 
	 * @return the readAt
	 */
	public Date getReadAt() {
		return readAt;
	}

	/**
	 * Function sets the value for attribute readAt
	 * 
	 * @param readAt
	 *            the readAt to set
	 */
	public void setReadAt(Date readAt) {
		this.readAt = readAt;
	}

	/**
	 * Function returns the value of attribute velSwCalc
	 * 
	 * @return the velSwCalc
	 */
	public double getVelSwCalc() {
		return velSwCalc;
	}

	/**
	 * Function sets the value for attribute velSwCalc
	 * 
	 * @param velSwCalc
	 *            the velSwCalc to set
	 */
	public void setVelSwCalc(double velSwCalc) {
		this.velSwCalc = velSwCalc;
	}

	/**
	 * Function returns the value of attribute expectedFlowRate
	 * 
	 * @return the expectedFlowRate
	 */
	public double getExpectedFlowRate() {
		return expectedFlowRate;
	}

	/**
	 * Function sets the value for attribute expectedFlowRate
	 * 
	 * @param expectedFlowRate
	 *            the expectedFlowRate to set
	 */
	public void setExpectedFlowRate(double expectedFlowRate) {
		this.expectedFlowRate = expectedFlowRate;
	}

	public void printData() {
		System.out.format("HFC=%1$8.2f ", getHfc());
		System.out.format("AG_status=%1$4d ", getAgStatus());
		System.out.format("GM1=%1$2d ", getGm1());
		System.out.format("R1=%1$2d ", getR1());
		System.out.format("Vel=%1$+22.21e ", getVel());
		System.out.format("Vel_re=%1$+22.21f ", getVelRe());
		System.out.format("Tstd=%1$+22.21e ", getTtstd());
		System.out.format("Trev=%1$+22.21e ", getTtrev());
		System.out.format("Vol_flow_re=%1$+20.19e ", getVolFlowRe());
		System.out.format("Vol_re=%1$+22.21f ", getVolRe());
		System.out.format("Accvol_re_std=%1$+09.5e ", getAccReVolume());
		System.out.format("Accvol_re_rev=%1$+09.5e ", getAccReVolumeRev());
		System.out.println();
	}

}
