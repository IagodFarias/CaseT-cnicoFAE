//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterTypeService.java
*    @author marcos
*    @date 19 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.MeterTypeDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.MeterTypeModel;

/**
 * @author marcos
 *
 */
public class MeterTypeService implements ServiceInterface<MeterTypeModel, Long, String> {
	private static MeterTypeDao meterTypeDao;

	/**
	 * Creates a object for class MeterTypeService.java
	 */
	public MeterTypeService() {
		meterTypeDao = new MeterTypeDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public MeterTypeModel persist(MeterTypeModel entity) throws CrudDatabaseException {
		return meterTypeDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public MeterTypeModel update(MeterTypeModel entity) throws CrudDatabaseException {
		return meterTypeDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public MeterTypeModel delete(MeterTypeModel entity) throws CrudDatabaseException {
		return meterTypeDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public MeterTypeModel read(MeterTypeModel entity) {
		return meterTypeDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public MeterTypeModel findById(Long id) {
		return meterTypeDao.findById(id);
	}

	public MeterTypeModel findByBatch(BatchModel batch) {
		return meterTypeDao.findByBatch(batch);
	}

	public MeterTypeModel findByMeter(MeterModel meter) {
		return meterTypeDao.findByMeter(meter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<MeterTypeModel> findAll() {
		return meterTypeDao.findAll();
	}
}
