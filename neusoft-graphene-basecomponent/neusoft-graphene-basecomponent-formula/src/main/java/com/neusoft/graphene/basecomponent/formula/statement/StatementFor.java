package com.neusoft.graphene.basecomponent.formula.statement;
import java.util.Collection;
import java.util.Map;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Syntax;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Interrupt;
import com.neusoft.graphene.basecomponent.formula.script.Reference;
import com.neusoft.graphene.basecomponent.formula.script.Script;
import com.neusoft.graphene.basecomponent.formula.script.Stack;

public class StatementFor implements Code
{
	int type = 1;
	
	Code f1, f2, f3, fc;
	
	public StatementFor(Words ws)
	{
		int left = 1;
		int right = Syntax.findRightBrace(ws, left + 1);
		Script c = new Script(ws.cut(left + 1, right));
		
		if (c.size() == 3)
		{
			f1 = c.getSentence(0);
			f2 = c.getSentence(1);
			f3 = c.getSentence(2);
		}
		else if (c.size() == 1)
		{
			f1 = c.getSentence(0);
			type = 2;
		}
		
		left = right + 1;
		int type1 = ws.getType(left);
		if (type1 == Words.BRACE)
		{
			right = Syntax.findRightBrace(ws, left + 1);
			fc = new Script(ws.cut(left + 1, right));
		}
		else
		{
			fc = new Script(ws.cut(left, ws.size()));
		}
	}

	public Object run(Factors factors)
	{
		Stack stack = new Stack(factors);
		
		if (type == 2)
		{
			Code[] cc = (Code[])f1.run(stack);
			Object value = cc[1].run(stack);
			Reference ref = (Reference)cc[0];
			
			if (value instanceof Object[])
			{
				for (Object v : (Object[])value)
				{
					ref.let(stack, v);
					
					Object result = fc.run(stack);
					
					if (Interrupt.isMatch(result, Interrupt.BREAK))
						break;
					if (Interrupt.isMatch(result, Interrupt.RETURN))// || Interrupt.isMatch(result, Interrupt.THROW))
						return result;
				}
			}
			else if (value instanceof Collection)
			{
				for (Object v : (Collection<?>)value)
				{
					ref.let(stack, v);
					
					Object result = fc.run(stack);
					
					if (Interrupt.isMatch(result, Interrupt.BREAK))
						break;
					if (Interrupt.isMatch(result, Interrupt.RETURN))// || Interrupt.isMatch(result, Interrupt.THROW))
						return result;
				}
			}
			else if (value instanceof Map)
			{
//				Collection val = null;
//
//				if (f1 instanceof ArithmeticColon)
//				{
//					ArithmeticColon ac = (ArithmeticColon) f1;
//					if ("::".equals(ac.getSymbol()))
//						val = ((Map<?, ?>)value).values();
//				}
//
//				if (val == null)
//					val = ((Map<?, ?>)value).keySet();

				for (Object v : ((Map<?, ?>)value).keySet())
				{
					ref.let(stack, v);

					Object result = fc.run(stack);

					if (Interrupt.isMatch(result, Interrupt.BREAK))
						break;
					if (Interrupt.isMatch(result, Interrupt.RETURN))// || Interrupt.isMatch(result, Interrupt.THROW))
						return result;
				}
			}
		}
		else
		{
			if (f1 != null)
				f1.run(stack);
			
			while (Value.booleanOf(f2, stack))
			{
				Object result = fc.run(stack);
				
				if (Interrupt.isMatch(result, Interrupt.BREAK))
					break;
				if (Interrupt.isMatch(result, Interrupt.RETURN))// || Interrupt.isMatch(result, Interrupt.THROW))
					return result;
				
				f3.run(stack);
			}
		}
		
		return null;
	}

	public String toText(String space)
	{
		StringBuffer buf = new StringBuffer("FOR (");
		if (type == 2)
			buf.append(f1.toText(""));
		else
			buf.append((f1 == null ? "" : f1.toText("")) + "; " + (f2 == null ? "" : f2.toText("")) + "; " + (f3 == null ? "" : f3.toText("")));
		buf.append(")\n");
		buf.append(space + "{\n");
		buf.append(fc.toText(space + "    ") + "\n");
		buf.append(space + "}");
		
		return buf.toString();
	}
}
