package com.neusoft.graphene.basecomponent.document.typeset.element;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.LexColor;
import com.neusoft.graphene.basecomponent.document.LexFont;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentText;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetNumber;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;

import lombok.Data;

@Data
public abstract class TypesetElement {
	public static final int MODE_COMMON = 0;
	public static final int MODE_RESET_AT_FINAL = 1;

	protected TypesetNumber x_TypesetNumber;
	protected TypesetNumber y_TypesetNumber;
	protected TypesetNumber width_TypesetNumber;
	protected TypesetNumber height_TypesetNumber;

	protected LexFont font;
	protected int lineHeight;

	protected int mode = TypesetElement.MODE_COMMON;

	protected LexColor color;
	protected LexColor bgColor;
	protected LexColor borderColor;

	protected Formula value;

	protected TypesetNumber topBorder;
	protected TypesetNumber bottomBorder;
	protected TypesetNumber leftBorder;
	protected TypesetNumber rightBorder;

	protected int[] margin = null;

	public static final int ALIGN_LEFT = DocumentText.ALIGN_LEFT;
	public static final int ALIGN_CENTER = DocumentText.ALIGN_CENTER;
	public static final int ALIGN_RIGHT = DocumentText.ALIGN_RIGHT;

	public static final int ALIGN_TOP = DocumentText.ALIGN_TOP;
	public static final int ALIGN_MIDDLE = DocumentText.ALIGN_MIDDLE;
	public static final int ALIGN_BOTTOM = DocumentText.ALIGN_BOTTOM;

	protected int align = ALIGN_LEFT;
	protected int valign = ALIGN_MIDDLE;

	protected Map<String, String> style;
	protected String underline;

	protected Formula condition;

	// 内容过长自动扩展
	protected boolean fixed = false;

	protected boolean split = true;

	public abstract DocumentElementAbs build(TypesetParameters varSet, ResourceOps ops);

	public static LexColor getColor(String color) {
		if (color == null || "".equals(color.trim()))
			return LexColor.WHITE;

		else if (color.startsWith("#")) {
			int r = tranHex(color.substring(1, 3));
			int g = tranHex(color.substring(3, 5));
			int b = tranHex(color.substring(5, 7));
			return new LexColor(r, g, b);
		} else if ("red".equalsIgnoreCase(color))
			return LexColor.RED;
		else if ("black".equalsIgnoreCase(color))
			return LexColor.BLACK;
		else if ("blue".equalsIgnoreCase(color))
			return LexColor.BLUE;
		else if ("cyan".equalsIgnoreCase(color))
			return LexColor.CYAN;
		else if ("white".equalsIgnoreCase(color))
			return LexColor.WHITE;
		else if ("darkgreen".equalsIgnoreCase(color))
			return LexColor.DARK_GREEN;
		else if ("darkcyan".equalsIgnoreCase(color))
			return LexColor.DARK_CYAN;
		else if ("gray".equalsIgnoreCase(color))
			return LexColor.GRAY;

		return LexColor.WHITE;
	}

