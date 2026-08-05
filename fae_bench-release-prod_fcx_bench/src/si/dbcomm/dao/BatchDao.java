//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BatchDataDao.java
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

import org.hibernate.Hibernate;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BatchModel_;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.BateladaModel_;
import si.dbcomm.model.FirmwareModel;
import si.dbcomm.model.FirmwareModel_;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.MeterModel_;

/**
 * @author marcos
 *
 */
public class BatchDao extends DaoParent implements DaoInterface<BatchModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public BatchModel persist(BatchModel entity) throws CrudDatabaseException {
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
	public BatchModel update(BatchModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.update(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#update(java.lang.Object)
	 */
	public BatchModel merge(BatchModel entity) throws CrudDatabaseException {
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
	public BatchModel delete(BatchModel entity) throws CrudDatabaseException {
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
	public BatchModel read(BatchModel entity) {
		openCurrentSessionwithTransaction();
		BatchModel batch = persistenceSession.get(BatchModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return batch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public BatchModel findById(Long id) {
		openCurrentSessionwithTransaction();
		BatchModel benchData = persistenceSession.get(BatchModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return benchData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<BatchModel> findAll() {
		// openCurrentSessionwithTransaction();
		// @SuppressWarnings("unchecked")
		//// Query<BatchModel> query = persistenceSession.createQuery("FROM BatchModel");
		// Query<BatchModel> query = persistenceSession.createQuery("FROM BatchModel order by id_batch");
		// List<BatchModel> batchList = query.getResultList();
		// closeCurrentSessionReadwithTransaction();
		// return batchList;

		openCurrentSessionwithTransaction();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<BatchModel> criteriaQuery = cb.createQuery(BatchModel.class);
		Root<BatchModel> from = criteriaQuery.from(BatchModel.class);
		criteriaQuery.select(from).orderBy(cb.asc(from.get(BatchModel_.id)));
		// criteriaQuery.where(cb.equal(from.get(MeterModel_.meterUfoId), ufoid));
		List<BatchModel> list = persistenceSession.createQuery(criteriaQuery).getResultList();
		closeCurrentSessionReadwithTransaction();
		return list;
	}

	public List<BatchModel> findAllActive() {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<BatchModel> criteria = builder.createQuery(BatchModel.class);
		Root<BatchModel> root = criteria.from(BatchModel.class);
		// Join<BatchModel, MeterModel> join = root.join(BatchModel_.meters,
		// JoinType.INNER);

		criteria.select(root);
		criteria.where(builder.isFalse(root.get(BatchModel_.finished)));
		// BatchModel entity = null;
		List<BatchModel> list = persistenceSession.createQuery(criteria).getResultList();
		// if (!list.isEmpty()) {
		// entity = list.get(0);
		// }
		closeCurrentSessionReadwithTransaction();
		return list;
	}

	public BatchModel findByMeter(MeterModel meter) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<BatchModel> criteria = builder.createQuery(BatchModel.class);
		Root<BatchModel> root = criteria.from(BatchModel.class);
		Join<BatchModel, MeterModel> join = root.join(BatchModel_.meters, JoinType.INNER);

		criteria.select(root);
		criteria.where(builder.equal(join.get(MeterModel_.id), meter.getId()));
		BatchModel entity = null;
		List<BatchModel> list = persistenceSession.createQuery(criteria).getResultList();
		if (!list.isEmpty()) {
			entity = list.get(0);
		}
		closeCurrentSessionReadwithTransaction();
		return entity;
	}

	/**
	 * Finds Batch by batch id
	 * 
	 * @return BatchModel - object
	 */
	public BatchModel findByBatchId(String batchId) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<BatchModel> criteria = builder.createQuery(BatchModel.class);
		Root<BatchModel> root = criteria.from(BatchModel.class);
		criteria.select(root);
		criteria.where(builder.equal(root.get(BatchModel_.batchId), batchId));
		BatchModel retorno = persistenceSession.createQuery(criteria).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return retorno;
	}

	/**
	 * Finds the last Serial number used for the determined batch
	 * 
	 * @return BatchModel - object
	 */
	public String findLastSerialInBatch(String batchId) {
		BatchModel batchAux = findByBatchId(batchId);
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<MeterModel> criteriaQuery = builder.createQuery(MeterModel.class);
		Root<MeterModel> root = criteriaQuery.from(MeterModel.class);
		criteriaQuery.where(builder.equal(root.get(MeterModel_.batch), batchAux.getId()));
		criteriaQuery.orderBy(builder.desc(root.get(MeterModel_.serialNumber)));
		String retorno = null;
		List<MeterModel> meterAuxList = persistenceSession.createQuery(criteriaQuery).getResultList();
		if (!meterAuxList.isEmpty()) {
			retorno = meterAuxList.get(0).getSerialNumber();
		}
		closeCurrentSessionReadwithTransaction();
		return retorno;
	}

	/**
	 * Returns the run count
	 * 
	 * @param batchId
	 * @return
	 */
	public int getBatchRunCount(String batchId) {
		BatchModel batchAux = findByBatchId(batchId);
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<BateladaModel> criteria = builder.createQuery(BateladaModel.class);
		Root<BateladaModel> root = criteria.from(BateladaModel.class);
		criteria.select(root);
		criteria.where(builder.equal(root.get(BateladaModel_.batch), batchAux));
		criteria.orderBy(builder.desc(root.get(BateladaModel_.runCount)));
		List<BateladaModel> retorno = null;
		int runCount = 0;
		retorno = persistenceSession.createQuery(criteria).getResultList();
		if (!retorno.isEmpty()) {
			runCount = retorno.get(0).getRunCount();
		}
		closeCurrentSessionReadwithTransaction();
		return runCount;
	}

	/**
	 * Returns the run count
	 * 
	 * @param batchId
	 * @return
	 */
	public BateladaModel getBatchLastBatelada(String batchId) {
		BatchModel batchAux = findByBatchId(batchId);
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<BateladaModel> criteria = builder.createQuery(BateladaModel.class);
		Root<BateladaModel> root = criteria.from(BateladaModel.class);
		criteria.select(root);
		criteria.where(builder.equal(root.get(BateladaModel_.batch), batchAux));
		criteria.orderBy(builder.desc(root.get(BateladaModel_.runCount)));
		List<BateladaModel> lista = null;
		BateladaModel retorno = null;
		lista = persistenceSession.createQuery(criteria).getResultList();
		if (!lista.isEmpty()) {
			retorno = lista.get(0);
		}
		closeCurrentSessionReadwithTransaction();
		return retorno;
	}

	/**
	 * Returns the run count
	 * 
	 * @param batchId
	 * @return
	 */
	public BateladaModel getBatchLastBatelada(BatchModel batch) {
		BateladaModel retorno = null;
		if (batch != null) {

			openCurrentSessionwithTransaction();
			CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
			CriteriaQuery<BateladaModel> criteria = builder.createQuery(BateladaModel.class);
			Root<BateladaModel> root = criteria.from(BateladaModel.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get(BateladaModel_.batch), batch));
			criteria.orderBy(builder.desc(root.get(BateladaModel_.runCount)));
			List<BateladaModel> lista = null;
			lista = persistenceSession.createQuery(criteria).getResultList();
			if (!lista.isEmpty()) {
				retorno = lista.get(0);
			}
			closeCurrentSessionReadwithTransaction();
		}
		return retorno;
	}

//	/**
//	 * Returns the run count
//	 * 
//	 * @param batchId
//	 * @return
//	 */
//	public FirmwareModel findFirmwareByBatch(BatchModel batch) {
//		FirmwareModel result = null;
//		if (batch != null) {
//			openCurrentSessionwithTransaction();
//			CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
//			CriteriaQuery<BatchModel> criteria = builder.createQuery(BatchModel.class);
//			Root<BatchModel> root = criteria.from(BatchModel.class);
//			criteria.select(root);
//			criteria.where(builder.equal(root.get(BatchModel_.id), batch.getId()));
//			BatchModel entity = persistenceSession.createQuery(criteria).getSingleResult();
//			if (entity != null) {
//				Hibernate.initialize(entity.getFirmware());
//				result = entity.getFirmware();
//			}
//			closeCurrentSessionReadwithTransaction();
//		}
//		return result;
//	}
}
