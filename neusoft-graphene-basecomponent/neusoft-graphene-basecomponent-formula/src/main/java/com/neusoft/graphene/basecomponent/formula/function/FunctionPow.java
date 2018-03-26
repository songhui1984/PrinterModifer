package com.neusoft.graphene.basecomponent.formula.function;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;
import com.neusoft.graphene.basecomponent.formula.Value;

public class FunctionPow implements Function
{
	public Object run(Object[] v, Factors factors)
	{
		if (v.length == 2)
		{
			double v1 = Value.valueOf(v[0]).doubleValue();
			double v2 = Value.valueOf(v[1]).doubleValue();
			
			return Double.valueOf(Math.pow(v1, v2));
		}
		
		throw new RuntimeException("错误的pow运算");
	}
}
