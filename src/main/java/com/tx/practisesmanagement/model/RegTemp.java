package com.tx.practisesmanagement.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegTemp {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Double humidity;
	private Double celcius;
	private Double fahrenheit;
	private Double heatIndexc;
	private Double heatIndexf;
	private LocalDateTime date;
	
	public RegTemp() {
		super();
		this.date = LocalDateTime.now();
	}

	public RegTemp(Double celcius, Double fahrenheit, Double heatIndexc, Double heatIndexf, Double humidity) {
		super();
		this.humidity = humidity;
		this.celcius = celcius;
		this.fahrenheit = fahrenheit;
		this.heatIndexc = heatIndexc;
		this.heatIndexf = heatIndexf;
		this.date = LocalDateTime.now();
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getCelcius() {
		return celcius;
	}

	public void setCelcius(Double celcius) {
		this.celcius = celcius;
	}

	public Double getFahrenheit() {
		return fahrenheit;
	}

	public void setFahrenheit(Double fahrenheit) {
		this.fahrenheit = fahrenheit;
	}
	
	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public Double getHeatIndexc() {
		return heatIndexc;
	}

	public void setHeatIndexc(Double heatIndexc) {
		this.heatIndexc = heatIndexc;
	}

	public Double getHeatIndexf() {
		return heatIndexf;
	}

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
