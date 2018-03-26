package hello;
/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlRepositoryTests {

	@Autowired
	XmlRepository repository;

	XmlTemplate one, two, three;

	@Autowired
	FileOops fileOps;

//	@Before
	public void setUp() {

//		repository.deleteAll();
//
//		String file = "src/main/resources/cover.jpg";
//
//		InputStream inputStream = null;
//		try {
//			inputStream = new FileInputStream(file);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		GridFSInputFile gridFSInputFile = fileOps.save(inputStream, file, file);
//
//		System.out.println(gridFSInputFile.getId());

	}

	// @Test
	// public void setsIdOnSave() {
	//
	// XmlTemplate xmlFile = repository.save(new XmlTemplate("one",
	// "static/resource/template/iyb_quote/template.xml"));
	//
	// assertThat(xmlFile.id).isNotNull();
	// }

	// @Test
	public void findByName() {

		// XmlTemplate xmlFile = repository.findByName("one");
		// System.out.println(xmlFile.getContext());
		//
		String file = "src/main/resources/template/cover.jpg";

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GridFSInputFile gridFSInputFile = fileOps.save(inputStream, file, file);

		GridFSDBFile gridFSDBFile = fileOps.getByFileName(file);

		try {
			gridFSDBFile.writeTo("src/main/resources/out.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(gridFSInputFile.getId());

	}
	
	@Test
	public void list() throws FileNotFoundException{
		List<String> filelist = getFiles("src/main/resources/template/");
		
		for(String str:filelist){

			String[] arrays = str.split("\\\\");
			String name = arrays[arrays.length-2]+"_"+arrays[arrays.length-1];
			System.out.println(name);
			GridFSInputFile gridFSInputFile = fileOps.save(new FileInputStream(str), name, name);
			if(gridFSInputFile!=null)
			System.out.println(gridFSInputFile.getId());
		}
	}

	static ArrayList<String> filelist = new ArrayList<String>();

	
//	@Test
	public void listFonts() throws FileNotFoundException{
		List<String> filelist = getFiles("src/main/resources/static/resource/fonts/");
		
//		src\main\resources\static\resource\fonts\consola.ttf
		
		for(String str:filelist){
			System.out.println(str);
			String[] arrays = str.split("\\\\");
			String name = arrays[arrays.length-1];
			System.out.println(name);
			GridFSInputFile gridFSInputFile = fileOps.save(new FileInputStream(str), name, name);
			if(gridFSInputFile!=null)
			System.out.println(gridFSInputFile.getId());
		}
	}
	
	
	@Test
	public void listSigns() throws FileNotFoundException{
		List<String> filelist = getFiles("src/main/resources/static/resource/sign/");
		
		for(String str:filelist){
			System.out.println(str);
			String[] arrays = str.split("\\\\");
			String name = arrays[arrays.length-1];
			System.out.println(name);
			GridFSInputFile gridFSInputFile = fileOps.save(new FileInputStream(str), name, name);
			if(gridFSInputFile!=null)
			System.out.println(gridFSInputFile.getId());
		}
	}
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
	
	

	// @Test
	public void findByNameAndFileName() {

		XmlTemplate xmlFile = repository.findByNameAndFileName("one",
				"static/resource/template/iyb_quote/template.xml");
		System.out.println(xmlFile.getContext());

		XmlTemplate xmlFile2 = repository.findByNameAndFileName("two",
				"static/resource/template/iyb_quote/template.xml");
		System.out.println(xmlFile2.getContext());

	}

	// @Test
	// public void findsByLastName() {
	//
	// List<Customer> result = repository.findByLastName("Beauford");
	//
	// assertThat(result).hasSize(1).extracting("firstName").contains("Carter");
	// }
	//
	// @Test
	// public void findsByExample() {
	//
	// Customer probe = new Customer(null, "Matthews");
	//
	// List<Customer> result = repository.findAll(Example.of(probe));
	//
	// assertThat(result).hasSize(2).extracting("firstName").contains("Dave",
	// "Oliver August");
	// }
}
