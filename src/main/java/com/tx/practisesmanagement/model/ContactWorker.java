package com.tx.practisesmanagement.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Trabajador de contacto de una empresa
 * @author Salvador
 */
@Entity
public class ContactWorker {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;																	// Id
	private String email;																// Nombre
	private String telefone;															// Teléfono
	private String name;																// Nombre
	
	/**
	 * Constructor con parámetros
	 * @param email Correo
	 * @param telefone Teléfono
	 * @param name Nombre
	 */
	public ContactWorker(String email, String telefone, String name) {
		super();
		this.email = email;
		this.telefone = telefone;
		this.name = name;
	}

	/**
	 * Constructor sin parámetros
	 */
	public ContactWorker() {
		super();
	}

	/**
	 * Constructor con id
	 * @param id Id del trabajador de contacto
	 */
	public ContactWorker(Integer id) {
		super();
		this.id = id;
	}
	
	/**
	 * Obtiene el id
	 * @return Id del trabajador
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
	 * Obtiene el correo
	 * @return: Correo electrónico
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Establece un nuevo correo
	 * @param email: Nuevo correo
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Obtiene el teléfono
	 * @return Teléfono del trabajador
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * Establece un nuevo teléfono
	 * @param telefone: Nuevo teléfono
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/**
	 * Obtiene el nombre del trabajador
	 * @return: Nombre del trabajador
	 */
	public String getName() {
		return name;
	}

	/**
	 * Establece un nuevo nombre
	 * @param name: Nuevo nombre del trabajador
	 */
	public void setName(String name) {
		this.name = name;
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
		ContactWorker other = (ContactWorker) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ContactWorker [id=" + id + ", email=" + email + ", telefone=" + telefone + ", name=" + name + "]";
	}
	
	
	
	
}
