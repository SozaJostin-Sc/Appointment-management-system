package com.gestionMedica.main.exceptions.user;

import com.gestionMedica.main.exceptions.baseClass.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
