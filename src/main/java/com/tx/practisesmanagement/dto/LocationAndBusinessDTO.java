package com.tx.practisesmanagement.dto;

import java.util.Objects;

/**
 * Dto de empresa y localización
 * @author Salvador Pérez
 *
 */
public class LocationAndBusinessDTO {
	private String cif;								// Cif de la empresa
	private String name;							// Nombre de la empresa
	private Integer numberOfStudents;				// Número de estudiantes	
	private String image;							// Imagen de la empresa
	
	private Double longitude;						// Longitud
	private Double latitude;						// Latitud
	
	public LocationAndBusinessDTO() {
		super();
	}
	
	public LocationAndBusinessDTO(String cif, String name, Integer numberOfStudents, String image, Double longitude,
			Double latitude) {
		super();
		this.cif = cif;
		this.name = name;
		this.numberOfStudents = numberOfStudents;
		this.image = image;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNumberOfStudents() {
		return numberOfStudents;
	}
	public void setNumberOfStudents(Integer numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cif, image, latitude, longitude, name, numberOfStudents);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationAndBusinessDTO other = (LocationAndBusinessDTO) obj;
		return Objects.equals(cif, other.cif) && Objects.equals(image, other.image)
				&& Objects.equals(latitude, other.latitude) && Objects.equals(longitude, other.longitude)
				&& Objects.equals(name, other.name) && Objects.equals(numberOfStudents, other.numberOfStudents);
	}
	@Override
	public String toString() {
		return "LocationAndBusinessDTO [cif=" + cif + ", name=" + name + ", numberOfStudents=" + numberOfStudents
				+ ", image=" + image + ", longitude=" + longitude + ", latitude=" + latitude + "]";
	}
	
}
