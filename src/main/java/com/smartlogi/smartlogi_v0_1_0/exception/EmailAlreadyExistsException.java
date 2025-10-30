package com.smartlogi.smartlogi_v0_1_0.exception;

public class EmailAlreadyExistsException extends Exception {

    public EmailAlreadyExistsException(String email) {
        super("Cette email déga existe " + email);
    }
}
