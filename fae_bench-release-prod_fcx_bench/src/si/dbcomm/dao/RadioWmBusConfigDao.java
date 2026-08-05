//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file RadioWmBusConfigDao.java
*    @author Marcos
*    @date 15 de mar de 2018
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
import si.dbcomm.model.MeterModel;
import si.dbcomm.model.MeterModel_;
import si.dbcomm.model.RadioWmBusConfigModel;
import si.dbcomm.model.RadioWmBusConfigModel_;

/**
 * @author Marcos
 *
 */
public class RadioWmBusConfigDao extends DaoParent implements DaoInterface<RadioWmBusConfigModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public RadioWmBusConfigModel persist(RadioWmBusConfigModel entity) throws CrudDatabaseException {
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
	public RadioWmBusConfigModel update(RadioWmBusConfigModel entity) throws CrudDatabaseException {
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
	public RadioWmBusConfigModel delete(RadioWmBusConfigModel entity) throws CrudDatabaseException {
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
	public RadioWmBusConfigModel read(RadioWmBusConfigModel entity) {
		openCurrentSessionwithTransaction();
		RadioWmBusConfigModel radioConfig = persistenceSession.get(RadioWmBusConfigModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return radioConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public RadioWmBusConfigModel findById(Long id) {
		openCurrentSessionwithTransaction();
		RadioWmBusConfigModel radioConfig = persistenceSession.get(RadioWmBusConfigModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return radioConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<RadioWmBusConfigModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<RadioWmBusConfigModel> query = persistenceSession.createQuery("FROM RadioWmBusConfigModel");
		List<RadioWmBusConfigModel> radioList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return radioList;
	}

	/**
	 * 
	 */
	public List<RadioWmBusConfigModel> findAllByMeterId(Long meterId) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<RadioWmBusConfigModel> criteriaQuery = cb.createQuery(RadioWmBusConfigModel.class);
		Root<RadioWmBusConfigModel> from = criteriaQuery.from(RadioWmBusConfigModel.class);
		criteriaQuery.select(from);
		criteriaQuery.where(cb.equal(from.get(RadioWmBusConfigModel_.meter), meterId));
		List<RadioWmBusConfigModel> list = persistenceSession.createQuery(criteriaQuery).getResultList();
		closeCurrentSessionReadwithTransaction();
		return list;
	}

}
