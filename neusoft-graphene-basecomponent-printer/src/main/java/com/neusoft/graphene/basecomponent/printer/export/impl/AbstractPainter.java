package com.neusoft.graphene.basecomponent.printer.export.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.neusoft.graphene.basecomponent.document.LexColor;
import com.neusoft.graphene.basecomponent.document.LexFont;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.*;
import com.neusoft.graphene.basecomponent.document.export.Painter;
import com.neusoft.graphene.basecomponent.document.size.Paper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Map;

public abstract class AbstractPainter implements Painter {

    Map<String, BaseFont> baseFontMap;

    private void handelDocumentText(PdfContentByte pdf, DocumentText dText, float sx, float sy, float sw, float sh) throws DocumentException {

        BaseFont textFont = null;

        LexFont f = dText.getFont();
        if (f != null) {
            textFont = baseFontMap.get(f.getFamily().getPath());
        }

        float fontSize = dText.getFont() == null ? 32 : dText.getFont().getSize();
        Font font = new Font(textFont, scale(fontSize), Font.NORMAL, translate(dText.getColor()));

        String text = dText.getText();
        if (text == null)
            text = "";


        if (!LexColor.WHITE.equals(dText.getBgColor()) && dText.getBgColor() != null) {
            pdf.setColorFill(translate(dText.getBgColor()));
            pdf.rectangle(sx, sy, sw, sh);
            pdf.fill();
        }

        float y1 = font.getBaseFont().getAscentPoint(text, scale(fontSize));
        float y2 = font.getBaseFont().getDescentPoint(text, scale(fontSize));

        ColumnText colText = new ColumnText(pdf);
        colText.setAlignment(Element.ALIGN_MIDDLE);
        colText.setSimpleColumn(new Phrase(text, font), sx, sy, sx + sw, sy + sh, (sh - (y1 - y2)) / 2 + y1, Element.ALIGN_CENTER);
        colText.go();

        if (dText.getUnderline() != null) {
            pdf.setColorStroke(translate(dText.getColor()));
            pdf.setLineWidth(scale(1));
            pdf.moveTo(sx, sy + 1);
            pdf.lineTo(sx + sw, sy + 1);
            pdf.stroke();
        }
    }


    private void handelDocumentLine(PdfContentByte pdfContentByte, DocumentLine element, float sx, float sy, float sw, float sh) {

        pdfContentByte.setColorStroke(translate(element.getColor()));
        pdfContentByte.moveTo(sx, sy);
        pdfContentByte.lineTo(sx + sw, sy + sh);
        pdfContentByte.stroke();
    }


    private void handelDocumentImage(Document document, int pageNum, DocumentImage dImage, Map<String, Object> appendMap, ResourceOps ops, float sx, float sy, float sw, float sh) throws IOException, DocumentException {
        Object imageDst = null;

        if (dImage.hasTypeImage(DocumentImage.TYPE_FILE)) {
            imageDst = dImage.getImageByType(DocumentImage.TYPE_FILE);
        } else if (dImage.hasTypeImage(DocumentImage.TYPE_SRC)) {
            imageDst = new File((String) dImage.getImageByType(DocumentImage.TYPE_SRC));
        } else if (dImage.hasTypeImage(DocumentImage.TYPE_PATH)) {
            imageDst = new File((String) dImage.getImageByType(DocumentImage.TYPE_PATH));
        } else if (dImage.hasTypeImage(DocumentImage.TYPE_BIN)) {
            imageDst = dImage.getImageByType(DocumentImage.TYPE_BIN);
        }

        Image image = null;

        if (imageDst instanceof File) {
            File imageFile = (File) imageDst;

            Path path = imageFile.toPath();

            String parent = path.subpath(path.getParent().getParent().getNameCount(), path.getParent().getNameCount()).toString();
            String self = path.subpath(path.getParent().getNameCount(), path.getNameCount()).toString();

            String resourceName = parent + "_" + self;

            image = (Image) appendMap.get("image:" + resourceName);
            if (image == null) {
                image = Image.getInstance(IOUtils.toByteArray(ops.getByFileName(resourceName)));
                appendMap.put("image:" + resourceName, image);
            }
        } else if (imageDst instanceof byte[]) {
            byte[] b = (byte[]) imageDst;
            image = Image.getInstance(b);
        }

        if (image != null) {
            if (dImage.isSign()) {
                appendMap.put("sign/image", image);
                appendMap.put("sign/rect", new Rectangle(sx, sy, sx + sw, sy + sh));
                appendMap.put("sign/page", pageNum);
            } else {
                image.setAbsolutePosition(sx, sy);
                image.scaleAbsolute(sw, sh);

                document.add(image);
            }
        }
    }

