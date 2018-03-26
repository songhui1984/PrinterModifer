package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		//
		// Resource resource = new
		// ClassPathResource("static/resource/template/iyb_proposal/cover.jpg");
		//
		// BufferedReader br;
		// try {
		// br = new BufferedReader(new
		// InputStreamReader(resource.getInputStream()));
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		//
		// File file;
		// try {
		// file = resource.getFile();
		//// BufferedReader br = new BufferedReader(new InputStreamReader(new
		// FileInputStream(file)));
		//
		//// System.out.println(br.readLine());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//

//		Resource resource = new ClassPathResource("static/resource/template/fonts/");
//		InputStream stream = null;
//		// BufferedReader br;
//		// try {
//		// stream = resource.getInputStream();
//		// br = new BufferedReader(new InputStreamReader(stream));
//		// System.out.println(br.readLine());
//		// } catch (IOException e1) {
//		// // TODO Auto-generated catch block
//		// e1.printStackTrace();
//		// }
//
//		File targetFile = new File("static/resource/template/iyb_proposal/");
//
//		File[] files = targetFile.listFiles();
//
//		for (File f : files) {
//			System.out.println(f.getName());
//		}

//		try {
//			stream = resource.getInputStream();
			// FileUtils.copyInputStreamToFile(stream, targetFile);
//			FileUtils.copyToFile(stream, targetFile);
			// targetFile.createNewFile();

//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		//获取容器资源解析器
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        
        try {
            //获取所有匹配的文件
            Resource[] resources = resolver.getResources("classpath:/static/resource/fonts/*.ttf");
            for(Resource resource : resources) {
                //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
                InputStream stream = resource.getInputStream();
                System.out.println("读取的文件流  [" + stream + "]");
                String targetFilePath = "font" + resource.getFilename();
                System.out.println(("放置位置  [" + targetFilePath + "]"));
                File ttfFile = new File(targetFilePath);
                FileUtils.copyToFile(stream, ttfFile);
            }
        } catch (IOException e) {
           
        }

	}
}
