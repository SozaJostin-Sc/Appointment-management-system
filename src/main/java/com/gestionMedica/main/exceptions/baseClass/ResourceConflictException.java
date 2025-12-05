package com.gestionMedica.main.exceptions.baseClass;

public abstract class ResourceConflictException extends RuntimeException {
    public ResourceConflictException(String message) {
        super(message);
    }
}
