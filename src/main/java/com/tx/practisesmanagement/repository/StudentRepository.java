package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Student;


/**
 * Repositorio de estudiantes
 * @author Salva
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, String>{

}
