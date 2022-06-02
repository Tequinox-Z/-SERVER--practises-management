package com.tx.practisesmanagement.model;



import java.util.Date;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tx.practisesmanagement.enumerators.Rol;


/**
 * Clase persona o usuario
 * @author Salvador
 */
@MappedSuperclass
public class Person {
	
	@Id
	protected String dni;							// Dni de la persona
	
	@Temporal(TemporalType.TIMESTAMP)
	protected Date birthDate;						// Fecha de nacimiento
	protected String name;							// Nombre 
	protected String lastName;						// Apellidos
	protected String image;							// Imagen
	protected String telefone;						// Teléfono
	protected String address;						// Dirección
	protected String email;							// Correo
	protected boolean enabled;
	
	@JsonIgnore
	protected String password;						// Contraseña
	protected Rol rol;								// Rol asignado

	/**
	 * Constructor con todos los parámetros
	 * @param dni: DNi
	 * @param birthDate: Fecha de nacimiento
	 * @param name: Nombre
	 * @param lastName: Apellidos
	 * @param image: Imágen
	 * @param telefone: Teléfono
	 * @param address: Dirección
	 * @param password: Contraseña
	 * @param email: Correo
	 */
	public Person(String dni, Date birthDate, String name, String lastName, String image, String telefone,
			String address, String password, String email) {
		
			super();
			
		// Establecemos todos los datos
		
			this.dni = dni;
			this.birthDate = birthDate;
			this.name = name;
			this.lastName = lastName;
			this.image = image;
			this.telefone = telefone;
			this.address = address;
			this.password = password;
			this.email = email;
			this.enabled = true;
	}

	/**
	 * Constructor sin parámetros
	 */
	
	public Person() {
		super();
		this.enabled = true;
	}

	/**
	 * Constructor con dni
	 * @param dni
	 */
	
	public Person(String dni) {
		super();
		this.dni = dni;
		this.enabled = true;
	}
	
	/**
	 * Constructor con dni y contraseña
	 * @param dni
	 * @param password
	 */
	public Person(String dni, String password) {
		super();
		this.dni = dni;
		this.password = password;
		this.enabled = true;
	}

	/**
	 * HashCode de persona
	 */
	@Override
	public int hashCode() {
		return Objects.hash(dni);
	}

	/**
	 * Equals de persona
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
	 * Muestra información de la persona
	 */
	@Override
	public String toString() {
		return "Person [dni=" + dni + ", birthDate=" + birthDate + ", name=" + name + ", lastName=" + lastName
				+ ", image=" + image + ", telefone=" + telefone + ", address=" + address + ", password=" + password
				+ "]";
	}

	/**
	 * Obtiene la fecha de nacimiento de la persona
	 * @return Fecha de nacimiento
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	
	/**
	 * Establece la fecha de nacimiento
	 * @param birthDate: Nueva fecha de nacimiento
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Obtiene el nombre
	 * @return: Nombre de la persona
	 */
	public String getName() {
		return name;
	}

	/**
	 * Establece el nombre
	 * @param name: Nuevo nombre de la persona
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtiene los apellidos de la persona
	 * @return: Apellidos
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Establece los apellidos de la persona
	 * @param lastName: Nuevos apellidos
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Obtiene la imagen del usuario
	 * @return: Imagen
	 */
	public String getImage() {
		return image;
	}
	
	

	/**
	 * Establece la imagen 
	 * @param image: Nueva imagen
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Obtiene el teléfono
	 * @return: Teléfono
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * Establece el teléfono
	 * @param telefone: Nuevo teléfono
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/**
	 * Obtiene la direccíon del usuario
	 * @return: Dirección del usuario
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Establece la dirección
	 * @param address: Nueva dirección
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Obtiene la contraseña
	 * @return: Contraseña
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Establece la contraseña
	 * @param password: Nueva contraseña
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Obtiene el dni
	 * @return: DNi
	 */
	public String getDni() {
		return dni;
	}

	/**
	 * Establece el dni
	 * @param dni: Nuevo dni
	 */
	public void setDni(String dni) {
		this.dni = dni;
	}

	/**
	 * Obtiene el rol
	 * @return: Rol
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * Establece el rol
	 * @param rol: Nuevo rol
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}

	/**
	 * Obtiene el correo
	 * @return: Correo
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Establece el correo
	 * @param email: Nuevo correo
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Indica si está habilitado o no
	 * @return Boolean si está habilitado o no
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Establece si el usuario estará habilitado o no
	 * @param enabled ¿Habilitado?
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
}
