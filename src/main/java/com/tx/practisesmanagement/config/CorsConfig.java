package com.tx.practisesmanagement.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración del CORS
 * @author Salva
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
	
	
    @Value("${jwt_secret}")
	private String urlServer;
	
	/**
	 * Nos permite configurar el acceso a recursos mediante CORS
	 * @return WebMvcConfigurer 
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				
				// Registro CORS de login
				
				registry.addMapping("auth/login")
				.allowedOrigins(urlServer)
				.allowedMethods("POST")
				.allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				// Registro CORS del registro
				
				registry.addMapping("auth/register")
				.allowedOrigins(urlServer)
				.allowedMethods("POST")
				.allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				// Registro CORS del checkeo de token
				
				registry.addMapping("auth/checktoken")
				.allowedOrigins(urlServer)
				.allowedMethods("GET")
				.allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				// Registro CORS del reseteo de contraseña
				
				registry.addMapping("reset-password")
				.allowedOrigins(urlServer)
				.allowedMethods("POST")
				.allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				// Registro CORS del acceso a la persona
				
				registry.addMapping("person")
				.allowedOrigins(urlServer)
				.allowedMethods("GET")
				.allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				// Registro CORS de administrador
				
				registry.addMapping("administrator")
				.allowedOrigins(urlServer)
				.allowedMethods("GET,POST,PUT,DELETE")
				.allowedHeaders("GET,POST,PUT,DELETE,Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				// Registro CORS de empresa
				
				registry.addMapping("business")
				.allowedOrigins(urlServer)
				.allowedMethods("GET,POST,PUT,DELETE")
				.allowedHeaders("GET,POST,PUT,DELETE,Content-Type", "Authorization", "X-Requested-With", "accept", "Origin")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				
				// Registro CORS de matrícula
				
				registry.addMapping("enrollment")
				.allowedOrigins(urlServer)
				.allowedMethods("GET,POST,PUT,DELETE")
				.allowedHeaders("GET,POST,PUT,DELETE,Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
			
				// Registro CORS de ciclo
				
				registry.addMapping("professionalDegree")
				.allowedOrigins(urlServer)
				.allowedMethods("GET,POST,PUT,DELETE")
				.allowedHeaders("GET,POST,PUT,DELETE,Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				// Registro CORS de escuela
				
				registry.addMapping("school")
				.allowedOrigins(urlServer)
				.allowedMethods("GET,POST,PUT,DELETE")
				.allowedHeaders("GET,POST,PUT,DELETE,Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				// Registro CORS de estudiante
				
				registry.addMapping("student")
				.allowedOrigins(urlServer)
				.allowedMethods("GET,POST,PUT,DELETE")
				.allowedHeaders("GET,POST,PUT,DELETE,Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				// Registro CORS de profesor
				
				registry.addMapping("teacher")
				.allowedOrigins(urlServer)
				.allowedMethods("GET,POST,PUT,DELETE")
				.allowedHeaders("GET,POST,PUT,DELETE,Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				registry.addMapping("school")
				.allowedOrigins(urlServer)
				.allowedMethods("GET,POST,PUT,DELETE")
				.allowedHeaders("GET,POST,PUT,DELETE,Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
			}
		};
	}


}
