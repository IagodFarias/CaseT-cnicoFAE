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

import java.util.Date;
import java.util.List;

import si.dbcomm.dao.DimensionalDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CarcassBatchModel;
import si.dbcomm.model.DimensionalModel;

/**
 * @author marcos
 *
 */
public class DimensionalService implements ServiceInterface<DimensionalModel, Long, String> {
	private static DimensionalDao dimensionalDao;

	/**
	 * Creates a object for class ProcessService.java
	 */
	public DimensionalService() {
		dimensionalDao = new DimensionalDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public DimensionalModel persist(DimensionalModel entity) throws CrudDatabaseException {
		return dimensionalDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public DimensionalModel update(DimensionalModel entity) throws CrudDatabaseException {
		return dimensionalDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public DimensionalModel delete(DimensionalModel entity) throws CrudDatabaseException {
		return dimensionalDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public DimensionalModel read(DimensionalModel entity) {
		return dimensionalDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public DimensionalModel findById(Long id) {
		return dimensionalDao.findById(id);
	}

	/*
	 * 
	 */
	public DimensionalModel findByDescription(String description) {
		return dimensionalDao.findByDescription(description);
	}

	/*
	 * 
	 */
	public DimensionalModel findByCarcassBatch(CarcassBatchModel carcassBatch) {
		return dimensionalDao.findByCarcassBatch(carcassBatch);
	}

	/*
	 * 
	 */
	public List<DimensionalModel> findAllByFilter(DimensionalModel dimensional) {
		return dimensionalDao.findAllByFilter(dimensional);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<DimensionalModel> findAll() {
		return dimensionalDao.findAll();
	}
}
