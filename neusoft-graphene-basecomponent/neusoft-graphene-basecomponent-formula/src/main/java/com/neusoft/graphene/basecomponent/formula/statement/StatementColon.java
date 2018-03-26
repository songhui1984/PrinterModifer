package com.neusoft.graphene.basecomponent.formula.statement;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;


public class StatementColon implements Code
{
	Code[] v;

	String symbol;
	
	public StatementColon(Words ws, int i)
	{
		v = new Code[2];
		v[0] = Expression.expressionOf(ws.cut(0, i));
		v[1] = Expression.expressionOf(ws.cut(i + 1));

		symbol = ws.getWord(i);
	}

	public String getSymbol()
	{
		return symbol;
	}

	public Object run(Factors factors)
	{
		return v;
	}
	
	public String toText(String space)
	{
		return v[0].toText("") + " : " + v[1].toText("");
	}
}
