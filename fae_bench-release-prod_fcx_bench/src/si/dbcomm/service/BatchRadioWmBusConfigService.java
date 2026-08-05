//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BatchRadioWmBusConfigService.java
*    @author Marcos
*    @date 15 de mar de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.BatchRadioWmBusConfigDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchRadioWmBusConfigModel;

/**
 * @author Marcos
 *
 */
public class BatchRadioWmBusConfigService implements ServiceInterface<BatchRadioWmBusConfigModel, Long, String> {

	private static BatchRadioWmBusConfigDao batchRadioConfigDao = new BatchRadioWmBusConfigDao();

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public BatchRadioWmBusConfigModel persist(BatchRadioWmBusConfigModel entity) throws CrudDatabaseException {

		int startSerialNewBatch = entity.getStartSerialSeq();
		int lastSerialNewBatch = startSerialNewBatch + entity.getBatch().getNumMeters();

		// Check serial overlay
		List<BatchRadioWmBusConfigModel> listBatch = batchRadioConfigDao.findAll();
		for (BatchRadioWmBusConfigModel oldBatch : listBatch) {
			//
			int startSerialOld = oldBatch.getStartSerialSeq();
			int lastSerialold = startSerialOld + oldBatch.getBatch().getNumMeters();

			if (!((startSerialNewBatch >= lastSerialold) || (lastSerialNewBatch < startSerialOld))) {
				throw new CrudDatabaseException("BatchRadioWmBusConfig with overlapping existing serial numbers");
			}
		}

		return batchRadioConfigDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public BatchRadioWmBusConfigModel update(BatchRadioWmBusConfigModel entity) throws CrudDatabaseException {
		return batchRadioConfigDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public BatchRadioWmBusConfigModel delete(BatchRadioWmBusConfigModel entity) throws CrudDatabaseException {
		return batchRadioConfigDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public BatchRadioWmBusConfigModel read(BatchRadioWmBusConfigModel entity) {
		return batchRadioConfigDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public BatchRadioWmBusConfigModel findById(Long id) {
		return batchRadioConfigDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<BatchRadioWmBusConfigModel> findAll() {
		return batchRadioConfigDao.findAll();
	}

	/**
	 * Return the object BatchRadioWmBusConfigModel by the batchId number
	 * 
	 * @param id
	 * @return
	 */
	public BatchRadioWmBusConfigModel findByBatchId(Long id) {
		return batchRadioConfigDao.findByBatchId(id);
	}
}
