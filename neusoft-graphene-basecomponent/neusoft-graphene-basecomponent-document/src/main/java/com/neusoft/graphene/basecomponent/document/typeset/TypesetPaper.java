package com.neusoft.graphene.basecomponent.document.typeset;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.graphene.basecomponent.document.size.Paper;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetPanel;

public class TypesetPaper
{
	private String name;

	private Paper paper;//A4

	private TypesetPanel body;

	private List<TypesetPanel> typesetPanelList = new ArrayList<>();
	
	public void addTemplate(TypesetPanel template)
	{
		typesetPanelList.add(template);
	}
	
	public int getTypesetPanelListSize()
	{
		return typesetPanelList.size();
	}
	
	public TypesetPanel getTemplate(int index)
	{
		return typesetPanelList.get(index);
	}
	
	public void setBody(TypesetPanel body)
	{
		this.body = body;
	}

	public TypesetPanel getBody()
	{
		return body;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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
