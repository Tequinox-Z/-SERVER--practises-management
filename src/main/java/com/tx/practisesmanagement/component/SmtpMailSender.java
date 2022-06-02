package com.tx.practisesmanagement.component;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de los envíos de correo
 * @author Salva
 */
@Component
public class SmtpMailSender {
	
	@Autowired private JavaMailSender javaMailSender;
	/**
	 * Permite enviar un correo
	 * @param to A quién va dirigido
	 * @param subject El asunto del mensaje
	 * @param body El cuerpo del mensaje
	 * @throws MessagingException
	 */
	public void send(String to, String subject, String body) throws MessagingException {
	        SimpleMailMessage message = new SimpleMailMessage(); 			// Creamos un mensage simple
	        message.setFrom("practisesmanagement@outlook.es"); 				// Asignamos el remitente
	        message.setTo(to); 												// Asignamos el destinatario
	        message.setSubject(subject); 									// Asignamos el sujeto
	        message.setText(body);											// Pasamos el cuerpo
	        javaMailSender.send(message);									// Enviamos el mensaje
	}
}
