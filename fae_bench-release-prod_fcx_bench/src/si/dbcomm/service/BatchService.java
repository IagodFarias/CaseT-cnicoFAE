//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BatchService.java
*    @author marcos
*    @date 19 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.text.SimpleDateFormat;
import java.util.List;

import si.dbcomm.dao.BatchDao;
import si.dbcomm.dao.BatchRadioWmBusConfigDao;
import si.dbcomm.dao.MeterTypeDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.BateladaModel;
import si.dbcomm.model.FirmwareModel;
import si.dbcomm.model.MeterModel;

/**
 * @author marcos
 *
 */
public class BatchService implements ServiceInterface<BatchModel, Long, String> {

	private static BatchDao batchDao;
	private static BatchRadioWmBusConfigDao batchRadioConfigDao = new BatchRadioWmBusConfigDao();
	private static MeterTypeDao meterTypeDao = new MeterTypeDao();

	private SimpleDateFormat manufacYearDateFormat = new SimpleDateFormat("yy");

	public BatchService() {
		batchDao = new BatchDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public BatchModel persist(BatchModel entity) throws CrudDatabaseException {
		return batchDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public BatchModel update(BatchModel entity) throws CrudDatabaseException {
		return batchDao.update(entity);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	public BatchModel merge(BatchModel entity) throws CrudDatabaseException {
		return batchDao.merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public BatchModel delete(BatchModel entity) throws CrudDatabaseException {
		if (entity.getLastAssignedSerialSeq() != -1) {
			throw new CrudDatabaseException("Lote já iniciado não pode ser deletado");
		}
		return batchDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public BatchModel read(BatchModel entity) {
		return batchDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public BatchModel findById(Long id) {
		return batchDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<BatchModel> findAll() {
		return batchDao.findAll();
	}

	public List<BatchModel> findAllActive() {
		return batchDao.findAllActive();
	}

	public BatchModel findByMeter(MeterModel meter) {
		return batchDao.findByMeter(meter);
	}

	/**
	 * Finds Batch by batch id
	 * 
	 * @return BatchModel - object
	 * @throws CrudDatabaseException
	 */
	public BatchModel findByBatchId(String batchId) {
		return batchDao.findByBatchId(batchId);
	}

	/**
	 * Last Serial number of the batch
	 * 
	 * @return BatchModel - object
	 * @throws CrudDatabaseException
	 */
	public String findLastSerialInBatch(String batchId) {
		return batchDao.findLastSerialInBatch(batchId);
	}

	public int getBatchRunCount(String batchId) {
		return batchDao.getBatchRunCount(batchId);
	}

	public BateladaModel getBatchLastBatelada(String batchId) {
		return batchDao.getBatchLastBatelada(batchId);
	}
	
	public BateladaModel getBatchLastBatelada(BatchModel batch) {
		return batchDao.getBatchLastBatelada(batch);
	}
	
//	public FirmwareModel findFirmwareByBatch(BatchModel batch) {
//		return batchDao.findFirmwareByBatch(batch);
//	}
}
