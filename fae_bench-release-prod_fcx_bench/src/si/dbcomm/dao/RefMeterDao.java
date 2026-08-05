//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RefMeterDao.java
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
import si.dbcomm.model.RefMeterModel;
import si.dbcomm.model.RefMeterModel_;

/**
 * @author marcos
 *
 */
public class RefMeterDao extends DaoParent implements DaoInterface<RefMeterModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public RefMeterModel persist(RefMeterModel entity) throws CrudDatabaseException {
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
	public RefMeterModel update(RefMeterModel entity) throws CrudDatabaseException {
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
	public RefMeterModel delete(RefMeterModel entity) throws CrudDatabaseException {
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
	public RefMeterModel read(RefMeterModel entity) {
		openCurrentSessionwithTransaction();
		RefMeterModel refMeter = persistenceSession.get(RefMeterModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return refMeter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public RefMeterModel findById(Long id) {
		openCurrentSessionwithTransaction();
		RefMeterModel refMeter = persistenceSession.get(RefMeterModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return refMeter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<RefMeterModel> findAll() {
		try {
			openCurrentSessionwithTransaction();
			if (persistenceSession == null) {
				throw new CrudDatabaseException("Database session is null");
			}
			
			@SuppressWarnings("unchecked")
			Query<RefMeterModel> query = persistenceSession.createQuery("FROM RefMeterModel");
			List<RefMeterModel> meters = query.getResultList();
			closeCurrentSessionReadwithTransaction();
			return meters;
		} catch (Exception e) {
			throw new CrudDatabaseException("Failed to retrieve meter data", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public RefMeterModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		RefMeterModel meter = new RefMeterModel();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<RefMeterModel> criteriaQuery = cb.createQuery(RefMeterModel.class);
		Root<RefMeterModel> from = criteriaQuery.from(RefMeterModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(RefMeterModel_.tag), tag));
		meter = persistenceSession.createQuery(criteriaQuery).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return meter;
	}
}
