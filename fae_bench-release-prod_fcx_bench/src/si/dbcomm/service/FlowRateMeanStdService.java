package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.FlowRateMeanStdDao;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.FlowRateMeanStdModel;

public class FlowRateMeanStdService {
	
	private FlowRateMeanStdDao flowRateMeanStdDao = new FlowRateMeanStdDao();
			

	public List<FlowRateMeanStdModel> findAllByBatchAndBatelada(BatchModel batch, BateladaModel startBatelada, BateladaModel endBatelada){
		
		return flowRateMeanStdDao.findAllByBatchAndBatelada(batch, startBatelada, endBatelada);
	}
	
	public List<FlowRateMeanStdModel> findAllByBatchAndFlowRate(BatchModel batch, double flowRate){
		
		return flowRateMeanStdDao.findAllByBatchAndFlowRate(batch, flowRate);
	}
}
