package com.tx.practisesmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.Preference;

/**
 * Repositorio de preferencias
 * @author Salvador
 */
@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

	/**
	 * Obtiene las preferencias por matriculación
	 * @param id Identificador de la matrícula
	 * @return Lista de preferencias
	 */
	@Query(value = "SELECT p FROM Preference p WHERE p.enrollment.id = ?1")
	public List<Preference> getPreferencesByIdEnrollment(Integer id);
	
	/**
	 * Obtiene las preferencias por matricula y empresa
	 * @param enrollment Matricula
	 * @param business Empresa
	 * @return Lista de preferencias
	 */
	@Query(value = "SELECT p FROM Preference p WHERE p.enrollment = ?1 AND p.business = ?2")
	public Preference getByEnrollmentAndBusiness(Enrollment enrollment, Business business);
	
	/**
	 * Obtiene un recuento de preferencias
	 * @param enrollment: Matricula
	 * @return Número de preferencias
	 */
	@Query(value = "SELECT COUNT(p) FROM Preference p WHERE p.enrollment = ?1")
	public Integer countByEnrollment(Enrollment enrollment);
	
	/**
	 * Obtiene aquellas preferencias con posición superior a la indicada
	 * @param position Posición
	 * @param enrollment Matricula
	 * @return Lista de preferencias
	 */
	@Query(value = "SELECT p FROM Preference p WHERE p.position > ?1 AND p.enrollment = ?2")
	public List<Preference> getPreferenceHiggerThat(Integer position, Enrollment enrollment);
	
	/**
	 * Verifica si existe cierta posición en la lista de preferencas
	 * @param enrollment Matrícula
	 * @param position Posición
	 * @return Preferencia
	 */
	@Query(value = "SELECT p FROM Preference p WHERE p.enrollment = ?1 AND p.position = ?2")
	public Preference existPositionInEnrollment(Enrollment enrollment, Integer position);
	
	/**
	 * Obtiene la empresa desde una preferencia concreta
	 * @param business: Empresa
	 * @return Preferencia
	 */
	@Query(value = "SELECT p FROM Preference p WHERE p.business = ?1")
	public List<Preference> getByBusiness(Business business);
}
