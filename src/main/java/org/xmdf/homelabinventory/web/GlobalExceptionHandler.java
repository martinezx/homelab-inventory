package org.xmdf.homelabinventory.web;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.xmdf.homelabinventory.exception.UserAlreadyExistsException;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        String errorMessage = ex.getConstraintViolations().stream()
                .map(cv -> "field '%s' %s".formatted(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.joining(", "));

        return handleException(ex, HttpStatus.BAD_REQUEST, errorMessage, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> "field '%s' %s".formatted(err.getField(), err.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        return handleException(ex, HttpStatus.BAD_REQUEST, errorMessage, request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex, WebRequest request) {

        return handleException(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        return handleException(ex, HttpStatus.FORBIDDEN, request);
    }

    private ResponseEntity<Object> handleException(Exception ex, HttpStatus status, WebRequest request) {
        return handleException(ex, status, ex.getMessage(), request);
    }

    private ResponseEntity<Object> handleException(Exception ex, HttpStatus status, String errorMessage, WebRequest request) {
        ProblemDetail problemDetail = createProblemDetail(ex, status, errorMessage,
                null, null, request);

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), status, request);
    }
}