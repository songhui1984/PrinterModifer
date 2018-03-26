package com.neusoft.graphene.basecomponent.formula.statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;



import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Syntax;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;
import com.neusoft.graphene.basecomponent.formula.script.Code;

public class StatementNew implements Code
{
	Code v, a;
	
	String cluss;
	int type;
	
	List<Code> codeList;
	
	public StatementNew(Words ws, int i)
	{
		if (i == ws.size() - 1 || ws.getType(i + 1) != Words.CLASS)
			throw new SyntaxException("没有找到new的类型");
		
		cluss = ws.getWord(i + 1);
		
		if (ws.size() > i + 2 && ws.getType(i + 2) == Words.BRACKET) //数组
		{
			type = 1; 
			
			codeList = new ArrayList<Code>();
			
			int l = i + 2;
			int r = Syntax.findRightBrace(ws, l + 1);
			codeList.add(Expression.expressionOf(ws.cut(l + 1, r)));
			
			if (ws.size() > r + 1 && ws.getType(r + 1) == Words.BRACKET)
			{
				l = r + 1;
				r = Syntax.findRightBrace(ws, l + 1);
				codeList.add(Expression.expressionOf(ws.cut(l + 1, r)));
			}
		}
		else if (ws.size() > i + 2 && ws.getType(i + 2) == Words.PRT) //对象
		{
			type = 2;
		}
	}

	public Object run(Factors factors)
	{
		if (type == 1 && codeList != null)
		{
			if (codeList.size() == 1)
			{
				int s = Value.intOf((Code)codeList.get(0), factors);
				
				if ("double".equals(cluss))
					return new double[s];
				if ("int".equals(cluss))
					return new int[s];
				if ("object".equals(cluss))
					return new Object[s];
			}
			else if (codeList.size() == 2)
			{
				int s1 = Value.intOf((Code)codeList.get(0), factors);
				int s2 = Value.intOf((Code)codeList.get(1), factors);
				
				Object r = null;
				if ("double".equals(cluss))
					return new double[s1][s2];
				if ("int".equals(cluss))
					return new int[s1][s2];
				if ("object".equals(cluss))
					return new Object[s1][s2];
				
				return r;
			}
		}
		else if (type == 2)
		{
			if ("map".equals(cluss))
				return new HashMap();
			if ("list".equals(cluss))
				return new ArrayList();
			if ("synchMap".equals(cluss))
				return new ConcurrentHashMap();
			if ("synchList".equals(cluss))
				return Collections.synchronizedList(new ArrayList());
		}
		
		return null;
	}

	public String toText(String space)
	{
		return "";
	}
}
