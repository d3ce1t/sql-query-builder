package edu.dai.lab.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class SQLWhere
{	
	private SQLSelectUpdate parent = null;
	private ArrayList<SQLCondition> filter = new ArrayList<SQLCondition>();	
	
	public boolean modeOr = false;
	
	public SQLWhere(SQLSelectUpdate parent) {
		this.parent = parent;
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
		if (!modeOr)
			return queryAnd();
		else
			return queryOr();
	}
	
	private String queryAnd()
	{
		String where = "";
		
		Iterator<SQLCondition> it = filter.iterator();
		SQLCondition condition = null;
		
		if (it.hasNext())
		{
			condition = it.next();
			where += condition;
			
			// Process Variables
			if (condition.getType() == ConditionType.IN || condition.getType() == ConditionType.NOT_IN) {
				copyVariables(condition.getSubSelect().getVariables(), parent.getVariables());
			}
			else if (condition.getType() != ConditionType.CUSTOM) {
				parent.getVariables().add(condition.getValue2());
			}
		}
			
		while (it.hasNext())
		{
			condition = it.next();
			where += " AND " + condition;
			
			// Process Variables
			if (condition.getType() == ConditionType.IN || condition.getType() == ConditionType.NOT_IN) {
				copyVariables(condition.getSubSelect().getVariables(), parent.getVariables());
			}
			else if (condition.getType() != ConditionType.CUSTOM) {
				parent.getVariables().add(condition.getValue2());
			}
		}
		
		return where;
	}
	
	private String queryOr()
	{
		String where = "";
		
		Iterator<SQLCondition> it = filter.iterator();
		SQLCondition condition = null;
		
		if (it.hasNext())
		{
			condition = it.next();
			where += condition;
			
			// Process Variables
			if (condition.getType() == ConditionType.IN || condition.getType() == ConditionType.NOT_IN) {
				copyVariables(condition.getSubSelect().getVariables(), parent.getVariables());
			}
			else if (condition.getType() != ConditionType.CUSTOM) {
				parent.getVariables().add(condition.getValue2());
			}
		}
			
		while (it.hasNext())
		{
			condition = it.next();
			where += " OR " +condition;
			
			// Process Variables
			if (condition.getType() == ConditionType.IN || condition.getType() == ConditionType.NOT_IN) {
				copyVariables(condition.getSubSelect().getVariables(), parent.getVariables());
			}
			else if (condition.getType() != ConditionType.CUSTOM) {
				parent.getVariables().add(condition.getValue2());
			}
		}
		
		return where;
	}
	
	private void copyVariables(LinkedList<String> source, LinkedList<String> target) {
		for (String var : source) {
			target.add(var);
		}
	}
}
