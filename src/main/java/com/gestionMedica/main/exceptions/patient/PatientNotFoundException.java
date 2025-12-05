package com.gestionMedica.main.exceptions.patient;

import com.gestionMedica.main.exceptions.baseClass.ResourceNotFoundException;

public class PatientNotFoundException extends ResourceNotFoundException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
