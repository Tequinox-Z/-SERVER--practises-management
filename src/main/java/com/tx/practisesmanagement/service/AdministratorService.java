package com.tx.practisesmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.Practise;
import com.tx.practisesmanagement.model.School;
import com.tx.practisesmanagement.repository.AdministratorRepository;


/**
 * Servicio de administrador
 * @author Salva
 */
@Service
public class AdministratorService {

	// Repositorio
 		@Autowired private AdministratorRepository adminRepository;
 		@Autowired private SchoolService schoolService;
 		@Autowired private PractiseService practiseService;
 		
 	/**
 	 * Almacena un administrador
 	 * @param administrator: Administrador a almacenar
 	 * @return: Administrador almacenado
 	 */
	public Administrator save(Administrator administrator) {
		return adminRepository.save(administrator);
	}
	
	
	
	/**
	 * Obtiene un determinado administrador
	 * @param dni: DNi del administrador
	 * @return: Administrador solicitado
	 */
	public Administrator get(String dni) {
		return adminRepository.findById(dni).orElse(null);
	}
	
	/** 
	 * Obtiene todos los administradores
	 * @return Lista de administradores
	 */
	public List<Administrator> getAll() {
		return adminRepository.findAll();
	}
	
	/**
	 * Actualiza un administrador
	 * @param administrator: Administrador a actualizar
	 * @param personData: Datos del administrador
	 * @return Administrador actualizado
	 */
	public Administrator updateAdministrator(String dni, PersonDTO personData) {
		
		Administrator admin = get(dni);
		
		// Actualizamos los datos
		
		if (personData.getBirthDate() != null) {
			admin.setBirthDate(personData.getBirthDate());
		}
		if (personData.getName() != null) {
			admin.setName(personData.getName());
		}
		if (personData.getLastName() != null) {
			admin.setLastName(personData.getLastName());
		}
		if (personData.getImage() != null) {
			admin.setImage(personData.getImage());
		}
		if (personData.getTelefone() != null) {
			admin.setTelefone(personData.getTelefone());
		}
		if (personData.getAddress() != null) {
			admin.setAddress(personData.getAddress());
		}
		if (personData.getEmail() != null) {
			admin.setEmail(personData.getEmail());
		}
		
		// Almacenamos los cambios
		
		return adminRepository.save(admin);
	}
	
	public void deleteAdministrator(String dni) throws UserErrorException {
		
		Administrator administrator = get(dni);
		
		if (administrator == null) {
			throw new UserErrorException("El usuario no existe");
		}
		else {
			removeAdministratorFromPractise(dni);
			
			if (administrator.getSchoolSetted() != null) {
				removeSchoolFromAdministrator(dni);
			}

			removeDegreesFromAdministrator(dni);
			
			adminRepository.delete(administrator);
		}	
	}
	
	/**
	 * Borra los ciclos de un administrador
	 * @param dni: DNi del administrador
	 * @return: Administrador sin ciclos
	 */
	public Administrator removeDegreesFromAdministrator(String dni) {
		Administrator administrator = this.get(dni);			// Obtenemos el administrador
		
		administrator.getProfessionalDegrees().clear();			// Borramos los ciclos
		
		return adminRepository.save(administrator);				// Guardamos
	}
	
	public Administrator removeAdministratorFromPractise(String dni) {
		
		Administrator administrator = get(dni);
		
		for (Practise currentPractise: administrator.getPractises()) {
			practiseService.setTeacher(currentPractise.getId(), null);
		}
		
		return administrator;
	}
	
	
	/**
	 * Borra la escuela de un Administrador
	 * @param dni: DNi del administrador
	 * @return Administrador
	 */
	public Administrator removeSchoolFromAdministrator(String dni) {
		Administrator administrator = this.get(dni);						// Obtenemos el administrador
		School school = administrator.getSchoolSetted();
		
		boolean deleteSchool = false;
		
		if (school.getAdministrators().size() == 1) {
			deleteSchool = true;
		}
		
		administrator.setSchoolSetted(null);								// Ponemos la escuela a null
		
		administrator = adminRepository.save(administrator);				// Guardamos
		
		if (deleteSchool) {
			schoolService.removeSchool(school.getId());
		}
		
		return administrator;
	}
	
	public Integer getCountFromSchool(Integer id) {
		return this.adminRepository.getCountFromSchool(id);
	}

	
	public void quitAdministratorsFromSchool(School currentSchool) {
		for (Administrator currentAdmin : currentSchool.getAdministrators()) {
			currentAdmin.setSchoolSetted(null);
			save(currentAdmin);
		}
	}
}
