package com.neusoft.graphene.basecomponent.formula.function;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;


public class FunctionFind implements Function
{
	public Object run(Object[] v, Factors factors)
	{
		Object array = v[0];
		Object value = v[1];

		int r = -1;
		
		if (array instanceof List)
		{
			List<?> list = (List<?>)array;
			r = list.indexOf(value);
		}
		else if (array instanceof Object[])
		{
			Object[] src = (Object[])array;
			for (int i=0;i<src.length;i++)
			{
				if (value == null && src[i] == null)
					return i;
				if (value != null && value.equals(src[i]))
					return i;
			}
		}
		
		return r < 0 ? null : r;
	}
}
