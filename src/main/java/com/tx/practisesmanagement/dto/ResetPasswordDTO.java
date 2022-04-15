package com.tx.practisesmanagement.dto;



import java.io.Serializable;
import java.util.Objects;


/**
 * DTO de reseteo de contraseña, transporta el dni del usuario para resetear la contraseña
 * @author Salva
 */
public class ResetPasswordDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String dni;									// Dni del usuario

	/**
	 * Constructor sin parámetros
	 */
	public ResetPasswordDTO() {
		super();
	}

	/**
	 * Constructos con dni
	 * @param email
	 */
	public ResetPasswordDTO(String dni) {
		super();
		this.dni = dni;
	}

	/**
	 * Obtiene el dni del usuario
	 * @return Dni del usuario
	 */
	public String getDni() {
		return dni;
	}

	/**
	 * Establece el dni del usuario
	 * @param dni: Nuevo dni
	 */
	public void setDni(String dni) {
		this.dni = dni;
	}

	/**
	 * Hashcode del usuario
	 */
	@Override
	public int hashCode() {
		return Objects.hash(dni);
	}

	/**
	 * Equals del usuario
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResetPasswordDTO other = (ResetPasswordDTO) obj;
		return Objects.equals(dni, other.dni);
	}

	/**
	 * Muestra información del usuario
	 */
	@Override
	public String toString() {
		return "ResetPasswordDTO [dni=" + dni + "]";
	}
	
}
