package com.tx.practisesmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.LaborTutor;
import com.tx.practisesmanagement.repository.LaborTutorRepository;

@Service
public class LaborTutorService {

	@Autowired LaborTutorRepository laborTutorRepository;
	
	public LaborTutor save(LaborTutor laborTutor) {
		return laborTutorRepository.save(laborTutor);
	}
}
