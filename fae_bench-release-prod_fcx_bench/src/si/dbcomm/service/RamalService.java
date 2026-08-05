//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RamalService.java
*    @author marcos
*    @date 18 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.RamalDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.RamalModel;

/**
 * @author marcos
 *
 */
public class RamalService implements ServiceInterface<RamalModel, Long, String> {
	private static RamalDao ramalDao;

	/**
	 * Creates a object for class RamalService.java
	 */
	public RamalService() {
		ramalDao = new RamalDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public RamalModel persist(RamalModel entity) throws CrudDatabaseException {
		return ramalDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public RamalModel update(RamalModel entity) throws CrudDatabaseException {
		return ramalDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public RamalModel delete(RamalModel entity) throws CrudDatabaseException {
		return ramalDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public RamalModel read(RamalModel entity) {
		return ramalDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public RamalModel findById(Long id) {
		return ramalDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<RamalModel> findAll() {
		return ramalDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag(java.io.Serializable)
	 */
	public RamalModel findByTag(String tag) {
		return ramalDao.findByTag(tag);
	}
}
