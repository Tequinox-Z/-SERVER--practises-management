package com.tx.practisesmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.LaborTutor;

/**
 * Repositorio de tutor laboral
 * @author Salvador
 */
@Repository
public interface LaborTutorRepository extends JpaRepository<LaborTutor, String> {
	@Query("SELECT l FROM LaborTutor l WHERE UPPER(l.name) LIKE %?1% OR UPPER(l.dni) LIKE %?1%")
	List<LaborTutor> getAllTutorByName(String name);
}
