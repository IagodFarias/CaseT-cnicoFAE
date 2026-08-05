//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file HumiditySensorService.java
*    @author marcos
*    @date 4 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.HumiditySensorDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.HumiditySensorModel;

/**
 * @author marcos
 *
 */
public class HumiditySensorService implements ServiceInterface<HumiditySensorModel, Long, String> {
	private static HumiditySensorDao humiditySensorDao;

	/**
	 * Creates a object for class HumiditySensorService.java
	 */
	public HumiditySensorService() {
		humiditySensorDao = new HumiditySensorDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public HumiditySensorModel persist(HumiditySensorModel entity) throws CrudDatabaseException {
		return humiditySensorDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public HumiditySensorModel update(HumiditySensorModel entity) throws CrudDatabaseException {
		return humiditySensorDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public HumiditySensorModel delete(HumiditySensorModel entity) throws CrudDatabaseException {
		return humiditySensorDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public HumiditySensorModel read(HumiditySensorModel entity) {
		return humiditySensorDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public HumiditySensorModel findById(Long id) {
		return humiditySensorDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<HumiditySensorModel> findAll() {
		return humiditySensorDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag(java.io.Serializable)
	 */
	public HumiditySensorModel findByTag(String tag) {
		return humiditySensorDao.findByTag(tag);
	}
}
