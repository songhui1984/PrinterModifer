package com.neusoft.graphene.basecomponent.formula.statement;
import java.util.ArrayList;
import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Syntax;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Stack;

public class StatementVar implements Code
{
	Code r;
	
	List<String> names = new ArrayList<String>();
	
	public StatementVar(Words ws)
	{
		names.add(ws.getWord(1));

		if (ws.size() > 2)
		{
			r = Expression.expressionOf(ws.cut(1));
			
			for (int i = 2; i < ws.size(); i++)
			{
				if (ws.getType(i) == Words.COMMA)
				{
					names.add(ws.getWord(i + 1));
					i++;
				}
				else if (Syntax.isLeftBrace(ws, i))
				{
					i = Syntax.findRightBrace(ws, i + 1);
				}
			}
		}
	}

	public Object run(Factors factors)
	{
		for (int i = 0; i < names.size(); i++)
			((Stack)factors).declare((String)names.get(i));
		
		if (r == null)
			return null;
		
		return r.run(factors);
	}

	public String toText(String space)
	{
		return "VAR " + (r == null ? names.toString() : r.toText(""));
	}
}
