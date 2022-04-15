package com.tx.practisesmanagement;

import java.util.Date;
import java.util.Properties;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

@SpringBootApplication
public class PractisesManagement22Application {

	public static void main(String[] args) {
		SpringApplication.run(PractisesManagement22Application.class, args);
	}


	@Bean
	CommandLineRunner initData(AdministratorRepository userRepositorio, TeacherRepository teacher, BCryptPasswordEncoder passwordEncoder) {
		return (args) -> {			
			// Añadimos un administrador
				userRepositorio.save(new Administrator("48124538M", "Calle Matallana", new Date(), "https://i.pinimg.com/originals/8b/16/7a/8b167af653c2399dd93b952a48740620.jpg", "Salva", "Pérez Agredano", passwordEncoder.encode("4b78e581bdaffa037a6b11d58bdc934a"), "643196361", "tequinoxtablet@gmail.com")); 
			// Añadimos un profesor
				teacher.save(new Teacher("48124538N", "Calle Matallana", new Date(), "https://electronicssoftware.net/wp-content/uploads/user.png", "Salva", "Pérez Agredano", passwordEncoder.encode("4b78e581bdaffa037a6b11d58bdc934a"), "643196361", "txaxati2018@gmail.com"));
		};
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
