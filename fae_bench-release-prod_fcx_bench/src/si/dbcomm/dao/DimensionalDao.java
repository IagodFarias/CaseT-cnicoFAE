//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file TempSensorDao.java
*    @author marcos
*    @date 12 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.CarcassBatchModel;
import si.dbcomm.model.CarcassBatchModel_;
import si.dbcomm.model.DimensionalModel;
import si.dbcomm.model.DimensionalModel_;

/**
 * @author marcos
 *
 */
public class DimensionalDao extends DaoParent implements DaoInterface<DimensionalModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public DimensionalModel persist(DimensionalModel entity) throws CrudDatabaseException {
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
	public DimensionalModel update(DimensionalModel entity) throws CrudDatabaseException {
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
	public DimensionalModel delete(DimensionalModel entity) throws CrudDatabaseException {
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
	public DimensionalModel read(DimensionalModel entity) {
		openCurrentSessionwithTransaction();
		DimensionalModel temp = persistenceSession.get(DimensionalModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public DimensionalModel findById(Long id) {
		openCurrentSessionwithTransaction();
		DimensionalModel temp = persistenceSession.get(DimensionalModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * 
	 */
	public DimensionalModel findByDescription(String description) {
		openCurrentSessionwithTransaction();
		DimensionalModel temp = null;
		CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<DimensionalModel> criteriaQuery = cb.createQuery(DimensionalModel.class);
		Root<DimensionalModel> from = criteriaQuery.from(DimensionalModel.class);
		// criteriaQuery.select(from).orderBy(cb.desc(from.get(DimensionalModel_.dateOfCalibration)));
		criteriaQuery.where(cb.equal(from.get(DimensionalModel_.description), description));
		List<DimensionalModel> list = persistenceSession.createQuery(criteriaQuery).getResultList();
		if (!list.isEmpty()) {
			temp = list.get(0);
		}
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * 
	 */
	public DimensionalModel findByCarcassBatch(CarcassBatchModel carcassBatch) {
		openCurrentSessionwithTransaction();

		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<DimensionalModel> criteria = builder.createQuery(DimensionalModel.class);

		Root<DimensionalModel> root = criteria.from(DimensionalModel.class);
		Join<DimensionalModel, CarcassBatchModel> joinCarcass = root.join(DimensionalModel_.carcassBatch, JoinType.INNER);
		criteria.select(root);

		criteria.where(builder.equal(joinCarcass.get(CarcassBatchModel_.id), carcassBatch.getId()));
		List<DimensionalModel> dimensionalList = null;
		DimensionalModel dimensionalModel = null;

		dimensionalList = persistenceSession.createQuery(criteria).getResultList();
		if (!dimensionalList.isEmpty()) {
			dimensionalModel = dimensionalList.get(0);
		}

		closeCurrentSessionReadwithTransaction();
		return dimensionalModel;
	}

	/*
	 * 
	 */
	public List<DimensionalModel> findAllByFilter(DimensionalModel dimensional) {
		openCurrentSessionwithTransaction();

		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<DimensionalModel> criteria = builder.createQuery(DimensionalModel.class);

		Root<DimensionalModel> root = criteria.from(DimensionalModel.class);
		// Join<DimensionalModel, CarcassBatchModel> joinCarcass = root.join(DimensionalModel_.carcassBatch, JoinType.INNER);
		CriteriaQuery<DimensionalModel> select = criteria.select(root);

		//
		ArrayList<Predicate> predList = new ArrayList<Predicate>();

		predList.add(builder.like(builder.lower(root.get(DimensionalModel_.description).as(String.class)), "%" + dimensional.getDescription().toLowerCase() + "%"));
		// Date
		if (dimensional.getDateOfSampling() != null) {
			// Volume
			predList.add(builder.greaterThanOrEqualTo(root.get(DimensionalModel_.dateOfSampling), dimensional.getDateOfSampling()));
			predList.add(builder.lessThanOrEqualTo(root.get(DimensionalModel_.dateOfSampling), addDays(dimensional.getDateOfSampling(), 1)));
		}
		//
		Predicate[] predArray = new Predicate[predList.size()];
		criteria.where(predList.toArray(predArray));
		TypedQuery<DimensionalModel> typedQuery = persistenceSession.createQuery(select);
		List<DimensionalModel> list = typedQuery.getResultList();

		closeCurrentSessionReadwithTransaction();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<DimensionalModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<DimensionalModel> query = persistenceSession.createQuery("FROM DimensionalModel");
		List<DimensionalModel> sensors = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return sensors;
	}

	/*
	 * 
	 */
	private Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}
}
