//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ClientDao.java
*    @author marcos
*    @date 19 de set de 2016
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
import si.dbcomm.model.BatchModel;
import si.dbcomm.model.FirmwareModel;
import si.dbcomm.model.FirmwareModel_;

/**
 * @author marcos
 *
 */
public class FirmwareDao extends DaoParent implements DaoInterface<FirmwareModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public FirmwareModel persist(FirmwareModel entity) throws CrudDatabaseException {
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
	public FirmwareModel update(FirmwareModel entity) throws CrudDatabaseException {
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
	public FirmwareModel delete(FirmwareModel entity) throws CrudDatabaseException {
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
	public FirmwareModel read(FirmwareModel entity) {
		openCurrentSessionwithTransaction();
		FirmwareModel result = persistenceSession.get(
			FirmwareModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.lang.Object)
	 */
	@Override
	public FirmwareModel findById(Long id) {
		openCurrentSessionwithTransaction();
		FirmwareModel entity = persistenceSession.get(FirmwareModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<FirmwareModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<FirmwareModel> query = persistenceSession.createQuery("FROM FirmwareModel");
		List<FirmwareModel> list = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return list;
	}

//	public FirmwareModel findByBatch(BatchModel batch) {
//		openCurrentSessionwithTransaction();
//		CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
//		CriteriaQuery<FirmwareModel> criteria = builder.createQuery(FirmwareModel.class);
//		Root<FirmwareModel> root = criteria.from(FirmwareModel.class);
//		criteria.select(root);
//		criteria.where(builder.equal(root.get(FirmwareModel_.id), batch.getFirmware()));
//		List<FirmwareModel> list = persistenceSession.createQuery(criteria).getResultList();
//		closeCurrentSessionReadwithTransaction();
//		return list;
//	}

	// public List<FirmwareModel> findAllByDeviceTypeAndStatus(DeviceTypeEnum type, boolean status) {
	// openCurrentSessionwithTransaction();
	// CriteriaBuilder builder = persistenceSession.getCriteriaBuilder();
	// CriteriaQuery<FirmwareModel> criteria = builder.createQuery(FirmwareModel.class);
	// Root<FirmwareModel> root = criteria.from(FirmwareModel.class);
	// criteria.select(root);
	// criteria.where(builder.equal(root.get(FirmwareModel_.deviceType), type), builder.equal(root.get(FirmwareModel_.isActive), status));
	// List<FirmwareModel> list = persistenceSession.createQuery(criteria).getResultList();
	// closeCurrentSessionReadwithTransaction();
	// return list;
	// }
}
