package com.gestionpvc.applications.Exceptions;

public class CustomUnauthorizedException extends RuntimeException{
    public CustomUnauthorizedException(String message) {
        super(message);
    }
}
