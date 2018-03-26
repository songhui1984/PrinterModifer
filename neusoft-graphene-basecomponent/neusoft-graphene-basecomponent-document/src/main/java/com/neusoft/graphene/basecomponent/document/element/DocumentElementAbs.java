package com.neusoft.graphene.basecomponent.document.element;

import java.io.Serializable;

import com.neusoft.graphene.basecomponent.document.LexColor;
import com.neusoft.graphene.basecomponent.formula.Formula;

import lombok.Data;



@Data
public abstract class DocumentElementAbs implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static int ALIGN_CENTER 			= 0x1;
	public static int ALIGN_LEFT 			= 0x2;
	public static int ALIGN_RIGHT			= 0x3;
	public static int ALIGN_MIDDLE			= 0x10;
	public static int ALIGN_TOP				= 0x20;
	public static int ALIGN_BOTTOM			= 0x30;

	int x;
	int y;
	int width;
	int height;

	
	int topBorder = -1;
	int bottomBorder = -1;
	int leftBorder = -1;
	int rightBorder = -1;

	int horizontalAlign = DocumentElementAbs.ALIGN_CENTER;
	int verticalAlign = DocumentElementAbs.ALIGN_MIDDLE;
	
	Formula resetAtFinal_Formula;
	
	
	LexColor color = LexColor.BLACK;
	LexColor bgColor = null;
	LexColor borderColor = LexColor.BLACK;


	boolean split	= true;//跨页时是否自动分割
	boolean extend	= true;//内容超出时是否自动向下伸展
	
	public abstract DocumentElementAbs copy();
	
	public void setLocation(int x, int y)
	{
		setX(x);
		setY(y);
	}

	public void setSize(int width, int height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	
	public void setBorder(int[] border)
	{
		setLeftBorder(border[0]);
		setTopBorder(border[1]);
		setRightBorder(border[2]);
		setBottomBorder(border[3]);
	}
	
	public void setBorder(int left, int top, int right, int bottom)
	{
		setLeftBorder(left);
		setRightBorder(right);
		setTopBorder(top);
		setBottomBorder(bottom);
	}

	
	
	public void setAll(DocumentElementAbs otherElement)
	{
		setX(otherElement.getX());
		setY(otherElement.getY());
		setWidth(otherElement.getWidth());
		setHeight(otherElement.getHeight());

		setColor(otherElement.getColor());
		setBgColor(otherElement.getBgColor());
		
		setBorderColor(otherElement.getBorderColor());
		setBottomBorder(otherElement.getBottomBorder());
		setLeftBorder(otherElement.getLeftBorder());
		setRightBorder(otherElement.getRightBorder());
		setTopBorder(otherElement.getTopBorder());
		
		setHorizontalAlign(otherElement.getHorizontalAlign());
		setVerticalAlign(otherElement.getVerticalAlign());
		
		setSplit(otherElement.canSplit());
		setExtend(otherElement.canExtend());
	}
	
	
	/**
	 * 跨页时是否自动分割
	 */
	public boolean canSplit()
	{
		return split;
	}

	public void setSplit(boolean split)
	{
		this.split = split;
	}

	/**
	 * 内容超出时是否自动向下伸展
	 */
	private boolean canExtend()
	{
		return extend;
	}

	public void setExtend(boolean extend)
	{
		this.extend = extend;
	}
}