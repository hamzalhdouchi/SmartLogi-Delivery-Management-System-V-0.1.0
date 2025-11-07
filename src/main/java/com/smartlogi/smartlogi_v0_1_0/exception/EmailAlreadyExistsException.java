package com.smartlogi.smartlogi_v0_1_0.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("L'email '" + email + "' est déjà utilisé par un autre destinataire");
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}