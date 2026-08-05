//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file FlowRateService.java
*    @author marcos
*    @date 1 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.FlowRateDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.PidConfigModel;

/**
 * @author marcos
 *
 */
public class FlowRateService implements ServiceInterface<FlowRateModel, Long, String> {
	private static FlowRateDao flowRateDao;

	/**
	 * Creates a object for class FlowRateService.java
	 */
	public FlowRateService() {
		flowRateDao = new FlowRateDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceLayerInterface#persist(java.lang.Object)
	 */
	@Override
	public FlowRateModel persist(FlowRateModel entity) throws CrudDatabaseException {
		return flowRateDao.persist(entity);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceLayerInterface#update(java.lang.Object)
	 */
	@Override
	public FlowRateModel update(FlowRateModel entity) throws CrudDatabaseException {
		return flowRateDao.update(entity);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceLayerInterface#delete(java.lang.Object)
	 */
	@Override
	public FlowRateModel delete(FlowRateModel entity) throws CrudDatabaseException {
		return flowRateDao.delete(entity);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceLayerInterface#read(java.lang.Object)
	 */
	@Override
	public FlowRateModel read(FlowRateModel entity) {
		return flowRateDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceLayerInterface#findById(java.io.Serializable)
	 */
	@Override
	public FlowRateModel findById(Long id) {
		return flowRateDao.findById(id);
	}

	public PidConfigModel findPidByFlowRate(FlowRateModel flowRate) {
		return flowRateDao.findPidByFlowRate(flowRate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceLayerInterface#findAll()
	 */
	@Override
	public List<FlowRateModel> findAll() {
		return flowRateDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	public List<FlowRateModel> findByFlowRateLike(String tag) {
		return flowRateDao.findByFlowRateLike(tag);
	}
}
