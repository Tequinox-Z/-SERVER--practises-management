package com.tx.practisesmanagement.model;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Clase de empresa
 * @author Salva
 */
@Entity
public class Business {
	@Id
	private String cif;								// Identificador de la empresa
	private String name;							// Nombre de la empresa
	private Integer numberOfStudents;				// Número de estudiantes	
	private String image;
	
	@JsonIgnore
	@OneToOne
	private Location location;
	
	@OneToMany(mappedBy = "business")
	@JsonIgnore
	private List<Practise> practises;				// Practicas que hace la empresa

	@OneToMany(mappedBy = "business")
	@JsonIgnore
	private List<LaborTutor> tutors;
	
	@ManyToMany
	@JsonIgnore
	private List<ProfessionalDegree> degrees;
	
	@OneToMany
	@JsonIgnore
	private List<ContactWorker> contactWorkers;
	
	/**
	 * Constructor sin parámetros
	 */
	public Business() {
		super();
		this.practises = new ArrayList<Practise>();			// Inicializamos la colección
	}

	
	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public List<ProfessionalDegree> getDegrees() {
		return degrees;
	}


	public void setDegrees(List<ProfessionalDegree> degrees) {
		this.degrees = degrees;
	}


	public List<ContactWorker> getContactWorkers() {
		return contactWorkers;
	}


	public void setContactWorkers(List<ContactWorker> contactWorkers) {
		this.contactWorkers = contactWorkers;
	}


	/**
	 * Constructor con parámetros
	 * @param cif: Identificador de la empresa
	 * @param name: Nombre de la empresa
	 * @param numberOfStudents: Número de estudiantes
	 * @param practises: Lista de prácticas
	 */
	public Business(String cif, String name, Integer numberOfStudents, List<Practise> practises) {
		super();
		// ASignamos todos los datos
		this.cif = cif;
		this.name = name;
		this.numberOfStudents = numberOfStudents;
		this.practises = practises;
	}

	/**
	 * Constructor con cif, nombre y número de estudiantes
	 * @param cif: Identificador de la empresa
	 * @param name: Nombre de la empresa
	 * @param numberOfStudents: Número de estudiantes
	 */
	public Business(String cif, String name, Integer numberOfStudents) {
		super();
		
		// Asignamos todos los datos
		
		this.cif = cif;
		this.name = name;
		this.numberOfStudents = numberOfStudents;
		this.practises = new ArrayList<Practise>();					// Inicializamos la colección
	}

	
	public List<LaborTutor> getTutors() {
		return tutors;
	}

	public void setTutors(List<LaborTutor> tutors) {
		this.tutors = tutors;
	}

	/**
	 * Obtiene el cif de la empresa
	 * @return CIF de la empresa
	 */
	public String getCif() {
		return cif;
	}

	/**
	 * Obtiene el nombre de la empresa
	 * @return Nombre de la empresa
	 */
	public String getName() {
		return name;
	}

	/**
	 * Establece el nombre de la empresa
	 * @param name: Nuevo nombre de la empresa
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtiene el número de estudiantes
	 * @return Número de esudiantes
	 */
	public Integer getNumberOfStudents() {
		return numberOfStudents;
	}

	/**
	 * Establece el número de estudiantes
	 * @param numberOfStudents: Nuevo número de estudiantes
	 */
	public void setNumberOfStudents(Integer numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	/**
	 * Obtiene todas las prácticas de la empresa
	 * @return Lista de prácticas
	 */
	public List<Practise> getPractises() {
		return practises;
	}
	
	/**
	 * Establece el cif de la empresa
	 * @param cif: Nuevo cif de la empresa
	 */
	public void setCif(String cif) {
		this.cif = cif;
	}
	
	/**
	 * Establece las prácticas de una empresa
	 * @param practises: Nueva lista de prácticas
	 */
	public void setPractises(List<Practise> practises) {
		this.practises = practises;
	}

	/**
	 * Hashcode de la empresa
	 */
	@Override
	public int hashCode() {
		return Objects.hash(cif);
	}

	/**
	 * Equals de la empresa
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Business other = (Business) obj;
		return Objects.equals(cif, other.cif);
	}

	/**
	 * Información de la empresa
	 */
	@Override
	public String toString() {
		return "Business [cif=" + cif + ", name=" + name + ", practises=" + practises + "]";
	}


}
