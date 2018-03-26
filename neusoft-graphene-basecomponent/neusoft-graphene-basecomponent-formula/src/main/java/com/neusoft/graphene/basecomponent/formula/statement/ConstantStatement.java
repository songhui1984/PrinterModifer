package com.neusoft.graphene.basecomponent.formula.statement;
import java.math.BigDecimal;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;

public class ConstantStatement implements Code
{
	int type;
	String text;
	
	Object v;
	
	public ConstantStatement(int type, String text)
	{
		this.type = type;
		this.text = text;
		
		if (type == Words.NULLPT)
		{
			v = null;
		}
		else if (type == Words.TRUE)
		{
			v = Boolean.TRUE;
		}
		else if (type == Words.FALSE)
		{
			v = Boolean.FALSE;
		}
		else if (type == Words.NUMBER)
		{
			v = new BigDecimal(text);
		}
		else if (type == Words.STRING)
		{
			v = text.substring(1, text.length() - 1);
		}
		else
		{
			throw new RuntimeException("无法识别的常量");
		}
	}
	
	public Object run(Factors factors)
	{
		return v;
	}

	public String toText(String space)
	{
		if (v == null)
			return "NULL";
		
		return v instanceof String ? "'" + v + "'" : v.toString();
	}
	
	public String toString()
	{
		return v == null ? null : v.toString();
	}

}
