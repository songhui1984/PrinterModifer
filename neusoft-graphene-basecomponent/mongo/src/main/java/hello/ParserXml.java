package hello;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParserXml {
	static DocumentBuilderFactory dbf;
	static DocumentBuilder docBuilder;

	static {
		try {
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			docBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static Element parse(String context) {
		try {
			Element element =	docBuilder.parse(new ByteArrayInputStream(context.getBytes())).getDocumentElement();
			return element;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
