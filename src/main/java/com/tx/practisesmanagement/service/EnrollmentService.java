package com.tx.practisesmanagement.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.repository.EnrollmentRepository;


/**
 * Servicio de Matrícula
 * @author Salva
 */
@Service
public class EnrollmentService {
	
	// Servicios
		@Autowired private PractiseService practiseService;
	// Repositorios
		@Autowired private EnrollmentRepository enrollmentRepository;
		
	/**
	 * Obtiene todas las matrículas
	 * @return Lista de matrículas
	 */
	public List<Enrollment> getAll() {
		return enrollmentRepository.findAll();
	}
	
	/**
	 * Obtiene una determinada matrícula
	 * @param id: Id de la matrícula
	 * @return Matrícula solicitada
	 */
	public Enrollment get(Integer id) {
		return enrollmentRepository.findById(id).orElse(null);
	}
	
	/**
	 * Guarda un matrícula
	 * @param enrollment: Matrícula a guardar
	 * @return Matrícula guardada
	 */
	public Enrollment save(Enrollment enrollment) {
		return enrollmentRepository.save(enrollment);
	}
	
	/**
	 * Borra una matrícula
	 * @param enrollment: Matrícula a borrar
	 */
	public void delete(Enrollment enrollment) {
		enrollment.setStudent(null);								// Establecemos el estudiante a nulo
		enrollment.setProfessionalDegree(null);						// Establecemos el ciclo a nulo

		Integer practiseID = enrollment.getPractise().getId();		// Obtenemos el id de la practica
		
		enrollment.setPractise(null);								// Establecemos la practica a nulo
		
		this.save(enrollment);										// Guardamos la matrícula
		
		practiseService.remove(practiseID);							// Borramos la práctica
	}
}
