package com.tx.practisesmanagement;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@EnableEurekaClient
@SpringBootApplication
public class PractisesManagement22Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PractisesManagement22Application.class, args);
	}
	
	/*
	 * 
	 * 	====================================== Lenguaje ======================================
	 * 
	 *  	
	 *  	- Este establecerá el idioma que usará la aplicación
	*/
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setCacheSeconds(-1);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasenames("src/main/resources/languages/messages");
		
		return messageSource;
	}
	
	
	
	/*
	 * 
	 * 	================================= Configuración de correo =================================
	 * 
	 *  	
	 *  	- Este bean se encarga de establecer los ajustes del mail
	*/
	
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.office365.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("practisesmanagement@outlook.es");
	    mailSender.setPassword("Rtqbt8rnbgmf5");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
}
