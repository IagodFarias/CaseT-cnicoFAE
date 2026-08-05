//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file FlowRateDao.java
*    @author marcos
*    @date 1 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.FlowRateModel_;
import si.dbcomm.model.PidConfigModel;
import si.dbcomm.model.PidConfigModel_;

/**
 * @author marcos
 *
 */
public class FlowRateDao extends DaoParent implements DaoInterface<FlowRateModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.connection.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public FlowRateModel persist(FlowRateModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		entity.setId((long) persistenceSession.save(entity));
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.connection.DaoInterface#update(java.lang.Object)
	 */
	@Override
	public FlowRateModel update(FlowRateModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.update(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.connection.DaoInterface#delete(java.lang.Object)
	 */
	@Override
	public FlowRateModel delete(FlowRateModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.delete(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.connection.DaoInterface#read(java.lang.Object)
	 */
	@Override
	public FlowRateModel read(FlowRateModel entity) {
		openCurrentSessionwithTransaction();
		FlowRateModel flowRate = persistenceSession.get(FlowRateModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return flowRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.connection.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public FlowRateModel findById(Long id) {
		openCurrentSessionwithTransaction();
		FlowRateModel flowRate = persistenceSession.get(FlowRateModel.class, id);

		// flowRate.getPidConfig();
		closeCurrentSessionReadwithTransaction();
		return flowRate;
	}

	public PidConfigModel findPidByFlowRate(FlowRateModel flowRate) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<PidConfigModel> criteria = builder.createQuery(PidConfigModel.class);
		Root<PidConfigModel> root = criteria.from(PidConfigModel.class);
		criteria.select(root);
		criteria.where(builder.equal(root.get(PidConfigModel_.flowRate), flowRate.getId()));
		PidConfigModel retorno = persistenceSession.createQuery(criteria).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return retorno;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.connection.DaoInterface#findAll()
	 */
	@Override
	public List<FlowRateModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		List<FlowRateModel> flowRates = persistenceSession.createQuery("from FlowRateModel").getResultList();
		closeCurrentSessionReadwithTransaction();
		return flowRates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public List<FlowRateModel> findByFlowRateLike(String tag) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();

		CriteriaQuery<FlowRateModel> criteria = builder.createQuery(FlowRateModel.class);
		Root<FlowRateModel> root = criteria.from(FlowRateModel.class);
		criteria.select(root);
		criteria.where(builder.like(root.get(FlowRateModel_.flowRate).as(String.class), tag + "%"));
		List<FlowRateModel> list = persistenceSession.createQuery(criteria).getResultList();

		closeCurrentSessionReadwithTransaction();
		return list;
	}
}
