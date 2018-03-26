package com.neusoft.graphene.basecomponent.printer;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.neusoft.graphene.basecomponent.document.LexDocument;
import com.neusoft.graphene.basecomponent.document.ResourceOps;
import com.neusoft.graphene.basecomponent.document.export.Painter;
import com.neusoft.graphene.basecomponent.document.typeset.Typeset;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetDocument;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetParameters;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetQrcode;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetUtil;
import com.neusoft.graphene.basecomponent.printer.export.impl.PdfPainterNDF;
import com.neusoft.graphene.basecomponent.printer.export.impl.PdfPainterSign;
import com.neusoft.graphene.basecomponent.printer.export.impl.TextDimensionAwt;
import com.neusoft.graphene.basecomponent.printer.mongo.MongoFileOps;
import com.neusoft.graphene.basecomponent.printer.tools.Common;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PrinterService {

	private Map<Object, TypesetTemplate> typesetTemplateMap;

	private Map<String,BaseFont> baseFontMap = new HashMap<String,BaseFont>();

//	Jar run load font resource to map
//	public void buildFontMap() {
//		try {
//			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//			// 获取所有匹配的文件
//			Resource[] resources = resolver.getResources("classpath:/static/resource/fonts/*.ttf");
//			for (Resource resource : resources) {
//				// 获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
//
//				BaseFont textFont = BaseFont.createFont("classpath:/static/resource/fonts/" + resource.getFilename(),
//						BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//				log.info("load font----------"+resource.getFilename());
//				baseFontMap.put(resource.getFilename(), textFont);
//			}
//		} catch (IOException | DocumentException e) {
//			e.printStackTrace();
//		}
//	}

	private void buildFontMap(){

		List<String> filelist = getFiles("src/main/resources/static/resource/fonts/");

		for(String str:filelist){

			BaseFont textFont = null;
			try {
				textFont = BaseFont.createFont(str,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			} catch (DocumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			baseFontMap.put(str.substring(str.lastIndexOf("\\")+1,str.length()), textFont);
		}
	}

	private static ArrayList<String> filelist = new ArrayList<String>();

	/*
	 * 通过递归得到某一路径下所有的目录及其文件
	 */
	private static List<String> getFiles(String filePath) {


		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				/*
				 * 递归调用
				 */
				getFiles(file.getAbsolutePath());
			} else {
				filelist.add(file.getAbsolutePath());
			}
		}
		return filelist;
	}



	@Autowired
	private MongoFileOps fileOps;

	@Autowired
	private PrinterDao printerDao;

	@Value("${path.printer}") // ./static/
	private String path;//src/main/resources/static/

	private Map<Long, Sign> signs;

	private Map<Long, Painter> pdfPainters;

	@PostConstruct
	private void init() {

		TypesetUtil.setResourcePath(Common.pathOf(path, "resource/template"));
		TypesetUtil.setTextDimension(new TextDimensionAwt());
		TypesetUtil.addElementFactory("qrcode", new TypesetQrcode());

		signs = printerDao.loadAllSign();
		pdfPainters = buildPdfSignPainters(fileOps);
		pdfPainters.put(0L, new PdfPainterNDF(baseFontMap));

		buildFontMap();
		fillTemplateMap();
	}

	private void fillTemplateMap() {
		typesetTemplateMap = new HashMap<>();

		List<TypesetTemplate> typesetTemplateList = printerDao.loadAllTypesetTemplate();

		if (typesetTemplateList == null)
			return;

		for (TypesetTemplate typesetTemplate : typesetTemplateList) {
			typesetTemplate.refresh(fileOps);

			typesetTemplateMap.put(typesetTemplate.getId(), typesetTemplate);
			typesetTemplateMap.put(typesetTemplate.getCode(), typesetTemplate);
		}
	}

	public boolean exists(String code) {
		return code != null && typesetTemplateMap.containsKey(code);
	}

	public Long create(String code, String name) {
		TypesetTemplate tt = printerDao.create(code, name);

		new File(Common.pathOf(TypesetUtil.getResourcePath(), tt.getWorkDir())).mkdirs();

		synchronized (typesetTemplateMap) {
			typesetTemplateMap.put(tt.getId(), tt);

			if (!Common.isEmpty(tt.getCode()))
				typesetTemplateMap.put(tt.getCode(), tt);
		}

		return tt.getId();
	}

	public void resetCode(TypesetTemplate tt, String code) {
		synchronized (typesetTemplateMap) {
			if (!Common.isEmpty(tt.getCode()))
				typesetTemplateMap.remove(tt.getCode());

			if (!Common.isEmpty(code)) {
				tt.setCode(code);
				typesetTemplateMap.put(tt.getCode(), tt);
			}
		}
	}

	public void save(TypesetTemplate tt) {
		tt.refresh(fileOps);

		printerDao.save(tt);
	}


	public Collection<TypesetTemplate> listTypesetTemplate() {
		fillTemplateMap();
		return typesetTemplateMap.values();
	}

	public TypesetTemplate getTypesetTemplate(Object code) {
		synchronized (typesetTemplateMap) {
			return typesetTemplateMap.get(code);
		}
	}

	public boolean isEmpty() {
		return typesetTemplateMap.isEmpty();
	}

	public void addTypeset(TypesetTemplate typesetTemplate) {
		synchronized (typesetTemplateMap) {
			typesetTemplateMap.put(typesetTemplate.getCode(), typesetTemplate);
		}
	}

	public void clear() {
		synchronized (typesetTemplateMap) {
			typesetTemplateMap.clear();
		}
	}

	public LexDocument build(TypesetTemplate template, JSONObject param,ResourceOps ops) {

		Typeset typeset = template.getTypeset();

		TypesetDocument typesetDocument = new TypesetDocument(typeset);

		/*
		  添加字体路径信息songhui
		 */
		param.put("FONT_HEI", "simhei.ttf");
		param.put("FONT_KAI", "simkai.ttf");
		param.put("FONT_SONG", "stsong.ttf");
		param.put("FONT_YOU", "simyou.ttf");
		param.put("FONT_CONSOLA", "consola.ttf");
		param.put("FONT_COURIER", "courier.ttf");

		param.put("FONT_BOLDSONG", "msyhbd.ttf");


		param.put("RESOURCE_PATH",
				TypesetUtil.getResourcePath() + File.separator + template.getCode() + File.separator);

		typesetDocument.build(new TypesetParameters(param), ops);

		return typesetDocument;
	}

	public synchronized Painter getPdfSignPainter(TypesetTemplate template) {

		if (template.getSignId() == null) {
			return pdfPainters.get(0L);
		} else {

			System.out.println("template.getSignId()------------》 ="+template.getSignId());
			return pdfPainters.get(template.getSignId());
		}
	}

	private Map<Long, Painter> buildPdfSignPainters(MongoFileOps fileOps) {
		Map<Long, Painter> pdfPainters = new HashMap<>();

		for (Sign sign : signs.values()) {
			try {
//				String keystore = getPath("resource/sign/") + sign.getKeystore();
				String keystore = sign.getKeystore();

				PdfPainterSign pdfPainterSign = new PdfPainterSign(baseFontMap,fileOps,keystore, sign.getPassword(), sign.getScope(),
						sign.getReason(), sign.getLocation());

				pdfPainters.put(sign.getId(), pdfPainterSign);

				System.out.println(sign.getScope() + "/" + sign.getKeystore() + " - load success");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return pdfPainters;
	}


	public void log(String key, Long templateId, String fileType, int outType, int result, String ip, int buildTime,
					int exportTime, int pages) {
		printerDao.log(key, templateId, fileType, outType, result, ip, buildTime, exportTime, new Date(), pages);
	}
}
