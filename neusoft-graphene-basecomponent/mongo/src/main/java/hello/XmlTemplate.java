package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.annotation.Id;

public class XmlTemplate {

	@Id
	public String id;

	private String name;

	private String fileName;
	
	private String context;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public XmlTemplate(String name, String fileName) {

		this.name = name;

		this.fileName = fileName;
		Resource resource = new ClassPathResource(fileName);
		BufferedReader br = null;
		String line;
		StringBuilder sb = new StringBuilder();

		try {
			br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			while ((line = br.readLine()) != null) {
//				sb.append(line.trim());
				sb.append(line);
				sb.append("\n");

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.context = sb.toString();

	}

}
