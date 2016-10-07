package com.hall4.commons.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class SQLSelect
{
	public SQLFrom from = new SQLFrom();
	public final SQLWhere where = new SQLWhere();
	private String orderBy = null;
	private String toAppend = null;
	private boolean selectDistinctFlag = false;
	
	private SQLGroupBy group = new SQLGroupBy();
	
	private ArrayList<String> _columns = new ArrayList<String>();
	
	public SQLSelect() {}
	
	public SQLSelect(String table) {
		from.addTable(table);
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
	
	@Override
	public String toString() {
		return createQuery().toString();
	}
	
	public SQLQuery createQuery()
	{
		if (from.size() == 0)
			throw new IllegalArgumentException("You must indicate a table or table names for the SELECT statement");
			
		Iterator<String> it = _columns.iterator();		
		String queryStr = "SELECT ";
		SQLQuery query = new SQLQuery();
		
		if (selectDistinctFlag)
			queryStr += "DISTINCT ";
		
		if (it.hasNext())
			queryStr += it.next();
		
		while (it.hasNext())
			queryStr += ", " + it.next();
		
		queryStr += " FROM " + from.query();
		
		if (where.size() > 0) {
			queryStr += " WHERE " + where.query();
			LinkedList<String> variables = where.getVariables(); // Obtain values for ? or ?s variables
			for (String var : variables) {
				query.addVariable(var);
			}
		}
		
		if (group.size() > 0)
			queryStr += " GROUP BY " + group.query();
		
		if (orderBy != null)
			queryStr += " ORDER BY " + orderBy;
		
		if (toAppend != null)
			queryStr += " " + toAppend;
		
		query.setQuery(queryStr, SQLQueryType.SQL_SELECT);
		
		return query; 
	}
}