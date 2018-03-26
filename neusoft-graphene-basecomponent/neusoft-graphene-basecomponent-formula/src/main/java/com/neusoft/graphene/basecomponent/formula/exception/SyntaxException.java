package com.neusoft.graphene.basecomponent.formula.exception;

public class SyntaxException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public SyntaxException(String descr)
	{
		super(descr);
	}
	
	public SyntaxException()
	{
	}
}
