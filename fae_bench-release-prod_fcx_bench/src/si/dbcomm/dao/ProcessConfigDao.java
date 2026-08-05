//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ProcessConfigDao.java
*    @author marcos
*    @date 10 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.FlowRateModel_;
import si.dbcomm.model.ProcessConfigModel;
import si.dbcomm.model.ProcessConfigModel_;

/**
 * @author marcos
 *
 */
public class ProcessConfigDao extends DaoParent implements DaoInterface<ProcessConfigModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public ProcessConfigModel persist(ProcessConfigModel entity) throws CrudDatabaseException {
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
	public ProcessConfigModel update(ProcessConfigModel entity) throws CrudDatabaseException {
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
	public ProcessConfigModel delete(ProcessConfigModel entity) throws CrudDatabaseException {
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
	public ProcessConfigModel read(ProcessConfigModel entity) {
		openCurrentSessionwithTransaction();
		ProcessConfigModel process = persistenceSession.get(ProcessConfigModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return process;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public ProcessConfigModel findById(Long id) {
		openCurrentSessionwithTransaction();
		ProcessConfigModel process = persistenceSession.get(ProcessConfigModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return process;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<ProcessConfigModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<ProcessConfigModel> query = persistenceSession.createQuery("FROM ProcessConfigModel");
		List<ProcessConfigModel> processes = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return processes;
	}

	public Set<FlowRateModel> findFlowRatesByProcessConfig(ProcessConfigModel processConfig) {
		openCurrentSessionwithTransaction();
		Set<FlowRateModel> retorno;
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<ProcessConfigModel> criteriaQuery = builder.createQuery(ProcessConfigModel.class);
		Root<ProcessConfigModel> root = criteriaQuery.from(ProcessConfigModel.class);
		criteriaQuery.where(builder.equal(root.get(ProcessConfigModel_.id), processConfig.getId()));
		ProcessConfigModel processConfigAux = persistenceSession.createQuery(criteriaQuery).getSingleResult();
		retorno = processConfigAux.getCalibFlowRates();
		closeCurrentSessionReadwithTransaction();
		return retorno;
	}

	public List<FlowRateModel> findVerifFlowRatesByProcessConfig(ProcessConfigModel processConfig) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<FlowRateModel> criteriaQuery = builder.createQuery(FlowRateModel.class);
		Root<FlowRateModel> root = criteriaQuery.from(FlowRateModel.class);
		criteriaQuery.where(builder.equal(root.get(FlowRateModel_.id), processConfig.getVerifFlowRates()));
		List<FlowRateModel> flowRateList = persistenceSession.createQuery(criteriaQuery).getResultList();
		closeCurrentSessionReadwithTransaction();
		return flowRateList;
	}

}
