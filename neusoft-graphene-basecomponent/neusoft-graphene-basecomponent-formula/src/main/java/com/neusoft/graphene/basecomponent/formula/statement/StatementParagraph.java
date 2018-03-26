package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.analyse.Syntax;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Script;
import com.neusoft.graphene.basecomponent.formula.script.Stack;

public class StatementParagraph implements Code
{
	Code c;
	
	public StatementParagraph(Words ws)
	{
		int i = 0;
		int r = Syntax.findRightBrace(ws, i + 1);
		
		if (i != 0 || r != ws.size() - 1)
			throw new SyntaxException("表达式内部脚本作为一个值，与周围的计算无法匹配");
		
		c = new Script(ws.cut(i + 1, r));
	}

	public Object run(Factors factors)
	{
		return c.run(new Stack(factors));
	}

	public String toText(String space)
	{
		StringBuffer buf = new StringBuffer("{\n");
		buf.append(c.toText(space + "    ") + "\n");
		buf.append(space + "}");
		
		return buf.toString();
	}
}
