package com.neusoft.graphene.basecomponent.printer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;


import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
//http://localhost:8080/swagger-ui.html
@ImportResource("classpath:spring.xml")
public class PrinterApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PrinterApplication.class, args);
	}
	
}
