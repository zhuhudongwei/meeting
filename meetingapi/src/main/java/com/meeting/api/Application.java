package com.meeting.api;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * 通过@Configuration+@ComponentScan开启注解扫描并自动注册相应的注解Bean
 * @author zdw
 *
 */
@Configuration  
@ComponentScan  
@EnableAutoConfiguration //开启自动配置
public class Application {
	
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("50MB");
        factory.setMaxRequestSize("50MB");
        return factory.createMultipartConfig();
    }
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication (Application.class);
        springApplication.run (args);
	}
}
