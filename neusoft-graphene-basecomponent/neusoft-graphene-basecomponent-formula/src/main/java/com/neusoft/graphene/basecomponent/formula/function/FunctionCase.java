package com.neusoft.graphene.basecomponent.formula.function;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.Function;
import com.neusoft.graphene.basecomponent.formula.Value;

public class FunctionCase implements Function
{
	public Object run(Object[] objArray, Factors factors)
	{
		int num = objArray.length;
		if (num == 3) //case(xxx>0, 1, -1)
		{
			if (Value.valueOf(objArray[0]).booleanValue())
				return objArray[1];
			else
				return objArray[2];
		}
		else if (num >= 4)//case(x, 1, 100, 2, 200, 300)
		{
			Object k = objArray[0];
			for (int j = 2; j <= num; j = j + 2)
			{
				Object key = objArray[j - 1];
				if (k == key || (k != null && k.equals(key)))
				{
					return objArray[j];
				}
			}
			
			if (num % 2 == 0) //偶数个参数，最后一个参数为都不符合的情况下的值。
				return objArray[num - 1];
		}
		
		throw new RuntimeException("错误的case运算");
	}
	
}
