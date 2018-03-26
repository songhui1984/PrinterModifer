package com.neusoft.graphene.basecomponent.document.exception;

class TypesetException extends RuntimeException
{
	private static final long	serialVersionUID	= 1L;

	TypesetException(String message, Exception e)
	{
		super(message, e);
	}
}
