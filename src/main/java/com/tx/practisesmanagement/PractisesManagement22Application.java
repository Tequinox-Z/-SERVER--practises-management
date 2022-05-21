package com.tx.practisesmanagement;

import java.util.Date;
import java.util.Properties;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.Teacher;
import com.tx.practisesmanagement.repository.AdministratorRepository;
import com.tx.practisesmanagement.repository.TeacherRepository;

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
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("practises.management.server@gmail.com");
	    mailSender.setPassword("Rtqbt8rnbgmf5");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
}
