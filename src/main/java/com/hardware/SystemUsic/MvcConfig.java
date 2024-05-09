package com.hardware.SystemUsic;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);

		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		log.info(resourcePath);
		
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(resourcePath);

		String resourcePath2 = Paths.get("uploads_bajas").toAbsolutePath().toUri().toString();
		log.info(resourcePath2);
		
		registry.addResourceHandler("/uploads_bajas/**")
		.addResourceLocations(resourcePath2);

		String resourcePath3 = Paths.get("uploads_servicio").toAbsolutePath().toUri().toString();
		log.info(resourcePath3);
		
		registry.addResourceHandler("/uploads_servicio/**")
		.addResourceLocations(resourcePath3);
	}

	
}

