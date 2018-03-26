package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;

public class ArithmeticOr extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public ArithmeticOr()
	{
		super.addSign("||");
		super.addSign("or");
		super.setPriority(10);
		super.setFuntion(false);
	}
	
	public ArithmeticOr(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	public Object run(Factors getter)
	{
		Value v1 = Value.valueOf(super.getFormula(0), getter);
		Value v2 = Value.valueOf(super.getFormula(1), getter);
		
		Object result = null;
		
		if (v1.isType(Value.TYPE_BOOLEAN) && v2.isType(Value.TYPE_BOOLEAN))
		{
			result = new Boolean(v1.booleanValue() || v2.booleanValue());
		}
		else
		{
			throw new CalculateException("“or”逻辑计算要求两方都是boolean类型。");
		}
		
		return result;
	}
}