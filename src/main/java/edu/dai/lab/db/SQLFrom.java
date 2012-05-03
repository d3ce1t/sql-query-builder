package edu.dai.lab.db;

import java.util.Enumeration;
import java.util.Hashtable;

public class SQLFrom
{
	private Hashtable<String, String> _from = new Hashtable<String, String>();
	
	public void addTable(String name)
	{
		_from.put(name, "");
	}
	
	public void addTable(String name, String alias)
	{
		_from.put(name, alias);
	}
	
	public int size()
	{
		return _from.size();
	}
	
	public String query()
	{
		String from = "";
		String key = null, value = null;
		
		Enumeration<String> it = _from.keys();
		
		if (it.hasMoreElements())
		{
			key = it.nextElement();
			value = _from.get(key);
			
			if (!value.equals(""))
				from += "`" + key + "` AS " + value;
			else
				from += "`" + key + "`";
		}
			
		while (it.hasMoreElements())
		{
			key = it.nextElement();
			value = _from.get(key);
				
			if (!value.equals(""))
				from += ", `" + key + "` AS " + value;
			else
				from += ", `" + key + "`";
		}

		return from;
	}
}
