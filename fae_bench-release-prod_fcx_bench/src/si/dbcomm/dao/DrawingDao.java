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
import si.dbcomm.model.DrawingModel;

/**
 * @author marcos
 *
 */
public class DrawingDao extends DaoParent implements DaoInterface<DrawingModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public DrawingModel persist(DrawingModel entity) throws CrudDatabaseException {
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
	public DrawingModel update(DrawingModel entity) throws CrudDatabaseException {
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
	public DrawingModel delete(DrawingModel entity) throws CrudDatabaseException {
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
	public DrawingModel read(DrawingModel entity) {
		openCurrentSessionwithTransaction();
		DrawingModel temp = persistenceSession.get(DrawingModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public DrawingModel findById(Long id) {
		openCurrentSessionwithTransaction();
		DrawingModel temp = persistenceSession.get(DrawingModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<DrawingModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<DrawingModel> query = persistenceSession.createQuery("FROM DrawingModel");
		List<DrawingModel> sensors = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return sensors;
	}
}
