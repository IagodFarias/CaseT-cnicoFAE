//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ValveDao.java
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
import si.dbcomm.model.ValveModel;
import si.dbcomm.model.ValveModel_;

/**
 * @author marcos
 *
 */
public class ValveDao extends DaoParent implements DaoInterface<ValveModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public ValveModel persist(ValveModel entity) throws CrudDatabaseException {
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
	public ValveModel update(ValveModel entity) throws CrudDatabaseException {
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
	public ValveModel delete(ValveModel entity) throws CrudDatabaseException {
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
	public ValveModel read(ValveModel entity) {
		openCurrentSessionwithTransaction();
		ValveModel valve = persistenceSession.get(ValveModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return valve;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public ValveModel findById(Long id) {
		openCurrentSessionwithTransaction();
		ValveModel valve = persistenceSession.get(ValveModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return valve;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<ValveModel> findAll() throws CrudDatabaseException {
		try {
			openCurrentSessionwithTransaction();
			if (persistenceSession == null) {
				throw new CrudDatabaseException("Database session is null");
			}
			
			@SuppressWarnings("unchecked")
			Query<ValveModel> query = persistenceSession.createQuery("FROM ValveModel");
			List<ValveModel> valveList = query.getResultList();
			closeCurrentSessionReadwithTransaction();
			return valveList;
		} catch (CrudDatabaseException e) {
			// You may want to log the error here
			throw e; // Re-throw to let callers know about the database problem
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public ValveModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<ValveModel> criteriaQuery = cb.createQuery(ValveModel.class);
		Root<ValveModel> from = criteriaQuery.from(ValveModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(ValveModel_.tag), tag));
		ValveModel temp = persistenceSession.createQuery(criteriaQuery).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

}
