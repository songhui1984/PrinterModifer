package com.neusoft.graphene.basecomponent.document.element;

public class DocumentRect extends DocumentElementAbs
{
	private static final long serialVersionUID = 1L;
	
	public DocumentElementAbs copy() 
	{
		DocumentRect documentRect = new DocumentRect();
		documentRect.setAll(this);
		documentRect.setColor(color);
		
		return documentRect;
	}
}
