package com.neusoft.graphene.basecomponent.formula.analyse;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.statement.StatementBreak;
import com.neusoft.graphene.basecomponent.formula.statement.StatementContinue;
import com.neusoft.graphene.basecomponent.formula.statement.StatementFor;
import com.neusoft.graphene.basecomponent.formula.statement.StatementIf;
import com.neusoft.graphene.basecomponent.formula.statement.StatementParagraph;
import com.neusoft.graphene.basecomponent.formula.statement.StatementReturn;
import com.neusoft.graphene.basecomponent.formula.statement.StatementThread;
import com.neusoft.graphene.basecomponent.formula.statement.StatementThrow;
import com.neusoft.graphene.basecomponent.formula.statement.StatementVar;
import com.neusoft.graphene.basecomponent.formula.statement.StatementWhile;


/**
 * 语法分析
 * @author lerrain
 */
public class Syntax
{
	public static Code sentenceOf(Words words)
	{
		if (words.getType(words.size() - 1) == Words.SEMICOLON)
			words = words.cut(0, words.size() - 1);
		
		if (words.isEmpty())
			return null;
//	TODO	
//		System.out.println("Syntax----words>"+words);

		int type = words.getType(0);
		
		String _word = words.getWord(0);

		if (type == Words.KEYWORD)
		{
			if ("if".equals(_word))
			{
				return new StatementIf(words);
			}
			else if ("return".equals(_word))
			{
				return new StatementReturn(words);
			}
			else if ("for".equals(_word))
			{
				return new StatementFor(words);
			}
			else if ("while".equals(_word))
			{
				return new StatementWhile(words);
			}
			else if ("break".equals(_word))
			{
				return new StatementBreak(words);
			}
			else if ("var".equals(_word))
			{
				return new StatementVar(words);
			}
			else if ("throw".equals(_word))
			{
				return new StatementThrow(words);
			}
			else if ("continue".equals(_word))
			{
				return new StatementContinue(words);
			}
			else if ("thread".equals(_word))
			{
				return new StatementThread(words);
			}
			
			throw new SyntaxException("无法识别的关键字：" + _word);
		}
		else if (type == Words.BRACE)
		{
			return new StatementParagraph(words);
		}
		
		return Expression.expressionOf(words);
	}
	
	/**
	 * 把一段程序，按语句切割
	 * @param words
	 * @return List of Function
	 */
	public static List<Words> split(Words words)
	{
		List<Words> word_list = new ArrayList<Words>();
		
		int size = words.size();
		
		for (int index = 0; index < size; index++)
		{
			int left = index;
			index = findSplit(words, index);
			index = index >= 0 ? index : size - 1;
			
			word_list.add(words.cut(left, index + 1));
		}
		
		return word_list;
	}
	
	/**
	 * 找到第一个右侧的括号，中间被括号包括的内容会被跳过
	 * @param ws
	 * @param i
	 * @return
	 */
	public static int findRightBrace(Words ws, int position)
	{
		int size = ws.size();
		
		int deep = 1;
		int index = position;
		
		for (; index < size; index++)
		{
			int t = ws.getType(index);
			
			if (t == Words.BRACE || t == Words.BRACKET || t == Words.PRT)
			{
				deep++;
			}
			else if (t == Words.BRACE_R || t == Words.BRACKET_R || t == Words.PRT_R)
			{
				deep--;
				if (deep == 0)
					break;
			}
		}
		
		if (index >= size)
			throw new SyntaxException("无法找到对应的右侧括号：" + ws + " at " + position);

		return	index;
	}
	
	public static int findLeftBrace(Words ws, int index)
	{
		int deep = 1;
		for (; index >= 0; index--)
		{
			int type = ws.getType(index);
			
			if (type == Words.BRACE_R || type == Words.BRACKET_R || type == Words.PRT_R)
			{
				deep++;
			}
			else if (type == Words.BRACE || type == Words.BRACKET || type == Words.PRT)
			{
				deep--;
				if (deep == 0)
					break;
			}
		}
		
		return index;
	}
	
	public static int findSplit(Words ws, int index)
	{
		int size = ws.size();
		
		for (; index < size; index++)
		{
			int type = ws.getType(index);
			
			if (type == Words.BRACE || type == Words.BRACKET || type == Words.PRT)
				index = findRightBrace(ws, index + 1);
			
			if (isSplit(ws, index))
				return index;
		}
		
		return -1;
	}
	
	public static boolean isLeftBrace(Words ws, int index)
	{
		int type = ws.getType(index);
		return type == Words.BRACE || type == Words.BRACKET || type == Words.PRT;
	}
	
	public static int findSplitWithoutSyntax(Words ws, int index)
	{
		int size = ws.size();
		
		for (; index < size; index++)
		{
			int type = ws.getType(index);
			
			if (type == Words.BRACE || type == Words.BRACKET || type == Words.PRT)
				index = findRightBrace(ws, index + 1);
			
			if (type == Words.SEMICOLON || type == Words.BRACE_R)
				return index;
		}
		
		return -1;
	}
	
	private static boolean isSplit(Words ws, int index)
	{
		int size = ws.size();
		int type = ws.getType(index);
		
		//分号和右大括号并不能完全代表行结尾，如果后面跟else、catch等，需要继续追加
		if (type == Words.SEMICOLON || type == Words.BRACE_R)
		{
			String s1 = index + 1 < size ? ws.getWord(index + 1) : null;
			return (!"else".equals(s1) && !"catch".equals(s1));
		}
		
		return false;
	}
}
