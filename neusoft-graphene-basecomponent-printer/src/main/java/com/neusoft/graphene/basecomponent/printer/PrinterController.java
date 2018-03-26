package com.neusoft.graphene.basecomponent.printer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.graphene.basecomponent.document.LexDocument;
import com.neusoft.graphene.basecomponent.document.export.Painter;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetUtil;
import com.neusoft.graphene.basecomponent.printer.export.impl.ObjectPainter;
import com.neusoft.graphene.basecomponent.printer.export.impl.PngPainter;
import com.neusoft.graphene.basecomponent.printer.mongo.MongoFileOps;
import com.neusoft.graphene.basecomponent.printer.tools.Common;
import com.neusoft.graphene.basecomponent.printer.tools.Disk;



@Controller
//@Slf4j
public class PrinterController {
	@Autowired
	private PrinterService printerService;
	
	@Autowired
	private MongoFileOps mongoOps;

	@Autowired
	private MailService mailService;
	
	@Autowired
	private OssService ossService;

//	@Value("${path.temp}")//./static/temp/
//	private String tempPath;

	@Value("${url.temp}")// http://127.0.0.1:7511/temp
	private String urlPrefix;
	

	@RequestMapping("/health")
	@ResponseBody
	@CrossOrigin//springMVC的版本要在4.2或以上版本才支持@CrossOrigin
	public String health() {
		return "OK";
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	public String testLogLevel() {
//		if(logger.isDebugEnabled()){
//			logger.debug("Logger Level ：DEBUG");
//		}
//		logger.debug("Logger Level ：DEBUG");
//		logger.info("Logger Level ：INFO");
//		logger.error("Logger Level ：ERROR");
//		return "";
//	}

	
	//Request URL:http://127.0.0.1:8081/resource/template/iyb_quote/photo1.jpg
	@RequestMapping("/*/template/**")
	//^http(s)?://.+\.(jp(e)?g|png)$
	//.(jp(e)?g|png|bmp)$
	//.+.(jp(e)?g|png|bmp)$
	public void viewResource(HttpServletRequest request,HttpServletResponse reponse){

		String url =request.getRequestURI();
		
		String[] arrays = url.split("/");
		String resourceName = arrays[arrays.length-2]+"_"+arrays[arrays.length-1];
		
		System.out.println(resourceName);
		
		//引入缓存可以优化速度
		try {
			reponse.getOutputStream().write(IOUtils.toByteArray(mongoOps.getByFileName(resourceName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * 列出所有的可用的模板
	 * @param requestJson
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list.json")
	@ResponseBody
	@CrossOrigin
	public JSONObject list(@RequestBody JSONObject requestJson) throws Exception {
		
		Collection<TypesetTemplate> list = printerService.listTypesetTemplate();
		
		JSONArray ja = new JSONArray();
		for (TypesetTemplate tt : list) {
			JSONObject j = new JSONObject();
			j.put("id", tt.getId());
			j.put("code", tt.getCode());
			j.put("name", tt.getName());
			ja.add(j);
		}

		JSONObject r = new JSONObject();
		r.put("list", ja);
		r.put("total", ja.size());

		JSONObject res = new JSONObject();
		res.put("result", "success");
		res.put("content", r);

		return res;
	}
	
	
	/**
	 * click test button
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/view.json")
	@ResponseBody//{"templateId":"2"}
	@CrossOrigin
	public JSONObject view(@RequestBody JSONObject json) throws Exception {
		TypesetTemplate template = getTemplate(json);

		JSONArray fs = new JSONArray();
		
		File dir = new File(Common.pathOf(TypesetUtil.getResourcePath(), template.getWorkDir()));
		File[] files = dir.listFiles();
		if (files != null)
			for (File f : files) {
				fs.add(f.getName());
			}

		JSONObject r = new JSONObject();
		r.put("id", template.getId());
		r.put("code", template.getCode());
		r.put("name", template.getName());
		r.put("workDir", template.getWorkDir());
		r.put("files", fs);

		r.put("example", template.getFileContext());
		
				
		JSONObject res = new JSONObject();
		res.put("result", "success");
		res.put("content", r);

		return res;
	}
	


	
	/**2 3 8
	 *  click preview / output button
	 *  preview page come to here
	 * @param printJsonData
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/print.json")
	@ResponseBody
	@CrossOrigin
	public JSONObject print(@RequestBody JSONObject printJsonData, HttpServletRequest request) throws Exception {
		//TODO should valid the client_key
		String client_key = printJsonData.getString("key");
		String ip_request = getIpAddr(request);

		String outputType = printJsonData.getString("outputType");
		if (Common.isEmpty(outputType))
			outputType = "pdf";

		TypesetTemplate typesetTemplate = getTemplate(printJsonData);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", "fail");

		long t = System.currentTimeMillis();

		try {
			LexDocument doc = printerService.build(typesetTemplate, printJsonData.getJSONObject("content"),mongoOps);

			int t1 = (int) (System.currentTimeMillis() - t);

			if ("pdf".equalsIgnoreCase(outputType)) {
				File tempFile = new File("./temp");
				if(!tempFile.exists())
					tempFile.mkdir();
				String fileName = Common.nextId() + ".pdf";
				File file = new File(Common.pathOf("./temp", fileName));
				
				//OL NDF
				try (FileOutputStream fos = new FileOutputStream(file)) {
					doc.export(printerService.getPdfSignPainter(typesetTemplate), fos, Painter.STREAM,mongoOps);
				}
				
				JSONObject mail = printJsonData.getJSONObject("email");
				if (mail != null && !mail.isEmpty()) {
					List<String[]> list = new ArrayList<String[]>();
					list.add(new String[] { file.getAbsolutePath(), fileName });

					mailService.send(mail.getString("address"), mail.getString("subject"), mail.getString("content"),
							list);
				} else {
					responseObject.put("content", urlPrefix + "/" + fileName);
				}
				
				String prefix = "graphene-doc_";
//				String ossKey = ossService.uploadFile(file.getPath(), prefix,false );//TODO take time
				
				responseObject.put("result", "success");
//				responseObject.put("osskey", ossKey);
				
			} else if ("data".equalsIgnoreCase(outputType)) {
				JSON data = new JSONArray();
				doc.export(new ObjectPainter(), data, Painter.OBJECT,mongoOps);

				responseObject.put("result", "success");
				responseObject.put("content", data);
			} else {
				throw new RuntimeException("outputType<" + outputType + "> not support");
			}

			int t2 = (int) (System.currentTimeMillis() - t) - t1;

			printerService.log(client_key, typesetTemplate.getId(), outputType, 1, 1, ip_request, t1, t2, doc.pageSize());
		} catch (Exception e) {
			throw e;
		}

		System.out.println("print in " + (System.currentTimeMillis() - t) + "ms");

		return responseObject;
	}

	/**
	 * 写入返回的流里面
	 * @param p
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "/print.stream", "/stream.json" })
	public void printStream(@RequestBody JSONObject p, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String key = p.getString("key");
		String ip = getIpAddr(request);

		String outputType = p.getString("outputType");
		if (Common.isEmpty(outputType))
			outputType = "pdf";

		TypesetTemplate typesetTemplate = getTemplate(p);

		long t = System.currentTimeMillis();

		try (OutputStream os = response.getOutputStream()) {
			LexDocument doc = printerService.build(typesetTemplate, p.getJSONObject("content"),mongoOps);

			int t1 = (int) (System.currentTimeMillis() - t);

			if ("pdf".equalsIgnoreCase(outputType)) {
				doc.export(printerService.getPdfSignPainter(typesetTemplate), os, Painter.STREAM,mongoOps);
			}

			int t2 = (int) (System.currentTimeMillis() - t) - t1;

			printerService.log(key, typesetTemplate.getId(), outputType, 2, 1, ip, t1, t2, doc.pageSize());
		} catch (Exception e) {
			throw e;
		}

		System.out.println("stream in " + (System.currentTimeMillis() - t) + "ms");
	}


	

	@RequestMapping("/test.json")
	@ResponseBody
	@CrossOrigin
	public JSONObject test(@RequestBody JSONObject p, HttpServletRequest request) throws Exception {
		TypesetTemplate template = getTemplate(p);
//		LexDocument doc = printerService.build(template, JSON.parseObject(Disk.load(template.getTest(), "utf-8")));
		//test.json's stream
		LexDocument doc = printerService.build(template, JSON.parseObject(template.getFileContext()),mongoOps);

		String outputType = p.getString("outputType");
		if (Common.isEmpty(outputType))
			outputType = "pdf";

		JSONObject res = new JSONObject();
		res.put("result", "fail");

		//java.io.FileNotFoundException: src\main\resources\static\temp\101514441484000001.pdf (系统找不到指定的路径。)
		
		if ("pdf".equalsIgnoreCase(outputType)) {
			File tempFile = new File("./temp");
			if(!tempFile.exists())
				tempFile.mkdir();
			
			String fileName = Common.nextId() + ".pdf";
			File file = new File(Common.pathOf("./temp", fileName));
			try (FileOutputStream fos = new FileOutputStream(file)) {
				
				doc.export(printerService.getPdfSignPainter(template), fos, Painter.STREAM,mongoOps);
			}
			
			res.put("content", urlPrefix + "/" + fileName);

			res.put("result", "success");
		} else if ("png".equalsIgnoreCase(outputType)) {
			File tempFile = new File("./temp");
			if(!tempFile.exists())
				tempFile.mkdir();
			
			String fileName = Common.nextId();
			File file = new File(Common.pathOf("./temp", fileName));

			doc.export(new PngPainter(), file,Painter.AUTO,mongoOps);

			JSONArray list = new JSONArray();
			for (int i = 0; i < doc.pageSize(); i++)
				list.add((i + 1) + ".png");

			res.put("content", list);
			res.put("result", "success");
		} else if ("data".equalsIgnoreCase(outputType)) {
			JSON data = new JSONArray();
			doc.export(new ObjectPainter(), data, Painter.OBJECT,mongoOps);

			res.put("result", "success");
			res.put("content", data);
		}

		return res;
	}


	

	private TypesetTemplate getTemplate(JSONObject printJsonData) {
		Long templateId = printJsonData.getLong("templateId");// 2 3 8
		String templateCode = printJsonData.getString("templateCode");
		Object template = templateId != null ? templateId : templateCode;

		return printerService.getTypesetTemplate(template);
	}

	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("x-real-ip");
		if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		} else {
			return request.getRemoteAddr();
		}
	}

	@RequestMapping("/{templateId}/upload")
	@ResponseBody
	@CrossOrigin
	public JSONObject file(@PathVariable Long templateId, @RequestParam("file") List<MultipartFile> files) {
		TypesetTemplate tt = printerService.getTypesetTemplate(templateId);

		for (MultipartFile file : files) {
			File ff = new File(Common.pathOf(TypesetUtil.getResourcePath(),
					Common.pathOf(tt.getWorkDir(), file.getOriginalFilename())));

			try (InputStream is = file.getInputStream()) {
				Disk.saveToDisk(is, ff);

				if (ff.getName().toLowerCase().endsWith(".xml")) {
					tt.setTemplateFile(ff.getName());
				}

				if (ff.getName().toLowerCase().equals("test.json")) {
					tt.setTestFile(ff.getName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		printerService.save(tt);

		JSONObject res = new JSONObject();
		res.put("result", "success");

		return res;
	}

	/**
	 * edit page clickX
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete.json")
	@ResponseBody
	@CrossOrigin
	public JSONObject delete(@RequestBody JSONObject json) throws Exception {
		TypesetTemplate template = getTemplate(json);

		String file = json.getString("file");
		if (Common.isEmpty(file))
			throw new RuntimeException("no file");

		File abbrFile = new File(
				Common.pathOf(TypesetUtil.getResourcePath(), Common.pathOf(template.getWorkDir(), file)));
		if (!abbrFile.delete())
			throw new RuntimeException("fail");

		JSONObject res = new JSONObject();
		res.put("result", "success");

		return res;
	}

	
	/**
	 * 添加模板
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/create.json")
	@ResponseBody
	@CrossOrigin
	public JSONObject create(@RequestBody JSONObject json) throws Exception {
		String code = json.getString("code");
		if (Common.isEmpty(code))
			code = null;

		if (printerService.exists(code))
			throw new RuntimeException("code " + code + " exists");

		String name = json.getString("name");

		JSONObject res = new JSONObject();
		res.put("result", "success");
		res.put("content", printerService.create(code, name));

		return res;
	}

	/**
	 * SAVE ALL button
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save.json")
	@ResponseBody
	@CrossOrigin
	public JSONObject save(@RequestBody JSONObject json) throws Exception {
		String code = json.getString("code");
		if (Common.isEmpty(code))
			code = null;

		TypesetTemplate template = getTemplate(json);
		if ((code == null && template.getCode() != null) || !code.equals(template.getCode())) {
			if (printerService.exists(code))
				throw new RuntimeException("code " + code + " exists");

			printerService.resetCode(template, code);
		}

		template.setName(json.getString("name"));
		template.setSignId(json.getLong("signId"));

		String testStr = json.getString("test");
		if (!Common.isEmpty(testStr)) {
			File testFile = new File(
					Common.pathOf(TypesetUtil.getResourcePath(), Common.pathOf(template.getWorkDir(), "test.json")));
			try (FileOutputStream fos = new FileOutputStream(testFile)) {
				fos.write(testStr.getBytes("utf-8"));
				template.setTestFile("test.json");
			} catch (Exception e) {
				template.setTestFile(null);
				e.printStackTrace();
			}
		} else {
			template.setTestFile(null);
		}

		printerService.save(template);

		JSONObject res = new JSONObject();
		res.put("result", "success");

		return res;
	}
	

}
