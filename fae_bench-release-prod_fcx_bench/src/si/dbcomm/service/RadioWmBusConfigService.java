//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RadioWmBusConfigService.java
*    @author Marcos
*    @date 15 de mar de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.RadioWmBusConfigDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.RadioWmBusConfigModel;

/**
 * @author Marcos
 *
 */
public class RadioWmBusConfigService implements ServiceInterface<RadioWmBusConfigModel, Long, String> {
	private static RadioWmBusConfigDao radioDao = new RadioWmBusConfigDao();

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public RadioWmBusConfigModel persist(RadioWmBusConfigModel entity) throws CrudDatabaseException {
		return radioDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public RadioWmBusConfigModel update(RadioWmBusConfigModel entity) throws CrudDatabaseException {
		return radioDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public RadioWmBusConfigModel delete(RadioWmBusConfigModel entity) throws CrudDatabaseException {
		return radioDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public RadioWmBusConfigModel read(RadioWmBusConfigModel entity) {
		return radioDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public RadioWmBusConfigModel findById(Long id) {
		return radioDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<RadioWmBusConfigModel> findAll() {
		return radioDao.findAll();
	}
	
	/**
	 * 
	 */
	public List<RadioWmBusConfigModel> findAllByMeterId(Long meterId) {
		return radioDao.findAllByMeterId(meterId);
	}
}
