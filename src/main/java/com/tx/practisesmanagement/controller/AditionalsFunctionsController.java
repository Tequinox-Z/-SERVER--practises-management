package com.tx.practisesmanagement.controller;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import com.tx.practisesmanagement.dto.BriefingDTO;
import com.tx.practisesmanagement.dto.EmailDTO;
import com.tx.practisesmanagement.dto.LocationAndBusinessDTO;
import com.tx.practisesmanagement.dto.LocationAndSchoolDTO;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.dto.ResetPasswordDTO;
import com.tx.practisesmanagement.dto.YearsDTO;
import com.tx.practisesmanagement.enumerators.TypeTokenToGenerate;
import com.tx.practisesmanagement.error.RestError;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.Business;
import com.tx.practisesmanagement.model.Location;
import com.tx.practisesmanagement.model.Person;
import com.tx.practisesmanagement.model.ProfessionalDegree;
import com.tx.practisesmanagement.model.RegTemp;
import com.tx.practisesmanagement.model.School;
import com.tx.practisesmanagement.model.UnusualMovement;
import com.tx.practisesmanagement.security.JWTUtil;
import com.tx.practisesmanagement.service.AditionalsFunctionsService;
import com.tx.practisesmanagement.service.AdministratorService;
import com.tx.practisesmanagement.service.BusinessService;
import com.tx.practisesmanagement.service.LocationService;
import com.tx.practisesmanagement.service.PersonService;
import com.tx.practisesmanagement.service.RegTempService;
import com.tx.practisesmanagement.service.SchoolService;
import com.tx.practisesmanagement.service.StudentService;
import com.tx.practisesmanagement.service.TeacherService;
import com.tx.practisesmanagement.service.UnusualMovementService;

/**
 * Controlador de funciones adicionales. Reune todas aquellas funciones adicionales
 * @author Salvador
 */
@RestController
@CrossOrigin(origins = {"https://tequinox-z.github.io/", "http://localhost:4200", "http://localhost:43205"})
public class AditionalsFunctionsController {
	
	// Servicios
	
		@Autowired private AdministratorService administratorService;		// Servicio de administrador
		@Autowired private StudentService studentService;					// Servicio de estudiante
		@Autowired private AditionalsFunctionsService aditinalsFuncions;	// Servicio de funciones adicionales
		@Autowired private TeacherService teacherService;					// Servicio de profesor
		@Autowired private SchoolService schoolService;						// Servicio de colegio
		@Autowired private PersonService personService;						// Sercicio de persona
		@Autowired private LocationService locationService;					// Servicio de localización
		@Autowired private RegTempService regTempService;					// Servicio de temperatura					
		@Autowired private UnusualMovementService unusualMovementService;	// Servicio de movimientos inusuales
		@Autowired private BusinessService businessService;					// Servicio de empresas
		
		@Autowired private AuthenticationManager authManager;				// Administrador de autenticación
		@Autowired private SmtpMailSender smtpMailSender;					// Componente de correo
		@Autowired private JWTUtil jwtUtil;									// JWT Util
		@Autowired private PasswordEncoder passwordEncoder;					// Codificador de contraseña
		
		@Value("${urlServer}")
		private String frontUrl;											// Url de front

