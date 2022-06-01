package com.tx.practisesmanagement.dto;

import java.io.Serializable;
import java.util.Objects;

public class NewPreferenceDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String cif;
	private Integer position;
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(cif, position);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewPreferenceDTO other = (NewPreferenceDTO) obj;
		return Objects.equals(cif, other.cif) && Objects.equals(position, other.position);
	}


	public NewPreferenceDTO() {
		super();
	}



	public NewPreferenceDTO(String cif, Integer position) {
		super();
		this.cif = cif;
		this.position = position;
	}



	public String getCif() {
		return cif;
	}



	public void setCif(String cif) {
		this.cif = cif;
	}



	public Integer getPosition() {
		return position;
	}



	public void setPosition(Integer position) {
		this.position = position;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "NewPreferenceDTO [cif=" + cif + ", position=" + position + "]";
	}
	
}
