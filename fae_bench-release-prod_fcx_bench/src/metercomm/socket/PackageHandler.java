package metercomm.socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import si.dbcomm.model.MeterDataModel;

public class PackageHandler {
	ByteBuffer bf;
	
	CommPackage commPackage;
	
	public MeterDataModel unpackData(ByteBuffer data){
		MeterDataModel pack = null;
		if(data != null){
			pack = new MeterDataModel();
			bf = data;
			bf.order(ByteOrder.LITTLE_ENDIAN);
	        pack.setAgStatus(bf.getShort(0)); // AG status from bytes 0-1
	        pack.setR1(bf.getShort(2)); // R1 gain from bytes 2-3
	        pack.setGm1(bf.getShort(4)); // GM1 gain from bytes 4-5
	        pack.setHfc(bf.getDouble(6)); // high-frequency clock calibration from bytes 6-13
	        pack.setTtstd(bf.getDouble(14)); // downstream transit time from bytes 14-21
	        pack.setTtrev(bf.getDouble(22)); // upstream transit time from bytes 22-29
	        pack.setVel(bf.getDouble(30)); // uncalibrated velocity from bytes 30-37
	        pack.setVelRe(bf.getDouble(38)); // calibrated velocity from bytes 38-45
	        pack.setVolFlowRe(bf.getDouble(46)); // current volume flow rate from bytes 46-53
	        pack.setVolRe(bf.getDouble(54)); // volume from bytes 54-61
	        pack.setAccReVolume(bf.getDouble(62)); // forward volume from bytes 62-69
	        pack.setAccReVolumeRev(bf.getDouble(70)); // reverse volume from bytes 70-77
		}
		return pack;
	}
	
	
	
}