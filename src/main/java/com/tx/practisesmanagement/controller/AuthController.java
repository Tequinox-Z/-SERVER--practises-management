package com.tx.practisesmanagement.controller;


import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tx.practisesmanagement.component.SmtpMailSender;
import com.tx.practisesmanagement.dto.PersonDTO;
import com.tx.practisesmanagement.dto.TokenDTO;
import com.tx.practisesmanagement.dto.TokenDecompressedDTO;
import com.tx.practisesmanagement.enumerators.Rol;
import com.tx.practisesmanagement.enumerators.TypeTokenToGenerate;
import com.tx.practisesmanagement.error.RestError;
import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Administrator;
import com.tx.practisesmanagement.model.LaborTutor;
import com.tx.practisesmanagement.model.Student;
import com.tx.practisesmanagement.model.Teacher;
import com.tx.practisesmanagement.security.JWTUtil;
import com.tx.practisesmanagement.service.AdministratorService;
import com.tx.practisesmanagement.service.LaborTutorService;
import com.tx.practisesmanagement.service.PersonService;
import com.tx.practisesmanagement.service.StudentService;
import com.tx.practisesmanagement.service.TeacherService;


/**
 * Controlador de autenticación
 * @author Salvador
 */
@CrossOrigin(origins = {"https://tequinox-z.github.io/", "http://localhost:4200", "http://localhost:43205"})
@RestController
public class AuthController {
	
		/* ============================= Autenticación y JWT ============================= */ 
	
    		@Autowired private AuthenticationManager authManager;
    		@Autowired private JWTUtil jwtUtil;
    		@Autowired private PasswordEncoder passwordEncoder;

		/* ================================== Servicios ================================== */ 
	
    		@Autowired private AdministratorService administratorService;
    		@Autowired private StudentService studentService;
    		@Autowired private TeacherService teacherService;
    		@Autowired private PersonService personService;	    
    		@Autowired private SmtpMailSender smtpMailSender;
    		@Autowired private LaborTutorService laborTutorService;
    		
    		@Value("${jwt_secret}")
    	    private String secret;										// Contraseña para cifrar	
    		
    		
    		@Value("${token-expirated-refresh-time}")
    		private long tokenExpiratedRefreshTime;						// Tiempo en el que se permite renovar el token una vez haya cumplido
    	
