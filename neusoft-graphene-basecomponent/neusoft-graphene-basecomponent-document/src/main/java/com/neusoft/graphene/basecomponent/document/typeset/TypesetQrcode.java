package com.neusoft.graphene.basecomponent.document.typeset;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.neusoft.graphene.basecomponent.document.element.DocumentImage;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.typeset.Typeset;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetElementFactory;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;
import com.neusoft.graphene.basecomponent.document.typeset.element.TypesetElement;
import com.neusoft.graphene.basecomponent.document.typeset.parser.ParserXml;
import com.neusoft.graphene.basecomponent.formula.Value;



public class TypesetQrcode extends TypesetElement implements TypesetElementFactory
{
	public TypesetQrcode()
	{
	}
	
	public DocumentElementAbs build(TypesetParameters tvs,ResourceOps ops)
	{
		DocumentImage dImage = new DocumentImage();
		
		try
		{
			String url = Value.stringOf(this.getValue(), tvs);
			if (url != null)
			{
				try
				{
					Map<EncodeHintType,String> hints = new HashMap<EncodeHintType,String>();
					hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

					MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
					BitMatrix bitMatrix = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);

					ByteArrayOutputStream os = new ByteArrayOutputStream();
					MatrixToImageWriter.writeToStream(bitMatrix, "png", os);
					os.close();

					dImage.addImageSource(os.toByteArray(), DocumentImage.TYPE_BIN);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		dImage.setSize(this.getWidth().value(tvs), this.getHeight().value(tvs));
		dImage.setLocation(this.getX_TypesetNumber().value(tvs), tvs.getDatum() + this.getY_TypesetNumber().value(tvs));
		
		resetY(tvs, dImage);
		
		return dImage;
	}

	@Override
	public TypesetElement parse(Typeset typeset, Object node)
	{
		TypesetQrcode qrcode = new TypesetQrcode();
		ParserXml.parseTypesetBase(typeset, (Element)node, qrcode);

		return qrcode;
	}
}
