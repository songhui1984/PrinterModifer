package com.neusoft.graphene.basecomponent.document.element;


public class DocumentReset extends DocumentElementAbs
{
	private static final long serialVersionUID = 1L;

	private boolean newPageFlag;
	
	public DocumentReset(boolean newPageFlag)
	{
		this.newPageFlag = newPageFlag;
	}
	
//	public boolean isNewPage()
//	{
//		return newPageFlag;
//	}
//
//	public void setNewPage(boolean newPageFlag)
//	{
//		this.newPageFlag = newPageFlag;
//	}
	
	public DocumentElementAbs copy() 
	{
		DocumentReset documentReset = new DocumentReset(newPageFlag);
		documentReset.setAll(this);
		
		return documentReset;
	}
}
