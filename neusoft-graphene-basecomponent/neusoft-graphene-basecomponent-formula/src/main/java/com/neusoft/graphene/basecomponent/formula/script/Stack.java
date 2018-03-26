package com.neusoft.graphene.basecomponent.formula.script;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.graphene.basecomponent.formula.Factors;


public class Stack implements Factors
{
	Factors root;
	
	Map<String,Object> heap;
	
	public Stack()
	{
	}
	
	public Stack(Factors root)
	{
		this.root = root;
	}

	public Stack(Map<String,Object> root)
	{
		this.heap = root;
	}
	
	public void declare(String name)
	{
		if (heap == null)
			heap = new HashMap<String,Object>();
		
		heap.put(name, null);
	}
	
	public void set(String name, Object value)
	{
		if (!hasVar(name))
			declare(name);
		
		if (heap != null && heap.containsKey(name))
			heap.put(name, value);
		else
			((Stack)root).set(name, value);
	}
	
	public void setAll(Map<String,Object> map)
	{
		if (heap == null)
			heap = new HashMap<String,Object>();
		
		if (map != null)
			heap.putAll(map);
	}
	
	public boolean hasVar(String name)
	{
		if (heap != null && heap.containsKey(name))
			return true;
		
		if (root instanceof Stack)
			return ((Stack)root).hasVar(name);
		else
			return false;
	}
	
	public Object get(String name)
	{
		if ("this".equals(name))
			return this;

		if (heap != null && heap.containsKey(name))
			return heap.get(name);
		
		if (root != null)
			return root.get(name);
		
		return null;
	}
	
	public void remove(String name)
	{
		if (heap != null && heap.containsKey(name))
			heap.remove(name);
	}
	
	public Factors getParent()
	{
		return root;
	}
}
