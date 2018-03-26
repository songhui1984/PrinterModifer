package com.neusoft.graphene.basecomponent.formula.aries.arithmetic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.neusoft.graphene.basecomponent.formula.Formula;


public class Arithmetic implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	int priority;
	
	boolean funtion = true;
	
	List<String> signs = new ArrayList<String>();
	
	List<Formula> formulaList;
	
	protected void setPriority(int priority)
	{
		this.priority = priority;
	}
	
	public int getPriority()
	{
		return priority;
	}
	
	protected void addSign(String sign)
	{
		signs.add(sign);
	}
	
	public List<String> getSigns()
	{
		return signs;
	}
	
	protected void setFuntion(boolean funtion)
	{
		this.funtion = funtion;
	}

	protected void setFormulaList(List<Formula> formulaList)
	{
		this.formulaList = formulaList;
	}
	
	protected Formula getFormula(int index)
	{
		return formulaList.get(index);
	}
	
	protected List<Formula> getFormuLaList()
	{
		return formulaList;
	}
	
	protected int getFormulaCount()
	{
		return formulaList.size();
	}

	public boolean isFuntion()
	{
		return funtion;
	}
}
