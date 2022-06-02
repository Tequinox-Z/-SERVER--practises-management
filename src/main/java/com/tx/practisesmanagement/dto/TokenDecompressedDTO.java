package com.tx.practisesmanagement.dto;

import java.util.Objects;

/**
 * Almacena los datos de un token descomprimido
 * @author Salvador
 */
public class TokenDecompressedDTO {

	private String sub;													// Sub
	private String iss;													// Iss
	private String exp;													// Expiración
	private String iat;													// Iat
	private String username;											// Usuario
	
	
	/**
	 * Constructor sin parámetros
	 */
	public TokenDecompressedDTO() {
		super();
	}

	/**
	 * Constructor con parámetros
	 * @param sub: Sub
	 * @param iss: Iss
	 * @param exp: Expiración
	 * @param iat: Iat
	 * @param username: Usuario
	 */
	public TokenDecompressedDTO(String sub, String iss, String exp, String iat, String username) {
		super();
		this.sub = sub;
		this.iss = iss;
		this.exp = exp;
		this.iat = iat;
		this.username = username;
	}

	/**
	 * Obtiene el sub
	 * @return: sub
	 */
	public String getSub() {
		return sub;
	}

	/**
	 * Establece el sub
	 * @param sub: Nuevo sub
	 */
	public void setSub(String sub) {
		this.sub = sub;
	}

	/**
	 * Obtiene el iss
	 * @return: Iss
	 */
	public String getIss() {
		return iss;
	}

	/**
	 * Establece el iss
	 * @param iss: Nuevo iss
	 */
	public void setIss(String iss) {
		this.iss = iss;
	}

	/**
	 * Obtiene la expiración
	 * @return: Expiración actual
	 */
	public String getExp() {
		return exp;
	}

	/**
	 * Establece la expiración
	 * @param exp: Nueva expiración
	 */
	public void setExp(String exp) {
		this.exp = exp;
	}

	/**
	 * Obtiene el iat
	 * @return: Iat
	 */
	public String getIat() {
		return iat;
	}

	/**
	 * Establece el Iat
	 * @param iat: Nuevo iat
	 */
	public void setIat(String iat) {
		this.iat = iat;
	}

	/**
	 * Obtiene el usuario
	 * @return: Usuario actual
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Establece el usuario
	 * @param username: Nuevo usuario
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenDecompressedDTO other = (TokenDecompressedDTO) obj;
		return Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "TokenDecompressedDTO [sub=" + sub + ", iss=" + iss + ", exp=" + exp + ", iat=" + iat + ", username="
				+ username + "]";
	}
	
}
