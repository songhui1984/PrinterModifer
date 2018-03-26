package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;

public class ArithmeticGreater extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public ArithmeticGreater()
	{
		super.addSign(">");
		super.addSign("gt");
		super.setPriority(50);
		super.setFuntion(false);
	}
	
	public ArithmeticGreater(List<Formula> formulaList)
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
			result = new Boolean(v1.toDecimal().compareTo(v2.toDecimal()) == 1);
		}
		else
		{
			throw new CalculateException("“大于”逻辑计算要求两方都是数字类型。");
		}
		
		return result;
	}
}