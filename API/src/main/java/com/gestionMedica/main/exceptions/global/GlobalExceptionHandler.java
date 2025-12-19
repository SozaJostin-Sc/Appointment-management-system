package com.gestionMedica.main.exceptions.global;

import com.gestionMedica.main.DTO.error.ErrorDetails;
import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;
import com.gestionMedica.main.exceptions.baseClass.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorDetails> createErrorDetails(String message, HttpStatus status) {
        ErrorDetails details = new ErrorDetails(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );

        return new ResponseEntity<>(details, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(MethodArgumentNotValidException ignoredException) {
        String message = "The request could not be processed due to invalid input data.";
        return createErrorDetails(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorDetails> handleAlreadyExistException(
            ResourceConflictException ex) {
        return createErrorDetails(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFoundExceptions(
            ResourceNotFoundException ex) {
        return createErrorDetails(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DTOEmptyException.class)
    public ResponseEntity<ErrorDetails> handleDtoEmptyExceptions(DTOEmptyException ex) {
        return createErrorDetails(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ignoredException) {
        String message = "Unexpected error.";
        return createErrorDetails(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleJsonParseException(HttpMessageNotReadableException ignoredException) {
        String message = "The request body is malformed or contains invalid data types.";
        return createErrorDetails(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDetails> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = String.format("Required parameter '%s' is missing.", ex.getParameterName());
        return createErrorDetails(message, HttpStatus.BAD_REQUEST);
    }

    // MANEJA 405 METHOD NOT ALLOWED
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDetails> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String message = String.format("Method '%s' is not supported for this endpoint. Supported methods: %s",
                ex.getMethod(), ex.getSupportedHttpMethods());
        return createErrorDetails(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDenied(AccessDeniedException ignoredException) {
        String message = "You do not have the necessary permissions to perform this action.";
        return createErrorDetails(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorDetails> handleMissingPathVariable(MissingPathVariableException ex) {
        String message = String.format("Required path variable '%s' is missing.", ex.getVariableName());
        return createErrorDetails(message, HttpStatus.BAD_REQUEST);
    }
}