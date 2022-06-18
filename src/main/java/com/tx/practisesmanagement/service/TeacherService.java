package com.tx.practisesmanagement.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.Teacher;
import com.tx.practisesmanagement.repository.TeacherRepository;

@Service
public class TeacherService {
	
	// Repositorio
		@Autowired TeacherRepository teacherRepository; 

	/**
	 * Guarda un profesor
	 * @param teacher: Profesor a guardar
	 * @return: Profesor guardado
	 */
	public Teacher save(Teacher teacher) {
		return teacherRepository.save(teacher);
	}
	
	/**
	 * Obtiene un determinado profesor
	 * @param dni: DNi del profesor
	 * @return: Profesor solicitado
	 */
	public Teacher get(String dni) {
		return teacherRepository.findById(dni).orElse(null);
	}
	
	/**
	 * Obtiene todos los profesores
	 * @return: Lista de profesores
	 */
	public List<Teacher> getAll() {
		return teacherRepository.findAll();
	}
	
	/**
	 * Obtiene todos los profesores por nombre o dni
	 * @param name Nombre o dni
	 * @return
	 */
	public List<Teacher> getAllByName(String name) {
		return teacherRepository.getAllTeacherByName(name.toUpperCase());
	}
	
	/**
	 * Actualiza un profesor
	 * @param teacher: Profesor
	 * @param personData: Datos nuevos
	 * @return: Profesor actualizado
	 */
	public Teacher updateTeacher(Teacher teacher, PersonDTO personData) {
		
		// Establecemos todos los datos
		
		if (personData.getAddress() != null) {
			teacher.setAddress(personData.getAddress());			
		}
		if (personData.getBirthDate() != null) {
			teacher.setBirthDate(personData.getBirthDate());			
		}
		if (personData.getImage() != null) {
			teacher.setImage(personData.getImage());			
		}
		if (personData.getLastName() != null) {
			teacher.setLastName(personData.getLastName());			
		}
		if (personData.getName() != null) {			
			teacher.setName(personData.getName());
		}
		if (personData.getTelefone() != null) {
			teacher.setTelefone(personData.getTelefone());			
		}
		if (personData.getEmail() != null) {
			teacher.setEmail(personData.getEmail());			
		}
		
		
		// Guardamos
		
		return teacherRepository.save(teacher);
	}
	
	/**
	 * Borra un profesor
	 * @param dni: DNi del profesor
	 * @return: Profesor borrado
	 */
	public Teacher deleteTeacher(String dni) {
		
		Teacher teacher = quitDegreesFromTeacher(dni);				// Quitamos sus ciclos
		
		teacherRepository.delete(teacher);							// Borramos el profesor
		
		return teacher;												// Retornamos el profesor
	}
	
	/**
	 * Borra los ciclos de un profesor
	 * @param dni: DNi del profesor
	 * @return: Profesor sin ciclos
	 */
	public Teacher quitDegreesFromTeacher(String dni) {
		Teacher teacher = this.get(dni);							// Obtenemos el profesor
		teacher.getProfessionalDegrees().clear();					// Borramos sus ciclos
		
		return teacherRepository.save(teacher);						// Guardamos el profesor
	}
	
	/**
	 * Añade un nuevo ciclo al profesor
	 * @param professionalDegree CIclo a añadir 
	 * @param teacher Profesor al que se le añadirá
	 * @return Profesor con ciclo añadido
	 */
	public Teacher addDegree(ProfessionalDegree professionalDegree, Teacher teacher) {
		Teacher currentTeacher = get(teacher.getDni());
		
		currentTeacher.getProfessionalDegrees().add(professionalDegree);
		
		return save(currentTeacher);
	}
	
	/**
	 * Obtiene el numero de profesor de una escuela
	 * @param id Id de la escuela
	 * @return Numero de profesores
	 */
	public Integer getCountTeacherFromSchool(Integer id) {
		return this.teacherRepository.countTeachersFromSchool(id);
	}
	
	
}