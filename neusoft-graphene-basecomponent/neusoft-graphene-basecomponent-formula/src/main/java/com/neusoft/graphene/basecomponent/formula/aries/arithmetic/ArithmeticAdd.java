package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.math.BigDecimal;
import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;
import com.neusoft.graphene.basecomponent.formula.exception.CalculateException;



public class ArithmeticAdd extends Arithmetic implements Formula
{
	private static final long serialVersionUID = 1L;

	public ArithmeticAdd()
	{
		super.addSign("+");
		super.setPriority(100);
		super.setFuntion(false);
	}
	
	public ArithmeticAdd(List<Formula> formulaList)
	{
		super.setFormulaList(formulaList);
	}
	
	public Object run(Factors factors)
	{
		Value v1 = Value.valueOf(super.getFormula(0), factors);
		Value v2 = Value.valueOf(super.getFormula(1), factors);
		
		Object result = null;
		
		if (v1.isDecimal() && v2.isDecimal())
		{
			result = v1.toDecimal().add(v2.toDecimal());
		}
		else if ((v1.isDecimal() || v1.isType(Value.TYPE_STRING)) && (v2.isDecimal() || v2.isType(Value.TYPE_STRING)))
		{
			String left = v1.isDecimal() ? v1.toDecimal().toString() : v1.toString();
			String right = v2.isDecimal() ? v2.toDecimal().toString() : v2.toString();
			result = left + right;
		}
		else if (((v1.isType(Value.TYPE_BOOLEAN)) || v1.isDecimal()) && ((v2.isType(Value.TYPE_BOOLEAN)) || v2.isDecimal()))
		{
			BigDecimal val1, val2;
			
			if (v1.isType(Value.TYPE_BOOLEAN))
				val1 = new BigDecimal(v1.booleanValue() ? 1 : 0);
			else
				val1 = v1.toDecimal();

			if (v2.isType(Value.TYPE_BOOLEAN))
				val2 = new BigDecimal(v2.booleanValue() ? 1 : 0);
			else
				val2 = v2.toDecimal();
			
			return val1.add(val2);
		}
		else
		{
			if (v1.getValue() == null || v2.getValue() == null)
				throw new CalculateException("加法计算要求两方都不能为空。v1 - " + v1.getValue() + "，v2 - " + v2.getValue());

			String left = v1.isDecimal() ? v1.toDecimal().toString() : v1.getValue().toString();
			String right = v2.isDecimal() ? v2.toDecimal().toString() : v2.getValue().toString();
			
			result = left + right;
		}
		
		return result;
	}
}
