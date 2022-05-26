package com.tx.practisesmanagement.service;



import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.RegTemp;
import com.tx.practisesmanagement.model.School;
import com.tx.practisesmanagement.model.UnusualMovement;
import com.tx.practisesmanagement.repository.SchoolRepository;

/**
 * Servicio de escuela
 * @author Salva
 */
@Service
public class SchoolService {
		
	// Repositorio
		@Autowired SchoolRepository schoolRepository;
	
	// Servicio
		@Autowired ProfessionalDegreeService professionalDegreeService; 
		@Autowired UnusualMovementService unusualMovementService;
		@Autowired RegTempService regTempService;
		
	/**
	 * Obtiene una escuela determinada
	 * @param id: Identificador de la escuela
	 * @return: Escuela solicitada
	 */
	public School get(Integer id) {
		return schoolRepository.findById(id).orElse(null);
	}
	
	/**
	 * Obtiene el ciclo de una escuela
	 * @param idSchool: Id del colegio
	 * @param name: Nombre del ciclo
	 * @return
	 */
	public ProfessionalDegree getDegree(Integer idSchool, Integer idDegree) {
		School school = this.get(idSchool);																// Obtenemos la escuela
		
		Integer position = school.getProfessionalDegrees().indexOf(new ProfessionalDegree(idDegree));		// Obtenemos la posición del ciclo
				
		if (position == -1) {
			throw new UserErrorException("El ciclo no existe");											// Si no existe el ciclo lanzamos excepción
		}
		else {
			return school.getProfessionalDegrees().get(position);										// Si no lo obtenemos
		}
		
	}
		

		
	/**
	 * Guarda una escuela
	 * @param school: Escuela
	 * @return Escuela
	 */
	public School save(School school) {
		return schoolRepository.save(school);
	}
		
	/**
	 * Obtiene todos los ciclos de una escuela
	 * @param idSchool: Escuela
	 * @return Lista de ciclos
	 */
	public List<ProfessionalDegree> getAllProfessionalDegrees(Integer idSchool) {
		return this.get(idSchool).getProfessionalDegrees();
	}
	
	/**
	 * Actualiza una escuela
	 * @param school: Nuevos datos de la escuela
	 * @param schoolId: Identificador de la escuela
	 * @return Escuela editada
	 */
	public School updateSchool(School school, Integer schoolId) {
		School schoolOld = this.get(schoolId);					// Obtenemos la escuela
		
		if (school.getAddress() != null) {			
			schoolOld.setAddress(school.getAddress());				// Establecemos la dirección
		}
		if (school.getImage() != null) {
			schoolOld.setImage(school.getImage());					// Establecemos la imagen			
		}
		if (school.getName() != null) {
			schoolOld.setName(school.getName());					// Establecemos el nombre			
		}
		if (school.getPassword() != null) {
			schoolOld.setPassword(school.getPassword());			// Establecemos la contraseña			
		}
		
		return this.save(schoolOld);							// Guardamos y retornamos
	}
	
	/**
	 * Borra una escuela
	 * @param idSchool: Identificador de la escuela
	 */
	public void removeSchool(Integer idSchool) {
		removeAllUnsualsMovements(idSchool);
		removeAllRegTempByIdSchool(idSchool);
		
		School school = this.get(idSchool);
		
		for (ProfessionalDegree currentDegree: school.getProfessionalDegrees()) {
			professionalDegreeService.removeDegree(currentDegree.getId());
		}
		
		schoolRepository.delete(school);
	}
	
	/**
	 * Obtiene todos los administradores
	 * @param idSchool: Id del colegio
	 * @return: Lista de administradores
	 */
	public List<Administrator> getAdministrators(Integer idSchool) {
		return this.get(idSchool).getAdministrators();
	}	
		
	/**
	 * Verifica si una escuela contiene un ciclo por su id
	 * @param schoolId: Id de la escuela
	 * @param professionalDegree: Id del ciclo
	 * @return: Boolean
	 */
	public boolean containProfessionalDegreeById(Integer schoolId, ProfessionalDegree professionalDegree) {	
		School school = this.get(schoolId);											// Obtenemos la escuela
		return school.getProfessionalDegrees().contains(professionalDegree);		// Verificamos si contiene el ciclo
	}
	
	
	public boolean containProfessionalDegreeByName(School school, String nameDegree) {
		return professionalDegreeService.existProfessionalDegreeByNameInSchool(school, nameDegree);
	}
		
	/**
	 * Obtiene todos los colegios por nombre
	 * @param name: Nombre
	 * @return Lista de colegios
	 */
	public List<School> getAllByName(String name) {
		return schoolRepository.findAllByName(name);
	}
		
	/**
	 * Obtiene todos los colegios
	 * @return Lista de colegios
	 */
	public List<School> getAll() {
		return schoolRepository.findAll();
	}
	
	public List<UnusualMovement> getMovementsById(Integer id) {
		School school = get(id);
		return school.getUnusualsMovements();
	}
	
	public List<RegTemp> getRegTempsByDate(Integer id, LocalDateTime date) {		
		return this.regTempService.getAllRegTempForDay(date, id);
	}
	
	public void removeAllRegTempByIdSchool(Integer id) {
		School school = get(id);

		List<RegTemp> regTemps = school.getTemperatureRecords();
		school.setTemperatureRecords(null);
		
		save(school);
		
		for (RegTemp currentRegTemp : regTemps) {
			regTempService.remove(currentRegTemp.getId());
		}
	}
	
	public void removeAllUnsualsMovements(Integer id) {
		School school = get(id);
		
		List<UnusualMovement> movements = school.getUnusualsMovements();
		school.getUnusualsMovements().clear();
		
		save(school);
		
		for (UnusualMovement currentMovement: movements) {
			this.unusualMovementService.removeUnusualMovement(currentMovement.getId());
		}
	}

		
}
