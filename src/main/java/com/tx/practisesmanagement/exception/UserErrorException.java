package com.tx.practisesmanagement.exception;


/**
 * Excepción generada por alguna acción del usuario
 * @author Salvador
 */
public class UserErrorException extends RuntimeException {

	private static final long serialVersionUID = -1593604610888645082L;

	/**
	 * Contructor vacío
	 */
	public UserErrorException() {
		super();
	}
	
	/**
	 * Constructor con parámtros
	 * @param message: Mensaje de error
	 */
	public UserErrorException(String message) {
		super(message);
	}

	/**
	 * Obtiene el serial
	 * @return: Serial
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
