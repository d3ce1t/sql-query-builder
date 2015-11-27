package com.hall4.commons.db;

import java.sql.SQLException;

public class SQLDelete
{
	public final SQLWhere where = new SQLWhere();
	private String _table = null;
	private String toAppend = null;
	
	public void setTable(String table) {
		_table = table.trim();
	}
	
	// FIXME: It's used now for LIMIT, do it well please :)
	public void appendToFinalQuery(String append) {
		toAppend = append;
	}
	
	public SQLQuery createQuery() throws SQLException
	{
		if (_table == null || (_table != null && _table.isEmpty() ) )
			throw new SQLException("You must indicate a table name for the DELETE statement");
		
		if (where.size() == 0)
			throw new SQLException("You should protect your DELETE query with a WHERE clause");
			
		String queryStr = "DELETE FROM `" + _table + "` WHERE " + where.query();
		
		if (toAppend != null)
			queryStr += " " + toAppend;
		
		SQLQuery query = new SQLQuery(queryStr, SQLQueryType.SQL_DELETE); 
		
		for (String var : where.getVariables()) {
			query.addVariable(var);
		}
		
		return query; 
	}
}
