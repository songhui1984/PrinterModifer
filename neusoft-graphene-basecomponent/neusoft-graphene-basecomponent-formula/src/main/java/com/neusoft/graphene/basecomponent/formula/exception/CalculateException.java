package com.neusoft.graphene.basecomponent.formula.exception;


public class CalculateException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public CalculateException(String desc)
	{
		super(desc);
	}

	public CalculateException(String desc, Exception e)
	{
		super(desc, e);
	}
}
