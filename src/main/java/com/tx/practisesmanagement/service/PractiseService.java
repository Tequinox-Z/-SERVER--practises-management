package com.tx.practisesmanagement.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.LaborTutor;
import com.tx.practisesmanagement.model.Practise;
import com.tx.practisesmanagement.model.Teacher;
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
	 * Obtiene una práctica determinada
	 * @param id: Identificador de la práctica
	 * @return: Práctica solicitada
	 */
	public Practise get(Integer id) {
		return practiseRepository.findById(id).orElse(null);
	}
	
	/**
	 * Edita una practica
	 * @param practise Practica a editar
	 * @return Pratica edita
	 * @throws UserErrorException En caso de que no exista
	 */
	public Practise edit(Practise practise) throws UserErrorException {
		Practise practiseBBDD = practiseRepository.findById(practise.getId()).orElse(null);			// Lo obtenemos
		
		
		// Comprobamos si existe
		
		if (practiseBBDD != null) {
			
			
			// Si exisste comenzamos a establecer los nuevos datos
			
			if (practise.getStart() != null) {
				practiseBBDD.setStart(practise.getStart());
			}
			if (practise.getFinish() != null) {
				practiseBBDD.setFinish(practise.getFinish());
			}
			if (practise.getTeacher() != null) {
				practiseBBDD.setTeacher(practise.getTeacher());
			}
			if (practise.getLaborTutor() != null) {
				practiseBBDD.setLaborTutor(practise.getLaborTutor());
			}
			if (practise.getBusiness() != null) {
				practiseBBDD.setBusiness(practise.getBusiness());
			}
			
			return practiseRepository.save(practiseBBDD);			// Guardamos cambios
			
		}
		else {
			
			// SI no existe lo indicamos
			throw new UserErrorException("La práctica no existe");
		}

	}
	/**
	 * Obtiene una empresa desde una practica
	 * @param id Id de la practica
	 * @return Empresa asignada
	 */
	public Business getBusinessFromPractise(Integer id) {
		Practise currentPractise = get(id);
		return currentPractise.getBusiness();
	}
	
	/**
	 * Obtiene el profesor asignado a una practica
	 * @param id Id de la practica
	 * @return Profesor asignado
	 */
	public Teacher getTeacherFromPractise(Integer id) {
		return get(id).getTeacher();
	}
	
	/**
	 * Obtiene el tutor laboral establecido en la practica
	 * @param id Id de la practica
	 * @return Tutor laboral establecido
	 */
	public LaborTutor getLaborTutorFromPractise(Integer id) {
		return get(id).getLaborTutor();
	}
	
	/**
	 * Establece un profesor a la practica
	 * @param id Id de la practica
	 * @param teacher Profesor asignado
	 * @return Practica editada
	 */
	public Practise setTeacher(Integer id, Teacher teacher) {
		Practise practise = get(id);
		
		practise.setTeacher(teacher);
		
		return practiseRepository.save(practise);
	}
	
	/**
	 * Borra una praćtica
	 * @param practiseID: ID de la práctica
	 */
	public void remove(Integer practiseID) {
		Practise practise = get(practiseID);					// Obtenemos la práctica
		
		practise.setBusiness(null);									// Establecemos la empresa a nulo
		practise.setLaborTutor(null);
		practise.setTeacher(null);
		
		practiseRepository.save(practise);							// Almacenamos la práctica
		
		practiseRepository.delete(practise);						// Borramos la práctica
	}
	
	
	/**
	 * Guarda una practica
	 * @param practise Practica a guardar
	 * @return Practica editada
	 */
	public Practise save(Practise practise) {
		return practiseRepository.save(practise);
	}
	
	
	/**
	 * Comprueba si existe una practica
	 * @param id Id de la practica
	 * @return ¿Existe la practica?
	 */
	public boolean exist(Integer id) {
		return get(id) != null;
	}
	
	/**
	 * Actualiza una practica
	 * @param practise: Practica a editar
	 * @param practiseData: Datos de la practica
	 * @return Practica editada
	 */
	public Practise updatePractise(Practise practise, Practise practiseData) {
		if (practiseData.getStart() != null) {
			practise.setStart(practiseData.getStart());								// Actualizamos la fecha de inicio
		}
		if (practiseData.getFinish() != null) {
			practise.setFinish(practiseData.getFinish());							// Actualizamos la fecha de fin
		}
		
		return save(practise);														// Guardamos
	}
}
