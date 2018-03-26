package com.neusoft.graphene.basecomponent.formula.statement;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Interrupt;

public class StatementContinue implements Code
{
	public StatementContinue(Words ws)
	{
	}

	public Object run(Factors factors)
	{
		return Interrupt.interruptOf(Interrupt.CONTINUE);
	}

	public String toText(String space)
	{
		return "CONTINUE";
	}
}
