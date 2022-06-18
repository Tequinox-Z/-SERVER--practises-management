package com.tx.practisesmanagement.controller;



import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tx.practisesmanagement.component.SmtpMailSender;
import com.tx.practisesmanagement.dto.MessageDTO;
import com.tx.practisesmanagement.dto.NewEnrollmentDTO;
import com.tx.practisesmanagement.dto.NewPreferenceDTO;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.dto.UpdatePreferencesDTO;
import com.tx.practisesmanagement.enumerators.Rol;
import com.tx.practisesmanagement.error.RestError;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.ContactWorker;
import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.LaborTutor;
import com.tx.practisesmanagement.model.Location;
import com.tx.practisesmanagement.model.Practise;
import com.tx.practisesmanagement.model.Preference;
import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.RegTemp;
import com.tx.practisesmanagement.model.School;
import com.tx.practisesmanagement.model.Student;
import com.tx.practisesmanagement.model.Teacher;
import com.tx.practisesmanagement.model.UnusualMovement;
import com.tx.practisesmanagement.service.AdministratorService;
import com.tx.practisesmanagement.service.BusinessService;
import com.tx.practisesmanagement.service.ContactWorkerService;
import com.tx.practisesmanagement.service.EnrollmentService;
import com.tx.practisesmanagement.service.LaborTutorService;
import com.tx.practisesmanagement.service.LocationService;
import com.tx.practisesmanagement.service.PersonService;
import com.tx.practisesmanagement.service.PractiseService;
import com.tx.practisesmanagement.service.PreferenceService;
import com.tx.practisesmanagement.service.ProfessionalDegreeService;
import com.tx.practisesmanagement.service.RegTempService;
import com.tx.practisesmanagement.service.SchoolService;
import com.tx.practisesmanagement.service.StudentService;
import com.tx.practisesmanagement.service.TeacherService;
import com.tx.practisesmanagement.service.UnusualMovementService;

/**
 * Controlador de los recursos de Practises Management
 * @author Salva
 */
@CrossOrigin(origins = {"https://tequinox-z.github.io/", "http://localhost:4200", "http://localhost:43205"})
@RestController
public class AppController {

	// Servicios
	
		@Autowired private PersonService personService;								// Servicio de personas
		@Autowired private AdministratorService administratorService;   			// Servicio de administrador
		@Autowired private StudentService studentService;							// Servicio de estudiante
		@Autowired private TeacherService teacherService;							// Servicio de profesor
		@Autowired private SchoolService schoolService;								// Servicio de escuela
		@Autowired private ProfessionalDegreeService professionalDegreeService;		// Servicio de ciclos
		@Autowired private EnrollmentService enrollmentService;						// Servicio de matrículas
		@Autowired private PractiseService practiseService;							// Servicio de prácticas
		@Autowired private BusinessService businessService;							// Servicio de empresas
		@Autowired private ContactWorkerService contactWorkerService;
		@Autowired private LaborTutorService labprTutorService;
		@Autowired private LocationService locationService;
		@Autowired private PreferenceService preferenceService;
		@Autowired private RegTempService regTempService;
		@Autowired private UnusualMovementService unusualMovementService;
		@Autowired private LaborTutorService laborTutorService;
		
	// Encriptador de contraseña
		
		@Autowired private PasswordEncoder passwordEncoder;							
	
	// Email
		
		@Autowired private SmtpMailSender smtpMailSender;

	/**
	 * Muestra una bienvenida a Practises Management
	 * @return Mensaje
	 */
    @GetMapping("/") 
    public ResponseEntity showWelcome() {
    	return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Bienvenid@ a Practises Management"));	// Retornamos el mensaje
    }

	/**
	 * Permite obtener una persona mediante su token
	 * @return Persona solicitada
	 */
	@GetMapping("person")
    public ResponseEntity getPerson() {
        String dni = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		// Obtenemos su dni

        Teacher teacher = teacherService.get(dni);															// Lo buscamos en profesor

        if (teacher != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
    			new PersonDTO(teacher)																		// Si existe el profesor lo retornamos mediante el DTO de usuario
    		);
        }

        Administrator admin = administratorService.get(dni);												// Si no existe, lo buscamos en administrador

