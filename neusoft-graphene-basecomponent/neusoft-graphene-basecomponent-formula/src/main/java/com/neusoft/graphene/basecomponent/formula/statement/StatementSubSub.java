package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Reference;


public class StatementSubSub implements Code
{
	Code l, r;
	
	public StatementSubSub(Words ws, int i)
	{
		if (i == 0)
			r = Expression.expressionOf(ws.cut(i + 1));
		else if (i == ws.size() - 1)
			l = Expression.expressionOf(ws.cut(0, i));
		
		if ((l == null && r == null) || (!(l instanceof Reference) && !(r instanceof Reference)))
			throw new SyntaxException("递减运算要求左右之一为引用");
	}

	public Object run(Factors factors)
	{
		if (l != null)
		{
			Value v = Value.valueOf(l, factors);
			if (v.isDecimal())
			{
				double num = v.doubleValue();
				((Reference)l).let(factors, Double.valueOf(num - 1));
				
	//			BigDecimal num = v.toDecimal();
	//			((Reference)l).let(factors, num.subtract(new BigDecimal(1)));
				return Double.valueOf(num);
			}
		}
		else
		{
			Value v = Value.valueOf(r, factors);
			if (v.isDecimal())
			{
				Double num = Double.valueOf(v.doubleValue() - 1);
				((Reference)r).let(factors, num);
	
	//			BigDecimal num = v.toDecimal().subtract(new BigDecimal(1));
	//			((Reference)r).let(factors, num);
				return num;
			}
		}
		
		throw new RuntimeException("只可以在数字上做递减");
	}

	public String toText(String space)
	{
		if (l != null)
			return l.toText("") + "--";
		else
			return "--" + l.toText("");
	}
}
