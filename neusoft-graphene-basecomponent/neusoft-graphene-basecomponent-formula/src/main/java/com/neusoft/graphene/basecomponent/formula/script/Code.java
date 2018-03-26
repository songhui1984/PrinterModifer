package com.neusoft.graphene.basecomponent.formula.script;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;

public interface Code extends Formula
{
	public Object run(Factors factors);
	
	public String toText(String space);
}
