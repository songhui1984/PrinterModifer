package com.neusoft.graphene.basecomponent.formula.function;

import java.math.BigDecimal;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;
import com.neusoft.graphene.basecomponent.formula.Value;


public class FunctionFloor implements Function
{
	public Object run(Object[] v, Factors factors)
	{
		if (v.length == 1)
			return Integer.valueOf((int)Math.floor(Value.doubleOf(v[0])));
		
		if (v.length == 2)
		{
			int scale = Value.valueOf(v[1]).intValue();
			
			BigDecimal d = Value.valueOf(v[0]).toDecimal();
			return d.setScale(scale, BigDecimal.ROUND_FLOOR);
		}
		
		throw new RuntimeException("错误的floor运算");
	}
}
