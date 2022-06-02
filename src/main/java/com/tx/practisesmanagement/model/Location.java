package com.tx.practisesmanagement.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Clase de localización 
 * @author Salvador
 */
@Entity
public class Location {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;													// Identificador
	private Double latitude;											// Latitud de la ubicación
	private Double longitude;											// Longitud de la ubicación
	
	/**
	 * Constructor sin parámetros
	 */
	public Location() {
		super();
	}
	
	/**
	 * Constructor con identificador
	 * @param id Identificador
	 */
	public Location(Integer id) {
		this.id = id;
	}

	/**
	 * Constructor con parámetros
	 * @param latitude Latitud
	 * @param longitude Longitud
	 */
	public Location(Double latitude, Double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Obtiene el identificador
	 * @return Identificador de la longitud
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Establece un identificador
	 * @param id Identificador de la localización
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene la latitud de la localización
	 * @return: Latitud de la localización
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Establece una nueva latitud
	 * @param latitude Nueva latitud
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Obtiene la longitud
	 * @return Longitud
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Establece una nueva longitud
	 * @param longitude: Nueva longitud
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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
		Location other = (Location) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
}
