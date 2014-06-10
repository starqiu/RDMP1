package com.dbs.sg.DTE12.common;

/**
 * Imports classes
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.NamingException;
/**
 * DB Connection Factory
 */
public class DBConnectionFactory {
	/**
	 * Logger
	 */
	private static Logger logger;

	/**
	 * Singleton instance
	 */
	private static DBConnectionFactory singleton = new DBConnectionFactory();
	
	/**
	 * LoadConfigXml
	 */
	private LoadConfigXml configXmlHelper;
	
	/**
	 * AESManager
	 */	
	private AESManager manager;
	/**
	 * Returns the Singleton instance.
	 */
	public static DBConnectionFactory singleton() {
		return singleton;
	}

	/**
	 * Gets an DB connection by resource reference.
	 */
	public Connection getConnection(String configPath) throws NamingException, SQLException {
		logger = Logger.getLogger(configPath, DBConnectionFactory.class);
		configXmlHelper = LoadConfigXml.getConfig(configPath);
		manager = new AESManager(configPath);
		Connection connection = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			logger.error("can not find db driver", e);
		}
		try {
			// setup connection
			String url = configXmlHelper.getConnectionstring();
			String username = configXmlHelper.getUserid();
			String password = manager.Descrypt(configXmlHelper.getKeyFile(),configXmlHelper.getEncryptFile());;
//			password = "password";
			//System.err.println("username is : "+username);
			//System.err.println("password is : "+password);
			connection = DriverManager.getConnection(url, username, password);
			//connection.setAutoCommit(false);
			logger.info("configPath:" + configPath +" db connect successfull.");
		} catch (SQLException e) {
			logger.error("", e);
			throw new SQLException(e.getMessage());
		}
		return connection;
	}
	
	/**
	 * Gets an DB connection by resource reference,and set AutoCommit to false
	 */
	public Connection getConnection(String configPath, boolean isAutoCommit) throws NamingException, SQLException {
		Connection connection = getConnection(configPath);
		connection.setAutoCommit(isAutoCommit);
		return connection;
	}
}
