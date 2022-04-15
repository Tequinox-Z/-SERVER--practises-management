package com.tx.practisesmanagement.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.repository.AdministratorRepository;


/**
 * Servicio de administrador
 * @author Salva
 */
@Service
public class AdministratorService {

	// Repositorio
 		@Autowired private AdministratorRepository adminRepository;

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
	public Administrator updateAdministrator(Administrator administrator, PersonDTO personData) {
		
		// Actualizamos los datos
		
		administrator.setAddress(personData.getAddress());
		administrator.setBirthDate(personData.getBirthDate());
		administrator.setImage(personData.getImage());
		administrator.setLastName(personData.getLastName());
		administrator.setName(personData.getName());
		administrator.setPassword(personData.getPassword());
		administrator.setTelefone(personData.getTelefone());
		administrator.setEmail(personData.getEmail());
		
		// Almacenamos los cambios
		
		return adminRepository.save(administrator);
	}
	
	
	
	public Administrator deleteAdministrator(String dni) {
		Administrator administrator = this.removeSchoolFromAdministrator(dni);
		administrator = this.removeDegreesFromAdministrator(dni);
		
		adminRepository.delete(administrator);
		
		return administrator; 
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
	
	
	/**
	 * Borra la escuela de un Administrador
	 * @param dni: DNi del administrador
	 * @return Administrador
	 */
	public Administrator removeSchoolFromAdministrator(String dni) {
		Administrator administrator = this.get(dni);			// Obtenemos el administrador
		administrator.setSchool(null);							// Ponemos la escuela a null
		
		return adminRepository.save(administrator);				// Guardamos
	}
	
	public String getCountFromSchool(Integer id) {
		return this.adminRepository.getCountFromSchool(id);
	}

}
