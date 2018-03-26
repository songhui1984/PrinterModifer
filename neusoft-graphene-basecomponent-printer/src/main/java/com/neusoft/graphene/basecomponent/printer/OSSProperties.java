package com.neusoft.graphene.basecomponent.printer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration  
@ConfigurationProperties(prefix = "printer.oss")  
@Data
public class OSSProperties {
	
	private String accessId;

	private String accessKey;

	private String endpoint;

	private String bucketName;

}
