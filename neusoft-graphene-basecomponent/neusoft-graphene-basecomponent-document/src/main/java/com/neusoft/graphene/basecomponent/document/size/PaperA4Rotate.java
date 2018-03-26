package com.neusoft.graphene.basecomponent.document.size;

import java.io.Serializable;


public class PaperA4Rotate implements Paper, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5147683584905483505L;

	public String getPaperName()
	{
		return "A4_rotate";
	}

	public int getHeight()
	{
		return 2100;
	}


	public int getWidth()
	{
		return 2970;
	}
}