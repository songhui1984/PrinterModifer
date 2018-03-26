package com.neusoft.graphene.basecomponent.document.size;

import java.io.Serializable;


public class PaperA4 implements Paper, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6563693872300402291L;

	public String getPaperName()
	{
		return "A4";
	}
	
	public int getHeight()
	{
		return 2970;
	}

	public int getWidth()
	{
		return 2100;
	}
}
