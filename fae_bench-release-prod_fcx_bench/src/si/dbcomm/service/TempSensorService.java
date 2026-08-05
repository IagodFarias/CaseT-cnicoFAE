//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file TempSensorService.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.TempSensorDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.TempSensorModel;

/**
 * @author marcos
 *
 */
public class TempSensorService implements ServiceInterface<TempSensorModel, Long, String> {
	private static TempSensorDao tempDao;

	/**
	 * Creates a object for class TempSensorService.java
	 */
	public TempSensorService() {
		tempDao = new TempSensorDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public TempSensorModel persist(TempSensorModel entity) throws CrudDatabaseException {
		return tempDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public TempSensorModel update(TempSensorModel entity) throws CrudDatabaseException {
		return tempDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public TempSensorModel delete(TempSensorModel entity) throws CrudDatabaseException {
		return tempDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public TempSensorModel read(TempSensorModel entity) {
		return tempDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public TempSensorModel findById(Long id) {
		return tempDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<TempSensorModel> findAll() {
		return tempDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag(java.io.Serializable)
	 */
	public TempSensorModel findByTag(String tag) {
		return tempDao.findByTag(tag);
	}
}
