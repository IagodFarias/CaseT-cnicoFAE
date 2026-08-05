//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PressureSensorService.java
*    @author marcos
*    @date 4 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.PressureSensorDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.PressureSensorModel;

/**
 * @author marcos
 *
 */
public class PressureSensorService implements ServiceInterface<PressureSensorModel, Long, String> {
	private static PressureSensorDao pressSensorDao;

	/**
	 * Creates a object for class PressureSensorService.java
	 */
	public PressureSensorService() {
		pressSensorDao = new PressureSensorDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public PressureSensorModel persist(PressureSensorModel entity) throws CrudDatabaseException {
		return pressSensorDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public PressureSensorModel update(PressureSensorModel entity) throws CrudDatabaseException {
		return pressSensorDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public PressureSensorModel delete(PressureSensorModel entity) throws CrudDatabaseException {
		return pressSensorDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public PressureSensorModel read(PressureSensorModel entity) {
		return pressSensorDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public PressureSensorModel findById(Long id) {
		return pressSensorDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<PressureSensorModel> findAll() {
		return pressSensorDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag(java.io.Serializable)
	 */
	public PressureSensorModel findByTag(String tag) {
		return pressSensorDao.findByTag(tag);
	}
}
