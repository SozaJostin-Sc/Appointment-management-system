package com.gestionMedica.main.exceptions.user;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class UserAlreadyInactiveException extends ResourceConflictException {
    public UserAlreadyInactiveException(String message) {
        super(message);
    }
}
