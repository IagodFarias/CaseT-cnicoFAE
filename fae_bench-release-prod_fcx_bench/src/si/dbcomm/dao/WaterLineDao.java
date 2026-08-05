//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file WaterLineDao.java
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
import si.dbcomm.model.WaterLineModel;

/**
 * @author marcos
 *
 */
public class WaterLineDao extends DaoParent implements DaoInterface<WaterLineModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public WaterLineModel persist(WaterLineModel entity) throws CrudDatabaseException {
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
	public WaterLineModel update(WaterLineModel entity) throws CrudDatabaseException {
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
	public WaterLineModel delete(WaterLineModel entity) throws CrudDatabaseException {
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
	public WaterLineModel read(WaterLineModel entity) {
		openCurrentSessionwithTransaction();
		WaterLineModel line = persistenceSession.get(WaterLineModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return line;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public WaterLineModel findById(Long id) {
		openCurrentSessionwithTransaction();
		WaterLineModel line = persistenceSession.get(WaterLineModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return line;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<WaterLineModel> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.lang.Object)
	 */
	public WaterLineModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<WaterLineModel> criteriaQuery = cb.createQuery(WaterLineModel.class);
		Root<WaterLineModel> from = criteriaQuery.from(WaterLineModel.class);
		TypedQuery<WaterLineModel> typedQuery = persistenceSession.createQuery(criteriaQuery.select(from).where(cb.equal(from.get("tag"), tag)));
		WaterLineModel line = typedQuery.getSingleResult();

		closeCurrentSessionReadwithTransaction();
		return line;
	}
}
