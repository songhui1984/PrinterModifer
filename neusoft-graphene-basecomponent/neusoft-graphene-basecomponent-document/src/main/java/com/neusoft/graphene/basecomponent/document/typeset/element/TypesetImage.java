package com.neusoft.graphene.basecomponent.document.typeset.element;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.graphene.basecomponent.document.element.DocumentImage;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;
import com.neusoft.graphene.basecomponent.formula.Formula;


/**
 * 
 * @author za-songhui001
 *
 */
public class TypesetImage extends TypesetElement
{
	private boolean sign = false;
	
	private Map<Integer,Formula> imgSrcMap = new HashMap<Integer,Formula>();
	
	public void addImageSource(Formula src, int type)
	{
		imgSrcMap.put(new Integer(type), src);
	}
	
	public boolean hasType(int type)
	{
		return imgSrcMap.containsKey(new Integer(type));
	}
	
	public Formula getImageSource(int type)
	{
		return imgSrcMap.get(new Integer(type));
	}
	
	public DocumentElementAbs build(TypesetParameters tvs,ResourceOps ops)
	{
		DocumentImage dImage = new DocumentImage();
		dImage.setSign(sign);
		
		try
		{
			if (hasType(DocumentImage.TYPE_URL))
				dImage.addImageSource(getImageSource(DocumentImage.TYPE_URL).run(tvs), DocumentImage.TYPE_URL);
			if (hasType(DocumentImage.TYPE_PATH))
				dImage.addImageSource(getImageSource(DocumentImage.TYPE_PATH).run(tvs), DocumentImage.TYPE_PATH);
			if (hasType(DocumentImage.TYPE_SRC))
				dImage.addImageSource(getImageSource(DocumentImage.TYPE_SRC).run(tvs), DocumentImage.TYPE_SRC);
			if (hasType(DocumentImage.TYPE_FILE))
				dImage.addImageSource(getImageSource(DocumentImage.TYPE_FILE).run(tvs), DocumentImage.TYPE_FILE);
			if (hasType(DocumentImage.TYPE_OTHER))
				dImage.addImageSource(getImageSource(DocumentImage.TYPE_OTHER).run(tvs), DocumentImage.TYPE_OTHER);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		dImage.setSize(super.getWidth().value(tvs), super.getHeight().value(tvs));
		dImage.setLocation(super.getX_TypesetNumber().value(tvs), tvs.getDatum() + super.getY_TypesetNumber().value(tvs));
		
		resetY(tvs, dImage);
		
		return dImage;
	}

	public boolean isSign()
	{
		return sign;
	}

	public void setSign(boolean sign)
	{
		this.sign = sign;
	}
}
