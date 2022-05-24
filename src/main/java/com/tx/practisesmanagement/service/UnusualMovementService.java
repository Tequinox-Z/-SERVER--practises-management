package com.tx.practisesmanagement.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.UnusualMovement;
import com.tx.practisesmanagement.repository.UnusualMovementRepository;

@Service
public class UnusualMovementService {
	
	@Autowired UnusualMovementRepository unusualMovementRepository;
	
	public UnusualMovement saveUnusualMovement(UnusualMovement newUnusualMovement) {
		newUnusualMovement.setDate(LocalDateTime.now());
		
		return this.unusualMovementRepository.save(newUnusualMovement);
	}
	
	public void removeUnusualMovement(Integer id) {
		this.unusualMovementRepository.deleteById(id);
	}
	
	
}
