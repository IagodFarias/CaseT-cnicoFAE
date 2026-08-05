//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PumpDao.java
*    @author marcos
*    @date 11 de jul de 2016
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
import si.dbcomm.model.PumpModel;
import si.dbcomm.model.PumpModel_;

/**
 * @author marcos
 *
 */
public class PumpDao extends DaoParent implements DaoInterface<PumpModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public PumpModel persist(PumpModel entity) throws CrudDatabaseException {
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
	public PumpModel update(PumpModel entity) throws CrudDatabaseException {
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
	public PumpModel delete(PumpModel entity) throws CrudDatabaseException {
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
	public PumpModel read(PumpModel entity) {
		openCurrentSessionwithTransaction();
		PumpModel pump = persistenceSession.get(PumpModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return pump;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public PumpModel findById(Long id) {
		openCurrentSessionwithTransaction();
		PumpModel pump = persistenceSession.get(PumpModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return pump;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<PumpModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<PumpModel> query = persistenceSession.createQuery("FROM PumpModel");
		List<PumpModel> pumps = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return pumps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public PumpModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		PumpModel pump = new PumpModel();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<PumpModel> criteriaQuery = cb.createQuery(PumpModel.class);
		Root<PumpModel> from = criteriaQuery.from(PumpModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(PumpModel_.tag), tag));
		pump = persistenceSession.createQuery(criteriaQuery).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return pump;
	}
}
