package com.hall4.commons.db;

public class SQLCondition
{
	private SQLSelect subselect;  // Condition can be a subselect result
	private String value1;        // or a two values comparison
	private String value2;
	private ConditionType type;
	
	public SQLCondition(String value1, String value2, ConditionType condition) {
		this.value1 = value1;
		this.value2 = value2;
		this.type = condition;
	}
	
	public SQLCondition(String value1, SQLSelect subselect, ConditionType condition) {
		this.value1 = value1;
		this.subselect = subselect;
		this.type = condition;
	}
	
	public SQLCondition(String expr) {
		this.value1 = expr;
		this.type = ConditionType.CUSTOM;
	}
	
	public String getValue2() {
		return value2;
	}
	
	public ConditionType getType() {
		return type;
	}
	
	public SQLSelect getSubSelect() {
		return subselect;
	}
	
	public String toString()
	{
		String result = null;
		
		switch(type) {
		case REFERENCE: // Used for comparing two fields, for instance, table1.id = table2.id
			result = value1 + " = " + value2;
			break;

		case EQUAL:
			result = value1 + " = ?";
			break;
			
		case LIKE:
			result = value1 + " LIKE ?";
			break;
			
		case NOT_EQUAL:
			result = value1 + " <> ?";
			break;
			
		case HIGHER_VALUE1_THAN_VALUE2:
			result = value1 + " > ?";
			break;
			
		case LOWER_VALUE1_THAN_VALUE2:
			result = value1 + " < ?";
			break;
			
		case NOT_IN:
			result = value1 + " NOT IN (" + subselect.createQuery().toString() + ")";
			break;
			
		case IN:
			result = value1 + " IN (" + subselect.createQuery().toString() + ")";
			break;
			
		case CUSTOM:
			result = value1;
			break;
		}
		
		return result;
	}
}
