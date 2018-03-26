package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;

public class FunctionTry extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public FunctionTry()
	{
		super.addSign("try");
		super.setPriority(1000);
	}
	
	public FunctionTry(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	public Object run(Factors getter)
	{
		int size = formulaList.size();
		if (size == 2)
		{
			try
			{
				return ((Formula)formulaList.get(0)).run(getter);
			}
			catch (Exception e)
			{
				return ((Formula)formulaList.get(1)).run(getter);
			}
		}
		else if (size == 1)
		{
			try
			{
				return ((Formula)formulaList.get(0)).run(getter);
			}
			catch (Exception e)
			{
				return null;
			}
		}
		
		throw new CalculateException("try运算需要一个或两个参数");
	}
}
