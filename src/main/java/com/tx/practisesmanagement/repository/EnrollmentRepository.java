package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Enrollment;


/**
 * Respositorio de matriculaciones
 * @author Salva
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer>{

}
