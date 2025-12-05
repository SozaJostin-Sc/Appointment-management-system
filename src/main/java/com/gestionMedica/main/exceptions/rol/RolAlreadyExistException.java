package com.gestionMedica.main.exceptions.rol;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class RolAlreadyExistException extends ResourceConflictException {
    public RolAlreadyExistException(String message) {
        super(message);
    }
}
