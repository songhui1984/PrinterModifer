package com.neusoft.graphene.basecomponent.formula.statement;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;
import com.neusoft.graphene.basecomponent.formula.analyse.Expression;
import com.neusoft.graphene.basecomponent.formula.analyse.Syntax;
import com.neusoft.graphene.basecomponent.formula.analyse.Words;
import com.neusoft.graphene.basecomponent.formula.exception.SyntaxException;
import com.neusoft.graphene.basecomponent.formula.function.FunctionArray;
import com.neusoft.graphene.basecomponent.formula.function.FunctionCall;
import com.neusoft.graphene.basecomponent.formula.function.FunctionCase;
import com.neusoft.graphene.basecomponent.formula.function.FunctionCeil;
import com.neusoft.graphene.basecomponent.formula.function.FunctionFill;
import com.neusoft.graphene.basecomponent.formula.function.FunctionFind;
import com.neusoft.graphene.basecomponent.formula.function.FunctionFloor;
import com.neusoft.graphene.basecomponent.formula.function.FunctionFormat;
import com.neusoft.graphene.basecomponent.formula.function.FunctionMax;
import com.neusoft.graphene.basecomponent.formula.function.FunctionMin;
import com.neusoft.graphene.basecomponent.formula.function.FunctionNum;
import com.neusoft.graphene.basecomponent.formula.function.FunctionPost;
import com.neusoft.graphene.basecomponent.formula.function.FunctionPow;
import com.neusoft.graphene.basecomponent.formula.function.FunctionPrint;
import com.neusoft.graphene.basecomponent.formula.function.FunctionRandom;
import com.neusoft.graphene.basecomponent.formula.function.FunctionRound;
import com.neusoft.graphene.basecomponent.formula.function.FunctionSize;
import com.neusoft.graphene.basecomponent.formula.function.FunctionSleep;
import com.neusoft.graphene.basecomponent.formula.function.FunctionStr;
import com.neusoft.graphene.basecomponent.formula.function.FunctionStrBegin;
import com.neusoft.graphene.basecomponent.formula.function.FunctionStrEnd;
import com.neusoft.graphene.basecomponent.formula.function.FunctionStrIndex;
import com.neusoft.graphene.basecomponent.formula.function.FunctionStrLen;
import com.neusoft.graphene.basecomponent.formula.function.FunctionStrReplace;
import com.neusoft.graphene.basecomponent.formula.function.FunctionStrRight;
import com.neusoft.graphene.basecomponent.formula.function.FunctionStrSplit;
import com.neusoft.graphene.basecomponent.formula.function.FunctionStrTrim;
import com.neusoft.graphene.basecomponent.formula.function.FunctionSum;
import com.neusoft.graphene.basecomponent.formula.function.FunctionTime;
import com.neusoft.graphene.basecomponent.formula.function.FunctionTimeStr;
import com.neusoft.graphene.basecomponent.formula.function.FunctionVal;
import com.neusoft.graphene.basecomponent.formula.script.Code;
import com.neusoft.graphene.basecomponent.formula.script.Script;
import com.neusoft.graphene.basecomponent.formula.script.Wrap;


/**
 * 
 * @author lerrain
 * 
 * 2014-08-11
 * 2014-08-10提到的这种形式，用?:来处理
 *
 * 2014-08-10
 * 由逗号分割开的多个表达式，通常是用作函数或数组的参数，并不是每个都需要计算的
 * 所以直接打包返回，处理的部分根据需要计算全部或者部分
 * 如：x[i] = case(i>0,x[i-1]+y,y); 
 * 如果每个都计算，那么这个函数是没办法运行的。
 * 
 */
public class StatementFunction implements Code
{
	String name;
	
	Code params;
	
	Function fs;

	static
	{
		Script.FUNCTIONS.put("case", new FunctionCase());
		Script.FUNCTIONS.put("round", new FunctionRound());
		Script.FUNCTIONS.put("ceil", new FunctionCeil());
		Script.FUNCTIONS.put("floor", new FunctionFloor());
		Script.FUNCTIONS.put("format", new FunctionFormat());
		Script.FUNCTIONS.put("array", new FunctionArray());
		Script.FUNCTIONS.put("min", new FunctionMin());
		Script.FUNCTIONS.put("max", new FunctionMax());
		Script.FUNCTIONS.put("pow", new FunctionPow());
		Script.FUNCTIONS.put("size", new FunctionSize());
		Script.FUNCTIONS.put("call", new FunctionCall());
		Script.FUNCTIONS.put("print", new FunctionPrint());
		Script.FUNCTIONS.put("fill", new FunctionFill());
		Script.FUNCTIONS.put("sum", new FunctionSum());
		Script.FUNCTIONS.put("val", new FunctionVal());
		Script.FUNCTIONS.put("find", new FunctionFind());
		Script.FUNCTIONS.put("str", new FunctionStr());
		Script.FUNCTIONS.put("str_begin", new FunctionStrBegin());
		Script.FUNCTIONS.put("str_end", new FunctionStrEnd());
		Script.FUNCTIONS.put("str_index", new FunctionStrIndex());
		Script.FUNCTIONS.put("str_split", new FunctionStrSplit());
		Script.FUNCTIONS.put("str_len", new FunctionStrLen());
		Script.FUNCTIONS.put("str_right", new FunctionStrRight());
		Script.FUNCTIONS.put("str_trim", new FunctionStrTrim());
		Script.FUNCTIONS.put("str_replace", new FunctionStrReplace());
		Script.FUNCTIONS.put("random", new FunctionRandom());
		Script.FUNCTIONS.put("post", new FunctionPost());
		Script.FUNCTIONS.put("time", new FunctionTime());
		Script.FUNCTIONS.put("timestr", new FunctionTimeStr());
		Script.FUNCTIONS.put("num", new FunctionNum());
		Script.FUNCTIONS.put("sleep", new FunctionSleep());
	}
	
