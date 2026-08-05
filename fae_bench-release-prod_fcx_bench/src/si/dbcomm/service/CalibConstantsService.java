//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CalibConstantsService.java
*    @author marcos
*    @date 27 de dez de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.CalibConstantsDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CalibConstantsModel;
import si.dbcomm.model.MeterModel;

/**
 * @author marcos
 *
 */
public class CalibConstantsService implements ServiceInterface<CalibConstantsModel, Long, String> {
	private CalibConstantsDao calibDao;

	/**
	 * Creates a object for class CalibConstantsService.java
	 */
	public CalibConstantsService() {
		calibDao = new CalibConstantsDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public CalibConstantsModel persist(CalibConstantsModel entity) throws CrudDatabaseException {
		return calibDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public CalibConstantsModel update(CalibConstantsModel entity) throws CrudDatabaseException {
		return calibDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public CalibConstantsModel delete(CalibConstantsModel entity) throws CrudDatabaseException {
		return calibDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public CalibConstantsModel read(CalibConstantsModel entity) {
		return calibDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public CalibConstantsModel findById(Long id) {
		return calibDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<CalibConstantsModel> findAll() {
		return calibDao.findAll();
	}

	public List<CalibConstantsModel> findConstantsByMeter(MeterModel meter) {
		return calibDao.findConstantsByMeter(meter);
	}
}
