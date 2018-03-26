package com.neusoft.graphene.basecomponent.printer.export.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.Document;//这是iText库中最常用的类，它代表了一个pdf实例。
//如果你需要从零开始生成一个PDF文件，你需要使用这个Document类。首先创建（new）该实例，然后打开（open）它，并添加（add）内容，最后关闭（close）该实例，即可生成一个pdf文件
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;//当这个PdfWriter被添加到PdfDocument后，所有添加到Document的内容将会写入到与文件或网络关联的输出流中。
import com.neusoft.graphene.basecomponent.document.LexColor;
import com.neusoft.graphene.basecomponent.document.LexDocument;
import com.neusoft.graphene.basecomponent.document.LexPage;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.element.DocumentElementAbs;
import com.neusoft.graphene.basecomponent.document.element.DocumentImage;
import com.neusoft.graphene.basecomponent.document.element.DocumentLine;
import com.neusoft.graphene.basecomponent.document.element.DocumentPanel;
import com.neusoft.graphene.basecomponent.document.element.DocumentRect;
import com.neusoft.graphene.basecomponent.document.element.DocumentText;
import com.neusoft.graphene.basecomponent.document.exception.DocumentExportException;
import com.neusoft.graphene.basecomponent.document.export.Painter;
import com.neusoft.graphene.basecomponent.document.size.Paper;

public class PdfPainterNDF extends AbstractPainter {

    public PdfPainterNDF(Map<String, BaseFont> baseFontMap) {
        this.baseFontMap = baseFontMap;
    }

    public void paint(LexDocument doc, Object canvas, int canvasType, ResourceOps ops) {
        if (canvasType == Painter.AUTO) {
            if (canvas instanceof File)
                canvasType = Painter.FILE;
            else if (canvas instanceof String)
                canvasType = Painter.FILEPATH;
            else if (canvas instanceof OutputStream)
                canvasType = Painter.STREAM;
        }

        if (canvasType == Painter.STREAM) {
            draw(doc, (OutputStream) canvas, ops);
        } else if (canvasType == Painter.FILE || canvasType == Painter.FILEPATH) {
            File file = canvasType == Painter.FILE ? (File) canvas : new File((String) canvas);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                draw(doc, fos, ops);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                }
            }
        } else {
            throw new DocumentExportException("不支持的导出格式");
        }
    }


    private void draw(LexDocument doc, OutputStream outputStream, ResourceOps ops) throws DocumentExportException {
        Map<String, Object> appendMap = new HashMap<String, Object>();

        PdfContentByte pdfContentByte;
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);

            document.open();//OPEN ADD CLOSE   document.add(new Paragraph("A Hello World PDF document."));

            int pageNum = doc.pageSize();
            for (int i = 0; i < pageNum; i++) {
                LexPage page = doc.getPage(i);

                if ("A4".equals(page.getPaper().getPaperName()))
                    document.setPageSize(PageSize.A4);
                else
                    document.setPageSize(PageSize.A4.rotate());
                document.newPage();

                pdfContentByte = pdfWriter.getDirectContent();

                int eNum = page.getElementNum();

                for (int j = 0; j < eNum; j++) {
                    DocumentElementAbs element = page.getElement(j);

                    translateElement(pdfContentByte, document,0, page.getPaper(), 0, 0, element, appendMap, ops);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }





}
