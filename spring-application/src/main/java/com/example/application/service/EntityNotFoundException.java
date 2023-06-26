package com.example.application.service;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
