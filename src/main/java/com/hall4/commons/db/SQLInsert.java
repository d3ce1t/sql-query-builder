package com.hall4.commons.db;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

public class SQLInsert
{
	private Hashtable<String, String> _values = new Hashtable<String, String>();
	private String _table;
	
	public SQLInsert(String table) {
		_table = table;
	}
	
	public void setTable(String table) {
		_table = table.trim();
	}
	
	public void putVal(String colName, String value) {
		_values.put(colName, value);
	}
	
	@Override
	public String toString() {
		return createQuery().toString();
	}
	
	public SQLQuery createQuery()
	{
		if (_table == null || (_table != null && _table.isEmpty()))
			throw new IllegalArgumentException("You must to indicate a table name for the INSERT statement");
		
		LinkedList<String> variables = new LinkedList<String>();
		
		String strColumns, strValues;		
		Enumeration<String> itKeys = _values.keys();		
		String key = itKeys.nextElement();
		
		strColumns = key;
		strValues = "?";
		variables.addLast(_values.get(key));
		
		while (itKeys.hasMoreElements()) {
			key = itKeys.nextElement();
			strColumns += ", " + key;
			strValues += ", ?";
			variables.addLast(_values.get(key));
		}
		
		SQLQuery query = new SQLQuery("INSERT INTO `" + _table + "` (" + strColumns + ") VALUES (" + strValues + ")", SQLQueryType.SQL_INSERT);
		
		for (String var : variables) {
			query.addVariable(var);
		}
		
		return query;
	}
}
