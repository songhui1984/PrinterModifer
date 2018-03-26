package com.neusoft.graphene.basecomponent.formula.analyse;


import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.statement.StatementAdd;
import com.neusoft.graphene.basecomponent.formula.statement.StatementAddAdd;
import com.neusoft.graphene.basecomponent.formula.statement.StatementAddLet;
import com.neusoft.graphene.basecomponent.formula.statement.StatementAnd;
import com.neusoft.graphene.basecomponent.formula.statement.StatementArray;
import com.neusoft.graphene.basecomponent.formula.statement.StatementBrace;
import com.neusoft.graphene.basecomponent.formula.statement.StatementColon;
import com.neusoft.graphene.basecomponent.formula.statement.StatementComma;
import com.neusoft.graphene.basecomponent.formula.statement.StatementDivide;
import com.neusoft.graphene.basecomponent.formula.statement.StatementEqual;
import com.neusoft.graphene.basecomponent.formula.statement.StatementFunction;
import com.neusoft.graphene.basecomponent.formula.statement.StatementFunctionDim;
import com.neusoft.graphene.basecomponent.formula.statement.StatementGreater;
import com.neusoft.graphene.basecomponent.formula.statement.StatementGreaterEqual;
import com.neusoft.graphene.basecomponent.formula.statement.StatementLess;
import com.neusoft.graphene.basecomponent.formula.statement.StatementLessEqual;
import com.neusoft.graphene.basecomponent.formula.statement.StatementLet;
import com.neusoft.graphene.basecomponent.formula.statement.StatementMod;
import com.neusoft.graphene.basecomponent.formula.statement.StatementMultiply;
import com.neusoft.graphene.basecomponent.formula.statement.StatementNew;
import com.neusoft.graphene.basecomponent.formula.statement.StatementNot;
import com.neusoft.graphene.basecomponent.formula.statement.StatementNotEqual;
import com.neusoft.graphene.basecomponent.formula.statement.StatementOr;
import com.neusoft.graphene.basecomponent.formula.statement.StatementParentheses;
import com.neusoft.graphene.basecomponent.formula.statement.StatementPointKey;
import com.neusoft.graphene.basecomponent.formula.statement.StatementPointMethod;
import com.neusoft.graphene.basecomponent.formula.statement.StatementQuestMark;
import com.neusoft.graphene.basecomponent.formula.statement.StatementSub;
import com.neusoft.graphene.basecomponent.formula.statement.StatementSubLet;
import com.neusoft.graphene.basecomponent.formula.statement.StatementSubSub;
import com.neusoft.graphene.basecomponent.formula.statement.ConstantStatement;
import com.neusoft.graphene.basecomponent.formula.statement.VariableStatement;

/**
 * 表达式处理
 * @author lerrain
 */
public class Expression
{
	public static final int PRIORITY_MIN		= 0;
	public static final int PRIORITY_MAX		= 100000;

	public static Code expressionOf(Words words)
	{
		if (words.size() == 0)
			return null;

		if (words.size() == 1)
		{
			int type = words.getType(0);
			if (type == Words.NULLPT || type == Words.NUMBER || type == Words.STRING || type == Words.TRUE || type == Words.FALSE)
				return new ConstantStatement(type, words.getWord(0));
			
			if (type == Words.VARIABLE || type == Words.WORD)
				return new VariableStatement(words.getWord(0));
		}
		
		int pr1 = 10000;
		int pos = -1;
		
		for (int index = words.size() - 1; index >= 0; index--)
		{
			int type = words.getType(index);
			
			if (type == Words.BRACE_R || type == Words.BRACKET_R || type == Words.PRT_R)
			{
				index = Syntax.findLeftBrace(words, index - 1);
				type = words.getType(index);
			}
			
			int pr2 = getPriority(type);
			
			if (pr2 > 0 && pr2 < pr1)
			{
				pr1 = pr2;
				pos = index;
			}
		}
		
		if (pos >= 0)
			return arithmeticOf(words.getType(pos), words, pos);
		
		throw new SyntaxException("无法处理的运算 - " + words.toString());
	}

