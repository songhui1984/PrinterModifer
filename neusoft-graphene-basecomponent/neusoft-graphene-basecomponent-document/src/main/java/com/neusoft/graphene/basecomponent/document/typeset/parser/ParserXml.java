package com.neusoft.graphene.basecomponent.document.typeset.parser;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.neusoft.graphene.basecomponent.document.DocumentUtil;
import com.neusoft.graphene.basecomponent.document.LexFontFamily;
import com.neusoft.graphene.basecomponent.document.element.DocumentImage;
import com.neusoft.graphene.basecomponent.document.exception.TypesetParseException;
import com.neusoft.graphene.basecomponent.document.typeset.Typeset;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetElementFactory;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetFontFamily;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetNumber;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetPaper;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParagraph;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParagraphLoop;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetUtil;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetElement;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetImage;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetLayout;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetLine;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetLoop;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetPanel;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetRect;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetReset;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetScript;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetTable;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetText;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetWhen;
import com.neusoft.graphene.basecomponent.document.typeset.element.grid.SimpleGrid;
import com.neusoft.graphene.basecomponent.document.typeset.element.grid.TypesetGrid;
import com.neusoft.graphene.basecomponent.document.typeset.element.sheet.SimpleSheet;
import com.neusoft.graphene.basecomponent.document.typeset.element.sheet.TypesetSheet;
import com.neusoft.graphene.basecomponent.formula.Formula;
import com.neusoft.graphene.basecomponent.formula.FormulaUtil;

public class ParserXml implements TypesetParser {
	private static DocumentBuilder docBuilder;

