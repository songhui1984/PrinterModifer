package com.neusoft.graphene.basecomponent.formula.exception;

public class DataNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public DataNotFoundException(String word)
	{
		super(word);
	}

	public DataNotFoundException(String word, Exception e)
	{
		super(word, e);
	}
}
