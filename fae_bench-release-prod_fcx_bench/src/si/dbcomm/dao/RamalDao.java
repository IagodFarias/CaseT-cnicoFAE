//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RamalDao.java
*    @author marcos
*    @date 18 de nov de 2016
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
import si.dbcomm.model.RamalModel;
import si.dbcomm.model.RamalModel_;

/**
 * @author marcos
 *
 */
public class RamalDao extends DaoParent implements DaoInterface<RamalModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public RamalModel persist(RamalModel entity) throws CrudDatabaseException {
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
	public RamalModel update(RamalModel entity) throws CrudDatabaseException {
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
	public RamalModel delete(RamalModel entity) throws CrudDatabaseException {
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
	public RamalModel read(RamalModel entity) {
		openCurrentSessionwithTransaction();
		RamalModel ramal = persistenceSession.get(RamalModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return ramal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public RamalModel findById(Long id) {
		openCurrentSessionwithTransaction();
		RamalModel ramal = persistenceSession.get(RamalModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return ramal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<RamalModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<RamalModel> query = persistenceSession.createQuery("FROM RamalModel");
		List<RamalModel> ramalList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return ramalList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public RamalModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		RamalModel ramal;
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<RamalModel> criteriaQuery = cb.createQuery(RamalModel.class);
		Root<RamalModel> from = criteriaQuery.from(RamalModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(RamalModel_.tag), tag));
		ramal = persistenceSession.createQuery(criteriaQuery).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return ramal;
	}

}
