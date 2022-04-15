package com.tx.practisesmanagement.dto;



import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Transporta el token del usuario
 * @author Salva
 */
public class TokenDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String token;									// Token del usuario
	
	/**
	 * Constructor sin parámetros
	 */
	public TokenDTO() {
		super();
	}

	/**
	 * Obtiene el token
	 * @return Token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Establece el token
	 * @param token: Nuevo token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Hashcode del usuario
	 */
	@Override
	public int hashCode() {
		return Objects.hash(token);
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
		TokenDTO other = (TokenDTO) obj;
		return Objects.equals(token, other.token);
	}

	/***
	 * Muestra información del usuario
	 */
	@Override
	public String toString() {
		return "TokenDTO [token=" + token + "]";
	}
}