package com.tx.practisesmanagement.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.enumerators.Rol;

@Entity
public class LaborTutor extends Person {
	
	@JsonIgnore
	@OneToMany(mappedBy = "laborTutor", fetch = FetchType.EAGER)
	private List<Practise> practises;
	
	@JsonIgnore
	@ManyToOne
	private Business business;
	
	public LaborTutor() {
		this.rol = Rol.ROLE_LABOR_TUTOR;
	}
	
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

	public List<Practise> getPractises() {
		return practises;
	}
	
	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public void setPractises(List<Practise> practises) {
		this.practises = practises;
	}

	@Override
	public String toString() {
		return "LaborTutor [practises=" + practises + "]";
	}
	
	
	
	
}