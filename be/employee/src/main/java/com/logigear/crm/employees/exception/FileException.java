package com.logigear.crm.employees.exception;

public class FileException extends RuntimeException {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -4676239540720602289L;

	public FileException(String message) {
	        super(message);
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}
}
