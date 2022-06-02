package com.tx.practisesmanagement.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Preferencia de una empresa de un alumno
 * @author Salvador
 */
@Entity
public class Preference {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;													// Identificador
	private Integer position;											// Posición de la empresa
	
	@ManyToOne
	@JsonIgnore
	private Enrollment enrollment;										// Matricula perteneciente
	
	@ManyToOne
	@JsonIgnore
	private Business business;											// Empresa elegida
	
	/**
	 * Constructor sin parámetros
	 */
	public Preference() {
		super();
	}
	
	/**
	 * Constructor con parámetros
	 * @param id: Identificador
	 */
	public Preference(Integer id) {
		super();
		this.id = id;
	}
	
	/**
	 * Constructor con parámetros
	 * @param id Identificador
	 * @param position Posición
	 */
	public Preference(Integer id, Integer position) {
		super();
		this.id = id;
		this.position = position;
	}
	
	/**
	 * Constructor con parámetros
	 * @param position Posición
	 * @param business Empresa
	 * @param enrollment Matricula
	 */
	public Preference(Integer position, Business business, Enrollment enrollment) {
		super();
		this.position = position;
		this.business = business;
		this.enrollment = enrollment;
	}

	/**
	 * Obtiene el identificador
	 * @return Identificador
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Establece el identificador
	 * @param id Nuevo identificador
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	/**
	 * Obtiene la matrícula
	 * @return Matricula asignada
	 */
	public Enrollment getEnrollment() {
		return enrollment;
	}

	/**
	 * Establece la matrícula
	 * @param enrollment: Nueva matrícula
	 */
	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	/**
	 * Obtiene la empresa
	 * @return Empresa elegida
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * Establece la empresa
	 * @param business: Nueva empresa
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

	/**
	 * Obtiene la posición
	 * @return Posición asignada
	 */
	public Integer getPosition() {
		return position;
	}
	
	/**
	 * Establece la posición
	 * @param position Nuena posición
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Preference other = (Preference) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Preference [id=" + id + ", position=" + position + "]";
	}
	
	
	
	
	
}
