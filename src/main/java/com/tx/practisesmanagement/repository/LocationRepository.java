package com.tx.practisesmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Location;

/**
 * Repositorio de localización
 * @author Salvador
 *
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

	/**
	 * Obtiene la localización de todas las empresas
	 * @return Lista de localización de empresas
	 */
	@Query(value = "SELECT l FROM Location l, Business b WHERE b.location.id = l.id")
	List<Location> getAllLocationBusineses();
	
	@Query(value = "SELECT l FROM Location l, Business b WHERE b.location.id = l.id AND UPPER(b.name) like %?1%")
	List<Location> getAllLocationByName(String name);
	
	/**
	 * Obtiene la localización de todos los centros
	 * @return Lista de localización de centros
	 */
	@Query(value = "SELECT l FROM Location l, School s WHERE s.location.id = l.id")
	List<Location> getAllLocationSchools();
	
	/**
	 * Obtiene la localización de todas las empresas por ubicación
	 * @return Lista de localización de empresas
	 */
	@Query(value = "SELECT l FROM Location l, Business b WHERE b.location.id = l.id AND l.latitude <= ?1 AND l.latitude >= ?2 AND l.longitude <= ?3 AND l.longitude >= ?4")
	List<Location> getAllLocationBusinesesByUbication(Double latitudeMax, Double latitudeMin, Double longitudeMax, Double LongitudeMin);
	
	/**
	 * Obtiene la localización de todos los centros por ubicación
	 * @return Lista de localización de centros
	 */
	@Query(value = "SELECT l FROM Location l, School b WHERE b.location.id = l.id AND l.latitude <= ?1 AND l.latitude >= ?2 AND l.longitude <= ?3 AND l.longitude >= ?4")
	List<Location> getAllLocationSchoolsByUbication(Double latitudeMax, Double latitudeMin, Double longitudeMax, Double LongitudeMin);
}
