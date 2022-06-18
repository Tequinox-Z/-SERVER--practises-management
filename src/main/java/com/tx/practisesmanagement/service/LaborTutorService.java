package com.tx.practisesmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.LaborTutor;
import com.tx.practisesmanagement.model.Practise;
import com.tx.practisesmanagement.model.Teacher;
import com.tx.practisesmanagement.repository.LaborTutorRepository;

@Service
/**
 * Servicio de tutores laborales
 * @author Salvador
 */
public class LaborTutorService {

	// Servicios
	
		@Autowired LaborTutorRepository laborTutorRepository;									
		@Autowired PractiseService practisesService;
	
	/**
	 * Guarda un tutor
	 * @param laborTutor: Nuevo tutor
	 * @return Tutor guardado
	 */
	public LaborTutor save(LaborTutor laborTutor) {
		return laborTutorRepository.save(laborTutor);
	}
	
	/**
	 * Obtiene un tutor por su id
	 * @param dni: Dni del tutor
	 * @return Tutor solicitado
	 */
	public LaborTutor getById(String dni) {
		return laborTutorRepository.findById(dni).orElse(null);
	}
	
	/**
	 * Obtiene todos los tutores
	 * @return Lista de tutores
	 */
	public List<LaborTutor> getAll() {
		return laborTutorRepository.findAll();
	}
	
	/**
	 * Obtiene tutores por su nombre
	 * @param name: Nombre del tutor
	 * @return: Lista de tutores
	 */
	public List<LaborTutor> getAllByName(String name) {
		return laborTutorRepository.getAllTutorByName(name.toUpperCase());
	}
	
	/**
	 * Borra un tutor
	 * @param dni DNi del tutor
	 */
	public void remove(String dni) {

		LaborTutor laborTutor = getById(dni);
		laborTutor.setBusiness(null);
		
		for (Practise currentPractise: laborTutor.getPractises()) {
			currentPractise.setLaborTutor(null);
			practisesService.save(currentPractise);
		}
		
		this.laborTutorRepository.deleteById(dni);
	}
	
	/**
	 * Actualiza un tutor
	 * @param dni: Dni del tutor
	 * @param personData Datos de la persona
	 * @return Persona actualizada
	 */
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
