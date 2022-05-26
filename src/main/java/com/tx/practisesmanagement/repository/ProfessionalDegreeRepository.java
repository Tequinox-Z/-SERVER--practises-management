package com.tx.practisesmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.School;


/**
 * Repositorio de ciclos
 * @author Salva
 */
@Repository
public interface ProfessionalDegreeRepository extends JpaRepository<ProfessionalDegree, Integer> {

	@Query(value = "SELECT pf FROM ProfessionalDegree pf WHERE pf.school = ?1 AND UPPER(pf.name) = UPPER(?2)")
	public ProfessionalDegree getBySchoolAndName(School school, String degreeName);
}





