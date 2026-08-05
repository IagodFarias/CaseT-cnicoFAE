//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PumpService.java
*    @author marcos
*    @date 11 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.PumpDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.PumpModel;

/**
 * @author marcos
 *
 */
public class PumpService implements ServiceInterface<PumpModel, Long, String> {
	private static PumpDao pumpDao;

	/**
	 * 
	 * Creates a object for class PumpService.java
	 */
	public PumpService() {
		pumpDao = new PumpDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public PumpModel persist(PumpModel entity) throws CrudDatabaseException {
		return pumpDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public PumpModel update(PumpModel entity) throws CrudDatabaseException {
		return pumpDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public PumpModel delete(PumpModel entity) throws CrudDatabaseException {
		return pumpDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public PumpModel read(PumpModel entity) {
		return pumpDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public PumpModel findById(Long id) {
		return pumpDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<PumpModel> findAll() {
		return pumpDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag(java.io.Serializable)
	 */
	public PumpModel findByTag(String tag) {
		return pumpDao.findByTag(tag);
	}
}
