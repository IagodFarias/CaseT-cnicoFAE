//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file LevelSensorDao.java
*    @author marcos
*    @date 4 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.LogChangeBatchModel;

/**
 * @author marcos
 *
 */
public class LogChangeBatchDao extends DaoParent implements DaoInterface<LogChangeBatchModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public LogChangeBatchModel persist(LogChangeBatchModel entity) throws CrudDatabaseException {
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
	public LogChangeBatchModel update(LogChangeBatchModel entity) throws CrudDatabaseException {
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
	public LogChangeBatchModel delete(LogChangeBatchModel entity) throws CrudDatabaseException {
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
	public LogChangeBatchModel read(LogChangeBatchModel entity) {
		openCurrentSessionwithTransaction();
		LogChangeBatchModel result = persistenceSession.get(LogChangeBatchModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public LogChangeBatchModel findById(Long id) {
		openCurrentSessionwithTransaction();
		LogChangeBatchModel result = persistenceSession.get(LogChangeBatchModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<LogChangeBatchModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<LogChangeBatchModel> query = persistenceSession.createQuery("FROM LogChangeBatchModel");
		List<LogChangeBatchModel> result = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return result;
	}
}
