package com.hall4.commons.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class SQLWhere
{	
	private ArrayList<SQLCondition> filter;
	private LinkedList<String> variables;
	
	public boolean modeOr = false;
	
	public SQLWhere() {
		filter = new ArrayList<SQLCondition>();	
		variables = new LinkedList<String>();
	}
	
	public void setModeOr(boolean value) {
		modeOr = value;
	}
	
	public void add(String value1, ConditionType conditionType, String value2) {
		filter.add(new SQLCondition(value1, value2, conditionType));
	}
	
	public void addCustom(String expr) {
		filter.add(new SQLCondition(expr));
	}
	
	public void addCondition(SQLCondition condition) {
		filter.add(condition);
	}
	
	public int size() {
		return filter.size();
	}
	
	public String query()
	{
		String whereStr = "";
		
		Iterator<SQLCondition> it = filter.iterator();
		SQLCondition condition = null;
				
		int i = 0;
		
		String logicCmp = modeOr ? " OR " : " AND "; // Keep spaces!!
		
		while (it.hasNext())
		{
			condition = it.next();
			
			if (i > 0) {
				whereStr += logicCmp + condition;
			} else {
				whereStr += condition;
			}
			
			i++;
			
			// Process Variables
			if (condition.getType() == ConditionType.IN || condition.getType() == ConditionType.NOT_IN) {
				copyVariables(condition.getSubSelect().where.getVariables(), variables);
			}
			else if (condition.getType() != ConditionType.CUSTOM && condition.getType() != ConditionType.REFERENCE) {
				variables.add(condition.getValue2());
			}
		}
		
		return whereStr;
	}
	
	public LinkedList<String> getVariables() {
		return variables;
	}
	
	private void copyVariables(LinkedList<String> source, LinkedList<String> target) {
		for (String var : source) {
			target.add(var);
		}
	}
}
