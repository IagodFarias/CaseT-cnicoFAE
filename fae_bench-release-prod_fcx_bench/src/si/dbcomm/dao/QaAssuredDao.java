//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file BatchDataDao.java
*    @author marcos
*    @date 19 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.QaAssuredModel;

/**
 * @author marcos
 *
 */
public class QaAssuredDao extends DaoParent implements DaoInterface<QaAssuredModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public QaAssuredModel persist(QaAssuredModel entity) throws CrudDatabaseException {
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
	public QaAssuredModel update(QaAssuredModel entity) throws CrudDatabaseException {
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
	public QaAssuredModel delete(QaAssuredModel entity) throws CrudDatabaseException {
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
	public QaAssuredModel read(QaAssuredModel entity) {
		openCurrentSessionwithTransaction();
		QaAssuredModel batch = persistenceSession.get(QaAssuredModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return batch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public QaAssuredModel findById(Long id) {
		openCurrentSessionwithTransaction();
		QaAssuredModel benchData = persistenceSession.get(QaAssuredModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return benchData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<QaAssuredModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<QaAssuredModel> query = persistenceSession.createQuery("FROM QaAssuredModel");
		List<QaAssuredModel> batchList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return batchList;
	}
	
	public List<QaAssuredModel> findAllByBatch(BatchModel batch) {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<QaAssuredModel> query = persistenceSession.createQuery("FROM QaAssuredModel");
		List<QaAssuredModel> batchList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return batchList;
	}
}
