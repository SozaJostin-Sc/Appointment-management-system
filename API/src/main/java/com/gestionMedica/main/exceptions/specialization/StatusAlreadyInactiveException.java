package com.gestionMedica.main.exceptions.specialization;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class StatusAlreadyInactiveException extends ResourceConflictException {
    public StatusAlreadyInactiveException(String message) {
        super(message);
    }
}
