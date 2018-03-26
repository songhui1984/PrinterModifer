package com.neusoft.graphene.basecomponent.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.export.Painter;


public class LexDocument implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<LexPage> lexPageList;

	private Map<String,Object> info = new HashMap<>();

	public void put(String key, Object inf)
	{
		info.put(key, inf);
	}

	public Object get(String key)
	{
		return info.get(key);
	}

	protected void addPage2List(LexPage page)
	{
		if (lexPageList == null)
			lexPageList = new ArrayList<>();
		
		lexPageList.add(page);
	}
	
	public int pageSize()
	{
		return lexPageList == null ? 0 : lexPageList.size();
	}
	
	public LexPage getPage(int index)
	{
		return (LexPage)lexPageList.get(index);
	}
	
//	public LexPage lastPage()
//	{
//		int size = pageSize();
//		return size == 0 ? null : getPage(size - 1);
//	}
//
//	public LexPage newPage()
//	{
//		LexPage page = new LexPage();
//		addPage2List(page);
//
//		return page;
//	}
	
	protected void removePage(int index)
	{
		if (index < pageSize())
			lexPageList.remove(index);
	}
	
	public void export(Painter painter, Object object, int destType, ResourceOps ops)
	{
		painter.paint(this, object, destType,ops);
	}

}
