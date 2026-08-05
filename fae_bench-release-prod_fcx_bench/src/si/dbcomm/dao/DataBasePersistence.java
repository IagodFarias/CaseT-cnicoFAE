//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ConectionHibernate.java
*    @author marcos
*    @date 30 de jun de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package si.dbcomm.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.File;




/**
 * @author marcos
 *
 */
public class DataBasePersistence {

//	private static final SessionFactory sessionFactory;
	private static SessionFactory sessionFactory;
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	
//	static {
//		try {
//			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//		} catch (Throwable e) {
//			System.err.println("Initial SessionFactory creation failed" + e);
//			throw new ExceptionInInitializerError(e);
//		}
//	}

	public static boolean initialize(String url, String username, String password){
		boolean sucessfull = true;
		
		// Check if URL is empty
		if (url == null || url.trim().isEmpty()) {
			System.err.println("Database URL cannot be empty");
			return false;
		}
		
		try {
			// Go back to a simpler approach using direct JDBC
			System.out.println("Initializing database connection using direct JDBC...");
			
			// Create a simple connection pool
			org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl connectionProvider = 
				new org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl();
			
			java.util.Properties props = new java.util.Properties();
			props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
			props.put("hibernate.connection.url", "jdbc:postgresql://" + url);
			props.put("hibernate.connection.username", username);
			props.put("hibernate.connection.password", password);
			props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
			props.put("hibernate.connection.pool_size", "5");
			
			// Disable proxies completely
			props.put("hibernate.bytecode.use_reflection_optimizer", "false");
			props.put("hibernate.cglib.use_reflection_optimizer", "false");
			
			// Create Configuration
			org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
			configuration.setProperties(props);
			
			// Add entity classes
			configuration.addAnnotatedClass(si.dbcomm.model.ValveModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.BatchModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.BatchRadioWmBusConfigModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.BateladaModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.BenchDataModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.CalculatedFixedConstModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.CalibConstantsModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.CarcassBatchModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.ClientModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.ConversorModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.DimensionalModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.DimensionMeasureModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.DrawingModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.FirmwareModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.FlowRateModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.HumiditySensorModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.LevelSensorModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.MeterDataModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.MeterModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.MeterTypeModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.PidConfigModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.PressureSensorModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.ProcessConfigModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.PumpModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.QaAssuredModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.QuotaModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.RadioWmBusConfigModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.RamalModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.RefMeterModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.ScaleModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.TempSensorModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.VerificationErrorModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.VolumeModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.VolumetricBenchModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.VolumetricErrorModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.WaterLineModel.class);
			configuration.addAnnotatedClass(si.dbcomm.model.LogChangeProcessBatchModel.class);
			
			// Build SessionFactory with enhanced options
			org.hibernate.boot.registry.StandardServiceRegistryBuilder serviceRegistryBuilder = 
				new org.hibernate.boot.registry.StandardServiceRegistryBuilder();
			serviceRegistryBuilder.applySettings(configuration.getProperties());
			
			org.hibernate.service.ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			
			// Print success message for debugging
			System.out.println("Hibernate SessionFactory created successfully!");
		} catch (Throwable e) {
			sucessfull = false;
			// Print more detailed error information
			System.err.println("Initial SessionFactory creation failed: " + e);
			e.printStackTrace();
			return sucessfull;
			//throw new ExceptionInInitializerError(e);
		}
		
		return sucessfull;
	}

	// retorna uma sessao de comunicacao com o BD
	public static Session getInstance() {
		Session session = (Session) threadLocal.get();
		session = sessionFactory.openSession();
		threadLocal.set(session);
		return session;
	}
}
