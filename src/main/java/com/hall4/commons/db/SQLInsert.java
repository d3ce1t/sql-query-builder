package com.hall4.commons.db;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

public class SQLInsert implements SQLSelectUpdate
{
	private Hashtable<String, String> _values = new Hashtable<String, String>();
	private String _table;
	private final LinkedList<String> variables;
	
	public SQLInsert() {
		variables = new LinkedList<String>();
	}
	
	public void setTable(String table) {
		_table = table.trim();
	}
	
	public void addValue(String colName, String value) {
		_values.put(colName, value);
	}
	
	public SQLQuery createQuery()
	{
		if (_table != null && _table.isEmpty())
			throw new IllegalArgumentException("You must to indicate a table name for the INSERT statement");
		
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
		
		SQLQuery queryObject = new SQLQuery("INSERT INTO `" + _table + "` (" + strColumns + ") VALUES (" + strValues + ")", SQLQueryType.SQL_INSERT);
		
		for (String var : variables) {
			queryObject.addVariable(var);
		}
		
		return queryObject;
	}

	@Override
	public LinkedList<String> getVariables() {
		return variables;
	}
}
