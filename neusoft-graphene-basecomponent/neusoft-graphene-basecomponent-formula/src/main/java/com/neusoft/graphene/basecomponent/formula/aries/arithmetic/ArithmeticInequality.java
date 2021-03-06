package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;

public class ArithmeticInequality extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public ArithmeticInequality()
	{
		super.addSign("<>");
		super.addSign("!=");
		super.setPriority(50);
		super.setFuntion(false);
	}
	
	public ArithmeticInequality(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	public Object run(Factors getter)
	{
		Value v1 = Value.valueOf(super.getFormula(0), getter);
		Value v2 = Value.valueOf(super.getFormula(1), getter);
		
		Object result = null;
		
		if (v1.getValue() == null || v2.getValue() == null)
		{
			result = new Boolean(v1.getValue() != v2.getValue()); 
		}
		else if (v1.isDecimal() && v2.isDecimal())
		{
			result = new Boolean(v1.toDecimal().compareTo(v2.toDecimal()) != 0);
		}
		else if (v1.getType() == Value.TYPE_STRING && v2.getType() == Value.TYPE_STRING)
		{
			result = new Boolean(!v1.toString().equals(v2.toString()));
		}
		else if (v1.getValue() == v2.getValue())
		{
			result = new Boolean(false);
		}
		else
		{
			result = new Boolean(true);
		}
		
		return result;
	}
}