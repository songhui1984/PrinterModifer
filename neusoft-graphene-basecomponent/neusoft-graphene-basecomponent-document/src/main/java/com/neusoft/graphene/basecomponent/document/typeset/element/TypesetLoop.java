package com.neusoft.graphene.basecomponent.document.typeset.element;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.element.DocumentPanel;
import com.neusoft.graphene.basecomponent.document.exception.TypesetBuildException;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetUtil;
import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.Value;



public class TypesetLoop extends TypesetPanel
{
	
	private Formula from_Formula;
	private Formula to_Formula;
	private Formula step_Formula;
	
	private String name;
	
	Factors varSet_Factors;
	
	public TypesetLoop(Formula from_Formula, Formula to_Formula, Formula step_Formula, String name)
	{
		this.from_Formula = from_Formula;
		this.to_Formula = to_Formula;
		this.step_Formula = step_Formula;
		
		this.name = name == null ? "I" : name;
	}
	
	public TypesetLoop(Formula from_Formula, Formula to_Formula, Formula step_Formula)
	{
		this(from_Formula, to_Formula, step_Formula, null);
	}
	
	public DocumentElementAbs build(TypesetParameters typesetParameters,ResourceOps ops)
	{
		try
		{
			DocumentPanel docPanel = new DocumentPanel();
			
			if (this.getX_TypesetNumber() != null)
				docPanel.setX(this.getX_TypesetNumber().value(typesetParameters));
			else
				docPanel.setX(0);
			
			if (this.getY_TypesetNumber() != null)
				docPanel.setY(typesetParameters.getDatum() + this.getY_TypesetNumber().value(typesetParameters));
			else
				docPanel.setY(typesetParameters.getDatum());
			
			TypesetParameters tvs2 = pack(typesetParameters);

			int width = 0, height = 0;

			Object forValue = value == null ? null : value.run(typesetParameters);
			if (value != null && forValue == null) //有循环的list value，不过是空，那么直接跳过
			{
			}
			else if (forValue instanceof Map) // value = "..."
			{
				tvs2.declare(name);

				Map map = (Map)forValue;
				for (Iterator iter = map.keySet().iterator(); iter.hasNext();)
				{
					tvs2.set(name, iter.next());

					int size = getElementNum();
					for (int index = 0; index < size; index++)
					{
						TypesetElement iye = (TypesetElement)this.getElement(index);
						DocumentElementAbs docAbs = iye.isShow(tvs2) ? iye.build(tvs2,ops) : null;

						if (docAbs != null)
						{
							docPanel.add(docAbs);

							if (docAbs.getX() + docAbs.getWidth() > width)
								width = docAbs.getX() + docAbs.getWidth();
							if (docAbs.getY() + docAbs.getHeight() > height)
								height = docAbs.getY() + docAbs.getHeight();
						}
					}
				}
			}
			else if (forValue != null) // value = "..."
			{
				int size = 0;
				if (forValue instanceof List)
					size = ((List)forValue).size();
				else if (forValue instanceof Object[])
					size = ((Object[])forValue).length;
				
				tvs2.declare(name);
				for (int i = 0; i < size; i++)
				{
					if (forValue instanceof List)
						tvs2.set(name, ((List)forValue).get(i));
					else if (forValue instanceof Object[])
						tvs2.set(name, ((Object[])forValue)[i]);
					
					int num = getElementNum();
					for (int index = 0; index < num; index++)
					{
						TypesetElement typesetElement = (TypesetElement)this.getElement(index);
						DocumentElementAbs docAbsTemp = typesetElement.isShow(tvs2) ? typesetElement.build(tvs2,ops) : null;
						
						if (docAbsTemp != null)
						{
							docPanel.add(docAbsTemp);
							
							if (docAbsTemp.getX() + docAbsTemp.getWidth() > width)
								width = docAbsTemp.getX() + docAbsTemp.getWidth();
							if (docAbsTemp.getY() + docAbsTemp.getHeight() > height)
								height = docAbsTemp.getY() + docAbsTemp.getHeight();
						}
					}
				}
			}
			else // from="..." to="..." step="..."
			{
				int form = 0, to = 2, step = 1;
				try
				{
					form = Value.intOf(from_Formula, typesetParameters);
				}
				catch (Exception e)
				{
					if (TypesetUtil.getMode() == TypesetUtil.MODE_FAIL)
					{
						throw new TypesetBuildException("exception in loop<from>'s formula: " + this.getValue(), e);
					}
					else if (TypesetUtil.getMode() == TypesetUtil.MODE_ALWAYS)
					{
						form = 0;
//						System.out.println("exception in loop<from> formula: " + this.getValue() + ", set it 0 instead.");
					}
				}
				
				try
				{
					to = Value.intOf(to_Formula, typesetParameters);
				}
				catch (Exception e)
				{
					if (TypesetUtil.getMode() == TypesetUtil.MODE_FAIL)
					{
						throw new TypesetBuildException("exception in loop<to>'s formula: " + this.getValue(), e);
					}
					else if (TypesetUtil.getMode() == TypesetUtil.MODE_ALWAYS)
					{
						to = 1;
//						System.out.println("exception in loop<to> formula: " + this.getValue() + ", set it 1 instead.");
					}
				}
				
				try
				{
					step = step_Formula == null ? 1 : Value.intOf(step_Formula, typesetParameters);
				}
				catch (Exception e)
				{
					if (TypesetUtil.getMode() == TypesetUtil.MODE_FAIL)
					{
						throw new TypesetBuildException("exception in loop<step>'s formula: " + this.getValue(), e);
					}
					else if (TypesetUtil.getMode() == TypesetUtil.MODE_ALWAYS)
					{
						step = 1;
//						System.out.println("exception in loop<step> formula: " + this.getValue() + ", set it 1 instead.");
					}
				}
				
				tvs2.declare(name);
				
				for (int i = form; i <= to; i += step)
				{
					tvs2.set(name, new Integer(i));
					
					int size = getElementNum();
					for (int j = 0; j < size; j++)
					{
						TypesetElement typesetElement = (TypesetElement)this.getElement(j);
						DocumentElementAbs docAbsTemp = typesetElement.isShow(tvs2) ? typesetElement.build(tvs2,ops) : null;
						
						if (docAbsTemp != null)
						{
							docPanel.add(docAbsTemp);
							
							if (docAbsTemp.getX() + docAbsTemp.getWidth() > width)
								width = docAbsTemp.getX() + docAbsTemp.getWidth();
							if (docAbsTemp.getY() + docAbsTemp.getHeight() > height)
								height = docAbsTemp.getY() + docAbsTemp.getHeight();
						}
					}
				}
			}

			if (this.getWidth() != null)
				docPanel.setWidth(this.getWidth().value(typesetParameters));
			else
				docPanel.setWidth(width);
			
			if (this.getHeight() != null)
				docPanel.setHeight(this.getHeight().value(typesetParameters));
			else
				docPanel.setHeight(height);
			
			resetY(typesetParameters, docPanel);
			
			return docPanel;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
