package com.gestionMedica.main.exceptions.doctor;

import com.gestionMedica.main.exceptions.baseClass.ResourceNotFoundException;

public class DoctorNotFoundException extends ResourceNotFoundException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
}
