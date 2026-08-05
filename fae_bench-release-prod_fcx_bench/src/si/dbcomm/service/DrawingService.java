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

import si.dbcomm.dao.DrawingDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.DrawingModel;

/**
 * @author marcos
 *
 */
public class DrawingService implements ServiceInterface<DrawingModel, Long, String> {
	private static DrawingDao drawingDao;

	/**
	 * Creates a object for class ProcessService.java
	 */
	public DrawingService() {
		drawingDao = new DrawingDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public DrawingModel persist(DrawingModel entity) throws CrudDatabaseException {
		return drawingDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public DrawingModel update(DrawingModel entity) throws CrudDatabaseException {
		return drawingDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public DrawingModel delete(DrawingModel entity) throws CrudDatabaseException {
		return drawingDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public DrawingModel read(DrawingModel entity) {
		return drawingDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public DrawingModel findById(Long id) {
		return drawingDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<DrawingModel> findAll() {
		return drawingDao.findAll();
	}
}