	/**
	 * Obtiene un recuento de administradores, estudiantes, empresas y profesores
	 * @param idSchool
	 */
	@GetMapping("school/{idSchool}/briefing")
	public ResponseEntity getBriefingFromSchool(@PathVariable Integer idSchool) {
		
		School school = schoolService.get(idSchool);										// Obtenemos la escuela
		
		if (school == null) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        			new RestError(HttpStatus.NOT_FOUND, "El centro no existe")				// Si no existe el centro lo indicamos
    		);
		}
		
		BriefingDTO newBriefing = new BriefingDTO();										// Creamos un nuevo documento
		
		
		/* Asignamos todos los datos */
		
		newBriefing.setAdministrators(administratorService.getCountFromSchool(idSchool));
		newBriefing.setStudents(studentService.getCountFromSchool(idSchool));
		newBriefing.setBusiness(businessService.getCount());
		newBriefing.setTeachers(teacherService.getCountTeacherFromSchool(idSchool));
		
		return ResponseEntity.status(HttpStatus.OK).body(
				newBriefing 																// Retornamos el resultado
		);
	}
	

	/**
	 * Obtiene la ubicación de todos los centros
	 * @param maxLatitude Latitud máxima
	 * @param minLatitude Latitud mínima
	 * @param maxLongitude Longitud máxima
	 * @param minLongitude Longitud mínima
	 */
	@GetMapping("location/schools")
	public ResponseEntity getLocationsOfSchools(@QueryParam("maxLatitude") Double maxLatitude, @QueryParam("minLatitude") Double minLatitude, @QueryParam("maxLongitude") Double maxLongitude, @QueryParam("minLongitude") Double minLongitude) {
		
		List<Location> locations;										// Esta variable tendrá las localizaciones
		
		// Si nos han indicado algún cuadrado de localizaciones...
		
		if (maxLongitude != null && minLongitude != null && maxLatitude != null && minLatitude != null) {
			locations = locationService.getAllSchoolsLocationsByLocation(maxLatitude, minLatitude, maxLongitude, minLongitude);			// Buscamos las localizaciones
		}
		else {
			locations = locationService.getAllShoolsLocations();																		// Buscamos las localizaciones
		}
		
		List<LocationAndSchoolDTO> locationsData = new ArrayList<>();
		
		
		for (Location currentLocation: locations) {			
			School school = schoolService.getSchoolByLocation(currentLocation);
			
			if (school != null) {
				LocationAndSchoolDTO newLocation = new LocationAndSchoolDTO();
				
				newLocation.setLatitude(currentLocation.getLatitude());
				newLocation.setLongitude(currentLocation.getLongitude());
				
				newLocation.setId(school.getId());
				
				newLocation.setName(school.getName());
				locationsData.add(newLocation);
			}
		}
		
		
		return ResponseEntity.status(HttpStatus.OK).body(
				locationsData																				// Retornamos el resultado
		);

	}
	
	
	@GetMapping("school/{idSchool}/degree-years")
	public ResponseEntity getDegree(@PathVariable Integer idSchool) {
		School currentSchool = schoolService.get(idSchool);
		
		if (currentSchool == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Centro no encontrado")									// Si no existe lo indicamos
			);
		}
		
		YearsDTO years = new YearsDTO();
		
		for (ProfessionalDegree pf : schoolService.getAllProfessionalDegrees(idSchool)) {
			if (!years.getYears().contains(pf.getYear())) {
				years.getYears().add(pf.getYear());
			}
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
			years
		);			
				
	}
	
	
	
	/**
	 * Obtiene las localizaciones de las empresas
	 * @param maxLatitude Latitud máxima
	 * @param minLatitude Latitud mínima
	 * @param maxLongitude Longitud máxima
	 * @param minLongitude Longitud mínima
	 * @return
	 */
	@GetMapping("location/business")
	public ResponseEntity getLocationsOfBusiness(@RequestParam(required = false) String name, @QueryParam("maxLatitude") Double maxLatitude, @QueryParam("minLatitude") Double minLatitude, @QueryParam("maxLongitude") Double maxLongitude, @QueryParam("minLongitude") Double minLongitude) {
		
		List<Location> locations;												// Esta variable almacenará las distinas localizaciones
		List<LocationAndBusinessDTO> businessDTO = new ArrayList<>();
		
		// Si nos han pasado un cuadrado de localizaciones...
		
		if (name != null) {
			locations = locationService.getAllBusinessLocationsAndName(name);
		}
		else {
			locations = locationService.getAllBusinessLocations();																	// Buscamos las localizaciones
		}
		
		
		for (Location currentLocation: locations) {			
			Business business = businessService.getByLocation(currentLocation);
			

			if (business != null) {
				LocationAndBusinessDTO locationB = new LocationAndBusinessDTO();
				
				locationB.setLatitude(currentLocation.getLatitude());
				locationB.setLongitude(currentLocation.getLongitude());
				locationB.setCif(business.getCif());
				locationB.setImage(business.getImage());
				locationB.setName(business.getName());
				locationB.setNumberOfStudents(business.getNumberOfStudents());
				
				businessDTO.add(locationB);
			}
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				businessDTO																				// Retornamos las localizaciones
		);

	}
	
	/**
	 * Permite obtener una localización mediante su id
	 * @param id: Id de la ubicación
	 * @return
	 */
	@GetMapping("location/{id}")
	public ResponseEntity getLocationById(@PathVariable Integer id) {
		Location queryLocation = this.locationService.getLocationById(id);								// Obtenemos la localización
		
		
		// Comprobamos si existe una localización ...
		
		if (queryLocation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No se ha encontrado ninguna ubicación con el id: " + id)		// Si no existe lo indicamos
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					queryLocation													// Si existe lo retornamos
			);
		}
	}
	
	/**
	 * Permite editar una ubicación
	 * @param id: Id de la ubicacion
	 * @param location: Localización
	 */
	@PutMapping("location/{id}")
	public ResponseEntity editLocationById(@PathVariable Integer id, @RequestBody Location location) {
		
		Location queryLocation = this.locationService.getLocationById(id);						// Obtenemos la localización
		
		
		// Comprobamos si existe
		
		if (queryLocation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No se ha encontrado ninguna ubicación con el id: " + id)		// Si no existe lo indicamos
			);
		}
		
		if (location.getLatitude() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la nueva latitud")									// Comprobamos si nos han indicado la latitud
			);
		}
		else if (location.getLongitude() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la nueva longitud")									// Comprobamos si nos han indicado la longitud
			);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
			this.locationService.editLocation(id, location.getLongitude(), location.getLatitude())						// Editamos
		);
	}
	
	
	/**
	 * Borra la localización
	 * @param id: Identificador
	 * @param location: Localización
	 */
	@DeleteMapping("location/{id}")
	public ResponseEntity removeLocationById(@PathVariable Integer id) {
		
		Location queryLocation = this.locationService.getLocationById(id);				// Obtenemos la localización
		
		
		// Comprobamos si existe
		
		if (queryLocation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No se ha encontrado ninguna ubicación con el id: " + id)		// Si no existe lo indicamos
			);
		}
		this.locationService.removeLocation(id);																		// Borramos
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/**
	 * Crea una localización
	 * @param location Localización
	 */
	@PostMapping("location")
	public ResponseEntity editLocationById(@RequestBody Location location) {
		
		if (location.getLatitude() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la nueva latitud")					// Comprobamos si nos han indicado la latitud
			);	
		}
		else if (location.getLongitude() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la nueva longitud")					// Comprobamos si nos han indicado la longitud
			);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
			this.locationService.saveLocation(
					new Location(location.getLatitude(), location.getLongitude())						// Creamos y retornamos
			)
		);
	}
	
	
	
	
	/**
	 * Registra un nuevo movimiento en un centro
	 */
	@PostMapping("/motion")
	public ResponseEntity registryMotion() {
        String dni = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				// Obtenemos el dni del administrador

        Administrator admin = administratorService.get(dni.toUpperCase());											// Obtenemos el administrador
        UnusualMovement unusualMovement = null;																		// Esta variable tendra el nuevo movimiento
        
        // Comprobamos si es nulo
        
        if (admin == null) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        			new RestError(HttpStatus.NOT_FOUND, "El administrador no existe")								// Indicamos que es nulo
    		);
        }
        
        // Comprobamos si tiene escuela asignada
        
        if (admin.getSchoolSetted() != null) {
        	
        
        	// Comprobamos is es nulo
        	
        	if (admin.getSchoolSetted().getOpeningTime() == null || admin.getSchoolSetted().getClosingTime() == null) {
        		return ResponseEntity.status(HttpStatus.CONFLICT).body(
            			new RestError(HttpStatus.CONFLICT, "Asigne hora de apertura y cierre")										
        		);
        	}
        	
        	
        	// Obtenemos la fecha actual
        	
        	LocalTime newTime = LocalTime.now();
        	
        	
        	/*  Convertimos las fechas a tiempo */
        	
        	LocalTime openTime = LocalDateTime.ofInstant(admin.getSchoolSetted().getOpeningTime().toInstant(), ZoneId.systemDefault()).toLocalTime();
        	LocalTime closeTime = LocalDateTime.ofInstant(admin.getSchoolSetted().getClosingTime().toInstant(), ZoneId.systemDefault()).toLocalTime();
        	
        	// Comprobamos si está en el horario del colegio

        	if (newTime.isAfter(openTime) && newTime.isBefore(closeTime)) {
            	return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    	new RestError(HttpStatus.CONFLICT, "Escuela abierta aún")										
                );
        	}
        	
        	
        	// Si no está creamos un movimiento
        	
        	unusualMovement = new UnusualMovement();																// Creamos el movimiento
        	unusualMovement.setDate(LocalDateTime.now());															// Le ponemos la fecha
        	
        	unusualMovementService.saveUnusualMovement(unusualMovement);											// Guardamos el movimiento
        	School school = admin.getSchoolSetted();																// Obtenemos la escuela
        	
        	school.getUnusualsMovements().add(unusualMovement);														// Añadimos el moimiento
        	
        	schoolService.save(school);																				// Guardamos la escuela
        }
        else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        			new RestError(HttpStatus.NOT_FOUND, "Escuela no asignada")										// Si no, indicamos que es nulo
    		);
        }
        
		return ResponseEntity.status(HttpStatus.CREATED).body(
				unusualMovement																						// Retornamos el resultado
		);
	}
	
	/**
	 * Añade un registro de temperatura
	 * @param regTemp: Datos del ambiente
	 */
	@PostMapping("/temp-humidity")
	public ResponseEntity addTemp(@RequestBody RegTemp regTemp) {
	      String dni = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();					// Obtenemos el dni del administrador

	        Administrator admin = administratorService.get(dni.toUpperCase());											// Obtenemos el administrador
	        
	        // Comprobamos si existe
	        
	        if (admin == null) {
	    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
	        			new RestError(HttpStatus.NOT_FOUND, "El administrador no existe")								// Si no existe lo indicamos
	    		);
	        }
	        
	        regTemp.setDate(LocalDateTime.now());																		// Ponemos la fecha y hora
	        
	        
	        // Comprobamos si tiene escuela asignada
	        
	        if (admin.getSchoolSetted() != null) {
	        	regTemp.setId(null);																					// Ponemos el id en nulo
	        	
	        	schoolService.addRegTemp(admin.getSchoolSetted(), regTemp);
//	        	School school = admin.getSchoolSetted();																// Obtenemos la escuela
//	        	
//	        	RegTemp newReg = regTempService.save(regTemp, admin.getSchoolSetted().getId());							// Guardamos el registro de temperatura si no existe o si existe ya uno con la misma hora y fecha hace la media	        	
	        }
	        else {
	    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
	        			new RestError(HttpStatus.NOT_FOUND, "Escuela no asignada")										// Si no, indicamos que es nula la escuela
	    		);
	        }
	        
			return ResponseEntity.status(HttpStatus.CREATED).body(
					regTemp																								// Retornamos el resultado
			);
	}
	
	
	/**
	 * Deshabilita un usuario
	 * @param person: Datos del usuario a deshabilitar
	 */
	@PostMapping("/disable-user")
	public ResponseEntity disableUser(@RequestBody PersonDTO person) {
		
		
		// Comprobamos el dni
		
		if (person.getDni() == null || person.getDni().trim().length() < 8 || person.getDni().trim().length() > 9) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        			new RestError(HttpStatus.BAD_REQUEST, "Especifique un dni válido")
    		);
		}
		
		// Comprobamos si existe
		
		else if (!personService.existPerson(person.getDni())) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        			new RestError(HttpStatus.NOT_FOUND, "Persona no encontrada")
    		);
		}
		else {
			try {
				personService.disable(person.getDni());																					// Lo deshabilitamos
			}
			catch (Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestError(HttpStatus.NOT_FOUND, e.getMessage())); 			// En caso de error lo indicamos
			}
			
			return ResponseEntity.status(HttpStatus.OK).build();																	
		}		
	}
	
	/**
	 * Habilita un usuario
	 * @param person: Datos de la persona a habilitar
	 */
	@PostMapping("/enable-user")
	public ResponseEntity enableUser(@RequestBody PersonDTO person) {
		
		// Comprobamos el dni
		
		if (person.getDni() == null || person.getDni().trim().length() < 8 || person.getDni().trim().length() > 9) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        			new RestError(HttpStatus.BAD_REQUEST, "Especifique un dni válido")
    		);
		}
		
		// Comprobamos si existe
		
		else if (!personService.existPerson(person.getDni())) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        			new RestError(HttpStatus.NOT_FOUND, "Persona no encontrada")
    		);
		}
		else {
			try {
				personService.enable(person.getDni());																			// Intentamos habilitarlo
			}
			catch (Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestError(HttpStatus.NOT_FOUND, e.getMessage()));	// En caso de error lo indicamos	
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}	
	}
	
	/**
	 * Permite resetear una contraseña olvidada enviando un token válido por un tiempo determinado al correo del usuario
	 * @param resetPassword : DTO que contiene el dni del usuario
	 */
	@PostMapping("/send-reset-password")
	public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPassword) {
		
		// Comprobamos el dni
		
		if (resetPassword.getDni() == null || resetPassword.getDni().trim().length() < 8 || resetPassword.getDni().trim().length() > 9) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	    			new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")
			);
		}
		
		// Comprobamos si existe
		
		if (!personService.existPerson(resetPassword.getDni())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Usuario no registrado")
			);
		}
		
		// Comprobamos si el usuario está habilitado
		
		if (!personService.getPerson(resetPassword.getDni()).isEnabled() ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Usuario desactivado")
			);
		}
						
        String newToken = jwtUtil.generateToken(resetPassword.getDni(), TypeTokenToGenerate.TOKEN_RESET_PASSWORD);					// Obtenemos su token

        Person person = personService.getPerson(resetPassword.getDni());															// Obtenemos la persona
		
        try {
        	
        	// Enviamos el mensaje de recuperación
        	
			smtpMailSender.send(person.getEmail(), "Reestablecer contraseña", "Hola " + person.getName() + ", pulsa sobre el siguiente enlace para configurar de nuevo tu contraseña (Enlace válido sólo durante 24 horas). \n " + frontUrl + "auth/reset-my-password/" + newToken);
		} 
        catch (MessagingException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
	    			new RestError(HttpStatus.CONFLICT, "Error al enviar el correo")													// En caso de error lo indicamos
			);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(new EmailDTO(person.getEmail()));										// Retornamos el resultado
	}
	
	/**
	 * Permite configurar una nueva contraseña
	 * @param personData: Datos de la persona
	 */
	@PostMapping("/configure-new-password")
	public ResponseEntity setPassword(@RequestBody PersonDTO personData) {
		
		// Comprobamos la contraseña
		
		if (personData.getPassword() == null || personData.getPassword().length() < 8) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	    			new RestError(HttpStatus.BAD_REQUEST, "Indique una nueva contraseña válida")
			);
		}
		
		String dni = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();							// Obtenemos la persona

		
		// Comprobamos si existe
		
		if (!personService.existPerson(dni)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
	    			new RestError(HttpStatus.NOT_FOUND, "El usuario no existe")
			);
		}
		
		
		try {
			
			// Establecemos la nueva contraseña
			
			personService.setNewPassword(dni, passwordEncoder.encode(personData.getPassword()));
			
			// Generamos un token
			
            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(dni, personData.getPassword());		// Creamos un nuevo usuario de autenticación

            // Lo autenticamos
            
            authManager.authenticate(authInputToken);			

            // Retornamos el resultado
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("jwt_token", jwtUtil.generateToken(dni, TypeTokenToGenerate.TOKEN_USER)));			// Retornamos el resultado
			
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
	    			new RestError(HttpStatus.NOT_FOUND, "El usuario no existe")										// En caso de error lo indicamos
			);
		}

	}


	
}
