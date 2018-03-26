package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;

public class ArithmeticMod extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public ArithmeticMod()
	{
		super.addSign("%");
		super.setPriority(200);
		super.setFuntion(false);
	}
	
	public ArithmeticMod(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	public Object run(Factors getter)
	{
		Value v1 = Value.valueOf(super.getFormula(0), getter);
		Value v2 = Value.valueOf(super.getFormula(1), getter);
		
		Object result = null;
		
		if (v1.isDecimal() && v2.isDecimal())
		{
			result = new Integer(v1.toDecimal().intValue() % v2.toDecimal().intValue());
		}
		else
		{
			throw new CalculateException("取余计算要求两方都是数字类型。");
		}
		
		return result;
	}
}