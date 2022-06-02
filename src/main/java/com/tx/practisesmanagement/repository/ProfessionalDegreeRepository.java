package com.tx.practisesmanagement.repository;

import java.util.List;

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

	/**
	 * Obtiene un ciclo por centro y nombre
	 * @param school Centro
	 * @param degreeName Nombre del ciclo
	 * @return Ciclo
	 */
	@Query(value = "SELECT pf FROM ProfessionalDegree pf WHERE pf.school = ?1 AND UPPER(pf.name) = UPPER(?2)")
	public ProfessionalDegree getBySchoolAndName(School school, String degreeName);
	
	
	/**
	 * Obtiene todos los ciclos por centro y año
	 * @param idSchool Centro
	 * @param year Año 
	 * @return Lista de ciclos
	 */
	@Query(value = "SELECT pf FROM ProfessionalDegree pf WHERE pf.school.id = ?1 AND pf.year = ?2")
	public List<ProfessionalDegree> getAllBySchoolAndYear(Integer idSchool, Integer year);
}





