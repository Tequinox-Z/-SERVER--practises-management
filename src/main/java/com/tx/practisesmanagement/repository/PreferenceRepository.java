package com.tx.practisesmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.Preference;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

	@Query(value = "SELECT p FROM Preference p WHERE p.enrollment.id = ?1")
	public List<Preference> getPreferencesByIdEnrollment(Integer id);
	
	@Query(value = "SELECT p FROM Preference p WHERE p.enrollment = ?1 AND p.business = ?2")
	public Preference getByEnrollmentAndBusiness(Enrollment enrollment, Business business);
	
	@Query(value = "SELECT COUNT(p) FROM Preference p WHERE p.enrollment = ?1")
	public Integer countByEnrollment(Enrollment enrollment);
	
	@Query(value = "SELECT p FROM Preference p WHERE p.position > ?1 AND p.enrollment = ?2")
	public List<Preference> getPreferenceHiggerThat(Integer position, Enrollment enrollment);
	
	@Query(value = "SELECT p FROM Preference p WHERE p.enrollment = ?1 AND p.position = ?2")
	public Preference existPositionInEnrollment(Enrollment enrollment, Integer position);
	
	@Query(value = "SELECT p FROM Preference p WHERE p.business = ?1")
	public List<Preference> getByBusiness(Business business);
}