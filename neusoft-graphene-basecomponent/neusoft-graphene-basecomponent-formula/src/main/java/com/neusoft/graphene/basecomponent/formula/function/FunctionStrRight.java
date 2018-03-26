package com.neusoft.graphene.basecomponent.formula.function;


import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;
import com.neusoft.graphene.basecomponent.formula.Value;

public class FunctionStrRight implements Function
{
	public Object run(Object[] v, Factors factors)
	{
		if (v.length == 2)
		{
			String str = v[0].toString();
			int s = Value.intOf(v[1]);

			int len = str.length();
			if (len <= s)
				return str;
			else
				return str.substring(len - s, len);
		}
		
		throw new RuntimeException("错误的str_right运算");
	}
}
