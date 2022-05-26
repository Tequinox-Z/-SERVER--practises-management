package com.tx.practisesmanagement.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.Enrollment;
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
	
	public Practise edit(Practise practise) throws UserErrorException {
		Practise practiseBBDD = practiseRepository.findById(practise.getId()).orElse(null);
		
		if (practiseBBDD != null) {
			
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
			
			return practiseRepository.save(practiseBBDD);
			
		}
		else {
			throw new UserErrorException("La prácatica no existe");
		}

	}
	
	public Business getBusinessFromPractise(Integer id) {
		Practise currentPractise = get(id);
		return currentPractise.getBusiness();
	}
	
	public Teacher getTeacherFromPractise(Integer id) {
		return get(id).getTeacher();
	}
	
	public LaborTutor getLaborTutorFromPractise(Integer id) {
		return get(id).getLaborTutor();
	}
	
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

//		Al controlador
		
//		Enrollment enrollment = practise.getEnrollment();
//		enrollment.setPractise(null);
		
//		this.enrollmentService.save(enrollment);
		
		practiseRepository.save(practise);							// Almacenamos la práctica
		
		practiseRepository.delete(practise);						// Borramos la práctica
	}
	
	public Practise save(Practise practise) {
		return practiseRepository.save(practise);
	}
	
	public boolean exist(Integer id) {
		return get(id) != null;
	}
}
