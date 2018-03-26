package com.neusoft.graphene.basecomponent.document.typeset.element.grid;

import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetText;

public class SimpleGrid implements GridTheme
{
	public void styleContent(TypesetGrid table, TypesetText tx, int i, int j)
	{
		tx.setLeftBorder(1);
		tx.setTopBorder(1);
		tx.setRightBorder(1);
		tx.setBottomBorder(1);
	}

	public void styleTitle(TypesetGrid table, TypesetText tx, int i, int j)
	{
		tx.setLeftBorder(1);
		tx.setTopBorder(1);
		tx.setRightBorder(1);
		tx.setBottomBorder(1);
	}
}
