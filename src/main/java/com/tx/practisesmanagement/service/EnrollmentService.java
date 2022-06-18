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
	
	/**
	 * Comprueba si existe una matriculación
	 * @param enrollmentDate: Fecha de matriculación
	 * @param degree: Ciclo
	 * @param currentStudent: Estudiante
	 * @return: ¿Existe?
	 */
	public boolean existEnrollment(Integer enrollmentDate, ProfessionalDegree degree, Student currentStudent) {
		boolean exist = false;
				
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
	
	/**
	 * Establece un ciclo a una matricula
	 * @param enrollmentId: Id de la matricula
	 * @param degree: Ciclo
	 * @return Matricula actualizada
	 */
	public Enrollment setDegree(Integer enrollmentId, ProfessionalDegree degree) {
		Enrollment enrollment = get(enrollmentId);
		
		enrollment.setProfessionalDegree(degree);
		
		return this.enrollmentRepository.save(enrollment);
	}
	
	
	/**
	 * Quita un ciclo de la matricula
	 * @param enrollmentId: Id de la matricula
	 * @param degree: Ciclo
	 * @return Matricula actualizada
	 */
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
		Enrollment enrollmentUpdate = enrollment;								// Obtenemos la matricula
		
		
		// Recorremos sus preferencias y las borramos
		
		for (Preference currentPreference : enrollmentUpdate.getPreferences()) {
			preferenceService.removePreference(currentPreference.getId());
		}
																			
		// Comprobamos si tiene practica asignada
		
		if (enrollmentUpdate.getPractise() != null) {
			
			enrollmentUpdate = get(enrollmentUpdate.getId());					// Lo obtenemos
			
			Integer practiseID = enrollmentUpdate.getPractise().getId();		// Obtenemos el id de la practica			
			enrollmentUpdate.setPractise(null);									// Establecemos la practica a nulo
			
			enrollmentUpdate.getPreferences().clear();
			
			this.save(enrollmentUpdate);										// Guardamos la matrícula
			practiseService.remove(practiseID);
		}
				
		// Si existe una matriculación lo borramos
		
		if (enrollmentRepository.findById(enrollmentUpdate.getId()).orElse(null) != null) {
			enrollmentRepository.deleteById(enrollmentUpdate.getId());
		}
	}
	
	/**
	 * Borra las prácticas de la matriculación 
	 * @param enrollmentId: Id de la matricula
	 * @return Matrícula actualizada
	 */
	public Enrollment deletePractise(Integer enrollmentId) {
		Enrollment enrollment = get(enrollmentId);
		enrollment.setPractise(null);
		
		return enrollmentRepository.save(enrollment);
	}
	
	
	/**
	 * Crea una practica para la matricula actual
	 * @param enrollmentId: Id de la practica
	 * @return: Matricula actualizada
	 */
	public Enrollment createPractise(Integer enrollmentId) {
		Enrollment enrollment = get(enrollmentId);
		
		Practise practise = practiseService.save(new Practise());
		enrollment.setPractise(practise);
		
		return enrollmentRepository.save(enrollment);
	}
	
	/**
	 * Añade una preferencia
	 * @param currentEnrollment Matricula actual
	 * @param business Empresa
	 * @return Preferencia añadida
	 * @throws UserErrorException: En caso de que la preferencia ya exista
	 */
	public Preference addPreference(Enrollment currentEnrollment, Business business) throws UserErrorException {

		Preference newPreference;								// Creamos una preferencia
		
		// Comprobamos si ya existe
		
		if (preferenceService.getByEnrollmentAndBusiness(currentEnrollment, business) != null) {
			throw new UserErrorException("La empresa ya está añadida como preferencia a esta matrícula");
		}
		else {
			
			// Si no existe la creamos y guardamos
			
			newPreference = new Preference(preferenceService.getCountPreferenceByEnrollment(currentEnrollment) + 1, business, currentEnrollment);
			
			return preferenceService.save(newPreference);			// Retornamos el resultado
		}
	}
	
	/**
	 * Actualiza las posiciones de las preferencias
	 * @param preferences: Lista de preferencias
	 * @return: Lista de preferencias actualizadas
	 */
	public List<Preference> updatePreferencesPositions(List<Preference> preferences) {
		List<Preference> preferenceUpdated = new ArrayList<>();							// Inicializamos la lista
		
		/**
		 * Iteramos y establecemos la nueva posición
		 */
		for (Preference currentPreference : preferences) {
			Preference preference = preferenceService.get(currentPreference.getId());
			
			if (preference != null) {
				preference.setPosition(currentPreference.getPosition());
				preferenceUpdated.add(preferenceService.save(preference));
			}
		}
		
		return preferenceUpdated;														// Retornamos el resultado
	}
	/**
	 * Obtiene las preferencias
	 * @param enrollmentId: Id de la matricula
	 * @return: Lista de preferencias
	 */
	public List<Preference> getPreferences(Integer enrollmentId) {
		Enrollment enrollment = get(enrollmentId);

		return enrollment.getPreferences();
	}
	
	/**
	 * Borra una preferencias
	 * @param preferenceId: Id de la preferencias
	 * @throws UserErrorException: En caso de que no exista la preferencia
	 */
	public void removePreference(Integer preferenceId) throws UserErrorException {
		Preference preference = preferenceService.get(preferenceId);
		
		if (preference == null) {
			throw new UserErrorException("La preferencia no existe");							// En caso de que no exista
		}
		
		/**
		 * Inciamos la lista de preferencias a actualizar
		 */
		List<Preference> preferencesToUpdate = preferenceService.getPreferenceHiggerThat(preference.getPosition(), preference.getEnrollment());
		
		// QUitamos su empresa y matrícula
		
		preference.setBusiness(null);
		preference.setEnrollment(null);
		
		// Guardamos
		
		preferenceService.save(preference);
		
		// Borramos la preferencia
		
		preferenceService.removePreference(preferenceId);
		
		// Por cada preferenica a actualizar restamos uno
		
		for (Preference currentPreference : preferencesToUpdate) {
			currentPreference.setPosition(currentPreference.getPosition() - 1);
			preferenceService.save(currentPreference);
		}
	}
}
