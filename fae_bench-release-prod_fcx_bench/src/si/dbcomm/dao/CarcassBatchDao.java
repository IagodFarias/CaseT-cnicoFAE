//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file TempSensorDao.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CarcassBatchModel;

/**
 * @author marcos
 *
 */
public class CarcassBatchDao extends DaoParent implements DaoInterface<CarcassBatchModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public CarcassBatchModel persist(CarcassBatchModel entity) throws CrudDatabaseException {
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
	public CarcassBatchModel update(CarcassBatchModel entity) throws CrudDatabaseException {
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
	public CarcassBatchModel delete(CarcassBatchModel entity) throws CrudDatabaseException {
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
	public CarcassBatchModel read(CarcassBatchModel entity) {
		openCurrentSessionwithTransaction();
		CarcassBatchModel temp = persistenceSession.get(CarcassBatchModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public CarcassBatchModel findById(Long id) {
		openCurrentSessionwithTransaction();
		CarcassBatchModel temp = persistenceSession.get(CarcassBatchModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<CarcassBatchModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<CarcassBatchModel> query = persistenceSession.createQuery("FROM CarcassBatchModel");
		List<CarcassBatchModel> sensors = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return sensors;
	}
}