	public boolean isShow(TypesetParameters vs) {
		if (condition == null)
			return true;

		try {
			Object v = condition.run(vs);

			if (v != null)
				return Value.booleanOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public void resetY(TypesetParameters tvs, DocumentElementAbs element) {
		if (element.getY() + element.getHeight() > tvs.getY()) {
			tvs.setY(element.getY() + element.getHeight());
		}
	}

	public TypesetParameters pack(TypesetParameters tvs) {
		TypesetParameters tvs2 = new TypesetParameters(tvs);
		tvs2.setPaper(tvs.getPaper());

		return tvs2;
	}

	public void setStyle(String styleStr) {
		if (styleStr != null && !"".equals(styleStr.trim())) {
			style = new HashMap<String, String>();

			String[] css = styleStr.split(";");
			for (int i = 0; i < css.length; i++) {
				String[] ss = css[i].split(":");
				style.put(ss[0].trim(), ss[1].trim());
			}
		}
	}

	public Map<String, String> getStyle() {
		return style;
	}

	public int valueOf(TypesetNumber num, TypesetParameters tvs) {
		if (num == null)
			return -1;

		return num.value(tvs);
	}

	public void setX(int x) {
		this.x_TypesetNumber = new TypesetNumber(x);
	}

	public void setX(String x) {
		this.x_TypesetNumber = TypesetNumber.numberOf(x);
	}

	public void setY(int y) {
		this.y_TypesetNumber = new TypesetNumber(y);
	}

	public void setY(String y) {
		this.y_TypesetNumber = TypesetNumber.numberOf(y);
	}

	public void setHeight(int height) {
		this.height_TypesetNumber = new TypesetNumber(height);
	}

	public void setHeight(String height) {
		this.height_TypesetNumber = TypesetNumber.numberOf(height);
	}

	public TypesetNumber getHeight() {
		return height_TypesetNumber;
	}

	public TypesetNumber getWidth() {
		return width_TypesetNumber;
	}

	public void setWidth(int width) {
		this.width_TypesetNumber = new TypesetNumber(width);
	}

	public void setWidth(String width) {
		this.width_TypesetNumber = TypesetNumber.numberOf(width);
	}

	public void setWidth(TypesetNumber width) {
		this.width_TypesetNumber = width;
	}

	public void setAlign(int align) {
		this.align = align;
	}

	public void setAlign(String str) {
		if ("left".equalsIgnoreCase(str))
			this.setAlign(TypesetElement.ALIGN_LEFT);
		else if ("center".equalsIgnoreCase(str))
			this.setAlign(TypesetElement.ALIGN_CENTER);
		else if ("right".equalsIgnoreCase(str))
			this.setAlign(TypesetElement.ALIGN_RIGHT);
	}

	public int getVerticalAlign() {
		return valign;
	}

	public void setVerticalAlign(int valign) {
		this.valign = valign;
	}

	public void setVerticalAlign(String str) {
		if ("top".equalsIgnoreCase(str))
			this.setVerticalAlign(TypesetElement.ALIGN_TOP);
		else if ("middle".equalsIgnoreCase(str))
			this.setVerticalAlign(TypesetElement.ALIGN_MIDDLE);
		else if ("bottom".equalsIgnoreCase(str))
			this.setVerticalAlign(TypesetElement.ALIGN_BOTTOM);
	}

	public TypesetNumber getBottomBorder() {
		return bottomBorder;
	}

	public void setBottomBorder(int bottomBorder) {
		this.bottomBorder = new TypesetNumber(bottomBorder);
	}

	public void setBottomBorder(String bottomBorder) {
		this.bottomBorder = TypesetNumber.numberOf(bottomBorder);
	}

	public TypesetNumber getLeftBorder() {
		return leftBorder;
	}

	public void setLeftBorder(int leftBorder) {
		this.leftBorder = new TypesetNumber(leftBorder);
	}

	public void setLeftBorder(String leftBorder) {
		this.leftBorder = TypesetNumber.numberOf(leftBorder);
	}

	public TypesetNumber getRightBorder() {
		return rightBorder;
	}

	public void setRightBorder(int rightBorder) {
		this.rightBorder = new TypesetNumber(rightBorder);
	}

	public void setRightBorder(String rightBorder) {
		this.rightBorder = TypesetNumber.numberOf(rightBorder);
	}

	public TypesetNumber getTopBorder() {
		return topBorder;
	}

	public void setTopBorder(int topBorder) {
		this.topBorder = new TypesetNumber(topBorder);
	}

	public void setTopBorder(String topBorder) {
		this.topBorder = TypesetNumber.numberOf(topBorder);
	}

	private static int tranHex(String hex) {
		int[] r1 = new int[2];
		for (int i = 0; i < 2; i++) {
			char c1 = hex.charAt(i);
			if (c1 >= '0' && c1 <= '9')
				r1[i] = c1 - '0';
			else if (c1 >= 'A' && c1 <= 'F')
				r1[i] = c1 - 'A' + 10;
			else if (c1 >= 'a' && c1 <= 'f')
				r1[i] = c1 - 'a' + 10;
		}

		return r1[0] * 16 + r1[1];
	}

}
