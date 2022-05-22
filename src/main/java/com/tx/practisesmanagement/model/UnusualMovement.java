package com.tx.practisesmanagement.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UnusualMovement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private LocalDateTime date;
	
	public UnusualMovement() {
		super();
	}

	public UnusualMovement(Integer id, LocalDateTime date) {
		super();
		this.id = id;
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
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
		UnusualMovement other = (UnusualMovement) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "UnusualMovement [id=" + id + ", date=" + date + "]";
	}
	
	
}
