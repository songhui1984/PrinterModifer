package com.neusoft.graphene.basecomponent.formula.analyse;

import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;


/**
 * 文本处理
 * 
 * @author za-songhui001
 *
 */
public class TextUtil
{
	/**
	 * 去除代码中的注释
	 */
	public static String cutComment(String text)
	{
		StringBuffer buf = new StringBuffer();

		int len = text.length();
		int begin = 0;
		
		for (int i = 0; i < len; i++)
		{
			char c = text.charAt(i);
			
			if (c == '/')
			{
				char c1 = text.charAt(i + 1);
				if (c1 == '/') //如果是//的注释
				{
					buf.append(text.substring(begin, i));
					
					i = find(text, i + 1, '\n');
					begin = i; //单行注释结尾的回车不需要跳过，保留，否则很多跟在代码后的注释会导致正常的回车也没有了
					
					if (i < 0) break;
				}
				else if (c1 == '*') //如果是/*的注释
				{
					buf.append(text.substring(begin, i));

					i = find(text, i + 2, "*/");
					begin = i < 0 ? i : i + 2;
					
					if (i < 0)  break;

					i++; //注释的结尾符号是两个字节，全部都要跳过，只靠循环的i++还差一个，这里要补一下。
				}
			}
			else if (c == '\"' || c == '\'') //如果是字符串，或单引号圈起来的
			{
				i = findInString(text, i + 1, c);
			}
		}

		if (begin >= 0)
			buf.append(text.substring(begin));
		
		return buf.toString();
	}
	
	public static int find(String text, int begin, char f)
	{
		int len = text.length();
		for (int i = begin; i < len; i++)
		{
			if (text.charAt(i) == f)
				return i;
		}
		
		return -1;
	}
	
	public static int find(String text, int begin, String f)
	{
		int s = f.length();
		int len = text.length() - s + 1;
		char c = f.charAt(0);
		for (int i = begin; i < len; i++)
		{
			if (text.charAt(i) == c)
			{
				if (f.equals(text.substring(i, i + s)))
					return i;
			}
		}
		
		return -1;
	}
	
	public static int findInString(String text, int begin, char quote)
	{
		int len = text.length();
		for (int i = begin; i < len; i++)
		{
			char c = text.charAt(i);
			
			if (c == '\\')
				i++;
			else if (c == quote)
				return i;
		}
		
		throw new SyntaxException("缺少结束的引号或单引号");
	}


}
