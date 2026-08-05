//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file LevelSensorService.java
*    @author marcos
*    @date 4 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.LevelSensorDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.LevelSensorModel;

/**
 * @author marcos
 *
 */
public class LevelSensorService implements ServiceInterface<LevelSensorModel, Long, String> {
	private LevelSensorDao levelSensorDao;

	/**
	 * Creates a object for class LevelSensorService.java
	 */
	public LevelSensorService() {
		levelSensorDao = new LevelSensorDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public LevelSensorModel persist(LevelSensorModel entity) throws CrudDatabaseException {
		return levelSensorDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public LevelSensorModel update(LevelSensorModel entity) throws CrudDatabaseException {
		return levelSensorDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public LevelSensorModel delete(LevelSensorModel entity) throws CrudDatabaseException {
		return levelSensorDao.delete(entity);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public LevelSensorModel read(LevelSensorModel entity) {
		return levelSensorDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public LevelSensorModel findById(Long id) {
		return levelSensorDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<LevelSensorModel> findAll() {
		return levelSensorDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag(java.io.Serializable)
	 */
	public LevelSensorModel findByTag(String tag) {
		return levelSensorDao.findByTag(tag);
	}
}
