//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterTypeDao.java
*    @author marcos
*    @date 19 de set de 2016
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
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.MeterModel_;
import si.dbcomm.model.MeterTypeModel;
import si.dbcomm.model.MeterTypeModel_;

/**
 * @author marcos
 *
 */
public class MeterTypeDao extends DaoParent implements DaoInterface<MeterTypeModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public MeterTypeModel persist(MeterTypeModel entity) throws CrudDatabaseException {
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
	public MeterTypeModel update(MeterTypeModel entity) throws CrudDatabaseException {
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
	public MeterTypeModel delete(MeterTypeModel entity) throws CrudDatabaseException {
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
	public MeterTypeModel read(MeterTypeModel entity) {
		openCurrentSessionwithTransaction();
		MeterTypeModel meterType = persistenceSession.get(MeterTypeModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return meterType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public MeterTypeModel findById(Long id) {
		openCurrentSessionwithTransaction();
		MeterTypeModel meterType = persistenceSession.get(MeterTypeModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return meterType;
	}

	public MeterTypeModel findByBatch(BatchModel batch) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<MeterTypeModel> criteria = builder.createQuery(MeterTypeModel.class);
		Root<MeterTypeModel> root = criteria.from(MeterTypeModel.class);
		Join<MeterTypeModel, BatchModel> join = root.join(MeterTypeModel_.batches, JoinType.INNER);

		criteria.select(root);
		criteria.where(builder.equal(join.get(BatchModel_.id), batch.getId()));
		MeterTypeModel entity = null;
		List<MeterTypeModel> list = persistenceSession.createQuery(criteria).getResultList();
		if (!list.isEmpty()) {
			entity = list.get(0);
		}
		closeCurrentSessionReadwithTransaction();
		return entity;
	}

	public MeterTypeModel findByMeter(MeterModel meter) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<MeterTypeModel> criteria = builder.createQuery(MeterTypeModel.class);
		Root<MeterTypeModel> root = criteria.from(MeterTypeModel.class);
		Join<MeterTypeModel, MeterModel> join = root.join(MeterTypeModel_.meters, JoinType.INNER);

		criteria.select(root);
		criteria.where(builder.equal(join.get(MeterModel_.id), meter.getId()));
		MeterTypeModel entity = null;
		List<MeterTypeModel> list = persistenceSession.createQuery(criteria).getResultList();
		if (!list.isEmpty()) {
			entity = list.get(0);
		}
		closeCurrentSessionReadwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<MeterTypeModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<MeterTypeModel> query = persistenceSession.createQuery("FROM MeterTypeModel");
		List<MeterTypeModel> meterTypeList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return meterTypeList;
	}

}
