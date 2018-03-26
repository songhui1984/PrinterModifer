package com.neusoft.graphene.basecomponent.document.typeset.element.grid;

import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetText;

public interface GridTheme
{
	void styleTitle(TypesetGrid table, TypesetText text, int i, int j);

	void styleContent(TypesetGrid table, TypesetText text, int i, int j);
}
