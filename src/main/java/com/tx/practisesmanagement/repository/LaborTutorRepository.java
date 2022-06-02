package com.tx.practisesmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.LaborTutor;

/**
 * Repositorio de tutor laboral
 * @author Salvador
 */
@Repository
public interface LaborTutorRepository extends JpaRepository<LaborTutor, String> {

}
