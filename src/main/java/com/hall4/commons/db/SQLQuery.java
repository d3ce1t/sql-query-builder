package com.hall4.commons.db;

import java.util.LinkedList;

public class SQLQuery 
{
	private final LinkedList<String> _variables;
	private String _query;
	private SQLQueryType _type;
	
	public SQLQuery() {
		_variables = new LinkedList<String>();
	}
	
	public SQLQuery(String query, SQLQueryType type) {
		_query = query;
		_type = type;
		_variables = new LinkedList<String>();
	}
	
	public void setQuery(String query, SQLQueryType type) {
		_query = query;
		_type = type;
	}
	
	public void addVariable(String variable) {
		_variables.add(variable);
	}
	
	public SQLQueryType getType() {
		return _type;
	}
	
	@Override
	public String toString() {
		return _query;
	}

	public LinkedList<String> getVariables() {
		return _variables;
	}
}
