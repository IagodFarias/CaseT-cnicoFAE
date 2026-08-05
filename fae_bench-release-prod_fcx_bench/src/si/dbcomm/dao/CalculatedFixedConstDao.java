//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CalculatedFixedConstDao.java
*    @author Marcos
*    @date 6 de abr de 2018
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

import si.dbcomm.model.CalculatedFixedConstModel_;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CalculatedFixedConstModel;

/**
 * @author Marcos
 *
 */
public class CalculatedFixedConstDao extends DaoParent implements DaoInterface<CalculatedFixedConstModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public CalculatedFixedConstModel persist(CalculatedFixedConstModel entity) throws CrudDatabaseException {
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
	public CalculatedFixedConstModel update(CalculatedFixedConstModel entity) throws CrudDatabaseException {
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
	public CalculatedFixedConstModel delete(CalculatedFixedConstModel entity) throws CrudDatabaseException {
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
	public CalculatedFixedConstModel read(CalculatedFixedConstModel entity) {
		openCurrentSessionwithTransaction();
		CalculatedFixedConstModel calConst = persistenceSession.get(CalculatedFixedConstModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return calConst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public CalculatedFixedConstModel findById(Long id) {
		openCurrentSessionwithTransaction();
		CalculatedFixedConstModel calConst = persistenceSession.get(CalculatedFixedConstModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return calConst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<CalculatedFixedConstModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<CalculatedFixedConstModel> query = persistenceSession.createQuery("FROM CalculatedFixedConstModel");
		List<CalculatedFixedConstModel> calcConstList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return calcConstList;
	}

	/*
	 * 
	 */
	public List<CalculatedFixedConstModel> findByFlowRateLike(String tag) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();

		CriteriaQuery<CalculatedFixedConstModel> criteria = builder.createQuery(CalculatedFixedConstModel.class);
		Root<CalculatedFixedConstModel> root = criteria.from(CalculatedFixedConstModel.class);
		criteria.select(root);
		criteria.where(builder.like(root.get(CalculatedFixedConstModel_.flowrate).as(String.class), tag + "%"));
		List<CalculatedFixedConstModel> list = persistenceSession.createQuery(criteria).getResultList();

		closeCurrentSessionReadwithTransaction();
		return list;
	}
}
