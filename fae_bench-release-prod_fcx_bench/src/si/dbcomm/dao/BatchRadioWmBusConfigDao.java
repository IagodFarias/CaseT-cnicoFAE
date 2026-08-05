//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BatchRadioWmBusConfigDao.java
*    @author Marcos
*    @date 15 de mar de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;

import si.dbcomm.model.BatchRadioWmBusConfigModel_;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchRadioWmBusConfigModel;

/**
 * @author Marcos
 *
 */
public class BatchRadioWmBusConfigDao extends DaoParent implements DaoInterface<BatchRadioWmBusConfigModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public BatchRadioWmBusConfigModel persist(BatchRadioWmBusConfigModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.persist(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#update(java.lang.Object)
	 */
	@Override
	public BatchRadioWmBusConfigModel update(BatchRadioWmBusConfigModel entity) throws CrudDatabaseException {
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
	public BatchRadioWmBusConfigModel delete(BatchRadioWmBusConfigModel entity) throws CrudDatabaseException {
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
	public BatchRadioWmBusConfigModel read(BatchRadioWmBusConfigModel entity) {
		openCurrentSessionwithTransaction();
		BatchRadioWmBusConfigModel batchRadioConfig = persistenceSession.get(BatchRadioWmBusConfigModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return batchRadioConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public BatchRadioWmBusConfigModel findById(Long id) {
		openCurrentSessionwithTransaction();
		BatchRadioWmBusConfigModel batchRadioConfig = persistenceSession.get(BatchRadioWmBusConfigModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return batchRadioConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<BatchRadioWmBusConfigModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<BatchRadioWmBusConfigModel> query = persistenceSession.createQuery("FROM BatchRadioWmBusConfigModel");
		List<BatchRadioWmBusConfigModel> batchRadioConfigList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return batchRadioConfigList;
	}

	/**
	 * @param id
	 * @return
	 */
	public BatchRadioWmBusConfigModel findByBatchId(Long id) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<BatchRadioWmBusConfigModel> criteriaQuery = builder.createQuery(BatchRadioWmBusConfigModel.class);
		Root<BatchRadioWmBusConfigModel> root = criteriaQuery.from(BatchRadioWmBusConfigModel.class);
		criteriaQuery.where(builder.equal(root.get(BatchRadioWmBusConfigModel_.batch), id));
		BatchRadioWmBusConfigModel retorno = null;
		List<BatchRadioWmBusConfigModel> batchRadioConfigListAux = persistenceSession.createQuery(criteriaQuery).getResultList();
		if (!batchRadioConfigListAux.isEmpty()) {
			retorno = batchRadioConfigListAux.get(0);
		}
		closeCurrentSessionReadwithTransaction();
		return retorno;
	}
}
