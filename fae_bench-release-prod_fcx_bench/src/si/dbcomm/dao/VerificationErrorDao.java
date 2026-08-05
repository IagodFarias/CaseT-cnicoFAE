//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file VerificationErrorDao.java
*    @author marcos
*    @date 18 de mar de 2017
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
import si.dbcomm.model.VerificationErrorModel;
import si.dbcomm.model.VerificationErrorModel_;

/**
 * @author marcos
 *
 */
public class VerificationErrorDao extends DaoParent implements DaoInterface<VerificationErrorModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public VerificationErrorModel persist(VerificationErrorModel entity) throws CrudDatabaseException {
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
	public VerificationErrorModel update(VerificationErrorModel entity) throws CrudDatabaseException {
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
	public VerificationErrorModel delete(VerificationErrorModel entity) throws CrudDatabaseException {
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
	public VerificationErrorModel read(VerificationErrorModel entity) {
		openCurrentSessionwithTransaction();
		VerificationErrorModel verifError = persistenceSession.get(VerificationErrorModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return verifError;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public VerificationErrorModel findById(Long id) {
		openCurrentSessionwithTransaction();
		VerificationErrorModel verifError = persistenceSession.get(VerificationErrorModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return verifError;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<VerificationErrorModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<VerificationErrorModel> query = persistenceSession.createQuery("FROM VerificationErrorModel");
		List<VerificationErrorModel> errorList = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return errorList;
	}

	public List<VerificationErrorModel> findByMeter(MeterModel meter) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<VerificationErrorModel> criteriaQuery = builder.createQuery(VerificationErrorModel.class);
		Root<VerificationErrorModel> root = criteriaQuery.from(VerificationErrorModel.class);
		criteriaQuery.where(builder.equal(root.get(VerificationErrorModel_.meter), meter));
		List<VerificationErrorModel> retorno = persistenceSession.createQuery(criteriaQuery).getResultList();
		closeCurrentSessionReadwithTransaction();
		return retorno;
	}

}
