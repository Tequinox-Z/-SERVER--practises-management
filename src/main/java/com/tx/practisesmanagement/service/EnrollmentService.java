package com.tx.practisesmanagement.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.Practise;
import com.tx.practisesmanagement.model.Preference;
import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.Student;
import com.tx.practisesmanagement.repository.EnrollmentRepository;


/**
 * Servicio de Matrícula
 * @author Salva
 */
@Service
public class EnrollmentService {
	
	// Repositorios
		@Autowired private EnrollmentRepository enrollmentRepository;
		@Autowired private PractiseService practiseService;
		@Autowired private PreferenceService preferenceService;
	/**
	 * Obtiene todas las matrículas
	 * @return Lista de matrículas
	 */
	public List<Enrollment> getAll() {
		return enrollmentRepository.findAll();
	}
	
	
	public boolean existEnrollment(Integer enrollmentDate, ProfessionalDegree degree, Student currentStudent) {
		boolean exist = false;
		
		System.out.println(enrollmentDate);
		
		if (enrollmentRepository.existEnrollment(enrollmentDate, degree, currentStudent) != null) {
			exist = true;
		}
	
		return exist;
	}
	
	
	/**
	 * Obtiene una determinada matrícula
	 * @param id: Id de la matrícula
	 * @return Matrícula solicitada
	 */
	public Enrollment get(Integer id) {
		return enrollmentRepository.findById(id).orElse(null);
	}
	
	public Enrollment setDegree(Integer enrollmentId, ProfessionalDegree degree) {
		Enrollment enrollment = get(enrollmentId);
		
		enrollment.setProfessionalDegree(degree);
		
		return this.enrollmentRepository.save(enrollment);
	}
	
	public Enrollment quitDegree(Integer enrollmentId, ProfessionalDegree degree) {
		Enrollment enrollment = get(enrollmentId);
		
		enrollment.setProfessionalDegree(null);
		
		return this.enrollmentRepository.save(enrollment);
	}
	
	/**
	 * Guarda un matrícula
	 * @param enrollment: Matrícula a guardar
	 * @return Matrícula guardada
	 */
	public Enrollment save(Enrollment enrollment) {
		return enrollmentRepository.save(enrollment);
	}
	
	/**
	 * Borra una matrícula
	 * @param enrollment: Matrícula a borrar
	 */
	public void delete(Enrollment enrollment) {
		enrollment.setStudent(null);								// Establecemos el estudiante a nulo
		enrollment.setProfessionalDegree(null);						// Establecemos el ciclo a nulo
		
		for (Preference currentPreference : enrollment.getPreferences()) {
			preferenceService.removePreference(currentPreference.getId());
		}
		
		this.save(enrollment);										// Guardamos la matrícula
		
		if (enrollment.getPractise() != null) {
			Integer practiseID = enrollment.getPractise().getId();		// Obtenemos el id de la practica			
			enrollment.setPractise(null);								// Establecemos la practica a nulo
			this.save(enrollment);										// Guardamos la matrícula
			practiseService.remove(practiseID);
		}
				
		enrollmentRepository.deleteById(enrollment.getId());
	}
	
	
	public Enrollment deletePractise(Integer enrollmentId) {
		Enrollment enrollment = get(enrollmentId);
		enrollment.setPractise(null);
		
		return enrollmentRepository.save(enrollment);
	}
	
	public Enrollment createPractise(Integer enrollmentId) {
		Enrollment enrollment = get(enrollmentId);
		
		Practise practise = practiseService.save(new Practise());
		enrollment.setPractise(practise);
		
		return enrollmentRepository.save(enrollment);
	}
	
	public Preference addPreference(Enrollment currentEnrollment, Business business) throws UserErrorException {

		Preference newPreference;
		
		if (preferenceService.getByEnrollmentAndBusiness(currentEnrollment, business) != null) {
			throw new UserErrorException("La empresa ya está añadida como preferencia a esta matrícula");
		}
		else {
			newPreference = new Preference(preferenceService.getCountPreferenceByEnrollment(currentEnrollment) + 1, business, currentEnrollment);
			
			return preferenceService.save(newPreference);
		}
	}
	
	public List<Preference> updatePreferencesPositions(List<Preference> preferences) {
		List<Preference> preferenceUpdated = new ArrayList<>();
		
		for (Preference currentPreference : preferences) {
			Preference preference = preferenceService.get(currentPreference.getId());
			
			if (preference != null) {
				preference.setPosition(currentPreference.getPosition());
				preferenceUpdated.add(preferenceService.save(preference));
			}
		}
		
		return preferenceUpdated;
	}
	
	public List<Preference> getPreferences(Integer enrollmentId) {
		Enrollment enrollment = get(enrollmentId);

		return enrollment.getPreferences();
	}
	
	public void removePreference(Integer preferenceId) throws UserErrorException {
		Preference preference = preferenceService.get(preferenceId);
		
		if (preference == null) {
			throw new UserErrorException("La preferencia no existe");
		}
		
		List<Preference> preferencesToUpdate = preferenceService.getPreferenceHiggerThat(preference.getPosition(), preference.getEnrollment());
		
		preference.setBusiness(null);
		preference.setEnrollment(null);
		
		preferenceService.save(preference);
		
		preferenceService.removePreference(preferenceId);
		
		for (Preference currentPreference : preferencesToUpdate) {
			currentPreference.setPosition(currentPreference.getPosition() - 1);
			preferenceService.save(currentPreference);
		}
	}
}
