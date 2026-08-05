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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.TempSensorModel;
import si.dbcomm.model.TempSensorModel_;

/**
 * @author marcos
 *
 */
public class TempSensorDao extends DaoParent implements DaoInterface<TempSensorModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public TempSensorModel persist(TempSensorModel entity) throws CrudDatabaseException {
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
	public TempSensorModel update(TempSensorModel entity) throws CrudDatabaseException {
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
	public TempSensorModel delete(TempSensorModel entity) throws CrudDatabaseException {
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
	public TempSensorModel read(TempSensorModel entity) {
		openCurrentSessionwithTransaction();
		TempSensorModel temp = persistenceSession.get(TempSensorModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public TempSensorModel findById(Long id) {
		openCurrentSessionwithTransaction();
		TempSensorModel temp = persistenceSession.get(TempSensorModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<TempSensorModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<TempSensorModel> query = persistenceSession.createQuery("FROM TempSensorModel");
		List<TempSensorModel> sensors = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return sensors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public TempSensorModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		TempSensorModel temp = new TempSensorModel();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<TempSensorModel> criteriaQuery = cb.createQuery(TempSensorModel.class);
		Root<TempSensorModel> from = criteriaQuery.from(TempSensorModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(TempSensorModel_.tag), tag));
		temp = persistenceSession.createQuery(criteriaQuery).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return temp;
	}
}
