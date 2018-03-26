package com.neusoft.graphene.basecomponent.document.typeset.element.sheet;

import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetText;

public interface SheetTheme
{
	 void styleTitle(TypesetSheet table, TypesetText text, int i, int j);

	 void styleContent(TypesetSheet table, TypesetText text, int i, int j);
}
