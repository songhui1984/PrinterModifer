package com.neusoft.graphene.basecomponent.document.typeset.element;

import com.neusoft.graphene.basecomponent.document.element.DocumentPanel;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;

public class TypesetLayout extends TypesetPanel
{
	public static final int MODE_TEXT			= 1;
	public static final int BASELINE_TOP		= 1;//上部 即不同大小的文字怎么对齐 
	public static final int BASELINE_CENTER		= 2;//中部 即不同大小的文字怎么对齐 
	public static final int BASELINE_BOTTOM		= 3;//底部 即不同大小的文字怎么对齐 
	
	int baseLine = BASELINE_BOTTOM; //3 底部 2 中部 1 上部，即不同大小的文字怎么对齐 
	
	public TypesetLayout(int mode)
	{
		this.mode = mode;
	}

	public DocumentElementAbs build(TypesetParameters tvs,ResourceOps ops)
	{
		boolean fixed = isFixed();
		int bodyWidth = tvs.getPaper().getBody().getWidth().value(tvs);
		
		int width = this.getWidth() == null ? 0 : this.getWidth().value(tvs);
		int height = this.getHeight() == null ? 0 : this.getHeight().value(tvs);

		DocumentPanel documentPanel = new DocumentPanel();
		documentPanel.setType(1);
//		dPanel.setAdditional("type", "layout");
		documentPanel.setColor(color);
		documentPanel.setBgColor(this.getBgColor());
		documentPanel.setX(this.getX_TypesetNumber() == null ? 0 : this.getX_TypesetNumber().value(tvs));
		documentPanel.setY(tvs.getDatum() + (this.getY_TypesetNumber() == null ? 0 : this.getY_TypesetNumber().value(tvs)));
		documentPanel.setHorizontalAlign(this.getAlign());
		documentPanel.setVerticalAlign(this.getVerticalAlign());
		documentPanel.setBorder(valueOf(getLeftBorder(), tvs), valueOf(getTopBorder(), tvs), valueOf(getRightBorder(), tvs), valueOf(getBottomBorder(), tvs));
		documentPanel.setBorderColor(this.getBorderColor());

		TypesetParameters tvs2 = pack(tvs);
		tvs2.setStreamY(tvs.getStreamY() + documentPanel.getY());
		
		int x = 0, y = 0, h = 0, p = 0;
		for (int i=0;i<typesetElementList.size();i++)
		{
			TypesetElement typesetElement = typesetElementList.get(i);
			if (typesetElement.getFont() == null)
				typesetElement.setFont(super.getFont());
			if (typesetElement.getColor() == null)
				typesetElement.setColor(super.getColor());
			if (typesetElement.getWidth() == null)
				typesetElement.setWidth(super.getWidth());
				
			tvs2.set("text_x", new Integer(x));
			tvs2.set("text_y", new Integer(y));
			
			DocumentElementAbs documentElementAbs = typesetElement.build(tvs2,ops);
			
			if (documentElementAbs == null)
				continue;
			if (documentElementAbs instanceof DocumentPanel)
			{
				DocumentPanel dpanel_tmp = (DocumentPanel)documentElementAbs;
				int size = dpanel_tmp.getElementCount();
				for (int index=0;index<size;index++)
				{
					DocumentElementAbs e = dpanel_tmp.getElement(index);
					if (x + e.getWidth() > width || x + e.getWidth() > bodyWidth)
					{
						resetLastLine(documentPanel, y, h);
						
						y += h;
						e.setLocation(0, y);
						
						x = e.getWidth();
						h = e.getHeight();
						if (p < x)
							p = x;
					}
					else
					{
						e.setLocation(x, y);
						
						x += e.getWidth();
						if (p < x)
							p = x;
						if (h < e.getHeight())
							h = e.getHeight();
					}
					documentPanel.add(e);
				}
			}
			else
			{
				if (x + documentElementAbs.getWidth() > width || x + documentElementAbs.getWidth() > bodyWidth)
				{
					resetLastLine(documentPanel, y, h);
					
					y += h;
					documentElementAbs.setLocation(0, y);
					
					x = documentElementAbs.getWidth();
					h = documentElementAbs.getHeight();
					if (p < x)
						p = x;
				}
				else
				{
					documentElementAbs.setLocation(x, y);
					
					x += documentElementAbs.getWidth();
					if (p < x)
						p = x;
					if (h < documentElementAbs.getHeight())
						h = documentElementAbs.getHeight();
				}
				documentPanel.add(documentElementAbs);
			}
		}
		
		resetLastLine(documentPanel, y, h);
		y += h;
		
		//基准坐标可以作为推移画板的手段
		if (height < tvs2.getDatum())
			height = tvs2.getDatum();
			
		if (!fixed)
		{
			if (width < p)
				width = p;
			if (height < y)
				height = y;
		}

		int s = documentPanel.getElementCount();
		for (int i=0;i<s;i++)
		{
			DocumentElementAbs e = documentPanel.getElement(i);
			
			if (this.getAlign() == DocumentElementAbs.ALIGN_CENTER)
			{
				e.setX((width - p) / 2 + e.getX());
			}
			else if (this.getAlign() == DocumentElementAbs.ALIGN_RIGHT)
			{
				e.setX(width - p + e.getX());
			}

			if (this.getVerticalAlign() == DocumentElementAbs.ALIGN_MIDDLE)
			{
				e.setY((height - y) / 2 + e.getY());
			}
			else if (this.getAlign() == DocumentElementAbs.ALIGN_BOTTOM)
			{
				e.setY(height - y + e.getY());
			}
		}
		
		documentPanel.setSize(width, height);
		resetY(tvs, documentPanel);

		return documentPanel;
	}
	
	private void resetLastLine(DocumentPanel dPanel, int y, int h)
	{
		if (baseLine == TypesetLayout.BASELINE_BOTTOM)
		{
			for (int k=dPanel.getElementCount()-1;k>=0;k--) //下对齐
			{
				DocumentElementAbs l = dPanel.getElement(k);
				if (l.getY() == y)
					l.setY(y + h - l.getHeight());
				else
					break;
			}
		}
		else if (baseLine == TypesetLayout.BASELINE_CENTER)
		{
			for (int k=dPanel.getElementCount()-1;k>=0;k--) //下对齐
			{
				DocumentElementAbs l = dPanel.getElement(k);
				if (l.getY() == y)
					l.setY(y + (h - l.getHeight()) / 2);
				else
					break;
			}
		}
	}

	public int getBaseLine()
	{
		return baseLine;
	}

	public void setBaseLine(int baseLine)
	{
		this.baseLine = baseLine;
	}
}