    void translateElement(PdfContentByte pdfContentByte, Document document, int pageNum, Paper paper, int x, int y, DocumentElementAbs element, Map<String, Object> appendMap, ResourceOps ops) throws DocumentException, MalformedURLException, IOException {
        float sx = scale(x + element.getX());
        float sy = scale(paper.getHeight() - y - element.getY() - element.getHeight());
        float sw = scale(element.getWidth());
        float sh = scale(element.getHeight());

        if (element instanceof DocumentRect) {
            DocumentRect dRect = (DocumentRect) element;

            pdfContentByte.setColorFill(translate(dRect.getColor()));
            pdfContentByte.rectangle(sx, sy, sw, sh);
            pdfContentByte.fill();
        } else if (element instanceof DocumentLine) {
            handelDocumentLine(pdfContentByte, (DocumentLine) element, sx, sy, sw, sh);
        } else if (element instanceof DocumentImage) {
            handelDocumentImage(document, pageNum, (DocumentImage) element, appendMap, ops, sx, sy, sw, sh);
        } else if (element instanceof DocumentText) {
            handelDocumentText(pdfContentByte, (DocumentText) element, sx, sy, sw, sh);
        } else if (element instanceof DocumentPanel) {
            handelDocumentPanel(pdfContentByte, document, pageNum, paper, x, y, (DocumentPanel) element, appendMap, ops, sx, sy, sw, sh);
        }
    }

    private void handelDocumentPanel(PdfContentByte pdfContentByte, Document document, int pageNum, Paper paper, int x, int y, DocumentPanel dPanel, Map<String, Object> appendMap, ResourceOps ops, float sx, float sy, float sw, float sh) throws DocumentException, IOException {

        if (!LexColor.WHITE.equals(dPanel.getBgColor()) && dPanel.getBgColor() != null) {
            pdfContentByte.setColorFill(translate(dPanel.getBgColor()));
            pdfContentByte.rectangle(sx, sy, sw, sh);
            pdfContentByte.fill();
        }

        pdfContentByte.setColorStroke(translate(dPanel.getBorderColor()));
        pdfContentByte.setLineWidth(scale(1));

        if (dPanel.getLeftBorder() >= 0) {
            pdfContentByte.moveTo(sx, sy);
            pdfContentByte.lineTo(sx, sy + sh);
            pdfContentByte.stroke();
        }

        if (dPanel.getRightBorder() >= 0) {
            pdfContentByte.moveTo(sx + sw, sy);
            pdfContentByte.lineTo(sx + sw, sy + sh);
            pdfContentByte.stroke();
        }

        if (dPanel.getTopBorder() >= 0) {
            pdfContentByte.moveTo(sx, sy + sh);
            pdfContentByte.lineTo(sx + sw, sy + sh);
            pdfContentByte.stroke();
        }

        if (dPanel.getBottomBorder() >= 0) {
            pdfContentByte.moveTo(sx, sy);
            pdfContentByte.lineTo(sx + sw, sy);
            pdfContentByte.stroke();
        }

        int count = dPanel.getElementCount();
        for (int i = 0; i < count; i++)
            translateElement(pdfContentByte, document, pageNum, paper, x + dPanel.getX(), y + dPanel.getY(), dPanel.getElement(i), appendMap, ops);
    }


    public BaseColor translate(LexColor color) {

        if (color != null)
            return new BaseColor(color.getRed(), color.getGreen(), color.getBlue());

        return BaseColor.WHITE;
    }

    public static float scale(float mm10) {
        return (72f * mm10 / 100 / 2.54f);
    }

}
