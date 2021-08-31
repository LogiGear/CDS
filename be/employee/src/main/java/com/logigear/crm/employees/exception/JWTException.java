package com.logigear.crm.employees.exception;

public class JWTException extends RuntimeException {

    private static final long serialVersionUID = -190179833578894440L;

    public JWTException(String message, Exception ex) {
        super(message, ex);
    }
}
