package com.logigear.crm.authenticate.exception;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.logigear.crm.authenticate.util.MessageUtil;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Value("${reflectoring.trace:false}")
	private boolean printStackTrace;


	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorResponse> customException(AppException ex, WebRequest request) {
		return buildErrorResponse(ex, MessageUtil.getMessage("MSG_INTERNAL_ERROR", ""), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorResponse> noSuchElementFoundException(NoSuchElementException ex, WebRequest request) {
		return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NOT_FOUND", ""), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> unauthorizedException(AccessDeniedException ex, WebRequest request) {
		return buildErrorResponse(ex, MessageUtil.getMessage("MSG_RESOURCE_NO_PERMISSION", ""), HttpStatus.UNAUTHORIZED, request);
	}

	@ExceptionHandler(UniqueEmailException.class)
	public ResponseEntity<ErrorResponse> uniqueEmailException(UniqueEmailException ex, WebRequest request) {
		return buildErrorResponse(ex, MessageUtil.getMessage("MSG_INTERNAL_ERROR", ""), HttpStatus.BAD_REQUEST, request);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus,
			WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), exception.getMessage());

		if (printStackTrace) {
			errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
		}
		return ResponseEntity.status(httpStatus).body(errorResponse);
	}
}
