package com.tx.practisesmanagement.controller;



import java.time.ZoneId;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tx.practisesmanagement.component.SmtpMailSender;
import com.tx.practisesmanagement.dto.MessageDTO;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.enumerators.Rol;
import com.tx.practisesmanagement.error.RestError;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.Enrollment;
import com.tx.practisesmanagement.model.Location;
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
@CrossOrigin(origins = "*")
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

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
			new RestError(HttpStatus.BAD_REQUEST, "Persona no registrada")										// La persona no existe por lo que indicamos que está disponible
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


	/* ============================================= Profesor =============================================  */

	/* Primer nivel */

	/**
	 * Permite obtener todos los profesores
	 * @return Lista de profesores
	 */
	@GetMapping("teacher")
	public ResponseEntity getTeachers() {
		List<Teacher> teachers = teacherService.getAll();													// Obtenemos los profesores

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
		else if (school.getOpeningTime().isAfter(school.getClosingTime())) {
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
		else if (school.getOpeningTime().isAfter(school.getClosingTime())) {
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
	public ResponseEntity getRegTempsByDate(@PathVariable Integer idSchool, @RequestParam(required = false)  @DateTimeFormat(pattern = "dd-MM-yyyy") Date date) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")							// Si no existe lo indicamos
			);
		}
		
		if (date != null) {
			if (date.after(new Date())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new RestError(HttpStatus.BAD_REQUEST, "No se soportan las previsiones aún")							
				);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(
					schoolService.getRegTempsByDate(idSchool, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
			);
			
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					currentSchool.getTemperatureRecords()
			);
		}
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
					new RestError(HttpStatus.BAD_REQUEST, "Indique la imagen del centro")						// Si no existe lo indicamos
			);
		}
		
		degree.setId(null);		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				schoolService.addDegreeToSchool(idSchool, degree)
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
	
	
	
	
	
