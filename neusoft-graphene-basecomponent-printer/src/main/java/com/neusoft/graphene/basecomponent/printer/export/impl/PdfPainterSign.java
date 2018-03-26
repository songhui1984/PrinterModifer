package com.neusoft.graphene.basecomponent.printer.export.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

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

public class PdfPainterSign extends AbstractPainter {

//	private String tempDir;

    private PrivateKey pk;
    private Certificate[] chain;

    private String scope, reason, location;

    public PdfPainterSign(Map<String, BaseFont> baseFontMap, ResourceOps ops, String keystore, String password, String scope, String reason, String location) {
//		this.tempDir = tempDir;
        this.scope = scope;
        this.reason = reason;
        this.location = location;
        this.baseFontMap = baseFontMap;

        char[] pwd = password.toCharArray();

//		try (InputStream keyIs = new FileInputStream(keystore))
        try (InputStream keyIs = ops.getByFileName(keystore)) {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(keyIs, pwd);
            String alias = ks.aliases().nextElement();
            pk = (PrivateKey) ks.getKey(alias, pwd);
            chain = ks.getCertificateChain(alias);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void paint(LexDocument doc, Object canvas, int canvasType, ResourceOps ops) {

        String filePath = "/" + UUID.randomUUID().toString() + ".pdf";

        Map<String, Object> map = new HashMap<String, Object>();

        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            draw(doc, fos, map, ops);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (canvasType == Painter.AUTO) {
            if (canvas instanceof File)
                canvasType = Painter.FILE;
            else if (canvas instanceof String)
                canvasType = Painter.FILEPATH;
            else if (canvas instanceof OutputStream)
                canvasType = Painter.STREAM;
        }

        Image image = (Image) map.get("sign/image");
        Rectangle rect = (Rectangle) map.get("sign/rect");
        Integer page = (Integer) map.get("sign/page");

        if (page == null)
            page = 0;

        if (canvasType == Painter.STREAM) {
            try {
                sign(filePath, (OutputStream) canvas, image, rect, page, scope, chain, pk, DigestAlgorithms.SHA1, MakeSignature.CryptoStandard.CMS, reason, location);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (canvasType == Painter.FILE || canvasType == Painter.FILEPATH) {
            File file = canvasType == Painter.FILE ? (File) canvas : new File((String) canvas);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                sign(filePath, fos, image, rect, page, scope, chain, pk, DigestAlgorithms.SHA1, MakeSignature.CryptoStandard.CMS, reason, location);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new DocumentExportException("不支持的导出格式");
        }
    }

    public static void copy(String file, OutputStream fos) {
        System.out.println("WARN: no sign image");

        try (FileInputStream is = new FileInputStream(new File(file))) {
            byte[] b = new byte[4096];
            int c = 0;
            while (c >= 0) {
                c = is.read(b);
                if (c > 0)
                    fos.write(b, 0, c);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void draw(LexDocument doc, OutputStream os, Map<String, Object> appendMap, ResourceOps ops) throws DocumentExportException {
        PdfContentByte pdfContentByte;
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, os);

            document.open();

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
                    translateElement(pdfContentByte, document, i, page.getPaper(), 0, 0, element, appendMap, ops);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }



    public void sign(String src, OutputStream os, Image image, Rectangle rect, int page, String scope, Certificate[] chain, PrivateKey pk, String digestAlgorithm, MakeSignature.CryptoStandard subfilter, String reason, String location)
            throws GeneralSecurityException, IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);

        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);
        appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

        if (image != null) {
            appearance.setVisibleSignature(rect, page + 1, scope);
            appearance.setSignatureGraphic(image);
        }

        ExternalDigest digest = new BouncyCastleDigest();
        ExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, null);
        MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, subfilter);
    }
}
