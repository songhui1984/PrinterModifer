package com.neusoft.graphene.basecomponent.printer.export.impl;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.neusoft.graphene.basecomponent.document.LexFont;
import com.neusoft.graphene.basecomponent.document.typeset.TypesetCoord;
import com.neusoft.graphene.basecomponent.document.typeset.enviroment.TextDimension;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TextDimensionAwt implements TextDimension {
	
	private Map<String,Font> fontMap = new HashMap<String,Font>();

	Graphics2D g2D = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
	
	
	
	private static ArrayList<String> filelist = new ArrayList<String>();

	/*
	 * 通过递归得到某一路径下所有的目录及其文件
	 */
	static List<String> getFiles(String filePath) {


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

	//TODO local run
	public TextDimensionAwt() {

		try {
			List<String> filelist = getFiles("src/main/resources/static/resource/fonts/");

			for (String str : filelist) {
				InputStream stream = new FileInputStream(new File(str));
				Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
				fontMap.put(str.substring(str.lastIndexOf("\\") + 1, str.length()), font);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//TODO this is use for jar run compare to local run
//	public TextDimensionAwt(){
//		  ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//		  try {
//	            //获取所有匹配的文件
//	            Resource[] resources = resolver.getResources("classpath:/static/resource/fonts/*.ttf");
//	            for(Resource resource : resources) {
//	                //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
//	                InputStream stream = resource.getInputStream();
//	                Font font  = Font.createFont(Font.TRUETYPE_FONT, stream);
//	            	fontMap.put(resource.getFilename(), font);
//	                org.apache.commons.io.IOUtils.closeQuietly(stream);
//	            }
//	        } catch (IOException | FontFormatException e) {
//	                log.warn("load font exception",  e);
//	        }
//	}
	
	

	public synchronized TypesetCoord getSize(LexFont font, String text) {
		if (text == null || "".equals(text))
			return new TypesetCoord(0, 0);

		Font f = getFont(font.getFamily().getPath(), font.getSize());

		g2D.setFont(f);
		FontRenderContext frc = g2D.getFontRenderContext();
		Rectangle2D rect = f.getStringBounds(text, frc);

		return new TypesetCoord((int) Math.round(rect.getWidth() + 2), (int) Math.round(rect.getHeight() + 2));
	}

	private Font getFont(String fontFileName, float size) {
		Font font = fontMap.get(fontFileName);

		if (font == null) {
			font = new Font("黑体", Font.PLAIN, (int) size);
			fontMap.put(fontFileName, font);
		} else {
			font = font.deriveFont(size);
		}

		return font;
	}
}
