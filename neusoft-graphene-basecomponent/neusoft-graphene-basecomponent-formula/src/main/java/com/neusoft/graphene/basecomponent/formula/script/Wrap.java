package com.neusoft.graphene.basecomponent.formula.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;


public class Wrap
{
	List<Object> values = new ArrayList<Object>();
	
	public Wrap()
	{
	}
	
	public Wrap(Object r)
	{
		values.add(r);
	}
	
	public Wrap(Object v1, Object v2)
	{
		if (v1 instanceof Wrap)
			values.addAll(((Wrap)v1).values);
		else
			values.add(v1);
		
		if (v2 instanceof Wrap)
			values.addAll(((Wrap)v2).values);
		else
			values.add(v2);
	}
	
	public List<Object> toList()
	{
		return values;
	}
	
	
	public int size()
	{
		return values.size();
	}
	
	
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		for (Iterator<Object> iter = values.iterator(); iter.hasNext();)
		{
			Object v = iter.next();
			s.append(v + ", ");
		}
		
		return s.toString();
	}

	
	public Object[] toArray()
	{
		return values.toArray();
	}
	
	/**
	 * 下面的已经作废，现在是都自动计算了
	 * 
	 * 如果只有一个参数，那么直接就计算了
	 * 如果是多个参数，那么直接把计算公式打包返回，不计算
	 * 
	 * @author lerrain
	 *
	 */
	public static Wrap wrapOf(Formula code, Factors p)
	{
		if (code == null)
			return new Wrap();
		
		Object r = code.run(p);
		if (r instanceof Wrap)
			return (Wrap)r;
		else
			return new Wrap(r);
	}
	
	public static Object[] arrayOf(Formula code, Factors p)
	{
		return wrapOf(code, p).toArray();
	}
	
	public static double[] doubleArrayOf(Formula code, Factors p)
	{
		Wrap wrap = wrapOf(code, p);
		
		double[] r = new double[wrap.size()];
		for (int i = 0; i < r.length; i++)
			r[i] = Value.doubleOf(wrap.values.get(i));
		
		return r;
	}
}
