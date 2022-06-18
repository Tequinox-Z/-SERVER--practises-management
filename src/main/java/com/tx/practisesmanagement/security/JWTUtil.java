package com.tx.practisesmanagement.security;


import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tx.practisesmanagement.enumerators.TypeTokenToGenerate;

/**
 * JWT Util, otorga las funciones de generación de token y validación del mismo
 * @author Salvador
 */
@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;										// Secreto

    @Value("${jwt_expiration}")
    private Integer minutesUser;									// Minutos de expiración
    
    @Value("${minutes_token_email}")
    private Integer minutesTokenEmail;
    
    @Value("${minutes_token_iot}")
    private Integer minutesTokenIoT;
    
    /**
     * Genera el token
     * @param username: Dni del usuario
     * @return Token generado
     * @throws IllegalArgumentException Argumentos inválidos
     * @throws JWTCreationException Error al crear token
     */
    public String generateToken(String username, TypeTokenToGenerate type) throws IllegalArgumentException, JWTCreationException {
      	
    	// Comprobamos hacia quien va dirigido el token, asignando un tiempo de vida concreto
    	
    	Integer minutesToAdd;
    	
    	switch (type) {
		case TOKEN_IOT:
			minutesToAdd = minutesTokenIoT;							// En caso de un inicio del dispositivo IoT
			break;
		case TOKEN_RESET_PASSWORD:
			minutesToAdd = minutesTokenEmail;						// En caso de un token para resetear la contraseña del usuario
			break;
		case TOKEN_USER:
			minutesToAdd = minutesUser;								// En caso de un inicio de sesión normal
			break;
		default:
			minutesToAdd = 0;										// En caso desconocido
			break;
		}
    	
    	// Establecemos la fecha de expiración
    	
    	Calendar date = Calendar.getInstance();
    	Calendar expirationDate = Calendar.getInstance();
    	expirationDate.add(Calendar.MINUTE, minutesToAdd);				// Añadimos los minutos de diferencia
    	
    	// Asignamos datos
    	
        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", username)
                .withIssuedAt(date.getTime())
                .withIssuer("Practises/Management")
                .withExpiresAt(expirationDate.getTime())
                .sign(Algorithm.HMAC256(secret));
    }
    
    /**
     * Validar datos
     * @param token: Token
     * @return
     * @throws JWTVerificationException
     */
    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("Practises/Management")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        
        return jwt.getClaim("username").asString();
    }

}