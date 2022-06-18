package com.tx.practisesmanagement.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.Student;
import com.tx.practisesmanagement.repository.StudentRepository;


/**
 * Servicio de estudiantes
 * @author Salva
 */
@Service
public class StudentService {

	// Repositorio
		@Autowired private StudentRepository studentRepository;
	
	// Servicio
		@Autowired private EnrollmentService enrollmentService;
	

	/**
	 * Guarda un estudiante
	 * @param student: Estudiante a guardar
	 * @return: Estudiante guardado
	 */
	public Student save(Student student) {
		return studentRepository.save(student);
	}
	
	/**
	 * Obtiene el numero de estudiantes en una escuela
	 * @param idSchool Id del centro
	 * @return Numero de estudiantes
	 */
	public Integer getCountFromSchool(Integer idSchool) {
		return this.studentRepository.getCountFromSchool(idSchool);
	}
	
	/**
	 * Obtiene todos los estudiantes por nombre
	 * @param name Nombre a buscar
	 * @return Lista de estudiantes que coinciden con la busqueda
	 */
	public List<Student> getAllByName(String name) {
		return studentRepository.getAllStudentByName(name.toUpperCase());
	}
	
	/**
	 * Obtiene un estudiante por dni
	 * @param dni: Dni del estudiante
	 * @return: Estudiante solicitado
	 */
	public Student get(String dni) {
		return studentRepository.findById(dni).orElse(null);
	} 
	
	/**
	 * Obtiene todos los estudiantes
	 * @return: Lista de estudiantes
	 */
	public List<Student> getAll() {
		return studentRepository.findAll();
	}
	
	/**
	 * Actualiza un estudiante
	 * @param student: Estudiante a actualizar
	 * @param personData: Nuevos datos del estudiantes
	 * @return: Estudiante actualizado
	 */
	public Student updateStudent(Student student, PersonDTO personData) {
		
		// Actualizamos todos los datos
		
		student.setAddress(personData.getAddress());
		student.setBirthDate(personData.getBirthDate());
		
		
		if (personData.getImage() != null) {
			student.setImage(personData.getImage());
		}
		
		student.setLastName(personData.getLastName());
		student.setName(personData.getName());
		student.setPassword(personData.getPassword());
		student.setTelefone(personData.getTelefone());
		student.setEmail(personData.getEmail());
		
		
		// Guardamos el estudiante
		
		return studentRepository.save(student);
	}
	
	/**
	 * Borra un estudiante
	 * @param dni: DNi del estudiante
	 */
	public void deleteStudent(String dni) {
		Student student = this.get(dni);									// Obtenemos el estudiante
		
		for (Enrollment currentEnrollment : student.getEnrollments()) {
			enrollmentService.delete(currentEnrollment);					// Por cada matrícula que tenga la eliminamos
		}
		
		studentRepository.delete(student);									// Borramos el estudiante
	}
	
	
	
}
