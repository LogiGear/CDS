package com.logigear.crm.admins.aspect;

import com.logigear.crm.admins.exception.JWTException;
import com.logigear.crm.admins.exception.ResourceNotFoundException;
import com.logigear.crm.admins.response.ErrorResponse;
import com.logigear.crm.admins.util.MessageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Support catch exception right from the controller
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Should exception stack trace be printed?
     **/
    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    /**
     * This method supports for handling Resource Not Found Exception.
     *
     * @author hung.tran
     * @param ex The Resource Not Found Exception in which occurred.
     * @param request The Web Request of the HTTP Request
     * @return The ResponseEntity where the ErrorResponse is wrapped.
     **/
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""), HttpStatus.NOT_FOUND, request);
    }

    /**
     * This method supports for handling custom JWT Exception.
     *
     * @author hung.tran
     * @param ex The custom JWT Exception in which occurred.
     * @param request The Web Request of the HTTP Request
     * @return The ResponseEntity where the ErrorResponse is wrapped.
     **/
    @ExceptionHandler(JWTException.class)
    public ResponseEntity<ErrorResponse> customException(JWTException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_BAD_REQUEST", ""), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * This method supports for handling no such element found exception.
     *
     * @author hung.tran
     * @param ex The No Such Element Found Exception in which occurred.
     * @param request The Web Request of the HTTP Request
     * @return The ResponseEntity where the ErrorResponse is wrapped.
     **/
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> noSuchElementFoundException(NoSuchElementException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""), HttpStatus.NOT_FOUND, request);
    }

    /**
     * This method supports for handling the unauthorized exception.
     *
     * @author hung.tran
     * @param ex The Access Denied Exception in which occurred.
     * @param request The Web Request of the HTTP Request
     * @return The ResponseEntity where the ErrorResponse is wrapped.
     **/
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> unauthorizedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NO_PERMISSION", ""), HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * This method supports for building the error response object to the client.
     *
     * @author hung.tran
     * @param exception The exception in which occurred.
     * @param message The message which has the detail of the exception
     * @param httpStatus The HTTP Status of the HTTP Response
     * @param request The Web Request of the HTTP Request
     * @return The ResponseEntity where the ErrorResponse is wrapped.
     **/
    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus,
                                                             WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        if (printStackTrace) {
            errorResponse.setStackTrace(Arrays.toString(exception.getStackTrace()));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
