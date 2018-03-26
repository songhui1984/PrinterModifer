package com.neusoft.graphene.basecomponent.formula.statement;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Wrap;


public class StatementComma implements Code
{
	Code l, r;
	
	public StatementComma(Words ws, int i)
	{
		l = Expression.expressionOf(ws.cut(0, i));
		r = Expression.expressionOf(ws.cut(i + 1));
	}

	public Object run(Factors factors)
	{
		return new Wrap(l.run(factors), r.run(factors));
		
//		/*
//		 * 由逗号分割开的多个表达式，通常是用作函数或数组的参数，并不是每个都需要计算的
//		 * 所以直接打包返回，处理的部分根据需要计算全部或者部分
//		 * 如：x[i] = case(i>0,x[i-1]+y,y); 
//		 * 如果每个都计算，那么这个函数是没办法运行的。
//		 */
//		return new Wrap(l, r);
	}
	
	public Code left()
	{
		return l;
	}

	public Code right()
	{
		return r;
	}

	public String toText(String space)
	{
		return l.toText("") + ", " + r.toText("");
	}
}
