//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CalculatedFixedConstService.java
*    @author Marcos
*    @date 6 de abr de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.CalculatedFixedConstDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CalculatedFixedConstModel;

/**
 * @author Marcos
 *
 */
public class CalculatedFixedConstService implements ServiceInterface<CalculatedFixedConstModel, Long, String> {

	CalculatedFixedConstDao constDao = new CalculatedFixedConstDao();

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public CalculatedFixedConstModel persist(CalculatedFixedConstModel entity) throws CrudDatabaseException {
		return constDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public CalculatedFixedConstModel update(CalculatedFixedConstModel entity) throws CrudDatabaseException {
		return constDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public CalculatedFixedConstModel delete(CalculatedFixedConstModel entity) throws CrudDatabaseException {
		return constDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public CalculatedFixedConstModel read(CalculatedFixedConstModel entity) {
		return constDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public CalculatedFixedConstModel findById(Long id) {
		return constDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<CalculatedFixedConstModel> findAll() {
		return constDao.findAll();
	}

	/*
	 * 
	 */
	public List<CalculatedFixedConstModel> findByFlowRateLike(String like) {
		return constDao.findByFlowRateLike(like);
	}
}
