package com.smartlogi.smartlogi_v0_1_0.exception;

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

    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }

    // Exceptions de validation spécifiques
    public static ValidationException (String email) {
        return new ValidationException(String.format("L'email %s est déjà utilisé", email));
    }

    public static ValidationException telephoneInvalide(String telephone) {
        return new ValidationException(String.format("Le numéro de téléphone %s est invalide", telephone));
    }

    public static ValidationException codePostalInvalide(String codePostal) {
        return new ValidationException(String.format("Le code postal %s est invalide", codePostal));
    }
}