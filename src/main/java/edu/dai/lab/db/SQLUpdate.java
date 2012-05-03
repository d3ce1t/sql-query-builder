package edu.dai.lab.db;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

public class SQLUpdate implements SQLSelectUpdate
{
	public SQLFrom from = new SQLFrom();
	public final SQLWhere where;
	private LinkedList<String> variables;
	private String toAppend = null;
	
	protected Hashtable<String, String> _values = new Hashtable<String, String>();
	protected Hashtable<String, OperationType> _operations = new Hashtable<String, OperationType>();
	
	public SQLUpdate() {
		where = new SQLWhere(this);
		variables = new LinkedList<String>();
	}
	
	// FIXME: It's used now for LIMIT, do it well please :)
	public void appendToFinalQuery(String append) {
		toAppend = append;
	}
	
	public void addValue(String colName, String value) {
		_values.put(colName, value);	
	}
	
	public void addOperation(String colName, OperationType operation) {
		_operations.put(colName, operation);
	}
	
	public SQLQuery createQuery()
	{
		if (from.size() == 0)
			throw new IllegalArgumentException("You must to indicate a table or table names for the UPDATE statement");
			
		if (where.size() == 0)
			throw new IllegalArgumentException("You should protect your query update with a WHERE clause");
		
		LinkedList<String> newVars = new LinkedList<String>();
		Enumeration<String> itKeys = _values.keys();
		String key = null;
		String strValues = "";
		
		if (itKeys.hasMoreElements()) {
			key = itKeys.nextElement();
			strValues = key + " = ?";
			newVars.addLast(_values.get(key));
		}
			
		while (itKeys.hasMoreElements()) {
			key = itKeys.nextElement();
			strValues += ", " + key + " = ?";
			newVars.addLast(_values.get(key));
		}
		
		// Combine both lists
		for (String value : variables) {
			newVars.addLast(value);
		}
		
		variables.clear(); // don't need
		
		// Variables is now newVars.
		variables = newVars;
		
		String allOperations = getAllOperationsString();
		String updateValues = "";
		
		if (!allOperations.isEmpty() && !strValues.isEmpty()) {
			updateValues = allOperations + ", " + strValues;
		} else if (!allOperations.isEmpty()) {
			updateValues = allOperations;
		} else if (!strValues.isEmpty()) {
			updateValues = strValues;
		} else {
			throw new IllegalArgumentException("You should at least indicate one column to update");
		}
		
		String query = "UPDATE " + from.query() + " SET " + updateValues + " WHERE " + where.query();
		
		if (toAppend != null)
			query += " " + toAppend;
		
		SQLQuery queryObject = new SQLQuery(query, SQLQueryType.SQL_UPDATE);
		
		for (String var : variables) {
			queryObject.addVariable(var);
		}
		
		return queryObject;
	}

	@Override
	public LinkedList<String> getVariables() {
		return variables;
	}
	
	private String getAllOperationsString()
	{
		String result = "";
				
		Enumeration<String> itKeys = _operations.keys();
		String key = null;
		
		if (itKeys.hasMoreElements()) {
			key = itKeys.nextElement();
			result = getOperationString(key, _operations.get(key));
		}
		
		while (itKeys.hasMoreElements()) {
			key = itKeys.nextElement();
			result += ", " + getOperationString(key, _operations.get(key));
		}
		
		return result;
	}
	
	private String getOperationString(String colName, OperationType type)
	{
		if (colName == null) throw new NullPointerException();
		if (type == null) throw new NullPointerException();
		
		String result = null;
		
		result = colName + " = ";
		
		switch (type) {
		case INCREMENT:
			result += colName + "+1";
		}
		
		return result;
	}
}
