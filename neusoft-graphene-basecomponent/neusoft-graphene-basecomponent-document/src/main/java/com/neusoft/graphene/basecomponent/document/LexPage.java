package com.neusoft.graphene.basecomponent.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.size.Paper;


public class LexPage implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Paper paper;
	
	private List<DocumentElementAbs> elementList = new ArrayList<DocumentElementAbs>();
	
	public void add(DocumentElementAbs element)
	{
		elementList.add(element);
	}
	
	public void remove(DocumentElementAbs element)
	{
		elementList.remove(element);
	}
	
	public int getElementNum()
	{
		return elementList.size();
	}
	
	public DocumentElementAbs getElement(int index)
	{
		return elementList.get(index);
	}
	
	public void clear()
	{
		elementList.clear();
	}

	public Paper getPaper()
	{
		return paper;
	}

	public void setPaper(Paper paper)
	{
		this.paper = paper;
	}
}
