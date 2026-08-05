//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ScaleModel.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.ScaleDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.ScaleModel;

/**
 * @author marcos
 *
 */
public class ScaleService implements ServiceInterface<ScaleModel, Long, String> {
	private static ScaleDao scaleDao;

	/**
	 * 
	 */
	public ScaleService() {
		scaleDao = new ScaleDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public ScaleModel persist(ScaleModel entity) throws CrudDatabaseException {
		return scaleDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public ScaleModel update(ScaleModel entity) throws CrudDatabaseException {
		return scaleDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public ScaleModel delete(ScaleModel entity) throws CrudDatabaseException {
		return scaleDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public ScaleModel read(ScaleModel entity) {
		return scaleDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public ScaleModel findById(Long id) {
		return scaleDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<ScaleModel> findAll() {
		return scaleDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag(java.io.Serializable)
	 */
	public ScaleModel findByTag(String tag) {
		return scaleDao.findByTag(tag);
	}
}
