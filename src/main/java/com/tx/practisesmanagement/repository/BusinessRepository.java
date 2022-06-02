package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Business;

/**
 * Repositorio de empresas
 * @author Salvador
 *
 */
@Repository
public interface BusinessRepository extends JpaRepository<Business, String>{

	/**
	 * Obtiene el recuento de estudiantes en una empresa en un año concreto
	 * @param business Empresa
	 * @param year Año
	 * @return Cantidad de estudiantes
	 */
	@Query(value = "SELECT COUNT(e) FROM Enrollment e WHERE extract(year FROM e.date) = ?2 AND e.practise.business = ?1")
	public Integer getCountOfStudentInBusinessAndYear(Business business, Integer year);
	
	/**
	 * Obtiene la cantidad de empresas existentes
	 * @return Cantidad de empresas
	 */
	@Query(value = "SELECT COUNT(b) From Business b")
	public Integer getCount();
}
