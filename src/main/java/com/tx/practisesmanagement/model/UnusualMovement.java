package com.tx.practisesmanagement.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * Clase de movimiento inusual
 * @author Salvador
 */
@Entity
public class UnusualMovement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;													// Identificador

	@DateTimeFormat
	private LocalDateTime date;											// Fecha del registro
	
	/**
	 * Constructor sin parámetros
	 */
	public UnusualMovement() {
		super();
	}

	/**
	 * Constructor con parámetros
	 * @param id Identificador
	 * @param date Fecha
	 */
	public UnusualMovement(Integer id, LocalDateTime date) {
		super();
		this.id = id;
		this.date = date;
	}

	/**
	 * Obtiene el identificador
	 * @return Identificador
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Establece el id
	 * @param id Identificador
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene la fecha
	 * @return Fecha
	 */ 
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Establece la fecha
	 * @param date Fecha establecida
	 */
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
