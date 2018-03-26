package com.neusoft.graphene.basecomponent.formula.function;


import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;


public class FunctionStrLen implements Function
{
	public Object run(Object[] v, Factors factors)
	{
		if (v.length == 1)
		{
			return v[0].toString().length();
		}
		
		throw new RuntimeException("错误的str_len运算");
	}
}
