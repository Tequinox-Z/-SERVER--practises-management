package com.tx.practisesmanagement.dto;



import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tx.practisesmanagement.enumerators.Rol;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.Student;
import com.tx.practisesmanagement.model.Teacher;

/**
 * DTO de persona, permite transmitir información de personas
 * @author Salva
 */
public class PersonDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String dni;												// DNI del usuario
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private  Date  birthDate;										// Fecha del usuario
	private String name;											// Nombre del usuario
	private String lastName;										// Apellidos del usuario
	private String image;											// Imagen del usuario
	private String telefone;										// Teléfono del usuario
	private String address;											// Dirección del usuario
	private String password;										// Contraseña del usuario
	private String email;											// Correo del usuario
	private Rol rol;												// Rol del usuario
	private boolean enabled;
	
	/**
	 * Constructor vacío de persona
	 */
	public PersonDTO() {
		super();
	}
	
	public PersonDTO(String dni, String email) {
		this.dni = dni;
		this.email = email;
	}
	
	/**
	 * Constructor desde un administrador
	 * @param administrator: Administrador
	 */
	public PersonDTO(Administrator administrator) {
		super();
		
		// Asignamos todos los campos 
		
		this.dni = administrator.getDni();
		this.birthDate = administrator.getBirthDate();
		this.name = administrator.getName();
		this.lastName = administrator.getLastName();
		this.image = administrator.getImage();
		this.telefone = administrator.getTelefone();
		this.address = administrator.getAddress();
		this.rol = administrator.getRol();
		this.email = administrator.getEmail();
		this.password = administrator.getPassword();
		this.enabled = administrator.isEnabled();
	}
	
	/**
	 * Constructor desde estudiante
	 * @param student: Estudiante
	 */
	public PersonDTO(Student student) {
		super();
		
		// Asignamos todos los campos
		
		this.dni = student.getDni();
		this.birthDate = student.getBirthDate();
		this.name = student.getName();
		this.lastName = student.getLastName();
		this.image = student.getImage();
		this.telefone = student.getTelefone();
		this.address = student.getAddress();
		this.rol = student.getRol();
		this.email = student.getEmail();
		this.password = student.getPassword();
		this.enabled = student.isEnabled();
	}
	
	/**
	 * Constructor de person desde profesor
	 * @param teacher: Profesor
	 */
	public PersonDTO(Teacher teacher) {
		super();
		
		// Asignamos todos los campos
		
		this.dni = teacher.getDni();
		this.birthDate = teacher.getBirthDate();
		this.name = teacher.getName();
		this.lastName = teacher.getLastName();
		this.image = teacher.getImage();
		this.telefone = teacher.getTelefone();
		this.address = teacher.getAddress();
		this.rol = teacher.getRol();
		this.email = teacher.getEmail();
		this.password = teacher.getPassword();
		this.enabled = teacher.isEnabled();
	}

	/**
	 * Obtiene el dni
	 * @return Dni
	 */
	public String getDni() {
		return dni;
	}
	/**
	 * Establece el dni
	 *@param dni: Nuevo dni
	 */
	public void setDni(String dni) {
		this.dni = dni;
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
	 * @param birthDate: Nueva fecha
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Obtiene el nombre
	 * @return Nombre
	 */
	public String getName() {
		return name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Establece el nombre
	 * @param name: Nuevo nombre
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtiene el apellido
	 * @return Apellido
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Establce los apellidos 
	 * @param lastName: Nuevos apellidos
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Obtiene la imagen
	 * @return imagen
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
	 * @return Teléfono
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * Establece el teléfono
	 * @param telefone: Teléfono
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/**
	 * Obtiene la dirección del usuario
	 * @return Dirección del usuario
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Establece la dirección del usuario
	 * @param address: Nueva dirección del usuari
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Obtiene la contraseña del usuario
	 * @return Contraseña del usuario
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Establece la contraseña del usuario
	 * @param password: Nueva contraseña
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Obtiene el correo del usuario
	 * @return Correo del usuario
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Establece el correo del usuari
	 * @param email: Nuevo correo
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	/**
	 * Información de la persona
	 */
	@Override
	public String toString() {
		return "PersonDTO [dni=" + dni + ", birthDate=" + birthDate + ", name=" + name + ", lastName=" + lastName
				+ ", image=" + image + ", telefone=" + telefone + ", address=" + address + ", password=" + password
				+ ", rol=" + rol + "]";
	}

	/**
	 * HashCode de la persona
	 */
	@Override
	public int hashCode() {
		return Objects.hash(dni);
	}

	/**
	 * Equals de la persona
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonDTO other = (PersonDTO) obj;
		return Objects.equals(dni, other.dni);
	}
}
