package com.neusoft.graphene.basecomponent.document.typeset.enviroment;

import com.neusoft.graphene.basecomponent.document.LexFont;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetCoord;

public class TextDimensionSimple implements TextDimension
{

	public TypesetCoord getSize(LexFont font, String text)
	{
		if (text == null)
			return new TypesetCoord(0, 0);
		else
			return new TypesetCoord((int)(text.length() * font.getSize()), (int)font.getSize());
	}

}
