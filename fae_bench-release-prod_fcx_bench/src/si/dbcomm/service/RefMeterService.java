//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RefMeterService.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.RefMeterDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.RefMeterModel;

/**
 * @author marcos
 *
 */
public class RefMeterService implements ServiceInterface<RefMeterModel, Long, String> {
	private static RefMeterDao refMeterDao;

	/**
	 * Creates a object for class RefMeterService.java
	 */
	public RefMeterService() {
		refMeterDao = new RefMeterDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public RefMeterModel persist(RefMeterModel entity) throws CrudDatabaseException {
		return refMeterDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public RefMeterModel update(RefMeterModel entity) throws CrudDatabaseException {
		return refMeterDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public RefMeterModel delete(RefMeterModel entity) throws CrudDatabaseException {
		return refMeterDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public RefMeterModel read(RefMeterModel entity) {
		return refMeterDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public RefMeterModel findById(Long id) {
		return refMeterDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<RefMeterModel> findAll() {
		return refMeterDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag(java.io.Serializable)
	 */
	public RefMeterModel findByTag(String tag) {
		return refMeterDao.findByTag(tag);
	}
}
