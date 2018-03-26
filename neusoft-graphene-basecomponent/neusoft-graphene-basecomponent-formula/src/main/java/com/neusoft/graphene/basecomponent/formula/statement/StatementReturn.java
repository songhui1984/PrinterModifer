package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Interrupt;

public class StatementReturn implements Code
{
	Code r;
	
	public StatementReturn(Words ws)
	{
		Words exp = ws.cut(1);
		if (exp.size() != 0)
			r = Expression.expressionOf(exp);
	}

	public Object run(Factors factors)
	{
		return Interrupt.interruptOf(Interrupt.RETURN, r == null ? null : r.run(factors));
	}

	public String toText(String space)
	{
		return "RETURN" + (r == null ? "" : " " + r.toText(""));
	}
}
