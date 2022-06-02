package com.tx.practisesmanagement;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@EnableEurekaClient
@SpringBootApplication
public class PractisesManagement22Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PractisesManagement22Application.class, args);
	}
	
	/**
	 * Establece la configuración para el envío de correos
	 */
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.office365.com");											// Host
	    mailSender.setPort(587);															// Puerto
	    mailSender.setUsername("practisesmanagement@outlook.es");							// Usuario
	    mailSender.setPassword("Rtqbt8rnbgmf5");											// Contraseña
	    
	    // Otras propiedades
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
}
