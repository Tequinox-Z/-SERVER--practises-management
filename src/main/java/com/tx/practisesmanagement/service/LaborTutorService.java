package com.tx.practisesmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
	
	
	public void remove(String dni) {
//		
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
}
