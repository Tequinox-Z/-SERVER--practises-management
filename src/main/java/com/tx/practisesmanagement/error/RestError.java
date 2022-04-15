package com.tx.practisesmanagement.error;



import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Indica un error ocurrido
 * @author Salva
 */
public class RestError implements Serializable {

	private static final long serialVersionUID = 1L;
	private HttpStatus estado;													// Código de estado
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
	private LocalDateTime fecha;												// Fecha de la excepción
	private String mensaje;														// Mensaje
	
	
	/**
	 * Constructor con parámetros
	 * @param estado
	 * @param mensaje
	 */
	public RestError(HttpStatus estado, String mensaje) {
		super();
		this.estado = estado;
		this.fecha = LocalDateTime.now();
		this.mensaje = mensaje;
	}
	
	/**
	 * Obtiene el código de estado
	 * @return Código de estado
	 */
	public HttpStatus getEstado() {
		return estado;
	}
	
	/**
	 * Obtiene la fecha
	 * @return Fecha 
	 */
	public LocalDateTime getFecha() {
		return fecha;
	}
	
	/**
	 * Obtiene el mensaje
	 * @return Mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}
}
