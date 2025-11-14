package com.smartlogi.smartlogiv010.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DatabaseException contrainteViolation(String contrainte) {
        return new DatabaseException(String.format(
                "Violation de contrainte : %s", contrainte));
    }

    public static DatabaseException connexionPerdue() {
        return new DatabaseException("Connexion à la base de données perdue");
    }
}