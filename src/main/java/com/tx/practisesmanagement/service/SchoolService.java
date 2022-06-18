package com.tx.practisesmanagement.service;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.Location;
import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.RegTemp;
import com.tx.practisesmanagement.model.School;
import com.tx.practisesmanagement.model.UnusualMovement;
import com.tx.practisesmanagement.repository.SchoolRepository;

/**
 * Servicio de centro
 * @author Salva
 */
@Service
public class SchoolService {
		
	// Repositorio
	
		@Autowired SchoolRepository schoolRepository;
	
	// Servicios
		
		@Autowired ProfessionalDegreeService professionalDegreeService; 
		@Autowired UnusualMovementService unusualMovementService;
		@Autowired RegTempService regTempService;
		@Autowired LocationService locationService;
		
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
	 * Añade un movimiento inusual
	 * @param school Centro al que se añadirá el movimiento
	 * @param newMovement Nuevo movimiento
	 * @return Movimiento guardado
	 */
	public UnusualMovement addUnusualMovement(School school, UnusualMovement newMovement) {
		
		UnusualMovement currentMovement = unusualMovementService.saveUnusualMovement(newMovement);
		school.addUnusualMovement(currentMovement);
		
		save(school);
		
		return currentMovement;
	}  
	
	/**
	 * Añade un nuevo registro de temperatura
	 * @param school Escuela a la que se le añadirá el registro
	 * @param regTemp Registro de temperatura
	 * @return Registro de temperatura guardado
	 */
	
	public RegTemp addRegTemp(School school, RegTemp regTemp) {
		
		RegTemp currentRegTemp = regTempService.save(regTemp, school.getId());
		
		if (!school.getTemperatureRecords().contains(currentRegTemp)) {
			school.addRecordTemp(currentRegTemp);			
			save(school);			
		}
		
		return currentRegTemp;
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
		if (school.getOpeningTime() != null) {
			schoolOld.setOpeningTime(school.getOpeningTime());
		}
		if (school.getClosingTime() != null) {
			schoolOld.setClosingTime(school.getClosingTime());
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
		removeLocation(idSchool);
		
		School school = this.get(idSchool);
		
		for (ProfessionalDegree currentDegree: school.getProfessionalDegrees()) {
			professionalDegreeService.removeDegree(currentDegree.getId());
		}
		
		schoolRepository.delete(school);
	}
	
	/**
	 * Establece una ubicación a un centro
	 * @param idSchool Id del centro
	 * @param location Localización
	 * @return Localización guardada
	 */
	public Location setLocation(Integer idSchool, Location location) {
		School currentSchool = get(idSchool);
	
		Location currentLocation = locationService.saveLocation(location);
		currentSchool.setLocation(currentLocation);
		
		save(currentSchool);
		
		return currentLocation;
	}
	
	
	/**
	 * Borra la localización del centro
	 * @param idSchool Id del centro
	 */
	public void removeLocation(Integer idSchool) {
		School currentSchool = get(idSchool);					// Obtenemos el centro
		
		Location location = currentSchool.getLocation();		// Obtenemos la localización
		if (location != null) {									
			currentSchool.setLocation(null);					// Si existe la quitamos
			
			save(currentSchool);								// Guardamos
			
			locationService.removeLocation(location.getId());	// Borramos la localización
		}
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
	
	/**
	 * Comprueba si existe un ciclo por nombre
	 * @param school Centro a buscar
	 * @param nameDegree Nombre del ciclo
	 * @return ¿Existe?
	 */
	public boolean containProfessionalDegreeByName(School school, String nameDegree) {
		return professionalDegreeService.existProfessionalDegreeByNameInSchool(school, nameDegree);
	}
		
	/**
	 * Obtiene todos los colegios por nombre
	 * @param name: Nombre
	 * @return Lista de colegios
	 */
	public List<School> getAllByName(String name) {
		return schoolRepository.findAllByName(name.toUpperCase());
	}
		
	/**
	 * Obtiene todos los colegios
	 * @return Lista de colegios
	 */
	public List<School> getAll() {
		return schoolRepository.findAll();
	}
	/**
	 * Obtiene los movimientos por id del centro
	 * @param id Id del centro
	 * @return Lista de movimientos
	 */
	public List<UnusualMovement> getMovementsById(Integer id) {
		School school = get(id);
		return school.getUnusualsMovements();
	}
	
	/**
	 * Obtiene los registros de temperatura por fecha
	 * @param id Id del centro
	 * @param date Fecha de los registros
	 * @return Lista de registros de temperatura
	 */
	public List<RegTemp> getRegTempsByDate(Integer id, LocalDateTime date) {		
		return this.regTempService.getAllRegTempForDay(date, id);
	}
	
	/**
	 * Borra todos los registros de temperatura 
	 * @param id Id del centro
	 */
	public void removeAllRegTempByIdSchool(Integer id) {
		School school = get(id);															// Obtenemos el centro

		List<RegTemp> regTemps = new ArrayList<>(school.getTemperatureRecords());			// Obtenemos sus registros
		school.getTemperatureRecords().clear();												// Limpiamos los registros
		
		save(school);																		// Guardamos el centro
				
		for (RegTemp currentRegTemp : regTemps) {
			regTempService.remove(currentRegTemp.getId());									// Borramos cada registro
		}
	}
	
	/**
	 * Borra todos los movimientos inusuales
	 * @param id: Id del centro
	 */
	public void removeAllUnsualsMovements(Integer id) {
		School school = get(id);																// Obtenemos el centro
		
		List<UnusualMovement> movements = new ArrayList<>(school.getUnusualsMovements());		// obtenemos sus movimientos
		school.getUnusualsMovements().clear();													// Limpiamos los movimientos
		
		save(school);																			// Guardamos 
		
		for (UnusualMovement currentMovement: movements) {
			this.unusualMovementService.removeUnusualMovement(currentMovement.getId());			// Borramos cada movimiento
		}
	}

	/**
	 * Añade un ciclo a un centro
	 * @param idSchool Id del centro
	 * @param professionalDegreeToAdd Ciclo a añadir
	 * @return CIclo añadido
	 */
	public ProfessionalDegree addDegreeToSchool(Integer idSchool, ProfessionalDegree professionalDegreeToAdd) {
		School school = get(idSchool);
		
		professionalDegreeToAdd.setSchool(school);
		
		return professionalDegreeService.saveProfessionalDegree(professionalDegreeToAdd);
	}
	
	/**
	 * Obtiene un centro por su localización 
	 * @param location Localización
	 * @return Centro al que pertenece la localización
	 */
	public School getSchoolByLocation(Location location) {
		return this.schoolRepository.getByLocation(location);
	}
		
}
