package com.gestionMedica.main.exceptions.global;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class DTOEmptyException extends ResourceConflictException {
    public DTOEmptyException(String message) {
        super(message);
    }
}