//	
//	/**
//	 * Establece un administrador en una escuela
//	 * @param dni: Identificador del administrador
//	 * @param school: Objeto escuela que contiene el identificador de la escuela a añadir
//	 * @return Escuela asignada
//	 */
//	@PostMapping("school/{idSchool}/administrator")
//	public ResponseEntity addAdministrator(@PathVariable Integer idSchool, @RequestBody PersonDTO administrator) {
//		
//		if (schoolService.get(idSchool) == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")								// Si el centro no existe lo indicamos
//			);
//		}
//		else if (administrator.getDni() == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//					new RestError(HttpStatus.BAD_REQUEST, "Indique un dni")										// Si no nos han indicado el dni lo indicamos
//			);
//		}
//		
//		Administrator currentAdministrator = administratorService.get(administrator.getDni());					// Obtenemos el administrador
//		
//		if (currentAdministrator == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//					new RestError(HttpStatus.BAD_REQUEST, "El usuario no existe o no tiene privilegios para esta acción")	// Si no existe o no es un administrador lo indicamos
//			);
//		}
//		else if (currentAdministrator.getSchool() != null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//					new RestError(HttpStatus.BAD_REQUEST, "El administrador ya administra una escuela")						// Si el administrador ya administra una escuela lo indicamos
//			);
//		}
//
//		return ResponseEntity.status(HttpStatus.CREATED).body(
//				schoolService.addAdministratorToSchool(idSchool, currentAdministrator)										// Añadimos el administrador y lo devolvemos
//		);
//	}
//	
//	/**
//	 * Borra un administrador de una escuela
//	 * @param idSchool: Identificador de la escuela
//	 * @param dniAdministrator
//	 * @return
//	 */
//	@DeleteMapping("school/{idSchool}/administrator/{dniAdministrator}")
//	public ResponseEntity deleteAdministrator(@PathVariable Integer idSchool, @PathVariable String dniAdministrator) {
//		
//		School school = schoolService.get(idSchool);
//		
//		if (school == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")								// Verificamos si el centro existe					
//			);
//		}
//		
//		Administrator administrator = administratorService.get(dniAdministrator);
//		if (administrator == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//					new RestError(HttpStatus.BAD_REQUEST, "El administrador no existe")							// Si no existe lo indicamos 
//			);
//		}
//		if (!school.getAdministrators().contains(administrator)) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//					new RestError(HttpStatus.BAD_REQUEST, "El usuario no administra esta escuela")				// Si no existe o no administra esta escuela lo indicamos 
//			);
//		}
//		
//		schoolService.removeAdministrator(dniAdministrator);													// Borramos el administrador de la escuela							
//		
//		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();											// Retornamos un 204
//		
//		
//	}
	

	/**
	 * Permite obtener los administradores de una escuela determinada
	 * @return Lista de administradores
	 */
	@GetMapping("school/{idSchool}/administrator")
	public ResponseEntity getAdministratorsFromSchool(@PathVariable Integer idSchool) {

		if (schoolService.get(idSchool) == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")								// Si el centro no existe lo indicamos
			);
		}

		List<Administrator> administrators = schoolService.getAdministrators(idSchool);							// Obtenemos los administradores

		if (administrators.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El centro no dispone de administradores")				// Si no hay administradores lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(administrators);									// Si hay administradores lo indicamos
		}
	}

	/**
	 * Permite obtener los ciclos profesionales de una escuela determinada
	 * @return Lista de ciclos
	 */
	@GetMapping("school/{idSchool}/professional-degree")
	public ResponseEntity getProfessionalDegreeFromSchool(@PathVariable Integer idSchool) {

		if (schoolService.get(idSchool) == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")								// Si el centro no existe lo indicamos
			);
		}

		List<ProfessionalDegree> professionalDegree = schoolService.getAllProfessionalDegrees(idSchool);			// Obtenemos los ciclos

		if (professionalDegree.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "El centro no dispone de ciclos profesionales")			// Si no hay ciclos lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(professionalDegree);								// Si hay ciclos lo indicamos
		}
	}
	
	

	/**
	 * Permite editar un ciclo
	 * @param idSchool: Id del colegio
	 * @param idDegree: Id del ciclo
	 * @param newDegree: Nuevos datos del ciclo
	 * @return Ciclo editado
	 */
	@PutMapping("school/{idSchool}/professional-degree/{idDegree}")
	public ResponseEntity addProfessionalDegreeFromSchool(@PathVariable Integer idSchool, @PathVariable Integer idDegree, @RequestBody ProfessionalDegree newDegree) {

		School school = schoolService.get(idSchool);														// Obtenemos el colegio 
		
		if (school == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")							// Si no existe lo indicamos
			);
		}
		else if (newDegree.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el nuevo nombre del ciclo")				// Si no nos han indicado el nombre lo indicamos
			);
		}
		else if (!school.getProfessionalDegrees().contains(professionalDegreeService.get(idDegree))) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Ciclo no encontrado")							// Si el ciclo no existe lo indicamos
			);
		}

		return ResponseEntity.status(HttpStatus.OK).body(
				professionalDegreeService.updateDegree(idDegree, newDegree)								// Actualizamos el ciclo y lo retornamos
		);

	}
	

	
	/**
	 * Permite obtener un ciclo de una escuela determinada
	 * @param idSchool: Identificador de una escuela
	 * @param idDegree: Identificador del ciclo
	 * @return	Ciclo solicitado
	 */
	@GetMapping("school/{idSchool}/professional-degree/{idDegree}")
	public ResponseEntity getProfessionalDegreeFromSchool(@PathVariable Integer idSchool, @PathVariable Integer idDegree) {

		School school = schoolService.get(idSchool);												// Obtenemos la escuela
		
		if (school == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")					// Si no existe lo indicamos
			);
		}
		
		ProfessionalDegree degree = schoolService.getDegree(idSchool, idDegree);					// Obtenemos el ciclo de la escuela indicada
		
		if (degree == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El ciclo no existe o no pertenece a esta escuela")	// Si no existe o no pertenece a esta escuela lo indicamos 
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				degree																				// Si existe lo retornamos
		);
	}

	/**
	 * Permite añadir un ciclo a una escuela
	 * @param idSchool: Identificador de la escuela
	 * @param professionalDegree: Ciclo con los datos
	 * @return Ciclo creado
	 */
	@PostMapping("school/{idSchool}/professional-degree")
	public ResponseEntity addProfessionalDegreeFromSchool(@PathVariable Integer idSchool, @RequestBody ProfessionalDegree professionalDegree) {

		if (schoolService.get(idSchool) == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")							// Comprobamos si existe el ciclo
			);
		}
		else if (professionalDegree.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el nombre del ciclo")					// Si no nos han indicado el nombre lo indicamos
			);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(
				schoolService.createProfessionalDegree(idSchool, new ProfessionalDegree(professionalDegree.getName()))		// Creamos y retornamos el ciclo
		);

	}

	/**
	 * Permite borrar una escuela
	 * @param idSchool: Identificador de la escuela
	 * @param idDegree: Identificador del ciclo
	 */
