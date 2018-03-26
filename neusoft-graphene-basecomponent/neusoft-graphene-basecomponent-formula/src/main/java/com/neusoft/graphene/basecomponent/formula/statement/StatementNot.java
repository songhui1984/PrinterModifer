package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;


public class StatementNot implements Code
{
	Code r;
	
	public StatementNot(Words ws, int i)
	{
		r = Expression.expressionOf(ws.cut(i + 1));
	}

	public Object run(Factors factors)
	{
		Value v = Value.valueOf(r, factors);
		if (v.isBoolean() || v.isDecimal())
			return new Boolean(!v.booleanValue());
		
		throw new RuntimeException("NOT逻辑运算，要求值为boolean类型");
	}

	public String toText(String space)
	{
		return "NOT " + r.toText("");
	}
}
