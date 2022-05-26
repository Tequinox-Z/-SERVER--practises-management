package com.tx.practisesmanagement.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Clase de ciclo profesional
 * @author Salva
 */
@Entity
public class ProfessionalDegree {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;									// Nombre
	private Integer year;
	private String image;
	
	
	@JsonIgnore
	@ManyToMany(mappedBy = "degrees")
	private List<Business> businesses;						// Empresas
	
	@JsonIgnore
	@ManyToOne
	private School school;									// Escuela
	
	@ManyToMany(mappedBy = "professionalDegrees")
	@JsonIgnore
	private List<Teacher> teachers;							// Lista de profesores
	
	@OneToMany(mappedBy = "professionalDegree")
	@JsonIgnore
	private List<Enrollment> enrollments;					// Lista de matrículas
	
	/**
	 * Constructor sin parámetros
	 */
	public ProfessionalDegree() {
		super();
	}
	
	public Integer getYear() {
		return year;
	}



	public void setYear(Integer year) {
		this.year = year;
	}



	public String getImage() {
		return image;
	}



	public void setImage(String image) {
		this.image = image;
	}



	public List<Business> getBusinesses() {
		return businesses;
	}



	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}


	public ProfessionalDegree(Integer id) {
		super();
		this.id = id;
	}

	/**
	 * Constructor con todos los datos
	 * @param id: Identificador
	 * @param name: Nombre
	 * @param school: Escuela
	 * @param teachers: Profesores
	 * @param enrollments: Matrículas
	 */
	public ProfessionalDegree(String name, School school, List<Teacher> teachers,
			List<Enrollment> enrollments) {
		super();
		this.name = name;
		this.school = school;
		this.teachers = teachers;
		this.enrollments = enrollments;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre
	 * @return: Nombre
	 */
	public String getName() {
		return name;
	}

	/**
	 * Establece el nombre
	 * @param name: Nuevo nombre
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtiene la escuela
	 * @return: Escuela
	 */
	public School getSchool() {
		return school;
	}

	/**
	 * Establece la escuela
	 * @param school: Nueva escuela
	 */
	public void setSchool(School school) {
		this.school = school;
	}

	/**
	 * Obtiene los profesores
	 * @return: Profesores
	 */
	public List<Teacher> getTeachers() {
		return teachers;
	}

	/**
	 * Establece los profesores
	 * @param teachers: Nuevos profesores
	 */
	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	/**
	 * Obtiene las matrículas
	 * @return: Matrículas
	 */
	public List<Enrollment> getEnrollments() {
		return enrollments;
	}

	/**
	 * Establece las matrículas del ciclo
	 * @param enrollments: Nuevas matrículas
	 */
	public void setEnrollments(List<Enrollment> enrollments) {
		this.enrollments = enrollments;
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
		ProfessionalDegree other = (ProfessionalDegree) obj;
		return Objects.equals(id, other.id);
	}

	/**
	 * Muestra información del ciclo
	 */
	@Override
	public String toString() {
		return "ProfessionalDegree name=" + name + ", school=" + school + "]";
	} 
}
