package com.tx.practisesmanagement.controller;



import java.util.Collections;
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
import org.springframework.web.bind.annotation.RestController;

import com.tx.practisesmanagement.component.SmtpMailSender;
import com.tx.practisesmanagement.dto.EmailDTO;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.dto.ResetPasswordDTO;
import com.tx.practisesmanagement.error.RestError;
import com.tx.practisesmanagement.model.Location;
import com.tx.practisesmanagement.model.Person;
import com.tx.practisesmanagement.model.RegTemp;
import com.tx.practisesmanagement.model.School;
import com.tx.practisesmanagement.model.UnusualMovement;
import com.tx.practisesmanagement.security.JWTUtil;
import com.tx.practisesmanagement.service.AditionalsFunctionsService;
import com.tx.practisesmanagement.service.AdministratorService;
import com.tx.practisesmanagement.service.LocationService;
import com.tx.practisesmanagement.service.PersonService;
import com.tx.practisesmanagement.service.RegTempService;
import com.tx.practisesmanagement.service.SchoolService;
import com.tx.practisesmanagement.service.StudentService;
import com.tx.practisesmanagement.service.TeacherService;
import com.tx.practisesmanagement.service.UnusualMovementService;

/**
 * Controlador de funciones adicionales. Reune todas aquellas funciones que no son recursos
 * @author Salva
 */
@RestController
@CrossOrigin(origins = "*")
public class AditionalsFunctionsController {
	
	// Servicios
	
		@Autowired private AdministratorService administratorService;		// Servicio de administrador
		@Autowired private StudentService studentService;					// Servicio de estudiante
		@Autowired private AditionalsFunctionsService aditinalsFuncions;	// Servicio de funciones adicionales
		@Autowired private TeacherService teacherService;					// Servicio de profesor
		@Autowired private SchoolService schoolService;						// Servicio de colegio
		@Autowired private PersonService personService;
		@Autowired private LocationService locationService;
		
		@Autowired private AuthenticationManager authManager;
		@Autowired private SmtpMailSender smtpMailSender;
		@Autowired private JWTUtil jwtUtil;
		@Autowired private PasswordEncoder passwordEncoder;
		@Autowired private RegTempService regTempService;
		
