package com.neusoft.graphene.basecomponent.formula.statement;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;


public class StatementAdd implements Code
{
	Code l, r;
	
	public StatementAdd(Words ws, int i)
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
//			return left.toDecimal().add(right.toDecimal());
			return Double.valueOf(left.doubleValue() + right.doubleValue());
		}
		else if (left.isNull())
		{
			return right.getValue();
		}
		else if (right.isNull())
		{
			return left.getValue();
		}
		else
		{
			return left.toString() + right.toString();
		}
	}

	public String toText(String space)
	{
		return l.toText("") + " + " + r.toText("");
	}
}
