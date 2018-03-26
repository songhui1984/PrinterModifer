package com.neusoft.graphene.basecomponent.formula.statement;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Wrap;

import java.util.LinkedHashMap;
import java.util.Map;

public class StatementBrace implements Code
{
	String type = null;
	
	Code v, a;
	
//	ArithmeticArray pv;
	
	public StatementBrace(Words ws, int i)
	{
		if (ws.getType(i) != Words.BRACE || ws.getType(ws.size() - 1) != Words.BRACE_R)
			throw new SyntaxException("找不到数组的右括号");
		
		if (i > 0) //否则为Object数组定义 [a, b, c, ...]
		{
			if (i == 1 && "double".equals(ws.getWord(0))) // double[a, b, c, ...]
				type = "double";
			else
				v = Expression.expressionOf(ws.cut(0, i));
		}
		
		a = Expression.expressionOf(ws.cut(i + 1, ws.size() - 1));
	}

	public Object run(Factors factors)
	{
		Map res = new LinkedHashMap();

		if (a == null)
			return res;

		Object r = a.run(factors);

		if (r instanceof Wrap)
		{
			for (Object val : ((Wrap)r).toList())
			{
				Code[] v = (Code[])val;
				res.put(v[0].toString(), v[1].run(factors));
			}
		}
		else if (r instanceof Code[])
		{
			Code[] v = (Code[])r;
			res.put(v[0].toString(), v[1].run(factors));
		}
		
		return res;
	}

	public String toText(String space)
	{
		return (v == null ? "OBJECT" : v.toText("")) + "{" + a.toText("") + "}";
	}
}
