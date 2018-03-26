package com.neusoft.graphene.basecomponent.document.typeset.element;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.graphene.basecomponent.document.element.DocumentPanel;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;


public class TypesetPanel extends TypesetElement
{
	List<TypesetElement> typesetElementList = new ArrayList<TypesetElement>();
	
	
	public TypesetPanel(String x, String y, String width, String height)
	{
		super.setX(x);
		super.setY(y);
		super.setWidth(width);
		super.setHeight(height);
	}
	
	public TypesetPanel()
	{
	}
	
	public void add(TypesetElement element)
	{
		typesetElementList.add(element);
	}
	
	public int getElementNum()
	{
		return typesetElementList.size();
	}
	
	public TypesetElement getElement(int index)
	{
		return (TypesetElement)typesetElementList.get(index);
	}

	public DocumentElementAbs build(TypesetParameters tvs,ResourceOps _ops)
	{
		DocumentPanel dPanel = new DocumentPanel();
		
		if (this.getX_TypesetNumber() != null)
			dPanel.setX(this.getX_TypesetNumber().value(tvs));
		else
			dPanel.setX(0);
		
		if (this.getY_TypesetNumber() != null)
			dPanel.setY(tvs.getDatum() + this.getY_TypesetNumber().value(tvs));
		else
			dPanel.setY(tvs.getDatum());

		TypesetParameters tvs2 = pack(tvs);
		tvs2.setStreamY(tvs.getStreamY() + dPanel.getY());
		
		int width = 0, height = 0;
		for (int i=0;i<typesetElementList.size();i++)
		{
			TypesetElement typesetElement = typesetElementList.get(i);
			DocumentElementAbs ile = typesetElement.isShow(tvs2) ? typesetElement.build(tvs2,_ops) : null;
			
			if (ile != null)
			{
				dPanel.add(ile);
				
				if (ile.getX() + ile.getWidth() > width)
					width = ile.getX() + ile.getWidth();
				if (ile.getY() + ile.getHeight() > height)
					height = ile.getY() + ile.getHeight();
			}
		}
		
		//基准坐标可以作为推移画板的手段
		if (height < tvs2.getDatum())
			height = tvs2.getDatum();
			
		if (this.getWidth() != null)
			dPanel.setWidth(this.getWidth().value(tvs));
		else
			dPanel.setWidth(width);
		
		if (this.getHeight() != null)
			dPanel.setHeight(this.getHeight().value(tvs));
		else
			dPanel.setHeight(height);
		
		resetY(tvs, dPanel);
		
		return dPanel;
	}
}
