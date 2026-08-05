package metercomm.socket;
//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CommPackage.java
*    @author Marcos
*    @date 24 de jun de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================

/**
 * @author Marcos
 *
 */
public class CommPackage {
	private short agStatus;
	private short r1;
	private short gm1;
	private double hfc;
	private double tsdt;
	private double trev;
	private double vel;
	private double vel_re;
	private double volflow_re;
	private double vol_re;
	private double accvol_re_std;
	private double accvol_re_rev;
	
	public CommPackage(){
		
	}
	
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the agStatus
	 */
	public short getAgStatus() {
		return agStatus;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param agStatus the agStatus to set
	 */
	public void setAgStatus(short agStatus) {
		this.agStatus = agStatus;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the r1
	 */
	public short getR1() {
		return r1;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param r1 the r1 to set
	 */
	public void setR1(short r1) {
		this.r1 = r1;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the gm1
	 */
	public short getGm1() {
		return gm1;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param gm1 the gm1 to set
	 */
	public void setGm1(short gm1) {
		this.gm1 = gm1;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the hfc
	 */
	public double getHfc() {
		return hfc;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param hfc the hfc to set
	 */
	public void setHfc(double hfc) {
		this.hfc = hfc;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the tsdt
	 */
	public double getTsdt() {
		return tsdt;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param tsdt the tsdt to set
	 */
	public void setTsdt(double tsdt) {
		this.tsdt = tsdt;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the trev
	 */
	public double getTrev() {
		return trev;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param trev the trev to set
	 */
	public void setTrev(double trev) {
		this.trev = trev;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the vel
	 */
	public double getVel() {
		return vel;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param vel the vel to set
	 */
	public void setVel(double vel) {
		this.vel = vel;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the vel_re
	 */
	public double getVel_re() {
		return vel_re;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param vel_re the vel_re to set
	 */
	public void setVel_re(double vel_re) {
		this.vel_re = vel_re;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the volflow_re
	 */
	public double getVolflow_re() {
		return volflow_re;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param volflow_re the volflow_re to set
	 */
	public void setVolflow_re(double volflow_re) {
		this.volflow_re = volflow_re;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the vol_re
	 */
	public double getVol_re() {
		return vol_re;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param vol_re the vol_re to set
	 */
	public void setVol_re(double vol_re) {
		this.vol_re = vol_re;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the accvol_re_std
	 */
	public double getAccvol_re_std() {
		return accvol_re_std;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param accvol_re_std the accvol_re_std to set
	 */
	public void setAccvol_re_std(double accvol_re_std) {
		this.accvol_re_std = accvol_re_std;
	}
	/**
	 * Function returns the flowRate of attribute flowRate
	 * @return the accvol_re_rev
	 */
	public double getAccvol_re_rev() {
		return accvol_re_rev;
	}
	/**
	 * Function sets the flowRate for attribute flowRate
	 * @param accvol_re_rev the accvol_re_rev to set
	 */
	public void setAccvol_re_rev(double accvol_re_rev) {
		this.accvol_re_rev = accvol_re_rev;
	}
	
	public void printData(){
		System.out.print("Accvol_re_rev="+getAccvol_re_rev()+" ");
		System.out.print("Accvol_re_std="+getAccvol_re_std()+" ");
		System.out.print("HFC="+getHfc()+" ");
		System.out.print("AG status="+getAgStatus()+" ");
		System.out.print("GM1="+getGm1()+" ");
		System.out.print("R1="+getR1()+" ");
		System.out.print("Trev="+getTrev()+" ");
		System.out.print("Tstd="+getTsdt()+" ");
		System.out.print("Vel="+getVel()+" ");
		System.out.print("Vel rev="+getVel_re()+" ");
		System.out.print("Vol rev="+getVol_re()+" ");
		System.out.print("Vol flow re="+getVolflow_re()+" ");
		System.out.println();
	}
	
}
