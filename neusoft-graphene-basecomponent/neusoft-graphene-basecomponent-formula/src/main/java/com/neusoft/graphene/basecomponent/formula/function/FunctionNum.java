package com.neusoft.graphene.basecomponent.formula.function;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;


import java.math.BigDecimal;

public class FunctionNum implements Function
{
	public Object run(Object[] v, Factors factors)
	{
		if (v == null || v.length == 0 || v[0] == null)
			return null;
		else if (v[0] instanceof Number)
			return v[0];
		else
			return new BigDecimal(v[0].toString());
	}
}
