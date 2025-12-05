package com.gestionMedica.main.exceptions.appointment;

import com.gestionMedica.main.exceptions.baseClass.ResourceNotFoundException;

public class AppointmentNotFoundException extends ResourceNotFoundException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
