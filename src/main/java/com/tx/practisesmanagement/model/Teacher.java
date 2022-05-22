package com.tx.practisesmanagement.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.enumerators.Rol;

/**
 * Clase profesor
 * @author Salva
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Teacher extends Person {
	
	@ManyToMany
	@JsonIgnore
	protected List<ProfessionalDegree> professionalDegrees;		// Ciclos
	
	@OneToMany(mappedBy = "teacher")
	@JsonIgnore
	private List<Practise> practises;
	
	/**
	 * Constructor sin parámetros
	 */
	public Teacher() {
		super();		
		this.rol = Rol.ROLE_TEACHER;							// Asignamos el rol
	}
	
	/**
	 * Constructor desde persona
	 * @param person
	 */
	public Teacher(PersonDTO person) {
		super();
		// Asignamos los datos
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
		
		this.rol = Rol.ROLE_TEACHER;						// Asignamos el rol
	}
	
	
	
	/**
	 * Constructor con todos los datos
	 * @param dni: DNi
	 * @param address: Dirección 
	 * @param birthDate: Fecha de nacimiento
	 * @param image: Imagen
	 * @param name: Nombre
	 * @param lastName: Apellidos
	 * @param password: Contraseña
	 * @param telefone: Teléfono
	 * @param email: Correo
	 */
	public Teacher(String dni, String address, Date birthDate, String image, String name, String lastName, String password, String telefone, String email) {
		super();
		
		// Establecemos los datos
		this.dni = dni;
		this.address = address;
		this.birthDate = birthDate;
		this.image = image;
		this.name = name;
		this.lastName = lastName;
		this.password = password;
		this.telefone = telefone;
		this.email = email;
		
		this.rol = Rol.ROLE_TEACHER;		// Asingamos el rol
	}
	
	
	
	public List<Practise> getPractises() {
		return practises;
	}

	public void setPractises(List<Practise> practises) {
		this.practises = practises;
	}

	/**
	 * Obtiene los ciclos
	 * @return Ciclos
	 */
	public List<ProfessionalDegree> getProfessionalDegrees() {
		return professionalDegrees;
	}

	/**
	 * Establece los ciclos
	 * @param professionalDegrees: Nuevos ciclos
	 */
	public void setProfessionalDegrees(List<ProfessionalDegree> professionalDegrees) {
		this.professionalDegrees = professionalDegrees;
	}
	
	
	/**
	 * Hashcode de profesor
	 */
	@Override
	public int hashCode() {
		return Objects.hash(dni);
	}

	/**
	 * Equals de profesor
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
	 * Muestra información del profesor
	 */
	@Override
	public String toString() {
		return "Teacher [professionalDegrees=" + professionalDegrees + ", toString()=" + super.toString()
				+ ", getBirthDate()=" + getBirthDate() + ", getName()=" + getName() + ", getLastName()=" + getLastName()
				+ ", getImage()=" + getImage() + ", getTelefone()=" + getTelefone() + ", getAddress()=" + getAddress()
				+ ", getPassword()=" + getPassword() + ", getDni()=" + getDni() + ", getRol()=" + getRol() + "]";
	}


}
