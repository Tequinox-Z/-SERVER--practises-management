package com.tx.practisesmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.RegTemp;

@Repository
public interface RegTempRepository extends JpaRepository<RegTemp, Integer> {

	@Query("select rt FROM School s JOIN s.temperatureRecords rt WHERE EXTRACT(HOUR from rt.date) = ?1 AND s.id = ?2")
	public RegTemp getByHour(Integer hour, Integer idSchool);
	
	@Query(value = "select rt FROM School s JOIN s.temperatureRecords rt WHERE s.id = ?4 and extract(day from rt.date) = ?1 and extract(month from rt.date) = ?2 and extract(year from rt.date) = ?3")
	public List<RegTemp> getByDay(Integer day, Integer month, Integer year, Integer idSchool);
	
	@Query(value = "select rt FROM School s JOIN s.temperatureRecords rt WHERE s.id = ?5 and extract(hour from rt.date) = ?4 and extract(day from rt.date) = ?1 and extract(MONTH from rt.date) = ?2 and extract(year from rt.date) = ?3")
	public RegTemp getByDayAndHour(Integer day, Integer month, Integer year, Integer hour, Integer idSchool);
	
}
