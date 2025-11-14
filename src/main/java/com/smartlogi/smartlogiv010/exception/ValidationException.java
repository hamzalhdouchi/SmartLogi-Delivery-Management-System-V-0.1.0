package com.smartlogi.smartlogiv010.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;
import jakarta.validation.ConstraintViolation;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    private Set<? extends ConstraintViolation<?>> violations;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        this.violations = violations;
    }

}