package com.neusoft.graphene.basecomponent.document.element;

import java.util.HashMap;
import java.util.Map;



public class DocumentImage extends DocumentElementAbs
{
	private static final long serialVersionUID = -6882380377016506076L;
	
	public static final int TYPE_FILE	= 1; //文件
	public static final int TYPE_PATH	= 2; //文件路径
	public static final int TYPE_SRC	= 3; //文件相对路径
	public static final int TYPE_URL	= 4; //url
	public static final int TYPE_BIN	= 5; //byte[]
	public static final int TYPE_OTHER	= 99;

	private boolean sign = false;

	private Map<Integer,Object> imageMap = new HashMap<>();
	
	public void addImageSource(Object image, int type)
	{
		imageMap.put(type, image);
	}

	public Object getImageByType(int type)
	{
		return imageMap.get(type);
	}

	public boolean hasTypeImage(int type)
	{
		return imageMap.containsKey(type);
	}

	public boolean isSign()
	{
		return sign;
	}

	public void setSign(boolean sign)
	{
		this.sign = sign;
	}

	public DocumentElementAbs copy()
	{
		DocumentImage documentImage = new DocumentImage();
		documentImage.imageMap = imageMap;
		documentImage.sign = sign;
		
		documentImage.setAll(this);
		
		return documentImage;
	}
}
