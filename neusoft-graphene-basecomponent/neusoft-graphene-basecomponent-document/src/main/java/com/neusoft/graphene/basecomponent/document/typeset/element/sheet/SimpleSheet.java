package com.neusoft.graphene.basecomponent.document.typeset.element.sheet;

import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetText;

public class SimpleSheet implements SheetTheme
{
	public void styleContent(TypesetSheet table, TypesetText tx, int i, int j)
	{

		tx.setLeftBorder(1);
		tx.setTopBorder(1);
		tx.setRightBorder(1);
		tx.setBottomBorder(1);
	}

	public void styleTitle(TypesetSheet table, TypesetText tx, int i, int j)
	{
		tx.setLeftBorder(j == 0 ? -1 : 1);
		tx.setTopBorder(i == 0 ? -1 : 1);
		tx.setRightBorder(-1);
		tx.setBottomBorder(-1);
	}
}
