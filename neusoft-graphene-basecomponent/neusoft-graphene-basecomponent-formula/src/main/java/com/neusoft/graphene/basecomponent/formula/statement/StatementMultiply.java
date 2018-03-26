package com.neusoft.graphene.basecomponent.formula.statement;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;

public class StatementMultiply implements Code
{
	Code l, r;
	
	public StatementMultiply(Words ws, int i)
	{
		l = Expression.expressionOf(ws.cut(0, i));
		r = Expression.expressionOf(ws.cut(i + 1));
	}

	public Object run(Factors factors)
	{
		Value left = Value.valueOf(l, factors);
		Value right = Value.valueOf(r, factors);
		
		if (left.isDecimal() && right.isDecimal())
		{
//			return left.toDecimal().multiply(right.toDecimal());
			return Double.valueOf(left.doubleValue() * right.doubleValue());
		}
		
		throw new RuntimeException("只可以对数字做乘法运算 - " + left + " * " + right);
	}

	public String toText(String space)
	{
		return l.toText("") + " * " + r.toText("");
	}
}
