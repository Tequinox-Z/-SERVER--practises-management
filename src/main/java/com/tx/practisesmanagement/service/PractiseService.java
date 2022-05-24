package com.tx.practisesmanagement.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.model.Practise;
import com.tx.practisesmanagement.repository.PractiseRepository;


/**
 * Servicio de prácticas
 * @author Salva
 */
@Service
public class PractiseService {
	
	// Repositorios
		@Autowired private PractiseRepository practiseRepository;
	
	/**
	 * Obtiene todas las prácticas
	 * @return Lista de prácticas
	 */
	public List<Practise> getAll() {
		return practiseRepository.findAll();
	}
	
	/**
	 * Obtiene una práctica determinada
	 * @param id: Identificador de la práctica
	 * @return: Práctica solicitada
	 */
	public Practise get(Integer id) {
		return practiseRepository.findById(id).orElse(null);
	}
	
	/**
	 * Borra una praćtica
	 * @param practiseID: ID de la práctica
	 */
	public void remove(Integer practiseID) {
		Practise practise = this.get(practiseID);					// Obtenemos la práctica
		practise.setBusiness(null);									// Establecemos la empresa a nulo
		
		practiseRepository.save(practise);							// Almacenamos la práctica
		
		practiseRepository.delete(practise);						// Borramos la práctica
		
	}
	
	
	public Practise save(Practise practise) {
		return practiseRepository.save(practise);
	}
}
