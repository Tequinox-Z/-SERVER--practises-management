package com.tx.practisesmanagement.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.tx.practisesmanagement.model.Preference;

public class UpdatePreferencesDTO implements Serializable {

	private static final long serialVersionUID = -8948275349719673447L;
	private List<Preference> preferences;

	
	public UpdatePreferencesDTO() {
		super();
	}
	

	public UpdatePreferencesDTO(List<Preference> preferences) {
		super();
		this.preferences = preferences;
	}

	public List<Preference> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(preferences);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpdatePreferencesDTO other = (UpdatePreferencesDTO) obj;
		return Objects.equals(preferences, other.preferences);
	}

	@Override
	public String toString() {
		return "UpdatePreferencesDTO [preferences=" + preferences + "]";
	}
	
}
