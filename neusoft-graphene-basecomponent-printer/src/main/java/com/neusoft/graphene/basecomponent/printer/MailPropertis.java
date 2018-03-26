package com.neusoft.graphene.basecomponent.printer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration  
@ConfigurationProperties(prefix = "printer.mail")  
@Data
public class MailPropertis {
	
	String account;

	String password;

	String smtpHost;

	String smtpPort;

	String smtpAuth;

}
