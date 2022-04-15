package com.tx.practisesmanagement.security;



import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.tx.practisesmanagement.model.Person;
import com.tx.practisesmanagement.service.AdministratorService;
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
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        
    	Person user = administratorService.get(dni);						// Buscamos en administradores
    	
    	if (user == null) {
    		user = teacherService.get(dni);									// Si no existe lo buscamos en profesores
    		
    		if (user == null) {
    			user = studentService.get(dni);								// SI no existe buscamos en Estudiantes
    			
    			if (user == null) {
    	            throw new UsernameNotFoundException("No se encontró el usuario con dni " + dni);	// Si no existe lo indicamos
    			}
    		}
    	}

    	// Creamos un nuevo usuario
    	
        return new User(
        		dni,
        		user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRol().toString()))
        );
    }
}
