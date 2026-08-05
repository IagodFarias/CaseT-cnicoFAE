//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file CalibConstantsDao.java
*    @author marcos
*    @date 27 de dez de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CalibConstantsModel;
import si.dbcomm.model.CalibConstantsModel_;
import si.dbcomm.model.MeterModel;

/**
 * @author marcos
 *
 */
public class CalibConstantsDao extends DaoParent implements DaoInterface<CalibConstantsModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public CalibConstantsModel persist(CalibConstantsModel entity) throws CrudDatabaseException {
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
	public CalibConstantsModel update(CalibConstantsModel entity) throws CrudDatabaseException {
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
	public CalibConstantsModel delete(CalibConstantsModel entity) throws CrudDatabaseException {
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
	public CalibConstantsModel read(CalibConstantsModel entity) {
		openCurrentSessionwithTransaction();
		CalibConstantsModel calibConst = persistenceSession.get(CalibConstantsModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return calibConst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public CalibConstantsModel findById(Long id) {
		openCurrentSessionwithTransaction();
		CalibConstantsModel calibConstData = persistenceSession.get(CalibConstantsModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return calibConstData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<CalibConstantsModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<CalibConstantsModel> query = persistenceSession.createQuery("FROM CalibConstantsModel");
		List<CalibConstantsModel> CalibConstantsModelList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return CalibConstantsModelList;
	}

	/**
	 * Finds the meters constants
	 * 
	 * @param meter
	 * @return
	 * @throws CrudDatabaseException
	 */
	public List<CalibConstantsModel> findConstantsByMeter(MeterModel meter) {
		openCurrentSessionwithTransaction();
		List<CalibConstantsModel> retornoList = new ArrayList<>();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<CalibConstantsModel> criteriaQuery = builder.createQuery(CalibConstantsModel.class);
		Root<CalibConstantsModel> root = criteriaQuery.from(CalibConstantsModel.class);
		criteriaQuery.where(builder.equal(root.get(CalibConstantsModel_.meter), meter.getId()));
		retornoList = persistenceSession.createQuery(criteriaQuery).getResultList();
		closeCurrentSessionReadwithTransaction();
		return retornoList;
	}

}
