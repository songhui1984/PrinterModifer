package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;

public class ArithmeticIn extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public ArithmeticIn()
	{
		super.addSign("in");
		super.setPriority(50);
		super.setFuntion(false);
	}
	
	public ArithmeticIn(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	@SuppressWarnings("rawtypes")
	public Object run(Factors getter)
	{
		Value v1 = Value.valueOf(super.getFormula(0), getter);
		Value v2 = Value.valueOf(super.getFormula(1), getter);
		
		Object result = null;
		
		if (v2.getType() == Value.TYPE_LIST)
		{
			result = new Boolean(((List)v2.getValue()).indexOf(v1.getValue()) >= 0);
		}
		else
		{
			throw new CalculateException("in运算要求右侧为List类型。");
		}
		
		return result;
	}
}