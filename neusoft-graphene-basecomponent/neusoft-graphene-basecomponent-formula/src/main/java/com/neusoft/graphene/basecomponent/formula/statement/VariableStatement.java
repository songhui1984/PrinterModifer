package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Reference;
import com.neusoft.graphene.basecomponent.formula.script.Stack;

public class VariableStatement implements Code, Reference
{
	String varName;
	
	public VariableStatement(String name)
	{
		this.varName = name;
	}

	public Object run(Factors factors)
	{
		if ("timems".equals(varName))
			return Double.valueOf((double)System.currentTimeMillis());
		
		return factors.get(varName);
	}
	
	public void let(Factors factors, Object value)
	{
		((Stack)factors).set(varName, value);
	}
	
	public String toString()
	{
		return varName;
	}

	public String toText(String space)
	{
		return varName;
	}
}
