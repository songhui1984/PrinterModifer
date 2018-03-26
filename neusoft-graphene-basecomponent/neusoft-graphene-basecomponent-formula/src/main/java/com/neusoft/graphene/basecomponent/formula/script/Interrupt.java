package com.neusoft.graphene.basecomponent.formula.script;

public class Interrupt
{
	public static final int CONTINUE	= 1;
	public static final int BREAK		= 2;
	public static final int RETURN		= 3;
//	public static final int THROW		= 4;
	
	int type;
	
	Object value;
	
	public Interrupt(int type)
	{
		this(type, null);
	}
	
	public Interrupt(int type, Object value)
	{
		this.type = type;
		this.value = value;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

	public int getType()
	{
		return type;
	}
	
	public static Object getValue(Object v)
	{
		return ((Interrupt)v).value;
	}
	
	public static Interrupt interruptOf(int type, Object v)
	{
		return new Interrupt(type, v);
	}
	
	public static Interrupt interruptOf(int type)
	{
		return interruptOf(type, null);
	}
	
	public static boolean isMatch(Object v, int type)
	{
		return v instanceof Interrupt && ((Interrupt)v).getType() == type;
	}

}
