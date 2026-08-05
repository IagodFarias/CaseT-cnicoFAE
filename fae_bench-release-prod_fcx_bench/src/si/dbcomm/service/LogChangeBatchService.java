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

import si.dbcomm.dao.LogChangeBatchDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.LogChangeBatchModel;

/**
 * @author marcos
 *
 */
public class LogChangeBatchService implements ServiceInterface<LogChangeBatchModel, Long, String> {
	private LogChangeBatchDao logChangeBatchDao;

	/**
	 * Creates a object for class LevelSensorService.java
	 */
	public LogChangeBatchService() {
		logChangeBatchDao = new LogChangeBatchDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public LogChangeBatchModel persist(LogChangeBatchModel entity) throws CrudDatabaseException {
		return logChangeBatchDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public LogChangeBatchModel update(LogChangeBatchModel entity) throws CrudDatabaseException {
		return logChangeBatchDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public LogChangeBatchModel delete(LogChangeBatchModel entity) throws CrudDatabaseException {
		return logChangeBatchDao.delete(entity);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public LogChangeBatchModel read(LogChangeBatchModel entity) {
		return logChangeBatchDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public LogChangeBatchModel findById(Long id) {
		return logChangeBatchDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<LogChangeBatchModel> findAll() {
		return logChangeBatchDao.findAll();
	}
}
