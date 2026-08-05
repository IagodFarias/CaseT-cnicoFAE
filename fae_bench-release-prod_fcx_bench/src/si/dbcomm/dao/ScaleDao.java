//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ScaleDao.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.ScaleModel;

/**
 * @author marcos
 *
 */
public class ScaleDao extends DaoParent implements DaoInterface<ScaleModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public ScaleModel persist(ScaleModel entity) throws CrudDatabaseException {
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
	public ScaleModel update(ScaleModel entity) throws CrudDatabaseException {
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
	public ScaleModel delete(ScaleModel entity) throws CrudDatabaseException {
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
	public ScaleModel read(ScaleModel entity) {
		openCurrentSessionwithTransaction();
		ScaleModel scale = persistenceSession.get(ScaleModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public ScaleModel findById(Long id) {
		openCurrentSessionwithTransaction();
		ScaleModel scale = persistenceSession.get(ScaleModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<ScaleModel> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public ScaleModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<ScaleModel> criteriaQuery = cb.createQuery(ScaleModel.class);
		Root<ScaleModel> from = criteriaQuery.from(ScaleModel.class);
		TypedQuery<ScaleModel> typedQuery = persistenceSession.createQuery(criteriaQuery.select(from).where(cb.equal(from.get("tag"), tag)));
		ScaleModel scale = typedQuery.getSingleResult();

		closeCurrentSessionReadwithTransaction();
		return scale;
	}
}
