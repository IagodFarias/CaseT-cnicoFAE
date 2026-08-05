package si.dbcomm.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import si.dbcomm.model.BatchModel;
import si.dbcomm.model.CriticalPointModel;
import si.dbcomm.model.DistancePointsModel;

public class DistancePointsDao extends DaoParent{
	
	public List<DistancePointsModel> findAllByBatch(BatchModel batch, Double flowRateRef) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<DistancePointsModel> criteria = builder.createQuery(DistancePointsModel.class);
		Root<DistancePointsModel> root = criteria.from(DistancePointsModel.class);
		criteria.select(root);
		criteria.where(
				builder.and(builder.equal(root.get("batch"), batch),
							builder.equal(root.get("flowRateFrom"), flowRateRef)));
		List<DistancePointsModel> list = persistenceSession.createQuery(criteria).getResultList();
		closeCurrentSessionReadwithTransaction();
		return list;
	}
	
}
