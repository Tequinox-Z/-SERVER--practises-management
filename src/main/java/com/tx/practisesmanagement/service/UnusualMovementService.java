package com.tx.practisesmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.RegTemp;
import com.tx.practisesmanagement.model.UnusualMovement;
import com.tx.practisesmanagement.repository.UnusualMovementRepository;

@Service
/**
 * Servicio de movimientos inusuales
 * @author Salvador
 *
 */
public class UnusualMovementService {
	
	// Repositorios
	
		@Autowired UnusualMovementRepository unusualMovementRepository;
	
	
	/**
	 * Guarda un movimiento inusual	
	 * @param newUnusualMovement Nuevo movimiento inusual
	 * @return MOvimieno a guardar
	 */
	public UnusualMovement saveUnusualMovement(UnusualMovement newUnusualMovement) {
		newUnusualMovement.setDate(LocalDateTime.now());
		
		return this.unusualMovementRepository.save(newUnusualMovement);
	}
	
	/**
	 * Borra un movimiento inusual
	 * @param id Id del movimiento
	 */
	public void removeUnusualMovement(Integer id) {
		this.unusualMovementRepository.deleteById(id);
	}
	
	
}
