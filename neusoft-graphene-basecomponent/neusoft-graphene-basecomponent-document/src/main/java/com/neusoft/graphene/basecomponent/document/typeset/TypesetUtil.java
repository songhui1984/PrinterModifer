package com.neusoft.graphene.basecomponent.document.typeset;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetText;
import com.neusoft.graphene.basecomponent.document.typeset.enviroment.TextDimension;
import com.neusoft.graphene.basecomponent.document.typeset.parser.ParserXml;
import com.neusoft.graphene.basecomponent.document.typeset.parser.TypesetParser;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.FormulaUtil;



public class TypesetUtil {
	public static final int MODE_ALWAYS = 1;
	public static final int MODE_FAIL = 2;

	private static TypesetParser typesetParser = new ParserXml();

	public static Typeset parseTypeset(InputStream inputStream) {
		return typesetParser.parse(inputStream);
	}

	private static int mode = MODE_ALWAYS;

	private static String resourcePath = "";

	private static Map<String, TypesetElementFactory> typesetElementFactory_map = new HashMap<>();

	public static void addElementFactory(String label, TypesetElementFactory typesetElementFactory) {
		typesetElementFactory_map.put(label, typesetElementFactory);
	}

	public static TypesetElementFactory getElementFactory(String label) {
		return typesetElementFactory_map.get(label);
	}


	public static void setTextDimension(TextDimension textDimension) {
		TypesetText.setTextDimension(textDimension);
	}

	public static int getMode() {
		return mode;
	}

	public static void setMode(int mode) {
		TypesetUtil.mode = mode;
	}

	public static Formula formulaOf(String formula) {
		return FormulaUtil.formulaOf(formula);
	}
	
	public static void setResourcePath(String path) {
		resourcePath = path;
	}

	//TODO other function
	public static String getResourcePath() {
		return resourcePath;
	}

}
