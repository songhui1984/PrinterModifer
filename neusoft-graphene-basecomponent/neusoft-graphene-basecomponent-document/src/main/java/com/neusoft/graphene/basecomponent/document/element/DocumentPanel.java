package com.neusoft.graphene.basecomponent.document.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 复合元素
 * @author za-songhui001
 *
 */
public class DocumentPanel extends DocumentElementAbs
{
	private static final long serialVersionUID = 1L;
	
	private List<DocumentElementAbs> elementList = new ArrayList<>();
	
	private Map<String,Object> additional;
	
	private int type;
	
	public void add(DocumentElementAbs element)
	{
		elementList.add(element);
	}
	
	public DocumentElementAbs getElement(int index)
	{
		return elementList.get(index);
	}
	
	public int getElementCount()
	{
		return elementList.size();
	}
	
	public void clear()
	{
		elementList.clear();
	}

	public void setAdditional(String name, Object value)
	{
		if (additional == null)
			additional = new HashMap<>();
		
		additional.put(name, value);
	}
	
	public Object getAdditional(String name)
	{
		return additional == null ? null : additional.get(name);
	}
	
	public DocumentElementAbs copy() 
	{
		DocumentPanel otherPanel = new DocumentPanel();
		otherPanel.setAll(this);
		otherPanel.setType(type);
		
		if (elementList != null)
		{
			for (DocumentElementAbs anElementList : elementList) {
				otherPanel.add((anElementList).copy());
			}
		}
		
		if (additional != null)
		{
			otherPanel.additional = new HashMap<>();
			otherPanel.additional.putAll(additional);
		}
		
		return otherPanel;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
}