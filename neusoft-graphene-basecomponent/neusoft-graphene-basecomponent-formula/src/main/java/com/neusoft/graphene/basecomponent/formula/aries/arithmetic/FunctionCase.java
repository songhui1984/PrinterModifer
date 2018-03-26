package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;


public class FunctionCase extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public FunctionCase()
	{
		super.addSign("case");
		super.setPriority(1000);
	}
	
	public FunctionCase(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	/*
	 * case(A > 100, -1, 1)
	 * case(A, 1, -1, 2, -1, 3, -1, 1)
	 */
	public Object run(Factors getter)
	{
		int size = formulaList.size();
		
//		LexValue key = ((IFormula)paramList.get(0)).getResult(getter);
		
		Value key = null;
		try
		{
			key = Value.valueOf((Formula)formulaList.get(0), getter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
//			System.out.println(paramList.get(0));
//			System.out.println(paramList.get(1));
//			System.out.println(paramList.get(2));
		}
		
		if (size == 3)
		{
			if (key.getType() == Value.TYPE_BOOLEAN)
			{
				if (key.booleanValue())
					return ((Formula)formulaList.get(1)).run(getter);
				else
					return ((Formula)formulaList.get(2)).run(getter);
			}
		}
		else
		{
			for (int i=1;i<size-1;i+=2)
			{
				Value v = Value.valueOf((Formula)formulaList.get(i), getter);
				if (key.equals(v))
					return ((Formula)formulaList.get(i+1)).run(getter);
			}
			
			if (size % 2 == 0)
				return ((Formula)formulaList.get(size-1)).run(getter);
		}

		throw new CalculateException("case函数必须返回一个值");
	}
}