	public StatementFunction(Words ws, int i)
	{
		if (i > 0)
			throw new SyntaxException("不是一个有效的函数语法 - " + ws);
		
		name = ws.getWord(i);
		
		//内置函数，参数不直接运算
		fs = (Function)Script.FUNCTIONS.get(name);

//		if ("case".equals(name))
//			fs = new FunctionCase();
//		else if ("round".equals(name))
//			fs = new FunctionRound();
//		else if ("ceil".equals(name))
//			fs = new FunctionCeil();
//		else if ("floor".equals(name))
//			fs = new FunctionFloor();
//		else if ("format".equals(name))
//			fs = new FunctionFormat();
//		else if ("array".equals(name))
//			fs = new FunctionArray();
//		else if ("min".equals(name))
//			fs = new FunctionMin();
//		else if ("max".equals(name))
//			fs = new FunctionMax();
//		else if ("pow".equals(name))
//			fs = new FunctionPow();
//		else if ("size".equals(name))
//			fs = new FunctionSize();
//		else if ("str_begin".equals(name))
//			fs = new FunctionStrBegin();
//		else if ("str_end".equals(name))
//			fs = new FunctionStrEnd();
//		else if ("str_index".equals(name))
//			fs = new FunctionStrIndex();
//		else if ("call".equals(name))
//			fs = new FunctionCall();
//		else if ("print".equals(name))
//			fs = new FunctionPrint();
//		else if ("fill".equals(name))
//			fs = new FunctionFill();
//		else if ("sum".equals(name))
//			fs = new FunctionSum();
//		else if ("val".equals(name))
//			fs = new FunctionVal();
//		else if ("find".equals(name))
//			fs = new FunctionFind();
//		else if ("str".equals(name))
//			fs = new FunctionStr();
//		else if ("str_split".equals(name))
//			fs = new FunctionStrSplit();
//		else if ("str_len".equals(name))
//			fs = new FunctionStrLen();
//		else if ("str_right".equals(name))
//			fs = new FunctionStrRight();
//		else if ("str_trim".equals(name))
//			fs = new FunctionStrTrim();
//		else if ("str_replace".equals(name))
//			fs = new FunctionStrReplace();
//		else if ("random".equals(name))
//			fs = new FunctionRandom();
//		else if ("post".equals(name))
//			fs = new FunctionPost();
//		else if ("time".equals(name))
//			fs = new FunctionTime();
//		else if ("timestr".equals(name))
//			fs = new FunctionTimeStr();
//		else if ("num".equals(name))
//			fs = new FunctionNum();
//		else if ("sleep".equals(name))
//			fs = new FunctionSleep();

		int l = i + 1;
		int r = Syntax.findRightBrace(ws, l + 1);
		
		params = l + 1 == r ? null : Expression.expressionOf(ws.cut(l + 1, r));
	}

	public Object run(Factors factors)
	{
		if ("try".equals(name))
		{
			StatementComma c = (StatementComma)params;
			try
			{
				return c.left().run(factors);
			}
			catch (Exception e)
			{
				return c.right().run(factors);
			}
		}
		
		Function f = fs;
		
		Object v = factors.get(name);
		if (v != null && v instanceof Function)
			f = (Function)v;
		
		if (f == null)
		{
			if (v == null)
				throw new RuntimeException("未找到函数 - " + name);
			else
				throw new RuntimeException("该变量对应的值不是函数体 - " + name + " is " + v.getClass());
		}

		//用户自定义的函数，参数直接运算
		Object[] wrap = Wrap.arrayOf(params, factors);

//		Stack stack = new Stack(factors);
//		if (wrap != null)
//		{
//			stack.set("#0", new Integer(wrap.length));
//			for (int i = 0; i < wrap.length; i++)
//			{
//				stack.set("#" + (i + 1), wrap[i]);
//			}
//		}
		
		return f.run(wrap, factors);
	}

	public String toText(String space)
	{
		return name + "(" + (params == null ? "" : params.toText("")) + ")";
	}
}
