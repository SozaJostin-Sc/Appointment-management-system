package com.gestionMedica.main.exceptions.specialization;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class StatusAlreadyActiveException extends ResourceConflictException {
    public StatusAlreadyActiveException(String message) {
        super(message);
    }
}
