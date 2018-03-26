package com.neusoft.graphene.basecomponent.document.typeset;

/**
 * 扩展参数表，附带坐标信息
 */

import java.io.Serializable;
import java.util.Map;

import com.neusoft.graphene.basecomponent.formula.Factors;
import com.neusoft.graphene.basecomponent.formula.script.Stack;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TypesetParameters extends Stack implements Factors, Serializable {
	private static final long serialVersionUID = 1L;

	TypesetPaper paper;

	int y; // 当前最靠下的元素的坐标（相对于基准坐标）
	int datum; // 当前的基准坐标，reset时，当前的y坐标会被重置为基准坐标，y则会被置0
	int streamY; // 当前画板的绝对Y坐标

	int windage; // 由于额外插入的元素造成的偏移量，如跨页时自动带入的表头，他们的数量通常是开始时无法预计的

	int pageTop; // 当前页面正文部分的顶部在流式文档中的坐标
	int pageBottom; // 当前页面正文部分的底部在流式文档中的坐标

	/*
	 * 之所以用extends Stack，不直接在这里new是因为：
	 * script在执行的时候，如果传入的不是stack，那么就会在动在外面包裹一层stack，这样TypesetParameters就不是最外层
	 * <script>标签下的脚本赋值都直接赋值在里层了，执行完脚本以后即被释放，无法起到调整整个环境值的作用
	 */
	// Stack stack;

	public TypesetParameters(Factors varSet) {
		super(varSet);

		// stack = this;
	}

	public TypesetParameters(Map map) {
		super();
		super.setAll(map);

	}
}
