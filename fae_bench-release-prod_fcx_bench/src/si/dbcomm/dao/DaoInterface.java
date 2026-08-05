//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file DaoInterface.java
*    @author marcos
*    @date 1 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import java.io.Serializable;
import java.util.List;

import si.dbcomm.exceptions.CrudDatabaseException;

/**
 * @author marcos
 *
 */
public interface DaoInterface<T, Id, Tag extends Serializable> {

	/**
	 * Persists an object represented by object
	 * 
	 * @param entity
	 */
	public T persist(T entity) throws CrudDatabaseException;

	/**
	 * Updates an object represented by object
	 * 
	 * @param entity
	 */
	public T update(T entity) throws CrudDatabaseException;

	/**
	 * Deletes an object represented by object
	 * 
	 * @param entity
	 */
	public T delete(T entity) throws CrudDatabaseException;

	/**
	 * Reads an object represented by object
	 * 
	 * @param entity
	 * @return Complete object represented on the entity
	 */
	public T read(T entity);

	/**
	 * Find object by the id
	 * 
	 * @param id
	 * @return the selected object
	 */
	public T findById(Id id);

	/**
	 * retrieves all entities defined by object from database
	 * 
	 * @return
	 */
	public List<T> findAll() throws CrudDatabaseException;
	//
	// /**
	// * retrieves all entities defined by object from database
	// *
	// * @return
	// * @throws CrudDatabaseException
	// */
	// public T findByTag(Tag tag);

}
