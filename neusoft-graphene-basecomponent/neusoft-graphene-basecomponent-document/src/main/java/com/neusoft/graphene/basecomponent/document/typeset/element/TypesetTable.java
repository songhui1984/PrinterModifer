package com.neusoft.graphene.basecomponent.document.typeset.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.LexColor;
import com.neusoft.graphene.basecomponent.document.LexFont;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentPanel;
import com.neusoft.graphene.basecomponent.document.element.DocumentText;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetCoord;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.FormulaUtil;



public class TypesetTable extends TypesetElement
{
	private Formula header_Formula, content_Formula;
	
	private int[] col_left_width = {-1}; //列宽...左侧
	private int[] col_right_width = {}; //列宽...右侧，为空表示没有...
	private int rowHeight = 60; //行高
	private int groupNum = 0; //奇偶变色，为0时表示没有
	private String groupMode = "common";
//	private boolean groupReset = false; //到下一页时，n行背景换色是继续前一页的还是重新开始
//	private boolean appendHeader = true; //到下一页时，是否重画表头
	private LexColor evenColor;//偶数
	private LexColor oddColor;//奇数
	
	
	private LexColor headerColor = LexColor.WHITE;
	private LexColor headerBgColor = LexColor.DARK_CYAN;
	private int headerPadding = 0;
	private LexColor headerBorderColor = LexColor.WHITE;
	
	private LexColor innerBorderColor = null;
	private LexColor color, bgColor;
	
	private int measureMode = 0; //标题和内容平方差、标题平方差、内容平方差、内容最大
	
	private boolean expand = true; //每一行是否固定行高
	
	//临时计算的变量，理论上一个模板在多个并发同时进行排版的时候有可能出现冲突，所以build前面要加锁，这个问题是很容易解决的，以后有时间改一下
	private DocumentPanel dPanel;
	private int[] cols;
	
	private int getColWidth(int index, int max)
	{
		if (col_left_width == null)
			return -1;
		
		if (index < col_left_width.length)
			return col_left_width[index];
		
		if (col_right_width == null)
			return -1;
		
		if (index >= max - col_right_width.length)
			return col_right_width[index - (max - col_right_width.length)];
			
		return col_left_width[col_left_width.length - 1];
	}

