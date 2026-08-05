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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.LevelSensorModel;
import si.dbcomm.model.LevelSensorModel_;

/**
 * @author marcos
 *
 */
public class LevelSensorDao extends DaoParent implements DaoInterface<LevelSensorModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public LevelSensorModel persist(LevelSensorModel entity) throws CrudDatabaseException {
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
	public LevelSensorModel update(LevelSensorModel entity) throws CrudDatabaseException {
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
	public LevelSensorModel delete(LevelSensorModel entity) throws CrudDatabaseException {
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
	public LevelSensorModel read(LevelSensorModel entity) {
		openCurrentSessionwithTransaction();
		LevelSensorModel levelSensor = persistenceSession.get(LevelSensorModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return levelSensor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public LevelSensorModel findById(Long id) {
		openCurrentSessionwithTransaction();
		LevelSensorModel levelSensor = persistenceSession.get(LevelSensorModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return levelSensor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<LevelSensorModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<LevelSensorModel> query = persistenceSession.createQuery("FROM LevelSensorModel");
		List<LevelSensorModel> sensors = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return sensors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public LevelSensorModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		LevelSensorModel temp = new LevelSensorModel();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<LevelSensorModel> criteriaQuery = cb.createQuery(LevelSensorModel.class);
		Root<LevelSensorModel> from = criteriaQuery.from(LevelSensorModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(LevelSensorModel_.tag), tag));
		temp = persistenceSession.createQuery(criteriaQuery).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return temp;
	}
}
