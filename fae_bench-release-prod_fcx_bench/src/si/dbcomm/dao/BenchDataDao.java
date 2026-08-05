//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BenchDataDao.java
*    @author marcos
*    @date 18 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BenchDataModel;

/**
 * @author marcos
 *
 */
public class BenchDataDao extends DaoParent implements DaoInterface<BenchDataModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public BenchDataModel persist(BenchDataModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		entity.setId((long) persistenceSession.save(entity));
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#update(java.lang.Object)
	 */
	@Override
	public BenchDataModel update(BenchDataModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.update(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#delete(java.lang.Object)
	 */
	@Override
	public BenchDataModel delete(BenchDataModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		persistenceSession.delete(entity);
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#read(java.lang.Object)
	 */
	@Override
	public BenchDataModel read(BenchDataModel entity) {
		openCurrentSessionwithTransaction();
		BenchDataModel benchData = persistenceSession.get(BenchDataModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return benchData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public BenchDataModel findById(Long id) {
		openCurrentSessionwithTransaction();
		BenchDataModel benchData = persistenceSession.get(BenchDataModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return benchData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<BenchDataModel> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	// public List<BenchDataModel> findAllByProcess(ProcessModel process) {
	// openCurrentSessionwithTransaction();
	// CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
	// CriteriaQuery<BenchDataModel> criteria = builder.createQuery(BenchDataModel.class);
	// Root<BenchDataModel> root = criteria.from(BenchDataModel.class);
	// criteria.select(root);
	// criteria.where(builder.equal(root.get("process"), process.getId()));
	// criteria.select(root);
	// List<BenchDataModel> benchData = persistenceSession.createQuery(criteria).getResultList();
	// closeCurrentSessionReadwithTransaction();
	// return benchData;
	// }

}
