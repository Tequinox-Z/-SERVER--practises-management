package com.tx.practisesmanagement.model;

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
	private Double celcius;
	private Double fahrenheit;
	private Double heatIndexc;
	private Double heatIndexf;
	
	public RegTemp() {
		super();
	}

	public RegTemp(Double celcius, Double fahrenheit, Double heatIndexc, Double heatIndexf) {
		super();
		this.celcius = celcius;
		this.fahrenheit = fahrenheit;
		this.heatIndexc = heatIndexc;
		this.heatIndexf = heatIndexf;
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
				+ heatIndexc + ", heatIndexf=" + heatIndexf + "]";
	}
	
	
}
