package edu.dai.lab.db;

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
		connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, userName, password);
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
	
	public void executeQuery(SQLQuery query) throws SQLException
	{	
		PreparedStatement pst = connection.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
		ResultSet rs = null;
		int autoGeneratedKey = -1;
		int affectedRows = -1;
		
		int i = 1;
		for (String var : query.getVariables()) {
			pst.setString(i, var);
			i++;
		}

		switch (query.getType())
		{
		case SQL_QUERY:
			rs = pst.executeQuery();
			query.setResultSet(rs);
			break;

		case SQL_INSERT:
			affectedRows = pst.executeUpdate();
			rs = pst.getGeneratedKeys();

			if (rs.next()) {
				autoGeneratedKey = rs.getInt(1);
			}

			rs.close();

			query.setAffectedRows(affectedRows);
			query.setAutoGeneratedKey(autoGeneratedKey);
			break;

		case SQL_UPDATE:
			affectedRows = pst.executeUpdate();
			query.setAffectedRows(affectedRows);
			break;

		case SQL_DELETE:
			affectedRows = pst.executeUpdate();
			query.setAffectedRows(affectedRows);
			break;
		}
	}
	
	public void close() throws SQLException {
		connection.close();
		connection = null;
	}
}