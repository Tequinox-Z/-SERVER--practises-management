package com.tx.practisesmanagement.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Servicio de personas
 * @author Salva
 */
@Service
public class PersonService {
	
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
		
		boolean exist = false;					// Esta variable indicará si existe o no
		
		if (administratorService.get(dni) != null || teacherService.get(dni) != null || studentService.get(dni) != null) {
			exist = true;		// Si el usuario existe lo indicamos como true
		}
		
		return exist;			// Retornamos el resultado
	}
	
}
