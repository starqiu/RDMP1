package com.dbs.sg.DTE12.DAO;

/**
 * Imports classes
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import javax.naming.NamingException;

import com.dbs.sg.DTE12.common.DBConnectionFactory;
import com.dbs.sg.DTE12.common.Logger;

/**
 * Ancestor of Data Access objects
 */
public abstract class BaseTableDAO {
	/**
	 * Connection Created
	 */
	protected boolean connCreated = false;

	/**
	 * Database Connection
	 */
	protected Connection dbConnection = null;

	/**
	 * Prepared Statment
	 */
	protected PreparedStatement prepStatement = null;

	/**
	 * Result Set
	 */
	protected ResultSet resultSet = null;

	protected String configPath;
	/**
	 * Logger
	 */
	protected Logger logger;

	/**
	 * Creates object without parameter
	 */
	protected BaseTableDAO(String configPath) throws SQLException {
		logger = Logger.getLogger(configPath, this.getClass());
		try {
			setConnection(DBConnectionFactory.singleton().getConnection(
					configPath));
		} catch (NamingException namingException) {
			throw new SQLException(namingException.getMessage());
		}

		connCreated = true;
	}

	/**
	 * Creates object with parameter
	 */
	protected BaseTableDAO(String configPath, boolean isAutoCommit)
			throws SQLException {
		logger = Logger.getLogger(configPath, this.getClass());
		try {
			setConnection(DBConnectionFactory.singleton().getConnection(
					configPath, isAutoCommit));
		} catch (NamingException namingException) {
			throw new SQLException(namingException.getMessage());
		}

		connCreated = true;
	}

	/**
	 * Creates object.
	 */
	protected BaseTableDAO(Connection connection) throws SQLException {
		setConnection(connection);
	}

	/**
	 * Sets database connection.
	 */
	protected void setConnection(Connection connection) throws SQLException {
		if (connection == null) {
			throw new SQLException("Invalid connection.");
		}

		if (connection.isClosed()) {
			throw new SQLException("Connection already closed.");
		}

		dbConnection = connection;

		if (connection.getTransactionIsolation() == Connection.TRANSACTION_READ_COMMITTED) {
			logger.debug("Transaction Isolation is TRANSACTION_READ_COMMITTED "
					+ connection);
		} else if (connection.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED) {
			logger
					.debug("Transaction Isolation is TRANSACTION_READ_UNCOMMITTED "
							+ connection);
		} else if (connection.getTransactionIsolation() == Connection.TRANSACTION_REPEATABLE_READ) {
			logger
					.debug("Transaction Isolation is TRANSACTION_REPEATABLE_READ "
							+ connection);
		} else if (connection.getTransactionIsolation() == Connection.TRANSACTION_SERIALIZABLE) {
			logger.debug("Transaction Isolation is TRANSACTION_SERIALIZABLE "
					+ connection);
		}
	}

	/**
	 * Initializes before each DB access.
	 */
	public void initialize() {
		prepStatement = null;
		resultSet = null;
	}

	/**
	 * Cleans up after each DB access.
	 */
	public void cleanUp() {
		try {
			if (resultSet != null) {
				// while (resultSet.next())
				// {
				// }
				resultSet.close();
			}

			if (prepStatement != null) {
				// while (prepStatement.getMoreResults())
				// {
				// }
				prepStatement.close();
			}
		} catch (SQLException sqlException) {
			// sqlException.printStackTrace();
		} finally {
			prepStatement = null;
			resultSet = null;
		}
	}

	/**
	 * Closes object to release all resources.
	 */
	public void commit() {
		try {
			if (connCreated) {
				if (dbConnection != null) {
					dbConnection.commit();
				}
			}
		} catch (SQLException sqlException) {
			// sqlException.printStackTrace();
		}
	}

	/**
	 * Closes object to release all resources.
	 */
	public void rollback() {
		try {
			if (connCreated) {
				if (dbConnection != null) {
					dbConnection.rollback();
				}
			}
		} catch (SQLException sqlException) {
			// sqlException.printStackTrace();
		}
	}

	/**
	 * Closes object to release all resources.
	 */
	public void close() {
		try {
			if (connCreated) {
				if (dbConnection != null) {
					dbConnection.close();
				}
			}
		} catch (SQLException sqlException) {
			// sqlException.printStackTrace();
		} finally {
			connCreated = false;
			dbConnection = null;
		}
	}

	/**
	 * Sets CHAR, VARCHAR values to the prepared statement.
	 */
	protected void prepareStatement(String statement) throws SQLException {
		if (dbConnection != null) {
			prepStatement = dbConnection.prepareStatement(statement);
		}

		logger.debug("Prepared SQL Statement is [" + statement + "]");
	}

	/**
	 * Sets CHAR, VARCHAR values to the prepared statement.
	 */
	protected void executeStatement() throws SQLException {
		if (prepStatement != null) {
			resultSet = prepStatement.executeQuery();
		}
	}

	/**
	 * Sets CHAR, VARCHAR argument to the prepared statement.
	 */
	protected void setStringArgument(int index, String value)
			throws SQLException {
		if (value == null) {
			prepStatement.setNull(index, Types.CHAR);
		} else {
			prepStatement.setString(index, value);
		}
	}

	/**
	 * Sets DECIMAL argument to the prepared statement.
	 */
	protected void setDecimalArgument(int index, BigDecimal value)
			throws SQLException {
		if (value == null) {
			prepStatement.setNull(index, Types.DECIMAL);
		} else {
			prepStatement.setBigDecimal(index, value);
		}
	}

	/**
	 * Sets DATE argument to the prepared statement.
	 */
	protected void setDateArgument(int index, Date value) throws SQLException {
		if (value == null) {
			prepStatement.setNull(index, Types.DATE);
		} else {
			prepStatement.setDate(index, value);
		}
	}

	/**
	 * Sets DATE argument (converted from String) to the prepared statement.
	 */
	protected void setDateArgument(int index, String value) throws SQLException {
		if (value == null) {
			prepStatement.setNull(index, Types.DATE);
		} else {
			prepStatement.setDate(index, Date.valueOf(value));
		}
	}

	/**
	 * Sets TIME argument to the prepared statement.
	 */
	protected void setTimeArgument(int index, Time value) throws SQLException {
		if (value == null) {
			prepStatement.setNull(index, Types.TIME);
		} else {
			prepStatement.setTime(index, value);
		}
	}

	/**
	 * Sets TIME argument (converted from String) to the prepared statement.
	 */
	protected void setTimeArgument(int index, String value) throws SQLException {
		if (value == null) {
			prepStatement.setNull(index, Types.TIME);
		} else {
			prepStatement.setTime(index, Time.valueOf(value));
		}
	}

	/**
	 * Sets TIMESTAMP argument to the prepared statement.
	 */
	protected void setTimestampArgument(int index, Timestamp value)
			throws SQLException {
		if (value == null) {
			prepStatement.setNull(index, Types.TIMESTAMP);
		} else {
			prepStatement.setTimestamp(index, value);
		}
	}

	/**
	 * Sets TIMESTAMP argument (converted from String) to the prepared
	 * statement.
	 */
	protected void setTimestampArgument(int index, String value)
			throws SQLException {
		if (value == null) {
			prepStatement.setNull(index, Types.TIMESTAMP);
		} else {
			prepStatement.setTimestamp(index, Timestamp.valueOf(value));
		}
	}
}
