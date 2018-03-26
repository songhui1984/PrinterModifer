package com.neusoft.graphene.basecomponent.document.typeset;

import com.neusoft.graphene.basecomponent.document.LexFontFamily;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;

public class TypesetFontFamily extends LexFontFamily
{
	private static final long serialVersionUID = 1L;

	Formula pathFunc;
	
	public TypesetFontFamily(String name, String path) 
	{
		super(name, path);
	}

	public TypesetFontFamily(String name, Formula path) 
	{
		super(name, null);
		
		this.pathFunc = path;
	}
	
	public void build(Factors p)
	{
		super.setPath(Value.stringOf(pathFunc, p));
	}
}
