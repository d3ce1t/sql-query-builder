package edu.dai.lab.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class SQLSelect implements SQLSelectUpdate
{
	public SQLQuery query = null;
	public SQLFrom from = new SQLFrom();
	public final SQLWhere where;
	private String orderBy = null;
	private String toAppend = null;
	private final LinkedList<String> variables;
	private boolean selectDistinctFlag = false;
	
	private SQLGroupBy group = new SQLGroupBy();
	
	private ArrayList<String> _columns = new ArrayList<String>();
	
	public SQLSelect() {
		where = new SQLWhere(this);
		variables = new LinkedList<String>();
	}
	
	public void addColumn(String colName, boolean groupBy)
	{
		if (groupBy) {
			group.addColumn(colName);
		}
		
		addColumn(colName);
	}
	
	public void addColumn(String colName, String alias, boolean groupBy)
	{
		if (groupBy) {
			group.addColumn(colName);
		}
		
		addColumn(colName, alias);
	}
	
	public void addColumn(String colName) {
		_columns.add(colName);
	}
	
	public void addColumn(String colName, String alias) {
		_columns.add(colName + " " + alias);
	}
	
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	// FIXME: It's used now for LIMIT, do it well please :)
	public void appendToFinalQuery(String append) {
		toAppend = append;
	}
	
	public void setDistinctFlag(boolean value) {
		selectDistinctFlag = value;
	}
	
	public SQLQuery createQuery()
	{
		if (from.size() == 0)
			throw new RuntimeException("You must to indicate a table or table names for the SELECT statement");
			
		Iterator<String> it = _columns.iterator();		
		String query = "SELECT ";
		
		if (selectDistinctFlag)
			query += "DISTINCT ";
		
		if (it.hasNext())
			query += it.next();
		
		while (it.hasNext())
			query += ", " + it.next();
		
		query += " FROM " + from.query();
		
		if (where.size() > 0)
			query += " WHERE " + where.query();
		
		if (group.size() > 0)
			query += " GROUP BY " + group.query();
		
		if (orderBy != null)
			query += " ORDER BY " + orderBy;
		
		if (toAppend != null)
			query += " " + toAppend;
		
		this.query = new SQLQuery(query, SQLQueryType.SQL_QUERY);
		
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