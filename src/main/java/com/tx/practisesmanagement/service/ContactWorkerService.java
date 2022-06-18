package com.tx.practisesmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.ContactWorker;
import com.tx.practisesmanagement.repository.ContactWorkerRepository;


@Service
/**
 * Servicio de trabajadores de contacto
 * @author Salvador
 */
public class ContactWorkerService {
	@Autowired ContactWorkerRepository contactWorkerRepository;
	
	
	/**
	 * Guarda un trabajador
	 * @param contactWorker: Nuevo trabajador
	 * @return: Trabajador guardado
	 */
	public ContactWorker save(ContactWorker contactWorker) {
		return this.contactWorkerRepository.save(contactWorker);
	}
	
	/**
	 * Edita un trabajador de contacto
	 * @param contactWorker: Trabajador de contacto
	 * @return: Trabajador de contacto editado
	 */
	public ContactWorker edit(ContactWorker contactWorker) {
		
		// Lo obtenemos
		
		ContactWorker contactWorkerBBDD = this.contactWorkerRepository.findById(contactWorker.getId()).orElse(null);

		
		// Comprobamos que no sea null
		
		if (contactWorkerBBDD != null) {	
		
			// Asignamos los datos
			
			if (contactWorker.getEmail() != null) {
				contactWorkerBBDD.setEmail(contactWorker.getEmail());	
			}
			if (contactWorker.getName() != null) {
				contactWorkerBBDD.setName(contactWorker.getName());	
			}
			if (contactWorker.getTelefone() != null) {
				contactWorkerBBDD.setTelefone(contactWorker.getTelefone());	
			}
			
			return this.contactWorkerRepository.save(contactWorkerBBDD);			// Guardamos
		}
		else {
			
			// En caso de que no exista lo indicamos
			
			throw new UserErrorException("El usuario no se puede editar por que no existe");
		}
		
	}
	
	
	/**
	 * Obtiene un trabajador por su id
	 * @param id: Id
	 * @return Trabajador de contacto
	 */
	public ContactWorker getById(Integer id) {
		return this.contactWorkerRepository.findById(id).orElse(null);
	}
	
	/**
	 * Trabajador de contacto
	 * @param id: Id del trabajador
	 */
	public void remove(Integer id) {
		this.contactWorkerRepository.deleteById(id);
	}
	
}
