package com.nube.portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * adding all static url-s in this configuration
 * 
 * @author kamoorr
 * 
 */
@Configuration
@EnableWebMvc
public class WebAppConfigurator extends WebMvcConfigurerAdapter{
	
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/~core/**")
				.addResourceLocations("/~core/");
		registry.setOrder(1);
		
	}
	

}
