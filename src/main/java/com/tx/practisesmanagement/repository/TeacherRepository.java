package com.tx.practisesmanagement.repository;



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
	
	@Query("SELECT COUNT(DISTINCT tt) FROM ProfessionalDegree pf JOIN pf.teachers tt WHERE pf.school.id = ?1")                 
	String countTeachersFromSchool(Integer id);

}


