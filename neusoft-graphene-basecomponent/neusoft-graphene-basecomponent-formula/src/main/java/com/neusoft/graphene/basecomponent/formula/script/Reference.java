package com.neusoft.graphene.basecomponent.formula.script;

import com.neusoft.graphene.basecomponent.formula.Factors;

/**
 * 引用，赋值时使用
 * 变量、POINT-KEY是引用，其他非引用操作不能出现在赋值号左侧
 * @author lerrain
 *
 */
public interface Reference
{
	public void let(Factors factors, Object value);
}
