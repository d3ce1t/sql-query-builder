package edu.dai.lab.db;

import java.sql.SQLException;

public final class SQLConnectionFactory {
	
	private static String USERNAME = null;
	private static String PASSWORD = null;
	private static String DBNAME = null;
	
	public static void init(String username, String password, String dbName) {
		USERNAME = username;
		PASSWORD = password;
		DBNAME = dbName;
		DataBase.init(username, password, dbName);
	}
	
	
	public static SQLConnection create() throws SQLException {
		
		if (USERNAME == null || PASSWORD == null || DBNAME == null)
			throw new SQLException("The SQLConnectionFactory must be initialized");
		
		return new SQLConnection(USERNAME, PASSWORD, DBNAME);
	}
	
	private SQLConnectionFactory() {}
}
