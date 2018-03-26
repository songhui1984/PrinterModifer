package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;

public class ArithmeticSubtract extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public ArithmeticSubtract()
	{
		super.addSign("-");
		super.setPriority(100);
		super.setFuntion(false);
	}
	
	public ArithmeticSubtract(List<Formula> formulaList)
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
			result = v1.toDecimal().subtract(v2.toDecimal());
		}
		else
		{
			throw new CalculateException("减法计算要求两方都是数字类型。");
		}
		
		return result;
	}
}
