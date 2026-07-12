package com.gestionpvc.applications.Exceptions;

public class AuthenticationException extends RuntimeException{
    
    public AuthenticationException(String message) {
        super(message);
    }
}
