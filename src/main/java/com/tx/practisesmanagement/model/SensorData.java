package com.tx.practisesmanagement.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SensorData {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private Double humidity;
	private Double tempCelcius;
	private Double tempFahrenheit;
	private Double heatIndexFahrenheit;
	private Double heatIndexCelsius;
	private Integer pin;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Double getHumidity() {
		return humidity;
	}
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
	public Double getTempCelcius() {
		return tempCelcius;
	}
	public void setTempCelcius(Double tempCelcius) {
		this.tempCelcius = tempCelcius;
	}
	public Double getTempFahrenheit() {
		return tempFahrenheit;
	}
	public void setTempFahrenheit(Double tempFahrenheit) {
		this.tempFahrenheit = tempFahrenheit;
	}
	public Double getHeatIndexFahrenheit() {
		return heatIndexFahrenheit;
	}
	public void setHeatIndexFahrenheit(Double heatIndexFahrenheit) {
		this.heatIndexFahrenheit = heatIndexFahrenheit;
	}
	public Double getHeatIndexCelsius() {
		return heatIndexCelsius;
	}
	public void setHeatIndexCelsius(Double heatIndexCelsius) {
		this.heatIndexCelsius = heatIndexCelsius;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
	}
	@Override
	public String toString() {
		return "SensorData [humidity=" + humidity + ", tempCelcius=" + tempCelcius + ", tempFahrenheit="
				+ tempFahrenheit + ", heatIndexFahrenheit=" + heatIndexFahrenheit + ", heatIndexCelsius="
				+ heatIndexCelsius + ", pin=" + pin + "]";
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
		SensorData other = (SensorData) obj;
		return id == other.id;
	}
	
}
