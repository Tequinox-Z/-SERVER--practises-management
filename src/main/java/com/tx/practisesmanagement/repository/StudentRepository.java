package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Student;


/**
 * Repositorio de estudiantes
 * @author Salva
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, String>{

	/**
	 * Obtiene el número de estudiantes en una escuela
	 * @param idSchool Identificador del centro
	 * @return Cantidad de estudiantes
	 */
	@Query(value = "SELECT count(e) FROM Enrollment e WHERE e.professionalDegree.school.id = ?1")
	public Integer getCountFromSchool(Integer idSchool);
}
