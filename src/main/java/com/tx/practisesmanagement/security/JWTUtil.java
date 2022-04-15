package com.tx.practisesmanagement.security;


import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * JWT Util
 * @author Salva
 */
@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;										// Secreto

    @Value("${jwt_expiration}")
    private Integer minutes;									// Minutos de expiración
    
    /**
     * Genera el token
     * @param username: Usuario
     * @return
     * @throws IllegalArgumentException
     * @throws JWTCreationException
     */
    public String generateToken(String username) throws IllegalArgumentException, JWTCreationException {
    	
    	Date date = new Date();								// Obtenemos la fecha
    	
    	date.setMinutes(date.getMinutes() + minutes);		// Establecemos la fecha de expiración
    	    	
    	// Asignamos datos
    	
        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer("Practises/Management")
                .withExpiresAt(date)
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