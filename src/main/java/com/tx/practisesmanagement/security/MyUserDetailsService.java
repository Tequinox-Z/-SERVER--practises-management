package com.tx.practisesmanagement.security;



import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Person;
import com.tx.practisesmanagement.service.AdministratorService;
import com.tx.practisesmanagement.service.PersonService;
import com.tx.practisesmanagement.service.StudentService;
import com.tx.practisesmanagement.service.TeacherService;

/**
 * Servicio de detalles de usuario
 * @author Salva
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

	// Servicios
    	
		@Autowired private TeacherService teacherService;
    	@Autowired private StudentService studentService;
    	@Autowired private AdministratorService administratorService;
    	
    /**
     * Obtiene un usuario por dni
     */
    	
    @Override
    public UserDetails loadUserByUsername(String dni) throws RuntimeException {
        
    	Person user = this.getPerson(dni);
    	
    	if (!user.isEnabled()) {
    		throw new UserErrorException("Usuario deshabilitado");
    	}

    	// Creamos un nuevo usuario
    	
        return new User(
        		dni,
        		user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRol().toString()))
        );
    }
    
	public Person getPerson(String dni) throws UsernameNotFoundException {
    	Person person = administratorService.get(dni);						// Buscamos en administradores

    	if(person == null) {
    		person = teacherService.get(dni);									// Si no existe lo buscamos en profesores
    		
    		if (person == null) {
    			person = studentService.get(dni);								// Si no existe buscamos en Estudiantes
    		}
    	}
    	
    	return person;
	}
	
}
