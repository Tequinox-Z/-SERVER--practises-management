package com.tx.practisesmanagement.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.School;
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
		@Autowired AdministratorService administratorService;
		@Autowired ProfessionalDegreeService professionalDegreeService; 
		
	/**
	 * Obtiene una escuela determinada
	 * @param id: Identificador de la escuela
	 * @return: Escuela solicitada
	 */
	public School get(Integer id) {
		return schoolRepository.findById(id).orElse(null);
	}
	
	
	/**
	 * Borra un administrador
	 * @param dniAdministrator: Dni del administrador
	 */
	public void removeAdministrator(String dniAdministrator) {		
		Administrator administrator = administratorService.get(dniAdministrator);		// Obtenemos el administrador
		
		administrator.setSchool(null);													// Establecemos la escuela a null
		
		administratorService.save(administrator);										// Guardamos
	}
	
	
	/**
	 * Obtiene el ciclo de una escuela
	 * @param idSchool
	 * @param idDegree
	 * @return
	 */
	public ProfessionalDegree getDegree(Integer idSchool, Integer idDegree) {
		School school = this.get(idSchool);																// Obtenemos la escuela
		
		Integer position = school.getProfessionalDegrees().indexOf(new ProfessionalDegree(idDegree));	// Obtenemos la posición del ciclo
		
		ProfessionalDegree degree;											// Esta variable almacenará el ciclo
		
		if (position == -1) {
			degree = null;													// Si no existe el ciclo establecemos el ciclo en null
		}
		else {
			degree = school.getProfessionalDegrees().get(position);			// Si no lo obtenemos
		}
		
		return degree;														// Retornamos el ciclo
	}
		
	/**
	 * Añade un administrador a una escuela
	 * @param schoolId: Identificador de la escuela
	 * @param administrator: Administrador
	 * @return: Escuela
	 */
	public School addAdministratorToSchool(Integer schoolId, Administrator administrator) {
			
		School school = this.get(schoolId);							// Obtenemos la escuela
		administrator.setSchool(school);							// Establecemos la escuela
		
		administratorService.save(administrator);					// Guardamos el administrador
			
		return school;												// Retornamos la escuela
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
	public List<ProfessionalDegree> getProfessionalDegrees(Integer idSchool) {
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
		
		schoolOld.setAddress(school.getAddress());				// Establecemos la dirección
		schoolOld.setImage(school.getImage());					// Establecemos la imagen
		schoolOld.setName(school.getName());					// Establecemos el nombre
		schoolOld.setPassword(school.getPassword());			// Establecemos la contraseña
		
		return this.save(schoolOld);							// Guardamos y retornamos
	}
	
	/**
	 * Borra una escuela
	 * @param idSchool: Identificador de la escuela
	 */
	public void removeSchool(Integer idSchool) {
		School school = this.get(idSchool);
		
		// Delete next code
		
		for (Administrator currentAdministrator: school.getAdministrators()) {
			this.removeAdministrator(currentAdministrator.getDni());
		}
		
		// END
		
		
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
	 * Crea un ciclo
	 * @param schoolId: Id de la escuela
	 * @param newProfessionalDegree: Ciclo a crear
	 * @return: Ciclo creado
	 */
	public ProfessionalDegree createProfessionalDegree(Integer schoolId, ProfessionalDegree newProfessionalDegree) {
		School school = this.get(schoolId);						// Obtenemos la escuela
		newProfessionalDegree.setSchool(school);				// Asignamos el ciclo
		
		return this.professionalDegreeService.saveProfessionalDegree(newProfessionalDegree);		// Guardamos				
	}
		
	/**
	 * Verifica si una escuela contiene un ciclo
	 * @param schoolId: Id de la escuela
	 * @param professionalDegree: Id del ciclo
	 * @return: Boolean
	 */
	public boolean containProfessionalDegree(Integer schoolId, ProfessionalDegree professionalDegree) {	
		School school = this.get(schoolId);											// Obtenemos la escuela
		return school.getProfessionalDegrees().contains(professionalDegree);		// Verificamos si contiene el ciclo
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
	
	public Integer getCountAdministrators(Integer idSchool) {
		return null;
	}
		
}
