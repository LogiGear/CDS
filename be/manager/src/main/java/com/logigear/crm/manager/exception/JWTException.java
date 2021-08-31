package com.logigear.crm.manager.exception;

public class JWTException extends RuntimeException {

    private static final long serialVersionUID = -190179833578894440L;

    public JWTException(String message, Exception ex) {
        super(message, ex);
    }
}
