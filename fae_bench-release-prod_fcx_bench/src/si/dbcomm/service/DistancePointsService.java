package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.DistancePointsDao;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.DistancePointsModel;

public class DistancePointsService {
	
	private DistancePointsDao distancePoints;
	
	public DistancePointsService(){
		distancePoints = new DistancePointsDao();
	}
	
	public List<DistancePointsModel> findAllByBatchAndFlowRef(BatchModel batch, double flowRateRef){
		return distancePoints.findAllByBatch(batch, flowRateRef);
	}

}
