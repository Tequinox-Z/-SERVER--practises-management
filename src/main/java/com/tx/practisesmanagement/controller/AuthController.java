package com.tx.practisesmanagement.controller;


import java.util.Collections;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tx.practisesmanagement.component.SmtpMailSender;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.error.RestError;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.Student;
import com.tx.practisesmanagement.model.Teacher;
import com.tx.practisesmanagement.security.JWTUtil;
import com.tx.practisesmanagement.service.AdministratorService;
import com.tx.practisesmanagement.service.PersonService;
import com.tx.practisesmanagement.service.StudentService;
import com.tx.practisesmanagement.service.TeacherService;


/**
 * Controlador de autenticación
 * @author Salvador Pérez Agredano
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthController {
	
		/* ============================= Autenticación y JWT ============================= */ 
	
    		@Autowired private AuthenticationManager authManager;
    		@Autowired private JWTUtil jwtUtil;
    		@Autowired private PasswordEncoder passwordEncoder;

		/* ================================== Servicios ================================== */ 
	
    		@Autowired private AdministratorService administratorService;
    		@Autowired private StudentService studentService;
    		@Autowired private TeacherService teacherService;
    		@Autowired private PersonService personService;	    
    		@Autowired private SmtpMailSender smtpMailSender;
    		
    	/**
    	 * Permite registrar un usuario, ya sea un administrador, estudiante o profesor
   		 * @param user: Usuario a registrar
    	 * @return Usuario registrado
   		 */
	    @PostMapping("auth/register")
	    public ResponseEntity registerAdmin(@RequestBody PersonDTO user) {

	    	if (user.getDni() == null) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un dni"));						// Si no nos han pasado dni lo indicamos
	    	} 
	    	else if (user.getName() == null) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un nombre para este usuario"));	// Si no nos han pasado nombre lo indicamos
	    	}
	    	else if (user.getPassword() == null) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó una contraseña"));				// Si no nos han pasado una contraseña lo indicamos
	    	}
	    	else if (user.getEmail() == null) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un correo"));						// Si no nos han pasado un correo lo indicamos
	    	}
	    	else if (user.getRol() == null) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un rol para este usuario"));		// Si no nos han indicado un rol lo indicamos
	    	}
	   	    	
	    	user.setDni(user.getDni().toUpperCase());																												// Convertimos el DNI en mayúscula
	    	
	    	if (personService.existPerson(user.getDni())) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Usuario ya registrado"));							// Si el usuario está registrafo lo indicamos
	    	}
	    	
	        String encodedPass = passwordEncoder.encode(user.getPassword());					// Codificamos la contraseña
	        user.setPassword(encodedPass);														// Establecemos la contraseña
	        
	        switch(user.getRol().toUpperCase()) {
	        	case "ROLE_ADMIN": {
	        		administratorService.save(new Administrator(user));
	    	        break;
	        	}
	        	case "ROLE_STUDENT": {
	    	        studentService.save(new Student(user));
	        		break;
	        	}
	        	case "ROLE_TEACHER": {
	        		teacherService.save(new Teacher(user));
	        		break;
	        	}
	        	default: {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Rol desconocido o incorrecto"));
	        	}
	        }
	        
	        try {
				smtpMailSender.send(user.getEmail(), "Bienvenido a Practises Management", "La cuenta de correo " + user.getEmail() + " ha sido registrada satisfactoriamente en Practises Management. Puede iniciar sesión con su dni.");
			} 
	        catch (Exception e) {
				
			}

	        // Enviamos un correo de bienvenida 

	        String token = jwtUtil.generateToken(user.getDni());															// Generamos el token
    		return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("jwt_token", token));			// Retornamos el token 
	    }
	    

	    /**
	     * Permite iniciar sesión
	     * @param person: Datos de la persona a iniciar sesión
	     * @return Token Token resultante
	     */
	    @PostMapping("auth/login")
	    public ResponseEntity loginHandler(@RequestBody PersonDTO person){
	        try {
	        	
	        	if (person.getDni() == null) {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
		    				new RestError(HttpStatus.BAD_REQUEST, "Indique dni")			// Si no nos han indicado un dni lo indicamos
		    		);
	        	}
	        	else if (person.getPassword() == null) {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
		    				new RestError(HttpStatus.BAD_REQUEST, "Indique contraseña")		// Si no nos han pasado una contraseña lo indicamos 	
		    		);
	        	}
	        	
	            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(person.getDni(), person.getPassword());		// Creamos un nuevo usuario de autenticación

	            authManager.authenticate(authInputToken);								// Autenticamos el usuari
	            String token = jwtUtil.generateToken(person.getDni());					// Obtenemos su token

	            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("jwt_token", token));			// Retornamos el resultado
	        }
	        catch (AuthenticationException authExc){
	        	if (personService.existPerson(person.getDni())) {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Contraseña incorrecta"));			// Si la contraseña es incorrecta lo indicamos
	        	}
	        	else {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "El usuario no existe"));			// Si el usuario no existe lo indicamos 
	        	}
	        }
	    }
	    
	    /**
	     * Permite validar si el token es válido
	     */
	    @GetMapping("auth/checktoken")
	    public ResponseEntity isTokenValid() {
    		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 				// Si estamos autorizados devolveremos un NO_CONTENT si no spring devolverá un 401
	    }
	    
}
