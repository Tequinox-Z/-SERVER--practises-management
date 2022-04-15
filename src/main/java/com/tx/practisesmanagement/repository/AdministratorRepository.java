package com.tx.practisesmanagement.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Administrator;



/**
 * Repositorio de administradores
 * @author Salva
 *
 */
@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, String>{
	
	/**
	 * Obtiene el número de administradores que tiene una determinada escuela
	 * @param id: Identificador de la escuela de la que se quiere consultar en número de administradores 
	 * @return Número de administradores de la escuela
	 */
	@Query("SELECT COUNT(a) FROM Administrator a WHERE a.schoolSetted.id = ?1")
	String getCountFromSchool(Integer id);
}
