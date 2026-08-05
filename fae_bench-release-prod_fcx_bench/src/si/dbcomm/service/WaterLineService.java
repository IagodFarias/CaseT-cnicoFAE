//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file WaterLineService.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.WaterLineDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.WaterLineModel;

/**
 * @author marcos
 *
 */
public class WaterLineService implements ServiceInterface<WaterLineModel, Long, String> {
	private static WaterLineDao lineDao;

	/**
	 * Creates a object for class WaterLineService.java
	 */
	public WaterLineService() {
		lineDao = new WaterLineDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public WaterLineModel persist(WaterLineModel entity) throws CrudDatabaseException {
		return lineDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public WaterLineModel update(WaterLineModel entity) throws CrudDatabaseException {
		return lineDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public WaterLineModel delete(WaterLineModel entity) throws CrudDatabaseException {
		return lineDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public WaterLineModel read(WaterLineModel entity) {
		return lineDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public WaterLineModel findById(Long id) {
		return lineDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<WaterLineModel> findAll() {
		return lineDao.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findByTag()
	 */
	public WaterLineModel findByTag(String tag) {
		return lineDao.findByTag(tag);
	}
}
