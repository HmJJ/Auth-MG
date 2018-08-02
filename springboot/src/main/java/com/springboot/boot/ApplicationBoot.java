package com.springboot.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;

@ImportResource(locations = {"classpath*:/beans/**/*.xml"})
@ComponentScan(basePackages= {"com.springboot"})
@SpringBootApplication
@EnableScheduling
public class ApplicationBoot extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationBoot.class, args);
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ApplicationBoot.class);
	}
	
	@RequestMapping(value= {"/",""})
	public String search() {
		return "index";
	}
	
	
}
