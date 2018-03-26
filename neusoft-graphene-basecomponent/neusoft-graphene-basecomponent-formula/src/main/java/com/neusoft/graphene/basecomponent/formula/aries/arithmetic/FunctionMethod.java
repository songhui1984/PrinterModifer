package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;


public class FunctionMethod implements Formula
{
	String methodName;
	List<Formula> formulaList;
	
	public FunctionMethod(String methodName, List<Formula> formulaList)
	{
		this.methodName = methodName;
		this.formulaList = formulaList;
	}

	@SuppressWarnings("unchecked")
	public Object run(Factors getter)
	{
//		Object object = getter.getValue(objectName);
//		Method method = object.getClass().getDeclaredMethod(functionName, new Class[] {});
//		Object result = method.invoke(object, new Object[] {});
//		return new LexValue(result);
		
		@SuppressWarnings("rawtypes")
		List result = new ArrayList();
		result.add(methodName);
		
		int s = formulaList == null ? 0 : formulaList.size();
		for (int i=0;i<s;i++)
		{
			Formula p = formulaList.get(i);
			result.add(p.run(getter));
		}
		
		return result;
	}
}
