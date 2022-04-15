package com.tx.practisesmanagement.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.repository.BusinessRepository;

/**
 * Servicio de empresa
 * @author Salva
 */
@Service
public class BusinessService {

	// Repositorios
		@Autowired private BusinessRepository businessRepository;
	
	/**
	 * Obtiene todas las empresas
	 * @return Lista de empresas
	 */
	public List<Business> getAll() {
		return businessRepository.findAll();
	}
	
	/**
	 * Edita una empresa
	 * @param cif: Cif de la empresa
	 * @param business: Empresa 
	 * @return Empresa editada
	 */
	public Business edit(String cif, Business business) {
		Business businessOld = this.get(cif);								// Obtiene la empresa
		
		businessOld.setName(business.getName());							// Establece el nuevo nombre
		businessOld.setNumberOfStudents(business.getNumberOfStudents());	// Establece el número de estudiantes
		
		return businessRepository.save(businessOld);						// Guardamos la empresa
	}
	
	/**
	 * Obtiene una empresa
	 * @param cif: Identificador de la empresa
	 * @return: Empresa solicitada
	 */
	public Business get(String cif) {
		return businessRepository.findById(cif).orElse(null);
	}
	
	/**
	 * Guarda una empresa
	 * @param business: Empresa guardada
	 * @return: Empresa resultante
	 */
	public Business save(Business business) {
		return businessRepository.save(business);
	}
	
	/**
	 * Comprueba si existe una empresa
	 * @param business: Empresa a comprobar
	 * @return Boolean: Existe True, no existe False
	 */
	public boolean exist(Business business) {
		return businessRepository.existsById(business.getCif());
	}
}
