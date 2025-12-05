package com.gestionMedica.main.exceptions.baseClass;

public abstract class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
