//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ProcessConfigService.java
*    @author marcos
*    @date 10 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;
import java.util.Set;

import si.dbcomm.dao.ProcessConfigDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.FlowRateModel;
import si.dbcomm.model.ProcessConfigModel;

/**
 * @author marcos
 *
 */
public class ProcessConfigService implements ServiceInterface<ProcessConfigModel, Long, String> {
	private static ProcessConfigDao processConfigDao;

	/**
	 * Creates a object for class ProcessConfigService.java
	 */
	public ProcessConfigService() {
		processConfigDao = new ProcessConfigDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public ProcessConfigModel persist(ProcessConfigModel entity) throws CrudDatabaseException {
		return processConfigDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public ProcessConfigModel update(ProcessConfigModel entity) throws CrudDatabaseException {
		return processConfigDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public ProcessConfigModel delete(ProcessConfigModel entity) throws CrudDatabaseException {
		return processConfigDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public ProcessConfigModel read(ProcessConfigModel entity) {
		return processConfigDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public ProcessConfigModel findById(Long id) {
		return processConfigDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<ProcessConfigModel> findAll() {
		return processConfigDao.findAll();
	}

	public Set<FlowRateModel> findFlowRatesByProcessConfig(ProcessConfigModel processConfig) {
		return processConfigDao.findFlowRatesByProcessConfig(processConfig);
	}

	public List<FlowRateModel> findVerifFlowRatesByProcessConfig(ProcessConfigModel processConfig) {
		return processConfigDao.findVerifFlowRatesByProcessConfig(processConfig);
	}
}
