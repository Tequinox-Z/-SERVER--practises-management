package com.tx.practisesmanagement.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO de localización con datos del centro
 * @author Salvador
 */
public class LocationAndSchoolDTO implements Serializable {

	private static final long serialVersionUID = 8959541724470096354L;
	private Double latitude;											// Latitud de la ubicación
	private Double longitude;											// Longitud de la ubicación
	private Integer id;													// Identificador
	private String name;												// Nombre

	public LocationAndSchoolDTO() {
		super();
	}
	public LocationAndSchoolDTO(Double latitude, Double longitude, Integer id, String name) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		this.name = name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(id, latitude, longitude, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationAndSchoolDTO other = (LocationAndSchoolDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(latitude, other.latitude)
				&& Objects.equals(longitude, other.longitude) && Objects.equals(name, other.name);
	}
	
	

	@Override
	public String toString() {
		return "LocationAndSchoolDTO [latitude=" + latitude + ", longitude=" + longitude + ", id=" + id + ", name="
				+ name + "]";
	}
}
