package com.gestionMedica.main.exceptions.user;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class UserAlreadyActiveException extends ResourceConflictException {
    public UserAlreadyActiveException(String message) {
        super(message);
    }
}
