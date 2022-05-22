package com.tx.practisesmanagement.dto;

import java.io.Serializable;
import java.util.Objects;

public class EmailDTO implements Serializable {

	private static final long serialVersionUID = 3979656155218166710L;
	private String email;
	
	public EmailDTO(String email) {
		super();
		this.email = email;
	}
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "EmailDTO [email=" + email + "]";
	}
	
	
	
	
	
	
	
}
