package com.neusoft.graphene.basecomponent.document.exception;

/**
 * 文档导出异常
 * @author za-songhui001
 *
 */
public class DocumentExportException extends RuntimeException
{
	private static final long serialVersionUID = -7042201385598606176L;

	public DocumentExportException(String message)
	{
		super(message);
	}

	public DocumentExportException(String message, Exception e)
	{
		super(message, e);
	}
}
