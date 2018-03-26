package com.neusoft.graphene.basecomponent.printer;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	
    	if (log.isDebugEnabled()) {
            log.debug("StaticResourceConfiguration:{}", "init the static resource to ./temp/ ");
        }

        registry.addResourceHandler("/**").addResourceLocations("file:./temp/","classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/","context:/");
    }
}
