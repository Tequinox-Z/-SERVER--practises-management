package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Business;

/**
 * Repositorio de empresas
 * @author Salva
 *
 */
@Repository
public interface BusinessRepository extends JpaRepository<Business, String>{

	@Query(value = "SELECT COUNT(e) FROM Enrollment e WHERE extract(year FROM e.date) = ?2 AND e.practise.business = ?1")
	public Integer getCountOfStudentInBusinessAndYear(Business business, Integer year);
}
