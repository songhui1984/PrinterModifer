package com.neusoft.graphene.basecomponent.document.typeset.element;

import com.neusoft.graphene.basecomponent.document.element.DocumentReset;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetNumber;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;


public class TypesetReset extends TypesetElement
{
	private boolean newPage = false;
	
	private TypesetNumber skip;
	
	public TypesetReset(boolean newPage)
	{
		this.newPage = newPage;
	}
	
	public TypesetReset(TypesetNumber skip, boolean newPage)
	{
		this.skip = skip;
		this.newPage = newPage;
	}
	
	public DocumentElementAbs build(TypesetParameters typesetParameters,ResourceOps ops)
	{
		int datum = typesetParameters.getY();
		
		if (newPage)
		{
			typesetParameters.setDatum(datum + (skip == null ? 0 : skip.value(typesetParameters)));
			typesetParameters.setY(typesetParameters.getDatum());
			
			DocumentReset reset = new DocumentReset(true);
			reset.setLocation(0, typesetParameters.getDatum());
			
			return reset;
		}
		else
		{
			typesetParameters.setDatum(datum + (skip == null ? 0 : skip.value(typesetParameters)));
			typesetParameters.setY(typesetParameters.getDatum());
			
			return null;
		}
	}
}
