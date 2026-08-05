//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BenchDataService.java
*    @author marcos
*    @date 19 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.BenchDataDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BenchDataModel;

/**
 * @author marcos
 *
 */
public class BenchDataService implements ServiceInterface<BenchDataModel, Long, String> {
	private static BenchDataDao benchDataDao;

	/**
	 * Creates a object for class BenchDataService.java
	 */
	public BenchDataService() {
		benchDataDao = new BenchDataDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public BenchDataModel persist(BenchDataModel entity) throws CrudDatabaseException {
		return benchDataDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public BenchDataModel update(BenchDataModel entity) throws CrudDatabaseException {
		return benchDataDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public BenchDataModel delete(BenchDataModel entity) throws CrudDatabaseException {
		return benchDataDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public BenchDataModel read(BenchDataModel entity) {
		return benchDataDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.io.Serializable)
	 */
	@Override
	public BenchDataModel findById(Long id) {
		return benchDataDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<BenchDataModel> findAll() {
		return benchDataDao.findAll();
	}

	// public List<BenchDataModel> findAllByProcess(ProcessModel process) {
	// return benchDataDao.findAllByProcess(process);
	// }
}
