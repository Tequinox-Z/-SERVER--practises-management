package com.tx.practisesmanagement.security;



import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración de seguridad JWT
 * @author Salvador
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private JWTFilter filter;				// Filtro
    @Autowired private MyUserDetailsService uds;		// Servicio de detalles de usuario

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                
   // Público
                
                .antMatchers(
                		"/auth/login", "/send-reset-password", "/", "/auth/login-iot"
                ).permitAll()
                
   // Sólo lectura
                
                .antMatchers(
                		HttpMethod.GET,
                		"/administrator/**"
                )
                .hasAnyRole("ADMIN", "TEACHER", "STUDENT", "LABOR_TUTOR")
                .antMatchers(
                		HttpMethod.GET,
                		"/administrator"
                )
                .hasAnyRole("ADMIN", "TEACHER", "STUDENT", "LABOR_TUTOR")
                
   // Acceso sólo para el administrador  
                
                .antMatchers(
                		"/administrator", "/administrator/**", "/auth/register", "/temp-humidity", "/motions", "/disable-user", "/enable-user"
                ).hasAnyRole("ADMIN")
                
   // Acceso para cualquier usuario registrado
                
                .antMatchers(
                		HttpMethod.POST, "/temp-humidity", "/motion", "/auth/checktoken", "/student/**", "/enrollment/**", "/business/**", "/exist-person/**", "/person/**", "/configure-new-password", "/school/**"
                ).hasAnyRole("ADMIN", "TEACHER", "STUDENT", "LABOR_TUTOR")
                .antMatchers(
                		HttpMethod.PUT, "/temp-humidity", "/motion", "/auth/checktoken", "/student/**", "/enrollment/**", "/business/**", "/exist-person/**", "/person/**", "/configure-new-password", "/school/**"
                ).hasAnyRole("ADMIN", "TEACHER", "STUDENT", "LABOR_TUTOR")
                .antMatchers(
                		 HttpMethod.DELETE, "/temp-humidity", "/motion", "/auth/checktoken", "/student/**", "/enrollment/**", "/business/**", "/exist-person/**", "/person/**", "/configure-new-password", "/school/**"
                ).hasAnyRole("ADMIN", "TEACHER", "STUDENT", "LABOR_TUTOR")
                .antMatchers(
                		HttpMethod.GET, "/temp-humidity", "/motion", "/auth/checktoken", "/student/**", "/enrollment/**", "/business/**", "/exist-person/**", "/person/**", "/configure-new-password", "/school/**", "exist-business/**"
                ).hasAnyRole("ADMIN", "TEACHER", "STUDENT", "LABOR_TUTOR")
                
                
                
   // Acceso sólo para profesor y administrador
                
                .antMatchers(
                		"/teacher/**", "/school/**", "/temp-humidity", "/motion"
                ).hasAnyRole("ADMIN","TEACHER")
                
                .and()
                .userDetailsService(uds)
                .exceptionHandling()
                    .authenticationEntryPoint(
                            (request, response, authException) ->
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado")
                    )
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}