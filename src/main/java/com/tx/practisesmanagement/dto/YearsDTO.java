package com.tx.practisesmanagement.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Dto con los distintos años de un centro
 * @author Salvador
 */
public class YearsDTO {
	private List<Integer> years;							// Lista de años

	/**
	 * Obtiene los años
	 * @return Lista con lo distintos años
	 */
	public List<Integer> getYears() {
		return years;
	}

	/**
	 * Establece la lista de años
	 * @param years: Nueva lista de años
	 */
	public void setYears(List<Integer> years) {
		this.years = years;
	}

	@Override
	public int hashCode() {
		return Objects.hash(years);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YearsDTO other = (YearsDTO) obj;
		return Objects.equals(years, other.years);
	}

	@Override
	public String toString() {
		return "YearsDTO [years=" + years + "]";
	}

	/**
	 * Constructor sin parámetros
	 */
	public YearsDTO() {
		super();
		this.years = new ArrayList();
	}
	
}
