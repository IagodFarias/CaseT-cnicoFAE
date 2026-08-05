//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file VerificationErrorService.java
*    @author marcos
*    @date 20 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.VerificationErrorDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.VerificationErrorModel;

/**
 * @author marcos
 *
 */
public class VerificationErrorService implements ServiceInterface<VerificationErrorModel, Long, String> {
	private static VerificationErrorDao verifErrorDao;

	public VerificationErrorService() {
		verifErrorDao = new VerificationErrorDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public VerificationErrorModel persist(VerificationErrorModel entity) throws CrudDatabaseException {
		return verifErrorDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public VerificationErrorModel update(VerificationErrorModel entity) throws CrudDatabaseException {
		return verifErrorDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public VerificationErrorModel delete(VerificationErrorModel entity) throws CrudDatabaseException {
		return verifErrorDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public VerificationErrorModel read(VerificationErrorModel entity) {
		return verifErrorDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public VerificationErrorModel findById(Long id) {
		return verifErrorDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<VerificationErrorModel> findAll() {
		return verifErrorDao.findAll();
	}

	public List<VerificationErrorModel> findByMeter(MeterModel meter) {
		return verifErrorDao.findByMeter(meter);
	}
}
