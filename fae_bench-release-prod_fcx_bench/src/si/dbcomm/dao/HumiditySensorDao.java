//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file HumiditySensorDao.java
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
import si.dbcomm.model.HumiditySensorModel;
import si.dbcomm.model.HumiditySensorModel_;

/**
 * @author marcos
 *
 */
public class HumiditySensorDao extends DaoParent implements DaoInterface<HumiditySensorModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public HumiditySensorModel persist(HumiditySensorModel entity) throws CrudDatabaseException {
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
	public HumiditySensorModel update(HumiditySensorModel entity) throws CrudDatabaseException {
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
	public HumiditySensorModel delete(HumiditySensorModel entity) throws CrudDatabaseException {
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
	public HumiditySensorModel read(HumiditySensorModel entity) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public HumiditySensorModel findById(Long id) {
		openCurrentSessionwithTransaction();
		HumiditySensorModel humiditySensor = persistenceSession.get(HumiditySensorModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return humiditySensor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<HumiditySensorModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<HumiditySensorModel> query = persistenceSession.createQuery("FROM HumiditySensorModel");
		List<HumiditySensorModel> sensors = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return sensors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public HumiditySensorModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		HumiditySensorModel sensor = new HumiditySensorModel();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<HumiditySensorModel> criteriaQuery = cb.createQuery(HumiditySensorModel.class);
		Root<HumiditySensorModel> from = criteriaQuery.from(HumiditySensorModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(HumiditySensorModel_.tag), tag));
		sensor = persistenceSession.createQuery(criteriaQuery).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return sensor;
	}
}
