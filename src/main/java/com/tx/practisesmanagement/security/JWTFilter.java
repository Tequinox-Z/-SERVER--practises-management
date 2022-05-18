package com.tx.practisesmanagement.security;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tx.practisesmanagement.exception.UserErrorException;
import com.tx.practisesmanagement.model.Person;
import com.tx.practisesmanagement.service.PersonService;


/**
 * JWT Filtro
 * @author Salva
 */
@Component
public class JWTFilter extends OncePerRequestFilter {
	
	// Servicio
	
    	@Autowired private MyUserDetailsService userDetailsService; 
    // Utiles
    	
    	@Autowired private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String dni = "";
        if (authHeader != null && !authHeader.isEmpty() && authHeader.startsWith("Bearer ")){
            String jwt = authHeader.substring(7);
            if(jwt == null || jwt.isEmpty()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token inválido en header");
                return;
            }
            else {
                try {
                    dni = jwtUtil.validateTokenAndRetrieveSubject(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(dni);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(dni, userDetails.getPassword(), userDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null){
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                catch (JWTVerificationException | UserErrorException exception) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                catch (Exception exception) {
                	response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}