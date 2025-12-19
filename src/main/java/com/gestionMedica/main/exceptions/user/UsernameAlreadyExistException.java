package com.gestionMedica.main.exceptions.user;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class UsernameAlreadyExistException extends ResourceConflictException {
    public UsernameAlreadyExistException(String message) {
        super(message);
    }
}
