package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.math.BigDecimal;
import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;

public class FunctionRound extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public FunctionRound()
	{
		super.addSign("round");
		super.setPriority(1000);
	}
	
	public FunctionRound(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	public Object run(Factors getter)
	{
		BigDecimal r = null;

		int size = formulaList.size();
		if (size == 1)
		{
			Value key = Value.valueOf((Formula)formulaList.get(0), getter);
			r = key.toDecimal().setScale(0, BigDecimal.ROUND_HALF_UP);
		}
		else if (size == 2)
		{
			Value key = Value.valueOf((Formula)formulaList.get(0), getter);
			int scale = Value.valueOf((Formula)formulaList.get(1), getter).intValue();
			r = key.toDecimal().setScale(scale, BigDecimal.ROUND_HALF_UP);
		}
		
		return r;
	}
}
