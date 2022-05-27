package com.tx.practisesmanagement.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Preference {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer position;
	
	@ManyToOne
	@JsonIgnore
	private Enrollment enrollment;
	
	@ManyToOne
	@JsonIgnore
	private Business business;
	
	public Preference() {
		super();
	}
	
	public Preference(Integer id) {
		super();
		this.id = id;
	}
	
	public Preference(Integer id, Integer position) {
		super();
		this.id = id;
		this.position = position;
	}
	
	public Preference(Integer position, Business business, Enrollment enrollment) {
		super();
		this.position = position;
		this.business = business;
		this.enrollment = enrollment;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public Enrollment getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
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
		Preference other = (Preference) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Preference [id=" + id + ", position=" + position + "]";
	}
	
	
	
	
	
}
