package com.neusoft.graphene.basecomponent.document.typeset.element;

import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;
import com.neusoft.graphene.basecomponent.formula.Formula;

public class TypesetScript extends TypesetElement
{
	Formula script_Formula;
	
	public TypesetScript(Formula script_Formula)
	{
		this.script_Formula = script_Formula;
	}
	
	public DocumentElementAbs build(TypesetParameters typesetParameters,ResourceOps ops)
	{
		script_Formula.run(typesetParameters);
		
		return null;
	}

}
