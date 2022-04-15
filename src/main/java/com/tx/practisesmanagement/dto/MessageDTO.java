package com.tx.practisesmanagement.dto;



import java.io.Serializable;
import java.util.Objects;
/**
 * DTO de mensaje, permite emitir un mensaje
 * @author Salva
 */
public class MessageDTO implements Serializable {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;				// Mensaje

	/**
	 * Constructor de mensaje
	 * @param message
	 */
	public MessageDTO(String message) {
		super();
		this.message = message;
	}

	/**
	 * Obtiene el mensaje
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Establece el mensaje
	 * @param message: Mensaje a establecer
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Informaciónd del mensaje
	 */
	@Override
	public String toString() {
		return "WelcomeDTO [message=" + message + "]";
	}

	/**
	 * HashCode de mensaje
	 */
	@Override
	public int hashCode() {
		return Objects.hash(message);
	}

	/**
	 * Equals de mensaje
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageDTO other = (MessageDTO) obj;
		return Objects.equals(message, other.message);
	}
}
