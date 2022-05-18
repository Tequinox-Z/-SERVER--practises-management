package com.tx.practisesmanagement.dto;

import java.util.Objects;

public class TokenDecompressedDTO {

	private String sub;
	private String iss;
	private String exp;
	private String iat;
	private String username;
	
	public TokenDecompressedDTO() {
		super();
	}

	public TokenDecompressedDTO(String sub, String iss, String exp, String iat, String username) {
		super();
		this.sub = sub;
		this.iss = iss;
		this.exp = exp;
		this.iat = iat;
		this.username = username;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getIss() {
		return iss;
	}

	public void setIss(String iss) {
		this.iss = iss;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getIat() {
		return iat;
	}

	public void setIat(String iat) {
		this.iat = iat;
	}

	public String getUsername() {
		return username;
	}

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
