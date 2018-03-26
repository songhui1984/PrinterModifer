package com.neusoft.graphene.basecomponent.document.typeset.element;

import com.neusoft.graphene.basecomponent.document.element.DocumentRect;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;

public class TypesetRect extends TypesetElement
{
	public DocumentElementAbs build(TypesetParameters tvs,ResourceOps ops)
	{
		DocumentRect rect = new DocumentRect();
		
		rect.setX(x_TypesetNumber.value(tvs));
		rect.setY(tvs.getDatum() + y_TypesetNumber.value(tvs));
		rect.setWidth(width_TypesetNumber.value(tvs));
		rect.setHeight(height_TypesetNumber.value(tvs));
		
		rect.setColor(this.getColor());
		
		resetY(tvs, rect);
		
		return rect;
	}
}