        if (admin != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
    			new PersonDTO(admin)																		// Si existe lo retornamos
    		);
        }

        Student student = studentService.get(dni);															// Si no existe lo buscamos en estudiantes

        if (student != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
    			new PersonDTO(student)																		// Si existe lo retornamos
    		);
        }
        
        LaborTutor tutor = laborTutorService.getById(dni);															// Si no existe lo buscamos en tutores

        if (tutor != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
    			new PersonDTO(tutor)																		// Si existe lo retornamos
    		);
        }

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			new RestError(HttpStatus.NOT_FOUND, "Persona no encontrada")									// Si el usuario no existe, lanzamos error
		);
    }
	
	/**
	 * Obtiene una persona por su dni
	 * @param dni Dni de la persona a buscar
	 * @return Persona dueña del dni
	 */
	@GetMapping("person/{dni}")
    public ResponseEntity getPersonByDni(@PathVariable String dni) {

        Teacher teacher = teacherService.get(dni);															// Lo buscamos en profesor

        if (teacher != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
    			new PersonDTO(teacher)																		// Si existe el profesor lo retornamos mediante el DTO de usuario
    		);
        }

        Administrator admin = administratorService.get(dni);												// Si no existe, lo buscamos en administrador

        if (admin != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
    			new PersonDTO(admin)																		// Si existe lo retornamos
    		);
        }

        Student student = studentService.get(dni);															// Si no existe lo buscamos en estudiantes

        if (student != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
    			new PersonDTO(student)																		// Si existe lo retornamos
    		);
        }
        
        LaborTutor tutor = laborTutorService.getById(dni);															// Si no existe lo buscamos en tutores

        if (tutor != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
    			new PersonDTO(tutor)																		// Si existe lo retornamos
    		);
        }

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			new RestError(HttpStatus.NOT_FOUND, "Persona no encontrada")									// Si el usuario no existe, lanzamos error
		);
    }
	
	
	/**
	 * Verifica si existe una persona o no mediante su dni
	 * @return Persona solicitada
	 */
	@GetMapping("exist-person/{dni}")
    public ResponseEntity getPerson(@PathVariable String dni) {

		dni = dni.toUpperCase();																				// Convertimos el dni en mayúsculas
		
        Teacher teacher = teacherService.get(dni);																// Lo buscamos en profesores

        if (teacher != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
            		new RestError(HttpStatus.OK, "La persona existe")											// Si existe lo indicamos
    		);
        }

        Administrator admin = administratorService.get(dni);													// Lo buscamos en profesores

        if (admin != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
            		new RestError(HttpStatus.OK, "La persona existe")											// Si existe lo indicamos
    		);
        }

        Student student = studentService.get(dni);																// Lo buscamos en estudiantes

        if (student != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
        		new RestError(HttpStatus.OK, "La persona existe")												// Si existe lo indicamos
    		);
        }
        
        LaborTutor tutor = laborTutorService.getById(dni);														// Lo buscamos en tutores
        
        if (tutor != null) {
    		return ResponseEntity.status(HttpStatus.OK).body(
        		new RestError(HttpStatus.OK, "La persona existe")												// Si existe lo indicamos
    		);
        }

		return ResponseEntity.status(HttpStatus.OK).body(
			new RestError(HttpStatus.OK, "")										// La persona no existe por lo que indicamos que está disponible
		);
    }

	/* ============================================= Administrador =============================================  */

	/* Primer nivel */

	/**
	 * Permite obtener todos los administradores
	 * @return Lista de administradores
	 */
	@GetMapping("administrator")
	public ResponseEntity getAdministrators() {
		List<Administrator> administrators = administratorService.getAll();						// Obtenemos los administradores
		
		return ResponseEntity.status(HttpStatus.OK).body(
				administrators																	// Retornamos el resultado
		);
	}

	/**
	 * Permite obtener un administrador mediante su dni
	 * @return Administrador solicitado
	 */
	@GetMapping("administrator/{dni}")
	public ResponseEntity getAdministrator(@PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		Administrator administrator = administratorService.get(dni);							// Obtenemos el administrador

		if (administrator == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El administrador no existe")			//  Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					administrator																// Retornamos el administrador
			);
		}
	}


	/**
	 * Permite editar un administrador
	 * @return Administrador editado
	 */
	@PutMapping("administrator/{dni}")
	public ResponseEntity updateAdministrator(@RequestBody PersonDTO administrator, @PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		Administrator currentAdministrator = administratorService.get(dni);												// Obtenemos el administrador
		
		if (currentAdministrator == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona no existe o no es un administrador")					// Si no existe lo indicamos
			);
		}

    	else if (administrator.getBirthDate() == null || administrator.getBirthDate().after(new Date())) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique una fecha de nacimiento válida"));			// Si no nos han pasado nombre lo indicamos
    	}	    	
    	else if (administrator.getName() == null || administrator.getName().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique nombre"));	// Si no nos han pasado nombre lo indicamos
    	}	    	
    	else if (administrator.getLastName() == null || administrator.getLastName().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique apellidos"));	// Si no nos han pasado nombre lo indicamos
    	}
    	else if (administrator.getEmail() == null || !administrator.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique un correo válido"));						// Si no nos han pasado un correo lo indicamos
    	}
    	else if (administrator.getTelefone() == null || administrator.getTelefone().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un teléfono"));						// Si no nos han pasado un correo lo indicamos
    	}
    	else if (administrator.getAddress() == null || administrator.getAddress().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó una dirección"));						// Si no nos han pasado un correo lo indicamos
    	}
		
		if (administrator.getPassword() != null) {
			currentAdministrator.setPassword(passwordEncoder.encode(administrator.getPassword()));								// Codificamos la contraseña
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				administratorService.updateAdministrator(currentAdministrator.getDni(), administrator)							// Actualizamos el administrador y devolvemos el resultado
		);
	}
	
	/**
	 * Permite borrar un administrador
	 * @return Administrador borrado
	 */
	@DeleteMapping("administrator/{dni}")
	public ResponseEntity deleteAdministrator(@PathVariable String dni) {
		dni = dni.toUpperCase();
		
		if (administratorService.get(dni) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona a borrar no existe o no es un administrador")		// Si no existe lo indicamos
			);
		}
		
		administratorService.deleteAdministrator(dni);																// Borramos el administrador
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();												// Devolvemos el código de estado
	}
	
	
	/* Segundo nivel */

	/**
	 * Obtiene la escuela que administra un administrador
	 * @param dni: Dni del administrador
	 * @return	Escuela del administrador
	 */
	@GetMapping("administrator/{dni}/school")
	public ResponseEntity getSchoolFromAdministrator(@PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		Administrator currentAdministrator = administratorService.get(dni);													// Obtenemos el administrador

		if (currentAdministrator == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "La persona no existe o no cuenta con privilegios de administrador")	// Si no existe o no es administrador lo indicamos
			);
		}
		else if (currentAdministrator.getSchoolSetted() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "Aún no administra ninguna escuela")									// Si no tiene escuela lo indicamos
			);
		}

		currentAdministrator.getSchoolSetted().setPassword("");
		
		return ResponseEntity.status(HttpStatus.OK).body(currentAdministrator.getSchoolSetted());							// Devolvemos la escuela
	}
	
	/**
	 * Establece el centro a un administrador
	 * @param dni Dni del administrador
	 * @param school Escuela a asignar
	 * @return Escuela asignada
	 */
	@PostMapping("administrator/{dni}/school")
	public ResponseEntity addSchoolToAdministrator(@PathVariable String dni, @RequestBody School school) {
		
		dni = dni.toUpperCase();																									// Convertimos el dni en maýusculas
		
		Administrator currentAdministrator = administratorService.get(dni);															// Obtenemos el administrador


		if (currentAdministrator == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona no existe o no cuenta con privilegios de administrador")			// Comprobamos si existe
			);
		}
		
		// Comprobamos si administra una escuela
		
		if (currentAdministrator.getSchoolSetted() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Ya administra una escuela")	
			);
		}
		
		// Comprobamos si no shan indicado un id de escuela
		
		if (school.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Debe indicar el id de la escuela")	
			);
		}
		
		School schoolFromDB = schoolService.get(school.getId());																	// Obtenemos la escuela
		
		// Comprobamos si existe
		
		if (schoolFromDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La escuela con id " + school.getId() + " no existe")	
			);
		}
		else if (school.getPassword() == null || !school.getPassword().equals(schoolFromDB.getPassword())) {
			
			// Compromabamos la contraseña
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Contraseña incorrecta")	
			);
		}
		
		
		
		currentAdministrator.setSchoolSetted(schoolFromDB);																// Establecemos la escuela
		
		administratorService.save(currentAdministrator);																// Guardamos cambios
		
		return ResponseEntity.status(HttpStatus.CREATED).body(schoolFromDB);											// Retornamos el resultado
		
	}
	
	
	/**
	 * Quita una escuela a un administador
	 * @param dni Dni del administrador
	 * @return
	 */
	@DeleteMapping("administrator/{dni}/school")
	public ResponseEntity quitSchoolToAdministrator(@PathVariable String dni) {
		
		dni = dni.toUpperCase();																			// Convertimos el dni en maýusculas
		
		Administrator currentAdministrator = administratorService.get(dni);								 	// Obtenemos el adminsitrador

		// Comprobamos si existe
		
		if (currentAdministrator == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona no existe o no cuenta con privilegios de administrador")	
			);
		}
		
		// Comproabmos si tiene escuela asignada
		
		if (currentAdministrator.getSchoolSetted() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "No hay ninguna escuela asignada")	
			);
		}
		
		
		
		Integer schoolId = currentAdministrator.getSchoolSetted().getId();					// Obtenemos el id de la escuela
		
		currentAdministrator.setSchoolSetted(null);											// Establecemos la escuela a nulo
		
		administratorService.save(currentAdministrator);									// Guardamos el administrador
		
		
		if (schoolService.get(schoolId).getAdministrators().isEmpty()) {
			schoolService.removeSchool(schoolId);											// Si no hay administradores que administren el centro borramos el centro
		}
		
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();						// Indicamos el codigo de borrado
	}


	/* ============================================= Tutor laboral ======================================== */
	
	/**
	 * Obtiene todos los tutores laborales
	 * @param name Opcional: Nombre de los tutores
	 * @return Lista de tutores laborales
	 */
	@GetMapping("labor-tutor")
	public ResponseEntity getTutors(@RequestParam(required = false) String name) {
		List<LaborTutor> tutors;														// Obtenemos los tutores 
		
		if (name == null) {
			tutors = laborTutorService.getAll();										// Si no nos han inidcado nobre retornamos todo
		}
		else {
			tutors = laborTutorService.getAllByName(name);								// SI no buscamos por nombre
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				tutors																	// Retornamos el resultado
		);
	}

	/**
	 * Permite obtener un tutor mediante su dni
	 * @return Tutot solicitado
	 */
	@GetMapping("labor-tutor/{dni}")
	public ResponseEntity getLaborTutor(@PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		LaborTutor laborTutor = laborTutorService.getById(dni);							// Obtenemos el administrador

		if (laborTutor == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")			//  Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					laborTutor																// Retornamos el administrador
			);
		}
	}


	/**
	 * Permite editar un tutor
	 * @return Administrador editado
	 */
	@PutMapping("labor-tutor/{dni}")
	public ResponseEntity updateTUtor(@RequestBody PersonDTO tutor, @PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		LaborTutor currentTutor = laborTutorService.getById(dni);												// Obtenemos el administrador
		
		if (currentTutor == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona no existe o no es un administrador")					// Si no existe lo indicamos
			);
		}

    	else if (tutor.getBirthDate() == null || tutor.getBirthDate().after(new Date())) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique una fecha de nacimiento válida"));			// Si no nos han pasado nombre lo indicamos
    	}	    	
    	else if (tutor.getName() == null || tutor.getName().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique nombre"));	// Si no nos han pasado nombre lo indicamos
    	}	    	
    	else if (tutor.getLastName() == null || tutor.getLastName().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique apellidos"));	// Si no nos han pasado nombre lo indicamos
    	}
    	else if (tutor.getEmail() == null || !tutor.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique un correo válido"));						// Si no nos han pasado un correo lo indicamos
    	}
    	else if (tutor.getTelefone() == null || tutor.getTelefone().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un teléfono"));						// Si no nos han pasado un correo lo indicamos
    	}
    	else if (tutor.getAddress() == null || tutor.getAddress().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó una dirección"));						// Si no nos han pasado un correo lo indicamos
    	}
		
		if (tutor.getPassword() != null) {
			currentTutor.setPassword(passwordEncoder.encode(tutor.getPassword()));																	// Codificamos la contraseña
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				laborTutorService.updateLaborTutor(currentTutor.getDni(), tutor)							// Actualizamos el administrador y devolvemos el resultado
		);
	}
	
	/**
	 * Permite borrar un administrador
	 * @return Administrador borrado
	 */
	@DeleteMapping("labor-tutor/{dni}")
	public ResponseEntity deleteLaborTutor(@PathVariable String dni) {
		dni = dni.toUpperCase();																		// Convertimos el dni en maýusculas
		
		if (laborTutorService.getById(dni) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona a borrar no existe o no es un tutor")		// Si no existe lo indicamos
			);
		}
		
		laborTutorService.remove(dni);																// Borramos el tutor
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();												// Devolvemos el código de estado
	}
	
	
	/* Segundo nivel */
	
	/**
	 * Obtiene las practicas de un tutor laboral
	 * @param dni Dni del tutor laboral
	 * @return Lista de practicas
	 */
	@GetMapping("labor-tutor/{dni}/practise")
	public ResponseEntity getPractisesFromLaborTutor(@PathVariable String dni) {
		
		dni = dni.toUpperCase();														// Convertimos el dni en maýusculas
		
		LaborTutor laborTutor = laborTutorService.getById(dni);							// Obtenemos el tutor

		if (laborTutor == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")			//  Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					laborTutor.getPractises()											// Retornamos las practicas
			);
		}
	}

	/**
	 * Obtiene la empresa de un tutor laboral
	 * @param dni Dni del tutor
	 * @return Empresa asignada
	 */
	@GetMapping("labor-tutor/{dni}/business")
	public ResponseEntity getBusinessFromLaborTutor(@PathVariable String dni) {
		
		dni = dni.toUpperCase();														// Convertimos el dni en maýusculas
		
		LaborTutor laborTutor = laborTutorService.getById(dni);							// Obtenemos el tutor

		if (laborTutor == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")			//  Si no existe lo indicamos
			);
		}
		else if (laborTutor.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Empresa no establecida")			//  Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					laborTutor.getBusiness()												// Retornamos la empresa
			);
		}
	}
	
	/**
	 * Establece la empresa de un tutor laboral
	 * @param dni DNi dle tutor
	 * @param business Empresa a asignar
	 * @return Empresa asignada
	 */
	@PostMapping("labor-tutor/{dni}/business")
	public ResponseEntity setBusinessFromLaborTutor(@PathVariable String dni, @RequestBody Business business) {
		
		dni = dni.toUpperCase();														// Convertimos el dni en maýusculas
		
		LaborTutor laborTutor = laborTutorService.getById(dni);							// Obtenemos el tutor

		if (laborTutor == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")			//  Si no existe lo indicamos
			);
		}
		else if (laborTutor.getBusiness() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Empresa ya establecida")			//  Si no existe lo indicamos
			);
		}
		else if (business.getCif() == null || business.getCif().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el cif de la empresa")			//  Si no existe lo indicamos
			);
		}
		
		Business businessDB = businessService.get(business.getCif());
		
		if (businessDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")			//  Si no existe lo indicamos
			);
		}
		
		laborTutor.setBusiness(businessDB);												// Establecemos la empresa
		laborTutorService.save(laborTutor);												// Guardamos
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				businessDB																// Retornamos la empresa
		);
	}
	
	/**
	 * Quita la empresa de un tutor laboral
	 * @param dni Dni del tutor
	 * @return sin contenido
	 */
	@DeleteMapping("labor-tutor/{dni}/business")
	public ResponseEntity quitBusinessFromLaborTutor(@PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		LaborTutor laborTutor = laborTutorService.getById(dni);							// Obtenemos el administrador

		if (laborTutor == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")			//  Si no existe lo indicamos
			);
		}
		else if (laborTutor.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Empresa no establecida")			//  Si no existe lo indicamos
			);
		}
		
		laborTutor.setBusiness(null);
		laborTutorService.save(laborTutor);
		
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	/* ============================================= Profesor =============================================  */

	/* Primer nivel */

	/**
	 * Permite obtener todos los profesores
	 * @return Lista de profesores
	 */
	@GetMapping("teacher")
	public ResponseEntity getTeachers(@RequestParam(required = false) String name) {
		List<Teacher> teachers;																		// Inicializamos la lista de los profesores
		
		if (name == null) {
			teachers = teacherService.getAll();														// Si no nos han indicado nada los obtenemos todos
		}
		else {
			teachers = teacherService.getAllByName(name);											// SI nos indican nombre buscamos por nombre
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
			teachers																				// Si hay retornamos los mismos
		);
	}

	/**
	 * Permite obtener un profesor por dni
	 * @return Profesor solicitado
	 */
	@GetMapping("teacher/{dni}")
	public ResponseEntity getTeacher(@PathVariable String dni) {
		dni = dni.toUpperCase();
		Teacher teacher = teacherService.get(dni);															// Buscamos el profesor

		if (teacher == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El profesor no existe")							// Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					teacher																					// Si existe lo retornamos
			);
		}
	}

	/**
	 * Permite editar un administrador
	 * @return Administrador editado
	 */
	@PutMapping("teacher/{dni}")
	public ResponseEntity updateTeacher(@RequestBody PersonDTO teacher, @PathVariable String dni) {

		dni = dni.toUpperCase();																		// Convertimos el dni en maýusculas
		
		Teacher currentTeacher = teacherService.get(dni);												// Obtenemos el profesor
		
		if (currentTeacher == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona no existe o no es un profesor")					// Si no existe lo indicamos
			);
		}
    	else if (teacher.getBirthDate() == null || teacher.getBirthDate().after(new Date())) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique una fecha de nacimiento válida"));			// Si no nos han pasado nombre lo indicamos
    	}	    	
    	else if (teacher.getName() == null || teacher.getName().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique nombre"));	// Si no nos han pasado nombre lo indicamos
    	}	    	
    	else if (teacher.getLastName() == null || teacher.getLastName().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique apellidos"));	// Si no nos han pasado nombre lo indicamos
    	}
    	else if (teacher.getEmail() == null || !teacher.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique un correo válido"));						// Si no nos han pasado un correo lo indicamos
    	}
    	else if (teacher.getTelefone() == null || teacher.getTelefone().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un teléfono válido"));						// Si no nos han pasado un correo lo indicamos
    	}
    	else if (teacher.getAddress() == null || teacher.getAddress().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó una dirección"));						// Si no nos han pasado un correo lo indicamos
    	}
		
		if (teacher.getPassword() != null) {
			currentTeacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
		}

		return ResponseEntity.status(HttpStatus.OK).body(
			teacherService.updateTeacher(currentTeacher, teacher)										// Si todo está correcto actualizamos el profesor y lo retornamos
		);
	}

	/**
	 * Permite borrar un profesor
	 * @return Profesor borrado
	 */
	@DeleteMapping("teacher/{dni}")
	public ResponseEntity deleteTeacher(@PathVariable String dni) {
		dni = dni.toUpperCase();																						// Convertimos el dni en maýusculas
		
		if (teacherService.get(dni) == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "La persona a borrar no existe o no es un profesor")				// Si el profesor no existe o no es profesor lo indicamos
			);
		}
		
		if (teacherService.get(dni).getRol() == Rol.ROLE_ADMIN ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "No se puede eliminar un administrador desde profesores")		// Si intenta borrar un administrador		
			);
		}
		else {
			Teacher teacher = teacherService.get(dni);																// Obtenemos el profesor
			
			
			// Quitamos el profesor de todas las practicas
			
			for (Practise currentPractise : teacher.getPractises()) {
				currentPractise.setTeacher(null);
				practiseService.save(currentPractise);
			}
			
			teacherService.deleteTeacher(dni);																				// Borramos el profesor			
		}
		
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();													// Indicamos el código de estado 204
	}

	
	/* Segundo nivel */


	/**
	 * Obtiene los ciclos correspondientes a un profesor concreto
	 * @param dni: Identificador del profesor
	 * @return Ciclos del profesor
	 */
	@GetMapping("teacher/{dni}/degree")
	public ResponseEntity getDegrees(@PathVariable String dni) {
		dni = dni.toUpperCase();
		
		List<ProfessionalDegree> degrees = new ArrayList<>();
		
		Teacher currentTeacher = teacherService.get(dni);												// Obtenemos el profesor

		if (currentTeacher == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "El profesor no existe")						// Si no existe lanzamos error
			);
		}
		
		// Obtenemos la fecha
		
		Calendar date = Calendar.getInstance();
		
		// Por cada ciclo nos quedamos con los que sean de este año
		
		for (ProfessionalDegree currentDegree : currentTeacher.getProfessionalDegrees()) {
			
			if (currentDegree.getYear() == date.get(Calendar.YEAR)) {
				degrees.add(currentDegree);
			}
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				degrees																				// Retornamos los ciclos
		);
	}

	/**
	 * Obtiene la practicas en las que está asignada un profesor
	 * @param dni Dni del profesor
	 * @return Lista de practicas
	 */
	@GetMapping("teacher/{dni}/practise")
	public ResponseEntity getPractises(@PathVariable String dni) {
		dni = dni.toUpperCase();																	// Convertimos el dni en maýusculas
			
		Teacher currentTeacher = teacherService.get(dni);												// Obtenemos el profesor
		

		if (currentTeacher == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "El profesor no existe")						// Si no existe lanzamos error
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
			currentTeacher.getPractises()														// Obtenemos los ciclos y lo devolvemos
		);
	}
	
	
	/* ===============================================  Estudiante  =============================================  */

	/**
	 * Obtiene todos los estudiantes
	 * @param name (Opcional) Busca los esudiantes por nombre
	 * @return Lista de estudiantes
	 */
	@GetMapping("student")
	public ResponseEntity getStudents(@RequestParam(required = false) String name) {
		List<Student> students;																	// Obtenemos los estudiantes

		if (name == null) {
			 students = studentService.getAll();												// Si no no indican nombre obtenemos todos
		}
		else {
			 students = studentService.getAllByName(name);										// SI no buscamos por nombre
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
			students																				// Si hay retornamos los mismos
		);
	}

	/**
	 * Permite obtener un estudiante por dni
	 * @return Estudiante solicitado
	 */
	@GetMapping("student/{dni}")
	public ResponseEntity getStudent(@PathVariable String dni) {
		dni = dni.toUpperCase();
		Student student = studentService.get(dni);															// Buscamos el profesor

		if (student == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El estudiante no existe")							// Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					student																					// Si existe lo retornamos
			);
		}
	}
	/**
	 * Borra un estudiantes
	 * @param dni Dni del estudiante
	 * @return Estudiante borrado
	 */
	@DeleteMapping("student/{dni}")
	public ResponseEntity deleteStudent(@PathVariable String dni) {
		dni = dni.toUpperCase();																			// Convertimos el dni en maýusculas
		Student student = studentService.get(dni);															// Buscamos el profesor

		if (student == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El estudiante no existe")							// Si no existe lo indicamos
			);
		}
		else {
			studentService.deleteStudent(dni);																// Si existe lo borramos
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	/**
	 * Obtiene las matriculaciones de un estudiante
	 * @param dni DNi del estudiante
	 * @return Lista de matriculaciones
	 */
	@GetMapping("student/{dni}/enrollment")
	public ResponseEntity getEnrollmentsFromStudent(@PathVariable String dni) {
		dni = dni.toUpperCase();
		Student student = studentService.get(dni);															// Buscamos el profesor

		if (student == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El estudiante no existe")							// Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					student.getEnrollments()																			// Si existe lo retornamos
			);
		}
	}
	
	/**
	 * Edita un estudiante
	 * @param student Datos del estudiante a editar
	 * @param dni Dni del estudiante
	 * @return Estudiate editado
	 */
	@PutMapping("student/{dni}")
	public ResponseEntity updateStudent(@RequestBody PersonDTO student, @PathVariable String dni) {
		
		dni = dni.toUpperCase();																						// Convertimos el dni en maýusculas
		
		Student currentStudent = studentService.get(dni);																// Obtenemos el administrador
		
		if (currentStudent == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona no existe o no es un administrador")					// Si no existe lo indicamos
			);
		}

    	else if (student.getBirthDate() == null || student.getBirthDate().after(new Date())) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique una fecha de nacimiento válida"));			// Si no nos han pasado nombre lo indicamos
    	}	    	
    	else if (student.getName() == null || student.getName().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique nombre"));	// Si no nos han pasado nombre lo indicamos
    	}	    	
    	else if (student.getLastName() == null || student.getLastName().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique apellidos"));	// Si no nos han pasado nombre lo indicamos
    	}
    	else if (student.getEmail() == null || !student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique un correo válido"));						// Si no nos han pasado un correo lo indicamos
    	}
    	else if (student.getTelefone() == null || student.getTelefone().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un teléfono"));						// Si no nos han pasado un correo lo indicamos
    	}
    	else if (student.getAddress() == null || student.getAddress().trim().length() == 0) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó una dirección"));						// Si no nos han pasado un correo lo indicamos
    	}
		
		
		if (student.getPassword() != null) {
			currentStudent.setPassword(passwordEncoder.encode(student.getPassword()));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				studentService.updateStudent(currentStudent, student)							// Actualizamos el estudiante y devolvemos el resultado
		);
	}
	

	/* ===============================================  Escuela  =============================================  */

	/* Primer nivel */

	/**
	 * Obtiene todas las escuelas
	 * @param name: Si queremos obtener ciclos por nombre indicamos este parámetro
	 * @return Lista de escuelas
	 */
	@GetMapping("school")
	public ResponseEntity getAllSchools(@RequestParam(required = false) String name) {

		List<School> schools;																// Iniciaalizamos la lista de centros
				
		if (name != null) {
			schools = schoolService.getAllByName(name);										// Si no nos han indicado nombre los obtenemos por nombre
		}
		else {
			schools = schoolService.getAll();												// Si no obtenemos todos los centros
			
			for (School school : schools) {
				school.setPassword("");														// EN este caso limpiaremos la contraseña de estos
			}
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(schools);						// Retornamos los centros 
	}

	/**
	 * Permite obtener una escuela determinada
	 * @return Escuela solicitada
	 */
	@GetMapping("school/{idSchool}")
	public ResponseEntity getSchool(@PathVariable Integer idSchool) {
		School school = schoolService.get(idSchool);											// Obtenemos la escuela

		if (school == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El centro no existe")					// Si no existe lo indicamos
			);
		}
		else {
			school.setPassword("");
			return ResponseEntity.status(HttpStatus.OK).body(school);							// Si existe lo retornamos
		}
	}
	/**
	 * Permite obtener los administradores de una escuela
	 * @param idSchool Id de la escuela
	 * @return Lista de administradores del centro
	 */
	@GetMapping("school/{idSchool}/administrator")
	public ResponseEntity getAdministratorsOfSchool(@PathVariable Integer idSchool) {
		School school = schoolService.get(idSchool);											// Obtenemos la escuela

		if (school == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El centro no existe")					// Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(school.getAdministrators());							// Si existe lo retornamos
		}
	}

	/**
	 * Permite crear una nueva escuela
	 * @param school: Objeto escuela con los datos del nuevo centro a crear
	 * @return Centro creado
	 */
	@PostMapping("school")
	public ResponseEntity addSchool(@RequestBody School school) {
		
		if (school.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre para el centro")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getAddress() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la dirección al centro")					// Si no nos indican dirección lo indicamos
			);
		}
		else if (school.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique imagen del centro")					// Si no nos indican la imagen lo avisamos
			);
		}
		else if (school.getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique una contraseña")					// Si no nos indican contraseña lo avisamos
			);
		}
		else if (school.getOpeningTime() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique hora de apertura")					// Si no nos indican el hora de apertura lo avisamos
			);
		}
		else if (school.getClosingTime() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique hora de cierre")					// Si no nos indican hora de cierre lo avisamos
			);
		}
		else if (school.getOpeningTime().after(school.getClosingTime())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Horario no válido")					// Si nos indicand horario incorrecto lo avisamos
			);
		}
		
		school.setId(null);																		// Establecemos el id a nulo
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				schoolService.save(school)														// Guardamos el centro
		);
	}
	
	/**
	 * Edita un centro
	 * @param school Datos del centro
	 * @param id Id del centro
	 * @return Centro editado
	 */
	@PutMapping("school/{id}")
	public ResponseEntity editSchool(@RequestBody School school, @PathVariable Integer id) {
		
		School currentSchool = schoolService.get(id);													// Obtenemos el centro
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")					// Si no existe lo avisamos
			);
		}
		else if (school.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre para el centro")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getAddress() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(	
					new RestError(HttpStatus.BAD_REQUEST, "Indique la dirección al centro")					// Si no nos indican dirección al centro lo indicamos
			);
		}
		else if (school.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique imagen del centro")					// Si no nos indican la imagen lo avisamos
			);
		}
		else if (school.getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique una contraseña")					// Si no nos indican contraseña lo avisamos
			);
		}
		else if (school.getOpeningTime() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique hora de apertura")					// Si no nos indican la hora de apertura lo avisamos
			);
		}
		else if (school.getClosingTime() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique hora de cierre")					// Si no nos indican hora de cierre lo avisamos
			);
		}
		else if (school.getOpeningTime().after(school.getClosingTime())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Horario no válido")					// Si no nos indican un horario válido lo avisamos
			);
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(
				schoolService.updateSchool(school, currentSchool.getId())						// actualizamos el centro
		);
	}
	
	
	/**
	 * Borra una determinada escuela
	 * @param idSchool: Identificador de la escuela
	 */
	@DeleteMapping("school/{idSchool}")
	public ResponseEntity deleteSchool(@PathVariable Integer idSchool) {

		School currentSchool = schoolService.get(idSchool);													// Obtenemos la escuela
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Escuela no encontrada")							// Si no existe lo indicamos
			);
		}
		
		administratorService.quitAdministratorsFromSchool(currentSchool);									// Quitamos el administrador
		
		schoolService.removeSchool(idSchool);																// Borramos la escuela
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();										// Devolvemos un 204
	}
	
	/* Segundo nivel */
	
	
	// ================================================ Registros de temperatura ================================================
	
	/**
	 * Obtiene todos los registros de temperatura
	 * @param idSchool Id del centro
	 * @return Lista de registros de temperatura
	 */
	@GetMapping("school/{idSchool}/reg-temp")
	public ResponseEntity getRegTempsByDate(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);												// Obtenemos la escuela
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
			return ResponseEntity.status(HttpStatus.OK).body(
					schoolService.getRegTempsByDate(idSchool, new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())		// Retornamos los registros de temperatura de hoy
			);
	}
	
	/**
	 * Borra todos los registros de temperatura
	 * @param idSchool Id del centro
	 * @return Sin contenido
	 */
	@DeleteMapping("school/{idSchool}/reg-temp")
	public ResponseEntity removeAllRegTemps(@PathVariable Integer idSchool) {
		
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		
		schoolService.removeAllRegTempByIdSchool(idSchool);
		
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/**
	 * Añade un registro de temperatura
	 * @param idSchool Id del centro
	 * @param newRegTemp Nuevo registro
	 * @return Registro almacenado
	 */
	@PostMapping("school/{idSchool}/reg-temp")
	public ResponseEntity getRegTemps(@PathVariable Integer idSchool, @RequestBody RegTemp newRegTemp) {
		
		School currentSchool = schoolService.get(idSchool);												// Obtenemos el centro
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		
		if (newRegTemp.getHumidity() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique humedad")							// Si no existe la humedad lo indicamos
			);
		}
		else if (newRegTemp.getCelcius() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la temperatura en Celcius")							// Si no existe la temperatura en c lo indicamos
			);
		}
		else if (newRegTemp.getFahrenheit() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la temperatura en Fahrenheit")							// Si no existe la temperatura en f lo indicamos
			);
		}
		else if (newRegTemp.getHeatIndexc() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique índice de calor en Celcius")							// Si no existe la temperatura en indice de calor indicamos
			);
		}
		else if (newRegTemp.getHeatIndexf() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique índice de calor en Fahrenheit")							// Si no existe el indice de calor en f lo indicamos
			);
		}
		
		newRegTemp.setId(null);																							// Establecemos el id a nulo
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				schoolService.addRegTemp(currentSchool, newRegTemp)														// Añadimos y retornamos el resultado
		);
		
	}
	
	
	// ================================================ Movimientos inusuales ================================================
	
	/**
	 * Obtiene todos los movimientos inusuales
	 * @param idSchool Id del centro
	 * @return Lista de movimientos inusuales
	 */
	@GetMapping("school/{idSchool}/movement")
	public ResponseEntity getUnusualMovements(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);												// Obtenemos la escuela
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				currentSchool.getUnusualsMovements()													// Si existe retornamos los movimientos
		);
	}
	
	/**
	 * Añade un nuevo movimiento al centro
	 * @param idSchool: Id del centro
	 * @param unusualMovement Movimiento a añadir
	 * @return Movimiento resultante
	 */
	@PostMapping("school/{idSchool}/movement")
	public ResponseEntity getUnusualMovements(@PathVariable Integer idSchool, @RequestBody UnusualMovement unusualMovement) {
		School currentSchool = schoolService.get(idSchool);												// Obtenemos el centro
			
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		unusualMovement.setId(null);																	// Establecemos el id a nulo
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
			schoolService.addUnusualMovement(currentSchool, unusualMovement)							// Añadimos un nuevo movimiento inusual
		);
	}
	
	/**
	 * Borra todos los movimientos inusuales 
	 * @param idSchool Id del centro
	 * @return Sin contenido
	 */
	@DeleteMapping("school/{idSchool}/movement")
	public ResponseEntity deleteUnusualMovements(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);												// Obtenemos el centro
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		schoolService.removeAllUnsualsMovements(idSchool);												// Borramos todos los movimientos
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();									
	}
	
	
	
	// ================================================ Localización ================================================

	/**
	 * Obtiene la localización de un centro
	 * @param idSchool Id del centro
	 * @return Localización
	 */
	@GetMapping("school/{idSchool}/location")
	public ResponseEntity getLocation(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		
		if (currentSchool.getLocation() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Localización no establecida")							// Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					currentSchool.getLocation()																	// Retornamos la localización
			);
		}

	}
	
	/**
	 * Establece la localización del centro
	 * @param idSchool Id del centro
	 * @param location Localización 
	 * @return Localización establecida
	 */
	@PostMapping("school/{idSchool}/location")
	public ResponseEntity setLocation(@PathVariable Integer idSchool, @RequestBody Location location) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		if (currentSchool.getLocation() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "La localización ya está establecida")			// Si ya existe lo indicamos					
			);
		}
		else {
			if (location.getLatitude() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "Indique una latitud válida")							// Si la latitud no es válida lo indicamos
				);
			}
			else if (location.getLongitude() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "Indique una longitud válida")						// SI la longitud no es válida lo indicamos
				);
			}
			
			location.setId(null);																					// Establecemos el id de la localización en nulo
			
			
			return ResponseEntity.status(HttpStatus.CREATED).body(
					schoolService.setLocation(idSchool, location)													// Establecemos y retornamos el resultado
			);
			
		}
	}
	/**
	 * Edita la localización del centro
	 * @param idSchool Id del centro
	 * @param location Nueva localización
	 * @return Localización actualizada
	 */
	@PutMapping("school/{idSchool}/location")
	public ResponseEntity editLocation(@PathVariable Integer idSchool, @RequestBody Location location) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		if (currentSchool.getLocation() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Localización no establecida")							// Si no existe lo indicamos
			);
		}
		else {
			if (location.getLatitude() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "Indique una latitud válida")						// Si la latitud no es válida lo indicamos	
				);
			}
			else if (location.getLongitude() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "Indique una longitud válida")					// SI la longitud no es válida lo indicamos
				);
			}
			
			Location locationUpdated;																			// Creamos una nueva localización

			try {				
				locationUpdated = locationService.editLocation(currentSchool.getLocation().getId(), location.getLongitude(), location.getLatitude());  // Editamos la localización
			}
			catch (Exception e) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(
						new RestError(HttpStatus.CONFLICT, e.getMessage())										// En caso de error retornamos el mensaje
				);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(
					locationUpdated																				// SI se ha editado correctamente retornamos el resultado
			);
		}
		
	}
	
	/**
	 * Borra una localización 
	 * @param idSchool Id del centro
	 * @param location Localización del centro
	 * @return Sin contenido
	 */
	@DeleteMapping("school/{idSchool}/location")
	public ResponseEntity removeLocation(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);														// Obtenemos el centro
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		if (currentSchool.getLocation() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Localización no establecida")							// Si no existe lo indicamos
			);
		}
		
		schoolService.removeLocation(idSchool);																	// Borramos la localización
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();											
	}
	
	
	
	// ================================================ Ciclos ================================================

	/**
	 * Obtiene todos los ciclos de un centro
	 * @param year (Opcional) Año del que obtener los ciclos
	 * @param idSchool Id del centro
	 * @return Lista de ciclos
	 */
	@GetMapping("school/{idSchool}/degree")
	public ResponseEntity getDegree(@RequestParam(required = false) Integer year,  @PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);														// Obtenemos el centro
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		if (year != null) {
			return ResponseEntity.status(HttpStatus.OK).body(
				professionalDegreeService.getAllProfessionalDegreesByYear(idSchool, year)						// SI nos han indicado año los obtenemos por año
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
				schoolService.getAllProfessionalDegrees(idSchool)												// Si no, retornamos todos los ciclos
			);			
		}
		
	}
	/**
	 * Añade un nuevo ciclo al centro
	 * @param idSchool Id del centro
	 * @param degree Nuevo ciclo
	 * @return Ciclo actualizado
	 */
	@PostMapping("school/{idSchool}/degree")
	public ResponseEntity addDegree(@PathVariable Integer idSchool, @RequestBody ProfessionalDegree degree) {
		School currentSchool = schoolService.get(idSchool);														// Obtenemos el centro
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		
		if (degree.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre")									// Si no existe el nombre lo indicamos
			);
		}
		else if (degree.getYear() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un año")										// Si no existe el año lo indicamos
			);
		}
		else if (degree.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la imagen del ciclo")						// Si no existe la imagen lo indicamos
			);
		}
		
		degree.setId(null);																						// Establecemos el id a nulo
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				schoolService.addDegreeToSchool(idSchool, degree)												// Añadimos el ciclo al centro y retornamos el resultado
		);
	}
	
	/**
	 * Edita un ciclo de un centro
	 * @param idSchool Id del centro
	 * @param degree CIclo a editar
	 * @param idDegree Id del ciclo
	 * @return Ciclo editado
	 */
	@PutMapping("school/{idSchool}/degree/{idDegree}")
	public ResponseEntity editDegree(@PathVariable Integer idSchool, @RequestBody ProfessionalDegree degree, @PathVariable Integer idDegree) {
		School currentSchool = schoolService.get(idSchool);														// Obtenemos el centro
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		ProfessionalDegree pf = professionalDegreeService.get(idDegree);										// Obtenemos el ciclo
		
		if (pf == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Ciclo no encontrado")									// Si no existe lo indicamos
			);
		}
		
		if (degree.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre")									// Si no existe el nombre lo indicamos
			);
		}
		else if (degree.getYear() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un año")										// Si no existe el año lo indicamos
			);
		}
		else if (degree.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la imagen del ciclo")						// Si no existe la imagen lo indicamos
			);
		}
		
		degree.setId(null);																						// Quitamos el id del ciclo
		
		// Establecemos los nuevos datos
		
		pf.setName(degree.getName());																		
		pf.setImage(degree.getImage());
		pf.setYear(degree.getYear());
		
		
		return ResponseEntity.status(HttpStatus.OK).body(
				professionalDegreeService.saveProfessionalDegree(pf) 											// Retornamos el resultado
		);
	}
	
	
	/**
	 * Borra un ciclo de una escuela
	 * @param idSchool Id de la escuela
	 * @param idDegree Id del ciclo
	 * @return Sin contenido
	 */
	@DeleteMapping("school/{idSchool}/degree/{idDegree}")
	public ResponseEntity removeDegree(@PathVariable Integer idSchool, @PathVariable Integer idDegree) {
		School currentSchool = schoolService.get(idSchool);														// Obtenemos el centro
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		if (!currentSchool.getProfessionalDegrees().contains(new ProfessionalDegree(idDegree))) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		professionalDegreeService.removeDegree(idDegree);														// Borramos el ciclo
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();												
	}
	
	
	
	// ================================================ Ciclos profesionales ================================================
	
	/**
	 * Obtiene un ciclo concreto
	 * @param idDegree Id del ciclo
	 * @return Ciclo con el id indicado
	 */
	@GetMapping("degree/{idDegree}")
	public ResponseEntity getDegree(@PathVariable Integer idDegree) {
		
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);						// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
			professionalDegree																					// SI existe retornamos el ciclo
		);
	}
	
	/**
	 * Obtiene todos los ciclos
	 * @return Lista de ciclos
	 */
	@GetMapping("degree")
	public ResponseEntity getDegrees() {
		
		List<ProfessionalDegree> professionalDegrees = professionalDegreeService.getAll();								// Obtenemos todos los ciclos
		
		return ResponseEntity.status(HttpStatus.OK).body(
			professionalDegrees																							// Retornamos los ciclos
		);
	}
	
	
	/**
	 * Obtiene el centro de un ciclo
	 * @param idDegree Id del ciclo
	 * @return Centro establecido
	 */
	@GetMapping("degree/{idDegree}/school")
	public ResponseEntity getSchoolFromDegree(@PathVariable Integer idDegree) {
		
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);						// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		if (professionalDegree.getSchool() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Este ciclo no está asignado a ningún centro")		 	// Si no existe el centro lo indicamos
			);
		}
		else {			
			return ResponseEntity.status(HttpStatus.OK).body(
					professionalDegree.getSchool()																// SI existe retornamos el resultado
			);
		}
	}
	
	/**
	 * Obtiene los profesores de un ciclo
	 * @param idDegree Id del ciclo
	 * @return Lista de profesores del ciclo
	 */
	@GetMapping("degree/{idDegree}/teacher")
	public ResponseEntity getTeachersFromDegree(@PathVariable Integer idDegree) {
		
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);						// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
			professionalDegree.getTeachers()																	// Obtenemos los profesores
		);
	}
	
	/**
	 * Añade un nuevo profesor al ciclo
	 * @param idDegree Id del ciclo
	 * @param teacher Profesor a añadir
	 * @return Profesor añadido
	 */
	@PostMapping("degree/{idDegree}/teacher")
	public ResponseEntity addTeachersFromDegree(@PathVariable Integer idDegree, @RequestBody Teacher teacher) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);						// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		else if (teacher.getDni() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique dni del profesor")							// SI no existe el dni lo indicamos
			);
		}
			
		teacher.setDni(teacher.getDni().toUpperCase());															// COnvertimos el dni en mayúsculas
		
		Teacher teacherDB = teacherService.get(teacher.getDni());												// Obtenemos el profesor
		
		if (teacherDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El profesor no existe")								// SI no existe lo indicamos
			);
		}
		else if (professionalDegree.getTeachers().contains(teacherDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "El profesor ya está asignado a este ciclo")				// Si está ya asignado lo indicamos
			);
		}
		
		professionalDegreeService.addTeacherToDegree(professionalDegree, teacherDB);							// Si no añadimos el profesor al ciclo
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
			teacherDB																							// Retornamos el resultado
		);
	}
	
	/**
	 * Quita un profesor de un ciclo
	 * @param idDegree Id del ciclo
	 * @param idTeacher Id del profesor
	 * @return Sin contenido
	 */
	@DeleteMapping("degree/{idDegree}/teacher/{idTeacher}")
	public ResponseEntity quitTeacherFromDegree(@PathVariable Integer idDegree, @PathVariable String idTeacher) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);					// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		else if (idTeacher == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "Indique dni del profesor")							// SI no existe el dni lo indicamos
			);
		}

		Teacher teacherDB = teacherService.get(idTeacher.toUpperCase());									// Obtenemos el profesor
		
		if (teacherDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El profesor no existe")							// SI no existe lo indicamos
			);
		}
		else if (!professionalDegree.getTeachers().contains(teacherDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "El profesor no está asignado a este ciclo")			// SI no está añadido lo indicamos
			);
		}
		
		professionalDegreeService.quitTeacherFromDegree(professionalDegree, teacherDB);						// Quitamos el profesor
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();						
	}
	
	
	// ================================================ Enrollment ================================================

	/**
	 * Obtiene todas las matriculaciones de un ciclo
	 * @param idDegree Id del ciclo
	 * @return Lista de matriculaciones
	 */
	@GetMapping("degree/{idDegree}/enrollment")
	public ResponseEntity getEnrollmentsFromDegree(@PathVariable Integer idDegree) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);					// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				professionalDegree.getEnrollments()															// Retornamos sus matriculaciones
		);		
	}
	
	/**
	 * Añade una nueva matriculación al ciclo
	 * @param idDegree Id del ciclo
	 * @param enrollment Datos de la nueva matricula
	 * @return Nueva matricula
	 */
	@PostMapping("degree/{idDegree}/enrollment")
	public ResponseEntity getEnrollmentsFromDegree(@PathVariable Integer idDegree, @RequestBody NewEnrollmentDTO enrollment) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);							// Obtenemos el ciclo
		
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")											// Si no existe lo indicamos
			);
		}
		
		if (enrollment.getDate() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la fecha de matriculación")						// Si no existe la fecha lo indicamos
			);
		}
		
		if (enrollment.getDniStudent() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el dni del alumno")								// Si no existe el dni lo indicamos
			);
		}
		
		enrollment.setDniStudent(enrollment.getDniStudent().toUpperCase());											// Convertimos el dni en maýusculas

		
		Student currentStudent = studentService.get(enrollment.getDniStudent());									// Obtenemos el estudiante
		
		if (currentStudent == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El alumno no existe")										// Si no existe lo indicamos
			);
		}
		
		Calendar calendar = Calendar.getInstance();																	// Obtenemos la fecha actual
		calendar.setTime(enrollment.getDate());																		// Establecemos la fecha
		
		
		if (enrollmentService.existEnrollment(calendar.get(Calendar.YEAR), professionalDegree, currentStudent)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "El alumno ya ha sido matriculado este año en este ciclo")									// Si ya está matriculado lo indicamos
			);
		}
		
		Enrollment newEnrollment = new Enrollment(enrollment.getDate(), currentStudent, professionalDegree);										// Creamos una nueva matricula
		
		newEnrollment = enrollmentService.save(newEnrollment);																						// La guardams
		
		newEnrollment.setProfessionalDegree(professionalDegree);																					// Establecemos el ciclo
		
		newEnrollment = enrollmentService.save(newEnrollment);																						// Lo guardamos
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				newEnrollment																														// Retornamos el resultado
		);		
	}
	
	/**
	 * Borra una matricula de un ciclo
	 * @param idDegree Id del ciclo
	 * @param enrollmentId Id de la matricula
	 * @return Sin contenido
	 */
	@DeleteMapping("degree/{idDegree}/enrollment/{enrollmentId}")
	public ResponseEntity removeEnrollmentsFromDegree(@PathVariable Integer idDegree,@PathVariable Integer enrollmentId) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);						// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Este ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		Enrollment enrollment = enrollmentService.get(enrollmentId);											// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Esta matrícula no existe")								// Si no existe lo indicamos
			);
		}
		else if (!professionalDegree.getEnrollments().contains(enrollment)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Esta matrícula no pertenece a este ciclo")			// Si no pertenece al ciclo lo indicamos
			);
		}
		
		enrollmentService.delete(enrollment);																	// Borramos la matriculación
		
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();											
	}
	
	// ================================================ Enrollment ================================================

	
	/**
	 * Obtiene todas las matriculas
	 * @return Lista de matriculas
	 */
	@GetMapping("enrollment")
	public ResponseEntity getEnrollments() {
		List<Enrollment> enrollments = enrollmentService.getAll();												// Obtenemos todas las matriculas
		
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollments																						// Retornamos la lista de matriculas
		);		
	}
	
	/**
	 * Obtiene una matricula concreta
	 * @param enrollmentId Id de la matricula
	 * @return Matricula solicitada
	 */
	@GetMapping("enrollment/{enrollmentId}")
	public ResponseEntity getEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);											// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")								// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment																						// Retornamos el resultado
		);		
	}
	
	/**
	 * Obtiene el estudiante de una matricula
	 * @param enrollmentId Id de la matricula
	 * @return Estudiante de la matricula
	 */
	@GetMapping("enrollment/{enrollmentId}/student")
	public ResponseEntity getStudentFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);												// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getStudent() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Alumno no establecido")									// Si no existe el alumno lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment.getStudent()																				// Obtenemos el estudiante
		);		
	}
	
	// ================================================ Degree ================================================ 
	
	/**
	 * Obtiene el ciclo de una matricula concreta
	 * @param enrollmentId Id de la matricula
	 * @return Ciclo de la matricula
	 */
	@GetMapping("enrollment/{enrollmentId}/degree")
	public ResponseEntity getDegreeFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);												// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getProfessionalDegree() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Ciclo no establecido")									// Si no existe el ciclo lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment.getProfessionalDegree()																// Obtenemos los ciclos
		);
	}
	
	// ================================================ Preference ================================================ 
	/**
	 * Obtiene las preferencias de la matricula
	 * @param enrollmentId Id de la matricula
	 * @return Lista de preferencias
	 */
	@GetMapping("enrollment/{enrollmentId}/preference")
	public ResponseEntity getPreferencesFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);												// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment.getPreferences()																			// Si no retornamos ls preferencias
		);
	}
	
	
	/**
	 * Añade una preferencia
	 * @param enrollmentId Id de la matricula 
	 * @param newPreference Nueva preferencia
	 * @return Nueva preferencia
	 */
	@PostMapping("enrollment/{enrollmentId}/preference")
	public ResponseEntity addPreferencesFromEnrollment(@PathVariable Integer enrollmentId, @RequestBody NewPreferenceDTO newPreference) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);												// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (newPreference.getCif() == null || newPreference.getCif().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un cif válido")									// Si no existe lo indicamos
			);
		}

		
		newPreference.setCif(newPreference.getCif().toUpperCase());													// Convertimos el cif en maýusculas
		
		Business business = businessService.get(newPreference.getCif());											// Obtenemos la empresa
		
		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")										// Si no existe lo indicamos
			);
		}
		else if (preferenceService.getByEnrollmentAndBusiness(enrollment, business) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "La empresa ya está asignada como preferida")				// Si ya existe lo indicamos
			);
		}
		else if (!enrollment.getProfessionalDegree().getBusinesses().contains(business)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Esta empresa no está disponible para el ciclo que estás cursando")	// Si no existe lo indicamos 
			);
		}
		newPreference.setPosition(enrollment.getPreferences().size());															// Establecemos la nueva posición
		
		Preference preferenceToAdd = new Preference(newPreference.getPosition(), business, enrollment);							// Creamos una nueva preferencia
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				preferenceService.save(preferenceToAdd)																			// Guardamos y retornamos la preferencia
		);
	}
	
	/**
	 * Edita una preferenia
	 * @param enrollmentId Id de la matricula
	 * @param newPositions Nuevas posiciones de las preferencias
	 * @return Lista de preferencias
	 */
	@PutMapping("enrollment/{enrollmentId}/preference")
	public ResponseEntity updateAllPreferencesFromEnrollment(@PathVariable Integer enrollmentId, @RequestBody UpdatePreferencesDTO newPositions) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);													// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")										// Si no existe lo indicamos
			);
		}
		else if (newPositions.getPreferences() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "No se ha indicado ninguna preferencia")						// Si no se han indicado preferencias lo indicamos
			);
		}
		else if (enrollment.getPreferences().size() != newPositions.getPreferences().size()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "El número de preferencias no coincide")							// Si el número de preferencias no coincide lo indicamos
			);
		}
		else if (!preferenceService.checkIfExistAll(newPositions.getPreferences())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Existen identificadores nulos o no existentes")				// Si no existe alguna lo indicamos						
			);
		}
		else if (!preferenceService.checkIfOrderIsCorrect(newPositions.getPreferences())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El orden de las preferencias no es correcto")				// Si el orden no es correcto lo indicamos						
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				preferenceService.updatePositions(newPositions.getPreferences())										// Actualizamos las posiciones y retornamos el resultado
		);
		
	}
	
	/**
	 * Borra una preferencia
	 * @param enrollmentId Id de la preferencia
	 * @return Sin contenido
	 */
	@DeleteMapping("enrollment/{enrollmentId}/preference")
	public ResponseEntity deleteAllPreferencesFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);												// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getPreferences().size() == 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "No existen preferencias")									// Si no existen preferencias lo indicamos
			);
		}
		
		preferenceService.deleteAllPreferenceFromEnrollment(enrollment);											// Borramos todas las preferencias
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();												
	}
	
	
	// ================================================ Preferencias =============================================
	
	/**
	 * Obtiene una preferencia por su id
	 * @param idPreference Id de la preferencia
	 * @return Preferencia solicitada
	 */
	@GetMapping("preference/{idPreference}")
	public ResponseEntity getPreference(@PathVariable Integer idPreference) {
		Preference preference = preferenceService.get(idPreference);											// Obtenemos la preferncia
		
		if (preference == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La preferencia no existe")									// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				preference																						// Si no la retornamos
		);
	}
	/**
	 * Obtiene la empresa de una preferencia
	 * @param idPreference Id de la preferencia
	 * @return Empresa de la preferencia
	 */
	@GetMapping("preference/{idPreference}/business")
	public ResponseEntity getBusinessFromPreference(@PathVariable Integer idPreference) {
		
		Preference preference = preferenceService.get(idPreference);												// Obtenemos la preferencia
		
		if (preference == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La preferencia no existe")									// Si no existe lo indicamos
			);
		}
		if (preference.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Empresa no establecida")									// Si no existe la empresa lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				preference.getBusiness()																			// Obtenemos la empresa
		);
	}
	
	
	/**
	 * Obtiene la matricula de una preferencia
	 * @param idPreference Id de la preferencia
	 * @return Matricula de la preferencia
	 */
	@GetMapping("preference/{idPreference}/enrollment")
	public ResponseEntity getEnrollmentFromPreference(@PathVariable Integer idPreference) {
		Preference preference = preferenceService.get(idPreference);												// Obtenemos la preferncia
			
		if (preference == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La preferencia no existe")									// Si no existe lo indicamos
			);
		}
		if (preference.getEnrollment() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Matrícula no establecida")									// Si no existe la matricula lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				preference.getEnrollment()																			// Retornamos la matricula
		);
	}
	
	
	// ================================================ Practicas ================================================
	/**
	 * Obtiene la práctica de una matricula
	 * @param enrollmentId Id de la matricula
	 * @return Practica
	 */
	@GetMapping("enrollment/{enrollmentId}/practise")
	public ResponseEntity getPractiseFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);												// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getPractise() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Prácticas no comenzadas")									// Si no están comenzadas lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment.getPractise()																			// Si no retornamos la practica
		);
	}
	
	/**
	 * Crea la practica de la matricula
	 * @param enrollmentId Id de la matricula
	 * @return Practica creada
	 */
	@PostMapping("enrollment/{enrollmentId}/practise")
	public ResponseEntity addPractiseFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);																// Obtenemos la matricula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")													// Si no existe lo indicamos
			);
		}
		else if (enrollment.getPractise() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "La matrícula ya tiene práctica asignada")									// Si ya existe lo inidcamos
			);
		}
		
		Practise newPractise = new Practise();														// Creamos la practic
		newPractise = practiseService.save(newPractise);											// La guardamos
		
		enrollment.setPractise(newPractise);														// La establecemos a la matricula
		enrollmentService.save(enrollment);															// Guardamos la matricula
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				newPractise																			// Retornamos la practica
		);
	}
	
	/**
	 * Borra una practica
	 * @param enrollmentId Id de la matricula
	 * @return Sin contenido
	 */
	@DeleteMapping("enrollment/{enrollmentId}/practise")
	public ResponseEntity removePractiseFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);											// Obtenemos la maticula
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getPractise() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no tiene práctica asignada")									// Si no existe la practica lo indicamos
			);
		}
		
		Practise practiseToRemove = enrollment.getPractise();													// Obtenemos la practica
		
		enrollment.setPractise(null);																			// Establecemos esta a null
		enrollmentService.save(enrollment);																		// Guardamos
		
		practiseService.remove(practiseToRemove.getId());														// Borramos la practica
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();					
	}
	
	
	// ================================================ Practise ================================================ 	
	
	/**
	 * Obtiene una practica por su id
	 * @param practiseId Id de la practica
	 * @return Practica solicitada
	 */
	@GetMapping("practise/{practiseId}")
	public ResponseEntity getPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId); 				// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(	
				practise																		// Retornamos la practica
		);
	}
	
	/**
	 * Edita una practica
	 * @param practiseId Id de la practica
	 * @param practiseData Datos nuevos de la practica
	 * @return Practica actualizada
	 */
	@PutMapping("practise/{practiseId}")
	public ResponseEntity editPractiseFromEnrollment(@PathVariable Integer practiseId, @RequestBody Practise practiseData) {
		
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		

		
		if (practiseData.getStart() != null && practiseData.getFinish() != null) {
			if (practiseData.getStart().after(practiseData.getFinish())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "La fecha de fin debe ser posterior a la de inicio")		// Comprobamos si las fechas son correctas								
				);
			}
			else {
				practise = practiseService.updatePractise(practise, practiseData);										// Actualizamos
			}

		}
		else if (practiseData.getStart() != null && practise.getFinish() != null && practiseData.getStart().after(practise.getFinish())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "La fecha de inicio no puede ser superior a la de finalización")									// Comprobamos la fecha de fin
			);
		}
		else if (practiseData.getFinish() != null && practise.getStart() != null && practiseData.getFinish().before(practise.getStart())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "La fecha de fin no puede ser antes a la de inicio")									// Comprobamos la fecha de inciio
			);
		}
		else {
			practise = practiseService.updatePractise(practise, practiseData);																// Actualizamos la practica
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise																								// Retornamos el resultado
		);
	}
	
	
	// ================================================== Enrollment ==================================================
	
	/**
	 * Obtiene la matricula desde una practica
	 * @param practiseId Id de la practica
	 * @return Matricula solicitada
	 */
	@GetMapping("practise/{practiseId}/enrollment")
	public ResponseEntity getEnrollmentFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getEnrollment() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Matrícula no está establecida")									// Si no esá establecida la practica lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise.getEnrollment()																					// Retornamos la matricula
		);
	}
	
	// ================================================== Profesor ==================================================
	
	/**
	 * Obtiene el profesor de una practica
	 * @param practiseId Id de la practica
	 * @return Profesor asignado
	 */
	@GetMapping("practise/{practiseId}/teacher")
	public ResponseEntity getTeacherFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getTeacher() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Profesor no establecido")									// Si no existe el profesor lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise.getTeacher()																				// Retornamos el profesor
		);
	}
	
	/**
	 * Establece un profesor a una practica
	 * @param practiseId Id de la practica
	 * @param teacher Profesor a asignar
	 * @return Profesor asignado
	 */
	@PostMapping("practise/{practiseId}/teacher")
	public ResponseEntity setTeacherFromPractise(@PathVariable Integer practiseId, @RequestBody Teacher teacher) {
		
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getTeacher() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Profesor ya establecido")									// Si no existe el profesor lo indicamos
			);
		}
		else if (teacher.getDni() == null || teacher.getDni().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")									// Si no existe el dni lo indicamos
			);
		}

		teacher.setDni(teacher.getDni().toUpperCase());																// Convertimos el dni en maýusculas
		
		Teacher teacherDB = teacherService.get(teacher.getDni());													// Obtenemos el profesor
		
		if (teacherDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El profesor no existe")									// Si no existe lo indicamos
			);
		}
		
		practise.setTeacher(teacherDB);																				// Establecemos el profesor 
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getTeacher()															// Retornamos el profesor
		);
	}
	
	/**
	 * Edita el profesor de practicas
	 * @param practiseId Id de la practica
	 * @param teacher Profesor a asignar
	 * @return Profesor asignado
	 */
	@PutMapping("practise/{practiseId}/teacher")
	public ResponseEntity editTeacherFromPractise(@PathVariable Integer practiseId, @RequestBody Teacher teacher) {
		
		Practise practise = practiseService.get(practiseId);													// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (teacher.getDni() == null || teacher.getDni().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")									// Si no existe el dni lo indicamos
			);
		}

		teacher.setDni(teacher.getDni().toUpperCase());													// Convertimos el dni en maýusculas
		
		Teacher teacherDB = teacherService.get(teacher.getDni());										// Obtenemos el profesor
		
		if (teacherDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El profesor no existe")									// Si no existe lo indicamos
			);
		}
		
		practise.setTeacher(teacherDB);																	// Establecemos el nuevo profesor
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getTeacher()												// Guardamos y retornamos el resultado
		);
	}
	
	/**
	 * QUita un profesor de una practica
	 * @param practiseId id de la practica
	 * @return SIn contenido
	 */
	@DeleteMapping("practise/{practiseId}/teacher")
	public ResponseEntity quitTeacherFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);														// Obtemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getTeacher() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Profesor no establecido")									// Si no está establecido lo indicamos
			);
		}
		
		practise.setTeacher(null);																				// Quitamos el profesor
		practiseService.save(practise);																			// Guardamos
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();						
	}
	
	
	
	// ================================================== Tutor laboral ==================================================
	
	/**
	 * Obtiene el tutor laboral de la practica
	 * @param practiseId Id de la practica
	 * @return Tutor asignado
	 */
	@GetMapping("practise/{practiseId}/labor-tutor")
	public ResponseEntity getLaborTutorFromPractise(@PathVariable Integer practiseId) {
			
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getLaborTutor() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Tutor laboral no establecido")								// Si no esta establecido lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise.getLaborTutor()																			// Obtenemos el tutor laboral 
		);
	}
	
	/**
	 * Establece el tutor laboral de la practica
	 * @param practiseId Id de la practica
	 * @param laborTutor Tutor laboral
	 * @return Tutor establceido
	 */
	@PostMapping("practise/{practiseId}/labor-tutor")
	public ResponseEntity setLaborTutorFromPractise(@PathVariable Integer practiseId, @RequestBody LaborTutor laborTutor) {
		
		Practise practise = practiseService.get(practiseId);													// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getLaborTutor() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Tutor laboral ya establecido")									// Si ya existe lo indicamos
			);
		}
		else if (laborTutor.getDni() == null || laborTutor.getDni().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")									// Si no existe el dni lo indicamos
			);
		}

		laborTutor.setDni(laborTutor.getDni().toUpperCase());													// Convertimos el dni en maýusculas
		
		LaborTutor laborTutorDB = laborTutorService.getById(laborTutor.getDni());								// Obtenemos el tutor
		
		if (laborTutorDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")									// Si no existe lo indicamos
			);
		}
		
		practise.setLaborTutor(laborTutorDB);																	// Establecemos el tutor
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getLaborTutor()													// Guardamos y retornamos el resultado
		);
	}
	
	/**
	 * Edita el tutor laboral de la practica
	 * @param practiseId Id de la practica
	 * @param laborTutor Nuevo Tutor laboral
	 * @return Tutor laboral establecido
	 */
	@PutMapping("practise/{practiseId}/labor-tutor")
	public ResponseEntity editLaborTutorFromPractise(@PathVariable Integer practiseId, @RequestBody LaborTutor laborTutor) {
		
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (laborTutor.getDni() == null || laborTutor.getDni().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")									// Si no existe el dni lo indicamos
			);
		}

		laborTutor.setDni(laborTutor.getDni().toUpperCase());												// Convertimos el dni en maýusculas
		
		LaborTutor laborTutorDB = laborTutorService.getById(laborTutor.getDni());							// Obtenemos el tutor
		
		if (laborTutorDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")									// Si no existe lo indicamos
			);
		}
		
		practise.setLaborTutor(laborTutorDB);																// Establecemos el tutor
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getLaborTutor()												// Guardamos y retornamos
		);
	}
	
	/**
	 * Quita un tutor laboral de la practica
	 * @param practiseId Id de la practica
	 * @return Sin contenido
	 */
	@DeleteMapping("practise/{practiseId}/labor-tutor")
	public ResponseEntity quitLaborTutorFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getLaborTutor() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Tutor laboral no establecido")									// Si no existe el tutor lo indicamos
			);
		}
		
		practise.setLaborTutor(null);																		// Establecemos el tutor a nulo
		practiseService.save(practise);																		// Guardamos
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();									
	}
	
	
	// ================================================== Empresa ==================================================
	
	/**
	 * Obtiene la empresa de una practica
	 * @param practiseId Id de la practica
	 * @return Empresa asignada
	 */
	@GetMapping("practise/{practiseId}/business")
	public ResponseEntity getBusinessFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Empresa no establecida")									// Si no existe la emrpesa lo indicamos
			);
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise.getBusiness()																				// Retornamos el resultado
		);
	}
	
	/**
	 * Cambia la empresa de una practica
	 * @param practiseId id de la practica
	 * @param business Nueva empresa
	 * @return Empresa establecida
	 */
	@PutMapping("practise/{practiseId}/business")
	public ResponseEntity setNewBusinessFromPractise(@PathVariable Integer practiseId, @RequestBody Business business) {
		
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Empresa no establecida")									// Si no existe la empresa lo indicamos
			);
		}
		else if (business.getCif() == null || business.getCif().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un cif válido")									// Si no existe el cif lo indicamos
			);
		}

		business.setCif(business.getCif().toUpperCase());															// Convertimos el cif en maýusculas
		
		Business businessDB = businessService.get(business.getCif());												// Obtenemos la empresa
		


		
		if (businessDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getBusiness().getCif().toUpperCase().equals(business.getCif().toUpperCase())) {
			return ResponseEntity.status(HttpStatus.OK).body(
					businessDB
			);
		}
		else if (practise.getEnrollment() != null && practise.getEnrollment().getProfessionalDegree() != null && !practise.getEnrollment().getProfessionalDegree().getBusinesses().contains(businessDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Esta empresa no está disponible para el ciclo que cursa")									// Si no existe lo indicamos
			);
		}
		else if (businessService.getCountofStudentInBusinessInThisYear(businessDB) + 1 > businessDB.getNumberOfStudents()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Esta empresa no admite más alumnos en este año")									// Si no existe lo indicamos
			);
		}
		
		practise.setBusiness(businessDB);																				// Establecemos la empresa
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practiseService.save(practise).getBusiness()															// Guardamos y retornamos
		);
	}


	/**
	 * Establece una empresa a la practica
	 * @param practiseId Id de la practica
	 * @param business Nueva empresa
	 * @return EMpresa establecida
	 */
	@PostMapping("practise/{practiseId}/business")
	public ResponseEntity setBusinessFromPractise(@PathVariable Integer practiseId, @RequestBody Business business) {
		
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getBusiness() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Empresa ya establecida")									// Si ya existe lo indicamos
			);
		}
		else if (business.getCif() == null || business.getCif().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un cif válido")									// Si no existe el cif lo indicamos
			);
		}

		business.setCif(business.getCif().toUpperCase());															// Convertimos el cif en maýusculas
		
		Business businessDB = businessService.get(business.getCif());												// Obtenemos la empresa
		
		
		
		if (businessDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getEnrollment() != null && practise.getEnrollment().getProfessionalDegree() != null && !practise.getEnrollment().getProfessionalDegree().getBusinesses().contains(businessDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Esta empresa no está disponible para el ciclo que cursa")									// Si no está disponible lo indicamos
			);
		}
		else if(businessService.getCountofStudentInBusinessInThisYear(businessDB) >= businessDB.getNumberOfStudents()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Esta empresa no admite más alumnos en este año")									// Si no admite más alumnos lo indicamos
			);
		}
		
		practise.setBusiness(businessDB);																							// Establecemos la empresa a la practica
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getBusiness()																		// G
		);
	}
	/**
	 * Quita la empresa de una practica
	 * @param practiseId Id de la practica
	 * @return SIn contenido
	 */
	@DeleteMapping("practise/{practiseId}/business")
	public ResponseEntity quitBusinessFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);														// Obtenemos la practica
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Empresa no establecida")									// Si no existe la empresa lo indicamos
			);
		}
		
		practise.setBusiness(null);																					// Establecemos la empresa a null
		practiseService.save(practise);																				// Guardamos la practica
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();												
	}
	
	
	
	// ================================================ Business ================================================

	/**
	 * Obtiene las empresas de un ciclo
	 * @param idDegree Id del ciclo
	 * @return Lista de empresas
	 */
	@GetMapping("degree/{idDegree}/business")
	public ResponseEntity getBusinessFromDegree(@PathVariable Integer idDegree) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);					// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				professionalDegree.getBusinesses()															// Retornamos las empresas
		);		
	}

	/**
	 * Añade una nueva empresa a un ciclo
	 * @param idDegree Id del ciclo
	 * @param business Empresa a añadir
	 * @return Empresa añadida
	 */
	@PostMapping("degree/{idDegree}/business")
	public ResponseEntity addBusinessFromDegree(@PathVariable Integer idDegree, @RequestBody Business business) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);						// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		else if (business.getCif() == null || business.getCif().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un CIF válido")									// Si no existe el cif lo indicamos
			);
		}
		
		business.setCif(business.getCif().toUpperCase());														// Convertimos el dni en maýusculas
		
		Business businessDB = businessService.get(business.getCif());											// Obtenemos la empresa
		
		if (businessDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")									// Si no existe lo indicamos
			);
		}
		else if (professionalDegree.getBusinesses().contains(businessDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "La empresa ya está añadida a este ciclo")									// Si está añadida  lo indicamos
			);
		}
		
		businessDB.getDegrees().add(professionalDegree);														// Añadimos la empresa
			
		return ResponseEntity.status(HttpStatus.CREATED).body(
				businessService.save(businessDB)																// Guardamos y retornamos el resultado
		);		
	}
	
	
	/**
	 * Quita una empresa de un ciclo
	 * @param idDegree Id del ciclo
	 * @param businessId Id de la empresa
	 * @return Sin contenido
	 */
	@DeleteMapping("degree/{idDegree}/business/{businessId}")
	public ResponseEntity quitBusinessFromDegree(@PathVariable Integer idDegree, @PathVariable String businessId) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);					// Obtenemos el ciclo
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		Business businessDB = businessService.get(businessId.toUpperCase());
		
		if (businessDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")									// Si no existe la empresa lo indicamos
			);
		}
		else if (!professionalDegree.getBusinesses().contains(businessDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "La empresa no está añadida a este ciclo")									// Si no está añadida lo indicamos
			);
		}
		
		businessDB.getDegrees().remove(professionalDegree);																// Borramos el ciclo de la empresa
		
		businessService.save(businessDB);																			// Guardamos
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();									
	}

	// ================================================ Business ================================================
	
	
	/**
	 * Obtiene todas las empresas
	 * @return Listado de empresas
	 */
	@GetMapping("business")
	public ResponseEntity getAllBusiness(@RequestParam(required = false) String name) {
		List<Business> allBusiness;																		// Almacenará todas las empresas

		if (name != null) {
			allBusiness = businessService.getAllByName(name);											// Si nos indican un criterio de búsqueda buscamos por nombre
		}
		else {
			allBusiness = businessService.getAll();														// SI no retornamos todas las empresas
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				allBusiness 																			// Retornamos el resultado
		);
	}

	/**
 	* Obtiene una determinada empresa
 	* @return Empresa solicitada
 	*/
	@GetMapping("business/{cif}")
	public ResponseEntity getBusiness(@PathVariable String cif) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")		// Si no existe la empresa lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					business															// Si existe la retornamos
			);
		}
	}
	
	/**
	 * Valida si existe una empresa (Usada para validar si existe una empresa por el cif)
	 * @param cif
	 * @return
	 */
	@GetMapping("exist-business/{cif}")
	public ResponseEntity existBusiness(@PathVariable String cif) {
		
		ArrayList<Business> businessResult = new ArrayList<>();
		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business != null) {
			
			businessResult.add(business);														// Añadimos la empresa
			
			return ResponseEntity.status(HttpStatus.OK).body(
					businessResult																// Retornamos el resultado
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					businessResult															 	// Retornamos el resultado
			);
		}
	}
	
	/**
	 * Crea una nueva empresa
	 * @param Business: Empresa a crear
	 * @return Nueva empresa
	 */
	@PostMapping("business")
	public ResponseEntity createBusiness(@RequestBody Business business) {
		if (business.getCif() == null || business.getCif().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un cif de empresa válido")						// Si no nos indican el cif lo indicamos
			);
		}
		
		business.setCif(business.getCif().toUpperCase());
		
		if (businessService.get(business.getCif()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Ya existe una empresa con este CIF")					// Si no nos indican el nombre de la empresa lo indicamos
			);
		}
		else if (business.getName() == null || business.getName().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el nombre de la empresa")					// Si no nos indican el nombre de la empresa lo indicamos
			);
		}
		else if (business.getNumberOfStudents() == null || business.getNumberOfStudents() < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique una cantidad de estudiantes válida")			// Si no nos han indicado cantidad de estudiantes o es incorrecta lo indicamos
			);
		}
		else if (business.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique imagen de la empresa")			// Si no nos han indicado cantidad de estudiantes o es incorrecta lo indicamos
			);
		}
		else if (businessService.exist(business)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Ya existe una empresa con este CIF")					// Si ya existe una empresa con este cif lo indicamos
			);
		}
		
		business.setCif(business.getCif().toUpperCase());

		return ResponseEntity.status(HttpStatus.CREATED).body(
				businessService.save(business)																	// Guardamos la empresa y la retornamos
		);
	}
	
	/**
	 * Edita una empresa
	 * @param cif: Cif de la empresa
	 * @param business: Datos de la empresa
	 * @return Empresa editada
	 */
	@PutMapping("business/{cif}")
	public ResponseEntity editBusiness(@PathVariable String cif, @RequestBody Business business) {
	
	    if (business.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el nombre de la empresa")				// Si no nos indican el nombre de la empresa lo indicamos
			);
		}
	    if (business.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la imagen de la empresa")				// Si no nos indican el nombre de la empresa lo indicamos
			);
		}
		else if (business.getNumberOfStudents() == null || business.getNumberOfStudents() < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique una cantidad de estudiantes válida")		//  Si la cantidad no es válida lo indicamos
			);
		}
		
	    business.setCif(cif.toUpperCase());
	    
		if (!businessService.exist(business)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "No existe ninguna empresa con este CIF")			// Si no existe la empresa lo indicamos
			);
		}

		return ResponseEntity.status(HttpStatus.OK).body(
				businessService.edit(business)															// Editamos la empresa y retornamos el retultado
		);
	}
	
	
	/**
	 * Borra una empresa
	 * @param cif Cif de la empresa
	 * @return Sin contenido
	 */
	@DeleteMapping("business/{cif}")
	public ResponseEntity deleteBusiness(@PathVariable String cif) {
	    
		Business business = businessService.get(cif.toUpperCase()); 									// Convertimos el cif en maýusculas y obtenemos la empresa
		
		if (business == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "No existe ninguna empresa con este CIF")			// Si no existe la empresa lo indicamos
			);
		}
		
		
		preferenceService.removeFromBusinessAndUpdatePositions(business);								// Borramos la empresa y actualizamos posiciones de preferencias
		businessService.remove(cif.toUpperCase());														// Borramos la empresa
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/* Segundo nivel */
	
	/**
	 * Obtiene todas las prácticas de una empresa
	 * @param cif CIf de la empresa
	 * @return Lista de practicas
	 */
	@GetMapping("business/{cif}/practise")
	public ResponseEntity getPractisesFromBusiness(@PathVariable String cif) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")					// Si no existe la empresa lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					business.getPractises()															// Retornamos todas las practicas
			);
		}
	}
	
	/**
	 * Obtiene los tutores laborales de una empresa
	 * @param cif Cif de la empresa
	 * @return Lista de tutores laborales
	 */
	@GetMapping("business/{cif}/labor-tutor")
	public ResponseEntity getLaborTutorsFromBusiness(@PathVariable String cif) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")					// Si no existe la empresa lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					business.getTutors()															// Si existe retornamos los tutores
			);
		}
	}
	
	// ================================================= Trabajador de contacto =================================================

	
	/**
	 * Obtiene todos los trabajadores de contacto de una empresa
	 * @param cif CIF de la empresa
	 * @return Lista de trabajdores de contacto
	 */
	@GetMapping("business/{cif}/contact-worker")
	public ResponseEntity getContactWorkersFromBusiness(@PathVariable String cif) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")					// Si no existe la empresa lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					business.getContactWorkers()													// Si existe retornamos los trabajdores de contacto
			);
		}
	}
	
	/**
	 * Obtiene un trabjador de contacto concreto
	 * @param cif CIF de la empresa
	 * @param idContact Id del contacto
	 * @return Trabajador de contacto solicitado
	 */
	@GetMapping("business/{cif}/contact-worker/{idContact}")
	public ResponseEntity getContactWorkerFromBusiness(@PathVariable String cif, @PathVariable Integer idContact) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")					// Si no existe la empresa lo indicamos
			);
		}
		
		ContactWorker contact = contactWorkerService.getById(idContact);
		
		if (contact == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El contacto no existe")					// Si no existe el contacto lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					contact																			// Retonamos el resultado
			);
		}
	}
	
	/**
	 * Añade un nuevo trabajador de contacto a la empresa
	 * @param cif CIf de la empresa
	 * @param contactWorker Nuevo trabajador de contacto
	 * @return Nuevo trabajador de contacto
	 */
	@PostMapping("business/{cif}/contact-worker")
	public ResponseEntity addContactWorkersFromBusiness(@PathVariable String cif, @RequestBody ContactWorker contactWorker) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")					// Si no existe la empresa lo indicamos
			);
		}
		else if (contactWorker.getName() == null || contactWorker.getName().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre")					// Si no existe el nombre lo indicamos
			);
		}		
		else if (contactWorker.getEmail() == null || contactWorker.getEmail().trim().length() == 0 || !contactWorker.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un email")					// Si no existe el email o es incorrecto lo indicamos
			);
		}
		else if (contactWorker.getTelefone() == null || contactWorker.getTelefone().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un teléfono")					// Si no existe el telefono o es incorrecto lo indicamos
			);
		}
		
		contactWorker.setId(null);																	// Establecemos el id a nulo

		ContactWorker newContact = contactWorkerService.save(contactWorker);						// Creamos el contacto
		business.getContactWorkers().add(newContact);												// Lo añadimos
		
		businessService.save(business);																// Guardamos la empresa
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				newContact																			// Retornamos el resultado
		);
	}
	/**
	 * Edita un trabajador de contacto
	 * @param cif CIf de la empresa
	 * @param idContact Id de contacto
	 * @param contactWorker Datos del trabajador de contacto
	 * @return Trabajador de contacto editado
	 */
	@PutMapping("business/{cif}/contact-worker/{idContact}")
	public ResponseEntity editContactWorkersFromBusiness(@PathVariable String cif, @PathVariable Integer idContact, @RequestBody ContactWorker contactWorker) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")					// Si no existe la empresa lo indicamos
			);
		}
		else if (contactWorker.getName() == null || contactWorker.getName().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre")					// Si no existe el nombre lo indicamos
			);
		}		
		else if (contactWorker.getEmail() == null || contactWorker.getEmail().trim().length() == 0 || !contactWorker.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un email")					// Si no existe el email o no es correcto lo indicamos
			);
		}
		else if (contactWorker.getTelefone() == null || contactWorker.getTelefone().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un teléfono")				// Si no existe el telefono o no es correcto lo indicamos
			);
		}
			
		ContactWorker contact = contactWorkerService.getById(idContact);						// Obtenemos el trabajador de contacto
		
		if (contact == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El contacto no existe")				// SI no existe lo indicamos
			);
		}
		
		contactWorker.setId(idContact);															// Establecemos el nuevo id
		
		return ResponseEntity.status(HttpStatus.OK).body(
				contactWorkerService.edit(contactWorker)										// Editamos y retornamos el resutlado
		);
	}
	
	
	
	/**
	 * Borra un trabajador de contacto
	 * @param cif CIF de la empresa 
	 * @param idContact Id del trabajador
	 * @return SIn contenido
	 */
	@DeleteMapping("business/{cif}/contact-worker/{idContact}")
	public ResponseEntity addContactWorkersFromBusiness(@PathVariable String cif, @PathVariable Integer idContact) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")					// Si no existe la empresa lo indicamos
			);
		}
		ContactWorker currentContact = contactWorkerService.getById(idContact);
		
		if (currentContact == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe este contacto")					// Si no existe el contacto lo indicamos
			);
		}
		else if (!business.getContactWorkers().contains(currentContact)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El contacto no pertenece a esta empresa")					// Si no existe el contacto en esta empresa lo indicamos
			);
		}

		business.getContactWorkers().remove(business.getContactWorkers().indexOf(currentContact));						// Quitamos el trabajador
		businessService.save(business);																					// Guardamos la empresa
		
		contactWorkerService.remove(idContact);																			// Borramos el trabajador
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();												
	}
	
	
	
	// ================================================= Ubicación =================================================
	
	/**
	 * Obtiene la ubicaciónd de una empresa
	 * @param cif: Cif de la empresa
	 * @return Ubicación de la empresa
	 */
	@GetMapping("business/{cif}/ubication")
	public ResponseEntity getUbicationFromBusiness(@PathVariable String cif) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")		// Si no existe la empresa lo indicamos
			);
		}
		if (business.getLocation() == null) { 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Ubicación aún no establecida")		// Si no existe la ubicación lo indicamos
			);
		}
			
		return ResponseEntity.status(HttpStatus.OK).body(
			business.getLocation()													// Retornamos la localización
		);
	}
	
	/**
	 * Establece la ubicación de una empresa
	 * @param cif CIf de la empresa
	 * @param location Datos de la localización
	 * @return Nueva localiación
	 */
	@PostMapping("business/{cif}/ubication")
	public ResponseEntity addUbicationToBusiness(@PathVariable String cif, @RequestBody Location location) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")		// Si no existe la empresa lo indicamos
			);
		}
		else if (business.getLocation() != null) { 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Ubicación ya establecida")		// Si ya está establecida lo indicamos
			);
		}
		else if (location.getLatitude() == null || location.getLatitude() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique latitud")		// Si no existe la latitud lo indicamos
			);			
		}
		else if (location.getLongitude() == null || location.getLongitude() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique longitud")		// Si no existe la longitud lo indicamos
			);			
		}
		
		location.setId(null);														// Establecemos el id a nulo
		locationService.saveLocation(location);										// Guardamos la localización
		
		business.setLocation(location);												// Establecemos la localización
			
		return ResponseEntity.status(HttpStatus.CREATED).body(
			businessService.save(business).getLocation()							// Guardamos y retornamos el resultado
		);
	}
	
	@PutMapping("business/{cif}/ubication")
	public ResponseEntity editUbicationToBusiness(@PathVariable String cif, @RequestBody Location location) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")		// Si no existe la empresa lo indicamos
			);
		}
		else if (business.getLocation() == null) { 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Ubicación no establecida")		// Si no está la ubicación establecida lo indicamos
			);
		}
		else if (location.getLatitude() == null || location.getLatitude() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique latitud")		// Si no existe la latitud lo indicamos
			);			
		}
		else if (location.getLongitude() == null || location.getLongitude() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique longitud")		// Si no existe la longitud lo indicamos
			);			
		}
		
			
		return ResponseEntity.status(HttpStatus.OK).body(
				locationService.editLocation(business.getLocation().getId(), location.getLongitude(), location.getLatitude())	// Editamos y retornamos el resultado
		);
	}
	
	/**
	 * Borra la ubicación de la empresa
	 * @param cif CIf de la emrpesa
	 * @return Sin contenido
	 */
	@DeleteMapping("business/{cif}/ubication")
	public ResponseEntity removeUbicationToBusiness(@PathVariable String cif) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")		// Si no existe la empresa lo indicamos
			);
		}
		else if (business.getLocation() == null) { 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Ubicación no establecida")		// Si no está establecida la ubicación lo indicamos
			);
		}
		
		Location locationToRemove = business.getLocation();								// Obtenemos la ubicación a borrar
	
		business.setLocation(null);														// Establecemos la ubicación a nulo
		
		businessService.save(business);													// Guardamos la empresa
		
		locationService.removeLocation(locationToRemove.getId());						// Borramos la ubicación
			
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}