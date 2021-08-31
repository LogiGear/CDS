package com.logigear.crm.authenticate.exception;

public class UniqueEmailException extends RuntimeException{
    private static final long serialVersionUID = -190179833578894440L;

    public UniqueEmailException(String message, Exception e) {
        super(message, e);
    }
}
