//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file VolumeService.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.VolumeDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.VolumeModel;

/**
 * @author marcos
 *
 */
public class VolumeService implements ServiceInterface<VolumeModel, Long, String> {
	private static VolumeDao volumeDao;

	/**
	 * Creates a object for class VolumeService.java
	 */
	public VolumeService() {
		volumeDao = new VolumeDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public VolumeModel persist(VolumeModel entity) throws CrudDatabaseException {
		return volumeDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public VolumeModel update(VolumeModel entity) throws CrudDatabaseException {
		return volumeDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public VolumeModel delete(VolumeModel entity) throws CrudDatabaseException {
		return volumeDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public VolumeModel read(VolumeModel entity) {
		return volumeDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public VolumeModel findById(Long id) {
		return volumeDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<VolumeModel> findAll() {
		return volumeDao.findAll();
	}
}
