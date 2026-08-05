//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file QaAssuredService.java
*    @author osmenio
*    @date 17 de out de 2018
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import si.dbcomm.dao.BatchDao;
import si.dbcomm.dao.QaAssuredDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.QaAssuredModel;

/**
 * @author osmenio
 *
 */
public class QaAssuredService implements ServiceInterface<QaAssuredModel, Long, String> {

	private static QaAssuredDao qaAssuredDao = new QaAssuredDao();

	private static BatchDao batchDao = new BatchDao();

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public QaAssuredModel persist(QaAssuredModel entity) throws CrudDatabaseException {
		return qaAssuredDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public QaAssuredModel update(QaAssuredModel entity) throws CrudDatabaseException {
		return qaAssuredDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public QaAssuredModel delete(QaAssuredModel entity) throws CrudDatabaseException {
		return qaAssuredDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public QaAssuredModel read(QaAssuredModel entity) {
		return qaAssuredDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public QaAssuredModel findById(Long id) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<QaAssuredModel> findAll() {
		return qaAssuredDao.findAll();
	}

	/*
	 * 
	 */
	public List<QaAssuredModel> findAllByBatch(BatchModel batch) {
		HashMap<Long, QaAssuredModel> hash = new HashMap<>();
		List<QaAssuredModel> list = qaAssuredDao.findAll();
		for (QaAssuredModel qaAssured : list) {
			BatchModel batchAux = batchDao.findByMeter(qaAssured.getMeter());
			if (batchAux.getId() == batch.getId()) {
				hash.put(qaAssured.getId(), qaAssured);
			}
		}

		return new ArrayList<QaAssuredModel>(hash.values());
	}
	
//	/*
//	 * 
//	 */
//	public List<QaAssuredModel> findAllByBatch(BatchModel batch) {
//		return qaAssuredDao.findAllByBatch(batch);
//	}

	/*
	 * 
	 */
	public List<BatchModel> findAllDistinctBatch() {
		HashMap<Long, BatchModel> hash = new HashMap<>();
		List<QaAssuredModel> list = qaAssuredDao.findAll();
		for (QaAssuredModel qaAssured : list) {
			BatchModel batch = batchDao.findByMeter(qaAssured.getMeter());

			hash.put(batch.getId(), batch);
		}
		return new ArrayList<BatchModel>(hash.values());
	}
}
