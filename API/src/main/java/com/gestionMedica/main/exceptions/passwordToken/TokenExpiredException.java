package com.gestionMedica.main.exceptions.passwordToken;

import com.gestionMedica.main.exceptions.baseClass.ResourceConflictException;

public class TokenExpiredException extends ResourceConflictException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
