package com.hall4.commons.db;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public final class SQLConnectionFactory {
	
	private static final ComboPooledDataSource cpds = new ComboPooledDataSource();
	private static final int MAX_POOL_SIZE = 6;
	private static final int MIN_POOL_SIZE = 3;
	private static String USERNAME = null;
	private static String PASSWORD = null;
	private static String DBNAME = null;
	private static boolean initialized = false;
	
	public static void init(String username, String password, String dbName) {
		
		if (!initialized) {
			USERNAME = username;
			PASSWORD = password;
			DBNAME = dbName;
			
			try {
				cpds.setDriverClass("com.mysql.jdbc.Driver");
			} catch (PropertyVetoException ex) {
				throw new RuntimeException(ex);
			}
			
			//loads the jdbc driver
			cpds.setJdbcUrl( "jdbc:mysql://localhost/" + DBNAME + "?characterEncoding=utf8");
			cpds.setUser(USERNAME);
			cpds.setPassword(PASSWORD);
			
			cpds.setMinPoolSize(MIN_POOL_SIZE);                                     
			cpds.setAcquireIncrement(MIN_POOL_SIZE);
			cpds.setMaxPoolSize(MAX_POOL_SIZE);
			
			initialized = true;
		}
	}
	
	
	public static SQLConnection create() throws SQLException {
		
		if (!initialized)
			throw new SQLException("The SQLConnectionFactory must be initialized");
		
		return new SQLConnection(cpds.getConnection());
	}
	
	public static void destroy() throws SQLException {
		DataSources.destroy( cpds );
		initialized = false;
	}
	
	private SQLConnectionFactory() {}
}