	static {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			docBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public Typeset parse(InputStream inputStream) {
		try {
			return parseTypeset(docBuilder.parse(inputStream).getDocumentElement());
		} catch (Exception e) {
			throw new TypesetParseException("Fail to read typeset file.", e);
		}
	}
	
	/**
	 * 根据xml文件构建排版
	 * @param root
	 * @return
	 */
	private Typeset parseTypeset(Element root) {
		Typeset typeset = new Typeset();

		NodeList list = root.getChildNodes();
		for (int i = 0; list != null && i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;

			if ("paragraph".equals(node.getNodeName())) {
				typeset.addParagraph(parseParagraph(typeset, (Element) node));
			} else if ("loop".equals(node.getNodeName())) {
				Element e = (Element) node;

				Formula value = TypesetUtil.formulaOf(e.getAttribute("value"));

				Formula from = TypesetUtil.formulaOf(e.getAttribute("from"));
				Formula to = TypesetUtil.formulaOf(e.getAttribute("to"));
				Formula step = TypesetUtil.formulaOf(e.getAttribute("step"));

				String name = e.getAttribute("name");

				TypesetParagraphLoop loop = value == null ? new TypesetParagraphLoop(from, to, step, name)
						: new TypesetParagraphLoop(value, name);
				// 遍历loop里面的paragraph
				NodeList paras = node.getChildNodes();
				for (int j = 0; paras != null && j < paras.getLength(); j++) {
					Node para = paras.item(j);

					if ("paragraph".equals(para.getNodeName()))
						loop.addParagraph(parseParagraph(typeset, (Element) para));
				}

				typeset.addParagraph(loop);
			} else if ("page_setting".equals(node.getNodeName())) {
				NodeList list2 = node.getChildNodes();
				for (int j = 0; list2 != null && j < list2.getLength(); j++) {
					Node node2 = list2.item(j);

					if (node2.getNodeType() != Node.ELEMENT_NODE)
						continue;

					Element e2 = (Element) node2;
					TypesetPaper tp = parseTemplate(typeset, e2);
					if (tp != null) {
						String defaultStr = e2.getAttribute("default");

						if ("yes".equalsIgnoreCase(defaultStr))
							typeset.setPageDefineDefault(tp);

						typeset.addPageDefine(tp.getName(), tp);
					}
				}
			} else if ("fonts".equals(node.getNodeName()) || "font_mapping".equals(node.getNodeName())) {
				NodeList list2 = node.getChildNodes();
				for (int j = 0; list2 != null && j < list2.getLength(); j++) {
					Node node2 = list2.item(j);
					if (node2.getNodeType() != Node.ELEMENT_NODE)
						continue;

					Element e2 = (Element) node2;
					if ("font".equals(node2.getNodeName())) {
						String name = e2.getAttribute("name");
						String value = e2.getAttribute("value");

						if (!isEmpty(value)) {
							typeset.addFontFamily(new TypesetFontFamily(name, TypesetUtil.formulaOf(value)));
						}
					}
				}
			}
		}
		return typeset;
	}
	
	/**
	 * 构建每个段落
	 * @param typeset
	 * @param root
	 * @return
	 */
	private TypesetParagraph parseParagraph(Typeset typeset, Element root) {
		TypesetParagraph paragraph = new TypesetParagraph();

		String pageName = root.getAttribute("page");
		paragraph.setTypesetPaper(typeset.getPageDefine(pageName));

		String temp = root.getAttribute("visible");
		if (!isEmpty(temp))
			paragraph.setVisible(TypesetUtil.formulaOf(temp));

		NodeList list = root.getChildNodes();
		for (int i = 0; list != null && i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;

			TypesetElement ite = parseTypesetElement(typeset, (Element) node);
			if (ite != null)
				paragraph.add(ite);
		}
		return paragraph;
	}
	
	/**
	 * 构建段落的每个元素
	 * @param typeset
	 * @param node
	 * @return
	 */
	private TypesetElement parseTypesetElement(Typeset typeset, Element node) {
		if ("line".equals(node.getNodeName())) {
			String x1 = node.getAttribute("x1");
			String y1 = node.getAttribute("y1");
			String x2 = node.getAttribute("x2");
			String y2 = node.getAttribute("y2");

			TypesetLine line = new TypesetLine(x1, y1, x2, y2);
			parseTypesetBase(typeset, node, line);

			return line;
		} else if ("script".equals(node.getNodeName())) {
			TypesetScript s = new TypesetScript(FormulaUtil.formulaOf(node.getTextContent()));

			return s;
		} else if ("rect".equals(node.getNodeName())) {
			TypesetRect rect = new TypesetRect();
			parseTypesetBase(typeset, node, rect);

			return rect;
		} else if ("sheet".equals(node.getNodeName())) {
			TypesetSheet sheet = new TypesetSheet();
			parseTypesetBase(typeset, node, sheet);

			String header = node.getAttribute("header");
			if (!isEmpty(header))
				sheet.setHeader(TypesetUtil.formulaOf(header));

			String content = node.getAttribute("content");
			if (!isEmpty(content))
				sheet.setContent(TypesetUtil.formulaOf(content));

			String colWidth = node.getAttribute("col_width");
			if (!isEmpty(colWidth))
				sheet.setColWidth(TypesetUtil.formulaOf(colWidth));

			String colAlign = node.getAttribute("col_align");
			if (!isEmpty(colAlign))
				sheet.setColAlign(TypesetUtil.formulaOf(colAlign));

			String theme = (String) node.getAttribute("theme");
			if ("simple".equals(theme))
				sheet.setTheme(new SimpleSheet());

			String measure = node.getAttribute("measure");
			if (!isEmpty(measure)) {
				if ("title".equalsIgnoreCase(measure))
					sheet.setMeasureMode(1);
				else if ("content".equalsIgnoreCase(measure))
					sheet.setMeasureMode(2);
				else if ("max".equalsIgnoreCase(measure))
					sheet.setMeasureMode(3);
				else if ("equal".equalsIgnoreCase(measure))
					sheet.setMeasureMode(4);
			}

			return sheet;
		} else if ("table".equals(node.getNodeName())) // 作废
		{
			TypesetTable table = new TypesetTable();
			parseTypesetBase(typeset, node, table);

			String header = node.getAttribute("header");
			if (!isEmpty(header))
				table.setHeader(TypesetUtil.formulaOf(header));

			String content = node.getAttribute("content");
			if (!isEmpty(content))
				table.setContent(TypesetUtil.formulaOf(content));

			String measure = node.getAttribute("measure");
			if (!isEmpty(measure)) {
				if ("title".equalsIgnoreCase(measure))
					table.setMeasureMode(1);
				else if ("content".equalsIgnoreCase(measure))
					table.setMeasureMode(2);
				else if ("max".equalsIgnoreCase(measure))
					table.setMeasureMode(3);
				else if ("equal".equalsIgnoreCase(measure))
					table.setMeasureMode(4);
			}

			return table;
		} else if ("grid".equals(node.getNodeName())) // 作废
		{
			TypesetGrid table = new TypesetGrid();
			parseTypesetBase(typeset, node, table);

			String header = node.getAttribute("header");
			if (!isEmpty(header))
				table.setHeader(TypesetUtil.formulaOf(header));

			String content = node.getAttribute("content");
			if (!isEmpty(content))
				table.setContent(TypesetUtil.formulaOf(content));

			String colWidth = node.getAttribute("col_width");
			if (!isEmpty(colWidth))
				table.setColWidth(TypesetUtil.formulaOf(colWidth));

			String colAlign = node.getAttribute("col_align");
			if (!isEmpty(colAlign))
				table.setColAlign(TypesetUtil.formulaOf(colAlign));

			String theme = (String) node.getAttribute("theme");
			if ("simple".equals(theme))
				table.setTheme(new SimpleGrid());

			String measure = node.getAttribute("measure");
			if (!isEmpty(measure)) {
				if ("title".equalsIgnoreCase(measure))
					table.setMeasureMode(1);
				else if ("content".equalsIgnoreCase(measure))
					table.setMeasureMode(2);
				else if ("max".equalsIgnoreCase(measure))
					table.setMeasureMode(3);
				else if ("equal".equalsIgnoreCase(measure))
					table.setMeasureMode(4);
			}

			return table;
		} else if ("text".equals(node.getNodeName())) {
			TypesetText text = new TypesetText();
			parseTypesetBase(typeset, node, text);

			if (node.hasAttribute("underline"))
				text.setUnderline(node.getAttribute("underline"));

			return text;
		} else if ("image".equals(node.getNodeName()) || "sign".equals(node.getNodeName())) {
			TypesetImage image = new TypesetImage();
			parseTypesetBase(typeset, node, image);

			String src = node.getAttribute("src");
			String path = node.getAttribute("path");
			String file = node.getAttribute("file");
			String url = node.getAttribute("url");
			String other = node.getAttribute("other");

			if (!isEmpty(src)) {
				Formula vf = TypesetUtil.formulaOf("RESOURCE_PATH + (" + src + ")");
				image.addImageSource(vf, DocumentImage.TYPE_SRC);
			}
			if (!isEmpty(path)) {
				Formula vf = TypesetUtil.formulaOf(path);
				image.addImageSource(vf, DocumentImage.TYPE_SRC);
			}
			if (!isEmpty(file)) {
				Formula vf = TypesetUtil.formulaOf(file);
				image.addImageSource(vf, DocumentImage.TYPE_FILE);
			}
			if (!isEmpty(url)) {
				Formula vf = TypesetUtil.formulaOf(url);
				image.addImageSource(vf, DocumentImage.TYPE_URL);
			}
			if (!isEmpty(other)) {
				Formula vf = TypesetUtil.formulaOf(other);
				image.addImageSource(vf, DocumentImage.TYPE_OTHER);
			}

			if ("sign".equals(node.getNodeName()))
				image.setSign(true);

			return image;
		} else if ("reset".equals(node.getNodeName())) {
			String skip = node.getAttribute("y");
			if (isEmpty(skip))
				skip = node.getAttribute("skip");

			boolean newPage = "yes".equalsIgnoreCase(node.getAttribute("new_page"));
			TypesetReset reset = isEmpty(skip) ? new TypesetReset(newPage)
					: new TypesetReset(TypesetNumber.numberOf(skip), newPage);

			String condition = node.getAttribute("condition");
			if (isEmpty(condition))
				condition = node.getAttribute("c");
			reset.setCondition(TypesetUtil.formulaOf(condition));

			return reset;
		} else if ("if".equals(node.getNodeName())) {
			String condition = node.getAttribute("condition");
			if (isEmpty(condition))
				condition = node.getAttribute("c");

			TypesetWhen when = new TypesetWhen(TypesetUtil.formulaOf(condition));
			parseTypesetBase(typeset, node, when);

			NodeList list = node.getChildNodes();
			for (int i = 0; list != null && i < list.getLength(); i++) {
				Node node2 = list.item(i);
				if (node2.getNodeType() != Node.ELEMENT_NODE)
					continue;

				TypesetElement ite = parseTypesetElement(typeset, (Element) node2);
				if (ite != null)
					when.add(ite);
			}

			return when;
		} else if ("layout".equals(node.getNodeName())) {
			int mode = TypesetLayout.MODE_TEXT;
			String modeStr = node.getAttribute("mode");
			if ("text".equalsIgnoreCase(modeStr))
				mode = TypesetLayout.MODE_TEXT;

			int baseline = TypesetLayout.BASELINE_BOTTOM;
			String bsstr = node.getAttribute("baseline");
			if ("bottom".equalsIgnoreCase(bsstr))
				baseline = TypesetLayout.BASELINE_BOTTOM;
			else if ("top".equalsIgnoreCase(bsstr))
				baseline = TypesetLayout.BASELINE_TOP;
			else if ("center".equalsIgnoreCase(bsstr) || "middle".equalsIgnoreCase(bsstr))
				baseline = TypesetLayout.BASELINE_CENTER;

			TypesetLayout layout = new TypesetLayout(mode);
			layout.setBaseLine(baseline);
			parseTypesetBase(typeset, node, layout);

			NodeList list = node.getChildNodes();
			for (int i = 0; list != null && i < list.getLength(); i++) {
				Node node2 = list.item(i);
				if (node2.getNodeType() != Node.ELEMENT_NODE)
					continue;

				TypesetElement ite = parseTypesetElement(typeset, (Element) node2);
				if (ite != null)
					layout.add(ite);
			}

			return layout;
		} else if ("loop".equals(node.getNodeName())) {
			Formula from = TypesetUtil.formulaOf(node.getAttribute("from"));
			Formula to = TypesetUtil.formulaOf(node.getAttribute("to"));

			String stepStr = node.getAttribute("step");
			Formula step = isEmpty(stepStr) ? null : TypesetUtil.formulaOf(stepStr);

			String name = node.getAttribute("name");

			TypesetLoop loop = new TypesetLoop(from, to, step, name);
			parseTypesetBase(typeset, node, loop);

			NodeList list = node.getChildNodes();
			for (int i = 0; list != null && i < list.getLength(); i++) {
				Node node2 = list.item(i);
				if (node2.getNodeType() != Node.ELEMENT_NODE)
					continue;

				TypesetElement ite = parseTypesetElement(typeset, (Element) node2);
				if (ite != null)
					loop.add(ite);
			}

			return loop;
		} else {
			TypesetElementFactory tef = TypesetUtil.getElementFactory(node.getNodeName());
			if (tef != null)
				return tef.parse(typeset, node);
		}

		return null;
	}

	public static void parseTypesetBase(Typeset typeset, Element node, TypesetElement text) {
		String temp;

		temp = node.getAttribute("x");
		if (!isEmpty(temp))
			text.setX(temp);
		temp = node.getAttribute("y");
		if (!isEmpty(temp))
			text.setY(temp);
		temp = node.getAttribute("width");
		if (!isEmpty(temp))
			text.setWidth(temp);
		temp = node.getAttribute("height");
		if (!isEmpty(temp))
			text.setHeight(temp);

		temp = node.getAttribute("color");
		if (!isEmpty(temp))
			text.setColor(TypesetElement.getColor(temp));
		temp = node.getAttribute("bgcolor");
		if (!isEmpty(temp))
			text.setBgColor(TypesetElement.getColor(temp));
		temp = node.getAttribute("border_color");
		if (!isEmpty(temp))
			text.setBorderColor(TypesetElement.getColor(temp));

		temp = node.getAttribute("style");
		if (!isEmpty(temp))
			text.setStyle(temp);

		temp = node.getAttribute("mode");
		if (!isEmpty(temp)) {
			if ("reset_at_final".equalsIgnoreCase(temp))
				text.setMode(text.getMode() | TypesetElement.MODE_RESET_AT_FINAL);
		}

		temp = node.getAttribute("condition");
		if (isEmpty(temp))
			temp = node.getAttribute("c");
		if (!isEmpty(temp))
			text.setCondition(TypesetUtil.formulaOf(temp));

		temp = node.getAttribute("margin");
		if (!isEmpty(temp)) {
			int[] margin = new int[4];
			if (temp.indexOf(",") >= 0) {
				String[] s = temp.split(",");
				for (int i = 0; i < s.length; i++)
					margin[i] = Integer.parseInt(s[i]);
			} else {
				for (int i = 0; i < 4; i++)
					margin[i] = Integer.parseInt(temp);
			}
			text.setMargin(margin);
		}

		temp = node.getAttribute("align");
		if (!isEmpty(temp)) {
			if (temp.indexOf(",") >= 0) {
				String[] s = temp.split(",");
				text.setAlign(s[0]);
				text.setVerticalAlign(s[1]);
			} else {
				if ("top".equals(temp) || "middle".equals(temp) || "bottom".equals(temp))
					text.setVerticalAlign(temp);
				else
					text.setAlign(temp);
			}
		}
		temp = node.getAttribute("valign");
		if (!isEmpty(temp))
			text.setVerticalAlign(temp);
		temp = node.getAttribute("vertical-align");
		if (!isEmpty(temp))
			text.setVerticalAlign(temp);

		temp = node.getAttribute("value");
		if ((text instanceof TypesetText || text instanceof TypesetImage) && isEmpty(temp)) {
			temp = node.getTextContent();
		}
		if (!isEmpty(temp)) {
			temp = temp.replaceAll("\\\\n", "\n");
//			System.out.println("temp:------》"+temp);
			text.setValue(TypesetUtil.formulaOf(temp));
		}

		temp = node.getAttribute("border");
		if (!isEmpty(temp)) {
			text.setTopBorder(temp);
			text.setBottomBorder(temp);
			text.setLeftBorder(temp);
			text.setRightBorder(temp);
		}
		temp = node.getAttribute("top_border");
		if (!isEmpty(temp))
			text.setTopBorder(temp);
		temp = node.getAttribute("bottom_border");
		if (!isEmpty(temp))
			text.setBottomBorder(temp);
		temp = node.getAttribute("left_border");
		if (!isEmpty(temp))
			text.setLeftBorder(temp);
		temp = node.getAttribute("right_border");
		if (!isEmpty(temp))
			text.setRightBorder(temp);

		text.setFixed("yes".equalsIgnoreCase(node.getAttribute("fixed")));
		text.setSplit(!"no".equalsIgnoreCase(node.getAttribute("split")));

		String font = node.getAttribute("font");
		if (!isEmpty(font)) {
			String[] s = font.split("[:]");
			LexFontFamily f = typeset.getFontFamily(s[0]);
			if (f != null) {
				if (s.length == 1) {
					String fontSize = node.getAttribute("font_size");
					if (isEmpty(fontSize))
						fontSize = "20";
					text.setFont(f.derive(Integer.parseInt(fontSize)));
				} else {
					text.setFont(f.derive(Integer.parseInt(s[1])));
				}
			}
		}

		String lineHeight = node.getAttribute("line-height");
		if (!isEmpty(lineHeight))
			text.setLineHeight(Integer.parseInt(lineHeight));
	}

	

	private TypesetPaper parseTemplate(Typeset typeset, Element root) {
		TypesetPaper template = null;

		if ("page".equals(root.getNodeName())) {
			template = new TypesetPaper();

			String pageName = root.getAttribute("name");
			String pageSize = root.getAttribute("size");
			String cx = root.getAttribute("x");
			String cy = root.getAttribute("y");
			String cw = root.getAttribute("width");
			String ch = root.getAttribute("height");

			template.setName(pageName);
			template.setPaper(DocumentUtil.getPaperSize(pageSize));

			TypesetPanel body = new TypesetPanel(cx, cy, cw, ch);
			template.setBody(body);

			NodeList list = root.getChildNodes();
			for (int i = 0; list != null && i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getNodeType() != Node.ELEMENT_NODE)
					continue;

				Element e = (Element) node;
				if ("template".equals(node.getNodeName())) {
					String x = e.getAttribute("x");
					String y = e.getAttribute("y");
					String w = e.getAttribute("width");
					String h = e.getAttribute("height");

					TypesetPanel tp = new TypesetPanel(x, y, w, h);

					NodeList list2 = node.getChildNodes();
					for (int j = 0; list2 != null && j < list2.getLength(); j++) {
						Node node2 = list2.item(j);
						if (node2.getNodeType() != Node.ELEMENT_NODE)
							continue;

						Element e2 = (Element) node2;
						TypesetElement ite = parseTypesetElement(typeset, e2);
						if (ite != null)
							tp.add(ite);
					}

					template.addTemplate(tp);
				}
			}
		}

		return template;
	}

	

	private static boolean isEmpty(String s) {
		return s == null || "".equals(s.trim());
	}
}
