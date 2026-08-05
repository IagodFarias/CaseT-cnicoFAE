//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BateladaService.java
*    @author marcos
*    @date 20 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.BateladaDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.MeterModel;

/**
 * @author marcos
 *
 */
public class BateladaService implements ServiceInterface<BateladaModel, Long, String> {

	private BateladaDao bateladaDao = new BateladaDao();

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public BateladaModel persist(BateladaModel entity) throws CrudDatabaseException {
		return bateladaDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public BateladaModel update(BateladaModel entity) throws CrudDatabaseException {
		return bateladaDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public BateladaModel delete(BateladaModel entity) throws CrudDatabaseException {
		return bateladaDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public BateladaModel read(BateladaModel entity) {
		return bateladaDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public BateladaModel findById(Long id) {
		return bateladaDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<BateladaModel> findAll() {
		return bateladaDao.findAll();
	}

	public List<BateladaModel> findAllByBatch(BatchModel batch) {
		return bateladaDao.findAllByBatch(batch);
	}
	
	public List<MeterModel> findAllMeters(BateladaModel batelada) {
		return bateladaDao.findAllMeters(batelada);
	}
}
