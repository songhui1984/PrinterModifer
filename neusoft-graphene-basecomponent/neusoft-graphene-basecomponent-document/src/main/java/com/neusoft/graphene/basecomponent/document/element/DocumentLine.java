package com.neusoft.graphene.basecomponent.document.element;


public class DocumentLine extends DocumentElementAbs
{
	private static final long serialVersionUID = 1L;
	
	public DocumentElementAbs copy() 
	{
		DocumentLine documentLine = new DocumentLine();
		documentLine.setAll(this);
		documentLine.setColor(color);
		
		return documentLine;
	}
}
