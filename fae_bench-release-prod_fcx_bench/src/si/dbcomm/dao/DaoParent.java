//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file DaoParent.java
*    @author marcos
*    @date 4 de jul de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import si.dbcomm.exceptions.CrudDatabaseException;

import java.util.List;

/**
 * @author marcos
 *
 */
public class DaoParent {
	Session persistenceSession;
	Transaction transaction = null;

	/**
	 * Opens an session with a transaction for database communication
	 */
	public Session openCurrentSessionwithTransaction() {
		try {
			persistenceSession = DataBasePersistence.getInstance();
			transaction = persistenceSession.beginTransaction();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return persistenceSession;
	}

	/**
	 * Closes a session with database
	 */
	public void closeCurrentSessionwithTransaction() throws CrudDatabaseException {
		try {
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}			throw new CrudDatabaseException(e.getMessage());
		} finally {
			persistenceSession.close();
		}
	}

	/**
	 * Closes a session with database
	 */
	public void closeCurrentSessionReadwithTransaction() {
		try {
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}			// throw new CrudDatabaseException(e.getMessage());
		} finally {
			persistenceSession.close();
		}
	}

	/**
	 * Check if an entity has an ID (not null)
	 * @param entity The entity to check
	 * @return true if the entity has an ID, false otherwise
	 */
	protected boolean hasId(Object entity) {
		try {
			// Try to use reflection to get the ID field
			java.lang.reflect.Method getIdMethod = entity.getClass().getMethod("getId");
			Object id = getIdMethod.invoke(entity);
			return id != null;
		} catch (Exception e) {
			// If we can't get the ID, assume it doesn't have one
			return false;
		}
	}

	/**
	 * Safely updates or persists an entity based on whether it has an ID.
	 * If the entity has an ID, it will be updated.
	 * If the entity doesn't have an ID, it will be persisted.
	 * 
	 * @param entity The entity to update or persist
	 * @return The entity with its ID set
	 */
	protected Object saveOrUpdate(Object entity) {
		try {
			// Try to use reflection to get the ID and setId methods
			java.lang.reflect.Method getIdMethod = entity.getClass().getMethod("getId");
			Object id = getIdMethod.invoke(entity);
			
			if (id == null) {
				// If the entity has no ID, try to save it
				try {
					// First check if a similar entity already exists
					// This is specifically for entities with unique constraints
					if (entity.getClass().getSimpleName().equals("ConversorModel")) {
						// For ConversorModel, check if a record with same name exists
						java.lang.reflect.Method getNameMethod = entity.getClass().getMethod("getName");
						Object name = getNameMethod.invoke(entity);
						
						if (name != null) {
							String query = "FROM " + entity.getClass().getSimpleName() + " WHERE name = :name";
							@SuppressWarnings("unchecked")
							org.hibernate.query.Query<?> hibernateQuery = persistenceSession.createQuery(query);
							hibernateQuery.setParameter("name", name);
							
							List<?> results = hibernateQuery.getResultList();
							if (!results.isEmpty()) {
								// Found an existing entity with same name, use that instead
								System.out.println("Found existing entity with same name during saveOrUpdate");
								return results.get(0);
							}
						}
					}
					
					// No existing entity found, try to save the new one
					id = persistenceSession.save(entity);
					
					// Set the ID on the entity using reflection
					Class<?> idClass = id.getClass();
					java.lang.reflect.Method setIdMethod = entity.getClass().getMethod("setId", idClass);
					setIdMethod.invoke(entity, id);
				} catch (Exception ex) {
					// If save fails (e.g., constraint violation), try to find an existing entity
					if (ex.toString().contains("ConstraintViolationException")) {
						System.out.println("Constraint violation during save. Entity might already exist.");
						persistenceSession.clear();  // Clear session to avoid stale data
						
						// Try a more general approach to find an existing entity
						if (entity.getClass().getSimpleName().equals("ConversorModel")) {
							String query = "FROM " + entity.getClass().getSimpleName();
							List<?> results = persistenceSession.createQuery(query).getResultList();
							
							// For ConversorModel, check for matching name
							java.lang.reflect.Method getNameMethod = entity.getClass().getMethod("getName");
							Object entityName = getNameMethod.invoke(entity);
							
							for (Object result : results) {
								Object resultName = getNameMethod.invoke(result);
								if (entityName.equals(resultName)) {
									System.out.println("Found entity by name after constraint violation");
									return result;
								}
							}
						}
						
						// If we can't find an existing entity, rethrow the exception
						throw new RuntimeException("Failed to save entity and couldn't find existing one: " + ex.getMessage(), ex);
					}
					throw new RuntimeException("Failed to save entity: " + ex.getMessage(), ex);
				}
			} else {
				// If the entity has an ID, update it
				try {
					persistenceSession.update(entity);
				} catch (Exception ex) {
					System.err.println("Error during update: " + ex.getMessage());
					// If update fails, try merge instead
					entity = persistenceSession.merge(entity);
				}
			}
			
			return entity;
		} catch (Exception e) {
			throw new RuntimeException("Failed to save or update entity: " + e.getMessage(), e);
		}
	}
}
