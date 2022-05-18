package com.tx.practisesmanagement.service;



import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.Person;
import com.tx.practisesmanagement.model.Student;
import com.tx.practisesmanagement.model.Teacher;
/**
 * Servicio de personas
 * @author Salva
 */
@Service
public class PersonService {
	
		@Autowired private PasswordEncoder passwordEncoder;							
	
	// Servicios
		@Autowired AdministratorService administratorService;
		@Autowired StudentService studentService;
		@Autowired TeacherService teacherService;
	
	/**
	 * Valida si existe una persona
	 * @param dni: Dni de persona
	 * @return: boolean
	 */
	public boolean existPerson(String dni) {
		
		boolean exist = false;											// Esta variable indicará si existe o no
		
		if (administratorService.get(dni) != null || teacherService.get(dni) != null || studentService.get(dni) != null) {
			exist = true;												// Si el usuario existe lo indicamos como true
		}
		
		return exist;													// Retornamos el resultado
	}
	
	public Person getPerson(String dni) throws UsernameNotFoundException {
    	Person person = administratorService.get(dni);						// Buscamos en administradores

    	if(person == null) {
    		person = teacherService.get(dni);									// Si no existe lo buscamos en profesores
    		
    		if (person == null) {
    			person = studentService.get(dni);								// Si no existe buscamos en Estudiantes
    			
    			if (person == null) {
    	            throw new UsernameNotFoundException("No se encontró el usuario con dni " + dni);	// Si no existe lo indicamos
    			}
    		}
    	}
    	
    	return person;
	}
	
	
	public HashMap<HttpStatus, String> getErrorLogin(String dni, String password) {
		
		HashMap<HttpStatus, String> error = new HashMap<>();
		
		if (!this.existPerson(dni)) {
			error.put(HttpStatus.NOT_FOUND, "Usuario no registrado");
		}
		else {
			Person person = this.getPerson(dni);
			
			if (person.isEnabled()) {
				error.put(HttpStatus.BAD_REQUEST, "Contraseña incorrecta");
			}
			else {
				error.put(HttpStatus.UNAUTHORIZED, "Usuario deshabilitado");
			}
		}
		
		return error;
	}
	
	
	public void enable(String dni) {
		enableOrDisableUser(dni, true);
	}
	
	public void disable(String dni) {
		enableOrDisableUser(dni, false);
	}
	
	
	private void enableOrDisableUser(String dni, boolean setStatus) {
    	Administrator administrator = administratorService.get(dni);						// Buscamos en administradores

    	if (administrator == null) {
    		Teacher teacher = teacherService.get(dni);									// Si no existe lo buscamos en profesores
    		
    		if (teacher == null) {
    			Student student = studentService.get(dni);								// Si no existe buscamos en Estudiantes
    			
    			if (student == null) {
    	            throw new UsernameNotFoundException("No se encontró el usuario con dni " + dni);	// Si no existe lo indicamos
    			}
    			else {
    	    		student.setEnabled(setStatus);
    	    		studentService.save(student);
    			}
    		}
    		else {
        		teacher.setEnabled(setStatus);
        		teacherService.save(teacher);
    		}
    	}
    	else {
    		administrator.setEnabled(setStatus);
    		administratorService.save(administrator);
    	}
	}
	
}
