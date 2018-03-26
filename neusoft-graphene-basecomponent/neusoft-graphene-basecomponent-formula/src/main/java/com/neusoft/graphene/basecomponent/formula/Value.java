package com.neusoft.graphene.basecomponent.formula;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Value implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final static int TYPE_NULL		= 0;
	public final static int TYPE_DECIMAL	= 1;
	public final static int TYPE_STRING		= 2;
	public final static int TYPE_DATE		= 3;
	public final static int TYPE_LIST		= 4;
	public final static int TYPE_MAP		= 5;
	public final static int TYPE_ARRAY		= 6;
	public final static int TYPE_BOOLEAN	= 7;
	public final static int TYPE_FACTORS		= 90;
	public final static int TYPE_OBJECT		= 100;
	
	int type;
	
	Object valueObject;
	
	public Object getValue()
	{
		return valueObject;
	}
	
	public int getType()
	{
		return type;
	}

	
	public String toString()
	{
		return valueObject == null ? null : valueObject.toString();
	}
	
	public Value(Object obj)
	{
		if (obj == null)
		{
			type = TYPE_NULL;
		}
		else if (obj instanceof BigDecimal)
		{
			type = TYPE_DECIMAL;
		}
		else if (obj instanceof BigInteger)
		{
			type = TYPE_DECIMAL;
		}
		else if (obj instanceof Integer)
		{
			type = TYPE_DECIMAL;
		}
		else if (obj instanceof Float)
		{
			type = TYPE_DECIMAL;
		}
		else if (obj instanceof Double)
		{
			type = TYPE_DECIMAL;
		}
		else if (obj instanceof Long)
		{
			type = TYPE_DECIMAL;
		}
		else if (obj instanceof Boolean)
		{
			type = TYPE_BOOLEAN;
		}
		else if (obj instanceof List)
		{
			type = TYPE_LIST;
		}
		else if (obj instanceof BigDecimal[][])
		{
			type = TYPE_ARRAY;
		}
		else if (obj instanceof BigDecimal[])
		{
			type = TYPE_ARRAY;
		}
		else if (obj instanceof BigInteger[][])
		{
			type = TYPE_ARRAY;
		}
		else if (obj instanceof BigInteger[])
		{
			type = TYPE_ARRAY;
		}
		else if (obj instanceof Map)
		{
			type = TYPE_MAP;
		}
		else if (obj instanceof String)
		{
			type = TYPE_STRING;
		}
		else if (obj instanceof Date)
		{
			type = TYPE_DATE;
		}
		else if (obj instanceof Factors)
		{
			type = TYPE_FACTORS;
		}
		else
		{
			type = TYPE_OBJECT;
		}
		
		this.valueObject = obj;
	}
	
	public boolean isNull()
	{
		return valueObject == null;
	}
	
	public boolean isType(int type)
	{
		return this.type == type;
	}
	
	public boolean isFactors()
	{
		return isType(TYPE_FACTORS);
	}
	
	public boolean isBoolean()
	{
		return isType(TYPE_BOOLEAN);
	}
	
	public boolean isString()
	{
		return isType(TYPE_STRING);
	}
	
	public boolean isDecimal()
	{
		return isType(TYPE_DECIMAL);
	}
	
	public boolean isMap()
	{
		return isType(TYPE_MAP);
	}
	
	public int intValue()
	{
		return intOf(valueObject);
	}
	
	public static int intOf(Object obj, int def)
	{
		if (obj == null)
			return def;

		if (obj instanceof Number)
			return ((Number)obj).intValue();

		try
		{
			return Integer.parseInt(obj.toString());
		}
		catch (Exception e)
		{
			return def;
		}
	}

	public static int intOf(Object obj)
	{
		if (obj instanceof Number)
			return ((Number)obj).intValue();

		return Integer.parseInt(obj.toString());
	}

	public long longValue()
	{
		if (valueObject instanceof Number)
			return ((Number)valueObject).longValue();

		return Long.parseLong(valueObject.toString());
	}

	public double doubleValue()
	{
		return doubleOf(valueObject);
	}
	
	public static double doubleOf(Object obj)
	{
		if (obj instanceof Number)
			return ((Number)obj).doubleValue();
		else
			return Double.parseDouble(obj.toString());
	}
	
	public boolean booleanValue()
	{
		return booleanOf(valueObject);
	}
	
	public static boolean booleanOf(Object obj)
	{
		if (obj instanceof Boolean)
			return ((Boolean)obj).booleanValue();
		else
			return intOf(obj) != 0;
	}
	
	public boolean isEqualTo(Object obj)
	{
		return equals(new Value(obj));
	}
	
	public boolean equals(Value value)
	{
		if (this.isDecimal() && value.isDecimal())
		{
			return this.toDecimal().compareTo(value.toDecimal()) == 0;
		}
		else 
		{
			return this.getValue().equals(value.getValue());
		}
	}

	public BigDecimal toDecimal()
	{
		if (valueObject instanceof BigDecimal)
			return (BigDecimal)valueObject;
		else if (valueObject instanceof Integer)
			return decimalOf(((Integer)valueObject).intValue());
		else if (valueObject instanceof Float)
			return decimalOf(((Float)valueObject).floatValue());
		else if (valueObject instanceof Double)
			return decimalOf(((Double)valueObject).doubleValue());
		else if (valueObject instanceof BigInteger)
			return new BigDecimal((BigInteger)valueObject);
		else if (valueObject instanceof Long)
			return new BigDecimal((Long)valueObject);
		else
			return new BigDecimal(valueObject.toString()); //错误类型直接异常
	}
	
	
	private static BigDecimal decimalOf(int i)
	{
		return new BigDecimal(i);
	}
	
	private static BigDecimal decimalOf(float f)
	{
		return BigDecimal.valueOf(f);
	}

	private static BigDecimal decimalOf(double d)
	{
		return BigDecimal.valueOf(d);
	}
	
	public static Value valueOf(Object obj)
	{
		return new Value(obj);
	}
	
	
	public static Value valueOf(Formula function, Factors factors)
	{
		return new Value(function.run(factors));
	}
	
	public static Value valueOf(Formula function, Factors factors, Object obj)
	{
		if (function == null)
			return new Value(obj);
		
		try
		{
			return new Value(function.run(factors));
		}
		catch (Exception e)
		{
			return new Value(obj);
		}
	}
	
	public static BigDecimal decimalOf(Formula function, Factors factors)
	{
		return valueOf(function, factors).toDecimal();
	}
	
	public static String stringOf(Formula function, Factors factors)
	{
		return valueOf(function, factors).toString();
	}
	
	public static int intOf(Formula function, Factors factors, int i)
	{
		return valueOf(function, factors, new Integer(i)).intValue();
	}
	
	public static int intOf(Formula function, Factors factors)
	{
		return intOf(function.run(factors));
	}

	public static double doubleOf(Formula function, Factors factors)
	{
		return doubleOf(function.run(factors));
	}
	
	public static boolean booleanOf(Formula function, Factors factors)
	{
		return booleanOf(function.run(factors));
	}
}
