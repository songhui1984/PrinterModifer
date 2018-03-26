package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Reference;


public class StatementSubLet implements Code
{
	Code l, r;
	
	public StatementSubLet(Words ws, int i)
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
			Double v = Double.valueOf(left.doubleValue() - right.doubleValue());
//			BigDecimal v = left.toDecimal().subtract(right.toDecimal());
			((Reference)l).let(factors, v);

			return v;
		}
		
		throw new RuntimeException("只可以在数字上做递减赋值运算");
	}

	public String toText(String space)
	{
		return l.toText("") + " -= " + r.toText("");
	}
}