		@Autowired private UnusualMovementService unusualMovementService;
		@Value("${urlServer}")
		private String frontUrl;

	
	@GetMapping("school/{idSchool}/briefing")
	public ResponseEntity getBriefingFromSchool(@PathVariable Integer idSchool) {
		
		School school = schoolService.get(idSchool);
		
		if (school == null) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        			new RestError(HttpStatus.NOT_FOUND, "El centro no existe")			// Si no existe el centro lo indicamos
    		);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
    			administratorService.getCountFromSchool(idSchool)
		);
	}
	
	@GetMapping("example")
	public ResponseEntity get() {
		return ResponseEntity.status(HttpStatus.OK).body(this.administratorService.getCountFromSchool(1) + " - " + teacherService.getCountTeacherFromSchool(1));
	}
	
	
	@GetMapping("location/schools")
	public ResponseEntity<List<Location>> getLocationsOfSchools(@QueryParam("maxLatitude") Double maxLatitude, @QueryParam("minLatitude") Double minLatitude, @QueryParam("maxLongitude") Double maxLongitude, @QueryParam("minLongitude") Double minLongitude) {
		
		List<Location> locations;
		
		if (maxLongitude != null && minLongitude != null && maxLatitude != null && minLatitude != null) {
			locations = locationService.getAllSchoolsLocationsByLocation(maxLatitude, minLatitude, maxLongitude, minLongitude);
		}
		else {
			locations = locationService.getAllShoolsLocations();
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
			locations
		);

	}
	
	@GetMapping("location/business")
	public ResponseEntity<List<Location>> getLocationsOfBusiness(@QueryParam("maxLatitude") Double maxLatitude, @QueryParam("minLatitude") Double minLatitude, @QueryParam("maxLongitude") Double maxLongitude, @QueryParam("minLongitude") Double minLongitude) {
		
		List<Location> locations;
		
		if (maxLongitude != null && minLongitude != null && maxLatitude != null && minLatitude != null) {
			locations = locationService.getAllBusinessLocationsByLocation(maxLatitude, minLatitude, maxLongitude, minLongitude);
		}
		else {
			locations = locationService.getAllBusinessLocations();
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
			locations
		);

	}
	
	@GetMapping("location/{id}")
	public ResponseEntity getLocationById(@PathVariable Integer id) {
		Location queryLocation = this.locationService.getLocationById(id);
		
		if (queryLocation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No se ha encontrado ninguna ubicación con el id: " + id)
			);
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					queryLocation
			);
		}
	}
	
	@PutMapping("location/{id}")
	public ResponseEntity editLocationById(@PathVariable Integer id, @RequestBody Location location) {
		
		Location queryLocation = this.locationService.getLocationById(id);
		
		if (queryLocation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No se ha encontrado ninguna ubicación con el id: " + id)
			);
		}
		
		if (location.getLatitude() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la nueva latitud")
			);
		}
		else if (location.getLongitude() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la nueva longitud")
			);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
			this.locationService.editLocation(id, location.getLongitude(), location.getLatitude())
		);
	}
	
	
	@DeleteMapping("location/{id}")
	public ResponseEntity removeLocationById(@PathVariable Integer id, @RequestBody Location location) {
		
		Location queryLocation = this.locationService.getLocationById(id);
		
		if (queryLocation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "No se ha encontrado ninguna ubicación con el id: " + id)
			);
		}
		this.locationService.removeLocation(id);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@PostMapping("location")
	public ResponseEntity editLocationById(@RequestBody Location location) {
		
		if (location.getLatitude() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la nueva latitud")
			);
		}
		else if (location.getLongitude() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new RestError(HttpStatus.BAD_REQUEST, "Indique la nueva longitud")
			);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(
			this.locationService.saveLocation(
					new Location(location.getLatitude(), location.getLongitude())
			)
		);
	}
	
	
	
	
	
	@PostMapping("/motions")
	public ResponseEntity registryMotion() {
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/temp-humidity")
	public ResponseEntity addTemp(@RequestBody RegTemp regTemp) {
		System.out.println(regTemp);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@PostMapping("/disable-user")
	public ResponseEntity disableUser(@RequestBody PersonDTO person) {
		if (person.getDni() == null || person.getDni().trim().length() < 8 || person.getDni().trim().length() > 9) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        			new RestError(HttpStatus.BAD_REQUEST, "Especifique un dni válido")
    		);
		}
		else if (!personService.existPerson(person.getDni())) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        			new RestError(HttpStatus.NOT_FOUND, "Persona no encontrada")
    		);
		}
		else {
			try {
				personService.disable(person.getDni());
			}
			catch (Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestError(HttpStatus.NOT_FOUND, e.getMessage()));
			}
			
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}		
	}
	
	@PostMapping("/enable-user")
	public ResponseEntity enableUser(@RequestBody PersonDTO person) {
		if (person.getDni() == null || person.getDni().trim().length() < 8 || person.getDni().trim().length() > 9) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        			new RestError(HttpStatus.BAD_REQUEST, "Especifique un dni válido")
    		);
		}
		else if (!personService.existPerson(person.getDni())) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        			new RestError(HttpStatus.NOT_FOUND, "Persona no encontrada")
    		);
		}
		else {
			try {
				personService.enable(person.getDni());
			}
			catch (Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestError(HttpStatus.NOT_FOUND, e.getMessage()));
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}	
	}
	
	/**
	 * Permite resetear una contraseña olvidada enviando un token válido por un tiempo determinado al correo del usuario
	 * @param resetPassword : DTO que contiene el dni del usuario
	 * @return NO_CONTENT
	 */
	@PostMapping("/send-reset-password")
	public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPassword) {
		if (resetPassword.getDni() == null || resetPassword.getDni().trim().length() < 8 || resetPassword.getDni().trim().length() > 9) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	    			new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")
			);
		}
		
		if (!personService.existPerson(resetPassword.getDni())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new RestError(HttpStatus.NOT_FOUND, "Usuario no registrado")
			);
		}
						
        String newToken = jwtUtil.generateTokenForNewPassword(resetPassword.getDni());					// Obtenemos su token

        Person person = personService.getPerson(resetPassword.getDni());
		
        try {
			smtpMailSender.send(person.getEmail(), "Reestablecer contraseña", "Hola " + person.getName() + ", pulsa sobre el siguiente enlace para configurar de nuevo tu contraseña (Enlace válido sólo durante 24 horas). \n " + frontUrl + "auth/reset-my-password/" + newToken);
		} 
        catch (MessagingException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(
	    			new RestError(HttpStatus.CONFLICT, "Error al enviar el correo")
			);
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(new EmailDTO(person.getEmail()));
	}
	
	
	@PostMapping("/configure-new-password")
	public ResponseEntity setPassword(@RequestBody PersonDTO personData) {
		if (personData.getPassword() == null || personData.getPassword().length() < 8) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	    			new RestError(HttpStatus.BAD_REQUEST, "Indique una nueva contraseña válida")
			);
		}
		
		String dni = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!personService.existPerson(dni)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
	    			new RestError(HttpStatus.NOT_FOUND, "El usuario no existe")
			);
		}
		
		
		try {
			personService.setNewPassword(dni, passwordEncoder.encode(personData.getPassword()));
			
            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(dni, personData.getPassword());		// Creamos un nuevo usuario de autenticación

            authManager.authenticate(authInputToken);			

            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("jwt_token", jwtUtil.generateToken(dni)));			// Retornamos el resultado
			
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
	    			new RestError(HttpStatus.NOT_FOUND, "El usuario no existe")
			);
		}

	}

	
	@DeleteMapping("testingdel/{id}")
	public ResponseEntity asdasd(@PathVariable Integer id) {
		unusualMovementService.removeUnusualMovement(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping("testingsave")
	public ResponseEntity asdasd(@RequestBody RegTemp regTemp) {
		
		System.out.println(this.regTempService.save(regTemp, 12));
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
