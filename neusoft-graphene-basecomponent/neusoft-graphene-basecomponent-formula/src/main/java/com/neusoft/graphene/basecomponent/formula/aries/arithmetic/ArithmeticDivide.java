package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.math.BigDecimal;
import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;

public class ArithmeticDivide extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;
	
	int accuracy = 10;
	
	public ArithmeticDivide()
	{
		super.addSign("/");
		super.setPriority(200);
		super.setFuntion(false);
	}
	
	public ArithmeticDivide(List<Formula> formulaList)
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
			result = v1.toDecimal().divide(v2.toDecimal(), accuracy, BigDecimal.ROUND_HALF_UP);
		}
		else
		{
			throw new CalculateException("除法计算要求两方都是数字类型。");
		}
		
		return result;
	}
}