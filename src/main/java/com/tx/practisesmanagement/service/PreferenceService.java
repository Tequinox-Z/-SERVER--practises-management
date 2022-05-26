package com.tx.practisesmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.Preference;
import com.tx.practisesmanagement.repository.PreferenceRepository;

@Service
public class PreferenceService {
	
	@Autowired PreferenceRepository preferenceRepository;
	
	public Preference get(Integer id) {
		return preferenceRepository.findById(id).orElse(null);
	}
	
	
	public void removePreference(Integer id) {
		Preference currentPreference = get(id);
		
		currentPreference.setBusiness(null);
		currentPreference.setEnrollment(null);
		
		preferenceRepository.save(currentPreference);
		
		preferenceRepository.delete(currentPreference);
	}
	
	public Preference save(Preference preference) {
		return this.preferenceRepository.save(preference);
	}
	
	
	public Integer getCountPreferenceByEnrollment(Enrollment enrollment) {
		return this.preferenceRepository.countByEnrollment(enrollment);
	}
	
	public List<Preference> getPreferenceHiggerThat(Integer position) {
		return this.preferenceRepository.getPreferenceHiggerThat(position);
	}
	
	public List<Preference> getPreferencesByIdEnrollment(Integer id) {
		return this.preferenceRepository.getPreferencesByIdEnrollment(id);
	}
	
	
	public Preference getByEnrollmentAndBusiness(Enrollment enrollment, Business business) {
		return this.preferenceRepository.getByEnrollmentAndBusiness(enrollment, business);
	}
}
