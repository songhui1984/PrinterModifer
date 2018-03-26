package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Syntax;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Script;
import com.neusoft.graphene.basecomponent.formula.script.Wrap;

public class StatementThread implements Code
{
	Code callback, code;

	public StatementThread(Words ws)
	{
		int left = 1; //callback左括号的位置
		if (ws.getType(left) != Words.PRT)
			throw new RuntimeException("thread 后面需要为小括号");

		int right = Syntax.findRightBrace(ws, left + 1);
		callback = Expression.expressionOf(ws.cut(left + 1, right));

		left = right + 1;
		if (ws.getType(left) != Words.BRACE)
			throw new RuntimeException("thread 代码体需要以大括号包裹");

		right = Syntax.findRightBrace(ws, left + 1);
		code = new Script(ws.cut(left + 1, right));
	}

	public Object run(final Factors factors)
	{
		final Function res = callback == null ? null : (Function)callback.run(factors);

		Thread th = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Object r = code.run(factors);
				if (res != null)
				{
					if (r instanceof Wrap)
						res.run(((Wrap)r).toArray(), factors);
					else
						res.run(new Object[]{r}, factors);
				}
			}
		});

		th.start();

		return null;
	}

	public String toText(String space)
	{
		StringBuffer buf = new StringBuffer("THREAD (");
		buf.append(callback == null ? "" : callback.toText(""));
		buf.append(")\n");
		buf.append(space + "{\n");
		buf.append(code.toText(space + "    ") + "\n");
		buf.append(space + "}");

		return buf.toString();
	}
}
