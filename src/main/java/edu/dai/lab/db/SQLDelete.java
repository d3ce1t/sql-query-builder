package edu.dai.lab.db;

import java.sql.SQLException;
import java.util.LinkedList;

public class SQLDelete implements SQLSelectUpdate
{
	public SQLQuery query = null;
	public final SQLWhere where;
	private String _table = null;
	private final LinkedList<String> variables;
	private String toAppend = null;
	
	public SQLDelete() {
		where = new SQLWhere(this);
		variables = new LinkedList<String>();
	}
	
	public void setTable(String table) {
		_table = table.trim();
	}
	
	public int getAffectedRows() {
		return this.query.getAffectedRows();
	}
	
	// FIXME: It's used now for LIMIT, do it well please :)
	public void appendToFinalQuery(String append) {
		toAppend = append;
	}
	
	public SQLQuery createQuery() throws SQLException
	{
		if (_table != null && _table.isEmpty())
			throw new SQLException("You must to indicate a table name for the DELETE statement");
		
		if (where.size() == 0)
			throw new SQLException("You should protect your DELETE query with a WHERE clause");
			
		String query = "DELETE FROM `" + _table + "`";
				
		query += " WHERE " + where.query();
		
		if (toAppend != null)
			query += " " + toAppend;
		
		this.query = new SQLQuery(query, SQLQueryType.SQL_DELETE); 
		
		for (String var : variables) {
			this.query.addVariable(var);
		}
		
		return this.query; 
	}

	@Override
	public LinkedList<String> getVariables() {
		return variables;
	}
}
