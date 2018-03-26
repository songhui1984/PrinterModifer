package com.neusoft.graphene.basecomponent.document.element;

import com.neusoft.graphene.basecomponent.document.LexColor;
import com.neusoft.graphene.basecomponent.document.LexFont;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data//@Data相当于@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集
@EqualsAndHashCode(callSuper=false)
//1. 此注解会生成equals(Object other) 和 hashCode()方法。 
//2. 它默认使用非静态，非瞬态的属性 
//3. 可通过参数exclude排除一些属性 
//4. 可通过参数of指定仅使用哪些属性 
//5. 它默认仅使用该类中定义的属性且不调用父类的方法 
//6. 可通过callSuper=true解决上一点问题。让其生成的方法中调用父类的方法。
public class DocumentText extends DocumentElementAbs
{
	private static final long serialVersionUID = 671311254737319147L;

	private LexFont font;
	
	private String text;

	private int lineHeight;
	
	private String underline;
	
	private boolean bold;
	
	public DocumentText(String text, LexColor color, LexColor bgColor, LexFont font, int lineHeight, int width, int height, int horizontalAlign, int verticalAlign, int leftBorder, int topBorder, int rightBorder, int bottomBorder, LexColor borderColor)
	{
		setText(text);
		setColor(color);
		setBgColor(bgColor);
		setFont(font);
		setLineHeight(lineHeight);
		setHorizontalAlign(horizontalAlign);
		setVerticalAlign(verticalAlign);
		setBorder(leftBorder, topBorder, rightBorder, bottomBorder);
		setBorderColor(borderColor);
		setSize(width, height);
	}
	
	public DocumentText(String text, LexColor color, LexColor bgColor, LexFont font, int lineHeight, int width, int height, int horizontalAlign, int verticalAlign)
	{
		this(text, color, bgColor, font, lineHeight, width, height, horizontalAlign, verticalAlign, -1, -1, -1, -1, null);
	}
	
	public DocumentText()
	{
	}


	public DocumentElementAbs copy() 
	{
		DocumentText documentText = new DocumentText();
		documentText.setAll(this);
		documentText.setLineHeight(lineHeight);
		documentText.setText(text);
		documentText.setFont(font);
		return documentText;
	}

}