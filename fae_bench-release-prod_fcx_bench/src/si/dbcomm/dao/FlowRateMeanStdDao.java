package si.dbcomm.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.FlowRateMeanStdModel;

public class FlowRateMeanStdDao extends DaoParent{

	public List<FlowRateMeanStdModel> findAllByBatchAndBatelada(BatchModel batch, BateladaModel startBatelada, BateladaModel endBatelada) {
		openCurrentSessionwithTransaction();

		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<FlowRateMeanStdModel> criteria = builder.createQuery(FlowRateMeanStdModel.class);
		Root<FlowRateMeanStdModel> root = criteria.from(FlowRateMeanStdModel.class);
		criteria.select(root);
		criteria.where(builder.and(builder.equal(root.get("batch"), batch), 
						builder.equal(root.get("bateladaInit"), startBatelada), 
						builder.equal(root.get("bateladaEnd"), endBatelada)));
		List<FlowRateMeanStdModel> list = persistenceSession.createQuery(criteria).getResultList();
		
		closeCurrentSessionReadwithTransaction();
		return list;
	} 
	
	
	public List<FlowRateMeanStdModel> findAllByBatchAndFlowRate(BatchModel batch, double flowRate) {
		openCurrentSessionwithTransaction();

		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<FlowRateMeanStdModel> criteria = builder.createQuery(FlowRateMeanStdModel.class);
		Root<FlowRateMeanStdModel> root = criteria.from(FlowRateMeanStdModel.class);
		criteria.select(root);
		criteria.where(builder.and(builder.equal(root.get("batch"), batch), 
						builder.equal(root.get("flowRate"), flowRate)));
		List<FlowRateMeanStdModel> list = persistenceSession.createQuery(criteria).getResultList();
		
		closeCurrentSessionReadwithTransaction();
		return list;
	} 

	
}
