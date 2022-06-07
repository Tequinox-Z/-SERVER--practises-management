package com.tx.practisesmanagement.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YearsDTO {
	private List<Integer> years;

	public List<Integer> getYears() {
		return years;
	}

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

	public YearsDTO() {
		super();
		this.years = new ArrayList();
	}
	
}
