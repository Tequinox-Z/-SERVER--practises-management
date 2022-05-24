package com.tx.practisesmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

	@Query(value = "SELECT l FROM Location l, Business b WHERE b.location.id = l.id")
	List<Location> getAllLocationBusineses();
	
	@Query(value = "SELECT l FROM Location l, School s WHERE s.location.id = l.id")
	List<Location> getAllLocationSchools();
	
	@Query(value = "SELECT l FROM Location l, Business b WHERE b.location.id = l.id AND l.latitude <= ?1 AND l.latitude >= ?2 AND l.longitude <= ?3 AND l.longitude >= ?4")
	List<Location> getAllLocationBusinesesByUbication(Double latitudeMax, Double latitudeMin, Double longitudeMax, Double LongitudeMin);
	
	@Query(value = "SELECT l FROM Location l, School b WHERE b.location.id = l.id AND l.latitude <= ?1 AND l.latitude >= ?2 AND l.longitude <= ?3 AND l.longitude >= ?4")
	List<Location> getAllLocationSchoolsByUbication(Double latitudeMax, Double latitudeMin, Double longitudeMax, Double LongitudeMin);
}
