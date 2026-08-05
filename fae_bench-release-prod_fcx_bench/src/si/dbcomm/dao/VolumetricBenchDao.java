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

import java.util.List;

import org.hibernate.query.Query;

import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.VolumetricBenchModel;

/**
 * @author marcos
 *
 */
public class VolumetricBenchDao extends DaoParent implements DaoInterface<VolumetricBenchModel, Long, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#persist(java.lang.Object)
	 */
	@Override
	public VolumetricBenchModel persist(VolumetricBenchModel entity) throws CrudDatabaseException {
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
	public VolumetricBenchModel update(VolumetricBenchModel entity) throws CrudDatabaseException {
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
	public VolumetricBenchModel delete(VolumetricBenchModel entity) throws CrudDatabaseException {
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
	public VolumetricBenchModel read(VolumetricBenchModel entity) {
		openCurrentSessionwithTransaction();
		VolumetricBenchModel temp = persistenceSession.get(VolumetricBenchModel.class, entity.getId());
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findById(java.io.Serializable)
	 */
	@Override
	public VolumetricBenchModel findById(Long id) {
		openCurrentSessionwithTransaction();
		VolumetricBenchModel temp = persistenceSession.get(VolumetricBenchModel.class, id);
		closeCurrentSessionReadwithTransaction();
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.DaoInterface#findAll()
	 */
	@Override
	public List<VolumetricBenchModel> findAll() {
		openCurrentSessionwithTransaction();
		@SuppressWarnings("unchecked")
		Query<VolumetricBenchModel> query = persistenceSession.createQuery("FROM VolumetricBenchModel");
		List<VolumetricBenchModel> sensors = query.getResultList();
		closeCurrentSessionReadwithTransaction();
		return sensors;
	}
}
