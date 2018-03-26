package com.neusoft.graphene.basecomponent.formula.function;


import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;

public class FunctionStrTrim implements Function
{
	public Object run(Object[] v, Factors factors)
	{
		if (v[0] == null)
		{
			if (v.length > 1)
				return v[1].toString().trim();
			else
				return null;
		}

		return v[0].toString().trim();
	}
}
