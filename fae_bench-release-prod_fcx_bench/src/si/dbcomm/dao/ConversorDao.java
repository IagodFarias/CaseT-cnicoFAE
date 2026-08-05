//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ConversorDao.java
*    @author marcos
*    @date 13 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.NoResultException;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.ConversorModel;
import si.dbcomm.model.ConversorModel_;
import si.dbcomm.util.ConversorNumbers;

/**
 * @author marcos
 *
 */
public class ConversorDao extends DaoParent implements DaoInterface<ConversorModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public ConversorModel persist(ConversorModel entity) throws CrudDatabaseException {
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
	public ConversorModel update(ConversorModel entity) throws CrudDatabaseException {
		openCurrentSessionwithTransaction();
		
		// Use the saveOrUpdate method which will handle null IDs properly
		saveOrUpdate(entity);
		
		closeCurrentSessionwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#delete(java.lang.Object)
	 */
	@Override
	public ConversorModel delete(ConversorModel entity) throws CrudDatabaseException {
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
	public ConversorModel read(ConversorModel entity) {
		openCurrentSessionwithTransaction();
		ConversorModel conversor = persistenceSession.get(ConversorModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return conversor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public ConversorModel findById(Long id) {
		openCurrentSessionwithTransaction();
		ConversorModel conversor = persistenceSession.get(ConversorModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return conversor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<ConversorModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<ConversorModel> query = persistenceSession.createQuery("FROM ConversorModel");
		List<ConversorModel> clients = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return clients;
	}

	public ConversorModel findByName(String name) {
		try {
			openCurrentSessionwithTransaction();
			if (persistenceSession == null) {
				throw new CrudDatabaseException("Database session is null");
			}
			
			// Try a direct JPQL query with parameter binding
			try {
				String jpql = "FROM ConversorModel WHERE name = :name";
				@SuppressWarnings("unchecked")
				org.hibernate.query.Query<ConversorModel> query = persistenceSession.createQuery(jpql);
				query.setParameter("name", ConversorNumbers.valueOf(name));
				
				List<ConversorModel> results = query.getResultList();
				if (!results.isEmpty()) {
					ConversorModel conversor = results.get(0);
					closeCurrentSessionReadwithTransaction();
					return conversor;
				}
			} catch (Exception e) {
				System.err.println("Error in direct JPQL query: " + e.getMessage());
			}
			
			// If direct query failed, try criteria query as fallback
			ConversorModel conversor = null;
			
			try {
				CriteriaBuilder cb = persistenceSession.getCriteriaBuilder();
				CriteriaQuery<ConversorModel> criteriaQuery = cb.createQuery(ConversorModel.class);
				Root<ConversorModel> from = criteriaQuery.from(ConversorModel.class);
				criteriaQuery.select(from);
				criteriaQuery.where(cb.equal(from.get(ConversorModel_.name), ConversorNumbers.valueOf(name)));
				
				List<ConversorModel> resultList = persistenceSession.createQuery(criteriaQuery).getResultList();
				if (!resultList.isEmpty()) {
					conversor = resultList.get(0);
				}
			} catch (NoResultException e) {
				// No result found, will return null
			} finally {
				closeCurrentSessionReadwithTransaction();
			}
			
			return conversor;
		} catch (Exception e) {
			throw new CrudDatabaseException("Failed to find conversor by name: " + name, e);
		}
	}

	public ConversorModel findByIp(String Ip) {
		openCurrentSessionwithTransaction();
		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
		CriteriaQuery<ConversorModel> criteria = builder.createQuery(ConversorModel.class);
		Root<ConversorModel> conversorRoot = criteria.from(ConversorModel.class);
		criteria.select(conversorRoot);
		criteria.where(builder.equal(conversorRoot.get(ConversorModel_.ip), Ip));
		List<ConversorModel> conversores = persistenceSession.createQuery(criteria).getResultList();
		ConversorModel retorno = null;
		if (!conversores.isEmpty()) {
			retorno = conversores.get(0);
		}
		closeCurrentSessionReadwithTransaction();
		return retorno;
	}

}
