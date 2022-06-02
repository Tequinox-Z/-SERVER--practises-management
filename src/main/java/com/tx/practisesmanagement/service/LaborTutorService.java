package com.tx.practisesmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.LaborTutor;
import com.tx.practisesmanagement.model.Practise;
import com.tx.practisesmanagement.repository.LaborTutorRepository;

@Service
public class LaborTutorService {

	@Autowired LaborTutorRepository laborTutorRepository;
	@Autowired PractiseService practisesService;
	
	public LaborTutor save(LaborTutor laborTutor) {
		return laborTutorRepository.save(laborTutor);
	}
	
	public LaborTutor getById(String dni) {
		return laborTutorRepository.findById(dni).orElse(null);
	}
	
	public List<LaborTutor> getAll() {
		return laborTutorRepository.findAll();
	}
	
	public void remove(String dni) {

//		if (laborTutor.getBusiness().getTutors().size() == 1) {
		//			Esto al controlador			
//		}

		LaborTutor laborTutor = getById(dni);
		laborTutor.setBusiness(null);
		
		for (Practise currentPractise: laborTutor.getPractises()) {
			currentPractise.setLaborTutor(null);
			practisesService.save(currentPractise);
		}
		
		this.laborTutorRepository.deleteById(dni);
	}
	
	public LaborTutor updateLaborTutor(String dni, PersonDTO personData) {
		
		LaborTutor tutor = getById(dni);
		
		// Actualizamos los datos
		
		if (personData.getBirthDate() != null) {
			tutor.setBirthDate(personData.getBirthDate());
		}
		if (personData.getName() != null) {
			tutor.setName(personData.getName());
		}
		if (personData.getLastName() != null) {
			tutor.setLastName(personData.getLastName());
		}
		if (personData.getImage() != null) {
			tutor.setImage(personData.getImage());
		}
		if (personData.getTelefone() != null) {
			tutor.setTelefone(personData.getTelefone());
		}
		if (personData.getAddress() != null) {
			tutor.setAddress(personData.getAddress());
		}
		if (personData.getEmail() != null) {
			tutor.setEmail(personData.getEmail());
		}
		
		// Almacenamos los cambios
		
		return this.save(tutor);
	}
}
