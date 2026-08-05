//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ProcessService.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.CarcassBatchDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CarcassBatchModel;

/**
 * @author marcos
 *
 */
public class CarcassBatchService implements ServiceInterface<CarcassBatchModel, Long, String> {
	private static CarcassBatchDao carcassBatchDao;

	/**
	 * Creates a object for class ProcessService.java
	 */
	public CarcassBatchService() {
		carcassBatchDao = new CarcassBatchDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public CarcassBatchModel persist(CarcassBatchModel entity) throws CrudDatabaseException {
		return carcassBatchDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public CarcassBatchModel update(CarcassBatchModel entity) throws CrudDatabaseException {
		return carcassBatchDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public CarcassBatchModel delete(CarcassBatchModel entity) throws CrudDatabaseException {
		return carcassBatchDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public CarcassBatchModel read(CarcassBatchModel entity) {
		return carcassBatchDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public CarcassBatchModel findById(Long id) {
		return carcassBatchDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<CarcassBatchModel> findAll() {
		return carcassBatchDao.findAll();
	}
}
