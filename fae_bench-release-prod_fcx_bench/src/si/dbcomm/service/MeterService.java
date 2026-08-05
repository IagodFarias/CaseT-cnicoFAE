//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file MeterService.java
*    @author marcos
*    @date 16 de ago de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.MeterDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.MeterModel;

/**
 * @author marcos
 *
 */
public class MeterService implements ServiceInterface<MeterModel, Long, String> {
	private MeterDao meterDao;

	/**
	 * Creates a object for class MeterService.java
	 */
	public MeterService() {
		this.meterDao = new MeterDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public MeterModel persist(MeterModel entity) throws CrudDatabaseException {
		return meterDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public MeterModel update(MeterModel entity) throws CrudDatabaseException {
		return meterDao.update(entity);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	public MeterModel merge(MeterModel entity) throws CrudDatabaseException {
		return meterDao.merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public MeterModel delete(MeterModel entity) throws CrudDatabaseException {
		return meterDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public MeterModel read(MeterModel entity) {
		return meterDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public MeterModel findById(Long id) {
		return meterDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<MeterModel> findAll() {
		return meterDao.findAll();
	}

	public MeterModel findBySerialNumber(String serial) {
		return meterDao.findBySerialNumber(serial);
	}

	public List<MeterModel> findAllByChipId(int chipId) {
		return meterDao.findAllByChipId(chipId);
	}

	public MeterModel findLastCalibratedMeter(int chipId) {
		return meterDao.findLastCalibratedMeter(chipId);
	}
	
	public MeterModel findLastApprovedCalibratedMeter(int chipId) {
		return meterDao.findLastApprovedCalibratedMeter(chipId);
	}
	
	

	public MeterModel findByBatchAndBateladaAndPosition(BatchModel batchModel, BateladaModel bateladaModel, int position) {
		return meterDao.findByBatchAndBateladaAndPosition(batchModel, bateladaModel, position);
	}
}
