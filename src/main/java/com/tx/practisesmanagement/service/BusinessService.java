package com.tx.practisesmanagement.service;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.ContactWorker;
import com.tx.practisesmanagement.model.LaborTutor;
import com.tx.practisesmanagement.model.Location;
import com.tx.practisesmanagement.model.Practise;
import com.tx.practisesmanagement.repository.BusinessRepository;

/**
 * Servicio de empresa
 * @author Salva
 */
@Service
public class BusinessService {

	// Repositorio
	
		@Autowired private BusinessRepository businessRepository;
		@Autowired private LocationService locationService;
		@Autowired private ContactWorkerService contactWorkerService;
		@Autowired private LaborTutorService laborTutorService;
		@Autowired private PractiseService practiseService;
		
	/**
	 * Obtiene todas las empresas
	 * @return Lista de empresas
	 */
	public List<Business> getAll() {
		return businessRepository.findAll();
	}
	
	/**
	 * Obtiene el recuento de empresas que hay en la aplicación
	 * @return  Número de empresas
	 */
	public Integer getCount() {
		return this.businessRepository.getCount();
	}
	
	/**
	 * Edita una empresa
	 * @param cif: Cif de la empresa
	 * @param business: Empresa 
	 * @return Empresa editada
	 */
	public Business edit(Business business) {
		Business businessOld = this.get(business.getCif());								// Obtiene la empresa
		
		if (business.getName() != null) {
			businessOld.setName(business.getName());							// Establece el nuevo nombre
		}
		if (business.getNumberOfStudents() != null) {
			businessOld.setNumberOfStudents(business.getNumberOfStudents());			// Establece el nuevo nombre
		}
		if (business.getImage() != null) {
			businessOld.setImage(business.getImage());
		}
		
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
	 * Obtiene información de una empresa por su localización
	 * @param location: Localización
	 * @return: Empresa solicitada
	 */
	public Business getByLocation(Location location) {
		return businessRepository.getByLocation(location);
	}
	
	/**
	 * Comprueba si existe una empresa
	 * @param business: Empresa a comprobar
	 * @return Boolean: Existe True, no existe False
	 */
	public boolean exist(Business business) {
		return businessRepository.existsById(business.getCif());
	}
	
	/**
	 * Permite buscar empresas por una cadena de texto
	 * @param name Nombre de la empresa o porción del nombre del mismo
	 * @return Lista de empresas
	 */
	public List<Business> getAllByName(String name) {
		return businessRepository.getAllByName(name.toUpperCase());
	}
	
	/**
	 * Borra una empresa
	 */
	public void remove(String cif) throws UserErrorException {
		Business business = this.get(cif);										// Obtenemos la empresa
		
		List<Practise> practises = business.getPractises();						// Obtenemos la lista de prácticas
		
		Date currentDate = new Date();											// Obtenemos la fecha actual
		
		// Verificamos que todas las prácticas hayan terminado
		
		for (Practise currentPractise : practises) {
			if (!(currentPractise.getFinish() == null || currentPractise.getStart() == null) && currentPractise.getFinish().after(currentDate)) {
				throw new UserErrorException("Hay becarios que aún tienen que empezar o terminar las prácticas en su empresa");
			}
		}
		
		// Quitamos todas las prácticas
		
		for (Practise currentPractise : practises) {
			currentPractise.setBusiness(null);
			practiseService.save(currentPractise);
		}
		
		// Obtenemos su localización
		
		Location location = business.getLocation();
		
		// Obtenemos los trabajadores de contacto
		
		List<ContactWorker> contactWorkers = new ArrayList<>(business.getContactWorkers());
		
		// Limpiamos las dependencias
		
		business.getDegrees().clear();
		business.getContactWorkers().clear();		
		business.setLocation(null);
		
		
		// Guardamos
		
		this.save(business);
				
		// Quitamos la localización
		
		if (location != null) {			
			locationService.removeLocation(location.getId());
		}
		
		// Borramos los trabajadores de contacto
		
		for (ContactWorker currentContactWorker: contactWorkers) {
			contactWorkerService.remove(currentContactWorker.getId());
		}
		
		// Quitamos la empresa de los tutores laborales
		
		for (LaborTutor currentTutor : business.getTutors()) {
			currentTutor.setBusiness(null);
			laborTutorService.save(currentTutor);
		}
		
		// Finalmente borramos
		
		businessRepository.delete(business);
	}
	
	/**
	 * Obtiene el número de estudiantes que hay este año
	 * @param business: Empresa
	 * @return Número de estudiantes
	 */
	public Integer getCountofStudentInBusinessInThisYear(Business business) {
		return this.businessRepository.getCountOfStudentInBusinessAndYear(business, Calendar.getInstance().get(Calendar.YEAR));
	}
}
