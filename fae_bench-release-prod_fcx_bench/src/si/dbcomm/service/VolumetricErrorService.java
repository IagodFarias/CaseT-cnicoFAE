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

import si.dbcomm.dao.VolumetricErrorDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.VolumetricErrorModel;

/**
 * @author marcos
 *
 */
public class VolumetricErrorService implements ServiceInterface<VolumetricErrorModel, Long, String> {
	private static VolumetricErrorDao volumetricErrorDao;

	/**
	 * Creates a object for class ProcessService.java
	 */
	public VolumetricErrorService() {
		volumetricErrorDao = new VolumetricErrorDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public VolumetricErrorModel persist(VolumetricErrorModel entity) throws CrudDatabaseException {
		return volumetricErrorDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public VolumetricErrorModel update(VolumetricErrorModel entity) throws CrudDatabaseException {
		return volumetricErrorDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public VolumetricErrorModel delete(VolumetricErrorModel entity) throws CrudDatabaseException {
		return volumetricErrorDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public VolumetricErrorModel read(VolumetricErrorModel entity) {
		return volumetricErrorDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public VolumetricErrorModel findById(Long id) {
		return volumetricErrorDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<VolumetricErrorModel> findAll() {
		return volumetricErrorDao.findAll();
	}
}
