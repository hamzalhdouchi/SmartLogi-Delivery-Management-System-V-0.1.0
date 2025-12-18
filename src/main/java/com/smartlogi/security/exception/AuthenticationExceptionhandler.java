package com.smartlogi.security.exception;

public class AuthenticationExceptionhandler extends RuntimeException {
    public AuthenticationExceptionhandler(String message) {
        super(message);
    }
}
