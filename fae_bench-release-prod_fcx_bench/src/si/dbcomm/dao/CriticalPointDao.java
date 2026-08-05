package si.dbcomm.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BatchModel_;
import si.dbcomm.model.CriticalPointModel;
import si.dbcomm.model.MeterTypeModel;
import si.dbcomm.model.MeterTypeModel_;

public class CriticalPointDao extends DaoParent{
	
	public List<CriticalPointModel> findAllByBatch(BatchModel batch) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<CriticalPointModel> criteria = builder.createQuery(CriticalPointModel.class);
		Root<CriticalPointModel> root = criteria.from(CriticalPointModel.class);
		criteria.select(root);
		criteria.where(builder.equal(root.get("batch"), batch));
		List<CriticalPointModel> list = persistenceSession.createQuery(criteria).getResultList();
		closeCurrentSessionReadwithTransaction();
		return list;
	}

}
