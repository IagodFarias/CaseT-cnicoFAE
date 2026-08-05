//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ClientService.java
*    @author marcos
*    @date 19 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.ClientDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.ClientModel;

/**
 * @author marcos
 *
 */
public class ClientService implements ServiceInterface<ClientModel, Long, String> {
	private ClientDao clientDao;

	/**
	 * Creates a object for class ClientService.java
	 */
	public ClientService() {
		clientDao = new ClientDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public ClientModel persist(ClientModel entity) throws CrudDatabaseException {
		return clientDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public ClientModel update(ClientModel entity) throws CrudDatabaseException {
		return clientDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public ClientModel delete(ClientModel entity) throws CrudDatabaseException {
		return clientDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public ClientModel read(ClientModel entity) {
		return clientDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public ClientModel findById(Long id) {
		return clientDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<ClientModel> findAll() {
		return clientDao.findAll();
	}
}
