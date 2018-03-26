package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;


public class StatementNotEqual implements Code
{
	Code l, r;
	
	public StatementNotEqual(Words ws, int i)
	{
		l = Expression.expressionOf(ws.cut(0, i));
		r = Expression.expressionOf(ws.cut(i + 1));
	}

	public Object run(Factors factors)
	{
		Value v1 = Value.valueOf(l, factors);
		Value v2 = Value.valueOf(r, factors);
		
		if (v1.isNull() && v2.isNull())
			return new Boolean(false);
		else if (!v1.isNull() && !v2.isNull())
		{
			if (v1.isDecimal() && v2.isDecimal())
				return new Boolean(v1.doubleValue() != v2.doubleValue());
			else
				return new Boolean(!v1.getValue().equals(v2.getValue()));
		}
		else
			return new Boolean(true);
	}

	public String toText(String space)
	{
		return l.toText("") + " <> " + r.toText("");
	}
}
