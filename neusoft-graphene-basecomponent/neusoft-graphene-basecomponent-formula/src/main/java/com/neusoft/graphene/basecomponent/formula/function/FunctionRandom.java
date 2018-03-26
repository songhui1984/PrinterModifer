package com.neusoft.graphene.basecomponent.formula.function;

import java.util.Random;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;
import com.neusoft.graphene.basecomponent.formula.Value;


public class FunctionRandom implements Function
{
	static Random random = new Random();
	
	public Object run(Object[] v, Factors factors)
	{
		if (v == null || v.length == 0)
			return random.nextInt();
		
		if (v.length == 1)
			return random.nextInt(Value.intOf(v[0]));
		
		throw new RuntimeException("错误的random运算");
	}
}
