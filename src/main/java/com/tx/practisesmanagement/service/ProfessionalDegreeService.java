package com.tx.practisesmanagement.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.School;
import com.tx.practisesmanagement.model.Teacher;
import com.tx.practisesmanagement.repository.ProfessionalDegreeRepository;

/**
 * Servicio de ciclos
 * @author Salva
 *
 */
@Service
public class ProfessionalDegreeService {
	
	// Repositorios
		@Autowired ProfessionalDegreeRepository professionalDegreeRepository;
		
	// Servicios
		@Autowired TeacherService teacherService;
		@Autowired EnrollmentService enrollmentService;
	/**
	 * Guarda un ciclo
	 * @param professionalDegree: Ciclo a guardar
	 * @return Ciclo guardado
	 */
	public ProfessionalDegree saveProfessionalDegree(ProfessionalDegree professionalDegree) {
		return professionalDegreeRepository.save(professionalDegree);
	}
	
	
	public List<ProfessionalDegree> getAll() {
		return professionalDegreeRepository.findAll();
	}
	
	/**
	 * Obtiene un determinado ciclo
	 * @param professionalDegree: Ciclo a obtener
	 * @return: Ciclo solicitado
	 */
	public ProfessionalDegree get(Integer professionalDegree) {
		return professionalDegreeRepository.findById(professionalDegree).orElse(null);
	}
	
	/**
	 * Borra un determinado ciclo
	 * @param idDegree: Identificador del ciclo
	 */
	public void removeDegree(Integer idDegree) {
		ProfessionalDegree professionalDegree = this.get(idDegree);						// Obtiene el ciclo
		
		professionalDegree.setSchool(null);												// Establece el colegio a nulo
		
		for (Enrollment currentEnrollment : professionalDegree.getEnrollments()) {
			currentEnrollment.setProfessionalDegree(null);								// Por cada matrícula establece el ciclo a nulo
			enrollmentService.save(currentEnrollment);
			enrollmentService.delete(currentEnrollment);
		}
		
		for (Teacher teacher: professionalDegree.getTeachers()) {
			teacher.getProfessionalDegrees().remove(professionalDegree);
			teacherService.save(teacher);
		}

		
		professionalDegreeRepository.save(professionalDegree);							// Guarda el ciclo
		professionalDegreeRepository.delete(professionalDegree);						// Borra el ciclo
	}
	
	/**
	 * Obtiene todos los ciclos 
	 * @return: Lista de ciclos
	 */
//	public List<ProfessionalDegree> getAllDifferent() {
//		return professionalDegreeRepository.findAll();
//	}
	
	/**
	 * Añade un profesor a un ciclo
	 * @param id: Identificador del ciclo
	 * @param teacher: Profesor a añadir
	 * @return Ciclo con profesor
	 */
	public ProfessionalDegree addTeacherToDegree(ProfessionalDegree professionalDegree, Teacher teacher) {				
		teacher.getProfessionalDegrees().add(professionalDegree);		// Añadimos el ciclo al profesor
			
		teacherService.save(teacher);									// Guardamos el profesor
		
		return professionalDegree;										// Retornamos el ciclo
	}
	
	/**
	 * Actualiza el ciclo
	 * @param idDegree: Identificador de ciclo
	 * @param degree: Ciclo con los datos
	 * @return: Ciclo actualizado
	 */
	public ProfessionalDegree updateDegree(Integer idDegree, ProfessionalDegree degree) {
		ProfessionalDegree oldDegree = this.get(idDegree);						// Obtenemos el ciclo
		oldDegree.setName(degree.getName());									// Establecemos el nombre
		
		return this.professionalDegreeRepository.save(oldDegree);				// Guardamos el ciclo y lo retornamos
	}
	
	
	public boolean existProfessionalDegreeByNameInSchool(School school, String nameDegree) {
		return this.professionalDegreeRepository.getBySchoolAndName(school, nameDegree) != null;
	}
	
	
	public List<ProfessionalDegree> getAllProfessionalDegreesByYear(Integer idSchool, Integer year) {
		return this.professionalDegreeRepository.getAllBySchoolAndYear(idSchool, year);
	}
}
