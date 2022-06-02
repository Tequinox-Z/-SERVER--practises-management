package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.Student;


/**
 * Respositorio de matriculaciones
 * @author Salva
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer>{

	/**
	 * Verifica si existe o no una matrícula determinada
	 * @param year Año
	 * @param degree Ciclo
	 * @param student Estudiante
	 * @return Matrícula
	 */
	@Query(value = "SELECT e FROM Enrollment e WHERE extract(year FROM e.date) = ?1 AND e.professionalDegree = ?2 AND e.student = ?3")
	public Enrollment existEnrollment(Integer year, ProfessionalDegree degree, Student student);
}
