package com.neusoft.graphene.basecomponent.document;

import java.io.Serializable;

public class LexFont implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private LexFontFamily fontFamily;
	
	private float size;
	
	public LexFont(LexFontFamily fontFamily, float size)
	{
		this.fontFamily = fontFamily;
		this.size = size;
	}
	

	public LexFontFamily getFamily()
	{
		return fontFamily;
	}

	public void setFamily(LexFontFamily fontFamily)
	{
		this.fontFamily = fontFamily;
	}

	public float getSize()
	{
		return size;
	}
	
	public void setSize(float size)
	{
		this.size = size;
	}
	
//	public String getLexFontFamilyName()
//	{
//		return fontFamily.getName();
//	}
	
}
