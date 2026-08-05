package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.CriticalPointDao;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.CriticalPointModel;

public class CriticalPointService {

	private CriticalPointDao criticalPoint;
	
	public CriticalPointService(){
		criticalPoint = new CriticalPointDao();
	}
	
	public List<CriticalPointModel> findAllByBatch(BatchModel batch){
		return criticalPoint.findAllByBatch(batch);			
	}
}
