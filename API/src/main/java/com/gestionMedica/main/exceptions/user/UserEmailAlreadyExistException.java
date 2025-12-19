package com.gestionMedica.main.exceptions.user;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class UserEmailAlreadyExistException extends ResourceConflictException {
    public UserEmailAlreadyExistException(String message) {
        super(message);
    }
}
