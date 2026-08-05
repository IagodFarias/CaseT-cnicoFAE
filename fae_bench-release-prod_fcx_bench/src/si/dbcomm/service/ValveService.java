//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ValveService.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.ValveDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.ValveModel;

/**
 * @author marcos
 *
 */
public class ValveService implements ServiceInterface<ValveModel, Long, String> {
	private static ValveDao valveDao;

	/**
	 * Creates a object for class ValveService.java
	 */
	public ValveService() {
		valveDao = new ValveDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public ValveModel persist(ValveModel entity) throws CrudDatabaseException {
		return valveDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public ValveModel update(ValveModel entity) throws CrudDatabaseException {
		return valveDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public ValveModel delete(ValveModel entity) throws CrudDatabaseException {
		return valveDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public ValveModel read(ValveModel entity) {
		return valveDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public ValveModel findById(Long id) {
		return valveDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<ValveModel> findAll() throws CrudDatabaseException {
		return valveDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag(java.io.Serializable)
	 */
	public ValveModel findByTag(String tag) {
		return valveDao.findByTag(tag);
	}
}
