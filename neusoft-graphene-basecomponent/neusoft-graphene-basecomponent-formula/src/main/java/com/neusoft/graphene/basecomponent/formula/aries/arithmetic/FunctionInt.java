package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.math.BigDecimal;
import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;


public class FunctionInt extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public FunctionInt()
	{
		super.addSign("int");
		super.setPriority(1000);
	}
	
	public FunctionInt(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	public Object run(Factors getter)
	{
		Integer r = null;

		int size = formulaList.size();
		if (size == 1)
		{
			Value key = Value.valueOf((Formula)formulaList.get(0), getter);
			if (key.isType(Value.TYPE_STRING))
			{
				r = new Integer(new BigDecimal(key.toString()).toBigInteger().intValue());
			}
			else
			{
				r = new Integer((int)key.toDecimal().toBigInteger().intValue());
			}
		}
		
		return r;
	}
}
