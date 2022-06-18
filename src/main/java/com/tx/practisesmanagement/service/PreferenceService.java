package com.tx.practisesmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.Preference;
import com.tx.practisesmanagement.repository.PreferenceRepository;

@Service
/**
 * Servicio de preferencias
 * @author Salvador
 *
 */
public class PreferenceService {
	
	// Repositorio
	
	@Autowired PreferenceRepository preferenceRepository;
	
	/**
	 * Obtiene una preferencia por su id
	 * @param id Id de la preferencia
	 * @return Preferencia obtenida
	 */
	public Preference get(Integer id) {
		return preferenceRepository.findById(id).orElse(null);
	}
	
	/**
	 * Borra una preferencia por su id
	 * @param id Id de la preferencia
	 */
	public void removePreference(Integer id) {
		Preference currentPreference = get(id);
		
		preferenceRepository.delete(currentPreference);
	}
	
	/**
	 * Guarda una preferencia
	 * @param preference Preferencia a guardar
	 * @return Preferencia guardada
	 */
	public Preference save(Preference preference) {
		return this.preferenceRepository.save(preference);
	}
	
	/**
	 * Obtiene el numero de preferencias en una matricula
	 * @param enrollment Matricula a buscar
	 * @return NUmero de preferencias
	 */
	public Integer getCountPreferenceByEnrollment(Enrollment enrollment) {
		return this.preferenceRepository.countByEnrollment(enrollment);
	}
	/**
	 * Obtiene las preferencias mayores por posición
	 * @param position Posición minima
	 * @param enrollment Matricula 
	 * @return Lista de preferencias mayores
	 */
	public List<Preference> getPreferenceHiggerThat(Integer position, Enrollment enrollment) {
		return this.preferenceRepository.getPreferenceHiggerThat(position, enrollment);
	}
	
	/**
	 * Obtiene las preferencias por id de matricula
	 * @param id Identificado de la matricula
	 * @return Lista de preferencias
	 */
	public List<Preference> getPreferencesByIdEnrollment(Integer id) {
		return this.preferenceRepository.getPreferencesByIdEnrollment(id);
	}
	/**
	 * Obtiene la preferencia por matricula y empresa
	 * @param enrollment Matricula a buscar
	 * @param business EMpresa a buscar
	 * @return Preferencia
	 */
	public Preference getByEnrollmentAndBusiness(Enrollment enrollment, Business business) {
		return this.preferenceRepository.getByEnrollmentAndBusiness(enrollment, business);
	}
	
	
	/**
	 * Comprueba si existe en una matricula dicha posición
	 * @param enrollment Matricula
	 * @param position Posición
	 * @return Preferencia
	 */
	public Preference existPositionInEnrollment(Enrollment enrollment, Integer position) {
		return this.preferenceRepository.existPositionInEnrollment(enrollment, position);
	}
	
	/**
	 * Borra todas las preferencias de una matricula
	 * @param enrollment Matricula a buscar
	 */
	public void deleteAllPreferenceFromEnrollment(Enrollment enrollment) {
		List<Preference> preferencesToDelete = enrollment.getPreferences();
		
		for (Preference currentPreference: preferencesToDelete) {
			this.removePreference(currentPreference.getId());
		}
	}
	/**
	 * Comprueba si existen todas las preferencias
	 * @param preferences Lista de preferencias a comprobar
	 * @return ¿Existen todas?
	 */
	public boolean checkIfExistAll(List<Preference> preferences) {
		boolean result = true;
		
		try {
			
			// Iteramos las preferencias
			
			for (Preference currentPreference: preferences) {
				if (currentPreference.getId() == null || this.get(currentPreference.getId()) == null) {
					throw new UserErrorException();				// Si alguna no existe lanzamos excepción
				}
			}
		}
		catch (UserErrorException error) {
			result = false;										// SI alguna no existe retornamos falso
		}
		
		return result;
	}
	
	/**
	 * Comprueba si el orden de las preferencias es correcto
	 * @param preferences Lista de preferencias
	 * @return ¿Es correcto el orden?
	 */
	public boolean checkIfOrderIsCorrect(List<Preference> preferences) {
		boolean result = true;
		
		Integer lastPosition = 0;
		
		try {
			for (Preference preference: preferences) {
				if (preference.getPosition() != lastPosition) {
					throw new UserErrorException();										// Si el número de posición es distinto del esperado lanzamos excepción
				}
				lastPosition += 1;														// Sumamos una posición
			}
		}
		catch (Exception e) {
			result = false;																// En caso de error en la posición retornamos falso
		}
			
		return result;																	// Retornamos el resultado
	}
	
	/**
	 * Actualzia las posiciones
	 * @param preferences Lista de preferencias
	 * @return Lista de preferencias con posiciones actualizadas
	 */
	public List<Preference> updatePositions(List<Preference> preferences) {
		
		List<Preference> preferencesUpdated = new ArrayList<>();
		
		for (Preference currentPreference: preferences) {
			Preference preferenceDB = get(currentPreference.getId());
			
			preferenceDB.setPosition(currentPreference.getPosition());
			
			preferencesUpdated.add(save(preferenceDB));
		}
		
		return preferencesUpdated;
	}
	
	/**
	 * Obtiene la empresa de una preferencia
	 * @param business Empresa asignada en la practica
	 * @return Lista de preferencias
	 */
	public List<Preference> getByBusiness(Business business) {
		return this.preferenceRepository.getByBusiness(business);
	}
	
	
	/**
	 * Borra una empresa de las preferencias y actualiza las posiciones del resto de preferencias
	 * @param business Empresa a borrar de las preferencas
	 */
	public void removeFromBusinessAndUpdatePositions(Business business) {
		
		for (Preference preference : this.getByBusiness(business)) {
			
			// Obtenemos las preferencias a actualizar 
			
			List <Preference> preferencesToUpdate = this.preferenceRepository.getPreferenceHiggerThat(preference.getPosition(), preference.getEnrollment());
			
			
			// Actualizamos las posiciones de las preferencias y guardamos
			
			for (Preference updatePositionPreference : preferencesToUpdate) {
				updatePositionPreference.setPosition(updatePositionPreference.getPosition() - 1);
				save(updatePositionPreference);
			}
			
			removePreference(preference.getId());						// Borramos la preferencia
		}
		
		
		
	}
}
