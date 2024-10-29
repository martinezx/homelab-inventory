package org.xmdf.homelabinventory.web;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        String errorMessage = ex.getConstraintViolations().stream()
                .map(cv -> "field '%s' %s".formatted(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.joining(", "));

        ProblemDetail problemDetail = createProblemDetail(ex, HttpStatus.BAD_REQUEST, errorMessage,
                null, null, request);

        return handleExceptionInternal(ex, problemDetail,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
