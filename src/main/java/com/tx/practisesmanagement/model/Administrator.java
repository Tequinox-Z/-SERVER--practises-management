package com.tx.practisesmanagement.model;




import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.enumerators.Rol;

/**
 * Clase del usuario administrador
 * @author Salvador
 */
@Entity
public class Administrator extends Teacher {

	@ManyToOne
	@JsonIgnore
	private School schoolSetted;						// Escuela que administra
	
	/**
	 * Constructor sin parámetros
	 */
	public Administrator() {
		super();
		this.rol = Rol.ROLE_ADMIN;						// Asignamos el rol de admin	
	}

	/**
	 * Constructor desde un dto de persona
	 * @param person: Persona con los datos
	 */
	public Administrator(PersonDTO person) {
		super();
		
		// Asignamos todos los datos
		
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
		
		this.rol = Rol.ROLE_ADMIN;	// Asignamos el rol
	}
	
	/**
	 * Constructor con todos los datos
	 * @param dni: DNi
	 * @param address: Dirección
	 * @param birthDate: Fecha de nacimiento
	 * @param image: Imágen
	 * @param name: Nombre
	 * @param lastName: Apellidos
	 * @param password: Contraseña
	 * @param telefone: Teléfono
	 * @param email: Correo
	 */
	public Administrator(String dni, String address, Date birthDate, String image, String name, String lastName, String password, String telefone, String email, boolean enabled) {
		super();
		
		// Asignamos todos los datos
		
		this.dni = dni;
		this.address = address;
		this.birthDate = birthDate; 
		this.image = image; 
		this.name = name;
		this.lastName = lastName;
		this.password = password;
		this.telefone = telefone; 
		this.email = email;
		this.enabled = enabled;
		
		this.rol = Rol.ROLE_ADMIN;		// Asignamos el rol
	}

	/**
	 * Obtiene la escuela que administra
	 * @return: Escuela que administra
	 */
	public School getSchoolSetted() {
		return schoolSetted;
	}


	/**
	 * Establece una nueva escuela a administrar
	 * @param schoolSetted: Nueva escuela
	 */
	public void setSchoolSetted(School schoolSetted) {
		this.schoolSetted = schoolSetted;
	}

	/**
	 * Muestra información del administrador
	 */
	@Override
	public String toString() {
		return "Administrator [school=" + schoolSetted + ", getBirthDate()=" + getBirthDate() + ", getName()=" + getName()
				+ ", getLastName()=" + getLastName() + ", getImage()=" + getImage() + ", getTelefone()=" + getTelefone()
				+ ", getAddress()=" + getAddress() + ", getPassword()=" + getPassword() + ", getDni()=" + getDni()
				+ "]";
	}
	
	/**
	 * HashCode del administrador
	 */
	@Override
	public int hashCode() {
		return Objects.hash(dni);
	}

	/**
	 * Equals del administrador
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
	
}