	public synchronized DocumentElementAbs build(TypesetParameters typesetParameters,ResourceOps ops)
	{
		Map<String, String>  style = getStyle();
		if (style != null)
		{
			String cols = style.get("cols");
			if (cols != null)
			{
				int pos = cols.indexOf("...");
				String c1 = null, c2 = null;
				if (pos >= 0)
				{
					c1 = cols.substring(0, pos).trim();
					c2 = cols.substring(pos + 3).trim();
				}
				else
				{
					c1 = cols.trim();
					c2 = null;
				}
				
				if (c1 != null && !"".equals(c1))
				{
					if (c1.endsWith(","))
						c1 = c1.substring(0, c1.length() - 1).trim();
					
					String[] cc1 = c1.split(",");
					col_left_width = new int[cc1.length + 1];

					for (int i=0;i<cc1.length;i++)
					{
						String cc2 = cc1[i].trim();
						if ("auto".equalsIgnoreCase(cc2) || "".equalsIgnoreCase(cc2))
							col_left_width[i] = -1;
						else
							col_left_width[i] = Integer.parseInt(cc2);
					}
					
					col_left_width[cc1.length] = -1;
				}
				else
				{
					col_left_width = new int[1];
					col_left_width[0] = -1;
				}
				
				if (c2 != null && !"".equals(c2))
				{
					if (c2.startsWith(","))
						c2 = c2.substring(1).trim();
					
					String[] cc2 = c2.split(",");
					col_right_width = new int[cc2.length];
					
					for (int i=0;i<cc2.length;i++)
					{
						if ("auto".equalsIgnoreCase(cc2[i].trim()))
							col_right_width[i] = -1;
						else
							col_right_width[i] = Integer.parseInt(cc2[i].trim());
					}
				}
				else
				{
					if (c2 == null)
						col_right_width = null;
					else
						col_right_width = new int[0];
				}
			}
			
			String innerborderColor = style.get("line-color");
			if (innerborderColor != null)
			{
				innerBorderColor = getColor(innerborderColor);
			}
			
			String align = style.get("align");
			if (align != null)
			{
				String[] ss = align.split(",");
				if (ss.length >= 1)
					this.setAlign(ss[0].trim());
				if (ss.length >= 2)
					this.setVerticalAlign(ss[1].trim());
			}

			String colorStr = style.get("color");
			if (colorStr != null)
				color = getColor(colorStr);

			String bgcolorStr = style.get("bgcolor");
			if (bgcolorStr != null)
				bgColor = getColor(bgcolorStr);
			
			String measure = style.get("measure");
			if ("title".equals(measure))
				measureMode = 1;
			else if ("content".equals(measure))
				measureMode = 2;
			else if ("max".equals(measure))
				measureMode = 3;
			else if ("equal".equals(measure))
				measureMode = 4;
			
			String evenColorStr = style.get("even-color");
			if (evenColorStr != null)
				evenColor = getColor(evenColorStr);

			String groupNumStr = style.get("group-num");
			if (groupNumStr != null)
				groupNum = Integer.parseInt(groupNumStr.trim());

			String groupMode = style.get("group-mode");
			if (groupMode != null && !"".equals(groupMode.trim()))
				this.groupMode = groupMode;
		}
		
		LexFont font = this.getFont();
		
		dPanel = new DocumentPanel();
		dPanel.setBorder(valueOf(getLeftBorder(), typesetParameters), 
				valueOf(getTopBorder(), typesetParameters), valueOf(getRightBorder(), typesetParameters), 
				valueOf(getBottomBorder(), typesetParameters));
		dPanel.setBorderColor(this.getBorderColor());
		
		if (this.getX_TypesetNumber() != null)
			dPanel.setX(this.getX_TypesetNumber().value(typesetParameters));
		else
			dPanel.setX(0);
		
		if (this.getY_TypesetNumber() != null)
			dPanel.setY(typesetParameters.getDatum() + this.getY_TypesetNumber().value(typesetParameters));
		else
			dPanel.setY(typesetParameters.getDatum());

		
		String[][] header = this.header_Formula == null ? null : toArray2D(this.header_Formula.run(typesetParameters));
		String[][] content = this.content_Formula == null ? null : toArray2D(this.content_Formula.run(typesetParameters));

		double[] colsw = new double[content[0].length]; //每列最大宽度，固定列为0
		int[] colsh = new int[content[0].length];
		
		if (colsw.length > 25 && font.getSize() > 20)
			font.setSize(20);
		if (colsw.length > 18 && font.getSize() > 25)
			font.setSize(25);
		if (colsw.length > 12 && font.getSize() > 30)
			font.setSize(30);
		if (colsw.length > 8 && font.getSize() > 35)
			font.setSize(35);
		if (font.getSize() > 40)
			font.setSize(40);
		
		/*
		 * 1，以表头计算横宽分配
		 * 2，以内容计算横宽分配
		 * 3，以表头和内容计算横宽分配
		 * 
		 * 1，平方后平均值在开方，加权平均
		 * 2，最大值
		 * 3，均分
		 * 4，1或2处理后，在考虑以3的值为基准，限定最大最小值
		 */
		if (measureMode == 0 || measureMode == 1)
		{
			for (int i=0;header!=null && i<header.length;i++)
			{
				for (int j=0;header[i]!=null && j<header[i].length;j++)
				{
					int mode = getColWidth(j, colsw.length);
					if (mode < 0 && header[i][j] != null && !"".equals(header[i][j]) && !header[i][j].startsWith("@"))
					{
						if (j + 1 < colsw.length && getColspan(header, i, j) > 1) //如果是colspan>1的，就先忽略
							continue;
						
						String[] s = header[i][j].split("\n");
						for (int k=0;k<s.length;k++)
						{
							if (s[k] != null && !"".equals(s[k]))
							{
								int w = getSize(this.getFont(), s[k], ops)[0];
								colsw[j] += w * w;
								colsh[j]++;
							}
						}
					}
				}
			}
		}
		else if (measureMode == 2)
		{
			for (int i=0;content!=null && i<content.length;i++)
			{
				for (int j=0;content[i]!=null && j<content[i].length;j++)
				{
					int mode = getColWidth(j, colsw.length);
					if (mode < 0 && content[i][j] != null && !"".equals(content[i][j]))
					{
						String[] s = content[i][j].split("\n");
						for (int k=0;k<s.length;k++)
						{
							if (s[k] != null && !"".equals(s[k]))
							{
								int w = getSize(this.getFont(), s[k],ops)[0];
								colsw[j] += w * w;
								colsh[j]++;
							}
						}
					}
				}
			}
		}		
		else if (measureMode == 3)
		{
			for (int i=0;content!=null && i<content.length;i++)
			{
				for (int j=0;content[i]!=null && j<content[i].length;j++)
				{
					int mode = getColWidth(j, colsw.length);
					if (mode < 0 && content[i][j] != null && !"".equals(content[i][j]))
					{
						String[] s = content[i][j].split("\n");
						for (int k=0;k<s.length;k++)
						{
							if (s[k] != null && !"".equals(s[k]))
							{
								int w = getSize(this.getFont(), s[k], ops)[0];
								if (w * w > colsw[j]) colsw[j] = w * w;
								colsh[j] = 1;
							}
						}
					}
				}
			}
		}
		else if (measureMode == 4)
		{
			for (int i=0;content!=null && i<content.length;i++)
			{
				for (int j=0;content[i]!=null && j<content[i].length;j++)
				{
					int mode = getColWidth(j, colsw.length);
					if (mode < 0)
					{
						colsw[j]++;
						colsh[j]++;
					}
				}
			}
		}
		
		double colsm = 0;
		for (int j=0;j<colsw.length;j++)
		{
			if (colsh[j] > 0)
				colsw[j] = Math.sqrt(colsw[j] / colsh[j]);
			else
				colsw[j] = 0;
			
			colsm += colsw[j];
		}
		
		//设定最大值和最小值的范围，对特别宽和特别窄的行作限制（这里说的都是单列，不是colspan后的）
		colsm = colsm / colsw.length;
		for (int j=0;j<colsw.length;j++)
		{
			if (colsw[j] > colsm * 1.7f)
				colsw[j] = colsm * 1.7f;
			else if (colsw[j] < colsm * 0.6f)
				colsw[j] = colsm * 0.6f;
		}
		
		cols = new int[colsw.length];
		
		int max = this.getWidth() != null ? this.getWidth().value(typesetParameters) : typesetParameters.getPaper().getBody().getWidth().value(typesetParameters);
		int totalw = 0;

		for (int i=0;i<cols.length;i++)
		{
			totalw += colsw[i];
			
			int mode = getColWidth(i, cols.length);
			if (mode > 0)
			{
				cols[i] = mode;
				max -= mode;
			}
		}
		
		for (int i=0;i<cols.length;i++)
		{
			if (cols[i] == 0)
				cols[i] = (int)Math.ceil(colsw[i] * max / totalw);
		}
		
		dPanel.setAdditional("title", headerOf(pack(typesetParameters), header,ops));
		insertTable(pack(typesetParameters), dPanel, content, false, color != null ? color : this.getColor(), bgColor != null ? bgColor : this.getBgColor(), innerBorderColor == null ? -1 : 0, innerBorderColor, this.getAlign(), this.getVerticalAlign(),ops);
		
			
		if (this.getWidth() != null)
			dPanel.setWidth(this.getWidth().value(typesetParameters));
		
		if (this.getHeight() != null)
			dPanel.setHeight(this.getHeight().value(typesetParameters));
		
		resetY(typesetParameters, dPanel);
		
		return dPanel;
	}
	