    	/**
    	 * Permite registrar un usuario, ya sea un administrador, estudiante, profesor o tutor laboral
   		 * @param user: Usuario a registrar
    	 * @return Usuario registrado
   		 */
	    @PostMapping("auth/register")
	    public ResponseEntity registerAdmin(@RequestBody PersonDTO user) {

	    	// Comprobamos el dni
	    	
	    	if (user.getDni() == null || !user.getDni().matches("[0-9]{7,8}[A-Z a-z]")) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	    				new RestError(HttpStatus.BAD_REQUEST, "Dni no válido")								// Comprobamos si el dni es válido
	    		);			
	    	}	
	    	
	    	// Comprobamos la fecha de nacimiento 
	    	
	    	else if (user.getBirthDate() == null || user.getBirthDate().after(new Date())) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Indique una fecha de nacimiento válida"));			// Si no nos han pasado nombre lo indicamos
	    	}
	    	
	    	// Comprobamos el nombre de usuario
	    	
	    	else if (user.getName() == null || user.getName().trim().length() == 0) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un nombre para este usuario"));	// Si no nos han pasado nombre lo indicamos
	    	}	    	
	    	
	    	// Comprobamos los apellidos
	    	
	    	else if (user.getLastName() == null || user.getLastName().trim().length() == 0) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó los apellidos para este usuario"));	// Si no nos han pasado nombre lo indicamos
	    	}
	    	
	    	// Comprobamos la contraseña
	    	
	    	else if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó una contraseña"));				// Si no nos han pasado una contraseña lo indicamos
	    	}
	    	
	    	// Comprobamos el correo
	    	
	    	else if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un correo válido"));						// Si no nos han pasado un correo lo indicamos
	    	}
	    	
	    	// Comprobamos el teléfono
	    	
	    	else if (user.getTelefone() == null || user.getTelefone().trim().length() == 0) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un teléfono"));						// Si no nos han pasado un correo lo indicamos
	    	}
	    	
	    	// Comprobamos la dirección
	    	
	    	else if (user.getAddress() == null || user.getAddress().trim().length() == 0) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó una dirección"));						// Si no nos han pasado un correo lo indicamos
	    	}
	    	
	    	// Comprobamos el rol
	    	
	    	else if (user.getRol() == null) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "No se expecificó un rol para este usuario"));		// Si no nos han indicado un rol lo indicamos
	    	}
	    	
	    	if (user.getRol() == Rol.ROLE_LABOR_TUTOR || user.getRol() == Rol.ROLE_STUDENT) {
	    		user.setEnabled(false);	
	    	}
	    	else {
	    		user.setEnabled(true);	
	    	}
	   	    
	    											// Lo habilitamos
	    	
	    	user.setDni(user.getDni().trim().toUpperCase());				// Convertimos el dni en mayúsculas																											// Convertimos el DNI en mayúscula
	    	
	    	String passwordNotEncoded = user.getPassword();
	    	
	    	// Comprobamos si existe ya
	    	
	    	if (personService.existPerson(user.getDni())) {
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Usuario ya registrado"));							// Si el usuario está registrafo lo indicamos
	    	}
	    	
	        String encodedPass = passwordEncoder.encode(user.getPassword());					// Codificamos la contraseña
	        user.setPassword(encodedPass);														// Establecemos la contraseña
	        
	        
	        // Configuramos el rol
	        
	        switch(user.getRol()) {
	        	case ROLE_ADMIN: {
	        		administratorService.save(new Administrator(user));
	    	        break;
	        	}
	        	case ROLE_STUDENT: {
	    	        studentService.save(new Student(user));
	        		break;
	        	}
	        	case ROLE_TEACHER: {
	        		teacherService.save(new Teacher(user));
	        		break;
	        	}
	        	case ROLE_LABOR_TUTOR: {
	        		laborTutorService.save(new LaborTutor(user));
	        		break;
	        	}
	        	default: {
	        		
	        		// En caso de no existir lo indicamos
	        		
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestError(HttpStatus.BAD_REQUEST, "Rol desconocido o incorrecto"));
	        	}
	        }
	        
	        try {
	        	// Enviamos un correo de bienvenida
	        	
				smtpMailSender.send(user.getEmail(), "Bienvenido a Practises Management", "La cuenta de correo " + user.getEmail() + " ha sido registrada satisfactoriamente en Practises Management. Puede iniciar sesión con su dni " + user.getDni() + " y contraseña " + encodedPass);
			} 
	        catch (Exception e) {
				
			}

	        // Enviamos un correo de bienvenida 

	        String token = jwtUtil.generateToken(user.getDni(), TypeTokenToGenerate.TOKEN_USER);															// Generamos el token
    		return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("jwt_token", token));											// Retornamos el token 
	    }
	    
	    
	    /**
	     * Permite iniciar sesión
	     * @param person: Datos de la persona a iniciar sesión
	     * @return Token Token resultante
	     */
	    @PostMapping("auth/login")
	    public ResponseEntity loginHandler(@RequestBody PersonDTO person){
	        try {	
	        	
	        	// Comprobamos el dni
	        	
	        	if (person.getDni() == null || !person.getDni().matches("[0-9]{7,8}[A-Z a-z]")) {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
		    				new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")			// Si no nos han indicado un dni lo indicamos
		    		);
	        	}
	        	
	        	// Comprobamos la contraseña
	        	
	        	else if (person.getPassword() == null) {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
		    				new RestError(HttpStatus.BAD_REQUEST, "Indique contraseña")		// Si no nos han pasado una contraseña lo indicamos 	
		    		);
	        	}
	        	
	        	
	        	person.setDni(person.getDni().toUpperCase());								// Convertimos en mayúsculas
	        	
	            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(person.getDni(), person.getPassword());		// Creamos un nuevo usuario de autenticación

	            authManager.authenticate(authInputToken);								// Autenticamos el usuario
	            String token = jwtUtil.generateToken(person.getDni(), TypeTokenToGenerate.TOKEN_USER);					// Obtenemos su token

	            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("jwt_token", token));			// Retornamos el resultado
	        }
	        catch (Exception e) {
	        	
	        	// En caso de error lo indicamos
	        	
	        	HashMap<HttpStatus, String> errorGenerated = personService.getErrorLogin(person.getDni(), person.getPassword());
	        	
	        	HttpStatus codeHttp = errorGenerated.entrySet().iterator().next().getKey();
	        	
	    		return ResponseEntity.status(codeHttp).body(new RestError(codeHttp, errorGenerated.get(codeHttp)));			
	        }
	    }

	    /**
	     * Permite iniciar sesión al dispositivo IoT
	     * @param person: Datos del usuario
	     */
	    @PostMapping("/auth/login-iot")
	    public ResponseEntity loginIoT(@RequestBody PersonDTO person){
	        try {	
	        	
	        	// Comprobamos el dni
	        	
	        	if (person.getDni() == null || !person.getDni().matches("[0-9]{7,8}[A-Z a-z]")) {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
		    				new RestError(HttpStatus.BAD_REQUEST, "Indique un dni válido")			// Si no nos han indicado un dni lo indicamos
		    		);
	        	}
	        	
	        	// Comprobamos la contraseña
	        	
	        	else if (person.getPassword() == null) {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
		    				new RestError(HttpStatus.BAD_REQUEST, "Indique contraseña")		// Si no nos han pasado una contraseña lo indicamos 	
		    		);
	        	}
	        	
	        	person.setDni(person.getDni().toUpperCase());								// Convertimos en mayúsculas
	        
	        	
	        	// Comprobamos si el usuario es un administrador
	        	
	        	if (personService.getPerson(person.getDni()).getRol() != Rol.ROLE_ADMIN) {
		    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
		    				new RestError(HttpStatus.BAD_REQUEST, "El dispositivo IoT solo puede ser utilizado por administradores")		 	
		    		);
	        	}
	        	
	            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(person.getDni(), person.getPassword());		// Creamos un nuevo usuario de autenticación

	            authManager.authenticate(authInputToken);								// Autenticamos el usuari
	            String token = jwtUtil.generateToken(person.getDni(), TypeTokenToGenerate.TOKEN_IOT);					// Obtenemos su token

	            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("jwt_token", token));			// Retornamos el resultado
	        }
	        catch (Exception e) {
	        	
	        	// En caso de error, lo indicamos
	        	
	        	
	        	HashMap<HttpStatus, String> errorGenerated = personService.getErrorLogin(person.getDni(), person.getPassword());
	        	
	        	HttpStatus codeHttp = errorGenerated.entrySet().iterator().next().getKey();
	        	
	    		return ResponseEntity.status(codeHttp).body(new RestError(codeHttp, errorGenerated.get(codeHttp)));			
	        }
	    }
	    
	    
	    /**
	     * Permite validar si el token es válido
	     */
	    @GetMapping("auth/checktoken")
	    public ResponseEntity isTokenValid() {
    		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 				// Si estamos autorizados devolveremos un NO_CONTENT si no spring devolverá un 401
	    }
	    
	    /**
	     * Permite refrescar el token mediante un token anterior válido
	     * @param token: Token anterior
	     */
	    @PostMapping("auth/refresh-token")
	    public ResponseEntity refreshToken(@RequestBody TokenDTO token) {
	    	
	    	try {
	    		
	    		// Verificamos el token
	    		
		        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
		                .withSubject("User Details")
		                .withIssuer("Practises/Management")
		                .acceptExpiresAt(tokenExpiratedRefreshTime)
		                .build();
		        verifier.verify(token.getToken());
	    	}
	    	catch (JWTVerificationException e) {
	    		
	    		// En caso de no ser válido lo indicamos
	    		
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	    				new RestError(HttpStatus.BAD_REQUEST, "Tiempo de renovación agotado")			
	    		);
	    	}
	    	catch (Exception e) {
	    		
	    		// En caso de que no sea válido lo indicamos
	    		
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	    				new RestError(HttpStatus.BAD_REQUEST, "Token no válido")			
	    		);
	    	}
	    	
	    	// Obtenemos el paiload del token
	    	
	    	String paiload = token.getToken().split("\\.")[1];
	   
	    	// Lo decodificamos
	    	
	    	Base64.Decoder decoder = Base64.getUrlDecoder();
	    	
	    	// Obtenemos el payload desencriptado
	    	
	    	String pailoadDesencrypted = new String(decoder.decode(paiload));
	    	
	    	TokenDecompressedDTO dataToken;					// Esta variable mantendrá el token desencriptado
	    	
	    	try {
	    		// Leemos los datos del token
	    		
	    		dataToken = new ObjectMapper().readValue(pailoadDesencrypted, TokenDecompressedDTO.class);
				
	    		// Verificamos el dni del token
	    		
				if (dataToken.getUsername() == null || dataToken.getUsername().trim().length() < 8 || dataToken.getUsername().trim().length() > 9) {
					throw new UserErrorException("Dni incorrecto");
				}
			}
	    	catch (Exception e) {
	    		
	    		// En caso de error lo indicamos
	    		
	    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	    				new RestError(HttpStatus.BAD_REQUEST, e.getMessage())			
	    		);
			} 

	    	// Generamos un nuevo token
	    	
            String newToken = jwtUtil.generateToken(dataToken.getUsername(), TypeTokenToGenerate.TOKEN_USER);					// Obtenemos su token
	    	
    		return ResponseEntity.status(HttpStatus.CREATED).body(
    				new RestError(HttpStatus.CREATED, newToken)																	// Lo retornamos			
    		);
	    	
	    }
	    
}
