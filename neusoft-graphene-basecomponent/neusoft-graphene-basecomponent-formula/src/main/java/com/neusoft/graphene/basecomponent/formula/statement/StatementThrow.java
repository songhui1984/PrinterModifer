package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;

public class StatementThrow implements Code
{
	Code r;
	
	public StatementThrow(Words ws)
	{
		r = Expression.expressionOf(ws.cut(1));
	}

	public Object run(Factors factors)
	{
		throw new RuntimeException(Value.stringOf(r, factors));
//		return Interrupt.interruptOf(Interrupt.THROW, r.run(factors));
	}

	public String toText(String space)
	{
		return "THROW " + r.toText("");
	}
}
