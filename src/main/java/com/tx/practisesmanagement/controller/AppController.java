package com.tx.practisesmanagement.controller;



import java.time.ZoneId;
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

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			new RestError(HttpStatus.NOT_FOUND, "Persona no encontrada")									// Si el usuario no existe, lanzamos error
		);
    }
	
	
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

		dni = dni.toUpperCase();
		
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
	
	
	@PostMapping("administrator/{dni}/school")
	public ResponseEntity addSchoolToAdministrator(@PathVariable String dni, @RequestBody School school) {
		
		dni = dni.toUpperCase();
		
		Administrator currentAdministrator = administratorService.get(dni);		


		if (currentAdministrator == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona no existe o no cuenta con privilegios de administrador")	
			);
		}
		
		
		if (currentAdministrator.getSchoolSetted() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Ya administra una escuela")	
			);
		}
		
		if (school.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Debe indicar el id de la escuela")	
			);
		}
		
		School schoolFromDB = schoolService.get(school.getId());
		
		if (schoolFromDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La escuela con id " + school.getId() + " no existe")	
			);
		}
		else if (school.getPassword() == null || !school.getPassword().equals(schoolFromDB.getPassword())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Contraseña incorrecta")	
			);
		}
		
		currentAdministrator.setSchoolSetted(schoolFromDB);
		
		administratorService.save(currentAdministrator);
		
		return ResponseEntity.status(HttpStatus.OK).body(schoolFromDB);
		
	}
	
	
	
	@DeleteMapping("administrator/{dni}/school")
	public ResponseEntity quitSchoolToAdministrator(@PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		Administrator currentAdministrator = administratorService.get(dni);		

		if (currentAdministrator == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona no existe o no cuenta con privilegios de administrador")	
			);
		}
		
		
		if (currentAdministrator.getSchoolSetted() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "No hay ninguna escuela asignada")	
			);
		}
		
		Integer schoolId = currentAdministrator.getSchoolSetted().getId();
		
		currentAdministrator.setSchoolSetted(null);
		
		administratorService.save(currentAdministrator);
		
		
		if (schoolService.get(schoolId).getAdministrators().isEmpty()) {
			schoolService.removeSchool(schoolId);
		}
		
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}


	/* ============================================= Tutor laboral ======================================== */
	
	@GetMapping("labor-tutor")
	public ResponseEntity getTutors(@RequestParam(required = false) String name) {
		List<LaborTutor> tutors;						// Obtenemos los administradores
		
		if (name == null) {
			tutors = laborTutorService.getAll();
		}
		else {
			tutors = laborTutorService.getAllByName(name);
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
		dni = dni.toUpperCase();
		
		if (laborTutorService.getById(dni) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona a borrar no existe o no es un tutor")		// Si no existe lo indicamos
			);
		}
		
		laborTutorService.remove(dni);																// Borramos el tutor
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();												// Devolvemos el código de estado
	}
	
	
	/* Segundo nivel */
	
	
	@GetMapping("labor-tutor/{dni}/practise")
	public ResponseEntity getPractisesFromLaborTutor(@PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		LaborTutor laborTutor = laborTutorService.getById(dni);							// Obtenemos el administrador

		if (laborTutor == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")			//  Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					laborTutor.getPractises()
			);
		}
	}

	
	@GetMapping("labor-tutor/{dni}/business")
	public ResponseEntity getBusinessFromLaborTutor(@PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		LaborTutor laborTutor = laborTutorService.getById(dni);							// Obtenemos el administrador

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
					laborTutor.getBusiness()
			);
		}
	}
	
	@PostMapping("labor-tutor/{dni}/business")
	public ResponseEntity setBusinessFromLaborTutor(@PathVariable String dni, @RequestBody Business business) {
		
		dni = dni.toUpperCase();
		
		LaborTutor laborTutor = laborTutorService.getById(dni);							// Obtenemos el administrador

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
		
		laborTutor.setBusiness(businessDB);
		laborTutorService.save(laborTutor);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				businessDB
		);
	}
	
	
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
		List<Teacher> teachers;																		// Obtenemos los profesores
		
		if (name == null) {
			teachers = teacherService.getAll();
		}
		else {
			teachers = teacherService.getAllByName(name);
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

		dni = dni.toUpperCase();
		
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
		dni = dni.toUpperCase();
		
		if (teacherService.get(dni) == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "La persona a borrar no existe o no es un profesor")				// Si el profesor no existe o no es profesor lo indicamos
			);
		}
		
		if (teacherService.get(dni).getRol() == Rol.ROLE_ADMIN ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "No se puede eliminar un administrador desde profesores")				
			);
		}
		else {
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
		
		Teacher currentTeacher = teacherService.get(dni);												// Obtenemos el profesor
		

		if (currentTeacher == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "El profesor no existe")						// Si no existe lanzamos error
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
			currentTeacher.getProfessionalDegrees()														// Obtenemos los ciclos y lo devolvemos
		);
	}

	
	@GetMapping("teacher/{dni}/practise")
	public ResponseEntity getPractises(@PathVariable String dni) {
		dni = dni.toUpperCase();
		
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

	@GetMapping("student")
	public ResponseEntity getStudents(@RequestParam(required = false) String name) {
		List<Student> students;													// Obtenemos los profesores

		if (name == null) {
			 students = studentService.getAll();
		}
		else {
			 students = studentService.getAllByName(name);
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
	
	@PutMapping("student/{dni}")
	public ResponseEntity updateStudent(@RequestBody PersonDTO student, @PathVariable String dni) {
		
		dni = dni.toUpperCase();
		
		Student currentStudent = studentService.get(dni);												// Obtenemos el administrador
		
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
		
		return ResponseEntity.status(HttpStatus.OK).body(
				studentService.updateStudent(currentStudent, student)							// Actualizamos el administrador y devolvemos el resultado
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

		List<School> schools;
				
		if (name != null) {
			schools = schoolService.getAllByName(name);
		}
		else {
			schools = schoolService.getAll();
			
			for (School school : schools) {
				school.setPassword("");
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
					new RestError(HttpStatus.BAD_REQUEST, "Indique la dirección al centro")					
			);
		}
		else if (school.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique imagen del centro")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique una contraseña")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getOpeningTime() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique hora de apertura")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getClosingTime() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique hora de cierre")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getOpeningTime().after(school.getClosingTime())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Horario no válido")					// Si no nos indican el nombre lo avisamos
			);
		}
		
		school.setId(null);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				schoolService.save(school)
		);
	}
	
	
	@PutMapping("school/{id}")
	public ResponseEntity editSchool(@RequestBody School school, @PathVariable Integer id) {
		
		School currentSchool = schoolService.get(id);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre para el centro")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getAddress() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la dirección al centro")					
			);
		}
		else if (school.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique imagen del centro")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique una contraseña")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getOpeningTime() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique hora de apertura")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getClosingTime() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique hora de cierre")					// Si no nos indican el nombre lo avisamos
			);
		}
		else if (school.getOpeningTime().after(school.getClosingTime())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Horario no válido")					// Si no nos indican el nombre lo avisamos
			);
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(
				schoolService.updateSchool(school, currentSchool.getId())
		);
	}
	
	
	/**
	 * Borra una determinada escuela
	 * @param idSchool: Identificador de la escuela
	 */
	@DeleteMapping("school/{idSchool}")
	public ResponseEntity deleteSchool(@PathVariable Integer idSchool) {

		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Escuela no encontrada")							// Si no existe lo indicamos
			);
		}
		
		administratorService.quitAdministratorsFromSchool(currentSchool);
		
		schoolService.removeSchool(idSchool);																// Borramos la escuela
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();										// Devolvemos un 204
	}
	
	/* Segundo nivel */
	
	
	// ================================================ Registros de temperatura ================================================
	
	@GetMapping("school/{idSchool}/reg-temp")
	public ResponseEntity getRegTempsByDate(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
			return ResponseEntity.status(HttpStatus.OK).body(
					schoolService.getRegTempsByDate(idSchool, new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
			);
	}
	
	
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
	
	
	@PostMapping("school/{idSchool}/reg-temp")
	public ResponseEntity getRegTemps(@PathVariable Integer idSchool, @RequestBody RegTemp newRegTemp) {
		
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		
		if (newRegTemp.getHumidity() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique humedad")							// Si no existe lo indicamos
			);
		}
		else if (newRegTemp.getCelcius() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la temperatura en Celcius")							// Si no existe lo indicamos
			);
		}
		else if (newRegTemp.getFahrenheit() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la temperatura en Fahrenheit")							// Si no existe lo indicamos
			);
		}
		else if (newRegTemp.getHeatIndexc() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique índice de calor en Celcius")							// Si no existe lo indicamos
			);
		}
		else if (newRegTemp.getHeatIndexf() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique índice de calor en Fahrenheit")							// Si no existe lo indicamos
			);
		}
		
		newRegTemp.setId(null);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(
				schoolService.addRegTemp(currentSchool, newRegTemp)
		);
		
	}
	
	
	// ================================================ Movimientos inusuales ================================================
	
	
	@GetMapping("school/{idSchool}/movement")
	public ResponseEntity getUnusualMovements(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				currentSchool.getUnusualsMovements()							// Si no existe lo indicamos
		);
	}
	

	@PostMapping("school/{idSchool}/movement")
	public ResponseEntity getUnusualMovements(@PathVariable Integer idSchool, @RequestBody UnusualMovement unusualMovement) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		unusualMovement.setId(null);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
			schoolService.addUnusualMovement(currentSchool, unusualMovement)
		);
	}
	
	@DeleteMapping("school/{idSchool}/movement")
	public ResponseEntity deleteUnusualMovements(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		schoolService.removeAllUnsualsMovements(idSchool);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	// ================================================ Localización ================================================

	
	@GetMapping("school/{idSchool}/location")
	public ResponseEntity getLocation(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		
		if (currentSchool.getLocation() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Localización no establecida")							// Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					currentSchool.getLocation()
			);
		}

	}
	
	
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
					new RestError(HttpStatus.CONFLICT, "La localización ya está establecida")							
			);
		}
		else {
			if (location.getLatitude() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "Indique una latitud válida")							
				);
			}
			else if (location.getLongitude() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "Indique una longitud válida")							
				);
			}
			
			location.setId(null);
			
			
			return ResponseEntity.status(HttpStatus.CREATED).body(
					schoolService.setLocation(idSchool, location)							
			);
			
		}
	}
	
	@PutMapping("school/{idSchool}/location")
	public ResponseEntity editLocation(@PathVariable Integer idSchool, @RequestBody Location location) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
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
						new RestError(HttpStatus.BAD_REQUEST, "Indique una latitud válida")							
				);
			}
			else if (location.getLongitude() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "Indique una longitud válida")							
				);
			}
			
			Location locationUpdated;

			try {				
				locationUpdated = locationService.editLocation(currentSchool.getLocation().getId(), location.getLongitude(), location.getLatitude());
			}
			catch(Exception e) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(
						new RestError(HttpStatus.CONFLICT, e.getMessage())							
				);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(
					locationUpdated
			);
		}
		
	}
	
	@DeleteMapping("school/{idSchool}/location")
	public ResponseEntity removeLocation(@PathVariable Integer idSchool, @RequestBody Location location) {
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
		
		schoolService.removeLocation(idSchool);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	// ================================================ Ciclos ================================================

	
	@GetMapping("school/{idSchool}/degree")
	public ResponseEntity getDegree(@RequestParam(required = false) Integer year,  @PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		if (year != null) {
			return ResponseEntity.status(HttpStatus.OK).body(
				professionalDegreeService.getAllProfessionalDegreesByYear(idSchool, year)
			);	
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
				schoolService.getAllProfessionalDegrees(idSchool)
			);			
		}
		
	}
	
	@PostMapping("school/{idSchool}/degree")
	public ResponseEntity addDegree(@PathVariable Integer idSchool, @RequestBody ProfessionalDegree degree) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		
		if (degree.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre")									// Si no existe lo indicamos
			);
		}
		else if (degree.getYear() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un año")										// Si no existe lo indicamos
			);
		}
		else if (degree.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la imagen del ciclo")						// Si no existe lo indicamos
			);
		}
		
		degree.setId(null);		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				schoolService.addDegreeToSchool(idSchool, degree)
		);
	}
	
	@PutMapping("school/{idSchool}/degree/{idDegree}")
	public ResponseEntity editDegree(@PathVariable Integer idSchool, @RequestBody ProfessionalDegree degree, @PathVariable Integer idDegree) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		ProfessionalDegree pf = professionalDegreeService.get(idDegree);
		
		if (pf == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Ciclo no encontrado")									// Si no existe lo indicamos
			);
		}
		
		if (degree.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre")									// Si no existe lo indicamos
			);
		}
		else if (degree.getYear() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un año")										// Si no existe lo indicamos
			);
		}
		else if (degree.getImage() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la imagen del ciclo")						// Si no existe lo indicamos
			);
		}
		
		degree.setId(null);		
		
		pf.setName(degree.getName());
		pf.setImage(degree.getImage());
		pf.setYear(degree.getYear());
		
		return ResponseEntity.status(HttpStatus.OK).body(
				professionalDegreeService.saveProfessionalDegree(pf)
		);
	}
	
	
	
	@DeleteMapping("school/{idSchool}/degree/{idDegree}")
	public ResponseEntity removeDegree(@PathVariable Integer idSchool, @PathVariable Integer idDegree) {
		School currentSchool = schoolService.get(idSchool);
		
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
		
		professionalDegreeService.removeDegree(idDegree);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	// ================================================ Ciclos profesionales ================================================
	
	
	@GetMapping("degree/{idDegree}")
	public ResponseEntity getDegree(@PathVariable Integer idDegree) {
		
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
			professionalDegree
		);
	}
	
	
	@GetMapping("degree")
	public ResponseEntity getDegrees() {
		
		List<ProfessionalDegree> professionalDegrees = professionalDegreeService.getAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(
			professionalDegrees
		);
	}
	
	
	
	@GetMapping("degree/{idDegree}/school")
	public ResponseEntity getSchoolFromDegree(@PathVariable Integer idDegree) {
		
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		if (professionalDegree.getSchool() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Este ciclo no está asignado a ningún centro")									// Si no existe lo indicamos
			);
		}
		else {			
			return ResponseEntity.status(HttpStatus.OK).body(
					professionalDegree.getSchool()
			);
		}
	}
	
	
	@GetMapping("degree/{idDegree}/teacher")
	public ResponseEntity getTeachersFromDegree(@PathVariable Integer idDegree) {
		
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
			professionalDegree.getTeachers()
		);
	}
	
	
	@PostMapping("degree/{idDegree}/teacher")
	public ResponseEntity addTeachersFromDegree(@PathVariable Integer idDegree, @RequestBody Teacher teacher) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		else if (teacher.getDni() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique dni del profesor")		
			);
		}
		
		teacher.setDni(teacher.getDni().toUpperCase());
		
		Teacher teacherDB = teacherService.get(teacher.getDni());
		
		if (teacherDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El profesor no existe")									
			);
		}
		else if (professionalDegree.getTeachers().contains(teacherDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "El profesor ya está asignado a este ciclo")									
			);
		}
		
		professionalDegreeService.addTeacherToDegree(professionalDegree, teacherDB);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
			teacherDB
		);
	}
	
	@DeleteMapping("degree/{idDegree}/teacher/{idTeacher}")
	public ResponseEntity quitTeacherFromDegree(@PathVariable Integer idDegree, @PathVariable String idTeacher) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		else if (idTeacher == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "Indique dni del profesor")		
			);
		}

		Teacher teacherDB = teacherService.get(idTeacher.toUpperCase());
		
		if (teacherDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El profesor no existe")									
			);
		}
		else if (!professionalDegree.getTeachers().contains(teacherDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "El profesor no está asignado a este ciclo")									
			);
		}
		
		professionalDegreeService.quitTeacherFromDegree(professionalDegree, teacherDB);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	// ================================================ Enrollment ================================================

	
	@GetMapping("degree/{idDegree}/enrollment")
	public ResponseEntity getEnrollmentsFromDegree(@PathVariable Integer idDegree) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				professionalDegree.getEnrollments()
		);		
	}
	
	
	@PostMapping("degree/{idDegree}/enrollment")
	public ResponseEntity getEnrollmentsFromDegree(@PathVariable Integer idDegree, @RequestBody NewEnrollmentDTO enrollment) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		if (enrollment.getDate() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la fecha de matriculación")									// Si no existe lo indicamos
			);
		}
		
		if (enrollment.getDniStudent() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el dni del alumno")									// Si no existe lo indicamos
			);
		}
		
		enrollment.setDniStudent(enrollment.getDniStudent().toUpperCase());

		
		Student currentStudent = studentService.get(enrollment.getDniStudent());
		
		if (currentStudent == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El alumno no existe")									// Si no existe lo indicamos
			);
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(enrollment.getDate());
		
		if (enrollmentService.existEnrollment(calendar.get(Calendar.YEAR), professionalDegree, currentStudent)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "El alumno ya ha sido matriculado este año en este ciclo")									// Si no existe lo indicamos
			);
		}
		
		Enrollment newEnrollment = new Enrollment(enrollment.getDate(), currentStudent, professionalDegree);
		
		newEnrollment = enrollmentService.save(newEnrollment);
		
		newEnrollment.setProfessionalDegree(professionalDegree);
		
		newEnrollment = enrollmentService.save(newEnrollment);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				newEnrollment
		);		
	}
	
	
	@DeleteMapping("degree/{idDegree}/enrollment/{enrollmentId}")
	public ResponseEntity removeEnrollmentsFromDegree(@PathVariable Integer idDegree,@PathVariable Integer enrollmentId) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Este ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Esta matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (!professionalDegree.getEnrollments().contains(enrollment)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Esta matrícula no pertenece a este ciclo")									// Si no existe lo indicamos
			);
		}
		
		enrollmentService.delete(enrollment);
		
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();		
	}
	
	// ================================================ Enrollment ================================================

	
	@GetMapping("enrollment")
	public ResponseEntity getEnrollments() {
		List<Enrollment> enrollments = enrollmentService.getAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollments
		);		
	}
	
	@GetMapping("enrollment/{enrollmentId}")
	public ResponseEntity getEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment
		);		
	}
	
	@GetMapping("enrollment/{enrollmentId}/student")
	public ResponseEntity getStudentFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getStudent() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Alumno no establecido")									// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment.getStudent()
		);		
	}
	
	// ================================================ Degree ================================================ 
	
	@GetMapping("enrollment/{enrollmentId}/degree")
	public ResponseEntity getDegreeFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getProfessionalDegree() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Ciclo no establecido")									// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment.getProfessionalDegree()
		);
	}
	
	// ================================================ Preference ================================================ 
	
	@GetMapping("enrollment/{enrollmentId}/preference")
	public ResponseEntity getPreferencesFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment.getPreferences()
		);
	}
	
	@PostMapping("enrollment/{enrollmentId}/preference")
	public ResponseEntity addPreferencesFromEnrollment(@PathVariable Integer enrollmentId, @RequestBody NewPreferenceDTO newPreference) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
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

		
		newPreference.setCif(newPreference.getCif().toUpperCase());
		
		Business business = businessService.get(newPreference.getCif());
		
		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")									// Si no existe lo indicamos
			);
		}
		else if (preferenceService.getByEnrollmentAndBusiness(enrollment, business) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "La empresa ya está asignada como preferida")									// Si no existe lo indicamos
			);
		}
		else if (!enrollment.getProfessionalDegree().getBusinesses().contains(business)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Esta empresa no está disponible para el ciclo que estás cursando")									// Si no existe lo indicamos
			);
		}
		newPreference.setPosition(enrollment.getPreferences().size());
		
		Preference preferenceToAdd = new Preference(newPreference.getPosition(), business, enrollment);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				preferenceService.save(preferenceToAdd)
		);
	}
	

	@PutMapping("enrollment/{enrollmentId}/preference")
	public ResponseEntity updateAllPreferencesFromEnrollment(@PathVariable Integer enrollmentId, @RequestBody UpdatePreferencesDTO newPositions) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (newPositions.getPreferences() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "No se ha indicado ninguna preferencia")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getPreferences().size() != newPositions.getPreferences().size()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "El número de preferencias no coincide")								
			);
		}
		else if (!preferenceService.checkIfExistAll(newPositions.getPreferences())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Existen identificadores nulos o no existentes")								
			);
		}
		else if (!preferenceService.checkIfOrderIsCorrect(newPositions.getPreferences())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El orden de las preferencias no es correcto")								
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				preferenceService.updatePositions(newPositions.getPreferences())
		);
		
	}
	
	
	@DeleteMapping("enrollment/{enrollmentId}/preference")
	public ResponseEntity deleteAllPreferencesFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getPreferences().size() == 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "No existen preferencias")									// Si no existe lo indicamos
			);
		}
		
		preferenceService.deleteAllPreferenceFromEnrollment(enrollment);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	// ================================================ Preferencias =============================================
	
	
	@GetMapping("preference/{idPreference}")
	public ResponseEntity getPreference(@PathVariable Integer idPreference) {
		Preference preference = preferenceService.get(idPreference);
		
		if (preference == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La preferencia no existe")									// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				preference
		);
	}
	
	@GetMapping("preference/{idPreference}/business")
	public ResponseEntity getBusinessFromPreference(@PathVariable Integer idPreference) {
		Preference preference = preferenceService.get(idPreference);
		
		if (preference == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La preferencia no existe")									// Si no existe lo indicamos
			);
		}
		if (preference.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Empresa no establecida")									// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				preference.getBusiness()
		);
	}
	
	@GetMapping("preference/{idPreference}/enrollment")
	public ResponseEntity getEnrollmentFromPreference(@PathVariable Integer idPreference) {
		Preference preference = preferenceService.get(idPreference);
		
		if (preference == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La preferencia no existe")									// Si no existe lo indicamos
			);
		}
		if (preference.getEnrollment() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Matrícula no establecida")									// Si no existe lo indicamos
			);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				preference.getEnrollment()
		);
	}
	
	
	// ================================================ Practicas ================================================
	
	@GetMapping("enrollment/{enrollmentId}/practise")
	public ResponseEntity getPractiseFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getPractise() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Prácticas no comenzadas")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				enrollment.getPractise()
		);
	}
	
	@PostMapping("enrollment/{enrollmentId}/practise")
	public ResponseEntity addPractiseFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getPractise() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "La matrícula ya tiene práctica asignada")									// Si no existe lo indicamos
			);
		}
		
		Practise newPractise = new Practise();
		newPractise = practiseService.save(newPractise);
		
		enrollment.setPractise(newPractise);
		enrollmentService.save(enrollment);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				newPractise
		);
	}
	
	
	@DeleteMapping("enrollment/{enrollmentId}/practise")
	public ResponseEntity removePractiseFromEnrollment(@PathVariable Integer enrollmentId) {
		Enrollment enrollment = enrollmentService.get(enrollmentId);
		
		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no existe")									// Si no existe lo indicamos
			);
		}
		else if (enrollment.getPractise() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La matrícula no tiene práctica asignada")									// Si no existe lo indicamos
			);
		}
		
		Practise practiseToRemove = enrollment.getPractise();
		
		enrollment.setPractise(null);
		enrollmentService.save(enrollment);
		
		practiseService.remove(practiseToRemove.getId());
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	// ================================================ Practise ================================================ 	
	
	
	@GetMapping("practise/{practiseId}")
	public ResponseEntity getPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise
		);
	}
	
	
	@PutMapping("practise/{practiseId}")
	public ResponseEntity editPractiseFromEnrollment(@PathVariable Integer practiseId, @RequestBody Practise practiseData) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		

		
		if (practiseData.getStart() != null && practiseData.getFinish() != null) {
			if (practiseData.getStart().after(practiseData.getFinish())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "La fecha de fin debe ser posterior a la de inicio")									
				);
			}
			else {
				practise = practiseService.updatePractise(practise, practiseData);
			}

		}
		else if (practiseData.getStart() != null && practise.getFinish() != null && practiseData.getStart().after(practise.getFinish())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "La fecha de inicio no puede ser superior a la de finalización")									// Si no existe lo indicamos
			);
		}
		else if (practiseData.getFinish() != null && practise.getStart() != null && practiseData.getFinish().before(practise.getStart())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "La fecha de fin no puede ser antes a la de inicio")									// Si no existe lo indicamos
			);
		}
		else {
			practise = practiseService.updatePractise(practise, practiseData);
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise
		);
	}
	
	
	// ================================================== Enrollment ==================================================
	
	@GetMapping("practise/{practiseId}/enrollment")
	public ResponseEntity getEnrollmentFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getEnrollment() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Matrícula no está establecida")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise.getEnrollment()
		);
	}
	
	// ================================================== Profesor ==================================================
	
	
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
					new RestError(HttpStatus.NOT_FOUND, "Profesor no establecido")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise.getTeacher()
		);
	}
	
	@PostMapping("practise/{practiseId}/teacher")
	public ResponseEntity setTeacherFromPractise(@PathVariable Integer practiseId, @RequestBody Teacher teacher) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getTeacher() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Profesor ya establecido")									// Si no existe lo indicamos
			);
		}
		else if (teacher.getDni() == null || teacher.getDni().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")									// Si no existe lo indicamos
			);
		}

		teacher.setDni(teacher.getDni().toUpperCase());
		
		Teacher teacherDB = teacherService.get(teacher.getDni());
		
		if (teacherDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El profesor no existe")									// Si no existe lo indicamos
			);
		}
		
		practise.setTeacher(teacherDB);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getTeacher()
		);
	}
	
	@PutMapping("practise/{practiseId}/teacher")
	public ResponseEntity editTeacherFromPractise(@PathVariable Integer practiseId, @RequestBody Teacher teacher) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (teacher.getDni() == null || teacher.getDni().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")									// Si no existe lo indicamos
			);
		}

		teacher.setDni(teacher.getDni().toUpperCase());
		
		Teacher teacherDB = teacherService.get(teacher.getDni());
		
		if (teacherDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El profesor no existe")									// Si no existe lo indicamos
			);
		}
		
		practise.setTeacher(teacherDB);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getTeacher()
		);
	}
	
	@DeleteMapping("practise/{practiseId}/teacher")
	public ResponseEntity quitTeacherFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getTeacher() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Profesor no establecido")									// Si no existe lo indicamos
			);
		}
		
		practise.setTeacher(null);
		practiseService.save(practise);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	// ================================================== Tutor laboral ==================================================
	
	
	@GetMapping("practise/{practiseId}/labor-tutor")
	public ResponseEntity getLaborTutorFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getLaborTutor() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Tutor laboral no establecido")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise.getLaborTutor()
		);
	}
	
	@PostMapping("practise/{practiseId}/labor-tutor")
	public ResponseEntity setLaborTutorFromPractise(@PathVariable Integer practiseId, @RequestBody LaborTutor laborTutor) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getLaborTutor() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Tutor laboral ya establecido")									// Si no existe lo indicamos
			);
		}
		else if (laborTutor.getDni() == null || laborTutor.getDni().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")									// Si no existe lo indicamos
			);
		}

		laborTutor.setDni(laborTutor.getDni().toUpperCase());
		
		LaborTutor laborTutorDB = laborTutorService.getById(laborTutor.getDni());
		
		if (laborTutorDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")									// Si no existe lo indicamos
			);
		}
		
		practise.setLaborTutor(laborTutorDB);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getLaborTutor()
		);
	}
	
	
	@PutMapping("practise/{practiseId}/labor-tutor")
	public ResponseEntity editLaborTutorFromPractise(@PathVariable Integer practiseId, @RequestBody LaborTutor laborTutor) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (laborTutor.getDni() == null || laborTutor.getDni().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")									// Si no existe lo indicamos
			);
		}

		laborTutor.setDni(laborTutor.getDni().toUpperCase());
		
		LaborTutor laborTutorDB = laborTutorService.getById(laborTutor.getDni());
		
		if (laborTutorDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El tutor no existe")									// Si no existe lo indicamos
			);
		}
		
		practise.setLaborTutor(laborTutorDB);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getLaborTutor()
		);
	}
	
	@DeleteMapping("practise/{practiseId}/labor-tutor")
	public ResponseEntity quitLaborTutorFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getLaborTutor() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Tutor laboral no establecido")									// Si no existe lo indicamos
			);
		}
		
		practise.setLaborTutor(null);
		practiseService.save(practise);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	// ================================================== Empresa ==================================================
	
	@GetMapping("practise/{practiseId}/business")
	public ResponseEntity getBusinessFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Empresa no establecida")									// Si no existe lo indicamos
			);
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practise.getBusiness()
		);
	}
	
	@PutMapping("practise/{practiseId}/business")
	public ResponseEntity setNewBusinessFromPractise(@PathVariable Integer practiseId, @RequestBody Business business) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Empresa no establecida")									// Si no existe lo indicamos
			);
		}
		else if (business.getCif() == null || business.getCif().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un cif válido")									// Si no existe lo indicamos
			);
		}

		business.setCif(business.getCif().toUpperCase());
		
		Business businessDB = businessService.get(business.getCif());
		
		if (businessDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getEnrollment() != null && practise.getEnrollment().getProfessionalDegree() != null && !practise.getEnrollment().getProfessionalDegree().getBusinesses().contains(businessDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Esta empresa no está disponible para el ciclo que cursa")									// Si no existe lo indicamos
			);
		}
		else if(businessService.getCountofStudentInBusinessInThisYear(businessDB) >= businessDB.getNumberOfStudents()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Esta empresa no admite más alumnos en este año")									// Si no existe lo indicamos
			);
		}
		
		practise.setBusiness(businessDB);
		
		return ResponseEntity.status(HttpStatus.OK).body(
				practiseService.save(practise).getBusiness()
		);
	}


	@PostMapping("practise/{practiseId}/business")
	public ResponseEntity setBusinessFromPractise(@PathVariable Integer practiseId, @RequestBody Business business) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getBusiness() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Empresa ya establecida")									// Si no existe lo indicamos
			);
		}
		else if (business.getCif() == null || business.getCif().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un cif válido")									// Si no existe lo indicamos
			);
		}

		business.setCif(business.getCif().toUpperCase());
		
		Business businessDB = businessService.get(business.getCif());
		
		
		
		if (businessDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getEnrollment() != null && practise.getEnrollment().getProfessionalDegree() != null && !practise.getEnrollment().getProfessionalDegree().getBusinesses().contains(businessDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Esta empresa no está disponible para el ciclo que cursa")									// Si no existe lo indicamos
			);
		}
		else if(businessService.getCountofStudentInBusinessInThisYear(businessDB) >= businessDB.getNumberOfStudents()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Esta empresa no admite más alumnos en este año")									// Si no existe lo indicamos
			);
		}
		
		practise.setBusiness(businessDB);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				practiseService.save(practise).getBusiness()
		);
	}
	
	@DeleteMapping("practise/{practiseId}/business")
	public ResponseEntity quitBusinessFromPractise(@PathVariable Integer practiseId) {
		
		Practise practise = practiseService.get(practiseId);
		
		if (practise == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La práctica no existe")									// Si no existe lo indicamos
			);
		}
		else if (practise.getBusiness() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "Empresa no establecida")									// Si no existe lo indicamos
			);
		}
		
		practise.setBusiness(null);
		practiseService.save(practise);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	// ================================================ Business ================================================

	
	@GetMapping("degree/{idDegree}/business")
	public ResponseEntity getBusinessFromDegree(@PathVariable Integer idDegree) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				professionalDegree.getBusinesses()
		);		
	}

	
	@PostMapping("degree/{idDegree}/business")
	public ResponseEntity addBusinessFromDegree(@PathVariable Integer idDegree, @RequestBody Business business) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		else if (business.getCif() == null || business.getCif().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un CIF válido")									// Si no existe lo indicamos
			);
		}
		
		business.setCif(business.getCif().toUpperCase());
		
		Business businessDB = businessService.get(business.getCif());
		
		if (businessDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")									// Si no existe lo indicamos
			);
		}
		else if (professionalDegree.getBusinesses().contains(businessDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "La empresa ya está añadida a este ciclo")									// Si no existe lo indicamos
			);
		}
		
		businessDB.getDegrees().add(professionalDegree);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				businessService.save(businessDB)
		);		
	}
	
	
	@DeleteMapping("degree/{idDegree}/business/{businessId}")
	public ResponseEntity quitBusinessFromDegree(@PathVariable Integer idDegree, @PathVariable String businessId) {
		ProfessionalDegree professionalDegree = professionalDegreeService.get(idDegree);
		
		if (professionalDegree == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "El ciclo no existe")									// Si no existe lo indicamos
			);
		}
		
		Business businessDB = businessService.get(businessId.toUpperCase());
		
		if (businessDB == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "La empresa no existe")									// Si no existe lo indicamos
			);
		}
		else if (!professionalDegree.getBusinesses().contains(businessDB)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
					new RestError(HttpStatus.CONFLICT, "La empresa no está añadida a este ciclo")									// Si no existe lo indicamos
			);
		}
		
		businessDB.getDegrees().remove(professionalDegree);
		
		businessService.save(businessDB);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();		
	}

	// ================================================ Business ================================================
	
	
	/**
	 * Obtiene todas las empresas
	 * @return Listado de empresas
	 */
	@GetMapping("business")
	public ResponseEntity getAllBusiness(@RequestParam(required = false) String name) {
		List<Business> allBusiness;											// Obtenemos todas las empresas

		if (name != null) {
			allBusiness = businessService.getAllByName(name);
		}
		else {
			allBusiness = businessService.getAll();
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				allBusiness 																		// Si hay las retornamos
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
	
	
	@DeleteMapping("business/{cif}")
	public ResponseEntity deleteBusiness(@PathVariable String cif) {
	    
		Business business = businessService.get(cif.toUpperCase()); 
		
		if (business == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "No existe ninguna empresa con este CIF")			// Si no existe la empresa lo indicamos
			);
		}
		
		
		preferenceService.removeFromBusinessAndUpdatePositions(business);
		businessService.remove(cif.toUpperCase());
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/* Segundo nivel */
	
	@GetMapping("business/{cif}/practise")
	public ResponseEntity getPractisesFromBusiness(@PathVariable String cif) {

		Business business = businessService.get(cif.toUpperCase());									// Obtenemos la empresa

		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta empresa")		// Si no existe la empresa lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					business.getPractises()															// Si existe la retornamos
			);
		}
	}
	
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
					business.getTutors()															// Si existe la retornamos
			);
		}
	}
	
	// ================================================= Trabajador de contacto =================================================

	
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
					business.getContactWorkers()													// Si existe la retornamos
			);
		}
	}
	
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
					new RestError(HttpStatus.NOT_FOUND, "El contacto no existe")					// Si no existe la empresa lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					contact
			);
		}
	}
	
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
					new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre")					// Si no existe la empresa lo indicamos
			);
		}		
		else if (contactWorker.getEmail() == null || contactWorker.getEmail().trim().length() == 0 || !contactWorker.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un email")					// Si no existe la empresa lo indicamos
			);
		}
		else if (contactWorker.getTelefone() == null || contactWorker.getTelefone().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un teléfono")					// Si no existe la empresa lo indicamos
			);
		}
		
		contactWorker.setId(null);

		ContactWorker newContact = contactWorkerService.save(contactWorker);
		business.getContactWorkers().add(newContact);
		
		businessService.save(business);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				newContact
		);
	}
	
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
					new RestError(HttpStatus.BAD_REQUEST, "Indique un nombre")					// Si no existe la empresa lo indicamos
			);
		}		
		else if (contactWorker.getEmail() == null || contactWorker.getEmail().trim().length() == 0 || !contactWorker.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un email")					// Si no existe la empresa lo indicamos
			);
		}
		else if (contactWorker.getTelefone() == null || contactWorker.getTelefone().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un teléfono")					// Si no existe la empresa lo indicamos
			);
		}
		
		ContactWorker contact = contactWorkerService.getById(idContact);
		
		if (contact == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El contacto no existe")					
			);
		}
		
		contactWorker.setId(idContact);
		
		return ResponseEntity.status(HttpStatus.OK).body(
				contactWorkerService.edit(contactWorker)
		);
	}
	
	
	
	
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
					new RestError(HttpStatus.NOT_FOUND, "No existe este contacto")					// Si no existe la empresa lo indicamos
			);
		}
		else if (!business.getContactWorkers().contains(currentContact)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El contacto no pertenece a esta empresa")					// Si no existe la empresa lo indicamos
			);
		}

		business.getContactWorkers().remove(business.getContactWorkers().indexOf(currentContact));
		businessService.save(business);
		
		contactWorkerService.remove(idContact);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	// ================================================= Ubicación =================================================
	
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
					new RestError(HttpStatus.NOT_FOUND, "Ubicación aún no establecida")		// Si no existe la empresa lo indicamos
			);
		}
			
		return ResponseEntity.status(HttpStatus.OK).body(
			business.getLocation()													// Si existe la retornamos
		);
	}
	
	
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
					new RestError(HttpStatus.NOT_FOUND, "Ubicación ya establecida")		// Si no existe la empresa lo indicamos
			);
		}
		else if (location.getLatitude() == null || location.getLatitude() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique latitud")		// Si no existe la empresa lo indicamos
			);			
		}
		else if (location.getLongitude() == null || location.getLongitude() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique longitud")		// Si no existe la empresa lo indicamos
			);			
		}
		
		location.setId(null);
		locationService.saveLocation(location);
		
		business.setLocation(location);
			
		return ResponseEntity.status(HttpStatus.CREATED).body(
			businessService.save(business).getLocation()
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
					new RestError(HttpStatus.NOT_FOUND, "Ubicación no establecida")		// Si no existe la empresa lo indicamos
			);
		}
		else if (location.getLatitude() == null || location.getLatitude() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique latitud")		// Si no existe la empresa lo indicamos
			);			
		}
		else if (location.getLongitude() == null || location.getLongitude() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique longitud")		// Si no existe la empresa lo indicamos
			);			
		}
		
			
		return ResponseEntity.status(HttpStatus.OK).body(
				locationService.editLocation(business.getLocation().getId(), location.getLongitude(), location.getLatitude())
		);
	}
	
	
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
					new RestError(HttpStatus.NOT_FOUND, "Ubicación no establecida")		// Si no existe la empresa lo indicamos
			);
		}
		
		Location locationToRemove = business.getLocation();
	
		business.setLocation(null);
		
		businessService.save(business);
		
		locationService.removeLocation(locationToRemove.getId());
			
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}