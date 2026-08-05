//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BateladaDao.java
*    @author marcos
*    @date 20 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BatchModel_;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.BateladaModel_;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.MeterModel_;

/**
 * @author marcos
 *
 */
public class BateladaDao extends DaoParent implements DaoInterface<BateladaModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public BateladaModel persist(BateladaModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		entity.setId((long) persistenceSession.save(entity));
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#update(java.lang.Object)
	 */
	@Override
	public BateladaModel update(BateladaModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.update(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#delete(java.lang.Object)
	 */
	@Override
	public BateladaModel delete(BateladaModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.delete(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#read(java.lang.Object)
	 */
	@Override
	public BateladaModel read(BateladaModel entity) {
		openCurrentSessionwithTransaction();
		BateladaModel batelada = persistenceSession.get(BateladaModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return batelada;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public BateladaModel findById(Long id) {
		openCurrentSessionwithTransaction();
		BateladaModel batelada = persistenceSession.get(BateladaModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return batelada;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<BateladaModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<BateladaModel> query = persistenceSession.createQuery("FROM BateladaModel");
		List<BateladaModel> batchList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return batchList;
	}

	public List<BateladaModel> findAllByBatch(BatchModel batch) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<BateladaModel> criteria = builder.createQuery(BateladaModel.class);
		Root<BateladaModel> root = criteria.from(BateladaModel.class);
		Join<BateladaModel, BatchModel> join = root.join(BateladaModel_.batch, JoinType.INNER);

		criteria.select(root);
		criteria.where(builder.equal(join.get(BatchModel_.id), batch.getId()));
		// BateladaModel entity = null;
		List<BateladaModel> list = persistenceSession.createQuery(criteria).getResultList();
		// if (!list.isEmpty()) {
		// entity = list.get(0);
		// }
		closeCurrentSessionReadwithTransaction();
		return list;
	}
	
	public List<MeterModel> findAllMeters(BateladaModel batelada) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<MeterModel> criteria = builder.createQuery(MeterModel.class);
		Root<MeterModel> root = criteria.from(MeterModel.class);
		Join<MeterModel, BateladaModel> join = root.join(MeterModel_.batelada, JoinType.INNER);
		
		criteria.select(root);
		criteria.where(builder.equal(join.get(BateladaModel_.id), batelada.getId()));
		// BateladaModel entity = null;
		List<MeterModel> list = persistenceSession.createQuery(criteria).getResultList();
		// if (!list.isEmpty()) {
		// entity = list.get(0);
		// }
		closeCurrentSessionReadwithTransaction();
		return list;
	}
}
