package com.gestionMedica.main.exceptions.passwordToken;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class InvalidTokenException extends ResourceConflictException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
