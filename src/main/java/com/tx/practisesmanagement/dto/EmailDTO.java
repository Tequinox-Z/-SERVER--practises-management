package com.tx.practisesmanagement.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * DTO de Email, permite alamcenar un correo electrónico
 * @author Salvador
 */
public class EmailDTO implements Serializable {

	private static final long serialVersionUID = 3979656155218166710L;
	private String email;														// Email
	
	/**
	 * Constructor con email
	 * @param email
	 */
	public EmailDTO(String email) {
		super();
		this.email = email;
	}
	
	/**
	 * Constructor sin parámetros
	 */
	public EmailDTO() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailDTO other = (EmailDTO) obj;
		return Objects.equals(email, other.email);
	}

	
	/**
	 * Obtiene el correo
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Establece el correo
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "EmailDTO [email=" + email + "]";
	}	
	
	
}