	private int[] getSize(LexFont font, String text,ResourceOps ops)
	{
		if (text == null)
			return new int[] {0, 0};
		
		int w = 0, h = 0;
		
		String s[] = text.split("\n");
		for (int i=0;i<s.length;i++)
		{
			TypesetCoord tc = TypesetText.getTextDimension().getSize(this.getFont(), s[i]);
			if (tc.x > w)
				w = tc.x;
			h += tc.y;
		}
		
		return new int[] {w, h};
	}
	
	/**
	 * 获取该blank的colspan
	 * @param c
	 * @param i1
	 * @param j1
	 * @return
	 */
	private int getColspan(String[][] c, int i1, int j1)
	{
		for (int i=i1;i<c.length;i++)
		{
			if (i > i1 && c[i][j1] != null)
				break;
			
			for (int j=j1;j<c[0].length;j++)
			{
				if (i == i1 && j == j1)
					continue;
				
				if (c[i][j] != null)
				{
					if (c[i][j].startsWith("@"))
					{
						String[] xy = c[i][j].substring(1).split(",");
						int m = Integer.parseInt(xy[0]);
						int n = Integer.parseInt(xy[1]);
						if (m == i1 && n == j1)
							return j - j1 + 1;
					}
					break;
				}
			}
		}
		
		return 1;
	}
	
