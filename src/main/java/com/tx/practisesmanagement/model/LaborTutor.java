package com.tx.practisesmanagement.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.enumerators.Rol;

/**
 * Clase del usuario Tutor laboral
 * @author Salvador
 */
@Entity
public class LaborTutor extends Person {
	
	@JsonIgnore
	@OneToMany(mappedBy = "laborTutor", fetch = FetchType.EAGER)
	private List<Practise> practises;													// Practicas que administra
	
	@JsonIgnore
	@ManyToOne
	private Business business;															// Empresa asignada
	
	/**
	 * Constructor sin parámetros
	 */
	public LaborTutor() {
		this.rol = Rol.ROLE_LABOR_TUTOR;
	}
	
	/**
	 * Constructor desde persona
	 * @param person: Datos de la persona
	 */
	public LaborTutor(PersonDTO person) {
		this.dni = person.getDni();
		this.address = person.getAddress();
		this.birthDate = person.getBirthDate();
		this.image = person.getImage();
		this.name = person.getName();
		this.lastName = person.getLastName();
		this.password = person.getPassword();
		this.telefone = person.getTelefone();
		this.email = person.getEmail();
		this.enabled = person.isEnabled();

		this.rol = Rol.ROLE_LABOR_TUTOR;
	}

	/**
	 * Obtiene todas las prácticas que tiene a su cargo
	 * @return: Lista de prácticas a su cargo
	 */
	public List<Practise> getPractises() {
		return practises;
	}
	
	/**
	 * Obtiene la empresa
	 * @return Empresa asignada
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * Establece una nueva empresa
	 * @param business: Nueva empresa
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

	/**
	 * Establece una lista de prácticas
	 * @param practises: Nueva lista de prácticas
	 */
	public void setPractises(List<Practise> practises) {
		this.practises = practises;
	}

	@Override
	public String toString() {
		return "LaborTutor [practises=" + practises + "]";
	}
	
	
	
	
}