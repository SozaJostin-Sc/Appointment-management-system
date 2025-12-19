package com.gestionMedica.main.exceptions.specialization;

import com.gestionMedica.main.exceptions.baseClass.ResourceNotFoundException;

public class SpecializationIdNotFoundException extends ResourceNotFoundException {
    public SpecializationIdNotFoundException(String message) {
        super(message);
    }
}
