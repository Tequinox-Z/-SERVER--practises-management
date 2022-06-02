package com.tx.practisesmanagement.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.enumerators.Rol;

/**
 * Clase estudiante
 * @author Salva
 */
@Entity
public class Student extends Person {
	
	@OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Enrollment> enrollments;						// Matrículas

	/**
	 * Constructor sin parámetros
	 */
	public Student() {
		super();
		this.rol = Rol.ROLE_STUDENT;						// Asignamos el rol
	}
	
	/**
	 * Constructor desde persona
	 * @param person: Datos de la persona
	 */
	public Student(PersonDTO person) {
		super();
		// Establecemos los datos
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

		this.rol = Rol.ROLE_STUDENT;					// Asignamos el rol 
	}
	
	/**
	 * Obtiene las matrículas
	 * @return Matrículas
	 */
	public List<Enrollment> getEnrollments() {
		return enrollments;
	}

	/**
	 * Establece las matrículas
	 * @param enrollments: Nuevas matrículas
	 */
	public void setEnrollments(List<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}

	
	
	/**
	 * Hashcode de estudiante
	 */
	@Override
	public int hashCode() {
		return Objects.hash(dni);
	}

	/**
	 * Equals de estudiante
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return Objects.equals(dni, other.dni);
	}

	/**
	 * Muestra información del estudiante
	 */
	@Override
	public String toString() {
		return "Student [dni=" + dni + ", birthDate=" + birthDate + ", name=" + name + ", lastName=" + lastName
				+ ", image=" + image + ", telefone=" + telefone + ", address=" + address + ", password=" + password
				+ ", rol=" + rol + "]";
	}
	
	
}
