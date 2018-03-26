package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;

public class ArithmeticStartsWith extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public ArithmeticStartsWith()
	{
		super.addSign("sw");
		super.addSign("startswith");
		super.setPriority(75);
		super.setFuntion(false);
	}
	
	public ArithmeticStartsWith(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	public Object run(Factors getter)
	{
		Value v1 = Value.valueOf(super.getFormula(0), getter);
		Value v2 = Value.valueOf(super.getFormula(1), getter);
		
		Object result = null;
		
		if (v1.getType() == Value.TYPE_STRING && v2.getType() == Value.TYPE_STRING)
		{
			result = new Boolean(((String)v1.toString()).startsWith(v2.toString()));
		}
		else
		{
			throw new CalculateException("“startswith”逻辑计算要求两方都是字符串类型。");
		}
		
		return result;
	}
}