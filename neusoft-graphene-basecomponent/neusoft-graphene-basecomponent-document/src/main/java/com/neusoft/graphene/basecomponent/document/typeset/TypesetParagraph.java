package com.neusoft.graphene.basecomponent.document.typeset;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetElement;
import com.neusoft.graphene.basecomponent.formula.Formula;


/**
 * The definition of the document paragraph, include paper templates and paragraph elements.<p>
 *  
 * After a paragraph layout can generate one or more pages, paragraphs, arranged in sequence
 * elements in these pages.<p>
 * 
 * Paragraph of each page using the page template (including the header and footer, etc.), 
 * therefore, within the pages of the page paragraph style is the same, want to change the page 
 * style, it must be divided into multiple paragraphs.<p>
 * 
 * Adjacent two paragraphs if the page style is different, the last page of the preceding 
 * paragraphs and paragraphs of the first page of the back of a page can not be spliced.<p>

 * @author lerrain(lerrain@gmail.com)
 */

public class TypesetParagraph
{
	private Formula visible_Fromula;

	private TypesetPaper typesetPaper;

	private List<TypesetElement> elementList = new ArrayList<>();
	
	public void add(TypesetElement element)
	{
		elementList.add(element);
	}
	
	public int getElementSize()
	{
		return elementList.size();
	}
	
	public TypesetElement getElement(int index)
	{
		return elementList.get(index);
	}

	public Formula getVisible()
	{
		return visible_Fromula;
	}

	public void setVisible(Formula visible)
	{
		this.visible_Fromula = visible;
	}

	public TypesetPaper getTypesetPaper()
	{
		return typesetPaper;
	}

	public void setTypesetPaper(TypesetPaper typesetPaper)
	{
		this.typesetPaper = typesetPaper;
	}
}