	private int getRowspan(String[][] c, int i1, int j1)
	{
		for (int j=j1;j<c[0].length;j++)
		{
			if (j > j1 && c[i1][j] != null)
				break;
			
			for (int i=i1;i<c.length;i++)
			{
				if (i == i1 && j == j1)
					continue;
				
				if (c[i][j] != null)
				{
					if (c[i][j].startsWith("@"))
					{
						String[] xy = c[i][j].substring(1).split(",");
						int m = Integer.parseInt(xy[0]);
						int n = Integer.parseInt(xy[1]);
						if (m == i1 && n == j1)
							return i - i1 + 1;
					}
					break;
				}
			}
		}
		
		return 1;
	}
	
	private DocumentPanel headerOf(TypesetParameters tvs, String[][] content,ResourceOps ops)
	{
		DocumentPanel dPanel = new DocumentPanel();
		insertTable(tvs, dPanel, content, true, headerColor, headerBgColor, headerPadding, headerBorderColor, DocumentText.ALIGN_CENTER, DocumentText.ALIGN_MIDDLE,ops);
		
		return dPanel;
	}
	
	private void insertTable(TypesetParameters tvs, DocumentPanel dPanel, String[][] content,
			boolean header, LexColor color, LexColor bgColor, int tableLine, LexColor borderColor, 
			int align, int verticalAlign,ResourceOps ops)
	{
		if (content == null || content.length == 0)
			return;

		int x = 0, y = 0;
		int width = 0, height = 0;
			
		TypesetText[][] tt = new TypesetText[content.length][content[0].length];
		for (int i=0;i<content.length;i++)
		{
			int lineH = rowHeight;
			
			for (int j=0;j<content[i].length;j++)
			{
				String t = content[i][j];
				if (t != null && !t.startsWith("@"))
				{
					int ww = 0;
					int cs = getColspan(content, i, j);
					int rs = getRowspan(content, i, j);
					for (int k=0;k<cs;k++)
						ww += cols[j + k];
					
					if (expand && rs == 1)
					{
						Object[] r = TypesetText.format(t, this.getFont(), ww, -1,ops);
						int cy = ((Integer)r[0]).intValue();
						
						if (lineH < cy)
							lineH = cy;
					}
					
					TypesetText tx = new TypesetText();
					tx.setFixed(false);
					tx.setAlign(align);
					tx.setVerticalAlign(verticalAlign);
					tx.setValue(FormulaUtil.formulaOf("'" + t + "'"));
					tx.setColor(color);
					tx.setBgColor(bgColor);
					tx.setFont(this.getFont());
					tx.setX(x);
					tx.setY(y);
					tx.setWidth(ww);
					tx.setHeight(lineH);
					tx.setBorderColor(borderColor);
					tx.setLeftBorder(j == 0 ? -1 : tableLine);
					tx.setTopBorder(i == 0 ? -1 : tableLine);
					tx.setRightBorder(-1);
					tx.setBottomBorder(-1);
					
					tt[i][j] = tx;
				}
				
				x += cols[j];
				if (x > width)
					width = x;
			}

			if (lineH > rowHeight) //本行拉伸到指定位置
			{
				for (int j=0;j<content[i].length;j++)
				{
					if (tt[i][j] != null)
						tt[i][j].setHeight(lineH);
				}
			}

			y += lineH;
			if (y > height)
				height = y;
			
			x = 0;
			for (int j=0;j<content[i].length;j++) //处理前面的跨行到本行的格子
			{
				x += cols[j];
				if (x > width)
					width = x;

				if (content[i][j] != null && content[i][j].startsWith("@"))
				{
					String[] xy = content[i][j].substring(1).split(",");
					int m = Integer.parseInt(xy[0]);
					int n = Integer.parseInt(xy[1]);
					tt[m][n].setWidth(x - tt[m][n].getX_TypesetNumber().value(tvs));
					tt[m][n].setHeight(y - tt[m][n].getY_TypesetNumber().value(tvs));
				}
			}
			
			x = 0;
		}

		for (int i=0;i<content.length;i++)
		{
			for (int j=0;j<content[i].length;j++)
			{
				if (tt[i][j] != null)
				{
					DocumentElementAbs dt = tt[i][j].build(tvs,ops);
					if (dt instanceof DocumentPanel)
						dt.setSplit(false);
					dPanel.add(dt);
					
					if (!header && groupNum > 0)
					{
						if ("single".equalsIgnoreCase(groupMode))
						{
							if (i % groupNum == groupNum - 1)
							{
								if (evenColor != null)
									dt.setBgColor(evenColor);
							}
							else
							{
								if (oddColor != null)
									dt.setBgColor(oddColor);
							}
						}
						else
						{
							int xx = i / groupNum;
							if (xx % 2 == 0 && oddColor != null)
								dt.setBgColor(oddColor);
							else if (xx % 2 == 1 && evenColor != null)
								dt.setBgColor(evenColor);
						}
					}
				}
			}
		}

		dPanel.setWidth(width);
		dPanel.setHeight(height);
	}
	
