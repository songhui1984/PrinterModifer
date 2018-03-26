package com.neusoft.graphene.basecomponent.document.typeset.element.sheet;

import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetElement;

/**
 * Created by lerrain on 2017/5/1.
 */
public class SheetBlank
{
    private String text;

    private int align = TypesetElement.ALIGN_LEFT | TypesetElement.ALIGN_MIDDLE;

    private int col = 1;
    private int row = 1;

    private int x = -1, y = -1;

    public SheetBlank()
    {
    }

    public boolean isVirtual() //跨行跨列的格子，在右下角虚拟一个sheetblank标识左上角的位置
    {
        return x >= 0 || y >= 0;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getCol()
    {
        return col;
    }

    public void setCol(int col)
    {
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

//    public int getAlign()
//    {
//        return align & 0x0F;
//    }
//
//    public int getVerticalAlign()
//    {
//        return align & 0xF0;
//    }
}
