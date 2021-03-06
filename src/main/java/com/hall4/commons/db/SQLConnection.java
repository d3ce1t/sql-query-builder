package com.hall4.commons.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLConnection
{
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}
	}
	
	private Connection connection = null;
	
	
	public SQLConnection(String userName, String password, String dbName) throws SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName + "?characterEncoding=utf8", userName, password);
	}
	
	public SQLConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void startTransaction() throws SQLException
	{
		connection.setAutoCommit(false);
	}
	
	public void commit() throws SQLException
	{
		connection.commit();
		connection.setAutoCommit(true);
	}
	
	public void rollBack() throws SQLException
	{
		connection.rollback();
		connection.setAutoCommit(true);
	}
	
	public QueryResult executeQuery(SQLQuery query) throws SQLException
	{	
		PreparedStatement pst = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
		QueryResult result = new QueryResult();
		ResultSet rs = null;
		int autoGeneratedKey = -1;
		int affectedRows = -1;
		
		int i = 1;
		for (String var : query.getArgsValuesAsList()) {
			pst.setString(i, var);
			i++;
		}

		switch (query.getType())
		{
		case SQL_SELECT:
			rs = pst.executeQuery();
			result.setResultSet(rs);
			break;

		case SQL_INSERT:
			affectedRows = pst.executeUpdate();
			rs = pst.getGeneratedKeys();

			if (rs.next()) {
				autoGeneratedKey = rs.getInt(1);
			}

			rs.close();

			result.setAffectedRows(affectedRows);
			result.setAutoGeneratedKey(autoGeneratedKey);
			break;

		case SQL_UPDATE:
			affectedRows = pst.executeUpdate();
			result.setAffectedRows(affectedRows);
			break;

		case SQL_DELETE:
			affectedRows = pst.executeUpdate();
			result.setAffectedRows(affectedRows);
			break;
		}
		
		return result;
	}
	
	public void close() throws SQLException {
		connection.close();
		connection = null;
	}
}