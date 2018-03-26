package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Syntax;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Wrap;


public class StatementParentheses implements Code
{
	Code c;
	
	public StatementParentheses(Words ws, int i)
	{
		int l = i;
		int r = Syntax.findRightBrace(ws, l + 1);
		
		if (l != 0 || r != ws.size() - 1)
			throw new SyntaxException("小括号两侧有无法处理的代码");
		
		if (l + 1 == r)
			throw new SyntaxException("小括号运算内部不能为空");
		
		c = Expression.expressionOf(ws.cut(l + 1, r));
	}

	public Object run(Factors factors)
	{
		Object r = c.run(factors);
		if (r instanceof Wrap)
			return ((Wrap)r).toArray();
					
		return r;
	}

	public String toText(String space)
	{
		return "(" + c.toText("") + ")";
	}
}
