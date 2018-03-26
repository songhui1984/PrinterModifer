package hello;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SpringBootApplication
public class Application implements CommandLineRunner {

//	@Autowired
//	private CustomerRepository repository;
	
	@Autowired
	private XmlRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	fonts
//	page_setting
//	paragraph
//	paragraph
//	paragraph
//	paragraph
//	paragraph
//	paragraph
//	paragraph
	
	
//	fonts
//	page_setting
//	paragraph
//	loop
	
	@Override
	public void run(String... args) throws Exception {

//		repository.deleteAll();
//
//		XmlTemplate dave = repository.save(new XmlTemplate("one", "static/resource/template/iyb_proposal/template.xml"));
//
//		System.out.println(dave.id);
//
//		XmlTemplate save = repository.findByName("one");
//
//		System.out.println(save.getContext());
//
//		System.out.println(save.getFileName()+"_new");
//
//		Element root  = ParserXml.parse(save.getContext());
//
//		NodeList list = root.getChildNodes();
//		for (int i = 0; list != null && i < list.getLength(); i++) {
//			Node node = list.item(i);
//
//			if (node.getNodeType() != Node.ELEMENT_NODE)
//				continue;
//
//			if ("paragraph".equals(node.getNodeName())) {
//				System.out.println("paragraph");
//			} else if ("loop".equals(node.getNodeName())) {
//
//
//				System.out.println("loop");
//			}else if ("page_setting".equals(node.getNodeName())) {
//				System.out.println("page_setting");
//			}else if ("fonts".equals(node.getNodeName()) || "font_mapping".equals(node.getNodeName())) {
//				System.out.println("fonts");
//			}
//		}
		
//		FileUtils.writeStringToFile(new File(save.getFileName()+"_new"), save.getContext(), StandardCharsets.UTF_8);
		
		
		//
//		// save a couple of customers
//		repository.save(new Customer("Alice", "Smith"));
//		repository.save(new Customer("Bob", "Smith"));
//
//		// fetch all customers
//		System.out.println("Customers found with findAll():");
//		System.out.println("-------------------------------");
//		for (Customer customer : repository.findAll()) {
//			System.out.println(customer);
//		}
//		System.out.println();
//
//		// fetch an individual customer
//		System.out.println("Customer found with findByFirstName('Alice'):");
//		System.out.println("--------------------------------");
//		System.out.println(repository.findByFirstName("Alice"));
//
//		System.out.println("Customers found with findByLastName('Smith'):");
//		System.out.println("--------------------------------");
//		for (Customer customer : repository.findByLastName("Smith")) {
//			System.out.println(customer);
//		}

	}

}
