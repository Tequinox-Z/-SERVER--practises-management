package com.tx.practisesmanagement.model;



import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Clase de escuela
 * @author Salva
 */
@Entity
public class School {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;										// Identificador
	private String name;									// Nombre
	private String address;									// Dirección
	private String image;									// Imagen
	private String password;								// Contraseña

	@JsonIgnore
	@OneToMany(mappedBy = "school")
	private List<ProfessionalDegree> professionalDegrees;	// Ciclos

	@JsonIgnore
	@OneToMany(mappedBy = "schoolSetted")
	private List<Administrator> administrators;				// Administradores
	
	/**
	 * Constructor sin parámetros
	 */
	public School() {
		super();
	}

	/**
	 * Constructor con nombre, dirección y contraseña
	 * @param name: Nombre
	 * @param address: Dirección
	 * @param password: Contraseña
	 */
	public School(String name, String address, String password) {
		super();
		this.password = password;
		this.name = name;
		this.address = address;
	}
	
	public School(String name, String address, String image, String password) {
		super();
		this.name = name;
		this.address = address;
		this.password = password;
		this.image = image;
	}


	/**
	 * Constructor con todos los parámetros
	 * @param name: Nombre
	 * @param address: Dirección
	 * @param professionalDegrees: Ciclos
	 * @param administrators: Administradores
	 * @param password: Contraseña
	 */
	public School(String name, String address, List<ProfessionalDegree> professionalDegrees, List<Administrator> administrators,  String password) {
		super();
		this.password = password;
		this.name = name;
		this.address = address;
		this.professionalDegrees = professionalDegrees;
		this.administrators = administrators;
	}

	/**
	 * Obtiene el id
	 * @return: Identificador
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Establece el id
	 * @param id: Nuevo id
	 */
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
	 * Obtiene la dirección
	 * @return: Dirección
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
	 * Obtiene los ciclos
	 * @return: Ciclos
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
	 * Obtiene la imagen
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
	 * Obtiene la contraseña
	 * @return: Contraseña
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Establece la contraseña
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Obtiene los administradores
	 * @return: Administradores
	 */
	public List<Administrator> getAdministrators() {
		return administrators;
	}

	/**
	 * Establece los administradores del centro
	 * @param administrators: Nuevos administradores
	 */
	public void setAdministrators(List<Administrator> administrators) {
		this.administrators = administrators;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Hashcode de colegio
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		School other = (School) obj;
		return Objects.equals(id, other.id);
	}
	
	
	/**
	 * Muestra información del colegio
	 */
	@Override
	public String toString() {
		return "School [id=" + id + ", name=" + name + ", address=" + address + ", professionalDegrees="
				+ professionalDegrees + "]";
	}
	
}
