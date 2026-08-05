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

import si.dbcomm.dao.DimensionMeasureDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.DimensionMeasureModel;

/**
 * @author marcos
 *
 */
public class DimensionMeasureService implements ServiceInterface<DimensionMeasureModel, Long, String> {
	private static DimensionMeasureDao measureDao;

	/**
	 * Creates a object for class ProcessService.java
	 */
	public DimensionMeasureService() {
		measureDao = new DimensionMeasureDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public DimensionMeasureModel persist(DimensionMeasureModel entity) throws CrudDatabaseException {
		return measureDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public DimensionMeasureModel update(DimensionMeasureModel entity) throws CrudDatabaseException {
		return measureDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public DimensionMeasureModel delete(DimensionMeasureModel entity) throws CrudDatabaseException {
		return measureDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public DimensionMeasureModel read(DimensionMeasureModel entity) {
		return measureDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public DimensionMeasureModel findById(Long id) {
		return measureDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<DimensionMeasureModel> findAll() {
		return measureDao.findAll();
	}
}