	private static Code arithmeticOf(int arithmetic, Words ws, int pos)
	{
		if (arithmetic == Words.LET) return new StatementLet(ws, pos);
		if (arithmetic == Words.ADDLET) return new StatementAddLet(ws, pos);
		if (arithmetic == Words.SUBLET) return new StatementSubLet(ws, pos);
		if (arithmetic == Words.COMMA) return new StatementComma(ws, pos);
		if (arithmetic == Words.QUESTMARK) return new StatementQuestMark(ws, pos);
		if (arithmetic == Words.COLON) return new StatementColon(ws, pos);
		
		if (arithmetic == Words.NEW) return new StatementNew(ws, pos);
		if (arithmetic == Words.FUNCTION_DIM) return new StatementFunctionDim(ws, pos);

		if (arithmetic == Words.POINT_KEY) return new StatementPointKey(ws, pos);
		if (arithmetic == Words.POINT_METHOD) return new StatementPointMethod(ws, pos);
		if (arithmetic == Words.BRACKET) return new StatementArray(ws, pos);
		if (arithmetic == Words.FUNCTION) return new StatementFunction(ws, pos);
		if (arithmetic == Words.BRACE) return new StatementBrace(ws, pos);
		
		if (arithmetic == Words.POSITIVE) return new StatementAdd(ws, pos);
		if (arithmetic == Words.NEGATIVE) return new StatementSub(ws, pos);
		if (arithmetic == Words.ADDADD) return new StatementAddAdd(ws, pos);
		if (arithmetic == Words.SUBSUB) return new StatementSubSub(ws, pos);
		if (arithmetic == Words.ADD) return new StatementAdd(ws, pos);
		if (arithmetic == Words.SUB) return new StatementSub(ws, pos);
		if (arithmetic == Words.MULTIPLY) return new StatementMultiply(ws, pos);
		if (arithmetic == Words.DIVIDE) return new StatementDivide(ws, pos);
		if (arithmetic == Words.MOD) return new StatementMod(ws, pos);
		
		if (arithmetic == Words.AND) return new StatementAnd(ws, pos);
		if (arithmetic == Words.OR) return new StatementOr(ws, pos);
		if (arithmetic == Words.REVISE) return new StatementNot(ws, pos);
		if (arithmetic == Words.EQUAL) return new StatementEqual(ws, pos);
		if (arithmetic == Words.NOTEQUAL) return new StatementNotEqual(ws, pos);
		if (arithmetic == Words.GREATER) return new StatementGreater(ws, pos);
		if (arithmetic == Words.LESS) return new StatementLess(ws, pos);
		if (arithmetic == Words.GREATEREQUAL) return new StatementGreaterEqual(ws, pos);
		if (arithmetic == Words.LESSEQUAL) return new StatementLessEqual(ws, pos);
		
		if (arithmetic == Words.PRT) return new StatementParentheses(ws, pos);
		
		throw new SyntaxException("ARITHMETIC<" + arithmetic + "> not found");
	}
	
	private static int getPriority(int arithmetic)
	{
		if (arithmetic == Words.LET) return 50;
		if (arithmetic == Words.ADDLET) return 50;
		if (arithmetic == Words.SUBLET) return 50;
		if (arithmetic == Words.COMMA) return 60;
		if (arithmetic == Words.QUESTMARK) return 100;
		if (arithmetic == Words.COLON) return 101;
		
		if (arithmetic == Words.NEW) return 1250;
		if (arithmetic == Words.FUNCTION_DIM) return 1260;
		
		if (arithmetic == Words.POINT_KEY) return 1300;
		if (arithmetic == Words.POINT_METHOD) return 1300;
		if (arithmetic == Words.BRACKET) return 1300;
		if (arithmetic == Words.FUNCTION) return 1300;
		if (arithmetic == Words.BRACE) return 1300;
		
		if (arithmetic == Words.POSITIVE) return 1200;
		if (arithmetic == Words.NEGATIVE) return 1200;
		if (arithmetic == Words.ADDADD) return 1200;
		if (arithmetic == Words.SUBSUB) return 1200;
		if (arithmetic == Words.ADD) return 1000;
		if (arithmetic == Words.SUB) return 1000;
		if (arithmetic == Words.MULTIPLY) return 1010;
		if (arithmetic == Words.DIVIDE) return 1010;
		if (arithmetic == Words.MOD) return 1010;
		
		if (arithmetic == Words.AND) return 300;
		if (arithmetic == Words.OR) return 200;
		if (arithmetic == Words.REVISE) return 1200;
		if (arithmetic == Words.EQUAL) return 700;
		if (arithmetic == Words.NOTEQUAL) return 700;
		if (arithmetic == Words.GREATER) return 800;
		if (arithmetic == Words.LESS) return 800;
		if (arithmetic == Words.GREATEREQUAL) return 800;
		if (arithmetic == Words.LESSEQUAL) return 800;
		
		if (arithmetic == Words.PRT) return 2000;
		
		return -1;
	}
}
