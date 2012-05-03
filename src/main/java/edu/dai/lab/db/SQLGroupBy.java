package edu.dai.lab.db;

import java.util.ArrayList;
import java.util.Iterator;

public class SQLGroupBy
{
	private ArrayList<String> _columns = new ArrayList<String>();
	
	public void addColumn(String colName) {
		_columns.add(colName);
	}
	
	public int size() {
		return _columns.size();
	}
	
	public String query()
	{	
		Iterator<String> it = _columns.iterator();		
		String groupBy = "";
		
		if (it.hasNext())
			groupBy += it.next();
		
		while (it.hasNext())
			groupBy += ", " + it.next();
		
		return groupBy;
	}
}
