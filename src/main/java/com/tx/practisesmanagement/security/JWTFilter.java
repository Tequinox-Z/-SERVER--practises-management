package com.tx.practisesmanagement.security;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;


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
        if (authHeader != null && !authHeader.isEmpty() && authHeader.startsWith("Bearer ")){
            String jwt = authHeader.substring(7);
            if(jwt == null || jwt.isEmpty()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token inválido en header");
                return;
            }
            else {
                try {
                    String email = jwtUtil.validateTokenAndRetrieveSubject(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null){
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                catch (JWTVerificationException exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token no válido");
                    return;
                }
                catch (UsernameNotFoundException user) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El usuario no existe");
                    return;
                }
                catch (Exception exp) {
                    response.sendError(HttpServletResponse.SC_CONFLICT, "Error: " + exp.getMessage());
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}