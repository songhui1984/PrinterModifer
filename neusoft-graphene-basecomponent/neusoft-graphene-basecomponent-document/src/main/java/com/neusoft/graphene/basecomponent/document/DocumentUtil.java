package com.neusoft.graphene.basecomponent.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.size.Paper;
import com.neusoft.graphene.basecomponent.document.size.PaperA4;
import com.neusoft.graphene.basecomponent.document.size.PaperA4Rotate;



public class DocumentUtil
{
	
	private static List<Paper> paperList = new ArrayList<>();

	private static Map<String,LexFontFamily> LexFontFamily_Map = new HashMap<>();
	
	static
	{
		paperList.add(new PaperA4());
		paperList.add(new PaperA4Rotate());
		
	}
	
	/**
	 * 按照名称获得纸张规格
	 * @param name 纸张规格名称A4/A3/...
	 * @return 纸张规格定义
	 */
	public static Paper getPaperSize(String name)
	{
		for (Paper paperSize : paperList) {
			if (paperSize.getPaperName().equalsIgnoreCase(name)) {
				return paperSize;
			}
		}
		
		return null;
	}
	
	
	public static LexFontFamily getFont(String fontName)
	{
		return LexFontFamily_Map.get(fontName);
	}
	
	
}
