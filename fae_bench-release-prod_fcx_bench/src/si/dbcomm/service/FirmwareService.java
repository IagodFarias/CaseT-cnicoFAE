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

import java.util.List;

import si.dbcomm.dao.FirmwareDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.FirmwareModel;

/**
 * @author marcos
 *
 */
public class FirmwareService implements ServiceInterface<FirmwareModel, Long, String> {

	private static FirmwareDao firmwareDao;

	public FirmwareService() {
		firmwareDao = new FirmwareDao();
	}

	@Override
	public FirmwareModel persist(FirmwareModel entity) throws CrudDatabaseException {
		return firmwareDao.persist(entity);
	}

	@Override
	public FirmwareModel update(FirmwareModel entity) throws CrudDatabaseException {
		return firmwareDao.update(entity);
	}

	@Override
	public FirmwareModel delete(FirmwareModel entity) throws CrudDatabaseException {
		return firmwareDao.delete(entity);
	}

	@Override
	public FirmwareModel read(FirmwareModel entity) {
		return firmwareDao.read(entity);
	}

	@Override
	public FirmwareModel findById(Long id) {
		return firmwareDao.findById(id);
	}

	@Override
	public List<FirmwareModel> findAll() {
		return firmwareDao.findAll();
	}

//	public FirmwareModel findByBatch(BatchModel batch) {
//		return firmwareDao.findByBatch(batch);
//	}
	//
	// public List<FirmwareModel> findAllByDeviceTypeAndStatus(DeviceTypeEnum type, boolean status) {
	// return firmwareDao.findAllByDeviceTypeAndStatus(type, status);
	// }
}
