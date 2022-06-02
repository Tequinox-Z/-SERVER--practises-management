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
	 * Comprueba si existe una empresa
	 * @param business: Empresa a comprobar
	 * @return Boolean: Existe True, no existe False
	 */
	public boolean exist(Business business) {
		return businessRepository.existsById(business.getCif());
	}
	
	/**
	 * Borra una empresa
	 */
	public void remove(String cif) throws UserErrorException {
		Business business = this.get(cif);
		List<Practise> practises = business.getPractises();
		
		Date currentDate = new Date();
		
		for (Practise currentPractise : practises) {
			if (!(currentPractise.getFinish() == null || currentPractise.getStart() == null) && currentPractise.getFinish().after(currentDate)) {
				throw new UserErrorException("Hay becarios que aún tienen que empezar o terminar las prácticas en su empresa");
			}
		}
		
		for (Practise currentPractise : practises) {
			currentPractise.setBusiness(null);
			practiseService.save(currentPractise);
		}
		
		Location location = business.getLocation();
		List<ContactWorker> contactWorkers = new ArrayList<>(business.getContactWorkers());
		
		business.getDegrees().clear();
		business.getContactWorkers().clear();		
		business.setLocation(null);
		
		this.save(business);
				
		if (location != null) {			
			locationService.removeLocation(location.getId());
		}
		
		for (ContactWorker currentContactWorker: contactWorkers) {
			contactWorkerService.remove(currentContactWorker.getId());
		}
		
		for (LaborTutor currentTutor : business.getTutors()) {
			currentTutor.setBusiness(null);
			laborTutorService.save(currentTutor);
		}
		
		businessRepository.delete(business);
	}
	
	public Integer getCountofStudentInBusinessInThisYear(Business business) {
		return this.businessRepository.getCountOfStudentInBusinessAndYear(business, Calendar.getInstance().get(Calendar.YEAR));
	}
}
