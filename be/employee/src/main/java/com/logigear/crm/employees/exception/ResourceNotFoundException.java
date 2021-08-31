package com.logigear.crm.employees.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -190179833578894442L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

}