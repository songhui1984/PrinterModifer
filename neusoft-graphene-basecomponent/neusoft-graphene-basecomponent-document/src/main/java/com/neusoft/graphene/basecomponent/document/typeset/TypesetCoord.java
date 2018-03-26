package com.neusoft.graphene.basecomponent.document.typeset;

import java.io.Serializable;

public class TypesetCoord implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public int x;
	public int y;


	public TypesetCoord(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}
