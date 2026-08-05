package si.dbcomm.dao;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.LogChangeProcessBatchModel;

public class LogChangeProcessBatchDao extends DaoParent{

	public LogChangeProcessBatchModel findByBatch(BatchModel batch){
		
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<LogChangeProcessBatchModel> criteria = builder.createQuery(LogChangeProcessBatchModel.class);
		Root<LogChangeProcessBatchModel> root = criteria.from(LogChangeProcessBatchModel.class);
		criteria.select(root);
		criteria.where(builder.equal(root.get("batch"), batch));
		Query query = persistenceSession.createQuery(criteria);
		LogChangeProcessBatchModel retorno = null;
		if(query.getResultList().size() > 0){
			retorno = (LogChangeProcessBatchModel) query.getSingleResult();
		}
		
		closeCurrentSessionReadwithTransaction();
		return retorno;
		
	}
	
	
	public LogChangeProcessBatchModel update(LogChangeProcessBatchModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.update(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}
}
