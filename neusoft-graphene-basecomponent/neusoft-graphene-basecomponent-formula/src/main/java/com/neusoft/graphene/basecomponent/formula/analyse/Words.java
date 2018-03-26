package com.neusoft.graphene.basecomponent.formula.analyse;

import java.util.ArrayList;
import java.util.List;

/**
 * 词法分析
 * @author lerrain
 */
public class Words
{
	public static final int SYMBOL				= 10000; 
	public static final int WORD				= 11000;
	
	public static final int BRACE				= 10; //大括号  {
	
	public static final int BRACE_R				= 11; //大括号  }
	
	public static final int BRACKET				= 20; //中括号 [
	
	public static final int BRACKET_R			= 21; //中括号 ]
	
	public static final int PRT					= 30; //小括号 (

	public static final int PRT_R				= 31; //小括号)
	
	public static final int NUMBER				= 50; 
	public static final int STRING				= 60;
	
	public static final int TRUE				= 61;
	public static final int FALSE				= 62;
	
	public static final int NEW					= 70; 
	public static final int FUNCTION_DIM		= 71; 
	
	public static final int SEMICOLON			= 80; //;
	
	public static final int POINT_KEY			= 90; //.
	public static final int POINT_METHOD		= 91; 
	
	public static final int COMMA				= 100; //,
	public static final int QUESTMARK			= 101; //?
	public static final int COLON				= 102; //:
	public static final int COLON2				= 103; //:
	
	public static final int LET					= 110;
	public static final int AND					= 120; 
	public static final int OR					= 130; 
	
	public static final int REVISE				= 140; 
	public static final int EQUAL				= 150; //=
	public static final int NOTEQUAL			= 160; //
	
	public static final int GREATER				= 170; //>
	public static final int LESS				= 180; //<
	public static final int GREATEREQUAL		= 190; //>=
	public static final int LESSEQUAL			= 200; //<=
	
	public static final int ADD					= 210; //+
	public static final int SUB					= 220; //-
	public static final int ADDLET				= 230; //+=
	public static final int SUBLET				= 240; //-=
	public static final int ADDADD				= 250; //++
	public static final int SUBSUB				= 260; //--
	
	public static final int POSITIVE			= 270; //+
	public static final int NEGATIVE			= 280; //-
	public static final int MULTIPLY			= 281; //* 
	public static final int DIVIDE				= 282; //%
	public static final int MOD					= 283; //
	
	public static final int NULLPT				= 290; 
	public static final int KEYWORD				= 300;
	public static final int CLASS				= 310;
	
	//函数只有返回值的方法，方法是OO中的概念 在类型语言中（如：C#,Java）叫方法，在弱类型语言中（如：javascript,vbscript）叫函数，一个东西，用对象调用的叫方法，直接调函数名的叫函数
	public static final int VARIABLE			= 380; //变量
	public static final int FUNCTION			= 390; //函数
	public static final int METHOD				= 400; //方法
	public static final int KEY					= 410; //相对于前方值的KEY
	
	List<String> wordList = new ArrayList<String>();
	
	List<Integer> typeList = new ArrayList<Integer>();
	
	public int size()
	{
		return wordList.size();
	}
	
	public boolean isEmpty()
	{
		return wordList.isEmpty();
	}
	
	public void setType(int index, int type)
	{
		typeList.set(index, new Integer(type));
	}
	
	public String getWord(int index)
	{
		return wordList.get(index);
	}
	
	public void add(char word)
	{
		add(word + "", getSymbolType(word));
	}
	
	public void add(String word, int type)
	{
		wordList.add(word);
		typeList.add(new Integer(type));
	}
	
	public void add(Words ws, int from, int to)
	{
		for (int i = from; i < to; i++)
			add(ws.getWord(i), ws.getType(i));
	}
	
	public Words cut(int from, int to)
	{
		Words ws = new Words();
		ws.add(this, from, to);
		
		return ws;
	}
	
	public Words cut(int from)
	{
		return cut(from, size());
	}
	
	public int getType(int index)
	{
		if (index < 0)
			index += 0;
		
		return typeList.get(index).intValue();
	}
	
	public String toString()
	{
		return wordList.toString();
	}
	
	public static Words wordsOf(String text)
	{
		//去掉注释
		text = TextUtil.cutComment(text);
		
		//size(p.table[c]) > 0
//		System.out.println("text of words-------》"+text);
		
		Words words = new Words();
		
		int len = text.length();
		for (int index = 0; index < len; index++)
		{
			char _char = text.charAt(index);
			
			if (_char == '\"' || _char == '\'') //如果是字符串，或单引号圈起来的
			{
				int location = TextUtil.findInString(text, index + 1, _char);
				String str = text.substring(index, location + 1);
				
				str = str.replaceAll("[\\\\][\"]", "\"");
				str = str.replaceAll("[\\\\][\']", "\'");
				str = str.replaceAll("[\\\\][\\\\]", "\\");
				str = str.replaceAll("[\\\\][n]", "\n");
				
				words.add(str, STRING);
				
				index = location;
			}
			else if (isSpecialSymbol(_char))
			{
				words.add(_char);
			}
			else if (isSymbol(_char))		
			{
				int begin = index;
				for (; index < len - 1; index++)
				{
					_char = text.charAt(index + 1);
					if (!isSymbol(_char))
						break;
				}
				
				String word = text.substring(begin, index + 1);
				
				words.add(word, getSymbolType(word));
			}
			else if (isLetter(_char)) //如果是单词
			{
				int begin = index;
				for (; index < len - 1; index++)
				{
					_char = text.charAt(index + 1);
					if (!isLetter(_char) && !isNumber(_char))
						break;
				}

				String word = text.substring(begin, index + 1);
				words.add(word, getWordType(word));
			}
			else if (isNumber(_char)) //如果是数字(包括小数)，不接受C语言钟类似1.2f，3L这种形式的数字
			{
				int begin = index;
				for (; index < len - 1; index++)
				{
					_char = text.charAt(index + 1);
					if (!isNumber(_char) && _char != '.')
						break;
				}
				String number = text.substring(begin, index + 1);
				words.add(number, NUMBER);
			}
		}
		
//		System.out.println("words--------------"+words);
		
		words.analyse();
		
		return words;
	}
	
