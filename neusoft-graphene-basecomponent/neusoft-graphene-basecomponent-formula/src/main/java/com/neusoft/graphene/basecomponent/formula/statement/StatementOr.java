package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;


public class StatementOr implements Code
{
	Code l, r;
	
	public StatementOr(Words ws, int i)
	{
		l = Expression.expressionOf(ws.cut(0, i));
		r = Expression.expressionOf(ws.cut(i + 1));
	}

	public Object run(Factors factors)
	{
		return new Boolean(v(l, factors) || v(r, factors));
	}
	
	private boolean v(Code c, Factors factors)
	{
		return Value.booleanOf(c.run(factors));
		
//		Value v = Value.valueOf(c, factors);
//		if (v.isType(Value.TYPE_BOOLEAN))
//			return v.booleanValue();
//		if (v.isType(Value.TYPE_DECIMAL))
//			return v.intValue() != 0;
//		
//		throw new RuntimeException("AND逻辑运算，要求两侧返回值为boolean类型或数字类型(取整后0为false，其他值为true)");
	}

	public String toText(String space)
	{
		return l.toText("") + " OR " + r.toText("");
	}
}
