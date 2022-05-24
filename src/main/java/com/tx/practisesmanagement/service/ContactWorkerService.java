package com.tx.practisesmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.ContactWorker;
import com.tx.practisesmanagement.repository.ContactWorkerRepository;


@Service
public class ContactWorkerService {
	@Autowired ContactWorkerRepository contactWorkerRepository;
	
	public ContactWorker save(ContactWorker contactWorker) {
		return this.contactWorkerRepository.save(contactWorker);
	}
	
	public ContactWorker edit(ContactWorker contactWorker) {
		
		ContactWorker contactWorkerBBDD = this.contactWorkerRepository.findById(contactWorker.getId()).orElse(null);

		if (contactWorkerBBDD != null) {	
			
			if (contactWorker.getEmail() != null) {
				contactWorkerBBDD.setEmail(contactWorker.getEmail());	
			}
			if (contactWorker.getName() != null) {
				contactWorkerBBDD.setName(contactWorker.getName());	
			}
			if (contactWorker.getTelefone() != null) {
				contactWorkerBBDD.setTelefone(contactWorker.getTelefone());	
			}
			
			return this.contactWorkerRepository.save(contactWorkerBBDD);
		}
		else {
			throw new UserErrorException("El usuario no se puede editar por que no existe");
		}
		
	}
	
	public ContactWorker getById(Integer id) {
		return this.contactWorkerRepository.findById(id).orElse(null);
	}
	
	public void remove(Integer id) {
		this.contactWorkerRepository.deleteById(id);
	}
	
}
