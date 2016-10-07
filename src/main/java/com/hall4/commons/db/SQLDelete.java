package com.hall4.commons.db;

public class SQLDelete
{
	public final SQLWhere where = new SQLWhere();
	private String _table = null;
	private String toAppend = null;
	
	public SQLDelete(String table) {
		_table = table;
	}
	
	public void setTable(String table) {
		_table = table.trim();
	}
	
	// FIXME: It's used now for LIMIT, do it well please :)
	public void appendToFinalQuery(String append) {
		toAppend = append;
	}
	
	@Override
	public String toString() {
		return createQuery().toString();
	}
	
	public SQLQuery createQuery()
	{
		if (_table == null || (_table != null && _table.isEmpty() ) )
			throw new IllegalArgumentException("You must indicate a table name for the DELETE statement");
		
		if (where.size() == 0)
			throw new IllegalArgumentException("You should protect your DELETE query with a WHERE clause");
			
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