	/**
	 * 后续分析
	 * 1. WORD的前方如果不是POINT，并且后方不是PRT，那么这个WORD是个VARIABLE
	 * 2. WORD的前方如果不是POINT，并且后方是PRT，那么这个WORD是个FUNCTION
	 * 3. WORD的前方如果是POINT，并且后方不是PRT，那么这个WORD是个KEY（KEY的作用要视前方的值属性而定，可以是：Factors对象的name、Map的key、对象的成员变量等）
	 * 4. WORD的前方如果是POINT，并且后方是PRT，那么这个WORD是个METHOD
	 * 5. 正负号与加减号的判定，两者优先级不同
	 * 6. POINT的多种种类
	 */
	protected void analyse()
	{
		int size = size();
		for (int i = 0; i < size; i++)
		{
			int type = getType(i);
			if (type == WORD)
			{
				boolean isNew = i != 0 && getType(i - 1) == NEW;
				boolean isPoint = i != 0 && getType(i - 1) == POINT_KEY;
				boolean isPrt = i != size - 1 && getType(i + 1) == PRT;
				
				if (isNew)
					setType(i, CLASS);
				else if (isPoint && isPrt)
				{
					setType(i, METHOD);
					setType(i - 1, POINT_METHOD);
				}
				else if (isPoint && !isPrt)
					setType(i, KEY);
				else if (!isPoint && isPrt)
					setType(i, FUNCTION);
				else
					setType(i, VARIABLE);
			}
		}
	}
	
	private static int getSymbolType(char word)
	{
		if (word == ';')
			return SEMICOLON;
		else if (word == '.')
			return POINT_KEY;
		else if (word == ',')
			return COMMA;
		else if (word == '(')
			return PRT;
		else if (word == ')')
			return PRT_R;
		else if (word == '[')
			return BRACKET;
		else if (word == ']')
			return BRACKET_R;
		else if (word == '{')
			return BRACE;
		else if (word == '}')
			return BRACE_R;
		
		return SYMBOL;
	}
	
	private static int getSymbolType(String symbol)
	{
		if ("=".equals(symbol))
			return LET;
		if ("&&".equals(symbol))
			return AND;
		if ("||".equals(symbol))
			return OR;
		if ("!".equals(symbol))
			return REVISE;
		if ("==".equals(symbol))
			return EQUAL;
		if ("!=".equals(symbol))
			return NOTEQUAL;
		if (">".equals(symbol))
			return GREATER;
		if ("<".equals(symbol))
			return LESS;
		if (">=".equals(symbol))
			return GREATEREQUAL;
		if ("<=".equals(symbol))
			return LESSEQUAL;
		if ("+".equals(symbol))
			return ADD;
		if ("-".equals(symbol))
			return SUB;
		if ("+=".equals(symbol))
			return ADDLET;
		if ("-=".equals(symbol))
			return SUBLET;
		if ("++".equals(symbol))
			return ADDADD;
		if ("--".equals(symbol))
			return SUBSUB;
		if ("*".equals(symbol))
			return MULTIPLY;
		if ("/".equals(symbol))
			return DIVIDE;
		if ("%".equals(symbol))
			return MOD;
		if ("?".equals(symbol))
			return QUESTMARK;
		if (":".equals(symbol))
			return COLON;
		if ("::".equals(symbol))
			return COLON2;

		return SYMBOL;
	}
	
	private static int getWordType(String word)
	{
		if ("lt".equals(word))
			return LESS;
		if ("le".equals(word))
			return LESSEQUAL;
		if ("gt".equals(word))
			return GREATER;
		if ("ge".equals(word))
			return GREATEREQUAL;
		if ("and".equals(word))
			return AND;
		if ("or".equals(word))
			return OR;
		if ("not".equals(word))
			return REVISE;
		
		if ("new".equals(word))
			return NEW;
		if ("function".equals(word))
			return FUNCTION_DIM;

		if ("null".equals(word))
			return NULLPT;
		if ("true".equals(word))
			return TRUE;
		if ("false".equals(word))
			return FALSE;
		
		if (",for,while,if,else,return,continue,break,var,throw,thread,".indexOf("," + word + ",") >= 0)
			return KEYWORD;
		
		return WORD;
	}
	
	private static boolean isSpecialSymbol(char _char)
	{
		return _char == '(' || _char == '[' || _char == '{' || _char == '}' || _char == ']' || _char == ')' || _char == ',' || _char == '.' || _char == ';';
	}
	
	private static boolean isSymbol(char _char)
	{
		return _char == '+' || _char == '-' || _char == '*' || _char == '/' || _char == '=' || _char == '|' || _char == '&' || _char == '>' || _char == '<' || _char == '~' || _char == '?' || _char == ':' || _char == '^' || _char == '%' || _char == '!';
	}
	
	private static boolean isLetter(char _char)
	{
		return ((_char >= 'a' && _char <= 'z') || (_char >= 'A' && _char <= 'Z') || _char == '_' || _char == '#');
	}
	
	private static boolean isNumber(char _char)
	{
		return _char >= '0' && _char <= '9';
	}
}
