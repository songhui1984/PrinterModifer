package com.neusoft.graphene.basecomponent.printer.export.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.LexColor;
import com.neusoft.graphene.basecomponent.document.LexDocument;
import com.neusoft.graphene.basecomponent.document.LexPage;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentImage;
import com.neusoft.graphene.basecomponent.document.element.DocumentLine;
import com.neusoft.graphene.basecomponent.document.element.DocumentPanel;
import com.neusoft.graphene.basecomponent.document.element.DocumentRect;
import com.neusoft.graphene.basecomponent.document.element.DocumentText;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.exception.DocumentExportException;
import com.neusoft.graphene.basecomponent.document.export.Painter;
import com.neusoft.graphene.basecomponent.document.size.Paper;

/**
 * 预览模式使用
 * @author za-songhui001
 *
 */
public class ObjectPainter implements Painter {
	
	@SuppressWarnings("unchecked")
	public void paint(LexDocument doc, Object canvas_pageList, int canvasType,ResourceOps ops) {
		
		if (canvasType != Painter.OBJECT)
			throw new DocumentExportException("object输出器不支持的输出格式");

		build(doc, (List<Map<String, Object>>) canvas_pageList);
	}

	public void build(LexDocument lexDocument, List<Map<String, Object>> pageList) {

		int size = lexDocument.pageSize();

		for (int i = 0; i < size; i++) {
			Map<String, Object> map = new HashMap<>();
			
			LexPage lexPage = lexDocument.getPage(i);
			Paper paper = lexPage.getPaper();

			map.put("w", paper.getWidth());
			map.put("h", paper.getHeight());
			map.put("bgcolor", "FFFFFF");

			map.put("image", new ArrayList());
			map.put("line", new ArrayList());
			map.put("rect", new ArrayList());
			map.put("text", new ArrayList());
			map.put("panel", new ArrayList());

			int elementCount = lexPage.getElementNum();
			for (int j = 0; j < elementCount; j++) {
				DocumentElementAbs element = lexPage.getElement(j);
				translateElement(map, 0, 0, element);
			}

			pageList.add(map);
		}
	}

	private void translateElement(Map<String, Object> map, int x, int y, DocumentElementAbs element) {
		if (element instanceof DocumentRect) {
			addElement(map, "rect", x, y, element);
		} else if (element instanceof DocumentLine) {
			addElement(map, "line", x, y, element);
		} else if (element instanceof DocumentImage) {
			DocumentImage dImage = (DocumentImage) element;
			
			//Request URL:http://127.0.0.1:8081/resource/template/iyb_quote/photo1.jpg
			Map<String, Object> imageMap = addElement(map, "image", x, y, dImage);

			if (dImage.hasTypeImage(DocumentImage.TYPE_URL)) {
				imageMap.put("url", dImage.getImageByType(DocumentImage.TYPE_URL));
			}
			if (dImage.hasTypeImage(DocumentImage.TYPE_PATH)) {
				imageMap.put("path", dImage.getImageByType(DocumentImage.TYPE_PATH));
			}

		} else if (element instanceof DocumentText) {
			DocumentText dText = (DocumentText) element;
			Map<String, Object> textMap = addElement(map, "text", x, y, dText);

			textMap.put("v", dText.getText());

			if (dText.getFont() != null) {
				textMap.put("f", dText.getFont().getFamily().getName());
				textMap.put("fs", dText.getFont().getSize());
			} else {
				textMap.put("f", "song");
				textMap.put("fs", 30);
			}

			textMap.put("lh", dText.getLineHeight());
			textMap.put("va", dText.getVerticalAlign() == DocumentText.ALIGN_MIDDLE ? new Integer(0)
					: dText.getVerticalAlign() == DocumentText.ALIGN_BOTTOM ? new Integer(1) : new Integer(-1));
			textMap.put("ha", dText.getHorizontalAlign() == DocumentText.ALIGN_CENTER ? new Integer(0)
					: dText.getHorizontalAlign() == DocumentText.ALIGN_RIGHT ? new Integer(1) : new Integer(-1));

		} else if (element instanceof DocumentPanel) {
			addElement(map, "panel", x, y, element);

			DocumentPanel dPanel = (DocumentPanel) element;
			int count = dPanel.getElementCount();

			for (int i = 0; i < count; i++)
				translateElement(map, x + dPanel.getX(), y + dPanel.getY(), dPanel.getElement(i));
		}
	}

	private Map<String, Object> addElement(Map<String, Object> listMap, String type, int x, int y,
			DocumentElementAbs element) {
		int sx = x + element.getX();
		int sy = y + element.getY();
		int sw = element.getWidth();
		int sh = element.getHeight();

		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("x", sx);
		objectMap.put("y", sy);
		objectMap.put("w", sw);
		objectMap.put("h", sh);
		objectMap.put("c", translate(element.getColor()));
		objectMap.put("bg", translate(element.getBgColor()));
		objectMap.put("bdc", translate(element.getBorderColor()));
		objectMap.put("bd", new int[] { element.getLeftBorder(), element.getTopBorder(), element.getRightBorder(),
				element.getBottomBorder() });

		List<Map<String, Object>> list = (List<Map<String, Object>>) listMap.get(type);
		list.add(objectMap);

		return objectMap;
	}

	private String translate(LexColor color) {
		if (color == null)
			return null;
		
		String r = (color.getRed() < 16 ? "0" : "") + Integer.toHexString(color.getRed());
		String g = (color.getGreen() < 16 ? "0" : "") + Integer.toHexString(color.getGreen());
		String b = (color.getBlue() < 16 ? "0" : "") + Integer.toHexString(color.getBlue());
		return r + g + b;

	}

	
}
