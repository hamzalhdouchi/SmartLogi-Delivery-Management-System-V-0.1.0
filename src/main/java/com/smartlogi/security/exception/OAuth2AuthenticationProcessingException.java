package com.smartlogi.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown when OAuth2 authentication processing fails.
 */
public class OAuth2AuthenticationProcessingException extends AuthenticationException {

    public OAuth2AuthenticationProcessingException(String message) {
        super(message);
    }

    public OAuth2AuthenticationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
