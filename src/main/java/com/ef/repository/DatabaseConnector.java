package com.ef.repository;

import java.sql.*;

import com.ef.util.Utilities;

public class DatabaseConnector {

	private Connection sqlConnection;

	private PreparedStatement preparedStatement;

	private DatabaseConnector() {
	}

	private static class LazyHolder {
		static final DatabaseConnector INSTANCE = new DatabaseConnector();
	}

	public static DatabaseConnector getInstance() {
		return LazyHolder.INSTANCE;
	}

	public DatabaseConnector connectToDatabase(String dbUrl, String dbUsername, String dbPassword) {
		try {
			sqlConnection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		} catch (SQLException sqle) {
			throw new RuntimeException("Couldn't connect to the database, check url and credentials.", sqle);
		}
		return this;
	}

	public DatabaseConnector createNewPreparedStatement(String fileNameWithExtension, Object... params) {
		try {
			String sqlString = Utilities.readScriptFromResources(fileNameWithExtension);
			preparedStatement = sqlConnection.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
			if (params != null) {
				for (int i = 1; i <= params.length; i++) {
					preparedStatement.setObject(i, params[i - 1]);
				}
			}
		} catch (SQLException sqle) {
			throw new RuntimeException("Couldn't create statement connection might be closed.", sqle);
		}
		return this;
	}

	public DatabaseConnector disableAutoCommit() {
		try {
			sqlConnection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException("Couldn't set parameter.", e);
		}
		return this;
	}

	public DatabaseConnector commit() {
		try {
			sqlConnection.commit();
		} catch (SQLException e) {
			throw new RuntimeException("Couldn't set parameter.", e);
		}
		return this;
	}

	public DatabaseConnector addToBatch() {
		try {
			preparedStatement.addBatch();
		} catch (SQLException e) {
			throw new RuntimeException("Couldn't set parameter.", e);
		}
		return this;
	}

	public DatabaseConnector setPreparedStatementValues(Object... params) {
		if (params != null) {
			try {
				for (int i = 1; i <= params.length; i++) {
					preparedStatement.setObject(i, params[i - 1]);
				}
			} catch (SQLException e) {
				throw new RuntimeException("Couldn't set parameter.", e);
			}
		}
		return this;
	}

	public DatabaseConnector setPreparedStatementValue(int parameterPosition, Object value) {
		try {
			preparedStatement.setObject(parameterPosition, value);
		} catch (SQLException e) {
			throw new RuntimeException("Couldn't set parameter.", e);
		}
		return this;
	}

	public ResultSet executePreparedStatementQuery() {
		ResultSet resultSet = null;
		try {
			resultSet = preparedStatement.executeQuery();
		} catch (SQLException sqle) {
			throw new RuntimeException("Couldn't excute the query string.", sqle);
		}
		return resultSet;
	}

	public ResultSet executePreparedStatementUpdate() {
		ResultSet resultSet = null;
		try {
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
		} catch (SQLException sqle) {
			throw new RuntimeException("Couldn't excute the update string.", sqle);
		}
		return resultSet;
	}

	public void excuteBatch() {
		try {
			preparedStatement.executeBatch();
		} catch (SQLException sqle) {
			throw new RuntimeException("Couldn't excute the update string.", sqle);
		}
	}

	public void closeDatabaseStatementAndConnection() {
		CloseJdbcStatment();
		CloseJdbcConnection();
	}

	public void CloseJdbcStatment() {
		try {
			if (preparedStatement != null)
				preparedStatement.clearBatch();
			preparedStatement.clearParameters();
			preparedStatement.close();
		} catch (SQLException se2) {
		} // nothing we can do
	}

	public void CloseJdbcConnection() {
		try {
			if (sqlConnection != null)
				sqlConnection.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} // end finally try
	}
	
	public Connection getSqlConnection() {
		return sqlConnection;
	}

}
