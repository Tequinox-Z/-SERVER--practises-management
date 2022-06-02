package com.tx.practisesmanagement.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * Clase de registro de temperatura
 * @author Salvador
 */
@Entity
public class RegTemp {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;														// Identificador
	private Double humidity;												// Humedad
	private Double celcius;													// Temperatura en celcius	
	private Double fahrenheit;												// Temperatura en fahrenheit
	private Double heatIndexc;												// Indice de calor en celcius
	private Double heatIndexf;												// Indice de calor en fahrenheit
	private LocalDateTime date;												// Fecha del registro
	
	/**
	 * Constructor sin parámetros
	 */
	public RegTemp() {
		super();
		this.date = LocalDateTime.now();
	}

	/**
	 * Constructor con parámetros
	 * @param celcius Celcius
	 * @param fahrenheit Fahrenheit
	 * @param heatIndexc Índice de temperatura en celcius
	 * @param heatIndexf Índice de temperatura en fahrenheit
	 * @param humidity Humedad
	 */
	public RegTemp(Double celcius, Double fahrenheit, Double heatIndexc, Double heatIndexf, Double humidity) {
		super();
		this.humidity = humidity;
		this.celcius = celcius;
		this.fahrenheit = fahrenheit;
		this.heatIndexc = heatIndexc;
		this.heatIndexf = heatIndexf;
		this.date = LocalDateTime.now();
	}

	/**
	 * Obtiene la fecha del registro
	 * @return Fecha del registro
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Establece la fecha del registro
	 * @param date Nueva fecha de registro
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * Obtiene el identificador
	 * @return Identificador del regisro
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Establece el identificador del registro
	 * @param id: Nuevo identificador
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene la temperatura en celcius
	 * @return Temperatura en celcius
	 */
	public Double getCelcius() {
		return celcius;
	}

	/**
	 * Establece la temperatura en celcius
	 * @param celcius Nueva temperatura en celcius
	 */
	public void setCelcius(Double celcius) {
		this.celcius = celcius;
	}

	/**
	 * Obtiene la temperatura en fahrenheit
	 * @return Temperatura en fahrenheit
	 */
	public Double getFahrenheit() {
		return fahrenheit;
	}

	/**
	 * Establece la temperatura en fahrenheit
	 * @param fahrenheit Nueva temperatura en fahrenheit
	 */
	public void setFahrenheit(Double fahrenheit) {
		this.fahrenheit = fahrenheit;
	}
	
	/**
	 * Obtiene la humedad
	 * @return Humedad del registro
	 */
	public Double getHumidity() {
		return humidity;
	}

	/**
	 * Establece la humedad del registro
	 * @param humidity Nueva humedad
	 */
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	/**
	 * Obtiene el índice de calor en celcius
	 * @return Índice de calor en celcius
	 */
	public Double getHeatIndexc() {
		return heatIndexc;
	}

	/**
	 * Establece el índice de calor en celcius
	 * @param heatIndexc Nuevo índice de calor en celcius
	 */
	public void setHeatIndexc(Double heatIndexc) {
		this.heatIndexc = heatIndexc;
	}

	/**
	 * Obtiene el índice de calor en fahrenheit
	 * @return Índice de calor en fahrenheit
	 */
	public Double getHeatIndexf() {
		return heatIndexf;
	}

	/**
	 * Establece el índice de calor en fahrenheit
	 * @param heatIndexf: Índice de calor en fahrenheit
	 */
	public void setHeatIndexf(Double heatIndexf) {
		this.heatIndexf = heatIndexf;
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
		RegTemp other = (RegTemp) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "RegTemp [id=" + id + ", celcius=" + celcius + ", fahrenheit=" + fahrenheit + ", heatIndexc="
				+ heatIndexc + ", heatIndexf=" + heatIndexf + ", humidity=" + humidity + "]";
	}
	
	
}
