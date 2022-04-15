package com.tx.practisesmanagement.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.School;


/**
 * Repositorio de escuelas
 * @author Salva
 */
@Repository
public interface SchoolRepository extends JpaRepository<School, Integer>{

	/**
	 * Obtiene todos los colegios por nombre
	 * @param name: Nombre
	 * @return Lista de colegios
	 */
	@Query("SELECT s FROM School s WHERE s.name LIKE %?1%")
	List<School> findAllByName(String name);
	
}
