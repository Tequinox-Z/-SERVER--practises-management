package com.tx.practisesmanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tx.practisesmanagement.dto.ResetPasswordDTO;
import com.tx.practisesmanagement.error.RestError;
import com.tx.practisesmanagement.model.Person;
import com.tx.practisesmanagement.model.School;
import com.tx.practisesmanagement.service.AditionalsFunctionsService;
import com.tx.practisesmanagement.service.AdministratorService;
import com.tx.practisesmanagement.service.SchoolService;
import com.tx.practisesmanagement.service.StudentService;
import com.tx.practisesmanagement.service.TeacherService;



/**
 * Controlador de funciones adicionales. Reune todas aquellas funciones que no son recursos
 * @author Salva
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AditionalsFunctionsController {
	
	// Servicios
	
		@Autowired private AdministratorService administratorService;		// Servicio de administrador
		@Autowired private StudentService studentService;					// Servicio de estudiante
		@Autowired private TeacherService teacherService;					// Servicio de profesor
		@Autowired private AditionalsFunctionsService aditinalsFuncions;	// Servicio de funciones adicionales
		@Autowired private SchoolService schoolService;						// Servicio de colegio
		
	// Encriptación / Desencriptación
	
		//@Autowired private BCryptPasswordEncoder desencoder;
	
	/**
	 * Permite resetear una contraseña olvidada enviando la misma al correo del usuario
	 * @param resetPassword : DTO que contiene el dni del usuario
	 * @return NO_CONTENT
	 */
	@PostMapping("reset-password")
	public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPassword) {
		if (resetPassword.getDni() == null) {	
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        			new RestError(HttpStatus.BAD_GATEWAY, "Indique su dirección de correo")		// Si el correo es nulo lanzamos error
    		);
		}
		
		Person user = administratorService.get(resetPassword.getDni() ); 						// Obtenemos el usuario
    	
    	if (user == null) {
    		user = teacherService.get(resetPassword.getDni() );									// Si no existe lo buscamos en profesores
    		
    		if (user == null) {
    			user = studentService.get(resetPassword.getDni() );								// Si no existe lo buscamos en estudiantes
    			
    			if (user == null) {
    	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
    	        			new RestError(HttpStatus.BAD_GATEWAY, "Dni no registrado")			// Si no existe el usuario no está registrado
    	    		);
    			}
    		}
    	}
		
    	
    	//String result = "";																		// Esta variable almacenará el resultado
    	
    	// =================================== Desencriptar la contraseña cifrada por spring security ===================================
    	
    	try {
    		//result = aditinalsFuncions.desencryptMd5(Encryptors.text("", "salt").toString());	// Quitamos el cifrado MD5
    	}
    	catch(Exception e) {
    		
    	}						
				
	    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();							// Devolvemos un NO_CONTENT
	}
	
	
	@GetMapping("school/{idSchool}/briefing")
	public ResponseEntity getBriefingFromSchool(@PathVariable Integer idSchool) {
		
		School school = schoolService.get(idSchool);
		
		if (school == null) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        			new RestError(HttpStatus.NOT_FOUND, "El centro no existe")			// Si no existe el centro lo indicamos
    		);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
    			schoolService.getCountAdministrators(idSchool)
		);
	}
	
	@GetMapping("example")
	public ResponseEntity get() {
		return ResponseEntity.status(HttpStatus.OK).body(this.administratorService.getCountFromSchool(1) + " - " + teacherService.getCountTeacherFromSchool(1));
	}

}
