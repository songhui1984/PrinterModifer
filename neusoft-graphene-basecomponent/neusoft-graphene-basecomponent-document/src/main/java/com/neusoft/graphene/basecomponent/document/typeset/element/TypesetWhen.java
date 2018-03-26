package com.neusoft.graphene.basecomponent.document.typeset.element;

import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.exception.TypesetBuildException;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetUtil;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;

public class TypesetWhen extends TypesetPanel
{
	public TypesetWhen(Formula condition_Formula)
	{
		setCondition(condition_Formula);
	}

	
	public DocumentElementAbs build(TypesetParameters typesetParameters,ResourceOps ops)
	{
		boolean pass = false;
		try
		{
			pass = Value.booleanOf(condition, typesetParameters);
		}
		catch (Exception e)
		{
			if (TypesetUtil.getMode() == TypesetUtil.MODE_FAIL)
			{
				throw new TypesetBuildException("exception in condition's formula: " + condition, e);
			}
			else if (TypesetUtil.getMode() == TypesetUtil.MODE_ALWAYS)
			{
				pass = true;
//				System.out.println("exception in condition's formula: " + condition + ", set it TRUE instead.");
			}
		}
			
		return pass ? super.build(typesetParameters,ops) : null;
	}
}
