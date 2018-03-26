package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;


public class StatementQuestMark implements Code
{
	Code l, r;
	
	public StatementQuestMark(Words ws, int i)
	{
		l = Expression.expressionOf(ws.cut(0, i));
		r = Expression.expressionOf(ws.cut(i + 1));
	}

	public Object run(Factors factors)
	{
		Value v = Value.valueOf(l, factors);
		if (v.isType(Value.TYPE_BOOLEAN))
		{
			Object ro = r.run(factors);
			
			if (!(ro instanceof Code[]) || ((Code[])ro).length != 2)
				throw new RuntimeException("?:组合运算没有找到冒号");

			Code[] c = (Code[])ro;

			if (v.booleanValue())
				return c[0].run(factors);
			else
				return c[1].run(factors);
		}
		
		throw new RuntimeException("?!组合运算要求问号左侧值为boolean类型");
	}

	public String toText(String space)
	{
		return l.toText("") + " ? " + r.toText("");
	}
}
