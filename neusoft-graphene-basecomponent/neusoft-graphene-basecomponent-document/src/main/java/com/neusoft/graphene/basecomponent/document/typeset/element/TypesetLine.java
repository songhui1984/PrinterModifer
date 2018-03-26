package com.neusoft.graphene.basecomponent.document.typeset.element;

import com.neusoft.graphene.basecomponent.document.element.DocumentLine;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetNumber;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;

public class TypesetLine extends TypesetElement
{
	TypesetNumber x1, x2, y1, y2;
	
	public TypesetLine(String x1, String y1, String x2, String y2)
	{
		this.x1 = TypesetNumber.numberOf(x1);
		this.y1 = TypesetNumber.numberOf(y1);
		this.x2 = TypesetNumber.numberOf(x2);
		this.y2 = TypesetNumber.numberOf(y2);
	}
	
	public DocumentElementAbs build(TypesetParameters typesetParameters,ResourceOps ops)
	{
		DocumentLine line = new DocumentLine();
		
		line.setX(x1.value(typesetParameters));
		line.setY(typesetParameters.getDatum() + y1.value(typesetParameters));
		line.setWidth(x2.value(typesetParameters) - x1.value(typesetParameters));
		line.setHeight(y2.value(typesetParameters) - y1.value(typesetParameters));
		
		line.setColor(this.getColor());
		
		resetY(typesetParameters, line);
		
		return line;
	}
}