	public String[] toArray(Object obj)
	{
		if (obj == null)
			return null;
		if (obj instanceof String[])
			return (String[])obj;
		if (obj instanceof List)
		{
			List l1 = (List)obj;
			if (l1.isEmpty())
				return null;
			String[] s = new String[l1.size()];
			for (int i=0;i<s.length;i++)
			{
				Object val = l1.get(i);
				s[i] = val == null ? null : val.toString();
			}
			return s;
		}
		return null;
	}
	
	public String[][] toArray2D(Object obj)
	{
		if (obj == null)
			return null;
		if (obj instanceof String[][])
			return (String[][])obj;
		if (obj instanceof List)
		{
			List l1 = (List)obj;
			if (l1.isEmpty())
				return null;
			List l2 = new ArrayList();
			
			int max = 0;
			for (int i=0;i<l1.size();i++)
			{
				String[] s2 = toArray(l1.get(i));
				if (s2 == null)
					s2 = new String[0];
				if (s2.length > max)
					max = s2.length;
				l2.add(s2);
			}
			
			String[][] s = new String[l2.size()][max];
			for (int i=0;i<l2.size();i++)
			{
				String[] s2 = (String[])l2.get(i);
				for (int j=0;j<s2.length;j++)
					s[i][j] = s2[j];
			}
			return s;
		}
		return null;
	}

	public void setHeader(Formula header) {
		this.header_Formula = header;
	}

	public void setContent(Formula content) {
		this.content_Formula = content;
	}

	public void setMeasureMode(int measureMode)
	{
		this.measureMode = measureMode;
	}


}
