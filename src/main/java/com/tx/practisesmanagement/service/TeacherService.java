package com.tx.practisesmanagement.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.dto.PersonDTO;
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
	 * Actualiza un profesor
	 * @param teacher: Profesor
	 * @param personData: Datos nuevos
	 * @return: Profesor actualizado
	 */
	public Teacher updateTeacher(Teacher teacher, PersonDTO personData) {
		
		// Establecemos todos los datos
		
		teacher.setAddress(personData.getAddress());
		teacher.setBirthDate(personData.getBirthDate());
		teacher.setImage(personData.getImage());
		teacher.setLastName(personData.getLastName());
		teacher.setName(personData.getName());
		teacher.setPassword(personData.getPassword());
		teacher.setTelefone(personData.getTelefone());
		teacher.setEmail(personData.getEmail());
		
		
		// Guardamos
		
		return teacherRepository.save(teacher);
	}
	
	/**
	 * Borra un profesor
	 * @param dni: DNi del profesor
	 * @return: Profesor borrado
	 */
	public Teacher deleteTeacher(String dni) {
		
		Teacher teacher = removeDegreesFromTeacher(dni);				// Quitamos sus ciclos
		
		teacherRepository.delete(teacher);							// Borramos el profesor
		
		return teacher;												// Retornamos el profesor
	}
	
	/**
	 * Borra los ciclos de un profesor
	 * @param dni: DNi del profesor
	 * @return: Profesor sin ciclos
	 */
	public Teacher removeDegreesFromTeacher(String dni) {
		Teacher teacher = this.get(dni);							// Obtenemos el profesor
		teacher.getProfessionalDegrees().clear();					// Borramos sus ciclos
		
		return teacherRepository.save(teacher);						// Guardamos el profesor
	}
	
	public String getCountTeacherFromSchool(Integer id) {
		return this.teacherRepository.countTeachersFromSchool(id);
	}
	
	
}