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

import si.dbcomm.dao.QuotaDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.QuotaModel;

/**
 * @author marcos
 *
 */
public class QuotaService implements ServiceInterface<QuotaModel, Long, String> {
	private static QuotaDao quotaDao;

	/**
	 * Creates a object for class ProcessService.java
	 */
	public QuotaService() {
		quotaDao = new QuotaDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public QuotaModel persist(QuotaModel entity) throws CrudDatabaseException {
		return quotaDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public QuotaModel update(QuotaModel entity) throws CrudDatabaseException {
		return quotaDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public QuotaModel delete(QuotaModel entity) throws CrudDatabaseException {
		return quotaDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public QuotaModel read(QuotaModel entity) {
		return quotaDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public QuotaModel findById(Long id) {
		return quotaDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<QuotaModel> findAll() {
		return quotaDao.findAll();
	}
}
