package com.tx.practisesmanagement.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * Indica una preferencia de un alumno sobre una empresa
 * @author Salvador
 */
public class NewPreferenceDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String cif;														// Cif de la empresa				
	private Integer position;												// Posición
	
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


	/**
	 * Constructor vacío
	 */
	public NewPreferenceDTO() {
		super();
	}


	/**
	 * Constructor con parámetros
	 * @param cif: Cif de la empresa
	 * @param position: Posición de la empresa
	 */
	public NewPreferenceDTO(String cif, Integer position) {
		super();
		this.cif = cif;
		this.position = position;
	}


	/**
	 * Obtiene el cif de la empresa
	 * @return Cif de la empresa
	 */
	public String getCif() {
		return cif;
	}


	/**
	 * Establece el cif de la empresa
	 * @param cif: Nuevo cif
	 */
	public void setCif(String cif) {
		this.cif = cif;
	}


	/**
	 * Obtiene la posición
	 * @return: Posición de la empresa
	 */
	public Integer getPosition() {
		return position;
	}


	/**
	 * Establece la posición de la empresa
	 * @param position: Nueva posición
	 */
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
