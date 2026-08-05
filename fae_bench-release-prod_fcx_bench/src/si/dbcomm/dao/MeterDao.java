//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterDao.java
*    @author marcos
*    @date 16 de ago de 2016
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

import org.hibernate.Hibernate;
import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BatchModel_;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.BateladaModel_;
import si.dbcomm.model.ConversorModel;
import si.dbcomm.model.ConversorModel_;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.MeterModel_;

/**
 * @author marcos
 *
 */
public class MeterDao extends DaoParent implements DaoInterface<MeterModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public MeterModel persist(MeterModel entity) throws CrudDatabaseException {
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
	public MeterModel update(MeterModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.update(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	public MeterModel merge(MeterModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.merge(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#delete(java.lang.Object)
	 */
	@Override
	public MeterModel delete(MeterModel entity) throws CrudDatabaseException {
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
	public MeterModel read(MeterModel entity) {
		openCurrentSessionwithTransaction();
		MeterModel meter = persistenceSession.get(MeterModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return meter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public MeterModel findById(Long id) {
		openCurrentSessionwithTransaction();
		MeterModel meter = persistenceSession.get(MeterModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return meter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<MeterModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<MeterModel> query = persistenceSession.createQuery("FROM MeterModel");
		List<MeterModel> meterList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return meterList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public MeterModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		MeterModel meter = persistenceSession.get(MeterModel.class, tag);
		closeCurrentSessionReadwithTransaction();
		return meter;
	}

	public MeterModel findBySerialNumber(String serial) {
		openCurrentSessionwithTransaction();
		MeterModel temp = null;
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<MeterModel> criteriaQuery = cb.createQuery(MeterModel.class);
		Root<MeterModel> from = criteriaQuery.from(MeterModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(MeterModel_.serialNumber), serial));
		List<MeterModel> list = persistenceSession.createQuery(criteriaQuery).getResultList();
		if (!list.isEmpty()) {
			temp = list.get(0);
		}
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	public MeterModel findLastCalibratedMeter(int ufoid) {
		openCurrentSessionwithTransaction();
		MeterModel temp = null;
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<MeterModel> criteriaQuery = cb.createQuery(MeterModel.class);
		Root<MeterModel> from = criteriaQuery.from(MeterModel.class);
		criteriaQuery.select(from).orderBy(cb.desc(from.get(MeterModel_.dateOfCalibration)));
		criteriaQuery.where(cb.equal(from.get(MeterModel_.meterUfoId), ufoid));
		List<MeterModel> list = persistenceSession.createQuery(criteriaQuery).getResultList();
		if (!list.isEmpty()) {
			temp = list.get(0);
		}
		closeCurrentSessionReadwithTransaction();
		return temp;
	}
	
	public MeterModel findLastApprovedCalibratedMeter(int ufoid) {
		openCurrentSessionwithTransaction();
		MeterModel entity = null;
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<MeterModel> criteriaQuery = cb.createQuery(MeterModel.class);
		Root<MeterModel> from = criteriaQuery.from(MeterModel.class);
		criteriaQuery.select(from).orderBy(cb.desc(from.get(MeterModel_.dateOfCalibration)));
		criteriaQuery.where(cb.and(
				cb.equal(from.get(MeterModel_.meterUfoId), ufoid),
				cb.equal(from.get(MeterModel_.isApprovedVerification), true)));
		List<MeterModel> list = persistenceSession.createQuery(criteriaQuery).getResultList();
		if (!list.isEmpty()) {
			entity = list.get(0);
			Hibernate.initialize(entity.getBatch());
		}
		closeCurrentSessionReadwithTransaction();
		return entity;
	}
	

	public List<MeterModel> findAllByChipId(int chipId) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<MeterModel> criteriaQuery = cb.createQuery(MeterModel.class);
		Root<MeterModel> from = criteriaQuery.from(MeterModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(MeterModel_.meterUfoId), chipId));
		List<MeterModel> list = persistenceSession.createQuery(criteriaQuery).getResultList();
		closeCurrentSessionReadwithTransaction();
		return list;
	}

	public MeterModel findByBatchAndBateladaAndPosition(BatchModel batchModel, BateladaModel bateladaModel, int position) {
		openCurrentSessionwithTransaction();

		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<MeterModel> criteria = builder.createQuery(MeterModel.class);

		Root<MeterModel> root = criteria.from(MeterModel.class);
		Join<MeterModel, BatchModel> joinBatch = root.join(MeterModel_.batch, JoinType.INNER);
		Join<MeterModel, BateladaModel> joinBatelada = root.join(MeterModel_.batelada, JoinType.INNER);
		Join<MeterModel, ConversorModel> joinPos = root.join(MeterModel_.conversor, JoinType.INNER);
		criteria.select(root);

		criteria.where(builder.equal(joinBatch.get(BatchModel_.id), batchModel.getId()), builder.equal(joinBatelada.get(BateladaModel_.id), bateladaModel.getId()),
				builder.equal(joinPos.get(ConversorModel_.id), position));

		List<MeterModel> meterList = null;
		MeterModel meterModel = null;

		meterList = persistenceSession.createQuery(criteria).getResultList();
		if (!meterList.isEmpty()) {
			meterModel = meterList.get(0);
		}

		closeCurrentSessionReadwithTransaction();
		return meterModel;
	}
}