//	@DeleteMapping("school/{idSchool}/professional-degree/{idDegree}")
//	public ResponseEntity addProfessionalDegreeFromSchool(@PathVariable Integer idSchool, @PathVariable Integer idDegree) {
//
//		School school = schoolService.get(idSchool);													// Obtenemos el colegio
//		
//		if (school == null) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//					new RestError(HttpStatus.BAD_REQUEST, "El centro no existe")						// Si no existe el colegio lo indicamos
//			);
//		}
//		else if (!school.getProfessionalDegrees().contains(new ProfessionalDegree(idDegree))) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//					new RestError(HttpStatus.BAD_REQUEST, "El ciclo no existe o no pertenece a esta escuela")		// Si no existe el ciclo o no pertenece a esta escuela lo indicamos
//			);
//		}
//		
//		professionalDegreeService.removeDegree(idDegree);															// Borramos el ciclo									
//
//		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();												// Retornamos un 204
//
//	}


	/* ========================================= Estudiante ========================================= */

	/* Primer nivel */


	/**
 	* Obtiene todos los estudiantes
 	* @return Lista de estudiantes
 	*/
	@GetMapping("student")
	public ResponseEntity getAllStudents() {
		List<Student> students = studentService.getAll();												// Obtenemos todos los estudiantes

		if (students.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existen estudiantes")						// Si no existen estudiantes lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					students																			// Si hay estudiantes lo retornamos
			);
		}
	}

	/**
	 * Obtiene un estudiante concreto
	 * @param dni: Dni del estudiante
	 * @return Estudiante solicitado
	 */
	@GetMapping("student/{dni}")
	public ResponseEntity getStudent(@PathVariable String dni) {
		Student student = studentService.get(dni);													// Estudiante solicitado

		if (student == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe el estudiante")					// Si no existe lo indicamos				
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					student																			// Retornamos el estudiante
			);
		}
	}


	/**
	 * Crea un nuevo estudiante
	 * @param student: Datos del estudiante 
	 * @return Estudiante
	 */
	@PostMapping("student")
	public ResponseEntity createStudent(@RequestBody PersonDTO student) {
		if (student.getDni() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el dni del estudiante")						// Si no nos han pasado ningún dni lo indicamos
			);
		}
		else if (student.getName() == null || student.getLastName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el nombre del estudiante y sus apellidos")	// Si no nos han pasado un nombre o apellido lo indicamos
			);
		}
		else if (student.getPassword() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la contraseña del estudiante")				// Si no nos han pasado la contraseña lo indicamos
			);
		}
		else if (student.getEmail() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique un correo")									// Si no no han pasado un correo lo indicamos
			);
		}
		else if (personService.existPerson(student.getDni())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Esta persona ya está registrada")					// Si la persona ya está registrada lo indicamos
			);
		}

		student.setPassword(passwordEncoder.encode(student.getPassword()));										// Ciframos la contraseña
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				studentService.save(new Student(student))														// Retornamos el estudiante
		);
	}
	
	/**
	 * Permite editar un estudiante 
	 * @param student: Datos del nuevo estudiante
	 * @param dni: Dni del estudiante
	 * @return Estudiante editado
	 */
	@PutMapping("student/{dni}")
	public ResponseEntity updateStudent(@RequestBody PersonDTO student, @PathVariable String dni) {
		
		Student currentStudent = studentService.get(dni);												// Obtenemos el estudiante
		
		if (currentStudent == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new RestError(HttpStatus.NOT_FOUND, "La persona no existe o no es un estudiante")		// Si no existe lo indicamos
			);
		}
		else if (currentStudent.getAddress() == null || currentStudent.getBirthDate() == null || currentStudent.getLastName() == null || currentStudent.getName() == null || currentStudent.getPassword() == null || currentStudent.getTelefone() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "Indique todos los datos")						// Si no nos han pasado todos los datos lo indicamos
			);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				studentService.updateStudent(currentStudent, student)									// Actualizamos el estudiante y retornamos el estudiante resultante
		);
	}
	
	/**
	 * Permite borrar un estudiante
	 * @param dni: Dni del estudiante
	 * @return Estudiante editado
	 */
	@DeleteMapping("student/{dni}")
	public ResponseEntity deleteStudent(@PathVariable String dni) {
		
		if (studentService.get(dni) == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new RestError(HttpStatus.BAD_REQUEST, "La persona a borrar no existe o no es un estudiante")		// Si no existe el estudiante lo indicamos 
			);
		}
		
		studentService.deleteStudent(dni);																			// Borramos el estudiante
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();												// Retornamos un 204
	}



	/* Segundo nivel */


	/* ========================================= Matrícula ========================================= */

	/* Primer nivel */

	/**
	 * Obtiene todas las matrículas
	 * @return Lista de matrículas
	 */
	@GetMapping("enrollment")
	public ResponseEntity getAllEnrollments() {

		List<Enrollment> enrollments = enrollmentService.getAll();									// Obtenemos todas las matrículas

		if (enrollments.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existen matriculaciones")				// Si no hay ninguna lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					enrollments																		// Si hay las retornamos
			);
		}
	}

	
	/**
	 * Obtiene una determinada matrícula
	 * @param id: Identificador de la matrícula
	 * @return Matrícula solicitada
	 */
	@GetMapping("enrollment/{id}")
	public ResponseEntity getEnrollment(@PathVariable Integer id) {

		Enrollment enrollment = enrollmentService.get(id);											// Matrícula solicitada

		if (enrollment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existe esta matrícula")					// Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					enrollment																		// Si existe la retornamos
			);
		}
	}

	/**
	 * Crea una nueva matrícula
	 * @param enrollment: Datos de la matrícula 
	 * @return Matrícula creada
	 */
	@PostMapping("enrollment")
	public ResponseEntity createEnrollment(@RequestBody Enrollment enrollment) {
		if (enrollment.getDate() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la fecha de matriculación del estudiante")		// Establecemos la fecha	
			);
		}
		
		// Si no existe el estudiante
		// Si no existe el ciclo
		
		// Establecer el ciclo 
		// Establecer el estudiante 

		return ResponseEntity.status(HttpStatus.CREATED).body(
				enrollmentService.save(new Enrollment(enrollment.getDate()))										// Creamos y retornamos el resultado
		);
	}

	/* Segundo nivel */


	/* ========================================= Empresa ========================================= */

	/* Primer nivel */


	/**
	 * Obtiene todas las empresas
	 * @return Listado de empresas
	 */
	@GetMapping("business")
	public ResponseEntity getAllBusiness() {

		List<Business> allBusiness = businessService.getAll();											// Obtenemos todas las empresas

		if (allBusiness .isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No existen empresas")							// Si no hay empresas lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					allBusiness 																		// Si hay las retornamos
			);
		}
	}

	/**
 	* Obtiene una determinada empresa
 	* @return Empresa solicitada
 	*/
	@GetMapping("business/{cif}")
	public ResponseEntity getBusiness(@PathVariable String cif) {

		Business business = businessService.get(cif);									// Obtenemos la empresa

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
		if (business.getCif() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el cif de la empresa")						// Si no nos indican el cif lo indicamos
			);
		}
		else if (business.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el nombre de la empresa")					// Si no nos indican el nombre de la empresa lo indicamos
			);
		}
		else if (business.getNumberOfStudents() == null || business.getNumberOfStudents() < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique una cantidad de estudiantes válida")			// Si no nos han indicado cantidad de estudiantes o es incorrecta lo indicamos
			);
		}
		
		else if (businessService.exist(business)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Ya existe una empresa con este CIF")					// Si ya existe una empresa con este cif lo indicamos
			);
		}

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
		if (business.getCif() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el cif de la empresa")					// Si no nos han indicado el cif lo indicamos
			);
		}
		else if (business.getName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique el nombre de la empresa")				// Si no nos indican el nombre de la empresa lo indicamos
			);
		}
		else if (business.getNumberOfStudents() == null || business.getNumberOfStudents() < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique una cantidad de estudiantes válida")		//  Si la cantidad no es válida lo indicamos
			);
		}
		
		else if (!businessService.exist(business)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "No existe ninguna empresa con este CIF")			// Si no existe la empresa lo indicamos
			);
		}

		business.setCif(cif);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
				businessService.edit(business)															// Editamos la empresa y retornamos el retultado
		);
	}
	
	
	
	/* Segundo nivel */
	
}