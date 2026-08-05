//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PressureSensorDao.java
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
import si.dbcomm.model.PressureSensorModel;
import si.dbcomm.model.PressureSensorModel_;

/**
 * @author marcos
 *
 */
public class PressureSensorDao extends DaoParent implements DaoInterface<PressureSensorModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public PressureSensorModel persist(PressureSensorModel entity) throws CrudDatabaseException {
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
	public PressureSensorModel update(PressureSensorModel entity) throws CrudDatabaseException {
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
	public PressureSensorModel delete(PressureSensorModel entity) throws CrudDatabaseException {
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
	public PressureSensorModel read(PressureSensorModel entity) {
		openCurrentSessionwithTransaction();
		PressureSensorModel pressSensor = persistenceSession.get(PressureSensorModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return pressSensor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public PressureSensorModel findById(Long id) {
		openCurrentSessionwithTransaction();
		PressureSensorModel pressSensor = persistenceSession.get(PressureSensorModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return pressSensor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<PressureSensorModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<PressureSensorModel> query = persistenceSession.createQuery("FROM PressSensorModel");
		List<PressureSensorModel> pressSensors = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return pressSensors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findByTag(java.io.Serializable)
	 */
	public PressureSensorModel findByTag(String tag) {
		openCurrentSessionwithTransaction();
		PressureSensorModel press = new PressureSensorModel();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<PressureSensorModel> criteriaQuery = cb.createQuery(PressureSensorModel.class);
		Root<PressureSensorModel> from = criteriaQuery.from(PressureSensorModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(PressureSensorModel_.tag), tag));
		press = persistenceSession.createQuery(criteriaQuery).getSingleResult();
		closeCurrentSessionReadwithTransaction();
		return press;
	}
}
