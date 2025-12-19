package com.gestionMedica.main.exceptions.rol;

import com.gestionMedica.main.exceptions.baseClass.ResourceNotFoundException;

public class RolNotFoundException extends ResourceNotFoundException {
    public RolNotFoundException(String message) {
        super(message);
    }
}
