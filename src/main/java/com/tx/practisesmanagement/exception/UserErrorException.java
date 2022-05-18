package com.tx.practisesmanagement.exception;

public class UserErrorException extends RuntimeException {

	private static final long serialVersionUID = -1593604610888645082L;

	public UserErrorException() {
		super();
	}
	
	public UserErrorException(String message) {
		super(message);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
