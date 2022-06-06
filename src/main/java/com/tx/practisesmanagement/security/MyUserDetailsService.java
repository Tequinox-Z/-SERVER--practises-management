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
import com.tx.practisesmanagement.service.LaborTutorService;
import com.tx.practisesmanagement.service.PersonService;
import com.tx.practisesmanagement.service.StudentService;
import com.tx.practisesmanagement.service.TeacherService;

/**
 * Servicio de detalles de usuario, permite obtener un usuario mediante su dni
 * @author Salvador
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

	// ============================================ Servicios ============================================
    	
		@Autowired private TeacherService teacherService;
    	@Autowired private StudentService studentService;
    	@Autowired private AdministratorService administratorService;
    	@Autowired private LaborTutorService laborTutorService;
    	
    /**
     * Carga un usuario mediante su dni
     */
    	
    @Override
    public UserDetails loadUserByUsername(String dni) throws RuntimeException {
        
    	// Obtenemos la persona
    	
    	Person user = this.getPerson(dni);
    	
    	// Comprobamos si el usuario existe
    	
    	if (user == null) {
    		throw new UserErrorException("El usuario no existe");
    	}

    	// Comprobamos si está habilitado o no, en caso de que esté deshabilitado lanzamos excepción indicándolo
    	
    	if (!user.isEnabled()) {
    		throw new UserErrorException("Usuario deshabilitado");
    	}

    	// Creamos un nuevo usuario y otorgamos el rol
    	
        return new User(
        		dni,
        		user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRol().toString()))
        );
    }
    
	public Person getPerson(String dni) throws UsernameNotFoundException {
    	Person person = administratorService.get(dni);						// Buscamos en Administradores

    	if (person == null) {
    		person = teacherService.get(dni);									// Si no existe lo buscamos en Profesores
    		
    		if (person == null) {
    			person = studentService.get(dni);								// Si no existe buscamos en Estudiantes
    			
    			if (person == null) {
    				person = laborTutorService.getById(dni);
    			}
    		}
    	}
    	
    	return person;														// Retornamos el usuario, o en su defecto nulo
	}
	
}
