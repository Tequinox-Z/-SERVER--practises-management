package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.tx.practisesmanagement.model.Student;


/**
 * Repositorio de estudiantes
 * @author Salva
 */
public interface StudentRepository extends JpaRepository<Student, String>{

}
