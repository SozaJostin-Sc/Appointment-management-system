package com.gestionMedica.main.exceptions.specialization;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class SpecializationAlreadyExistException extends ResourceConflictException {
    public SpecializationAlreadyExistException(String message) {
        super(message);
    }
}
