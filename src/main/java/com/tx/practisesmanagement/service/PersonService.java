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
	
	/**
	 * Obtiene una persona 
	 * @param dni DNi de la persona
	 * @return Persona solicitada
	 * @throws UsernameNotFoundException En caso de que no exista
	 */
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
    	
    	return person;											// Retorna la persona
	}
	
	/**
	 * Establece una nueva contraseña a una persona
	 * @param dni Dni de la persona
	 * @param password COntraseña para la persona
	 * @return Persona actualizada
	 * @throws UsernameNotFoundException En caso de que no exista la persona
	 */
	public Person setNewPassword(String dni, String password) throws UsernameNotFoundException {
    	Administrator admin = administratorService.get(dni);						// Buscamos en administradores

    	if (admin == null) {
    		Teacher teacher = teacherService.get(dni);									// Si no existe lo buscamos en profesores
    		
    		if (teacher == null) {
    			Student student = studentService.get(dni);								// Si no existe buscamos en Estudiantes
    			
    			if (student == null) {
    	            throw new UsernameNotFoundException("No se encontró el usuario con dni " + dni);	// Si no existe lo indicamos
    			}
    			else {
    				student.setPassword(password);
    				return studentService.save(student);							// Establecemos y guardamos
    			}
    		}
    		else {
    			teacher.setPassword(password);
    			return teacherService.save(teacher);								// Establecemos y guardamos
    		}
    	}
    	else {
    		admin.setPassword(password);
    		return administratorService.save(admin);								// Establecemos y guardamos
    	}
	}
	
	/**
	 * Obtiene el error producido en el login
	 * @param dni Dni de la persona
	 * @param password Contrasñea
	 * @return Hasmpa con el codigo de error y el error
	 */
	public HashMap<HttpStatus, String> getErrorLogin(String dni, String password) {
		
		HashMap<HttpStatus, String> error = new HashMap<>();
		
		if (!this.existPerson(dni)) {
			error.put(HttpStatus.NOT_FOUND, "Usuario no registrado");							// Si no está registrado lo indicamos
		}
		else {
			Person person = this.getPerson(dni);												// Obtenemos la persona
			
			if (person.isEnabled()) {
				error.put(HttpStatus.BAD_REQUEST, "Contraseña incorrecta");					 	// Si la contrasñea es incorrecta lo indicamos
			}
			else {
				error.put(HttpStatus.UNAUTHORIZED, "Usuario deshabilitado");					// Si el usuario está deshabilitado lo indicamos
			}
		}
		
		return error;																			// Retornamos el error
	}
	
	/**
	 * Habilita un usuario
	 * @param dni Dni del usuario
	 */
	public void enable(String dni) {
		enableOrDisableUser(dni, true);
	}
	
	/**
	 * Deshabilita un usuario
	 * @param dni DNi del usuario
	 */
	public void disable(String dni) {
		enableOrDisableUser(dni, false);
	}
	
	/**
	 * Habilita o deshabilita un usuario
	 * @param dni Dni del usuario
	 * @param setStatus Estado a establecer
	 */
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
    	    		student.setEnabled(setStatus);													// Establecemos el estado y guardamos
    	    		studentService.save(student);
    			}
    		}
    		else {
        		teacher.setEnabled(setStatus);														// Establecemos el estado y guardamos
        		teacherService.save(teacher);
    		}
    	}
    	else {
    		administrator.setEnabled(setStatus);													// Establecemos el estado y guardamos
    		administratorService.save(administrator);
    	}
	}
	
}
