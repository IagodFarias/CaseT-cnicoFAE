//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ConversorService.java
*    @author marcos
*    @date 13 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.service;

import java.util.List;

import si.dbcomm.dao.ConversorDao;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.ConversorModel;
import si.dbcomm.util.ConversorNumbers;

/**
 * @author marcos
 *
 */
public class ConversorService implements ServiceInterface<ConversorModel, Long, String> {
	private ConversorDao conversorDao;

	/**
	 * Creates a object for class ConversorService.java
	 */
	public ConversorService() {
		conversorDao = new ConversorDao();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#persist(java.lang.Object)
	 */
	@Override
	public ConversorModel persist(ConversorModel entity) throws CrudDatabaseException {
		return conversorDao.persist(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#update(java.lang.Object)
	 */
	@Override
	public ConversorModel update(ConversorModel entity) throws CrudDatabaseException {
		return conversorDao.update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#delete(java.lang.Object)
	 */
	@Override
	public ConversorModel delete(ConversorModel entity) throws CrudDatabaseException {
		return conversorDao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#read(java.lang.Object)
	 */
	@Override
	public ConversorModel read(ConversorModel entity) {
		return conversorDao.read(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findById(java.lang.Object)
	 */
	@Override
	public ConversorModel findById(Long id) {
		return conversorDao.findById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.ServiceInterface#findAll()
	 */
	@Override
	public List<ConversorModel> findAll() {
		return conversorDao.findAll();
	}

	public ConversorModel findByName(ConversorNumbers conversorNumber) {
		try {
			// Convert enum to String if needed
			String name = conversorNumber.toString(); // or conversorNumber.getName() if it has such method
			
			// First try to find by name using the DAO
			ConversorModel conversor = conversorDao.findByName(name);
			
			// If not found by name, search using a direct query to make sure it really doesn't exist
			if (conversor == null) {
				// Try to find all converters and search manually
				List<ConversorModel> allConversors = findAll();
				for (ConversorModel existing : allConversors) {
					if (existing.getName() == conversorNumber) {
						System.out.println("Found conversor by manually checking all: " + conversorNumber);
						return existing;
					}
				}
				
				// If we still can't find it, create a new one
				System.out.println("Creating default converter for: " + name);
				conversor = new ConversorModel();
				conversor.setName(conversorNumber);
				conversor.setIp("127.0.0.1"); // Default IP
				conversor.setPort(8888); // Default port
				conversor.setColorCode("#808080"); // Default color - medium gray
				conversor.setEnabled(false); // Default to disabled
				
				// Persist the default converter to ensure it has an ID
				try {
					conversor = persist(conversor);
				} catch (CrudDatabaseException e) {
					// If we get a constraint violation, it means the entity already exists
					// but we couldn't find it for some reason. Try once more with findAll
					if (e.getMessage().contains("ConstraintViolationException")) {
						System.out.println("Constraint violation. Trying one more time to find existing conversor.");
						// Clear session and try again to find it
						List<ConversorModel> refreshed = findAll();
						for (ConversorModel existing : refreshed) {
							if (existing.getName() == conversorNumber) {
								System.out.println("Found conversor after constraint error: " + conversorNumber);
								return existing;
							}
						}
					}
					System.err.println("Failed to persist default conversor: " + e.getMessage());
				}
			}
			
			return conversor;
		} catch (Exception e) {
			System.err.println("Warning: Failed to find conversor: " + conversorNumber + ". Using default. Error: " + e.getMessage());
			
			// Create a default converter instead of throwing an exception
			ConversorModel defaultConversor = new ConversorModel();
			defaultConversor.setName(conversorNumber);
			defaultConversor.setIp("127.0.0.1"); // Default IP
			defaultConversor.setPort(8888); // Default port
			defaultConversor.setColorCode("#808080"); // Default color - medium gray
			defaultConversor.setEnabled(false); // Default to disabled
			
			// Don't try to persist the default converter here, since we're already in an error state
			// Just return it as a transient object, and let the saveOrUpdate method handle it
			
			return defaultConversor;
		}
	}

	public ConversorModel findByIp(String ip) {
		return conversorDao.findByIp(ip);
	}
}
