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

import si.dbcomm.dao.VolumetricBenchDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.VolumetricBenchModel;

/**
 * @author marcos
 *
 */
public class VolumetricBenchService implements ServiceInterface<VolumetricBenchModel, Long, String> {
	private static VolumetricBenchDao benchVolumetricDao;

	/**
	 * Creates a object for class ProcessService.java
	 */
	public VolumetricBenchService() {
		benchVolumetricDao = new VolumetricBenchDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public VolumetricBenchModel persist(VolumetricBenchModel entity) throws CrudDatabaseException {
		return benchVolumetricDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public VolumetricBenchModel update(VolumetricBenchModel entity) throws CrudDatabaseException {
		return benchVolumetricDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public VolumetricBenchModel delete(VolumetricBenchModel entity) throws CrudDatabaseException {
		return benchVolumetricDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public VolumetricBenchModel read(VolumetricBenchModel entity) {
		return benchVolumetricDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public VolumetricBenchModel findById(Long id) {
		return benchVolumetricDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<VolumetricBenchModel> findAll() {
		return benchVolumetricDao.findAll();
	}
}
