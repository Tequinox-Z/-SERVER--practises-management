package com.tx.practisesmanagement.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Teacher;


/**
 * Repositorio de profesores
 * @author Salva
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
	
	/**
	 * Obtiene el número de profesores en un cenro
	 * @param id Identificador
	 * @return Cantidad de profesores
	 */
	@Query("SELECT COUNT(DISTINCT tt) FROM ProfessionalDegree pf JOIN pf.teachers tt WHERE pf.school.id = ?1")                 
	Integer countTeachersFromSchool(Integer id);
	
	@Query("SELECT t FROM Teacher t WHERE UPPER(t.name) LIKE %?1% OR UPPER(t.dni) LIKE %?1%")
	List<Teacher> getAllTeacherByName(String name);
}


