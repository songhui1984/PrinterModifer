package com.neusoft.graphene.basecomponent.document.typeset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.DocumentUtil;
import com.neusoft.graphene.basecomponent.document.LexFontFamily;

/**
 * 排版
 * 
 * @author za-songhui001
 *
 */
//this map with template


public class Typeset implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	private Map<String, LexFontFamily> fontFamilyMap = new HashMap<String, LexFontFamily>();

	private Map<String, TypesetPaper> pageMap = new HashMap<String, TypesetPaper>();

	private List<Object> paragraphList = new ArrayList<Object>();

	public void addParagraph(Object paragraph) {
		paragraphList.add(paragraph);
	}

	public int getParagraphNum() {
		return paragraphList.size();
	}

	public Object getParagraph(int index) {
		return paragraphList.get(index);
	}

	public void addFontFamily(TypesetFontFamily ff) {
		fontFamilyMap.put(ff.getName(), ff);
	}

	public Map<String, LexFontFamily> getFontFamilyMap() {
		return fontFamilyMap;
	}

	public LexFontFamily getFontFamily(String name) {
		LexFontFamily lexFontFamily = fontFamilyMap.get(name);
		if (lexFontFamily == null)
			lexFontFamily = DocumentUtil.getFont(name);

		return lexFontFamily;
	}

	public void addPageDefine(String name, TypesetPaper pageDef) {
		pageMap.put(name, pageDef);
	}

	public TypesetPaper getPageDefine(String name) {
		return  pageMap.get(name);
	}

	public void setPageDefineDefault(TypesetPaper pageDef) {
		pageMap.put("@default", pageDef);
	}

	public TypesetPaper getPageDefineDefault() {
		return pageMap.get("@default");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
